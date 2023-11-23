package domain;

public class Distance implements Comparable<Distance>{

    public final Location origLocation;
    public final Location destLocation;
    public final int distance;

    public Distance(Location origLocation, Location destLocation, int distance) {
        this.origLocation = origLocation;
        this.destLocation = destLocation;
        this.distance = distance;
    }

    public Location getOrigLocation() {
        return origLocation;
    }

    public Location getDestLocation() {
        return destLocation;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public int compareTo(Distance o) {
        if (this.distance > o.distance) {
            return 1;
        } else if (this.distance < o.distance) {
            return -1;
        } else {
            return 0;
        }
    }
}
