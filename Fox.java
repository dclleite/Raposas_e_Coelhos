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
public class Fox extends Animal {
    // Características compartilhadas por todas as raposas (campos estáticos). 

    // A idade em que uma raposa pode começar a procriar.
    private static final int BREEDING_AGE = 8;
    // A idade até a qual uma raposa pode viver.
    private static final int MAX_AGE = 150;
    // A probabilidade de uma criação de raposas.
    private double BREEDING_PROBABILITY = 0.09;
    // O número máximo de nascimentos.
    private static final int MAX_LITTER_SIZE = 3;
    // O valor alimentar de um único coelho. Na verdade, este é o
    // número de passos que uma raposa pode dar antes de comer novamente.
    private int RABBIT_FOOD_VALUE = 4;
    // Um gerador de números aleatórios compartilhado para controlar a reprodução.
    private static final Random rand = new Random();

    // Características individuais (campos de instância).

    // A idade da raposa.
    private int age;
    // O nível de comida da raposa, que é aumentado comendo coelhos.
    private int foodLevel;

    /**
     * Crie uma raposa. Uma raposa pode ser criada como um recém-nascido (idade zero
     * e sem fome) ou com idade aleatória.
     *
     * @param randomAge Se for verdade, a raposa terá idade e nível de fome aleatórios.
     * @param field     O campo atualmente ocupado.
     * @param location  A localização dentro do campo. 
     */
    public Fox(boolean randomAge, Field field, Location location) {
        super(field, location);
        age = 0;
        if (randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(RABBIT_FOOD_VALUE);
        } else {
            age = 0;
            foodLevel = RABBIT_FOOD_VALUE;
        }
    }

    /**
     * Isso é o que a raposa faz na maioria das vezes: ela caça por
     * coelhos. No processo, ele pode se reproduzir, morrer de fome,
     * ou morrer de velhice.
     *
     * @param newFoxes Uma lista para adicionar raposas recém-nascidas.
     */
    public void act(List<Animal> newFoxes) {
        incrementAge();
        incrementHunger();
        if (isAlive()) {
            giveBirth(newFoxes);
            // Mova-se em direção a uma fonte de alimento, se encontrada.
            Location location = getLocation();
            Location newLocation = findFood();
            if (newLocation == null) {
                // Nenhum alimento foi encontrado - tente ir para um local livre.
                newLocation = getField().freeAdjacentLocation(location);
            }
            // Veja se dava para se mexer.
            if (newLocation != null) {
                setLocation(newLocation);
            } else {
                // Superlotação.
                setDead();
            }
        }
    }

    /**
     * Aumente a idade. Isso pode resultar na morte da raposa.
     */
    private void incrementAge() {
        age++;
        if (age > MAX_AGE) {
            setDead();
        }
    }

    /**
     * Deixe esta raposa com mais fome. Isso pode resultar na morte da raposa.
     */
    private void incrementHunger() {
        foodLevel--;
        if (foodLevel <= 0) {
            setDead();
        }
    }

    /**
     * Diga à raposa para procurar coelhos próximos à sua localização atual.
     * Apenas o primeiro coelho vivo é comido.
     *
     * @return Onde a comida foi encontrada, ou null se não for.
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
                    foodLevel = RABBIT_FOOD_VALUE;
                    // Remova o coelho morto do campo.
                    return where;
                }
            }
        }
        return null;
    }

    /**
     * Verifique se esta raposa dará à luz ou não nesta etapa.
     * Os novos nascimentos serão feitos em locais adjacentes livres.
     *
     * @param newFoxes Uma lista para adicionar raposas recém-nascidas.
     */
    private void giveBirth(List<Animal> newFoxes) {
        // Novas raposas nascem em locais adjacentes.
        // Obtenha uma lista de locais gratuitos adjacentes.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for (int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Fox young = new Fox(false, field, loc);
            newFoxes.add(young);
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
     * Uma raposa pode procriar se atingiu a idade reprodutiva.
     */
    private boolean canBreed() {
        return age >= BREEDING_AGE;
    }

    public String getPredadores() {
        return "Lobo";
    }

    public String getPresas() {
        return "Coelho";
    }

    public double getBreed() {
        return this.BREEDING_PROBABILITY;
    }

    public void setBreed(double novo) {
        this.BREEDING_PROBABILITY = novo;
    }

    public int getFood() {
        return this.RABBIT_FOOD_VALUE;
    }

    public void setFood(int novo) {
        this.RABBIT_FOOD_VALUE = novo;
    }
}
