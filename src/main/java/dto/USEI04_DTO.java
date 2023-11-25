package dto;

import domain.Location;

import java.util.LinkedList;

public class USEI04_DTO {

    private final boolean hasHamiltonianPath;
    private final LinkedList<Location> path;
    private final int pathWeight;

    public USEI04_DTO(boolean hasHamiltonianPath, LinkedList<Location> path, int pathWeight) {
        this.hasHamiltonianPath = hasHamiltonianPath;
        this.path = path;
        this.pathWeight = pathWeight;
    }

    public boolean hasHamiltonianPath() {
        return hasHamiltonianPath;
    }

    public LinkedList<Location> getPath() {
        return path;
    }

    public int getPathWeight() {
        return pathWeight;
    }
}
