import java.util.List;
import java.util.Random;

/**
 * Um modelo simples de coelho.
 * Coelhos envelhecem, se movem, se reproduzem e morrem.
 *
 * @author David J. Barnes and Michael Kolling
 * @version 2002-04-11
 */
public class Rabbit {
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
    // Se o coelho está vivo ou não. 
    private boolean alive;
    // A posição do coelho 
    private Location location;

    /**
     * Crie um novo coelho. Um coelho pode ser criado com a idade
     * zero (recém-nascido) ou com idade aleatória.
     * @param randomAge Se verdadeiro, o coelho terá uma idade aleatória.
     */
    public Rabbit(boolean randomAge) {
        age = 0;
        alive = true;
        if (randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
    }

    /**
     * Isso é o que o coelho faz na maioria das vezes - ele corre
     * por aí. Às vezes, ele se reproduz ou morre de velhice.
     */
    public void run(Field updatedField, List newRabbits) {
        incrementAge();
        if (alive) {
            int births = breed();
            for (int b = 0; b < births; b++) {
                Rabbit newRabbit = new Rabbit(false);
                newRabbits.add(newRabbit);
                Location loc = updatedField.randomAdjacentLocation(location);
                newRabbit.setLocation(loc);
                updatedField.place(newRabbit, loc);
            }
            Location newLocation = updatedField.freeAdjacentLocation(location);
            // Apenas transfira para o campo atualizado se houver um local livre 
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
     * Aumente a idade.
     * Isso pode resultar na morte do coelho.
     */
    private void incrementAge() {
        age++;
        if (age > MAX_AGE) {
            alive = false;
        }
    }

    /**
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
     */
    private boolean canBreed() {
        return age >= BREEDING_AGE;
    }

    /**
     * Verifique se o coelho está vivo ou não. 
     *
     * @return True se o coelho ainda estiver vivo.
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Diga ao coelho que ele está morto agora :(
     */
    public void setEaten() {
        alive = false;
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
     * Defina a localização do coelho.
     * @param location A localização do coelho.
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    public String getPredadores() {
        return "Raposa e lobo";
    }

    public String getPresas() {
        return "Ninguém";
    }

    public double getBreed(){
      return this.BREEDING_PROBABILITY;
    }

    public void setBreed(double novo){
      this.BREEDING_PROBABILITY = novo;
    }
}
