package domain;

public class Location{
    public final String code;
    public final int numberOfEmployees;
    public final Coordinate coordinate;
    public Schedule schedule;
    private boolean isHub = false;

    private int nrOfEmployees;
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


    public int getCHARGING_TIME() {
        return CHARGING_TIME;
    }

    public int getDEPLOY_TIME() {
        return DEPLOY_TIME;
    }

    public boolean isHub() {
        return isHub;
    }

    public void setHub(boolean isHub) {
        this.isHub = isHub;
    }

    public void setNrOfEmployees(int nrOfEmployees){this.nrOfEmployees = nrOfEmployees;}

    public int getNrOfEmployees() {
        return nrOfEmployees;
    }

    @Override
    public String toString() {
        return this.code;
    }


    // TODO: method to convert km to m and other to convert km/h to m/min on UTILS (add other conversions).
}
