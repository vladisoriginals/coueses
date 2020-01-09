package android.example.quakereport.data;

public class Earthquake {
    private String mag;
    private String place;
    private String locationOffset;
    private String data;
    private String time;
    private String urlDetails;

    public Earthquake(String mag, String place, String locationOffset,
                      String data, String time, String urlDetails) {
        this.mag = mag;
        this.place = place;
        this.locationOffset = locationOffset;
        this.data = data;
        this.time = time;
        this.urlDetails = urlDetails;
    }

    public String getMag() {
        return mag;
    }

    public String getPlace() {
        return place;
    }

    public String getLocationOffset() {
        return locationOffset;
    }

    public String getData() {
        return data;
    }

    public String getTime() {
        return time;
    }

    public String getUrlDetails() {
        return urlDetails;
    }
}
