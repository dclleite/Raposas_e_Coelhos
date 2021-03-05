import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Lobe extends Animal {

    // A idade em que um lobo pode começar a procriar.
    private static final int BREEDING_AGE = 5;
    // A idade até um lobo pode viver.
    private static final int MAX_AGE = 100;
    //  A probabilidade de uma criação de lobos.
    private double BREEDING_PROBABILITY = 0.03;
    //  número máximo de nascimentos.
    private static final int MAX_LITTER_SIZE = 3;

    // número de passos que lobo pode dar antes de comer novamente.    
    private int RABBIT_OR_FOX_FOOD_VALUE = 4;

    // Um gerador de números aleatórios compartilhado para controlar a reprodução.
    private static final Random rand = new Random();

    // Individual characteristics (instance fields).`

    // A idade do lobo.
    private int age;
    // O nível de comida do lobo, que é aumentado comendo coelhos ou raposas.
    private int foodLevel;

    /**
     * Crie uma raposa. Uma raposa pode ser criada como um recém-nascido (idade zero
     * e sem fome) ou com idade aleatória.
     *
     * @param randomAge If true, the fox will have random age and hunger level.
     * @param field     The field currently occupied.
     * @param location  The location within the field.
     */
    public Lobe(boolean randomAge, Field field, Location location) {
        super(field, location);
        age = 0;
        if (randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(RABBIT_OR_FOX_FOOD_VALUE);
        } else {
            age = 0;
            foodLevel = RABBIT_OR_FOX_FOOD_VALUE;
        }
    }

    /**
     * This is what the fox does most of the time: it hunts for
     * rabbits. In the process, it might breed, die of hunger,
     * or die of old age.
     *
     * @param newWolves A list to add newly born foxes to.
     */
    public void act(List<Animal> newWolves) {
        incrementAge();
        incrementHunger();
        if (isAlive()) {
            giveBirth(newWolves);
            // Move towards a source of food if found.
            Location location = getLocation();
            Location newLocation = findFood();
            if (newLocation == null) {
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(location);
            }
            // See if it was possible to move.
            if (newLocation != null) {
                setLocation(newLocation);
            } else {
                // Overcrowding.
                setDead();
            }
        }
    }

    /**
     * Increase the age. This could result in the fox's death.
     */
    private void incrementAge() {
        age++;
        if (age > MAX_AGE) {
            setDead();
        }
    }

    /**
     * Make this fox more hungry. This could result in the fox's death.
     */
    private void incrementHunger() {
        foodLevel--;
        if (foodLevel <= 0) {
            setDead();
        }
    }

    /**
     * Tell the fox to look for rabbits adjacent to its current location.
     * Only the first live rabbit is eaten.
     *
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood() {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while (it.hasNext()) {
            Location where = it.next();
            Animal animal = field.getObjectAt(where);
            if (animal instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) animal;
                if (rabbit.isAlive()) {
                    rabbit.setDead();
                    foodLevel = RABBIT_OR_FOX_FOOD_VALUE;
                    return where;
                }
            } else if (animal instanceof Fox) {
                Fox fox = (Fox) animal;
                if (fox.isAlive()) {
                    fox.setDead();
                    foodLevel = RABBIT_OR_FOX_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }

    /**
     * Check whether or not this fox is to give birth at this step.
     * New births will be made into free adjacent locations.
     *
     * @param newWolves A list to add newly born foxes to.
     */
    private void giveBirth(List<Animal> newWolves) {
        // New foxes are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for (int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Fox young = new Fox(false, field, loc);
            newWolves.add(young);
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
     * A fox can breed if it has reached the breeding age.
     */
    private boolean canBreed() {
        return age >= BREEDING_AGE;
    }

    public static String getPredadores() {
        return "Nenhum";
    }

    public static String getPresas() {
        return "Raposa e coelho";
    }
}