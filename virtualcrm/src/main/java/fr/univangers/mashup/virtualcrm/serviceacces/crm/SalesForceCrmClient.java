package fr.univangers.mashup.virtualcrm.serviceacces.crm;

import fr.univangers.mashup.internalcrm.thrift.InvalidDateFormatException;
import fr.univangers.mashup.virtualcrm.dto.VirtualLeadDto;
import fr.univangers.mashup.virtualcrm.utils.DateUtils;
import org.apache.hc.client5.http.fluent.Form;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpStatus;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.*;

import static fr.univangers.mashup.virtualcrm.utils.DateUtils.formatSfDateToApp;
import static java.lang.System.Logger.Level.*;
import static java.text.MessageFormat.format;

public class SalesForceCrmClient {
    private static final System.Logger logger = System.getLogger(SalesForceCrmClient.class.getSimpleName());
    private static final String SALESFORCE_PROPERTIES = "salesforce.properties";
    private static final Properties properties = new Properties();
    private final static ObjectMapper objectMapper = new ObjectMapper();

    private final String clientId;
    private final String clientSecret;
    private final String username;
    private final String password;
    private final String apiVersion;

    private String accessToken;
    private String instanceUrl;
    private String tokenType;

    static {
        try (InputStream propertiesStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(SALESFORCE_PROPERTIES)) {
            if (propertiesStream == null) {
                throw new IOException("Error: Failed to load '" + SALESFORCE_PROPERTIES + "': File not found in classpath.");
            }
            properties.load(propertiesStream);
            logger.log(INFO, "Salesforce properties loaded successfully.");
        } catch (IOException e) {
            logger.log(ERROR, "Failed to load Salesforce configuration", e);
            throw new RuntimeException("Error: Failed to load properties from '" + SALESFORCE_PROPERTIES + "': " + e.getMessage(), e);
        }
    }

    public SalesForceCrmClient() {
        try {
            clientId = Objects.requireNonNull(properties.getProperty("CLIENT_ID"), "Configuration error: CLIENT_ID is missing");
            clientSecret = Objects.requireNonNull(properties.getProperty("CLIENT_SECRET"), "Configuration error: CLIENT_SECRET is missing");
            username = Objects.requireNonNull(properties.getProperty("USERNAME"), "Configuration error: USERNAME is missing");
            password = Objects.requireNonNull(properties.getProperty("PASSWORD"), "Configuration error: PASSWORD is missing");
            apiVersion = Objects.requireNonNull(properties.getProperty("API_VERSION"), "Configuration error: API_VERSION is missing");
        } catch (NullPointerException e) {
            logger.log(ERROR, "Missing required Salesforce configuration properties");
            throw new RuntimeException("Critical configuration error: " + e.getMessage(), e);
        }
    }

    private void loginToSalesforce() {
        logger.log(INFO, "Attempting login to Salesforce");
        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request.post("https://login.salesforce.com/services/oauth2/token")
                    .bodyForm(Form.form()
                            .add("grant_type", "password")
                            .add("client_id", clientId)
                            .add("client_secret", clientSecret)
                            .add("username", username)
                            .add("password", password)
                            .build())
                    .execute()
                    .returnResponse();

            if (response.getCode() == HttpStatus.SC_OK) {
                JsonNode root = objectMapper.readTree(response.getEntity().getContent());

                if (root != null && !root.isEmpty()) {
                    accessToken = root.get("access_token").asString();
                    instanceUrl = root.get("instance_url").asString();
                    tokenType = root.get("token_type").asString();
                    logger.log(DEBUG, "Successfully logged in to Salesforce. Instance URL: {0}", instanceUrl);
                }
            } else {
                logger.log(ERROR, "Salesforce login failed with status code: {0}", response.getCode());
            }
        } catch (IOException e) {
            logger.log(ERROR, "Connection error during Salesforce login", e);
            throw new RuntimeException(e);
        }
    }

    public List<VirtualLeadDto> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state) {
        logger.log(INFO, "Querying Salesforce leads by revenue: {0} - {1} in state: {2}", lowAnnualRevenue, highAnnualRevenue, state);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setGroupingSeparator('\u0000');

        DecimalFormat df = new DecimalFormat("0.#####", symbols);
        df.setGroupingUsed(false);

        String low = df.format(lowAnnualRevenue);
        String high = df.format(highAnnualRevenue);

        return executeQuery(format("""
                SELECT FirstName,LastName,AnnualRevenue,Phone,Street,PostalCode,City,Country,CreatedDate,Company,State
                FROM Lead
                WHERE AnnualRevenue >= {0} AND AnnualRevenue <= {1} AND State = ''{2}''
                """, low, high, state));
    }

    public List<VirtualLeadDto> findLeadsByDate(String startDate, String endDate) {
        try {
            logger.log(INFO, "Querying Salesforce leads by date: {0} to {1}", startDate, endDate);
            String startDateSoql = DateUtils.toStartOfDaySoql(startDate);
            String endDateSoql = DateUtils.toEndOfDaySoql(endDate);
            return executeQuery(format("""
                    SELECT FirstName,LastName,AnnualRevenue,Phone,Street,PostalCode,City,Country,CreatedDate,Company,State
                    FROM Lead
                    WHERE CreatedDate >= {0} AND CreatedDate <= {1}
                    """, startDateSoql, endDateSoql));
        } catch (InvalidDateFormatException e) {
            logger.log(ERROR, "Invalid date format provided for Salesforce query: {0} / {1}", startDate, endDate);
            throw new RuntimeException(e);
        }
    }

    public List<VirtualLeadDto> retrieveAllLeads() {
        logger.log(INFO, "Querying all Salesforce leads");
        return executeQuery(format("""
                SELECT FirstName,LastName,AnnualRevenue,Phone,Street,PostalCode,City,Country,CreatedDate,Company,State
                FROM Lead
                """));
    }

    private List<VirtualLeadDto> executeQuery(String query) {
        if (accessToken == null) {
            loginToSalesforce();
        }

        try {
            Request request = Request.get(format("{0}/services/data/{1}/query/?q={2}",
                            instanceUrl,
                            apiVersion,
                            URLEncoder.encode(query, StandardCharsets.UTF_8)))
                    .addHeader("Authorization", tokenType + " " + accessToken)
                    .addHeader("Accept", "application/json");

            ClassicHttpResponse response = (ClassicHttpResponse) request.execute().returnResponse();

            if (response.getCode() != HttpStatus.SC_OK) {
                logger.log(WARNING, "Salesforce session expired, retrying login...");
                loginToSalesforce();
                response = (ClassicHttpResponse) request.execute().returnResponse();
            }

            if (response.getCode() == HttpStatus.SC_OK) {
                JsonNode root = objectMapper.readTree(response.getEntity().getContent());
                JsonNode records = root.get("records");
                int totalSize = root.get("totalSize").asInt();

                logger.log(DEBUG, "Salesforce query successful. Total records: {0}", totalSize);
                List<VirtualLeadDto> leads = new ArrayList<>(totalSize);

                if (records != null && records.isArray()) {
                    for (JsonNode node : records) {
                        VirtualLeadDto dto = mapToDto(node);
                        leads.add(dto);
                    }
                }
                return leads;
            } else {
                logger.log(ERROR, "Salesforce query failed with status code: {0}", response.getCode());
            }
        } catch (IOException e) {
            logger.log(ERROR, "Network error during Salesforce query execution", e);
            throw new RuntimeException(e);
        }
        return List.of();
    }

    private VirtualLeadDto mapToDto(JsonNode node) {
        VirtualLeadDto dto = new VirtualLeadDto();
        dto.setFirstName(node.path("FirstName").asString(null));
        dto.setLastName(node.path("LastName").asString(null));
        dto.setAnnualRevenue(node.path("AnnualRevenue").asDouble());
        dto.setPhone(node.path("Phone").asString(null));
        dto.setStreet(node.path("Street").asString(null));
        dto.setPostalCode(node.path("PostalCode").asString(null));
        dto.setCity(node.path("City").asString(null));
        dto.setCountry(node.path("Country").asString(null));
        dto.setCompany(node.path("Company").asString(null));
        dto.setState(node.path("State").asString(null));

        String rawDate = node.path("CreatedDate").asString(null);
        if (rawDate != null) {
            try {
                dto.setCreationDate(DateUtils.parse(formatSfDateToApp(rawDate)));
            } catch (ParseException | InvalidDateFormatException e) {
                logger.log(ERROR, "Failed to parse CreatedDate from Salesforce: {0}", rawDate);
            }
        }
        return dto;
    }
}
