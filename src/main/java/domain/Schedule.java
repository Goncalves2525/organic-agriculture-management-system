package domain;

public class Schedule {

    private int openHour;
    private int closeHour;

    public Schedule(int openHour, int closeHour) {
        this.openHour = openHour;
        this.closeHour = closeHour;
    }

    public int getOpenHour() {
        return openHour;
    }

    public int getCloseHour() {
        return closeHour;
    }

    @Override
    public String toString() {
        return String.format("%d:00 to %d:00", openHour, closeHour);
    }
}
