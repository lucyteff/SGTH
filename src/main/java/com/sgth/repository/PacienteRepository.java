package com.sgth.repository;

import com.sgth.model.Paciente;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PacienteRepository {
    private static final String FILE_PATH = "data/pacientes.csv";
    private static final String CSV_HEADER = "id,nome,cpf,dataNascimento,telefone";

    public PacienteRepository() {
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

    private void salvarTodos(List<Paciente> pacientes) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(FILE_PATH))) {
            writer.write(CSV_HEADER);
            writer.newLine();
            for (Paciente p : pacientes) {
                String linha = String.format("%d,%s,%s,%s,%s",
                        p.getId(),
                        p.getNome() != null ? p.getNome() : "",
                        p.getCpf() != null ? p.getCpf() : "",
                        p.getDataNascimento() != null ? p.getDataNascimento() : "",
                        p.getTelefone() != null ? p.getTelefone() : "");
                writer.write(linha);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar: " + e.getMessage());
        }
    }

    public List<Paciente> carregarTodos() {
        List<Paciente> pacientes = new ArrayList<>();
        Path path = Paths.get(FILE_PATH);
        if (!Files.exists(path)) return pacientes;

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String linha = reader.readLine();
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                String[] campos = linha.split(",");
                if (campos.length >= 5) {
                    Paciente p = new Paciente();
                    p.setId(Integer.parseInt(campos[0]));
                    p.setNome(campos[1]);
                    p.setCpf(campos[2]);
                    if (!campos[3].isEmpty()) p.setDataNascimento(LocalDate.parse(campos[3]));
                    p.setTelefone(campos[4]);
                    pacientes.add(p);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar: " + e.getMessage());
        }
        return pacientes;
    }

    public void adicionar(Paciente paciente) {
        List<Paciente> pacientes = carregarTodos();
        int novoId = pacientes.stream().mapToInt(Paciente::getId).max().orElse(0) + 1;
        paciente.setId(novoId);
        pacientes.add(paciente);
        salvarTodos(pacientes);
        System.out.println("Paciente adicionado com ID: " + novoId);
    }

    public Paciente buscarPorId(int id) {
        return carregarTodos().stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void atualizar(Paciente pacienteAtualizado) {
        List<Paciente> pacientes = carregarTodos();
        pacientes.removeIf(p -> p.getId() == pacienteAtualizado.getId());
        pacientes.add(pacienteAtualizado);
        salvarTodos(pacientes);
    }
}