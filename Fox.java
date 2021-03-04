import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * Um modelo simples de raposa.
 * As raposas envelhecem, se movem, comem coelhos e morrem.
 *
 * @author David J. Barnes and Michael Kolling
 * @version 2002-04-11
 */
public class Fox {
    // Características compartilhadas por todas as raposas (campos estáticos). 

    // A idade em que uma raposa pode começar a procriar.
    private static final int BREEDING_AGE = 8;
    // A idade até a qual uma raposa pode viver.
    private static final int MAX_AGE = 150;
    // A probabilidade de uma criação de raposas.
    private  double BREEDING_PROBABILITY = 0.09;
    // O número máximo de nascimentos.
    private static final int MAX_LITTER_SIZE = 3;
    // O valor alimentar de um único coelho. Na verdade, este é o
    // número de passos que uma raposa pode dar antes de comer novamente.
    private int RABBIT_FOOD_VALUE = 4;
    // Um ​​gerador de números aleatórios compartilhado para controlar a reprodução.
    private static final Random rand = new Random();

    // Características individuais (campos de instância). 

    // A idade da raposa.
    private int age;
    // Se a raposa está viva ou não.
    private boolean alive;
    // A posição da raposa
    private Location location;
    // O nível de comida da raposa, que é aumentado comendo coelhos.
    private int foodLevel;

    /**
     * Crie uma raposa. Uma raposa pode ser criada como um recém-nascido (idade zero
     * e sem fome) ou com idade aleatória.
     * @param randomAge Se verdadeiro, a raposa terá idade e nível de fome aleatórios.
     */
    public Fox(boolean randomAge) {
        age = 0;
        alive = true;
        if (randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(RABBIT_FOOD_VALUE);
        } else {
            // idade em 0
            foodLevel = RABBIT_FOOD_VALUE;
        }
    }

    /**
     * Isso é o que a raposa faz na maioria das vezes: ela caça
     * coelhos. No processo, ele pode se reproduzir, morrer de fome,
     * ou morrer de velhice.
     */

    public void hunt(Field currentField, Field updatedField, List newFoxes) {
        incrementAge();
        incrementHunger();
        if (isAlive()) {
            // Novas raposas nascem em locais adjacentes.
            int births = breed();
            for (int b = 0; b < births; b++) {
                Fox newFox = new Fox(false);
                newFoxes.add(newFox);
                Location loc = updatedField.randomAdjacentLocation(location);
                newFox.setLocation(loc);
                updatedField.place(newFox, loc);
            }
            // Mova-se em direção à fonte de alimento, se encontrada.
            Location newLocation = findFood(currentField, location);
            if (newLocation == null) {  // nenhum alimento encontrado - mova-se aleatoriamente
                newLocation = updatedField.freeAdjacentLocation(location);
            }
            if (newLocation != null) {
                setLocation(newLocation);
                updatedField.place(this, newLocation);
            } else {
                // não pode se mover nem ficar - superlotação - todos os locais tomados
                alive = false;
            }
        }
    }

    /**
     * Aumente a idade. Isso pode resultar na morte da raposa.
     */
    private void incrementAge() {
        age++;
        if (age > MAX_AGE) {
            alive = false;
        }
    }

    /**
     * Deixe esta raposa com mais fome. Isso pode resultar na morte da raposa.
     */
    private void incrementHunger() {
        foodLevel--;
        if (foodLevel <= 0) {
            alive = false;
        }
    }

    /**
     * Diga à raposa para procurar coelhos próximos à sua localização atual. 
     *
     * @param field    O campo no qual ele deve olhar.
     * @param location Onde no campo ele está localizado.
     * @return Onde a comida foi encontrada, ou null se não for.
     */
    private Location findFood(Field field, Location location) {
        Iterator adjacentLocations =
            field.adjacentLocations(location);
        while (adjacentLocations.hasNext()) {
            Location where = (Location) adjacentLocations.next();
            Object animal = field.getObjectAt(where);
            if (animal instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) animal;
                if (rabbit.isAlive()) {
                    rabbit.setEaten();
                    foodLevel = RABBIT_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
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
     * Uma raposa pode procriar se atingiu a idade reprodutiva.
     */
    private boolean canBreed() {
        return age >= BREEDING_AGE;
    }

    /**
     * @return Verdadeiro se a raposa ainda estiver viva.
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Defina a localização do animal.
     * @param row A coordenada vertical do local.
     * @param col A coordenada horizontal do local.
     */
    public void setLocation(int row, int col) {
        this.location = new Location(row, col);
    }

    /**
     * Defina a localização da raposa.
     * @param location A localização da raposa.
     */
    public void setLocation(Location location) {
        this.location = location;
    }


    //A raposa está morta agora
    public void setEaten() {
        alive = false;
    }

    public String getPredadores() {
        return "Lobo";
    }

    public String getPresas() {
        return "Coelho";
    }

    public double getBreed(){
      return this.BREEDING_PROBABILITY;
    }

    public void setBreed(double novo){
      this.BREEDING_PROBABILITY = novo;
    }

    public int getFood(){
      return this.RABBIT_FOOD_VALUE;
    }

    public void setFood(int novo){
      this.RABBIT_FOOD_VALUE = novo;
    }
}
