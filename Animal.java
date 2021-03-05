import java.util.List;

/**
 * A classe que representa características compartilhadas de animais.
 */
public abstract class Animal {
    // A idade da raposa.
    private int age;
    // Se o animal está vivo ou não.
    private boolean alive;
    // O local do campo do animal.
    private Field field;
    // Posição do animal no campo.
    private Location location;

    /**
     * Cria um novo animal no local no campo.
     *
     * @param field    O campo atualmente ocupado.
     * @param location A localização dentro do campo.
     */
    public Animal(Field field, Location location) {
        age = 0;
        alive = true;
        this.field = field;     //O campo atualmente ocupado.
        setLocation(location);  //A localização dentro do campo.
    }


    /**
     * Faça este animal agir - isto é: faça-o agir 
     * o que quer / precisa fazer.
     *
     * @param newAnimals Uma lista para adicionar animais recém-nascidos.
     */
    abstract public void act(List<Animal> newAnimals);

    /**
     * Verifique se o animal está vivo ou não. 
     *
     * @return Verdadeiro, se o animal ainda estiver vivo. 
     */
    public boolean isAlive() {
        return alive;
    }


    /**
     * Indique que o animal não está mais vivo.
     * Ele é removido do campo. 
     */
    public void setDead() {
        alive = false;
        if (location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * Retorne a localização do animal.
     *
     * @return A localização do animal.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Coloque o animal no novo local no campo determinado.
     *
     * @param newLocation A nova localização do animal.
     */
    public void setLocation(Location newLocation) {
        if (location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }

    /**
     * Retorne ao campo do animal.
     *
     * @return O campo do animal.
     */
    public Field getField() {
        return field;
    }
}
