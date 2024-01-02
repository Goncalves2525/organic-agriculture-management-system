package dto;

import domain.Location;

import java.time.LocalTime;
import java.util.*;

public class USEI07_DTO {

    public static class Hub {
        public String name;
        public int degreeCentrality;

        public int openHour;
        public int closeHour;

        public LocalTime arrivalTime;
        public LocalTime departureTime;

        public Hub(String name, int openHour, int closeHour) {
            this.name = name;
            this.openHour = openHour;
            this.closeHour = closeHour;
        }
        // Construtor atualizado
        public Hub(String name, int degreeCentrality, LocalTime arrivalTime, LocalTime departureTime) {
            this.name = name;
            this.degreeCentrality = degreeCentrality;
            this.arrivalTime = arrivalTime;
            this.departureTime = departureTime;
        }

        public Hub(String name, int degreeCentrality, LocalTime arrivalTime, int openHour, int closeHour, int travelTime, int chargingTime) {
            this.name = name;
            this.degreeCentrality = degreeCentrality;
            this.arrivalTime = arrivalTime;
            this.openHour = openHour;
            this.closeHour = closeHour;
            // Add any additional initialization logic for arrivalTime, travelTime, and chargingTime if needed
        }


        public Hub(String name) {
            this.name = name;
        }

        // New constructor
        public Hub(String name, int degreeCentrality) {
            this.name = name;
            this.degreeCentrality = degreeCentrality;
            // Add any additional initialization logic for degreeCentrality if needed
        }
        private void calculateArrivalAndDepartureTime(int openHour, int closeHour, int travelTime, int chargingTime) {
            // Calcular o horário de chegada
            LocalTime potentialArrivalTime = departureTime.plusMinutes(travelTime);

            // Verificar se o horário de chegada ultrapassa o horário de fechamento
            if (potentialArrivalTime.isAfter(LocalTime.of(closeHour, 0))) {
                // Se ultrapassar, definir o horário de chegada como o horário de fechamento
                this.arrivalTime = LocalTime.of(closeHour, 0);
            } else {
                // Caso contrário, manter o horário calculado
                this.arrivalTime = potentialArrivalTime;
            }

            // Calcular o horário de partida
            LocalTime potentialDepartureTime = this.arrivalTime.plusMinutes(chargingTime);

            // Verificar se o horário de partida é anterior ao horário de abertura
            if (potentialDepartureTime.isBefore(LocalTime.of(openHour, 0))) {
                // Se for anterior, definir o horário de partida como o horário de abertura
                this.departureTime = LocalTime.of(openHour, 0);
            } else {
                // Caso contrário, manter o horário calculado
                this.departureTime = potentialDepartureTime;
            }
        }


        // Método para validar se o horário está dentro do horário de funcionamento
        public boolean isWithinOperatingHours(Location location) {
            int openHour = location.getSchedule().getOpenHour();
            int closeHour = location.getSchedule().getCloseHour();

            // Lógica de validação do horário
            // Por exemplo, você pode usar a classe SimpleDateFormat para comparar horários

            // Substitua a lógica abaixo pelo método adequado à sua necessidade
            boolean isValid = true;

            return isValid;
        }

        @Override
        public String toString() {
            return "Hub{name='" + name + "', Arrival Time='" + openHour + "', Departure Time='" + closeHour + "'}";
        }
    }

    public static class PathResult {
        List<Hub> path;
        double totalDistance;
        // Adicionando os horários de chegada e partida
        String arrivalTime;
        String departureTime;

        public PathResult(List<Hub> path, double totalDistance, String arrivalTime, String departureTime) {
            this.path = path;
            this.totalDistance = totalDistance;
            this.arrivalTime = arrivalTime;
            this.departureTime = departureTime;
        }

        public static PathResult createWithPathAndDistance(List<Hub> path, double totalDistance, String arrivalTime, String departureTime) {
            return new PathResult(path, totalDistance, arrivalTime, departureTime);
        }
    }

    public USEI07_DTO(Hub hub) {
        // Your initialization logic here, for example:
        // You might want to store the Hub object or perform some additional setup.
        // For now, let's assume you have a field in USEI07_DTO to store the Hub.
        this.hub = hub;
    }

    private Hub hub;  // Assuming you have a field to store the Hub

    // Getter for the Hub field
    public Hub getHub() {
        return hub;
    }

    public static class DistanceMatrix {
        private Map<Hub, Map<Hub, Double>> distances;

        public DistanceMatrix() {
            this.distances = new HashMap<>();
        }

        public void addDistance(Hub hub1, Hub hub2, double distance) {
            distances.computeIfAbsent(hub1, k -> new HashMap<>()).put(hub2, distance);
            distances.computeIfAbsent(hub2, k -> new HashMap<>()).put(hub1, distance);
        }

        public double getDistance(Hub hub1, Hub hub2) {
            return distances.getOrDefault(hub1, Collections.emptyMap()).getOrDefault(hub2, Double.POSITIVE_INFINITY);
        }
    }

    public static List<Hub> findOptimizedPath(List<Hub> hubs, DistanceMatrix distanceMatrix) {
        List<Hub> bestPath = null;
        double bestDistance = Double.POSITIVE_INFINITY;

        List<Hub> currentPath = new ArrayList<>(hubs);
        currentPath.add(currentPath.get(0)); // Complete the cycle

        do {
            double currentDistance = calculatePathDistance(currentPath, distanceMatrix);
            if (currentDistance < bestDistance) {
                bestDistance = currentDistance;
                bestPath = new ArrayList<>(currentPath);
            }
        } while (nextPermutation(currentPath));

        return bestPath;
    }

    private static double calculatePathDistance(List<Hub> path, DistanceMatrix distanceMatrix) {
        double distance = 0.0;
        for (int i = 0; i < path.size() - 1; i++) {
            distance += distanceMatrix.getDistance(path.get(i), path.get(i + 1));
        }
        return distance;
    }

    private static boolean nextPermutation(List<Hub> array) {
        int i = array.size() - 2;
        while (i >= 0 && array.get(i).equals(array.get(i + 1))) {
            i--;
        }

        if (i < 0) {
            return false;
        }

        int j = array.size() - 1;
        while (array.get(j).equals(array.get(i))) {
            j--;
        }

        swap(array, i, j);
        reverse(array, i + 1, array.size() - 1);

        return true;
    }

    private static <T> void swap(List<T> list, int i, int j) {
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    private static <T> void reverse(List<T> list, int start, int end) {
        while (start < end) {
            swap(list, start, end);
            start++;
            end--;
        }
    }

    @Override
    public String toString() {
        return  "hub=" + hub.name +
                ", arrivalTime='" + hub.openHour + '\'' +
                ", departureTime='" + hub.closeHour + '\'';
    }
}
