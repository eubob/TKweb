package br.com.mvc.model;

public class Usuario {
    private Long id;
    private String nome;
    private String email;
    private String senha;
    private double mediaAvaliacao;
    private int totalAvaliacoes;
    private int pontos;

    public Usuario() {
    }

    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public double getMediaAvaliacao() { return mediaAvaliacao; }
    public void setMediaAvaliacao(double mediaAvaliacao) { this.mediaAvaliacao = mediaAvaliacao; }
    public int getTotalAvaliacoes() { return totalAvaliacoes; }
    public void setTotalAvaliacoes(int totalAvaliacoes) { this.totalAvaliacoes = totalAvaliacoes; }
    public int getPontos() { return pontos; }
    public void setPontos(int pontos) { this.pontos = pontos; }
}
