package tables;

public class Parcelas {

    // PARCELAS(QUINTAID, idParcela, nome, area, UNIDADEMEDIDA)
    private int quintaId;
    private int idParcela;
    private String nome;
    private double area;
    private String unidadeMedida;

    // CONSTRUCTORS
    public Parcelas(int quintaId, int idParcela, String nome, double area, String unidadeMedida) {
        this.quintaId = quintaId;
        this.idParcela = idParcela;
        this.nome = nome;
        this.area = area;
        this.unidadeMedida = unidadeMedida;
    }

    // GETTERS
    public int getQuintaId() {
        return quintaId;
    }

    public int getIdParcela() {
        return idParcela;
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
        // INSERT INTO PARCELAS(QUINTAID, idParcela, nome, area, UNIDADEMEDIDA) VALUES (?, ?, ?, ?, ?);
        System.out.println("INSERT INTO PARCELAS(QUINTAID, idParcela, nome, area, UNIDADEMEDIDA) VALUES (" +
                this.quintaId + ", " + this.idParcela + ", '" + this.nome + "', " +
                this.area + ", '" + this.unidadeMedida + "');");
    }

    public String append() {
        String text = "INSERT INTO PARCELAS(QUINTAID, idParcela, nome, area, UNIDADEMEDIDA) VALUES (" +
                this.quintaId + ", " + this.idParcela + ", '" + this.nome + "', " +
                this.area + ", '" + this.unidadeMedida + "');";
        return text;
    }
}
