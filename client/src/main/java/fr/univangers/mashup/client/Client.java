package fr.univangers.mashup.client;

import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static java.text.MessageFormat.format;

public class Client {
    private static final String CRM_SERVICE_ENDPOINT_ADDRESS = "http://localhost:8080/virtualcrm/";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }

        String command = args[0];

        try {
            switch (command) {
                case "findLeads":
                    if (args.length < 4) {
                        System.out.println("Missing parameters for findLeads\n");
                        printUsage();
                        return;
                    }

                    findLeads(args[1], args[2], args[3]);
                    break;

                case "findLeadsByDate":
                    if (args.length < 3) {
                        System.out.println("Missing parameters for findLeadsByDate\n");
                        printUsage();
                        return;
                    }

                    findLeadsByDate(args[1], args[2]);
                    break;

                default:
                    System.out.println("Unknown command: " + command);
                    printUsage();
                    break;
            }
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    private static void printUsage() {
        System.out.println("usage: java -jar client-all.jar <command> [options]");
        System.out.println("the commands are the following:");
        System.out.println("  findLeads lowRev highRev state");
        System.out.println("  findLeadsByDate startDate endDate");
        System.out.println("Examples:");
        System.out.println("  java -jar client-all.jar findLeads 300000000.0 400000000.0 NC");
        System.out.println("  java -jar client-all.jar findLeadsByDate 2025-10-01 2025-10-05");
    }

    private static void findLeads(String lowRev, String highRev, String state) throws IOException, ParseException {
        String url = CRM_SERVICE_ENDPOINT_ADDRESS + format("leads?low_annual_revenue={0}&high_annual_revenue={1}&state={2}",
                encode(lowRev), encode(highRev), encode(state));
        executeAndPrint(url);
    }

    private static void findLeadsByDate(String startDate, String endDate) throws IOException, ParseException {
        String url = CRM_SERVICE_ENDPOINT_ADDRESS + format("leads/date?start_date={0}&end_date={1}",
                encode(startDate), encode(endDate));
        executeAndPrint(url);
    }

    private static void executeAndPrint(String url) throws IOException, ParseException {
        ClassicHttpResponse response = (ClassicHttpResponse) Request.get(url)
                .execute()
                .returnResponse();

        if (response.getCode() == 200) {
            byte[] content = EntityUtils.toByteArray(response.getEntity());
            displayTable(content);
        } else {
            System.out.println("Server returned status code: " + response.getCode());
            System.out.println("Response body: " + EntityUtils.toString(response.getEntity()));
        }
    }

    private static void displayTable(byte[] jsonContent) {
        System.out.println();

        JsonNode root = objectMapper.readTree(jsonContent);
        if (!root.isArray() || root.isEmpty()) {
            System.out.println("No leads found.");
            return;
        }

        String rowFormat = "| %-12s | %-12s | %-20s | %-10s | %-14s | %-15s | %-8s | %-12s | %-8s | %-10s | %-10s |%n";
        String separator = "+--------------+--------------+----------------------+------------+----------------+-----------------+----------+--------------+----------+------------+------------+";

        System.out.println(separator);
        System.out.printf(rowFormat, "First Name", "Last Name", "Company", "Revenue", "Phone", "Street", "P.Code", "City", "Country", "State", "Created");
        System.out.println(separator);

        for (JsonNode lead : root) {
            System.out.printf(rowFormat,
                    truncate(getString(lead, "firstName"), 12),
                    truncate(getString(lead, "lastName"), 12),
                    truncate(getString(lead, "company"), 20),
                    String.format("%.0f", lead.get("annualRevenue").asDouble()),
                    truncate(getString(lead, "phone"), 14),
                    truncate(getString(lead, "street"), 15),
                    truncate(getString(lead, "postalCode"), 8),
                    truncate(getString(lead, "city"), 12),
                    truncate(getString(lead, "country"), 8),
                    truncate(getString(lead, "state"), 10),
                    truncate(getString(lead, "creationDate").substring(0, 10), 10)
            );
        }
        System.out.println(separator);
        System.out.println("Total leads: " + root.size() + "\n");
    }

    private static String getString(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return (value == null || value.isNull()) ? "" : value.asString();
    }

    private static String truncate(String value, int length) {
        if (value == null) return "";
        return (value.length() <= length) ? value : value.substring(0, length - 3) + "...";
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
