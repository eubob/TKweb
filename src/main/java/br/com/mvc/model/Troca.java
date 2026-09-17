package br.com.mvc.model;

import java.sql.Timestamp;

public class Troca {
    private Long id;
    private Long solicitanteId;
    private Long receptorId;
    private String solicitanteNome;
    private String receptorNome;
    private Long habilidadeSolicitadaId;
    private String habilidadeSolicitadaTitulo;
    private Long habilidadeOferecidaId;
    private String habilidadeOferecidaTitulo;
    private String status;
    private Timestamp criadaEm;
    private Timestamp atualizadaEm;
    private boolean avaliacaoEnviada;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getSolicitanteId() { return solicitanteId; }
    public void setSolicitanteId(Long solicitanteId) { this.solicitanteId = solicitanteId; }
    public Long getReceptorId() { return receptorId; }
    public void setReceptorId(Long receptorId) { this.receptorId = receptorId; }
    public String getSolicitanteNome() { return solicitanteNome; }
    public void setSolicitanteNome(String solicitanteNome) { this.solicitanteNome = solicitanteNome; }
    public String getReceptorNome() { return receptorNome; }
    public void setReceptorNome(String receptorNome) { this.receptorNome = receptorNome; }
    public Long getHabilidadeSolicitadaId() { return habilidadeSolicitadaId; }
    public void setHabilidadeSolicitadaId(Long habilidadeSolicitadaId) { this.habilidadeSolicitadaId = habilidadeSolicitadaId; }
    public String getHabilidadeSolicitadaTitulo() { return habilidadeSolicitadaTitulo; }
    public void setHabilidadeSolicitadaTitulo(String habilidadeSolicitadaTitulo) { this.habilidadeSolicitadaTitulo = habilidadeSolicitadaTitulo; }
    public Long getHabilidadeOferecidaId() { return habilidadeOferecidaId; }
    public void setHabilidadeOferecidaId(Long habilidadeOferecidaId) { this.habilidadeOferecidaId = habilidadeOferecidaId; }
    public String getHabilidadeOferecidaTitulo() { return habilidadeOferecidaTitulo; }
    public void setHabilidadeOferecidaTitulo(String habilidadeOferecidaTitulo) { this.habilidadeOferecidaTitulo = habilidadeOferecidaTitulo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Timestamp getCriadaEm() { return criadaEm; }
    public void setCriadaEm(Timestamp criadaEm) { this.criadaEm = criadaEm; }
    public Timestamp getAtualizadaEm() { return atualizadaEm; }
    public void setAtualizadaEm(Timestamp atualizadaEm) { this.atualizadaEm = atualizadaEm; }
    public boolean isAvaliacaoEnviada() { return avaliacaoEnviada; }
    public void setAvaliacaoEnviada(boolean avaliacaoEnviada) { this.avaliacaoEnviada = avaliacaoEnviada; }
}
