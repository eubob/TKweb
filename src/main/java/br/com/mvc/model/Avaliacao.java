package br.com.mvc.model;

public class Avaliacao {
    private Long id;
    private Long trocaId;
    private Long avaliadorId;
    private Long avaliadoId;
    private int nota;
    private String comentario;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTrocaId() { return trocaId; }
    public void setTrocaId(Long trocaId) { this.trocaId = trocaId; }
    public Long getAvaliadorId() { return avaliadorId; }
    public void setAvaliadorId(Long avaliadorId) { this.avaliadorId = avaliadorId; }
    public Long getAvaliadoId() { return avaliadoId; }
    public void setAvaliadoId(Long avaliadoId) { this.avaliadoId = avaliadoId; }
    public int getNota() { return nota; }
    public void setNota(int nota) { this.nota = nota; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
}
