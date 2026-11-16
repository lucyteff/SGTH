package com.sgth.repository;

import com.sgth.model.Visita;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class VisitaRepository {
    private static final String FILE_PATH = "data/visitas.csv";
    private static final String CSV_HEADER = "id,pacienteId,dataHoraChegada,motivo,status,resumoAtendimento";  // ATUALIZADO TP4
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public VisitaRepository() {
        garantirDiretorioExiste();
    }

    private void garantirDiretorioExiste() {
        Path diretorio = Paths.get("data");
        if (!Files.exists(diretorio)) {
            try {
                Files.createDirectories(diretorio);
            } catch (IOException e) {
                System.err.println("Erro ao criar diretório: " + e.getMessage());
            }
        }
    }

    private String escapeCsv(String valor) {
        if (valor == null) return "";
        if (valor.contains(",") || valor.contains("\"") || valor.contains("\n")) {
            return "\"" + valor.replace("\"", "\"\"") + "\"";
        }
        return valor;
    }

    private String unescapeCsv(String valor) {
        if (valor == null || valor.isEmpty()) return "";
        if (valor.startsWith("\"") && valor.endsWith("\"")) {
            valor = valor.substring(1, valor.length() - 1);
            valor = valor.replace("\"\"", "\"");
        }
        return valor;
    }

    private void salvarTodos(List<Visita> visitas) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(FILE_PATH))) {
            writer.write(CSV_HEADER);
            writer.newLine();
            for (Visita v : visitas) {
                String linha = String.format("%d,%d,%s,%s,%s,%s",
                        v.getId(),
                        v.getPacienteId(),
                        v.getDataHoraChegada().format(FORMATTER),
                        escapeCsv(v.getMotivo()),
                        v.getStatus(),
                        escapeCsv(v.getResumoAtendimento() != null ? v.getResumoAtendimento() : ""));  // NOVO TP4
                writer.write(linha);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar: " + e.getMessage());
        }
    }

    public List<Visita> carregarTodos() {
        List<Visita> visitas = new ArrayList<>();
        Path path = Paths.get(FILE_PATH);
        if (!Files.exists(path)) return visitas;

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String linha = reader.readLine(); // Pula cabeçalho

            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;

                try {
                    List<String> campos = new ArrayList<>();
                    StringBuilder campoAtual = new StringBuilder();
                    boolean dentroAspas = false;

                    for (int i = 0; i < linha.length(); i++) {
                        char c = linha.charAt(i);

                        if (c == '"') {
                            dentroAspas = !dentroAspas;
                        } else if (c == ',' && !dentroAspas) {
                            campos.add(campoAtual.toString());
                            campoAtual = new StringBuilder();
                        } else {
                            campoAtual.append(c);
                        }
                    }
                    campos.add(campoAtual.toString());

                    if (campos.size() >= 6) {  // ATUALIZADO pro tp4 para 6 campos
                        Visita v = new Visita();
                        v.setId(Integer.parseInt(campos.get(0)));
                        v.setPacienteId(Integer.parseInt(campos.get(1)));

                        String dataStr = campos.get(2);
                        if (!dataStr.isEmpty()) {
                            v.setDataHoraChegada(LocalDateTime.parse(dataStr, FORMATTER));
                        }

                        v.setMotivo(unescapeCsv(campos.get(3)));
                        v.setStatus(campos.get(4));
                        v.setResumoAtendimento(unescapeCsv(campos.get(5)));
                        visitas.add(v);
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao processar linha: " + linha);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar: " + e.getMessage());
        }
        return visitas;
    }

    public void adicionar(Visita visita) {
        List<Visita> visitas = carregarTodos();
        int novoId = visitas.stream().mapToInt(Visita::getId).max().orElse(0) + 1;
        visita.setId(novoId);
        visitas.add(visita);
        salvarTodos(visitas);
        System.out.println("Visita adicionada com ID: " + novoId);
    }

    public void atualizar(Visita visitaAtualizada) {
        List<Visita> visitas = carregarTodos();
        visitas.removeIf(v -> v.getId() == visitaAtualizada.getId());
        visitas.add(visitaAtualizada);
        salvarTodos(visitas);
        System.out.println("Visita ID " + visitaAtualizada.getId() + " atualizada com sucesso.");
    }

    public List<Visita> buscarPorPaciente(int pacienteId) {
        List<Visita> resultado = new ArrayList<>();
        for (Visita v : carregarTodos()) {
            if (v.getPacienteId() == pacienteId) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    public Visita buscarPorId(int id) {
        for (Visita v : carregarTodos()) {
            if (v.getId() == id) {
                return v;
            }
        }
        return null;
    }

    // NOVO MÉTODO TP4
    public List<Visita> buscarPorStatus(String status) {
        List<Visita> resultado = new ArrayList<>();
        for (Visita v : carregarTodos()) {
            if (v.getStatus().equals(status)) {
                resultado.add(v);
            }
        }
        return resultado;
    }
}