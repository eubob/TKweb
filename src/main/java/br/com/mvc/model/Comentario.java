package br.com.mvc.model;

import java.sql.Timestamp;

public class Comentario {
    private Long id;
    private Long habilidadeId;
    private Long usuarioId;
    private String usuarioNome;
    private String texto;
    private Timestamp criadoEm;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getHabilidadeId() { return habilidadeId; }
    public void setHabilidadeId(Long habilidadeId) { this.habilidadeId = habilidadeId; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public String getUsuarioNome() { return usuarioNome; }
    public void setUsuarioNome(String usuarioNome) { this.usuarioNome = usuarioNome; }
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
    public Timestamp getCriadoEm() { return criadoEm; }
    public void setCriadoEm(Timestamp criadoEm) { this.criadoEm = criadoEm; }
}
