package fr.univangers.mashup.virtualcrm.serviceacces;

import fr.univangers.mashup.virtualcrm.dto.GeographicPointDto;
import fr.univangers.mashup.virtualcrm.dto.VirtualLeadDto;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpStatus;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static java.lang.System.Logger.Level.*;
import static java.text.MessageFormat.format;

public class NominatimClient implements GeoLocalisationServiceClient {
    private static final System.Logger logger = System.getLogger(NominatimClient.class.getSimpleName());
    public static final String NOMINATIM_ENDPOINT_ADDRESS = "https://nominatim.openstreetmap.org/search?";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public GeographicPointDto getGeographicPointFromLead(VirtualLeadDto lead) {
        logger.log(INFO, "Fetching coordinates for: {0}, {1}", lead.getStreet(), lead.getCity());

        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request
                    .get(NOMINATIM_ENDPOINT_ADDRESS + format("city={0}&country={1}&postalcode={2}&street={3}&format=json&limit=1",
                            URLEncoder.encode(lead.getCity(), StandardCharsets.UTF_8),
                            URLEncoder.encode(lead.getCountry(), StandardCharsets.UTF_8),
                            URLEncoder.encode(lead.getPostalCode(), StandardCharsets.UTF_8),
                            URLEncoder.encode(lead.getStreet(), StandardCharsets.UTF_8)))
                    .execute()
                    .returnResponse();
            if (response.getCode() == HttpStatus.SC_OK) {
                JsonNode root = objectMapper.readTree(response.getEntity().getContent());

                if (root.isArray() && !root.isEmpty()) {
                    JsonNode result = root.get(0);
                    double latitude = result.get("lat").asDouble();
                    double longitude = result.get("lon").asDouble();

                    logger.log(DEBUG, "Coordinates found: lat={0}, lon={1}", latitude, longitude);
                    return new GeographicPointDto(latitude, longitude);
                }
            } else {
                logger.log(ERROR, "API error: received status code {0}", response.getCode());
            }
        } catch (IOException e) {
            logger.log(ERROR, "Network error while calling Nominatim API: " + e);
            throw new RuntimeException(e);
        }
        return null;
    }
}
