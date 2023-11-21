package domain;

public class Coordinate {

    private double latitude;
    private double longitude;

    /**
     * Constructor
     * @param latitude
     * @param longitude
     */
    public Coordinate(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude(){
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return String.format("(%f, %f)", latitude, longitude);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Coordinate))
            return false;
        if (obj == this)
            return true;
        return this.getLatitude() == ((Coordinate) obj).getLatitude()
                && this.getLongitude() == ((Coordinate) obj).getLongitude();
    }

}
