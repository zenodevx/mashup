package fr.univangers.mashup.virtualcrm.dto;

@SuppressWarnings("unused")
public class GeographicPointDto {
    private double latitude;
    private double longitude;

    public GeographicPointDto() {
    }

    public GeographicPointDto(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
