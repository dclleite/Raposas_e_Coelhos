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

    // Lista de animais no campo.
    private List<Animal> animals;
    // O estado atual do campo.
    private Field field;
    // A etapa atual da simulação.
    private int step;
    // Uma visão gráfica da simulação.
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

        // Crie uma visualização do estado de cada local no campo.
        view = new SimulatorView(depth, width);
        view.setColor(Rabbit.class, Color.green);
        view.setColor(Fox.class, Color.red);
        view.setColor(Lobe.class, Color.blue);

        // Configure um ponto de partida válido.
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

        // Fornece espaço para animais recém-nascidos.
        List<Animal> newAnimals = new ArrayList<Animal>();
        // Deixe todos os coelhos agirem.
        for (Iterator<Animal> it = animals.iterator(); it.hasNext(); ) {
            Animal animal = it.next();
            animal.act(newAnimals);
            if (!animal.isAlive()) {
                it.remove();
            }
        }

        // Adicione as raposas e os coelhos recém-nascidos às listas principais.
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

        // Mostra o estado inicial na vista.
        view.showStatus(step, field);
    }

    private void populate() {
        Random rand = new Random();
        field.clear();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Location location = new Location(row, col);
                Animal animal;
                if (rand.nextDouble() <= FOX_CREATION_PROBABILITY) {
                    animal = new Fox(true, field, location);
                    animals.add(animal);
                    field.place(animal, location);
                } else if (rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
                    animal = new Rabbit(true, field, location);
                    animals.add(animal);
                    field.place(animal, location);
                } else if (rand.nextDouble() <= LOBE_CREATION_PROBABILITY) {
                    animal = new Lobe(true, field, location);
                    animals.add(animal);
                    field.place(animal, location);
                }
            }
        }
    }
}
