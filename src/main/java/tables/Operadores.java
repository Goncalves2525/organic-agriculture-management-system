package tables;

public class Operadores {

    //OPERADORES(idOperador, nome)
    private int idOperador;
    private String nome;

    // CONSTRUCTORS
    public Operadores(int idOperador, String nome) {
        this.idOperador = idOperador;
        this.nome = nome;
    }

    // GETTERS
    public int getIdOperador() {
        return idOperador;
    }

    public String getNome() {
        return nome;
    }


    // FUNCTIONS
    public void print() {
        // INSERT INTO OPERADORES(idOperador, nome) VALUES (?, ?);
        System.out.println("INSERT INTO OPERADORES(idOperador, nome) VALUES (" +
                this.idOperador + ", '" + this.nome + "');");
    }

    public String append() {
        String text = "INSERT INTO OPERADORES(idOperador, nome) VALUES (" +
                this.idOperador + ", '" + this.nome + "');";
        return text;
    }
}
