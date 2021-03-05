import java.util.*;

/**
 * Representa uma grade retangular de posições de campo.
 * Cada posição pode armazenar um único animal.
 *
 * @author David J. Barnes e Michael Kolling
 * @version 09-04-2002
 */
public class Field {
    private static final Random rand = new Random();

    // A profundidade e largura do campo.
    private int depth, width;
    // Armazenamento para os animais.
    private Animal[][] field;

    /**
     * Representa um campo das dimensões fornecidas.
     *
     * @param depth A profundidade do campo.
     * @param width A largura do campo.
     */
    public Field(int depth, int width) {
        this.depth = depth;
        this.width = width;
        field = new Animal[depth][width];
    }

    /**
     * Esvazie o campo.
     */
    public void clear() {
        for (int row = 0; row < depth; row++) {
            for (int col = 0; col < width; col++) {
                field[row][col] = null;
            }
        }
    }

    /**
     * Limpe o local fornecido.
     *
     * @param location O local a ser limpo. 
     */
    public void clear(Location location) {
        field[location.getRow()][location.getCol()] = null;
    }

    /**
     * Coloque um animal no local determinado.
     * Se já houver um animal no local ele irá
     * estar perdido.
     *
     * @param animal O animal a ser colocado.
     * @param row    Coordenada de linha do local.
     * @param col    Coordenada da coluna do local.
     */
    public void place(Animal animal, int row, int col) {
        place(animal, new Location(row, col));
    }

    /**
     * Coloque um animal no local determinado.
     * Se já houver um animal no local ele irá
     * estar perdido.
     *
     * @param animal   O animal a ser colocado.
     * @param location Onde colocar o animal.
     */
    public void place(Animal animal, Location location) {
        field[location.getRow()][location.getCol()] = animal;
    }

    /**
     * Devolva o animal no local informado, se houver.
     *
     * @param location Onde no campo.
     * @return O animal no local fornecido ou null se não houver nenhum.
     */
    public Animal getObjectAt(Location location) {
        return getObjectAt(location.getRow(), location.getCol());
    }

    /**
     * Devolva o animal no local informado, se houver.
     *
     * @param row A linha desejada.
     * @param col A coluna desejada.
     * @return O animal no local fornecido ou null se não houver nenhum.
     */
    public Animal getObjectAt(int row, int col) {
        return field[row][col];
    }

    /**
     * Gere um local aleatório adjacente ao
     * determinado local ou é o mesmo local.
     * O local retornado estará dentro dos limites válidos
     * do campo. 
     *
     * @param location O local a partir do qual gerar uma adjacência.
     * @return Um local válido dentro da área da grade.
     */
    public Location randomAdjacentLocation(Location location) {
        List<Location> adjacent = adjacentLocations(location);
        return adjacent.get(0);
    }

    /**
     * Obtenha uma lista aleatória dos locais adjacentes gratuitos.
     *
     * @param location Obtenha locais adjacentes a este.
     * @return Uma lista de locais adjacentes livres.
     */
    public List<Location> getFreeAdjacentLocations(Location location) {
        List<Location> free = new LinkedList<Location>();
        List<Location> adjacent = adjacentLocations(location);
        for (Location next : adjacent) {
            if (getObjectAt(next) == null) {
                free.add(next);
            }
        }
        return free;
    }

    /**
     * Tente encontrar um local gratuito adjacente a
     * determinado local. Se não houver nenhum, retorna nulo
     * O local retornado estará dentro dos limites válido
     * do campo.
     *
     * @param location A localização a partir da qual uma adjacência deve ser gerada.
     * @return Um local válido dentro da área da grade. 
     */
    public Location freeAdjacentLocation(Location location) {
        // The available free ones.
        List<Location> free = getFreeAdjacentLocations(location);
        if (free.size() > 0) {
            return free.get(0);
        } else {
            return null;
        }
    }

    /**
     * Retorne uma lista embaralhada de locais adjacentes ao determinado.
     * A lista não incluirá o local em si.
     * Todos os locais ficarão dentro da grade. 
     *
     * @param location A localização a partir da qual gerar adjacências.
     * @return Uma lista de locais adjacentes àquele fornecido.
     */
    public List<Location> adjacentLocations(Location location) {
        assert location != null : "Null location passed to adjacentLocations";
        // A lista de locais a serem retornados. 
        List<Location> locations = new LinkedList<Location>();
        if (location != null) {
            int row = location.getRow();
            int col = location.getCol();
            for (int roffset = -1; roffset <= 1; roffset++) {
                int nextRow = row + roffset;
                if (nextRow >= 0 && nextRow < depth) {
                    for (int coffset = -1; coffset <= 1; coffset++) {
                        int nextCol = col + coffset;
                        // Exclua locais inválidos e o local original. 
                        if (nextCol >= 0 && nextCol < width && (roffset != 0 || coffset != 0)) {
                            locations.add(new Location(nextRow, nextCol));
                        }
                    }
                }
            }

            // Misture a lista. Vários outros métodos dependem da lista
            // estando em uma ordem aleatória.
            Collections.shuffle(locations, rand);
        }
        return locations;
    }

    /**
     * @return A profundidade do campo.
     */
    public int getDepth() {
        return depth;
    }

    /**
     * @return A largura do campo.
     */
    public int getWidth() {
        return width;
    }


}
