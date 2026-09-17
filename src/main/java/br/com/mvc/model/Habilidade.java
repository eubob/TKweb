package br.com.mvc.model;

public class Habilidade {

    private Long id;
    private String titulo;
    private String descricao;
    private String tipo;
    private Long usuarioId;

    public Habilidade() {
    }

    public Habilidade(String titulo, String descricao, String tipo, Long usuarioId) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.tipo = tipo;
        this.usuarioId = usuarioId;
    }

    public Habilidade(
            Long id,
            String titulo,
            String descricao,
            String tipo,
            Long usuarioId
    ) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.tipo = tipo;
        this.usuarioId = usuarioId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}