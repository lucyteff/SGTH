package com.sgth.repository;

import com.sgth.model.Usuario;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepository {
    private static final String FILE_PATH = "data/usuarios.csv";
    private static final String CSV_HEADER = "id,nome,login,senha,papel";

    public UsuarioRepository() {
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

    private void salvarTodos(List<Usuario> usuarios) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(FILE_PATH))) {
            writer.write(CSV_HEADER);
            writer.newLine();
            for (Usuario u : usuarios) {
                String linha = String.format("%d,%s,%s,%s,%s",
                        u.getId(), u.getNome(), u.getLogin(), u.getSenha(), u.getPapel());
                writer.write(linha);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar: " + e.getMessage());
        }
    }

    public List<Usuario> carregarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        Path path = Paths.get(FILE_PATH);
        if (!Files.exists(path)) return usuarios;

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String linha = reader.readLine(); // Pula cabeçalho
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                String[] campos = linha.split(",");
                if (campos.length >= 5) {
                    Usuario u = new Usuario();
                    u.setId(Integer.parseInt(campos[0]));
                    u.setNome(campos[1]);
                    u.setLogin(campos[2]);
                    u.setSenha(campos[3]);
                    u.setPapel(campos[4]);
                    usuarios.add(u);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar: " + e.getMessage());
        }
        return usuarios;
    }

    public void adicionar(Usuario usuario) {
        List<Usuario> usuarios = carregarTodos();
        int novoId = usuarios.stream().mapToInt(Usuario::getId).max().orElse(0) + 1;
        usuario.setId(novoId);
        usuarios.add(usuario);
        salvarTodos(usuarios);
        System.out.println("Usuário adicionado com ID: " + novoId);
    }

    public Usuario buscarPorId(int id) {
        return carregarTodos().stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Usuario buscarPorLogin(String login) {
        return carregarTodos().stream()
                .filter(u -> u.getLogin().equals(login))
                .findFirst()
                .orElse(null);
    }
}