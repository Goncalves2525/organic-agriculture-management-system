package tables;

public class Quintas {

    // QUINTAS(idQuinta, nome)

    private int idQuinta;
    private String nome;

    // CONSTRUCTORS

    public Quintas(int idQuinta, String nome) {
        this.idQuinta = idQuinta;
        this.nome = nome;
    }

    // GETTERS

    public int getIdQuinta() {
        return idQuinta;
    }

    public String getNome() {
        return nome;
    }

    // FUNCTIONS

    public void print(){
        // INSERT INTO QUINTAS(idQuinta, nome) VALUES (?, ?);
        System.out.println("INSERT INTO QUINTAS(idQuinta, nome) VALUES (" +
                this.idQuinta+", '"+this.nome+"');");
    }

    public String append(){
        String text = "INSERT INTO QUINTAS(idQuinta, nome) VALUES (" +
                this.idQuinta+", '"+this.nome+"');";
        return text;
    }
}
