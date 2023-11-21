package graphs;

import domain.Coordinate;
import domain.Schedule;
import graphs.matrix.MatrixGraph;

import java.util.Objects;

public class GFH {

    private static class Location {

        public final String code;
        public final int numberOfEmployees;
        public final Coordinate coordinate;
        public Schedule schedule;

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

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof Location)) return false;
            return code.equals(((Location) other).code);
        }

        @Override
        public int hashCode() {
            return Objects.hash(code, numberOfEmployees, coordinate);
        }
    }

    private static class Distance implements Comparable {

        public final Location origLocation;
        public final Location destLocation;
        public final int distance;

        public Distance(Location origLocation, Location destLocation, int distance) {
            this.origLocation = origLocation;
            this.destLocation = destLocation;
            this.distance = distance;
        }

        @Override
        public int hashCode() {
            return Objects.hash(origLocation, origLocation, distance);
        }

        @Override
        public int compareTo(Object other) {
            if (this.distance < ((Distance) other).distance) {
                return -1;
            } else if (this.distance > ((Distance) other).distance) {
                return 1;
            }
            return 0;
        }
    }

    private final Graph<Location, Distance> gfhMatrix = new MatrixGraph<>(false);

    public GFH() {}

    public Graph<Location, Distance> getGfhMatrix() {
        return gfhMatrix;
    }

    /**
     * Add location to the MatrixGraph
     *
     * @param code
     * @param numberOfEmployees
     * @param latitude
     * @param longitude
     * @return true if added to the matrix and false if not
     */
    public boolean insertLocation(String code, int numberOfEmployees, double latitude, double longitude, int openHour, int closeHour) {
        return gfhMatrix.addVertex(new Location(code, numberOfEmployees, latitude, longitude, openHour, closeHour));
    }

    /**
     * Adds distance between two valid locations.
     *
     * @param vOrigCode
     * @param vDestCode
     * @param distance
     * @return true if distance was added and false if not.
     */
    public boolean insertDistance(String vOrigCode, String vDestCode, int distance) {

        Location vOrig = null;
        Location vDest = null;

        for (Location location : gfhMatrix.vertices()) {
            if (location.getCode().equals(vOrigCode)) {
                vOrig = location;
            }
            if (location.getCode().equals(vDestCode)) {
                vDest = location;
            }
        }

        Distance locDistance = new Distance(vOrig, vDest, distance);

        return gfhMatrix.addEdge(vOrig, vDest, locDistance);
    }

    @Override
    public String toString() {
        return gfhMatrix.toString();
    }

    // TODO: impletementar exercicios abaixo

    // TODO: aqui ou na classe Algorithms? ou na MatrixGraph?

    /**
     * Minimum spanning tree - PRIM Algorithm
     *
     * @return minimum spanning tree
     */
    public static MatrixGraph<Integer, Double> mstPRIM(Graph<Integer, Double> g) {

        throw new UnsupportedOperationException("Not supported yet.");
    }

}
