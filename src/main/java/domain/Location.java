package domain;

public class Location {

    public final String code;
    public final int numberOfEmployees;
    public final Coordinate coordinate;
    public Schedule schedule;
    public int AUTONOMY = 100; // km
    public int AVG_VELOCITY = 50; // km/h
    public int CHARGING_TIME = 60; // min
    public int DEPLOY_TIME = 10; // min

    public Location(String code, int numberOfEmployees, double latitude, double longitude, int openHour, int closeHour) {
        this.code = code;
        this.numberOfEmployees = numberOfEmployees;
        this.coordinate = new Coordinate(latitude, longitude);
        this.schedule = new Schedule(openHour, closeHour);
    }

    public String getCode() {
        return code;
    }

    public int getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public int getAUTONOMY() {
        return AUTONOMY;
    }

    public int getAVG_VELOCITY() {
        return AVG_VELOCITY;
    }

    public int getCHARGING_TIME() {
        return CHARGING_TIME;
    }

    public int getDEPLOY_TIME() {
        return DEPLOY_TIME;
    }

    @Override
    public String toString() {
        return this.code;
    }

    // TODO: method to convert km to m and other to convert km/h to m/min on UTILS (add other conversions).
}
