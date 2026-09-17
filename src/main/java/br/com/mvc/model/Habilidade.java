package br.com.mvc.model;

import java.sql.Timestamp;

public class Habilidade {
    private Long id;
    private String titulo;
    private String descricao;
    private String tipo;
    private String categoria;
    private Long usuarioId;
    private String usuarioNome;
    private Timestamp criadoEm;
    private int curtidas;
    private int comentarios;
    private boolean curtidaPeloUsuario;

    public Habilidade() {
    }

    public Habilidade(String titulo, String descricao, String tipo, String categoria, Long usuarioId) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.tipo = tipo;
        this.categoria = categoria;
        this.usuarioId = usuarioId;
    }

    public Habilidade(Long id, String titulo, String descricao, String tipo, String categoria, Long usuarioId) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.tipo = tipo;
        this.categoria = categoria;
        this.usuarioId = usuarioId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public String getUsuarioNome() { return usuarioNome; }
    public void setUsuarioNome(String usuarioNome) { this.usuarioNome = usuarioNome; }
    public Timestamp getCriadoEm() { return criadoEm; }
    public void setCriadoEm(Timestamp criadoEm) { this.criadoEm = criadoEm; }
    public int getCurtidas() { return curtidas; }
    public void setCurtidas(int curtidas) { this.curtidas = curtidas; }
    public int getComentarios() { return comentarios; }
    public void setComentarios(int comentarios) { this.comentarios = comentarios; }
    public boolean isCurtidaPeloUsuario() { return curtidaPeloUsuario; }
    public void setCurtidaPeloUsuario(boolean curtidaPeloUsuario) { this.curtidaPeloUsuario = curtidaPeloUsuario; }
}
