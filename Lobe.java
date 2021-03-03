package raposaecoelho;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 *
 * @author samir
 */
public class Lobe {

    // A idade em que um lobo pode começar a procriar.
    private static final int BREEDING_AGE = 5;
    // A idade até um lobo pode viver.
    private static final int MAX_AGE = 100;
    //  A probabilidade de uma criação de lobos.
    private static final double BREEDING_PROBABILITY = 0.03;
    //  número máximo de nascimentos.
    private static final int MAX_LITTER_SIZE = 3;
    
    // número de passos que lobo pode dar antes de comer novamente.    
    private static final int RABBIT_OR_FOX_FOOD_VALUE = 4;
    
    // A shared random number generator to control breeding. // Um ​​gerador de números aleatórios compartilhado para controlar a reprodução.
    private static final Random rand = new Random();
    
    // Individual characteristics (instance fields).

    // A idade do lobo.
    private int age;
    // Se o lobo está vivo ou não. 
    private boolean alive;
    // A posição do lobo
    private Location location;
    // O nível de comida do lobo, que é aumentado comendo coelhos ou raposas.
    private int foodLevel;

    /**
     
     * Crie um lobo. Um lobo pode ser criado como um recém-nascido (idade zero
     * e sem fome) ou com idade aleatória.
     * @param randomAge Se verdadeiro, o lobo terá idade e nível de fome aleatórios.
     */
    public Lobe(boolean randomAge)
    {
        age = 0;
        alive = true;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(RABBIT_OR_FOX_FOOD_VALUE);
        }
        else {
            // leave age at 0
            // idade em 0
            foodLevel = RABBIT_OR_FOX_FOOD_VALUE;
        }
    }
    
    /**
     /**
    * Isso é o que o lobo faz na maioria das vezes: ela caça
     * coelhos ou raposas. No processo, ele pode se reproduzir, morrer de fome,
     * ou morrer de velhice.
    */
    
    public void hunt(Field currentField, Field updatedField, List newLobes)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            // Novos Lobos nascem em locais adjacentes. 
            int births = breed();
            for(int b = 0; b < births; b++) {
                Lobe newLobe = new Lobe(false);
                newLobes.add(newLobe);
                Location loc = updatedField.randomAdjacentLocation(location);
                newLobe.setLocation(loc);
                updatedField.place(newLobe, loc);
            }
            // Mova-se em direção à fonte de alimento, se encontrada. 
            Location newLocation = findFood(currentField, location);
            if(newLocation == null) {  // no food found - move randomly // nenhum alimento encontrado - mova-se aleatoriamente 
                newLocation = updatedField.freeAdjacentLocation(location);
            }
            if(newLocation != null) {
                setLocation(newLocation);
                updatedField.place(this, newLocation);
            }
            else {
                
                // não pode se mover nem ficar - superlotação - todos os locais tomados
                alive = false;
            }
        }
    }
    
    /**
     
     * Aumente a idade. Isso pode resultar na morte do lobo. 
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            alive = false;
        }
    }
    
    /**
     
     * Deixe o lobo com mais fome. Isso pode resultar na morte do lobo. 
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            alive = false;
        }
    }
    
    /**
     
     * 
     * Diga à raposa para procurar coelhos ou raposas próximos à sua localização atual.
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
                    foodLevel = RABBIT_OR_FOX_FOOD_VALUE;
                    return where;
                }
            }else if(animal instanceof Fox) {
                Fox fox = (Fox) animal;
                if(fox.isAlive()) { 
                    fox.setEaten();
                    foodLevel = RABBIT_OR_FOX_FOOD_VALUE;
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
     
     * Lobo pode procriar se atingiu a idade reprodutiva. 
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
    
    /**
    
     * 
     * Verifique se o lobo está vivo ou não.
     * @return Verdadeiro se a raposa ainda estiver viva.
     * / 
     */
    public boolean isAlive()
    {
        return alive;
    }

    /**
     
     * Defina a localização do animal.
     * @param row A coordenada vertical do local.
     * @param col A coordenada horizontal do local. 
     */
    public void setLocation(int row, int col)
    {
        this.location = new Location(row, col);
    }

    /**
     
     * 
     * Defina a localização do lobo.
     * @param location A localização da raposa.
     * / 
     */
    public void setLocation(Location location)
    {
        this.location = location;
    }
}