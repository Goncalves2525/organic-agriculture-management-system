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
        return String.format("(%d, %d)", openHour, closeHour);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Schedule))
            return false;
        if (obj == this)
            return true;
        return this.getOpenHour() == ((Schedule) obj).getOpenHour()
                && this.getCloseHour() == ((Schedule) obj).getCloseHour();
    }
}
