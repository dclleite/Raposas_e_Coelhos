import java.util.List;
import java.util.Random;

/**
 * Um modelo simples de coelho.
 * Coelhos envelhecem, se movem, se reproduzem e morrem.
 *
 * @author David J. Barnes and Michael Kolling
 * @version 2002-04-11
 */
public class Rabbit extends Animal {
    // Características compartilhadas por todos os coelhos (campos estáticos). 

    // A idade em que um coelho pode começar a procriar. 
    private static final int BREEDING_AGE = 5;
    // Até qual idade que um coelho pode viver. 
    private static final int MAX_AGE = 50;
    // A probabilidade de uma criação de coelhos. 
    private double BREEDING_PROBABILITY = 0.15;
    // O número máximo de nascimentos. 
    private static final int MAX_LITTER_SIZE = 5;
    //Um gerador de números aleatórios compartilhado para controlar a reprodução. 
    private static final Random rand = new Random();

    // Características individuais (campos de instância). 

    // A idade do coelho. 
    private int age;

    /**
     * Crie um novo coelho. Um coelho pode ser criado com a idade
     * zero (um recém-nascido) ou com uma idade aleatória.
     *
     * @param randomAge Se for verdade, o coelho terá uma idade aleatória.
     * @param field     O campo atualmente ocupado.
     * @param location  A localização dentro do campo.
     */
    public Rabbit(boolean randomAge, Field field, Location location) {
        super(field, location);
        age = 0;
        if (randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
    }

    /**
     * Isso é o que o coelho faz na maioria das vezes - ele corre
     * por aí. Às vezes, ele se reproduz ou morre de velhice.
     *
     * @param newRabbits Uma lista para adicionar coelhos recém-nascidos.
     */
    public void act(List<Animal> newRabbits) {
        incrementAge();
        if (isAlive()) {
            giveBirth(newRabbits);
            // Tente mudar para um local livre.
            Location newLocation = getField().freeAdjacentLocation(getLocation());
            if (newLocation != null) {
                setLocation(newLocation);
            } else {
                // Superlotação.
                setDead();
            }
        }
    }

    /**
     * Aumente a idade.
     * Isso pode resultar na morte do coelho.
     */
    private void incrementAge() {
        age++;
        if (age > MAX_AGE) {
            setDead();
        }
    }

    /**
     * Verifique se este coelho vai dar à luz ou não nesta etapa.
     * Os novos nascimentos serão feitos em locais adjacentes livres.
     *
     * @param newRabbits Uma lista para adicionar coelhos recém-nascidos. 
     */
    private void giveBirth(List<Animal> newRabbits) {
        // Novos coelhos nascem em locais adjacentes.
        // Obtenha uma lista de locais gratuitos adjacentes. 
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for (int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Rabbit young = new Rabbit(false, field, loc);
            newRabbits.add(young);
        }
    }

    /**
     * Gere um número que representa o número de nascimentos,
     * se pode procriar.
     *
     * @return O número de nascimentos (pode ser zero). 
     */
    private int breed() {
        int births = 0;
        if (canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * Um coelho pode procriar se tiver atingido a idade reprodutiva.
     *
     * @return Verdadeiro, se o coelho pode procriar, caso contrário, falso.
     */
    private boolean canBreed() {
        return age >= BREEDING_AGE;
    }

    public double getBreed() {
        return this.BREEDING_PROBABILITY;
    }

    public void setBreed(double novo) {
        this.BREEDING_PROBABILITY = novo;
    }

    public static String getPredadores() {
        return "Raposa e lobo";
    }

    public static String getPresas() {
        return "Ninguém";
    }
}
