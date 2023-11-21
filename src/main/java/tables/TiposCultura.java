package tables;

public class TiposCultura {

    // TIPOS_CULTURA(idTipoCultura, nome)
    private int idTipoCultura;
    private String nome;

    // CONSTRUCTOR
    public TiposCultura(int idTipoCultura, String nome){
        this.idTipoCultura = idTipoCultura;
        this.nome = nome;
    }

    // Getters
    public int getIdTipoCultura() {
        return idTipoCultura;
    }

    public String getNome() {
        return nome;
    }

    // Methods
    public void print() {
        // INSERT INTO TIPOS_CULTURA(idTipoCultura, nome) VALUES (?, ?);
        System.out.println("INSERT INTO TIPOS_CULTURA(idTipoCultura, nome) VALUES (" +
                this.idTipoCultura + ", '" + this.nome + "');");
    }

    public String append(){
        String text = "INSERT INTO TIPOS_CULTURA(idTipoCultura, nome) VALUES (" +
                this.idTipoCultura + ", '" + this.nome + "');";
        return text;
    }
}
