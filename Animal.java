import java.util.List;

/**
 * A classe que representa características compartilhadas de animais.
 */
public abstract class Animal {
    // Se o animal está vivo ou não.
    private boolean alive;
    // O local do campo do animal.
    private Field field;
    // Posição do animal no campo.
    private Location location;


    //Criando um novo animal no local no campo /
    public Animal(Field field, Location location) {
        alive = true;
        this.field = field;     //O campo atualmente ocupado.
        setLocation(location);  //A localização dentro do campo.
    }


    //Ação do animal
    abstract public void act(List<Animal> newAnimals); //Uma lista para receber animais recém-nascidos.

    // Verifique se o animal está vivo ou não.
    protected boolean isAlive() {
        return alive; //o retorno será true se o animal ainda estiver vivo e false caso contrario
    }


    //Indique que o animal não está mais vivo para ser removido do campo.
    protected void setDead() {
        alive = false;
        if (location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    // Retorne a localização do animal.
    protected Location getLocation() {
        return location;
    }


    //Coloque o animal no novo local no campo determinado.
    protected void setLocation(Location newLocation) {
        if (location != null) {
            field.clear(location);
        }
        location = newLocation; //A nova localização do animal.
        field.place(this, newLocation);
    }

    //Retorna o campo do animal.
    protected Field getField() {
        return field;
    }
}
