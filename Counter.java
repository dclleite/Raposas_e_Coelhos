import java.awt.Color;

/**
 * Fornece um contador para um participante da simulação.
 * Isso inclui uma sequência de identificação e uma contagem de como
 * muitos participantes deste tipo existem atualmente em
 * a simulação. 
 *
 * @author David J. Barnes and Michael Kolling
 * @version 2002-04-23
 */
public class Counter {
    // Um nome para este tipo de participante de simulação 
    private String name;
    // Quantos desse tipo existem na simulação. 
    private int count;

    /**
     * Forneça um nome para um dos tipos de simulação.
     *
     * @param name Um nome, por exemplo "Raposa". 
     */
    public Counter(String name) {
        this.name = name;
        count = 0;
    }

    /**
     * @return A breve descrição desse tipo. 
     */
    public String getName() {
        return name;
    }

    /**
     * @return A contagem atual para este tipo. 
     */
    public int getCount() {
        return count;
    }

    /**
     * Aumente a contagem atual em um. 
     */
    public void increment() {
        count++;
    }

    /**
     * Redefina a contagem atual para zero.
     */
    public void reset() {
        count = 0;
    }
}
