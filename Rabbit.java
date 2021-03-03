package raposaecoelho;

import java.util.List;
import java.util.Random;

/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 * 
 * Um modelo simples de coelho.
 * Coelhos envelhecem, se movem, se reproduzem e morrem.
 * 
 * @author David J. Barnes and Michael Kolling
 * @version 2002-04-11
 */
public class Rabbit
{
    // Characteristics shared by all rabbits (static fields). 
    // Características compartilhadas por todos os coelhos (campos estáticos). 

    // The age at which a rabbit can start to breed. // A idade em que um coelho pode começar a procriar. 
    private static final int BREEDING_AGE = 5;
    // The age to which a rabbit can live. // Até qual idade que um coelho pode viver. 
    private static final int MAX_AGE = 50;
    // The likelihood of a rabbit breeding. // A probabilidade de uma criação de coelhos. 
    private static final double BREEDING_PROBABILITY = 0.15;
    // The maximum number of births. // O número máximo de nascimentos. 
    private static final int MAX_LITTER_SIZE = 5;
    // A shared random number generator to control breeding.
    //Um gerador de números aleatórios compartilhado para controlar a reprodução. 
    private static final Random rand = new Random();
    
    // Individual characteristics (instance fields).
    // Características individuais (campos de instância). 

    // The rabbit's age.
    // A idade do coelho. 
    private int age;
    // Whether the rabbit is alive or not.
    private boolean alive;
    // The rabbit's position
    private Location location;

    /**
     * Create a new rabbit. A rabbit may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the rabbit will have a random age.
     * 
     * * Crie um novo coelho. Um coelho pode ser criado com a idade
     * zero (recém-nascido) ou com idade aleatória.
     *
     * @param randomAge Se verdadeiro, o coelho terá uma idade aleatória. 
     */
    public Rabbit(boolean randomAge)
    {
        age = 0;
        alive = true;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
    }
    
    /**
     * This is what the rabbit does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * 
     * Isso é o que o coelho faz na maioria das vezes - ele corre
     * por aí. Às vezes, ele se reproduz ou morre de velhice. 
     */
    public void run(Field updatedField, List newRabbits)
    {
        incrementAge();
        if(alive) {
            int births = breed();
            for(int b = 0; b < births; b++) {
                Rabbit newRabbit = new Rabbit(false);
                newRabbits.add(newRabbit);
                Location loc = updatedField.randomAdjacentLocation(location);
                newRabbit.setLocation(loc);
                updatedField.place(newRabbit, loc);
            }
            Location newLocation = updatedField.freeAdjacentLocation(location);
            // Only transfer to the updated field if there was a free location
            // Apenas transfira para o campo atualizado se houver um local livre 
            if(newLocation != null) {
                setLocation(newLocation);
                updatedField.place(this, newLocation);
            }
            else {
                // can neither move nor stay - overcrowding - all locations taken
                // não pode se mover nem ficar - superlotação - todos os locais tomados 
                alive = false;
            }
        }
    }
    
    /**
     * Increase the age.
     * This could result in the rabbit's death.
     * 
     * Aumente a idade.
     * Isso pode resultar na morte do coelho.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            alive = false;
        }
    }
    
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     * 
     * Gere um número que representa o número de nascimentos,
     * se pode procriar.
     * @return O número de nascimentos (pode ser zero). 
     */
    private int breed()
    {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * A rabbit can breed if it has reached the breeding age.
     * Um coelho pode procriar se tiver atingido a idade reprodutiva. 
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
    
    /**
     * Check whether the rabbit is alive or not.
     * @return True if the rabbit is still alive.
     * 
     * Verifique se o coelho está vivo ou não.
     * @return True se o coelho ainda estiver vivo. 
     */
    public boolean isAlive()
    {
        return alive;
    }

    /**
     * Tell the rabbit that it's dead now :(
     * 
     * Diga ao coelho que ele está morto agora :( 
     */
    public void setEaten()
    {
        alive = false;
    }
    
    /**
     * Set the animal's location.
     * @param row The vertical coordinate of the location.
     * @param col The horizontal coordinate of the location.
     * 
     * Defina a localização do animal.
     * @param row A coordenada vertical do local.
     * @param col A coordenada horizontal do local. 

     */
    public void setLocation(int row, int col)
    {
        this.location = new Location(row, col);
    }

    /**
     * Set the rabbit's location.
     * @param location The rabbit's location.
     * 
     * Defina a localização do coelho.
     * @param location A localização do coelho. 
     */
    public void setLocation(Location location)
    {
        this.location = location;
    }
}
