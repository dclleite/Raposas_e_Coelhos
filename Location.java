package raposaecoelho;

/**
 * Represent a location in a rectangular grid.
 * Represente um local em uma grade retangular. 
 * 
 * @author David J. Barnes and Michael Kolling
 * @version 2002-04-09
 */
public class Location
{
    // Row and column positions. // Posições de linha e coluna
    private int row;
    private int col;

    /**
     * Represent a row and column. // Representam a linha e a coluna.
     * @param row The row. // @param fileira A linha.
     * @param col The column. @param Coluna A coluna.
     */
    public Location(int row, int col)
    {
        this.row = row;
        this.col = col;
    }
    
    /**
     * Implement content equality.
     * Implemtentar qualidade de conteúdo.
     */
    public boolean equals(Object obj)
    {
        if(obj instanceof Location) {
            Location other = (Location) obj;
            return row == other.getRow() && col == other.getCol();
        }
        else {
            return false;
        }
    }
    
    /**
     * Return a string of the form row,column
     * @return A string representation of the location.
     * 
     * Retorna uma string da linha, coluna do formulário
     * @return Uma representação de string do local. 
     */
    public String toString()
    {
        return row + "," + col;
    }
    
    /**
     * Use the top 16 bits for the row value and the bottom for
     * the column. Except for very big grids, this should give a
     * unique hash code for each (row, col) pair.
     * 
     * Use os 16 bits superiores para o valor da linha e os inferiores para
     * a coluna. Exceto para grades muito grandes, isso deve dar uma
     * código hash exclusivo para cada par (linha, coluna).
     * / 
     */
    public int hashCode()
    {
        return (row << 16) + col;
    }
    
    /**
     * @return The row.
     * @return A linha.
     */
    public int getRow()
    {
        return row;
    }
    
    /**
     * @return The column.
     * @return A coluna.
     */
    public int getCol()
    {
        return col;
    }
}
