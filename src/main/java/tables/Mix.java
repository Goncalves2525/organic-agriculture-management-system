package tables;

public class Mix {

    // MIX(idReceita)
    private int idReceita;

    // CONSTRUCTORS
    public Mix(int idReceita) {
        this.idReceita = idReceita;
    }

    // GETTERS
    public int getIdReceita() {
        return idReceita;
    }

    // FUNCTIONS
    public void print() {
        // INSERT INTO MIX(idReceita) VALUES (?);
        System.out.println("INSERT INTO MIX(idReceita) VALUES (" +
                this.idReceita + ");");
    }

    public String append() {
        String text = "INSERT INTO MIX(idReceita) VALUES (" +
                this.idReceita + ");";
        return text;
    }
}
