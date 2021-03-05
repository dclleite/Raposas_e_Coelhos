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
     * Create a new rabbit. A rabbit may be created with age
     * zero (a new born) or with a random age.
     *
     * @param randomAge If true, the rabbit will have a random age.
     * @param field     The field currently occupied.
     * @param location  The location within the field.
     */
    public Rabbit(boolean randomAge, Field field, Location location) {
        super(field, location);
        age = 0;
        if (randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
    }

    /**
     * This is what the rabbit does most of the time - it runs
     * around. Sometimes it will breed or die of old age.
     *
     * @param newRabbits A list to add newly born rabbits to.
     */
    public void act(List<Animal> newRabbits) {
        incrementAge();
        if (isAlive()) {
            giveBirth(newRabbits);
            // Try to move into a free location.
            Location newLocation = getField().freeAdjacentLocation(getLocation());
            if (newLocation != null) {
                setLocation(newLocation);
            } else {
                // Overcrowding.
                setDead();
            }
        }
    }

    /**
     * Increase the age.
     * This could result in the rabbit's death.
     */
    private void incrementAge() {
        age++;
        if (age > MAX_AGE) {
            setDead();
        }
    }

    /**
     * Check whether or not this rabbit is to give birth at this step.
     * New births will be made into free adjacent locations.
     *
     * @param newRabbits A list to add newly born rabbits to.
     */
    private void giveBirth(List<Animal> newRabbits) {
        // New rabbits are born into adjacent locations.
        // Get a list of adjacent free locations.
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
     * Generate a number representing the number of births,
     * if it can breed.
     *
     * @return The number of births (may be zero).
     */
    private int breed() {
        int births = 0;
        if (canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * A rabbit can breed if it has reached the breeding age.
     *
     * @return true if the rabbit can breed, false otherwise.
     */
    private boolean canBreed() {
        return age >= BREEDING_AGE;
    }

    public String getPredadores() {
        return "Raposa e lobo";
    }

    public String getPresas() {
        return "Ninguém";
    }

    public double getBreed() {
        return this.BREEDING_PROBABILITY;
    }

    public void setBreed(double novo) {
        this.BREEDING_PROBABILITY = novo;
    }
}
