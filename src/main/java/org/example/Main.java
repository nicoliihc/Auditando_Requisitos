package org.example;
import java.io.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main{
    static String[][] logs = new String[50][2];
    static int qtdLogs = 0;

    static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);

        boolean emergencia = false;
        String statusRobo = "Aguardando";
        String luzStatus = "AMARELO";
        boolean programaHabilitado = true;
        boolean online = true;
        boolean cameraLigada = true;
        boolean gravando = true;
        String usuario = "Operador_SENAI";

        int[] pecas = {0, 0, 0, 0};
        double somaTempoCiclos = 0;

        String[] notificacoes = new String[20];
        int qtdNotificacoes = 0;

        salvarLog("Iniciando Sistema...");
        notificacoes[qtdNotificacoes++] = "Sistema iniciado!";

        int opcao = -1;

        while (opcao != 0) {
            String hora = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            System.out.println("\n======================================================================");
            System.out.println("[SENAI] Sistema de Monitoramento Integrado");
            System.out.println("Hora: " + hora + " (RF-17) | Notificações: " + qtdNotificacoes + " | User: " + usuario);
            System.out.println("======================================================================");

            System.out.println("\n--- MENU (SIDEBAR) ---");
            System.out.println("\n1 - Painel Geral");
            System.out.println("2 - Adicionar Peça Processada ");
            System.out.println("3 - Configurações");
            System.out.println("4 - Perfil e Cadastro");
            System.out.println("5 - Ver Notificações [Sino]");
            System.out.println("6 - Ler Arquivo de Logs Salvos");
            System.out.println("7 - [!] BOTÃO PARADA DE EMERGÊNCIA");
            System.out.println("0 - Sair");
            System.out.print("Opcao: ");
            opcao = entrada.nextInt();
            entrada.nextLine();

            switch (opcao) {
                case 1:
                    int totalPecas = pecas[0] + pecas[1] + pecas[2] + pecas[3];
                    int certas = pecas[0] + pecas[1] + pecas[2];
                    double cicloMedio = (totalPecas > 0) ? (somaTempoCiclos / totalPecas) : 0;
                    double taxaAcerto = (totalPecas > 0) ? ((double) certas / totalPecas) * 100 : 100;
                    double disponibilidade = emergencia ? 80.0 : 95.0;
                    double oee = disponibilidade * (taxaAcerto / 100);

                    System.out.println("\n--- PAINEL GERAL ---");
                    System.out.println("Robo: " + statusRobo + " | Luz: " + luzStatus);
                    System.out.println("Programa: " + (programaHabilitado ? "Habilitado" : "Desabilitado"));
                    System.out.println("Conexão: " + (online ? "Online" : "Offline"));

                    if (cameraLigada && online) {
                        System.out.println("Camera CAM-01 [ONLINE] " + (gravando ? "(o) REC" : "[Sem REC]"));
                        System.out.println("Visão Computacional: Processando");
                    } else {
                        System.out.println("Camera CAM-01 [OFFLINE]");
                    }

                    System.out.println("\nTotal de Peças: " + totalPecas);
                    System.out.printf("Tempo de Ciclo: %.1f seg\n", cicloMedio);
                    System.out.printf("Taxa de Acerto: %.1f%%\n", taxaAcerto);
                    System.out.printf("OEE: %.1f%% | Disponibilidade: %.1f%%\n", oee, disponibilidade);
                    System.out.println("Cores -> Azul: " + pecas[0] + " | Verde: " + pecas[1] + " | Vermelha: " + pecas[2] + " | Erro: " + pecas[3]);

                    System.out.println("\nUltimos Logs:");
                    for (int i = 0; i < qtdLogs; i++) {
                        System.out.println("[" + logs[i][0] + "] " + logs[i][1]);
                    }
                    break;

                case 2:
                    if (emergencia || !programaHabilitado || !online) {
                        System.out.println("Bloqueado! Verifique emergência, programa ou conexão.");
                    } else {
                        System.out.print("Cor da peça (1-Azul, 2-Verde, 3-Vermelha, 4-Erro): ");
                        int cor = entrada.nextInt();
                        System.out.print("Tempo do ciclo em segundos (ex: 4.2): ");
                        double tempo = entrada.nextDouble();
                        entrada.nextLine();

                        if (cor >= 1 && cor <= 4) {
                            pecas[cor - 1]++;
                            somaTempoCiclos += tempo;
                            statusRobo = "Rampa em execução";
                            luzStatus = "VERDE";
                            salvarLog("Peça registrada (opção " + cor + ") em " + tempo + "s.");
                            System.out.println("Peça adicionada!");
                        } else {
                            System.out.println("Cor inválida!");
                        }
                    }
                    break;

                case 3:
                    System.out.println("\n1 - Ligar/Desligar Programa");
                    System.out.println("2 - Alternar Online/Offline");
                    System.out.println("3 - Ligar/Desligar Camera");
                    System.out.println("4 - Ativar/Pausar REC");
                    System.out.print("Escolha: ");
                    int config = entrada.nextInt();
                    entrada.nextLine();

                    switch (config) {
                        case 1:
                            programaHabilitado =! programaHabilitado;
                            if (programaHabilitado == false){
                                System.out.println("Programa Desligado");
                            }else {
                                System.out.println("Programa Ligado");
                            }
                            break;
                        case 2:
                            online =! online;
                            if (online == false){
                                System.out.println("Offline");
                            }else {
                                System.out.println("Online");
                            }
                            break;
                        case 3:
                            cameraLigada =! cameraLigada;
                            if (cameraLigada == false){
                                System.out.println("Camêra Desligada");
                            }else {
                                System.out.println("Camêra Ligada");
                            }
                            break;
                        case 4:
                            gravando =! gravando;
                            if (gravando == false){
                                System.out.println("Gravação Desligada");
                            }else {
                                System.out.println("Gravação Ligada");
                            }
                            break;
                        default:
                            System.out.println("Opção inválida.");
                    }
                    salvarLog("Configuração alterada (opção " + config + ").");
                    System.out.println("Alterado com sucesso!");
                    break;

                case 4:
                    System.out.println("\nAvatar: [ (o_o) ] | Usuário atual: " + usuario);
                    System.out.print("Digite o novo nome do usuário: ");
                    usuario = entrada.nextLine();
                    salvarLog("Usuário alterado para " + usuario);
                    break;

                case 5:
                    System.out.println("\n--- NOTIFICAÇÕES ---");
                    for (int i = 0; i < qtdNotificacoes; i++) {
                        System.out.println("- " + notificacoes[i]);
                    }
                    break;

                case 6:
                    System.out.println("\n--- ARQUIVO DE LOGS (logs.txt) ---");
                    try (BufferedReader br = new BufferedReader(new FileReader("logs.txt"))) {
                        String linha;
                        while ((linha = br.readLine()) != null) {
                            System.out.println(linha);
                        }
                    } catch (IOException e) {
                        System.out.println("Nenhum arquivo encontrado ainda.");
                    }
                    break;

                case 7:
                    if (!emergencia) {
                        emergencia = true;
                        statusRobo = "PARADO (EMERGÊNCIA)";
                        luzStatus = "VERMELHO";
                        programaHabilitado = false;
                        salvarLog("PARADA DE EMERGÊNCIA ACIONADA!");
                        notificacoes[qtdNotificacoes++] = "ALERTA: Parada de emergência ativada!";
                        System.out.println("\n>>> [!] CÉLULA INTERROMPIDA IMEDIATAMENTE! <<<");
                    } else {
                        System.out.print("Emergência ativa. Deseja liberar o sistema? (S/N): ");
                        if (entrada.nextLine().equalsIgnoreCase("S")) {
                            emergencia = false;
                            statusRobo = "Aguardando";
                            luzStatus = "AMARELO";
                            programaHabilitado = true;
                            salvarLog("Emergencia liberada.");
                            System.out.println("Sistema liberado.");
                        }
                    }
                    break;

                case 0:
                    salvarLog("Logout realizado por " + usuario);
                    System.out.println("Sessão encerrada.");
                    break;

                default:
                    System.out.println("Opção inválida.");
            }
        }
        entrada.close();
    }

    public static void salvarLog(String evento) {
        String hora = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        if (qtdLogs < logs.length) {
            logs[qtdLogs][0] = hora;
            logs[qtdLogs][1] = evento;
            qtdLogs++;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("logs.txt", true))) {
            bw.write("[" + hora + "] " + evento);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Erro ao gravar arquivo.");
        }
    }
}