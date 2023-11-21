package tables;

public class SistemasRega {

    // SISTEMAS_REGA(QUINTAID, EDIFICIOID, nome, area, UNIDADEMEDIDA)
    private int quintaId;
    private int edificioId;
    private String nome;
    private double area;
    private String unidadeMedida;

    // CONSTRUCTORS
    public SistemasRega(int quintaId, int edificioId, String nome, double area, String unidadeMedida) {
        this.quintaId = quintaId;
        this.edificioId = edificioId;
        this.nome = nome;
        this.area = area;
        this.unidadeMedida = unidadeMedida;
    }

    // GETTERS
    public int getQuintaId() {
        return quintaId;
    }

    public int getEdificioId() {
        return edificioId;
    }

    public String getNome() {
        return nome;
    }

    public double getArea() {
        return area;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    // FUNCTIONS
    public void print() {
        // INSERT INTO SISTEMAS_REGA(QUINTAID, EDIFICIOID, nome, area, UNIDADEMEDIDA) VALUES (?, ?, ?, ?, ?);
        System.out.println("INSERT INTO SISTEMAS_REGA(QUINTAID, EDIFICIOID, nome, area, UNIDADEMEDIDA) VALUES (" +
                this.quintaId + ", " + this.edificioId + ", '" + this.nome + "', " +
                this.area + ", '" + this.unidadeMedida + "');");
    }

    public String append() {
        String text = "INSERT INTO SISTEMAS_REGA(QUINTAID, EDIFICIOID, nome, area, UNIDADEMEDIDA) VALUES (" +
                this.quintaId + ", " + this.edificioId + ", '" + this.nome + "', " +
                this.area + ", '" + this.unidadeMedida + "');";
        return text;
    }
}
