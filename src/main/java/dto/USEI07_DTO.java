package dto;

import domain.Location;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class USEI07_DTO {

    public static class Hub {
        public String name;
        private boolean isHub;
        private LocalTime arrivalTime;
        private LocalTime departureTime;

        public Hub(String name, int openHour, int closeHour) {
            this.name = name;
            this.isHub = true;
            // Set additional initialization logic if needed
        }

        public boolean isHub() {
            return isHub;
        }

        public void setArrivalTime(LocalTime arrivalTime) {
            this.arrivalTime = arrivalTime;
        }

        public LocalTime getArrivalTime() {
            return arrivalTime;
        }

        public void setDepartureTime(LocalTime departureTime) {
            this.departureTime = departureTime;
        }

        public LocalTime getDepartureTime() {
            return departureTime;
        }
    }

    public static class PathResult {
        List<Hub> path;
        double totalDistance;
        String arrivalTime;
        String departureTime;
        int numberOfLoadings;
        int totalTime;

        public PathResult(List<Hub> path, double totalDistance, String arrivalTime, String departureTime, int numberOfLoadings, int totalTime) {
            this.path = path;
            this.totalDistance = totalDistance;
            this.arrivalTime = arrivalTime;
            this.departureTime = departureTime;
            this.numberOfLoadings = numberOfLoadings;
            this.totalTime = totalTime;
        }

        public static PathResult createWithPathAndDistance(List<Hub> path, double totalDistance, String arrivalTime, String departureTime, int numberOfLoadings, int totalTime) {
            return new PathResult(path, totalDistance, arrivalTime, departureTime, numberOfLoadings, totalTime);
        }
    }

    private Hub hub;

//    public USEI07_DTO(Hub hub) {
//        this.hub = hub;
//    }

    public Hub getHub() {
        return hub;
    }

    private PathResult pathResult;

    public USEI07_DTO(Hub hub) {
        this.hub = hub;
        this.pathResult = new PathResult(new ArrayList<>(), 0, "", "", 0, 0);
    }

    public void setPathResult(PathResult pathResult) {
        this.pathResult = pathResult;
    }

    public PathResult getPathResult() {
        return pathResult;
    }

    public double getTotalDistance() {
        return pathResult.totalDistance;
    }

    public int getNumberOfLoadings() {
        return pathResult.numberOfLoadings;
    }

    public int getTotalTime() {
        return pathResult.totalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        pathResult.arrivalTime = arrivalTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public LocalTime getArrivalTime() {
        return LocalTime.parse(pathResult.arrivalTime, DateTimeFormatter.ofPattern("HH:mm"));
    }

    public void setDepartureTime(LocalTime departureTime) {
        pathResult.departureTime = departureTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public LocalTime getDepartureTime() {
        return LocalTime.parse(pathResult.departureTime, DateTimeFormatter.ofPattern("HH:mm"));
    }

    public void addToPath(Hub hub) {
        pathResult.path.add(hub);
    }

    // Métodos adicionados
    public void setTotalDistance(double totalDistance) {
        pathResult.totalDistance = totalDistance;
    }

    public void setNumberOfLoadings(int numberOfLoadings) {
        pathResult.numberOfLoadings = numberOfLoadings;
    }

    public void setTotalTime(int totalTime) {
        pathResult.totalTime = totalTime;
    }
}
