package tables;

public class ComponentesFT {

    // COMPONENTES(FICHATECNICAID, idxComponente, SUBSTANCIAID)
    private int fichaTecnicaId;
    private int idComponente;
    private int substancia;

    // CONSTRUCTORS
    public ComponentesFT(int fichaTecnicaId, int idComponente, int substancia) {
        this.fichaTecnicaId = fichaTecnicaId;
        this.idComponente = idComponente;
        this.substancia = substancia;
    }

    // GETTERS
    public int getFichaTecnicaId() {
        return fichaTecnicaId;
    }

    public int getIdComponente() {
        return idComponente;
    }

    public int getSubstancia() {
        return substancia;
    }

    // FUNCTIONS
    public void print() {
        // INSERT INTO COMPONENTES(FICHATECNICAID, idxComponente, SUBSTANCIAID) VALUES (?, ?, ?);
        System.out.println("INSERT INTO COMPONENTES(FICHATECNICAID, idxComponente, SUBSTANCIAID) VALUES (" +
                this.fichaTecnicaId + ", " + this.idComponente + ", " + this.substancia + ");");
    }

    public String append() {
        String text = "INSERT INTO COMPONENTES(FICHATECNICAID, idxComponente, SUBSTANCIAID) VALUES (" +
                this.fichaTecnicaId + ", " + this.idComponente + ", " + this.substancia + ");";
        return text;
    }
}
