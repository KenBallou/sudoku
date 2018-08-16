package sudoku.puzzle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Observable;
import java.util.Vector;

public class Grid extends Observable implements Cloneable
{
    private final Vector <Cell> cells = new Vector <Cell> ();

    // In theory, once we eliminate all hard-coded constants, we should be
    // able to manage sudoku puzzles of size other than 9x9.  For now, though,
    // we assume the puzzle grid is 9x9 with 3x3 boxes.

    public static final int ROWS = 9; 
    public static final int COLUMNS = 9;
    public static final int ROWS_PER_BOX = 3;
    public static final int COLUMNS_PER_BOX = 3;
    public static final int BOXES_VERTICAL = ROWS / ROWS_PER_BOX;
    public static final int BOXES_HORIZONTAL = COLUMNS / COLUMNS_PER_BOX;
    public static final int CELLS = ROWS * COLUMNS;
    
    // These are collections that tell us which cells are in any given
    // row, column, or box.
    private final ArrayList <Collection <Cell> > rows =
        new ArrayList <> ();
    private final ArrayList <Collection <Cell> > columns =
        new ArrayList <> ();
    private final ArrayList <Collection <Cell> > boxes =
        new ArrayList <> ();

    // These maps allow us to determine the row, column, and box
    // to which any given cell belongs.
    private final HashMap <Cell, Collection<Cell> > rowMap =
        new HashMap <> ();
    private final HashMap <Cell, Collection<Cell> > columnMap =
        new HashMap <> ();
    private final HashMap <Cell, Collection<Cell> > boxMap =
        new HashMap <> ();

    // Cell coordinates as a structure.
    // We don't try to hide the row and column fields.  This really is meant
    // to be used as a C struct.  We need this to be able to return both a row
    // and a column from a function.
    
    public static class UnsolvableException extends Exception
    {
    }
    
    public static class CellCoordinates
    {
        public int row;
        public int column;
        
        public CellCoordinates (int r, int c)
        {
            row = r;
            column = c;
        }
        
        public boolean isEqual (CellCoordinates other)
        {
            return ((this.row == other.row) && (this.column == other.column));
        }
    }
    
    public static class Notification
    {
        public CellCoordinates coord;
        public int value;
        
        Notification (CellCoordinates coord, int value)
        {
            this.coord = coord;
            this.value = value;
        }
        
        Notification (int row, int column, int value)
        {
            this.coord = new CellCoordinates (row, column);
            this.value = value;
        }
    }

    public Grid ()
    {
        int row, col;
        
        for (row = 0; row < ROWS; ++ row)
        {
            for (col = 0; col < COLUMNS; ++ col)
            {
                cells.add (new Cell (this, row, col));
            }
        }

        initializeGeometry ();
    }

    public Grid (Grid other)
    {
        for (Cell c : other.cells)
        {
            Cell clonedCell = new Cell (c);
            clonedCell.setContainer (this);
            this.cells.add (clonedCell);
        }

        initializeGeometry ();
    }

    // This function hides the detail of how a two-dimensional
    // grid is stored in a one-dimensional collection.
    // NOTE: this function assumes zero-based indexing for row and column.
    private int mapCoordinatesToIndex (int row, int column)
    {
        return COLUMNS * row + column;
    }
    
    // CellCoordinates are one-based.
    private int mapCoordinatesToIndex (CellCoordinates coords)
    {
        return mapCoordinatesToIndex (coords.row - 1, coords.column - 1);
    }
    
    private void initializeGeometry ()
    {
        int row, col;
        ArrayList <Cell> tempRow, tempColumn, tempBox;

        // Initialize the row map.
        for (row = 0; row < ROWS; ++ row)
        {
            tempRow = new ArrayList <Cell> ();

            for (col = 0; col < COLUMNS; ++ col)
            {
                tempRow.add (cells.elementAt (mapCoordinatesToIndex (row, col)));
            }

            for (Cell c : tempRow)
            {
                rowMap.put (c, tempRow);
            }

            rows.add (tempRow);
        }

        // Initialize the column map
        for (col = 0; col < COLUMNS; ++ col)
        {
            tempColumn = new ArrayList <Cell> ();
            
            for (row = 0; row < ROWS; ++ row)
            {
                tempColumn.add (cells.elementAt (mapCoordinatesToIndex (row, col)));
            }
            
            for (Cell c : tempColumn)
            {
                columnMap.put (c, tempColumn);
            }
        
            columns.add (tempColumn);
        }

        // Initialize the box map.
        for (row = 0; row < ROWS; row += ROWS_PER_BOX)
        {
            for (col = 0; col < COLUMNS; col += COLUMNS_PER_BOX)
            {
                tempBox = new ArrayList <Cell> ();

                for (int i = 0; i < ROWS_PER_BOX; ++ i)
                {
                    for (int j = 0; j < COLUMNS_PER_BOX; ++ j)
                    {
                        tempBox.add (cells.elementAt (
                            COLUMNS * (row + i) + (col + j)));
                    }
                }

                for (Cell c : tempBox)
                {
                    boxMap.put (c, tempBox);
                }

                boxes.add (tempBox);
            }
        }
    }

    public Cell getCell (int row, int column)
    {
        return cells.elementAt (mapCoordinatesToIndex (row, column));
    }
    
    public Cell getCell (CellCoordinates coords)
    {
        return cells.elementAt (mapCoordinatesToIndex (coords));
    }

    public void setCell (int row, int column, int value)
    {
        Cell c = getCell (row, column);

        c.setValue (value);
    }

    public void setCell (CellCoordinates coords, int value)
    {
        setCell (coords.row - 1, coords.column - 1, value);
    }

    public void setCell (Cell c, int value)
    {
        if (c.setValue (value))
        {
            setChanged ();
            notifyObservers (new Notification (c.getRow() + 1,
                                               c.getColumn() + 1,
                                               value));
        }
    }
    
    public Collection<Cell> getCells ()
    {
        return cells;
    }
    
    public ArrayList <Collection <Cell> > getRows ()
    {
        return rows;
    }
    
    public ArrayList <Collection <Cell> > getColumns ()
    {
        return columns;
    }
    public ArrayList <Collection <Cell> > getBoxes ()
    {
        return boxes;
    }

    public Collection<Cell> getRowForCell (Cell c, boolean includeSelf)
    {
        Collection<Cell> neighbors = rowMap.get (c);

        if (! includeSelf)
        {
            neighbors = new ArrayList <Cell> (neighbors);
            neighbors.remove (c);
        }

        return neighbors;
    }

    public Collection<Cell> getColumnForCell (Cell c, boolean includeSelf)
    {
        Collection<Cell> neighbors = columnMap.get (c);

        if (! includeSelf)
        {
            neighbors = new ArrayList <Cell> (neighbors);
            neighbors.remove (c);
        }

        return neighbors;
    }

    public Collection<Cell> getBoxForCell (Cell c, boolean includeSelf)
    {
        Collection<Cell> neighbors = boxMap.get (c);

        if (! includeSelf)
        {
            neighbors = new ArrayList <Cell> (neighbors);
            neighbors.remove (c);
        }

        return neighbors;
    }

    public int getFilledCount ()
    {
        return (int) cells.stream ()
                    .filter((Cell c) -> {
                        return c.valueKnown();
                    })
                    .count ();
    }

    public void initializeCandidates ()
    {
        for (Cell c: cells)
        {
            c.resetCandidates ();
        }
        
        for (Cell c : cells)
        {
            c.updateNeighborCandidates ();
        }
    }

    public void clear (boolean clearLocked)
    {
        cells.forEach ((Cell c) -> {
            if (c.isLocked ())
            {
                if (clearLocked)
                {
                    c.lock (false);
                    c.setValue (Cell.NO_VALUE);
                }
            }
            else
            {
                c.setValue (Cell.NO_VALUE);
            }
        });
    }
    
    public void setFinalized ()
    {
        cells.forEach ((c) -> {
            if (c.valueKnown ())
                c.lock (true);
        });
    };
    
    public void save (java.io.Writer output)
    {
        ArrayList <Cell> finalizedCells = new ArrayList <> ();
        ArrayList <Cell> otherCells = new ArrayList <> ();
        
        cells.forEach ((c) -> {
            if (c.isLocked ())
            {
                finalizedCells.add (c);
            }
            else
            {
                otherCells.add (c);
            }
        });
        
        try
        {
            java.io.BufferedWriter bw = new java.io.BufferedWriter(output);
            
            for (Cell c: finalizedCells)
            {
                bw.write ("set (" + (c.getRow() + 1) + "," +
                    (c.getColumn()+1) + ") = " + c.getValue () + "\n");
            };
            
            bw.write ("finalize\n");

            for (Cell c : otherCells)
            {
                if (c.valueKnown())
                {
                    bw.write ("set (" + (c.getRow() + 1) + "," +
                        (c.getColumn()+1) + ") = " + c.getValue () + "\n");
                }
            };

            bw.flush ();
            bw.close ();
        }
        catch (java.io.IOException e)
        {
            System.err.println ("I/O exception saving puzzle");
        }
    }
}
