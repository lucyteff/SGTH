package com.sgth.model;

import java.time.LocalDateTime;

public class Visita {
    private int id;
    private int pacienteId;
    private LocalDateTime dataHoraChegada;
    private String motivo;
    private String status; // AGUARDANDO_TRIAGEM, EM_TRIAGEM, AGUARDANDO_ATENDIMENTO, EM_ATENDIMENTO, FINALIZADO
    private String resumoAtendimento;  // novo atributo pro tp4

    public Visita() {}

    public Visita(int id, int pacienteId, LocalDateTime dataHoraChegada, String motivo, String status, String resumoAtendimento) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.dataHoraChegada = dataHoraChegada;
        this.motivo = motivo;
        this.status = status;
        this.resumoAtendimento = resumoAtendimento;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPacienteId() { return pacienteId; }
    public void setPacienteId(int pacienteId) { this.pacienteId = pacienteId; }
    public LocalDateTime getDataHoraChegada() { return dataHoraChegada; }
    public void setDataHoraChegada(LocalDateTime dataHoraChegada) { this.dataHoraChegada = dataHoraChegada; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getResumoAtendimento() { return resumoAtendimento; }  // NOVO
    public void setResumoAtendimento(String resumoAtendimento) { this.resumoAtendimento = resumoAtendimento; }  // NOVO

    @Override
    public String toString() {
        return String.format("ID: %d | PacienteID: %d | Status: %s", id, pacienteId, status);
    }
}