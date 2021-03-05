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
     * Make this animal act - that is: make it do
     * whatever it wants/needs to do.
     *
     * @param newAnimals A list to add newly born animals to.
     */
    abstract public void act(List<Animal> newAnimals);

    /**
     * Check whether the animal is alive or not.
     *
     * @return true if the animal is still alive.
     */
    public boolean isAlive() {
        return alive;
    }


    /**
     * Indicate that the animal is no longer alive.
     * It is removed from the field.
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
     * Return the animal's location.
     *
     * @return The animal's location.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Place the animal at the new location in the given field.
     *
     * @param newLocation The animal's new location.
     */
    public void setLocation(Location newLocation) {
        if (location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }

    /**
     * Return the animal's field.
     *
     * @return The animal's field.
     */
    public Field getField() {
        return field;
    }
}
