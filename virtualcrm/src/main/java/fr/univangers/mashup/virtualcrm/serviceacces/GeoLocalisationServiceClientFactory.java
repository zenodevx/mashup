package fr.univangers.mashup.virtualcrm.serviceacces;

public class GeoLocalisationServiceClientFactory {
    private static final GeoLocalisationServiceClient geoLocalisationServiceClient = new NominatimClient();

    public static GeoLocalisationServiceClient getGeoLocalisationServiceClient() {
        return geoLocalisationServiceClient;
    }
}
