package org.me.gcu.equakestartercode;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ted Hilton - S1708998
 */
public class Earthquake {

    private String location;
    private int depth;
    private double magnitude;
    private Date date;
    private Double geoLat;
    private Double geoLong;

    public void setDescription(String description) {
        String[] parts = description.split(";");
        if(parts.length != 1) {
            this.depth = extractDepth(parts[3]);
            this.magnitude = extractMagnitude(parts[4]);
            this.location = extractLocation(parts[1]).toLowerCase();
        }
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setGeoLat(Double geoLat) {
        this.geoLat = geoLat;
    }

    public void setGeoLong(Double geoLong) {
        this.geoLong = geoLong;
    }

    public Double getGeoLat() {
        return geoLat;
    }

    public Double getGeoLong() {
        return geoLong;
    }

    public Date getDate() {
        return date;
    }

    public int getDepth() {
        return depth;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public String getLocation() {
        return location;
    }

    private int extractDepth(String depth) {
        String str = depth.replaceAll("\\D+","");
        return Integer.parseInt(str);
    }

    private double extractMagnitude(String magnitude) {
        Matcher m = Pattern.compile("(?!=\\d\\.\\d\\.)([\\d.]+)").matcher(magnitude);
        double doubleMagnitude = 0;
        while (m.find()) {
            doubleMagnitude = Double.parseDouble(m.group(1));
        }
        return doubleMagnitude;
    }

    private String extractLocation(String location) {
        return location.substring(11);
    }

}
