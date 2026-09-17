package br.com.mvc.model;

import java.sql.Timestamp;

public class Notificacao {
    private Long id;
    private String tipo;
    private String mensagem;
    private String url;
    private boolean lida;
    private Timestamp criadaEm;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public boolean isLida() { return lida; }
    public void setLida(boolean lida) { this.lida = lida; }
    public Timestamp getCriadaEm() { return criadaEm; }
    public void setCriadaEm(Timestamp criadaEm) { this.criadaEm = criadaEm; }
}
