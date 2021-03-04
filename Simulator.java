import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import java.awt.Color;

/**
 * A simple predator-prey simulator, based on a field containing
 * rabbits and foxes.
 * <p>
 * Um simulador de presa-predador simples, baseado em um campo contendo
 * coelhos e raposas.
 *
 * @author David J. Barnes and Michael Kolling
 * @version 2002-04-09
 */
public class Simulator {
    // The private static final variables represent // As variáveis finais estáticas privadas representam
    // configuration information for the simulation. // informações de configuração para a simulação.
    // The default width for the grid. // A largura padrão da grade. 
    private static final int DEFAULT_WIDTH = 50;
    // The default depth of the grid. // A profundidade padrão da grade. 
    private static final int DEFAULT_DEPTH = 50;
    // The probability that a fox will be created in any given grid position. 
    // A probabilidade de que uma raposa seja criada em qualquer posição da grade. 
    private static final double FOX_CREATION_PROBABILITY = 0.03;
    // The probability that a rabbit will be created in any given grid position.
    // A probabilidade de que um coelho seja criado em qualquer posição da grade. 
    private static final double RABBIT_CREATION_PROBABILITY = 0.08;


    //Inclusão do animal predador Lobo
    private static final double LOBE_CREATION_PROBABILITY = 0.01; //A probabilidade de que um lobo seja criada em qualquer posição da grade. 


    // The list of animals in the field // A lista de animais no campo 
    private List animals;
    // The list of animals just born // A lista de animais recém-nascidos 
    private List newAnimals;
    // The current state of the field. // O estado atual do campo. 
    private Field field;
    // A second field, used to build the next stage of the simulation. 
    // Um segundo campo, usado para construir o próximo estágio da simulação. 
    private Field updatedField;
    // The current step of the simulation. // A etapa atual da simulação. 
    private int step;
    // A graphical view of the simulation. // Uma visão gráfica da simulação. 
    private SimulatorView view;

    /**
     * Construct a simulation field with default size.
     * Construa um campo de simulação com tamanho padrão.
     */
    public Simulator() {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }

    /**
     * Create a simulation field with the given size.
     *
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     *              <p>
     *              Crie um campo de simulação com o tamanho determinado.
     * @param depth Profundidade do campo. Deve ser maior que zero.
     * @param width Largura do campo. Deve ser maior que zero.
     */
    public Simulator(int depth, int width) {
        if (width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        animals = new ArrayList();
        newAnimals = new ArrayList();
        field = new Field(depth, width);
        updatedField = new Field(depth, width);

        // Create a view of the state of each location in the field.
        // Configure um ponto de partida válido. 
        view = new SimulatorView(depth, width);
        view.setColor(Fox.class, Color.blue);
        view.setColor(Rabbit.class, Color.orange);

        view.setColor(Lobe.class, Color.green);

        // Setup a valid starting point.
        // Configure um ponto de partida válido. 
        reset();
    }

    /**
     * Run the simulation from its current state for a reasonably long period,
     * e.g. 500 steps.
     * <p>
     * Execute a simulação de seu estado atual por um período razoavelmente longo,
     * por exemplo. 500 passos.
     */
    public void runLongSimulation() {
        simulate(500);
    }

    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * <p>
     * Execute a simulação de seu estado atual para um determinado número de etapas.
     * Pare antes de um determinado número de etapas se ele deixar de ser viável.
     */
    public void simulate(int numSteps) {
        for (int step = 1; step <= numSteps && view.isViable(field); step++) {
            simulateOneStep();
        }
    }

    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each
     * fox and rabbit.
     * <p>
     * Execute a simulação de seu estado atual para uma única etapa.
     * Repita em todo o campo atualizando o estado de cada
     * raposa e coelho.
     */
    public void simulateOneStep() {
        step++;
        newAnimals.clear();

        // let all animals act
        // deixe todos os animais agirem 
        for (Iterator iter = animals.iterator(); iter.hasNext(); ) {
            Object animal = iter.next();
            if (animal instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) animal;
                double breed = rabbit.getBreed();
                Estacao inv = new Estacao();
                breed = inv.getNovoBreed(breed);
                rabbit.setBreed(breed);
                if (rabbit.isAlive()) {
                    rabbit.run(updatedField, newAnimals);
                } else {
                    iter.remove();   // remove dead rabbits from collection. // remova coelhos mortos da coleção 
                }
            } else if (animal instanceof Fox) {
                  Fox fox = (Fox) animal;
                  double breed = fox.getBreed();
                  Estacao inv = new Estacao();
                  breed = inv.getNovoBreed(breed);
                  fox.setBreed(breed);
                  int food = fox.getFood();
                  food = inv.getNovoFood(food);
                  fox.setFood(food); 
                if (fox.isAlive()) {
                    fox.hunt(field, updatedField, newAnimals);
                } else {
                    iter.remove();   // remove dead foxes from collection. // remova raposas mortas da coleção 
                }
            } else if (animal instanceof Lobe) {
                Lobe lobe = (Lobe) animal;
                double breed = lobe.getBreed();
                Estacao inv = new Estacao();
                breed = inv.getNovoBreed(breed);
                lobe.setBreed(breed);
                int food = lobe.getFood();
                food = inv.getNovoFood(food);
                lobe.setFood(food);
                if (lobe.isAlive()) {
                    lobe.hunt(field, updatedField, newAnimals);
                } else {
                    iter.remove();   // remove dead lobe from collection. // remova lobos mortos da coleção 
                }
            } else {
                System.out.println("found unknown animal");
            }
        }
        // add new born animals to the list of animals
        // adicionar animais recém-nascidos à lista de animais 
        animals.addAll(newAnimals);

        // Swap the field and updatedField at the end of the step.
        // Troque o campo e updatedField no final da etapa. 
        Field temp = field;
        field = updatedField;
        updatedField = temp;
        updatedField.clear();

        // display the new field on screen
        // exibir o novo campo na tela 
        view.showStatus(step, field);
    }

    /**
     * Reset the simulation to a starting position.
     * Redefina a simulação para uma posição inicial.
     */
    public void reset() {
        step = 0;
        animals.clear();
        field.clear();
        updatedField.clear();
        populate(field);

        // Show the starting state in the view.
        // Mostra o estado inicial na vista. 
        view.showStatus(step, field);
    }

    /**
     * Populate the field with foxes and rabbits (and lobe).
     * Povoe o campo com raposas , coelhos e lobos.
     */
    private void populate(Field field) {
        Random rand = new Random();
        field.clear();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                if (rand.nextDouble() <= FOX_CREATION_PROBABILITY) {
                    Fox fox = new Fox(true);
                    animals.add(fox);
                    fox.setLocation(row, col);
                    field.place(fox, row, col);
                } else if (rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
                    Rabbit rabbit = new Rabbit(true);
                    animals.add(rabbit);
                    rabbit.setLocation(row, col);
                    field.place(rabbit, row, col);
                }
                //
                else if (rand.nextDouble() <= LOBE_CREATION_PROBABILITY) {
                    Lobe lobe = new Lobe(true);
                    animals.add(lobe);
                    lobe.setLocation(row, col);
                    field.place(lobe, row, col);
                }

                // else leave the location empty.
                // caso contrário, deixe o local vazio. 
            }
        }
        Collections.shuffle(animals);
    }
}
