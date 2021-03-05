/**
 * Para alterar este cabeçalho de licença, escolha Cabeçalhos de licença em Propriedades do projeto.
 * Para alterar este arquivo de modelo, escolha Ferramentas | Modelos
 * e abra o modelo no editor.
 */

import java.util.Scanner;

public class Principal {

    private static Scanner entrada = new Scanner(System.in);
    private static Simulator simulator;

    private static void menu() {
        System.out.println("Por favor esocolha uma das seguintes opções: ");
        System.out.println("1 - Simular passos ");
        System.out.println("2 - Imprimir predadores e presas de um animal  ");
        System.out.println("3 - Reiniciar execução  ");
        System.out.println("4 - Redimensionar");
        System.out.println("5 - Encerrar execução");
    }

    private static void executarOpcao(int opcao) {
        switch (opcao) {
            case 1:
                simularPassos();
                break;
            case 2:
                imprimirPredadoresPresas();
                break;
            case 3:
                reiniciarExecucao();
                break;
            case 4:
                redimensionar();
                break;
            case 5: //sair
                break;
            default:
                System.out.println("Opção invalida");
        }
    }

    private static void simularPassos() {
        System.out.println("Por favor digite o número de passos: ");
        simulator.simulate(entrada.nextInt());
    }

    private static void imprimirPredadoresPresas() {
//        System.out.println("Digite o animal desejado");
//        String animal = entrada.next();
//        if (animal.equals("raposa")) {
//            Fox raposa = new Fox(true);
//            System.out.print("Predadores: ");
//            System.out.println(raposa.getPredadores());
//            System.out.print("Presas: ");
//            System.out.println(raposa.getPresas());
//        } else if (animal.equals("lobo")) {
//            Lobe lobo = new Lobe(true);
//            System.out.print("Predadores: ");
//            System.out.println(lobo.getPredadores());
//            System.out.print("Presas: ");
//            System.out.println(lobo.getPresas());
//        } else if (animal.equals("coelho")) {
//            Rabbit coelho = new Rabbit(true);
//            System.out.print("Predadores: ");
//            System.out.println(coelho.getPredadores());
//            System.out.print("Presas: ");
//            System.out.println(coelho.getPresas());
//        } else {
//            System.out.println("Animal inválido");
//        }
    }

    private static void reiniciarExecucao() {
        simulator.reset();
        simulator.runLongSimulation();
    }

    private static void redimensionar() {
        simulator.reset();
        System.out.println("Por favor digite as novas dimensões");
        simulator = new Simulator(entrada.nextInt(), entrada.nextInt());
        simulator.runLongSimulation();
    }

    /**
     * @param args os argumentos da linha de comando 
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("Por favor digite as dimensões da janela: ");
        simulator = new Simulator(entrada.nextInt(), entrada.nextInt());
        simulator.runLongSimulation();
        int opcao;

        do {
            menu();
            opcao = entrada.nextInt();
            executarOpcao(opcao);
        }while(opcao != 5);
    }
}
