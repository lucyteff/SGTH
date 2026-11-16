package com.sgth.model;

import java.time.LocalDateTime;

public class Triagem {
    private int id;
    private int visitaId;
    private int enfermeiroId;
    private String sinaisVitais;
    private String queixaPrincipal;
    private String alergias;  // novo atributo pro tp4
    private String prioridade; // VERMELHO, LARANJA, AMARELO, VERDE, AZUL
    private LocalDateTime dataHoraTriagem;

    public Triagem() {}

    public Triagem(int id, int visitaId, int enfermeiroId, String sinaisVitais,
                   String queixaPrincipal, String alergias, String prioridade, LocalDateTime dataHoraTriagem) {
        this.id = id;
        this.visitaId = visitaId;
        this.enfermeiroId = enfermeiroId;
        this.sinaisVitais = sinaisVitais;
        this.queixaPrincipal = queixaPrincipal;
        this.alergias = alergias;
        this.prioridade = prioridade;
        this.dataHoraTriagem = dataHoraTriagem;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getVisitaId() { return visitaId; }
    public void setVisitaId(int visitaId) { this.visitaId = visitaId; }
    public int getEnfermeiroId() { return enfermeiroId; }
    public void setEnfermeiroId(int enfermeiroId) { this.enfermeiroId = enfermeiroId; }
    public String getSinaisVitais() { return sinaisVitais; }
    public void setSinaisVitais(String sinaisVitais) { this.sinaisVitais = sinaisVitais; }
    public String getQueixaPrincipal() { return queixaPrincipal; }
    public void setQueixaPrincipal(String queixaPrincipal) { this.queixaPrincipal = queixaPrincipal; }
    public String getAlergias() { return alergias; }  // NOVO
    public void setAlergias(String alergias) { this.alergias = alergias; }  // NOVO
    public String getPrioridade() { return prioridade; }
    public void setPrioridade(String prioridade) { this.prioridade = prioridade; }
    public LocalDateTime getDataHoraTriagem() { return dataHoraTriagem; }
    public void setDataHoraTriagem(LocalDateTime dataHoraTriagem) { this.dataHoraTriagem = dataHoraTriagem; }

    @Override
    public String toString() {
        return String.format("ID: %d | VisitaID: %d | Prioridade: %s", id, visitaId, prioridade);
    }
}