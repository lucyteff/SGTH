package com.sgth.repository;

import com.sgth.model.Triagem;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TriagemRepository {
    private static final String FILE_PATH = "data/triagens.csv";
    private static final String CSV_HEADER = "id,visitaId,enfermeiroId,sinaisVitais,queixaPrincipal,alergias,prioridade,dataHoraTriagem";  // ATUALIZADO TP4
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public TriagemRepository() {
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
        // remove aspas do início e fim se existirem
        if (valor.startsWith("\"") && valor.endsWith("\"")) {
            valor = valor.substring(1, valor.length() - 1);
            // substitui aspas duplicadas por aspas simples
            valor = valor.replace("\"\"", "\"");
        }
        return valor;
    }

    private void salvarTodos(List<Triagem> triagens) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(FILE_PATH))) {
            writer.write(CSV_HEADER);
            writer.newLine();
            for (Triagem t : triagens) {
                String linha = String.format("%d,%d,%d,%s,%s,%s,%s,%s",  // ATUALIZADO TP4
                        t.getId(),
                        t.getVisitaId(),
                        t.getEnfermeiroId(),
                        escapeCsv(t.getSinaisVitais()),
                        escapeCsv(t.getQueixaPrincipal()),
                        escapeCsv(t.getAlergias()),  // NOVO TP4
                        t.getPrioridade(),
                        t.getDataHoraTriagem().format(FORMATTER));
                writer.write(linha);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar: " + e.getMessage());
        }
    }

    public List<Triagem> carregarTodos() {
        List<Triagem> triagens = new ArrayList<>();
        Path path = Paths.get(FILE_PATH);
        if (!Files.exists(path)) return triagens;

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String linha = reader.readLine(); // Pula cabeçalho

            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;

                try {
                    // parse manual do CSV respeitando aspas
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
                    campos.add(campoAtual.toString()); // Último campo

                    if (campos.size() >= 8) {  // ATUALIZADO TP4 para 8 campos
                        Triagem t = new Triagem();
                        t.setId(Integer.parseInt(campos.get(0)));
                        t.setVisitaId(Integer.parseInt(campos.get(1)));
                        t.setEnfermeiroId(Integer.parseInt(campos.get(2)));
                        t.setSinaisVitais(unescapeCsv(campos.get(3)));
                        t.setQueixaPrincipal(unescapeCsv(campos.get(4)));
                        t.setAlergias(unescapeCsv(campos.get(5)));  // NOVO TP4
                        t.setPrioridade(campos.get(6));

                        String dataStr = campos.get(7);  // ATUALIZADO TP4 índice
                        if (!dataStr.isEmpty()) {
                            t.setDataHoraTriagem(LocalDateTime.parse(dataStr, FORMATTER));
                        }

                        triagens.add(t);
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao processar linha: " + linha);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar: " + e.getMessage());
        }
        return triagens;
    }

    public void adicionar(Triagem triagem) {
        List<Triagem> triagens = carregarTodos();
        int novoId = triagens.stream().mapToInt(Triagem::getId).max().orElse(0) + 1;
        triagem.setId(novoId);
        triagens.add(triagem);
        salvarTodos(triagens);
        System.out.println("Triagem adicionada com ID: " + novoId);
    }

    public Triagem buscarPorVisitaId(int visitaId) {
        return carregarTodos().stream()
                .filter(t -> t.getVisitaId() == visitaId)
                .findFirst()
                .orElse(null);
    }
}