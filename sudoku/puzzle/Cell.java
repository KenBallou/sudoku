 package sudoku.puzzle;

import java.util.Vector;
import java.util.Collection;

class NoCandidatesException extends Exception
{
}

class CellLockedException extends Exception
{
}

public class Cell
{
    private Vector<Integer> candidates;
    
    private int value;

    private boolean locked = false;
    
    private Grid container;

    private final int row;
    private final int column;
    
    static public final int NO_VALUE = 0;
    static public final int MAX_VALUE = Grid.ROWS;
    
    static private Vector<Integer> initialCandidates = new Vector<Integer>();

    static
    {
        for (int i = 1; i <= 9; ++ i)
        {
            initialCandidates.add (i);
        }
    }

    public Cell (Grid g, int r, int c)
    {
        candidates = new Vector<Integer> (initialCandidates);
        value = NO_VALUE;
        container = g;
        row = r;
        column = c;
    }

    Cell (Cell other)
    {
        this.container = null;
        this.value     = other.value;
        this.row       = other.row;
        this.column    = other.column;

        this.candidates = new Vector<Integer> ();
        for (Integer i : other.candidates)
        {
            this.candidates.add (i);
        }
    }

    void setContainer (Grid g)
    {
        container = g;
    }

    boolean isCandidate (int v)
    {
        return candidates.contains (v);
    }

    public void removeCandidate (int v)
    {
        if (candidates.contains (v))
        {
            // System.out.println ("Remove candidate " + v + " from cell " + getCoordinates ());
        }

        candidates.remove (new Integer (v));
    }

    public int removeCandidates (Collection<Integer> values)
    {
        for (int v : values)
        {
            removeCandidate (v);
        }

        return candidates.size ();
    }

    public void resetCandidates ()
    {
        candidates = new Vector <Integer> (initialCandidates);
    }
    
    public boolean valueKnown ()
    {
        return value != NO_VALUE;
    }

    public int getValue ()
    {
        return value;
    }
    
    public boolean isLocked ()
    {
        return locked;
    }
    
    public int getRow ()
    {
        return row;
    }

    public int getColumn ()
    {
        return column;
    }

    public int getCandidateCount ()
    {
        return candidates.size ();
    }

    public Vector<Integer> getCandidates ()
    {
        return candidates;
    }

    // Get the presentation coordinates (1-based) of the cell.
    public String getCoordinates ()
    {
        return "(" + (getRow () + 1) + "," + (getColumn () + 1) + ")";
    }

    // This method is package protected (only accessible from classes in the
    // sudoku.puzzle package) so we can enforce that all attempts to set a
    // cell value outside the package must go through the grid.  This
    // restriction allows the grid to maintain an undo list (for example).
    boolean setValue (int v)
    {
        if (locked)
        {
            return false;
        }
    
        if ((v >= NO_VALUE) && (v <= MAX_VALUE))
        {
            value = v;
            updateNeighborCandidates ();
            return true;
        }
        
        return false;
    }

    // This sets the cell's value but does not update the candidates for the
    // associated row, column, or box.  (This is useful for initializing the
    // grid before solving, as it allows errors to be corrected.)

    public void notateValue (int v)
    {
        if ((value > NO_VALUE) && (value <= MAX_VALUE))
        {
            value = v;
            locked = true;
        }
    }

    public void updateNeighborCandidates ()
    {
        Collection<Cell> neighbors;

        if (value != NO_VALUE)
        {
            candidates.clear ();

            neighbors = container.getRowForCell (this, false);
            for (Cell c : neighbors)
            {
                c.removeCandidate (value);
            }

            neighbors = container.getColumnForCell (this, false);
            for (Cell c : neighbors)
            {
                c.removeCandidate (value);
            }

            neighbors = container.getBoxForCell (this, false);
            for (Cell c : neighbors)
            {
                c.removeCandidate (value);
            }
        }
    }
    
    public void lock (boolean cellLocked)
    {
        locked = cellLocked;
    }
}