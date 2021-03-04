import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

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
    private Object[][] field;

    /**
     * Representa um campo das dimensões fornecidas.
     *
     * @param depth A profundidade do campo.
     * @param width A largura do campo. 
     */
    public Field(int depth, int width) {
        this.depth = depth;
        this.width = width;
        field = new Object[depth][width];
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
     * Coloque um animal no local determinado.
     * Se já houver um animal no local ele irá
     * estar perdido.
     *
     * @param animal O animal a ser colocado.
     * @param row Coordenada de linha do local.
     * @param col Coordenada da coluna do local. 
     */
    public void place(Object animal, int row, int col) {
        place(animal, new Location(row, col));
    }

    /**
     * Coloque um animal no local determinado.
     * Se já houver um animal no local ele irá
     * estar perdido.
     *
     * @param animal O animal a ser colocado.
     * @param location Onde colocar o animal. 
     */
    public void place(Object animal, Location location) {
        field[location.getRow()][location.getCol()] = animal;
    }

    /**
     * Devolva o animal no local informado, se houver.
     *
     * @param location Onde no campo.
     * @return O animal no local fornecido ou null se não houver nenhum. 
     */
    public Object getObjectAt(Location location) {
        return getObjectAt(location.getRow(), location.getCol());
    }

    /**
     * Devolva o animal no local informado, se houver.
     *
     * @param row A linha desejada.
     * @param col A coluna desejada.
     * @return O animal no local fornecido ou null se não houver nenhum. 
     */
    public Object getObjectAt(int row, int col) {
        return field[row][col];
    }

    /**
     * Gere um local aleatório adjacente ao
     * determinado local ou é o mesmo local.
     * O local retornado estará dentro dos limites válidos
     * do campo.
     *
     * @param location O local a partir do qual gerar uma adjacência.
     * @return Um local válido dentro da área da grade. Esta
     * pode ser o mesmo objeto que o parâmetro de localização.
     */
    public Location randomAdjacentLocation(Location location) {
        int row = location.getRow();
        int col = location.getCol();
        // Gere um deslocamento de -1, 0 ou +1 para a linha e coluna atuais. 
        int nextRow = row + rand.nextInt(3) - 1;
        int nextCol = col + rand.nextInt(3) - 1;
        // Verifique se o novo local está fora dos limites. 
        if (nextRow < 0 || nextRow >= depth || nextCol < 0 || nextCol >= width) {
            return location;
        } else if (nextRow != row || nextCol != col) {
            return new Location(nextRow, nextCol);
        } else {
            return location;
        }
    }

    /**
     * Tente encontrar um local gratuito adjacente ao
     * determinado local. Se não houver nenhum, então retorne o atual
     * localização, se for gratuito. Caso contrário, retorna nulo.
     * O local retornado estará dentro dos limites válidos
     * do campo.
     *
     * @param location O local a partir do qual gerar uma adjacência.
     * @return Um local válido dentro da área da grade. Este pode ser o
     * mesmo objeto que o parâmetro de localização, ou nulo se todos
     * as localizações ao redor estão cheias. 
     */
    public Location freeAdjacentLocation(Location location) {
        Iterator adjacent = adjacentLocations(location);
        while (adjacent.hasNext()) {
            Location next = (Location) adjacent.next();
            if (field[next.getRow()][next.getCol()] == null) {
                return next;
            }
        }
        // verifique se a localização atual está livre
        if (field[location.getRow()][location.getCol()] == null) {
            return location;
        } else {
            return null;
        }
    }

    /**
     * Gere um iterador sobre uma lista embaralhada de locais adjacentes
     * para o dado. A lista não incluirá o local em si.
     * Todos os locais ficarão dentro da grade.
     *
     * @param location O local a partir do qual gerar adjacências.
     * @return Um iterador sobre locais adjacentes àquele fornecido. 
     */
    public Iterator adjacentLocations(Location location) {
        int row = location.getRow();
        int col = location.getCol();
        LinkedList locations = new LinkedList();
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
        Collections.shuffle(locations, rand);
        return locations.iterator();
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


    //começando implementação do campo
    //Limpando o campo passando a linha e coluna para nulo
    public void clear(Location location) {
        field[location.getRow()][location.getCol()] = null;
    }


}
