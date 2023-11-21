package tables;

public class TiposConsumo {

    // TIPOS_CONSUMO(idTipoConsumo, nome)

    private int idTipoConsumo;
    private String descricao;

    // CONSTRUCTOR
    public TiposConsumo(int idTipoConsumo, String nomeCompleto) {
        this.idTipoConsumo = idTipoConsumo;
        this.descricao = nomeCompleto;
    }

    // Getters
    public int getIdTipoConsumo() {
        return idTipoConsumo;
    }

    public String getDescricao() {
        return descricao;
    }

    // Methods
    public void print() {
        //INSERT INTO TIPOS_CONSUMO(idTipoConsumo, nome) VALUES (?, ?);
        System.out.println("INSERT INTO TIPOS_CONSUMO(idTipoConsumo, nome) VALUES (" + this.idTipoConsumo + ", '" + this.descricao + "');");
    }

    public String append(){
        String text = "INSERT INTO TIPOS_CONSUMO(idTipoConsumo, nome) VALUES (" + this.idTipoConsumo + ", '" + this.descricao + "');";
        return text;
    }
}
