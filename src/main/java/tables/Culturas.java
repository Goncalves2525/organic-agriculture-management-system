package tables;

public class Culturas {

    // CULTURAS(idCultura, TIPOCULTURAID, ESPECIEVEGETALID, nome, nomeCompleto)

    private int idCultura;
    private int tipoCulturaId;
    private int especieVegetalId;
    private String nome;
    private String nomeCompleto;
    private String nomeComum;

    // CONSTRUCTORS

    public Culturas(int idCultura, int tipoCulturaId, int especieVegetalId, String nome, String nomeCompleto, String nomeComum) {
        this.idCultura = idCultura;
        this.tipoCulturaId = tipoCulturaId;
        this.especieVegetalId = especieVegetalId;
        this.nome = nome;
        this.nomeCompleto = nomeCompleto;
        this.nomeComum = nomeComum;
    }

    // GETTERS

    public int getIdCultura() {
        return idCultura;
    }

    public int getTipoCulturaId() {
        return tipoCulturaId;
    }

    public int getEspecieVegetalId() {
        return especieVegetalId;
    }

    public String getNome() {
        return nome.trim();
    }

    public String getNomeCompleto() {
        return nomeCompleto.trim();
    }

    public String getNomeComum() {
        return nomeComum.trim();
    } // used only for internal purposes

    // FUNCTIONS

    public void print(){
        // INSERT INTO CULTURAS(idCultura, TIPOCULTURAID, ESPECIEVEGETALID, nome, nomeCompleto) VALUES (?, ?, ?, ?, ?);
        System.out.println("INSERT INTO CULTURAS(idCultura, TIPOCULTURAID, ESPECIEVEGETALID, nome, nomeCompleto) VALUES (" +
                this.idCultura+", "+this.tipoCulturaId+", "+this.especieVegetalId+", '"+this.nome.replace("'","''")+"', '"+this.nomeCompleto.replace("'","''")+"');");
    }

    public String append(){
        String text = "INSERT INTO CULTURAS(idCultura, TIPOCULTURAID, ESPECIEVEGETALID, nome, nomeCompleto) VALUES (" +
                this.idCultura+", "+this.tipoCulturaId+", "+this.especieVegetalId+", '"+this.nome.replace("'","''")+"', '"+this.nomeCompleto.replace("'","''")+"');";
        return text;
    }
}
