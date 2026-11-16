package com.sgth;

import com.sgth.model.*;
import com.sgth.repository.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        PacienteRepository pacienteRepo = new PacienteRepository();
        UsuarioRepository usuarioRepo = new UsuarioRepository();
        VisitaRepository visitaRepo = new VisitaRepository();
        TriagemRepository triagemRepo = new TriagemRepository();

        Scanner scanner = new Scanner(System.in);
        int opcao;

        System.out.println("=== SGTH - Sistema de Gestão de Triagem Hospitalar ===");
        System.out.println("Diretório de trabalho: " + System.getProperty("user.dir"));
        System.out.println("Arquivos CSV serão salvos em: data/");

        // Cria usuários iniciais se não existirem
        criarUsuariosIniciais(usuarioRepo);

        do {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Gerenciar Pacientes");
            System.out.println("2. Gerenciar Usuários");
            System.out.println("3. Gerenciar Visitas");
            System.out.println("4. Gerenciar Triagens");
            System.out.println("5. Painel de Atendimento (Médico)");
            System.out.println("6. Iniciar Atendimento (Médico)");  // NOVO TP4
            System.out.println("7. Finalizar Atendimento (Médico)");  // NOVO TP4
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        menuPacientes(pacienteRepo, scanner);
                        break;
                    case 2:
                        menuUsuarios(usuarioRepo, scanner);
                        break;
                    case 3:
                        menuVisitas(visitaRepo, pacienteRepo, scanner);
                        break;
                    case 4:
                        menuTriagens(triagemRepo, visitaRepo, usuarioRepo, scanner);
                        break;
                    case 5:
                        visualizarPainel(visitaRepo, triagemRepo, pacienteRepo, scanner);
                        break;
                    case 6:  // NOVO TP4
                        iniciarAtendimento(visitaRepo, triagemRepo, pacienteRepo, usuarioRepo, scanner);
                        break;
                    case 7:  // NOVO TP4
                        finalizarAtendimento(visitaRepo, triagemRepo, pacienteRepo, usuarioRepo, scanner);
                        break;
                    case 0:
                        System.out.println("Encerrando sistema...");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                System.out.println("Erro: Entrada inválida. Digite apenas números.");
                scanner.nextLine();
                opcao = -1;
            }
        } while (opcao != 0);

        scanner.close();
        System.out.println("Sistema encerrado.");
    }

    private static void criarUsuariosIniciais(UsuarioRepository repo) {
        if (repo.carregarTodos().isEmpty()) {
            System.out.println("Criando usuários iniciais...");

            Usuario atendente = new Usuario(0, "Atendente Padrão", "atendente", "123", "ATENDENTE");
            Usuario enfermeiro = new Usuario(0, "Enfermeiro Padrão", "enfermeiro", "123", "ENFERMEIRO");
            Usuario medico = new Usuario(0, "Médico Padrão", "medico", "123", "MEDICO");
            Usuario admin = new Usuario(0, "Administrador", "admin", "123", "ADMIN");

            repo.adicionar(atendente);
            repo.adicionar(enfermeiro);
            repo.adicionar(medico);
            repo.adicionar(admin);

            System.out.println("Usuários criados! Login: atendente/enfermeiro/medico/admin | Senha: 123");
        }
    }

    private static void menuPacientes(PacienteRepository repo, Scanner scanner) {
        int opcao;
        do {
            System.out.println("\n--- MENU PACIENTES ---");
            System.out.println("1. Listar Pacientes");
            System.out.println("2. Adicionar Paciente");
            System.out.println("3. Buscar Paciente por ID");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            try {
                opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        listarPacientes(repo);
                        break;
                    case 2:
                        adicionarPaciente(repo, scanner);
                        break;
                    case 3:
                        buscarPaciente(repo, scanner);
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                System.out.println("Erro: Entrada inválida.");
                scanner.nextLine();
                opcao = -1;
            }
        } while (opcao != 0);
    }

    private static void menuUsuarios(UsuarioRepository repo, Scanner scanner) {
        int opcao;
        do {
            System.out.println("\n--- MENU USUÁRIOS ---");
            System.out.println("1. Listar Usuários");
            System.out.println("2. Adicionar Usuário");
            System.out.println("3. Buscar Usuário por ID");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            try {
                opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        listarUsuarios(repo);
                        break;
                    case 2:
                        adicionarUsuario(repo, scanner);
                        break;
                    case 3:
                        buscarUsuario(repo, scanner);
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                System.out.println("Erro: Entrada inválida.");
                scanner.nextLine();
                opcao = -1;
            }
        } while (opcao != 0);
    }

    private static void menuVisitas(VisitaRepository visitaRepo, PacienteRepository pacienteRepo, Scanner scanner) {
        int opcao;
        do {
            System.out.println("\n--- MENU VISITAS ---");
            System.out.println("1. Listar Visitas");
            System.out.println("2. Registrar Nova Visita");
            System.out.println("3. Buscar Visitas por Paciente");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            try {
                opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        listarVisitas(visitaRepo);
                        break;
                    case 2:
                        registrarVisita(visitaRepo, pacienteRepo, scanner);
                        break;
                    case 3:
                        buscarVisitasPorPaciente(visitaRepo, scanner);
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                System.out.println("Erro: Entrada inválida.");
                scanner.nextLine();
                opcao = -1;
            }
        } while (opcao != 0);
    }

    private static void menuTriagens(TriagemRepository triagemRepo, VisitaRepository visitaRepo,
                                     UsuarioRepository usuarioRepo, Scanner scanner) {
        int opcao;
        do {
            System.out.println("\n--- MENU TRIAGENS ---");
            System.out.println("1. Listar Triagens");
            System.out.println("2. Realizar Nova Triagem");
            System.out.println("3. Buscar Triagem por Visita");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            try {
                opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        listarTriagens(triagemRepo);
                        break;
                    case 2:
                        realizarTriagem(triagemRepo, visitaRepo, usuarioRepo, scanner);
                        break;
                    case 3:
                        buscarTriagemPorVisita(triagemRepo, scanner);
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                System.out.println("Erro: Entrada inválida. Digite apenas números.");
                scanner.nextLine();
                opcao = -1;
            }
        } while (opcao != 0);
    }

    // ==== MÉTODOS PACIENTES ====
    private static void listarPacientes(PacienteRepository repo) {
        System.out.println("\n--- Lista de Pacientes ---");
        try {
            List<Paciente> pacientes = repo.carregarTodos();
            if (pacientes.isEmpty()) {
                System.out.println("Nenhum paciente cadastrado.");
            } else {
                System.out.println("Total: " + pacientes.size());
                for (Paciente p : pacientes) {
                    System.out.println(p);
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar pacientes: " + e.getMessage());
        }
    }

    private static void adicionarPaciente(PacienteRepository repo, Scanner scanner) {
        System.out.println("\n--- Novo Paciente ---");
        try {
            Paciente p = new Paciente();
            System.out.print("Nome: ");
            p.setNome(scanner.nextLine());
            System.out.print("CPF: ");
            p.setCpf(scanner.nextLine());
            System.out.print("Data Nascimento (AAAA-MM-DD): ");
            String dataStr = scanner.nextLine();
            if (!dataStr.isEmpty()) {
                p.setDataNascimento(LocalDate.parse(dataStr));
            }
            System.out.print("Telefone: ");
            p.setTelefone(scanner.nextLine());
            repo.adicionar(p);
            System.out.println("Paciente cadastrado com sucesso!");
        } catch (DateTimeParseException e) {
            System.out.println("Erro: Data inválida! Use o formato AAAA-MM-DD");
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar paciente: " + e.getMessage());
        }
    }

    private static void buscarPaciente(PacienteRepository repo, Scanner scanner) {
        System.out.print("\nID do paciente: ");
        try {
            int id = scanner.nextInt();
            scanner.nextLine();
            Paciente p = repo.buscarPorId(id);
            if (p != null) {
                System.out.println("\n=== DADOS DO PACIENTE ===");
                System.out.println("ID: " + p.getId());
                System.out.println("Nome: " + p.getNome());
                System.out.println("CPF: " + p.getCpf());
                System.out.println("Nascimento: " + p.getDataNascimento());
                System.out.println("Telefone: " + p.getTelefone());
            } else {
                System.out.println("Paciente não encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Erro: ID inválido.");
            scanner.nextLine();
        }
    }

    // === MÉTODOS USUÁRIOS ==
    private static void listarUsuarios(UsuarioRepository repo) {
        System.out.println("\n--- Lista de Usuários ---");
        try {
            List<Usuario> usuarios = repo.carregarTodos();
            if (usuarios.isEmpty()) {
                System.out.println("Nenhum usuário cadastrado.");
            } else {
                System.out.println("Total: " + usuarios.size());
                for (Usuario u : usuarios) {
                    System.out.println(u);
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar usuários: " + e.getMessage());
        }
    }

    private static void adicionarUsuario(UsuarioRepository repo, Scanner scanner) {
        System.out.println("\n--- Novo Usuário ---");
        try {
            Usuario u = new Usuario();
            System.out.print("Nome: ");
            u.setNome(scanner.nextLine());
            System.out.print("Login: ");
            u.setLogin(scanner.nextLine());
            System.out.print("Senha: ");
            u.setSenha(scanner.nextLine());
            System.out.print("Papel (ATENDENTE/ENFERMEIRO/MEDICO/ADMIN): ");
            u.setPapel(scanner.nextLine().toUpperCase());
            repo.adicionar(u);
            System.out.println("Usuário cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }

    private static void buscarUsuario(UsuarioRepository repo, Scanner scanner) {
        System.out.print("\nID do usuário: ");
        try {
            int id = scanner.nextInt();
            scanner.nextLine();
            Usuario u = repo.buscarPorId(id);
            if (u != null) {
                System.out.println("\n=== DADOS DO USUÁRIO ===");
                System.out.println("ID: " + u.getId());
                System.out.println("Nome: " + u.getNome());
                System.out.println("Login: " + u.getLogin());
                System.out.println("Papel: " + u.getPapel());
            } else {
                System.out.println("Usuário não encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Erro: ID inválido.");
            scanner.nextLine();
        }
    }

    // ==== MÉTODOS VISITAS ====
    private static void listarVisitas(VisitaRepository repo) {
        System.out.println("\n--- Lista de Visitas ---");
        try {
            List<Visita> visitas = repo.carregarTodos();
            if (visitas.isEmpty()) {
                System.out.println("Nenhuma visita registrada.");
            } else {
                System.out.println("Total: " + visitas.size());
                for (Visita v : visitas) {
                    System.out.println(v);
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar visitas: " + e.getMessage());
        }
    }

    private static void registrarVisita(VisitaRepository visitaRepo, PacienteRepository pacienteRepo, Scanner scanner) {
        System.out.println("\n--- Nova Visita ---");

        try {
            List<Paciente> pacientes = pacienteRepo.carregarTodos();
            if (pacientes.isEmpty()) {
                System.out.println("ERRO: Cadastre um paciente primeiro!");
                return;
            }

            System.out.println("\nPacientes disponíveis:");
            for (Paciente p : pacientes) {
                System.out.println(p.getId() + " - " + p.getNome());
            }

            System.out.print("\nID do paciente: ");
            int pacienteId = scanner.nextInt();
            scanner.nextLine();

            Paciente paciente = pacienteRepo.buscarPorId(pacienteId);
            if (paciente == null) {
                System.out.println("Paciente não encontrado!");
                return;
            }

            Visita v = new Visita();
            v.setPacienteId(pacienteId);
            v.setDataHoraChegada(LocalDateTime.now());
            System.out.print("Motivo da visita: ");
            v.setMotivo(scanner.nextLine());
            v.setStatus("AGUARDANDO_TRIAGEM");
            v.setResumoAtendimento("");  // Inicia vazio

            visitaRepo.adicionar(v);
            System.out.println("Visita registrada com sucesso! Paciente aguardando triagem.");
        } catch (Exception e) {
            System.out.println("Erro ao registrar visita: " + e.getMessage());
            scanner.nextLine();
        }
    }

    private static void buscarVisitasPorPaciente(VisitaRepository repo, Scanner scanner) {
        System.out.print("\nID do paciente: ");
        try {
            int pacienteId = scanner.nextInt();
            scanner.nextLine();

            List<Visita> visitas = repo.buscarPorPaciente(pacienteId);
            if (visitas.isEmpty()) {
                System.out.println("Nenhuma visita encontrada para este paciente.");
            } else {
                System.out.println("\n=== VISITAS DO PACIENTE ===");
                for (Visita v : visitas) {
                    System.out.println("ID: " + v.getId() +
                            " | Data: " + v.getDataHoraChegada() +
                            " | Status: " + v.getStatus());
                }
            }
        } catch (Exception e) {
            System.out.println("Erro: ID inválido.");
            scanner.nextLine();
        }
    }

    // === MÉTODOS TRIAGENS ===
    private static void listarTriagens(TriagemRepository repo) {
        System.out.println("\n--- Lista de Triagens ---");
        try {
            List<Triagem> triagens = repo.carregarTodos();
            if (triagens.isEmpty()) {
                System.out.println("Nenhuma triagem registrada.");
            } else {
                System.out.println("Total: " + triagens.size());
                for (Triagem t : triagens) {
                    System.out.println("ID: " + t.getId() +
                            " | Visita ID: " + t.getVisitaId() +
                            " | Prioridade: " + t.getPrioridade() +
                            " | Data: " + t.getDataHoraTriagem());
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar triagens: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void realizarTriagem(TriagemRepository triagemRepo, VisitaRepository visitaRepo,
                                        UsuarioRepository usuarioRepo, Scanner scanner) {
        System.out.println("\n--- Nova Triagem ---");

        try {
            List<Visita> visitasAguardando = new java.util.ArrayList<>();
            for (Visita v : visitaRepo.carregarTodos()) {
                if ("AGUARDANDO_TRIAGEM".equals(v.getStatus())) {
                    visitasAguardando.add(v);
                }
            }

            if (visitasAguardando.isEmpty()) {
                System.out.println("Nenhuma visita aguardando triagem.");
                return;
            }

            System.out.println("\nVisitas aguardando triagem:");
            for (Visita v : visitasAguardando) {
                System.out.println(v.getId() + " - Visita ID: " + v.getId() +
                        " | Paciente ID: " + v.getPacienteId());
            }

            System.out.print("\nID da visita: ");
            int visitaId = scanner.nextInt();
            scanner.nextLine();

            Visita visita = visitaRepo.buscarPorId(visitaId);
            if (visita == null || !"AGUARDANDO_TRIAGEM".equals(visita.getStatus())) {
                System.out.println("Visita inválida ou não está aguardando triagem!");
                return;
            }

            List<Usuario> enfermeiros = new java.util.ArrayList<>();
            for (Usuario u : usuarioRepo.carregarTodos()) {
                if ("ENFERMEIRO".equals(u.getPapel())) {
                    enfermeiros.add(u);
                }
            }

            if (enfermeiros.isEmpty()) {
                System.out.println("Nenhum enfermeiro cadastrado!");
                return;
            }

            System.out.println("\nEnfermeiros disponíveis:");
            for (Usuario e : enfermeiros) {
                System.out.println(e.getId() + " - " + e.getNome());
            }

            System.out.print("ID do enfermeiro: ");
            int enfermeiroId = scanner.nextInt();
            scanner.nextLine();

            Usuario enfermeiro = usuarioRepo.buscarPorId(enfermeiroId);
            if (enfermeiro == null || !"ENFERMEIRO".equals(enfermeiro.getPapel())) {
                System.out.println("Enfermeiro inválido!");
                return;
            }

            Triagem t = new Triagem();
            t.setVisitaId(visitaId);
            t.setEnfermeiroId(enfermeiroId);

            System.out.print("Sinais vitais (PA, temperatura, etc): ");
            t.setSinaisVitais(scanner.nextLine());
            System.out.print("Queixa principal: ");
            t.setQueixaPrincipal(scanner.nextLine());

            // NOVO CAMPO TP4
            System.out.print("Alergias conhecidas: ");
            t.setAlergias(scanner.nextLine());

            System.out.print("Prioridade (VERMELHO/LARANJA/AMARELO/VERDE/AZUL): ");
            String prioridade = scanner.nextLine().toUpperCase();

            if (!prioridade.matches("VERMELHO|LARANJA|AMARELO|VERDE|AZUL")) {
                System.out.println("Prioridade inválida!");
                return;
            }

            t.setPrioridade(prioridade);
            t.setDataHoraTriagem(LocalDateTime.now());

            triagemRepo.adicionar(t);

            visita.setStatus("AGUARDANDO_ATENDIMENTO");
            visitaRepo.atualizar(visita);

            if ("VERMELHO".equals(prioridade)) {
                System.out.println("\nALERTA: PACIENTE CRÍTICO (VERMELHO)");
                System.out.println("Notificação enviada para a equipe médica!");
                System.out.println("Prioridade máxima - Atendimento imediato necessário!\n");
            }

            System.out.println("Triagem concluída com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao realizar triagem: " + e.getMessage());
            scanner.nextLine();
        }
    }

    private static void buscarTriagemPorVisita(TriagemRepository repo, Scanner scanner) {
        System.out.print("\nID da visita: ");
        try {
            int visitaId = scanner.nextInt();
            scanner.nextLine();

            Triagem t = repo.buscarPorVisitaId(visitaId);
            if (t != null) {
                System.out.println("\n=== DADOS DA TRIAGEM ===");
                System.out.println("ID: " + t.getId());
                System.out.println("Visita ID: " + t.getVisitaId());
                System.out.println("Enfermeiro ID: " + t.getEnfermeiroId());
                System.out.println("Sinais Vitais: " + t.getSinaisVitais());
                System.out.println("Queixa: " + t.getQueixaPrincipal());
                System.out.println("Alergias: " + t.getAlergias());  // NOVO TP4
                System.out.println("Prioridade: " + t.getPrioridade());
                System.out.println("Data/Hora: " + t.getDataHoraTriagem());
            } else {
                System.out.println("Triagem não encontrada para esta visita.");
            }
        } catch (Exception e) {
            System.out.println("Erro: ID inválido.");
            scanner.nextLine();
        }
    }

    // ==== MÉTODO DO PAINEL (UC06) ====
    private static void visualizarPainel(VisitaRepository visitaRepo, TriagemRepository triagemRepo,
                                         PacienteRepository pacienteRepo, Scanner scanner) {
        System.out.println("\n=== PAINEL DE ATENDIMENTO - PACIENTES TRIADOS ===\n");

        try {
            List<Visita> visitasTriadas = new java.util.ArrayList<>();
            for (Visita v : visitaRepo.carregarTodos()) {
                if ("AGUARDANDO_ATENDIMENTO".equals(v.getStatus())) {
                    visitasTriadas.add(v);
                }
            }

            if (visitasTriadas.isEmpty()) {
                System.out.println("Nenhum paciente aguardando atendimento.");
                System.out.println("\nPressione Enter para voltar...");
                scanner.nextLine();
                return;
            }

            System.out.println("Pacientes aguardando atendimento (ordenados por prioridade):\n");

            String[] ordemPrioridade = {"VERMELHO", "LARANJA", "AMARELO", "VERDE", "AZUL"};

            for (String prioridade : ordemPrioridade) {
                boolean temPaciente = false;
                for (Visita v : visitasTriadas) {
                    Triagem t = triagemRepo.buscarPorVisitaId(v.getId());
                    if (t != null && prioridade.equals(t.getPrioridade())) {
                        if (!temPaciente) {
                            System.out.println(prioridade + ":");
                            temPaciente = true;
                        }
                        Paciente p = pacienteRepo.buscarPorId(v.getPacienteId());
                        String nomePaciente = (p != null) ? p.getNome() : "ID: " + v.getPacienteId();
                        System.out.println("   • Visita " + v.getId() + " - " + nomePaciente +
                                " | Alergias: " + t.getAlergias());  // NOVO TP4
                    }
                }
            }

            System.out.println("\nPressione Enter para voltar...");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Erro ao carregar painel: " + e.getMessage());
            scanner.nextLine();
        }
    }

    // NOVO MÉTODO TP4 - UC13 Iniciar Atendimento
    private static void iniciarAtendimento(VisitaRepository visitaRepo, TriagemRepository triagemRepo,
                                           PacienteRepository pacienteRepo, UsuarioRepository usuarioRepo,
                                           Scanner scanner) {
        System.out.println("\n=== INICIAR ATENDIMENTO MÉDICO ===\n");

        try {
            // Busca visitas aguardando atendimento (já triadas)
            List<Visita> visitasAguardando = new java.util.ArrayList<>();
            for (Visita v : visitaRepo.carregarTodos()) {
                if ("AGUARDANDO_ATENDIMENTO".equals(v.getStatus())) {
                    visitasAguardando.add(v);
                }
            }

            if (visitasAguardando.isEmpty()) {
                System.out.println("Nenhum paciente aguardando atendimento.");
                System.out.println("\nPressione Enter para voltar...");
                scanner.nextLine();
                return;
            }

            // Mostra pacientes aguardando (ordenados por prioridade)
            System.out.println("Pacientes aguardando atendimento:\n");

            String[] ordemPrioridade = {"VERMELHO", "LARANJA", "AMARELO", "VERDE", "AZUL"};

            for (String prioridade : ordemPrioridade) {
                for (Visita v : visitasAguardando) {
                    Triagem t = triagemRepo.buscarPorVisitaId(v.getId());
                    if (t != null && prioridade.equals(t.getPrioridade())) {
                        Paciente p = pacienteRepo.buscarPorId(v.getPacienteId());
                        String nomePaciente = (p != null) ? p.getNome() : "ID: " + v.getPacienteId();
                        System.out.println("Visita " + v.getId() + " - " + nomePaciente +
                                " | Prioridade: " + prioridade +
                                " | Alergias: " + t.getAlergias());
                    }
                }
            }

            // Selecionar visita para atender
            System.out.print("\nID da visita que será atendida: ");
            int visitaId = scanner.nextInt();
            scanner.nextLine();

            Visita visita = visitaRepo.buscarPorId(visitaId);
            if (visita == null || !"AGUARDANDO_ATENDIMENTO".equals(visita.getStatus())) {
                System.out.println("Visita inválida ou não está aguardando atendimento!");
                return;
            }

            // Verificar se é médico (simulação - qualquer usuário pode "ser médico" aqui)
            System.out.print("ID do médico responsável: ");
            int medicoId = scanner.nextInt();
            scanner.nextLine();

            Usuario medico = usuarioRepo.buscarPorId(medicoId);
            if (medico == null) {
                System.out.println("Usuário não encontrado!");
                return;
            }

            if (!"MEDICO".equals(medico.getPapel())) {
                System.out.println("Acesso negado! Apenas médicos podem iniciar atendimento.");
                return;
            }

            // Iniciar atendimento
            visita.setStatus("EM_ATENDIMENTO");
            visitaRepo.atualizar(visita);

            System.out.println("\n=== ATENDIMENTO INICIADO ===");
            System.out.println("Médico: " + medico.getNome());

            // Mostrar dados completos do paciente
            Paciente p = pacienteRepo.buscarPorId(visita.getPacienteId());
            Triagem t = triagemRepo.buscarPorVisitaId(visitaId);

            System.out.println("\n--- DADOS DO PACIENTE ---");
            System.out.println("Paciente: " + p.getNome());
            System.out.println("CPF: " + p.getCpf());
            System.out.println("Telefone: " + p.getTelefone());
            System.out.println("\n--- DADOS DA TRIAGEM ---");
            System.out.println("Sinais Vitais: " + t.getSinaisVitais());
            System.out.println("Queixa: " + t.getQueixaPrincipal());
            System.out.println("Alergias: " + t.getAlergias());
            System.out.println("Prioridade: " + t.getPrioridade());
            System.out.println("\nPaciente em atendimento. Use a opção 7 para finalizar.");

        } catch (Exception e) {
            System.out.println("Erro ao iniciar atendimento: " + e.getMessage());
            scanner.nextLine();
        }
    }

    // NOVO MÉTODO TP4 - UC12 Finalizar Atendimento
    private static void finalizarAtendimento(VisitaRepository visitaRepo, TriagemRepository triagemRepo,
                                             PacienteRepository pacienteRepo, UsuarioRepository usuarioRepo,
                                             Scanner scanner) {
        System.out.println("\n=== FINALIZAR ATENDIMENTO ===\n");

        try {
            // Busca visitas em atendimento
            List<Visita> visitasEmAtendimento = new java.util.ArrayList<>();
            for (Visita v : visitaRepo.carregarTodos()) {
                if ("EM_ATENDIMENTO".equals(v.getStatus())) {
                    visitasEmAtendimento.add(v);
                }
            }

            if (visitasEmAtendimento.isEmpty()) {
                System.out.println("Nenhum paciente em atendimento no momento.");
                System.out.println("\nPressione Enter para voltar...");
                scanner.nextLine();
                return;
            }

            System.out.println("Pacientes em atendimento:");
            for (Visita v : visitasEmAtendimento) {
                Paciente p = pacienteRepo.buscarPorId(v.getPacienteId());
                String nomePaciente = (p != null) ? p.getNome() : "ID: " + v.getPacienteId();
                System.out.println("Visita " + v.getId() + " - " + nomePaciente);
            }

            System.out.print("\nID da visita que será finalizada: ");
            int visitaId = scanner.nextInt();
            scanner.nextLine();

            Visita visita = visitaRepo.buscarPorId(visitaId);
            if (visita == null || !"EM_ATENDIMENTO".equals(visita.getStatus())) {
                System.out.println("Visita inválida ou não está em atendimento!");
                return;
            }

            System.out.print("Resumo do atendimento / Prescrição: ");
            String resumo = scanner.nextLine();

            // Atualizar status para FINALIZADO e salvar resumo
            visita.setStatus("FINALIZADO");
            visita.setResumoAtendimento(resumo);
            visitaRepo.atualizar(visita);

            System.out.println("\n=== ATENDIMENTO FINALIZADO ===");
            System.out.println("Resumo: " + resumo);
            System.out.println("Paciente liberado com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao finalizar atendimento: " + e.getMessage());
            scanner.nextLine();
        }
    }
}