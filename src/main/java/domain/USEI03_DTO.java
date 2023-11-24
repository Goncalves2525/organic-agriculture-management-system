package domain;

import java.util.ArrayList;
import java.util.LinkedList;

public class USEI03_DTO {
    private boolean tripIsPossible;
    private LinkedList<Location> shortPath;
    private ArrayList<Location> chargeLocations;
    private Integer pathLength;
    int numberOfCharges;
    int autonomiaMax;

    public USEI03_DTO(boolean tripIsPossible, LinkedList<Location> shortPath, ArrayList<Location> chargeLocations, Integer pathLength, int numberOfCharges, int autonomiaMax) {
        this.tripIsPossible = tripIsPossible;
        this.shortPath = shortPath;
        this.chargeLocations = chargeLocations;
        this.pathLength = pathLength;
        this.numberOfCharges = numberOfCharges;
        this.autonomiaMax = autonomiaMax;
    }

    public boolean isTripIsPossible() {
        return tripIsPossible;
    }

    public void setTripIsPossible(boolean tripIsPossible) {
        this.tripIsPossible = tripIsPossible;
    }


    public LinkedList<Location> getShortPath() {
        return shortPath;
    }

    public void setShortPath(LinkedList<Location> shortPath) {
        this.shortPath = shortPath;
    }

    public ArrayList<Location> getChargeLocations() {
        return chargeLocations;
    }

    public void setChargeLocations(ArrayList<Location> chargeLocations) {
        this.chargeLocations = chargeLocations;
    }

    public Integer getPathLength() {
        return pathLength;
    }

    public void setPathLength(Integer pathLength) {
        this.pathLength = pathLength;
    }

    public int getNumberOfCharges() {
        return numberOfCharges;
    }

    public void setNumberOfCharges(int numberOfCharges) {
        this.numberOfCharges = numberOfCharges;
    }

    public int getAutonomiaMax() {
        return autonomiaMax;
    }

    public void setAutonomiaMax(int autonomiaMax) {
        this.autonomiaMax = autonomiaMax;
    }

    @Override
    public String toString() {
        return "USEI03_DTO{" +
                "tripIsPossible=" + tripIsPossible +
                ", shortPath=" + shortPath +
                ", chargeLocations=" + chargeLocations +
                ", pathLength=" + pathLength +
                ", numberOfCharges=" + numberOfCharges +
                ", autonomiaMax=" + autonomiaMax +
                '}';
    }

}
