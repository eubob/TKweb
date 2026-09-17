package br.com.mvc.model;

import java.sql.Timestamp;

public class Mensagem {
    private Long id;
    private Long trocaId;
    private Long remetenteId;
    private String remetenteNome;
    private String mensagem;
    private Timestamp enviadaEm;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTrocaId() { return trocaId; }
    public void setTrocaId(Long trocaId) { this.trocaId = trocaId; }
    public Long getRemetenteId() { return remetenteId; }
    public void setRemetenteId(Long remetenteId) { this.remetenteId = remetenteId; }
    public String getRemetenteNome() { return remetenteNome; }
    public void setRemetenteNome(String remetenteNome) { this.remetenteNome = remetenteNome; }
    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
    public Timestamp getEnviadaEm() { return enviadaEm; }
    public void setEnviadaEm(Timestamp enviadaEm) { this.enviadaEm = enviadaEm; }
}
