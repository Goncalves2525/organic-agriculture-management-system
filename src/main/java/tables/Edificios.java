package tables;

public class Edificios {

    // EDIFICIOS(QUINTAID, idEdificio)
    private int quintaId;
    private int idEdificio;

    // CONSTRUCTORS
    public Edificios(int quintaId, int idEdificio) {
        this.quintaId = quintaId;
        this.idEdificio = idEdificio;
    }

    // GETTERS
    public int getQuintaId() {
        return quintaId;
    }

    public int getIdEdificio() {
        return idEdificio;
    }


    // FUNCTIONS
    public void print() {
        // INSERT INTO EDIFICIOS(QUINTAID, idEdificio) VALUES (?, ?);
        System.out.println("INSERT INTO EDIFICIOS(QUINTAID, idEdificio) VALUES (" +
                this.quintaId + ", " + this.idEdificio + ");");
    }

    public String append(){
        String text = "INSERT INTO EDIFICIOS(QUINTAID, idEdificio) VALUES (" +
                this.quintaId + ", " + this.idEdificio + ");";
        return text;
    }
}
