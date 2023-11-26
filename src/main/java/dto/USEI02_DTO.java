package dto;

import domain.Location;

import java.util.ArrayList;
import java.util.LinkedList;

public class USEI02_DTO {

        private Location vertex;
        private int centrality;

        public USEI02_DTO(Location vertex, int centrality) {
            this.vertex = vertex;
            this.centrality = centrality;
        }

        public Location getVertex() {
            return vertex;
        }

        public int getCentrality() {
            return centrality;
        }

        public void setCentrality(int centrality) {
            this.centrality = centrality;
        }

        public void setVertex(Location vertex) {
            this.vertex = vertex;
        }

        @Override
        public String toString() {
            return "LocationCentralityInfoDTO{" +
                    "vertex=" + vertex +
                    ", centrality=" + centrality +
                    '}';
        }
}
