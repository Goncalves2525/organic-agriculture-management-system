package tables;

public class Substancias {

    // SUBSTANCIAS(idSubstancia, substancia, percentagem, UNIDADEMEDIDA)
    private int idSubstancia;
    private String substancia;
    private double percentagem;
    private String unidadeMedida;

    // CONSTRUCTORS
    public Substancias(int idSubstancia, String substancia, double percentagem, String unidadeMedida) {
        this.idSubstancia = idSubstancia;
        this.substancia = substancia;
        this.percentagem = percentagem;
        this.unidadeMedida = unidadeMedida;
    }

    // GETTERS

    public int getIdSubstancia() {
        return idSubstancia;
    }

    public String getSubstancia() {
        return substancia;
    }

    public double getPercentagem() {
        return percentagem;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    // FUNCTIONS
    public void print() {
        // INSERT INTO SUBSTANCIAS(idSubstancia, substancia, percentagem, UNIDADEMEDIDA) VALUES (?, ?, ?, ?);
        System.out.println("INSERT INTO SUBSTANCIAS(idSubstancia, substancia, percentagem, UNIDADEMEDIDA) VALUES (" +
                this.idSubstancia + ", '" + this.substancia + "', " + this.percentagem + ", '" + this.unidadeMedida + "');");
    }

    public String append() {
        String text = "INSERT INTO SUBSTANCIAS(idSubstancia, substancia, percentagem, UNIDADEMEDIDA) VALUES (" +
                this.idSubstancia + ", '" + this.substancia + "', " + this.percentagem + ", '" + this.unidadeMedida + "');";
        return text;
    }
}
