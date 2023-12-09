package tables;

import java.sql.Timestamp;

public class Logs {
    private int idLog;
    private int operacaoId;
    private Timestamp timestamp;
    private String descricao;

    public Logs(int idLog, int operacaoId, Timestamp timestamp, String descricao) {
        this.idLog = idLog;
        this.operacaoId = operacaoId;
        this.timestamp = timestamp;
        this.descricao = descricao;
    }

    public int getIdLog() {
        return idLog;
    }

    public int getOperacaoId() {
        return operacaoId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getDescricao() {
        return descricao;
    }

    public void print() {
        System.out.println("INSERT INTO LOGS(IDLOG, OPERACAOID, TIMESTAMP, DESCRICAO) VALUES (" +
                this.idLog + ", " + this.operacaoId + ", " + this.timestamp + ", '" + this.descricao + "');");
    }

    public String append() {
        String text = "INSERT INTO LOGS(IDLOG, OPERACAOID, TIMESTAMP, DESCRICAO) VALUES (" +
                this.idLog + ", " + this.operacaoId + ", " + this.timestamp + ", '" + this.descricao + "');";
        return text;
    }

    @Override
    public String toString() {
        return "Logs{" +
                "idLog=" + idLog +
                ", operacaoId=" + operacaoId +
                ", timestamp=" + timestamp +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}
