//package raposaecoelho;

import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a fox.
 * Foxes age, move, eat rabbits, and die.
 * 
 * Um modelo simples de raposa.
 * As raposas envelhecem, se movem, comem coelhos e morrem.
 *
 * @author David J. Barnes and Michael Kolling
 * @version 2002-04-11
 */
public class Fox
{
    // Characteristics shared by all foxes (static fields).
    
    // The age at which a fox can start to breed. // A idade em que uma raposa pode começar a procriar.
    private static final int BREEDING_AGE = 8;
    // The age to which a fox can live. // A idade até a qual uma raposa pode viver.
    private static final int MAX_AGE = 150;
    // The likelihood of a fox breeding.//  A probabilidade de uma criação de raposas.
    private static final double BREEDING_PROBABILITY = 0.09;
    // The maximum number of births. O número máximo de nascimentos.
    private static final int MAX_LITTER_SIZE = 3;
    // The food value of a single rabbit. In effect, this is the // O valor alimentar de um único coelho. Na verdade, este é o
    // number of steps a fox can go before it has to eat again.     // número de passos que uma raposa pode dar antes de comer novamente.    
    private static final int RABBIT_FOOD_VALUE = 4;
    // A shared random number generator to control breeding. // Um ​​gerador de números aleatórios compartilhado para controlar a reprodução.
    private static final Random rand = new Random();
    
    // Individual characteristics (instance fields).

    // The fox's age. // A idade da raposa.
    private int age;
    // Whether the fox is alive or not. // Se a raposa está viva ou não. 
    private boolean alive;
    // The fox's position // A posição da raposa
    private Location location;
    // The fox's food level, which is increased by eating rabbits. // O nível de comida da raposa, que é aumentado comendo coelhos.
    private int foodLevel;

    /**
     * Create a fox. A fox can be created as a new born (age zero
     * and not hungry) or with random age.
     * @param randomAge If true, the fox will have random age and hunger level.
     * 
     * Crie uma raposa. Uma raposa pode ser criada como um recém-nascido (idade zero
     * e sem fome) ou com idade aleatória.
     * @param randomAge Se verdadeiro, a raposa terá idade e nível de fome aleatórios.
     */
    public Fox(boolean randomAge)
    {
        age = 0;
        alive = true;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(RABBIT_FOOD_VALUE);
        }
        else {
            // leave age at 0
            // idade em 0
            foodLevel = RABBIT_FOOD_VALUE;
        }
    }
    
    /**
     * This is what the fox does most of the time: it hunts for
     * rabbits. In the process, it might breed, die of hunger,
     * or die of old age.
     */
    
     /**
    * Isso é o que a raposa faz na maioria das vezes: ela caça
     * coelhos. No processo, ele pode se reproduzir, morrer de fome,
     * ou morrer de velhice.
    */
    
    public void hunt(Field currentField, Field updatedField, List newFoxes)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            // New foxes are born into adjacent locations. // Novas raposas nascem em locais adjacentes. 
            int births = breed();
            for(int b = 0; b < births; b++) {
                Fox newFox = new Fox(false);
                newFoxes.add(newFox);
                Location loc = updatedField.randomAdjacentLocation(location);
                newFox.setLocation(loc);
                updatedField.place(newFox, loc);
            }
            // Move towards the source of food if found. // Mova-se em direção à fonte de alimento, se encontrada. 
            Location newLocation = findFood(currentField, location);
            if(newLocation == null) {  // no food found - move randomly // nenhum alimento encontrado - mova-se aleatoriamente 
                newLocation = updatedField.freeAdjacentLocation(location);
            }
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
     * Increase the age. This could result in the fox's death.
     * Aumente a idade. Isso pode resultar na morte da raposa. 
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            alive = false;
        }
    }
    
    /**
     * Make this fox more hungry. This could result in the fox's death.
     * Deixe esta raposa com mais fome. Isso pode resultar na morte da raposa. 
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            alive = false;
        }
    }
    
    /**
     * Tell the fox to look for rabbits adjacent to its current location.
     * @param field The field in which it must look.
     * @param location Where in the field it is located.
     * @return Where food was found, or null if it wasn't.
     * 
     * Diga à raposa para procurar coelhos próximos à sua localização atual.
     * @param field O campo no qual ele deve olhar.
     * @param location Onde no campo ele está localizado.
     * @return Onde a comida foi encontrada, ou null se não for. 
     */
    private Location findFood(Field field, Location location)
    {
        Iterator adjacentLocations =
                          field.adjacentLocations(location);
        while(adjacentLocations.hasNext()) {
            Location where = (Location) adjacentLocations.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) animal;
                if(rabbit.isAlive()) { 
                    rabbit.setEaten();
                    foodLevel = RABBIT_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }
        
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     * 
     * Gere um número que representa o número de nascimentos,
     * se pode procriar.
     * @return O número de nascimentos (pode ser zero).
     * / 
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
     * A fox can breed if it has reached the breeding age.
     * Uma raposa pode procriar se atingiu a idade reprodutiva. 
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
    
    /**
     * Check whether the fox is alive or not.
     * @return True if the fox is still alive.
     * 
     * Verifique se a raposa está viva ou não.
     * @return Verdadeiro se a raposa ainda estiver viva.
     * / 
     */
    public boolean isAlive()
    {
        return alive;
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
     * Set the fox's location.
     * @param location The fox's location.
     * 
     * Defina a localização da raposa.
     * @param location A localização da raposa.
     * / 
     */
    public void setLocation(Location location)
    {
        this.location = location;
    }
    
    
    //A raposa está morta agora
    public void setEaten()
    {
        alive = false;
    }

    public String getPredadores(){
      return "Lobo";
    }

    public String getPresas(){
      return "Coelho";
    }
}
