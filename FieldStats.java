import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class collects and provides some statistical data on the state
 * of a field. It is flexible: it will create and maintain a counter
 * for any class of object that is found within the field.
 * <p>
 * Esta classe coleta e fornece alguns dados estatísticos sobre o estado
 * de um campo. É flexível: criará e manterá um contador
 * para qualquer classe de objeto encontrada dentro do campo
 *
 * @author David J. Barnes and Michael Kolling
 * @version 2002-04-23
 */
public class FieldStats {
    // Counters for each type of entity (fox, rabbit, etc.) in the simulation.
    // Contadores para cada tipo de entidade (raposa, coelho, etc.) na simulação.
    private HashMap counters;
    // Whether the counters are currently up to date.
    // Se os contadores estão atualizados atualmente.
    private boolean countsValid;

    /**
     * Construct a field-statistics object.
     * Construa um objeto de estatísticas de campo.
     */
    public FieldStats() {
        // Set up a collection for counters for each type of animal that we might find
        // Configure uma coleção de contadores para cada tipo de animal que possamos encontrar
        counters = new HashMap();
        countsValid = true;
    }

    /**
     * @return Uma string que descreve quais animais estão no campo.
     */
    public String getPopulationDetails(Field field) {
        StringBuffer buffer = new StringBuffer();
        if (!countsValid) {
            generateCounts(field);
        }
        Iterator keys = counters.keySet().iterator();
        while (keys.hasNext()) {
            Counter info = (Counter) counters.get(keys.next());
            buffer.append(info.getName());
            buffer.append(": ");
            buffer.append(info.getCount());
            buffer.append(' ');
        }
        return buffer.toString();
    }

    /**
     * Invalidate the current set of statistics; reset all counts to zero.
     * Invalide o conjunto atual de estatísticas; redefinir todas as contagens para zero.
     */
    public void reset() {
        countsValid = false;
        Iterator keys = counters.keySet().iterator();
        while (keys.hasNext()) {
            Counter cnt = (Counter) counters.get(keys.next());
            cnt.reset();
        }
    }

    /**
     * Increment the count for one class of animal.
     * Aumente a contagem para uma classe de animal.
     */
    public void incrementCount(Class animalClass) {
        Counter cnt = (Counter) counters.get(animalClass);
        if (cnt == null) {
            // we do not have a counter for this species yet - create one
            // não temos um contador para esta espécie ainda - crie um
            cnt = new Counter(animalClass.getName());
            counters.put(animalClass, cnt);
        }
        cnt.increment();
    }

    /**
     * Indicate that an animal count has been completed.
     * Indique que uma contagem de animais foi concluída.
     */
    public void countFinished() {
        countsValid = true;
    }

    /**
     * Determine whether the simulation is still viable.
     * I.e., should it continue to run.
     *
     * @return true Se houver mais de uma espécie viva.
     */
    public boolean isViable(Field field) {
        // How many counts are non-zero.
        int nonZero = 0;
        if (!countsValid) {
            generateCounts(field);
        }
        Iterator keys = counters.keySet().iterator();
        while (keys.hasNext()) {
            Counter info = (Counter) counters.get(keys.next());
            if (info.getCount() > 0) {
                nonZero++;
            }
        }
        return nonZero > 1;
    }

    /**
     * Generate counts of the number of foxes and rabbits.
     * These are not kept up to date as foxes and rabbits
     * are placed in the field, but only when a request
     * is made for the information.
     * <p>
     * Gere contagens do número de raposas e coelhos.
     * Estes não são atualizados como raposas e coelhos
     * são colocados no campo, mas apenas quando um pedido
     * é feito para a informação.
     */
    private void generateCounts(Field field) {
        reset();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Object animal = field.getObjectAt(row, col);
                if (animal != null) {
                    incrementCount(animal.getClass());
                }
            }
        }
        countsValid = true;
    }
}
