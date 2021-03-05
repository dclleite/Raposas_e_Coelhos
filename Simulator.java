import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import java.awt.Color;

/**
 * Um simulador de presa-predador simples, baseado em um campo contendo
 * coelhos e raposas.
 *
 * @author David J. Barnes and Michael Kolling
 * @version 2002-04-09
 */
public class Simulator {
    // As variáveis finais estáticas privadas representam
    // informações de configuração para a simulação.
    // A largura padrão da grade. 
    private static final int DEFAULT_WIDTH = 50;
    // A profundidade padrão da grade. 
    private static final int DEFAULT_DEPTH = 50;
    // A probabilidade de que uma raposa seja criada em qualquer posição da grade. 
    private static final double FOX_CREATION_PROBABILITY = 0.03;
    // A probabilidade de que um coelho seja criado em qualquer posição da grade. 
    private static final double RABBIT_CREATION_PROBABILITY = 0.08;
    //A probabilidade de que um lobo seja criada em qualquer posição da grade.
    private static final double LOBE_CREATION_PROBABILITY = 0.01;

    // List of animals in the field.
    private List<Animal> animals;
    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private SimulatorView view;

    /**
     * Construa um campo de simulação com tamanho padrão.
     */
    public Simulator() {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }

    /**
     * Crie um campo de simulação com o tamanho determinado.
     *
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

        animals = new ArrayList<Animal>();
        field = new Field(depth, width);

        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width);
        view.setColor(Rabbit.class, Color.green);
        view.setColor(Fox.class, Color.red);

        // Setup a valid starting point.
        reset();
    }

    /**
     * Execute a simulação de seu estado atual por um período razoavelmente longo,
     * por exemplo. 500 passos.
     */
    public void runLongSimulation() {
        simulate(500);
    }

    /**
     * Execute a simulação de seu estado atual para um determinado número de etapas.
     * Pare antes de um determinado número de etapas se ele deixar de ser viável.
     */
    public void simulate(int numSteps) {
        for (int step = 1; step <= numSteps && view.isViable(field); step++) {
            simulateOneStep();
        }
    }

    /**
     * Execute a simulação de seu estado atual para uma única etapa.
     * Repita em todo o campo atualizando o estado de cada
     * raposa e coelho.
     */
    public void simulateOneStep() {
        step++;

        // Provide space for newborn animals.
        List<Animal> newAnimals = new ArrayList<Animal>();
        // Let all rabbits act.
        for (Iterator<Animal> it = animals.iterator(); it.hasNext(); ) {
            Animal animal = it.next();
            animal.act(newAnimals);
            if (!animal.isAlive()) {
                it.remove();
            }
        }

        // Add the newly born foxes and rabbits to the main lists.
        animals.addAll(newAnimals);

        view.showStatus(step, field);
    }

    /**
     * Redefina a simulação para uma posição inicial.
     */
    public void reset() {
        step = 0;
        animals.clear();
        populate();

        // Show the starting state in the view.
        view.showStatus(step, field);
    }

    private void populate() {
        Random rand = new Random();
        field.clear();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                if (rand.nextDouble() <= FOX_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Fox fox = new Fox(true, field, location);
                    animals.add(fox);
                } else if (rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Rabbit rabbit = new Rabbit(true, field, location);
                    animals.add(rabbit);
                } else if (rand.nextDouble() <= LOBE_CREATION_PROBABILITY) {
//                    Lobe lobe = new Lobe(true);
//                    animals.add(lobe);
//                    lobe.setLocation(row, col);
//                    field.place(lobe, row, col);
                }
            }
        }
    }
}
