package tables;

public class Produtos {

    // PRODUTOS(CULTURAID, produto, quantidade, UNIDADEMEDIDAID, TIPOCONSUMOID)

    private int culturaId;
    private String produto;
    private double quantidade;
    private String quantidadeUnidadeMedida;
    private int tipoConsumoId;

    // CONSTRUCTORS
    public Produtos(int culturaId, String produto) {
        this.culturaId = culturaId;
        this.produto = produto;
        this.quantidade = 0;
    }

    // GETTERS
    public int getCulturaId() {
        return culturaId;
    }

    public String getProduto() {
        return produto;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public int getTipoConsumoId() {
        return tipoConsumoId;
    }

    public void setQuantidadeUnidadeMedida(String quantidadeUnidadeMedida) {
        this.quantidadeUnidadeMedida = quantidadeUnidadeMedida;
    }

    public void addQuantidade(double quantidade) {
        this.quantidade = this.quantidade + quantidade;
    }

    public void removeQuantidade(double quantidade) {
        if (quantidade > this.quantidade) {
            this.quantidade = 0;
        } else {
            this.quantidade = this.quantidade - quantidade;
        }
    }

    // FUNCTIONS
    public void print(){
        // INSERT INTO PRODUTOS(CULTURAID, produto) VALUES (?, ?);
        System.out.println("INSERT INTO PRODUTOS(CULTURAID, produto) VALUES (" +
                this.culturaId + ", '" + this.produto + "');");
    }

    public String append(){
        String text = "INSERT INTO PRODUTOS(CULTURAID, produto) VALUES (" +
                this.culturaId + ", '" + this.produto + "');";
        return text;
    }
}
