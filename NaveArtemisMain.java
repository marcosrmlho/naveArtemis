import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NaveArtemisMain {

    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println(" INICIANDO DIAGNOSTICO DE PRE-VOO... ");
        System.out.println("=========================================");

        List<String> falhas = new ArrayList<>();

        if (!testarTanqueCombustivel()) falhas.add("01: TanqueCombustivel");
        if (!testarMotorPropulsao()) falhas.add("02: MotorPropulsao");
        if (!testarCanhaoLaser()) falhas.add("03: CanhaoLaser");

        if (!falhas.isEmpty()) {
            System.out.println("ERRO CRITICO! Lançamento abortado! As seguintes classes falharam nos testes:");
            for (String falha : falhas) {
                System.out.println(falha);
            }
            System.out.println("Corrijam a lógica, garantam o encapsulamento e tentem novamente.");
            return;
        }

        System.out.println("[SUCESSO] Todos os módulos operacionais!");
        System.out.println("Iniciando interface de comando da Nave Artemis...");

        iniciarTerminalDaNave();
    }

    public static boolean testarTanqueCombustivel() {
        try {
            TanqueCombustivel tanque = new TanqueCombustivel("PLASMA");
            if (tanque.getVolumeAtual() != 1000.0) throw new Exception("Volume inicial nao e 1000.0.");

            tanque.consumir(500.0);
            if (tanque.getVolumeAtual() != 500.0) throw new Exception("Erro no calculo de consumo normal.");

            // Sobrecarga: consumir() sem argumento usa o valor padrao (15.0)
            tanque.consumir();
            if (tanque.getVolumeAtual() != 485.0) throw new Exception("consumir() [sobrecarga] nao usou o valor padrao (15.0).");

            tanque.consumir(1000.0);
            if (tanque.getVolumeAtual() != 0 || !tanque.verificarVazamento()) {
                throw new Exception("Nao zerou o tanque nem ativou vazamento ao sobrecarregar o consumo.");
            }

            tanque.injetarCombustivel(100.0);
            if (tanque.getVolumeAtual() != 50.0) throw new Exception("Com vazamento detectado, a injecao nao reduziu para 50%.");

            // Sobrescrita: equals(Object) precisa reconhecer tanques do mesmo tipo, e rejeitar outros tipos.
            TanqueCombustivel tanqueIgual = new TanqueCombustivel("PLASMA");
            if (!tanque.equals(tanqueIgual)) throw new Exception("equals(Object) nao reconheceu tanques do mesmo tipo como iguais.");
            if (tanque.equals("PLASMA")) throw new Exception("equals(Object) deveria retornar false para um objeto de outro tipo.");

            System.out.println("TanqueCombustivel.... OK");
            return true;
        } catch (Exception e) {
            System.out.println("TanqueCombustivel.... FALHA: " + e.getMessage());
            return false;
        }
    }

    public static boolean testarMotorPropulsao() {
        try {
            MotorPropulsao motor = new MotorPropulsao("M1");
            TanqueCombustivel tanque = new TanqueCombustivel("T1");

            motor.acelerar(tanque);
            if (motor.getPotenciaAtual() != 20 || tanque.getVolumeAtual() != 985.0) {
                throw new Exception("acelerar(tanque) nao subiu a potencia em 20 ou nao gastou 15.0 de combustivel.");
            }

            // Sobrecarga: acelerar com incremento customizado (proporcao 0.75 de combustivel por ponto de potencia)
            motor.acelerar(tanque, 40);
            if (motor.getPotenciaAtual() != 60 || tanque.getVolumeAtual() != 955.0) {
                throw new Exception("acelerar(tanque, incremento) [sobrecarga] nao calculou corretamente potencia/combustivel.");
            }

            motor.setPotenciaAtual(100);
            motor.avaliarSuperaquecimento();
            if (!motor.isSuperaquecido() || motor.getPotenciaAtual() != 50) {
                throw new Exception("avaliarSuperaquecimento() nao ativou o alerta ou nao reduziu a potencia pela metade.");
            }

            motor.acionarResfriamentoEmergencia();
            if (motor.isSuperaquecido() || motor.getPotenciaAtual() != 0) {
                throw new Exception("acionarResfriamentoEmergencia() nao resetou o motor corretamente.");
            }

            // Sobrescrita: equals(Object) precisa reconhecer motores com o mesmo idMotor, e rejeitar outros tipos.
            MotorPropulsao motorIgual = new MotorPropulsao("M1");
            if (!motor.equals(motorIgual)) throw new Exception("equals(Object) nao reconheceu motores com o mesmo idMotor como iguais.");
            if (motor.equals(tanque)) throw new Exception("equals(Object) deveria retornar false para um objeto de outro tipo.");

            System.out.println("MotorPropulsao....... OK");
            return true;
        } catch (Exception e) {
            System.out.println("MotorPropulsao....... FALHA: " + e.getMessage());
            return false;
        }
    }

    public static boolean testarCanhaoLaser() {
        try {
            CanhaoLaser canhao = new CanhaoLaser("LASER-1");
            TanqueCombustivel tanque = new TanqueCombustivel("T1");
            MotorPropulsao motor = new MotorPropulsao("M1");

            canhao.carregarArma(tanque);
            canhao.carregarArma(tanque);
            canhao.carregarArma(tanque);
            if (canhao.getCargaArma() != 60.0 || !canhao.isProntoParaDisparo()) {
                throw new Exception("carregarArma nao acumulou carga ou nao liberou o disparo apos passar de 50.0.");
            }

            canhao.atirar(motor);
            if (canhao.getCargaArma() != 0.0 || canhao.isProntoParaDisparo()) {
                throw new Exception("atirar(motor) nao debitou a carga ou nao desarmou o canhao ao cair abaixo de 50.");
            }

            // Recarrega bastante e tenta uma rajada maior do que a carga permite
            for (int i = 0; i < 5; i++) canhao.carregarArma(tanque);
            // Sobrecarga: atirar em rajada - pede 3 tiros, mas so ha carga para 2
            canhao.atirar(motor, 3);
            if (canhao.isProntoParaDisparo() || canhao.getCargaArma() != 0.0) {
                throw new Exception("atirar(motor, quantidade) [sobrecarga] nao interrompeu a rajada corretamente ao ficar sem carga.");
            }

            // Motor superaquecido deve bloquear o disparo, mesmo com o canhao pronto
            motor.setSuperaquecido(true);
            canhao.setCargaArma(100.0);
            canhao.setProntoParaDisparo(true);
            double cargaAntes = canhao.getCargaArma();
            canhao.atirar(motor);
            if (canhao.getCargaArma() != cargaAntes) {
                throw new Exception("atirar nao bloqueou o disparo com o motor superaquecido.");
            }

            // Sobrescrita: equals(Object) precisa reconhecer canhoes com o mesmo idCanhao, e rejeitar outros tipos.
            CanhaoLaser canhaoIgual = new CanhaoLaser("LASER-1");
            if (!canhao.equals(canhaoIgual)) throw new Exception("equals(Object) nao reconheceu canhoes com o mesmo idCanhao como iguais.");
            if (canhao.equals(motor)) throw new Exception("equals(Object) deveria retornar false para um objeto de outro tipo.");

            System.out.println("CanhaoLaser........... OK");
            return true;
        } catch (Exception e) {
            System.out.println("CanhaoLaser........... FALHA: " + e.getMessage());
            return false;
        }
    }

    public static void iniciarTerminalDaNave() {
        Scanner scanner = new Scanner(System.in);

        TanqueCombustivel tanque = new TanqueCombustivel("PLASMA-PROTIO");
        MotorPropulsao motor = new MotorPropulsao("MOTOR-ION");
        CanhaoLaser canhao = new CanhaoLaser("LASER-DEFESA");

        boolean executando = true;

        while (executando) {
            System.out.println("================================================");
            System.out.println(" PAINEL DE CONTROLE - NAVE ARTEMIS (EM VOO)");
            System.out.println(" Combustivel: " + tanque.getVolumeAtual() + "L | " + motor.toString());
            System.out.println(" " + canhao.toString());
            System.out.println("================================================");
            System.out.println("1. Acelerar Nave (incremento padrao +20)");
            System.out.println("2. Acelerar Nave com incremento customizado (sobrecarga)");
            System.out.println("3. Avaliar Superaquecimento do Motor");
            System.out.println("4. Acionar Resfriamento de Emergencia");
            System.out.println("5. Carregar Arma do Canhao Laser (usa combustivel)");
            System.out.println("6. Atirar (disparo unico)");
            System.out.println("7. Atirar em rajada (sobrecarga - escolher quantidade)");
            System.out.println("8. Desarmar Canhao");
            System.out.println("9. Comparar dois motores (demonstra equals sobrescrito)");
            System.out.println("0. Desligar Sistema e Encerrar Aula");
            System.out.print("Comando: ");

            try {
                int opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1:
                        System.out.println(">> Acionando propulsores (incremento padrao)...");
                        motor.acelerar(tanque);
                        System.out.println(motor.toString());
                        break;
                    case 2:
                        System.out.print(">> Digite o incremento de potencia desejado: ");
                        int incremento = Integer.parseInt(scanner.nextLine());
                        motor.acelerar(tanque, incremento);
                        System.out.println(motor.toString());
                        break;
                    case 3:
                        System.out.println(">> Avaliando estabilidade termica do motor...");
                        motor.avaliarSuperaquecimento();
                        System.out.println(motor.toString());
                        break;
                    case 4:
                        motor.acionarResfriamentoEmergencia();
                        System.out.println(motor.toString());
                        break;
                    case 5:
                        System.out.println(">> Carregando arma do combustivel do tanque...");
                        canhao.carregarArma(tanque);
                        System.out.println(canhao.toString());
                        break;
                    case 6:
                        canhao.atirar(motor);
                        System.out.println(canhao.toString());
                        break;
                    case 7:
                        System.out.print(">> Quantos tiros disparar na rajada? ");
                        int quantidade = Integer.parseInt(scanner.nextLine());
                        canhao.atirar(motor, quantidade);
                        System.out.println(canhao.toString());
                        break;
                    case 8:
                        canhao.desarmar();
                        System.out.println(canhao.toString());
                        break;
                    case 9:
                        System.out.print(">> Digite o ID do segundo motor para comparar com \"" + motor.getIdMotor() + "\": ");
                        String outroId = scanner.nextLine();
                        MotorPropulsao outroMotor = new MotorPropulsao(outroId);
                        System.out.println(">> motor.equals(outroMotor) = " + motor.equals(outroMotor));
                        break;
                    case 0:
                        System.out.println("Desligando sistemas. Pouso autorizado.");
                        executando = false;
                        break;
                    default:
                        System.out.println("Comando invalido no painel de voo!");
                }
            } catch (Exception e) {
                System.out.println("[ERRO DE OPERACAO] Entrada invalida. Tente novamente.");
            }
        }
        scanner.close();
    }
}