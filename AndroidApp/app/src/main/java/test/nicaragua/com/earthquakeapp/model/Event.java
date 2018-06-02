package test.nicaragua.com.earthquakeapp.model;

/**
 * Created by macbook_prueba on 6/1/18.
 */

public class Event {
    private String Id;
    private String Type;
    private Double Magnitude;
    private String Direction;
    private Long Time;
    private Double Latitude;
    private Double Longitude;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public Double getMagnitude() {
        return Magnitude;
    }

    public void setMagnitude(Double magnitude) {
        Magnitude = magnitude;
    }

    public String getDirection() {
        return Direction;
    }

    public void setDirection(String direction) {
        Direction = direction;
    }

    public Long getTime() {
        return Time;
    }

    public void setTime(Long time) {
        Time = time;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }
}
