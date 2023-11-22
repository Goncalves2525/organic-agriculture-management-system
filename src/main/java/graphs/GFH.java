package graphs;

import domain.Location;
import graphs.map.MapGraph;

public class GFH {

    private final Graph<Location, Integer> gfh;

    public GFH() {
        gfh = new MapGraph<>(false);
    }

    public Graph<Location, Integer> getGfh() {
        return new MapGraph<>(gfh);
    }

    /**
     * Add location to the Graph
     *
     * @param location
     * @return true if added to the matrix and false if not
     */
    public boolean addLocation(Location location) {
        return gfh.addVertex(location);
    }

    /**
     * Adds distance between two valid locations.
     *
     * @param locA
     * @param locB
     * @param distance
     * @return true if distance was added and false if not.
     */
    public boolean addDistance(String locA, String locB, Integer distance) {

        Location vOrig = null;
        Location vDest = null;

        for (Location location : gfh.vertices()) {
            if (location.getCode().equals(locA)) {
                vOrig = location;
            }
            if (location.getCode().equals(locB)) {
                vDest = location;
            }
        }

        return gfh.addEdge(vOrig, vDest, distance);
    }

    @Override
    public String toString() {
        return gfh.toString();
    }

    // TODO: impletementar exercicios abaixo

    // TODO: aqui ou na classe Algorithms? ou na MatrixGraph?



}
