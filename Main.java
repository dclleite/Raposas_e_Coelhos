/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 
 * Para alterar este cabeçalho de licença, escolha Cabeçalhos de licença em Propriedades do projeto.
 * Para alterar este arquivo de modelo, escolha Ferramentas | Modelos
 * e abra o modelo no editor.
 * / 
 */
//package raposaecoelho;

/**
 *
 * @author samir
 */
 import java.util.Scanner;
 class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner entrada = new Scanner(System.in);
        int x, y, passos;
        System.out.println("Por favor digite as dimensões: ");
        x = entrada.nextInt();
        y = entrada.nextInt();
        Simulator simulator = new Simulator(x,y);
        simulator.runLongSimulation();
        int opcao = 0;

        while(opcao != 21) {
          System.out.println("Por favor esocolha uma das seguintes opções: ");
          System.out.println("1 - Simular passos ");
          System.out.println("2 - Imprimir predadores e presas de um animal  ");
          System.out.println("3 - Reiniciar execução  ");
          System.out.println("4 - Redimensionar  ");
          System.out.println("21 - Encerrar execução  ");
          opcao = entrada.nextInt();
          if(opcao == 1){
            System.out.println("Por favor digite o número de passos: ");
            passos = entrada.nextInt();
            simulator.simulate(passos);
          }
          else if(opcao == 2){
           System.out.println("Digite o animal desejado");
           String animal = entrada.next();
           if(animal.equals("raposa")){
            Fox raposa = new Fox(true);
            System.out.print("Predadores: ");
            System.out.println(raposa.getPredadores());
            System.out.print("Presas: ");
            System.out.println(raposa.getPresas());
           } else if(animal.equals("lobo")){
            Lobe lobo = new Lobe(true);
            System.out.print("Predadores: ");
            System.out.println(lobo.getPredadores());
            System.out.print("Presas: ");
            System.out.println(lobo.getPresas());
           } else if(animal.equals("coelho")){
            Rabbit coelho = new Rabbit(true);
            System.out.print("Predadores: ");
            System.out.println(coelho.getPredadores());
            System.out.print("Presas: ");
            System.out.println(coelho.getPresas());
           } else{
              System.out.println("Animal inválido");
           }
          }
          else if(opcao == 3){
            simulator.reset();
            simulator.runLongSimulation();
          }
          else if(opcao == 4){
            simulator.reset();
            System.out.println("Por favor digite as novas dimensões  ");
            x = entrada.nextInt();
            y = entrada.nextInt();
            simulator = new Simulator(x,y);
            simulator.runLongSimulation();
          }
          else if (opcao != 21){
            System.out.println("Opcao inválida  ");
          }
        }
    }
}
