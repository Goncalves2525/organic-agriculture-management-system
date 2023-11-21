package tables;

public class Formulacoes {

    // FORMULACOES(idFormulacao, formato, tipo, aplicacao)
    private int idFormulacao;
    private String formato;
    private String tipo;
    private String aplicacao;

    // CONSTRUCTORS
    public Formulacoes(int idFormulacao, String formato, String tipo, String aplicacao) {
        this.idFormulacao = idFormulacao;
        this.formato = formato;
        this.tipo = tipo;
        this.aplicacao = aplicacao;
    }

    // GETTERS
    public int getIdFormulacao() {
        return idFormulacao;
    }

    public String getFormato() {
        return formato;
    }

    public String getTipo() {
        return tipo;
    }

    public String getAplicacao() {
        return aplicacao;
    }

    // FUNCTIONS
    public void print() {
        // INSERT INTO FORMULACOES(idFormulacao, formato, tipo) VALUES (?, ?, ?, ?);
        System.out.println("INSERT INTO FORMULACOES(idFormulacao, formato, tipo, aplicacao) VALUES (" +
                this.idFormulacao + ", '" + this.formato + "', '" + this.tipo + "', '" + this.aplicacao + "');");
    }

    public String append() {
        String text = "INSERT INTO FORMULACOES(idFormulacao, formato, tipo, aplicacao) VALUES (" +
                this.idFormulacao + ", '" + this.formato + "', '" + this.tipo + "', '" + this.aplicacao + "');";
        return text;
    }
}
