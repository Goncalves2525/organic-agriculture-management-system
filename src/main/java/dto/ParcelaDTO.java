package dto;

public class ParcelaDTO {
    private int quintaId;
    private int idParcela;
    private String nome;
    private int area;
    private String UNIDADE = "ha";

    private String dataMonda;

    // Constructor
    public ParcelaDTO(int quintaId, int idParcela, String nome, int area, String unidade) {
        this.quintaId = quintaId;
        this.idParcela = idParcela;
        this.nome = nome;
        this.area = area;
        this.UNIDADE = unidade;
    }

    // Getters
    public int getQuintaId() {
        return quintaId;
    }

    public int getIdParcela() {
        return idParcela;
    }

    public String getNome() {
        return nome;
    }

    public int getArea() {
        return area;
    }

    public String getUNIDADE() {
        return UNIDADE;
    }

    public String getDataMonda() {
        return dataMonda;
    }

    // Setters
    public void setQuintaId(int quintaId) {
        this.quintaId = quintaId;
    }

    public void setIdParcela(int idParcela) {
        this.idParcela = idParcela;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public void setDataMonda(String dataMonda) {
        this.dataMonda = dataMonda;
    }

    @Override
    public String toString() {
        return "Parcela da quinta " + quintaId + " - " + nome + " ID: " + idParcela +" com " + area + UNIDADE;
    }
}
