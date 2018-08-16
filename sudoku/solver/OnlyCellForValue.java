package sudoku.solver;

import sudoku.puzzle.Grid;
import sudoku.puzzle.Cell;
import java.util.Collection;
import java.util.ArrayList;

public class OnlyCellForValue implements Strategy
{
    // Examine a group of cells for a particular candidate value.  If the value
    // is a candidate for exactly one cell in the group, we can set that cell.
    private boolean findAndSetSingleton (Grid g, int value, Collection <Cell> group)
    {
        ArrayList <Cell> temp = new ArrayList<> ();

        for (Cell c : group)
        {
            if (c.getCandidates().contains(value))
            {
                temp.add (c);
            }
        }

        if (temp.size () == 1)
        {
            g.setCell (temp.get(0), value);
            return true;
        }

        return false;
    }

    @Override
    public boolean apply (Grid g)
    {
        boolean retval = false;
        
        for (int i = 1; i <= Cell.MAX_VALUE; ++ i)
        {
            for (Collection <Cell> collection : g.getColumns ())
            {
                if (findAndSetSingleton (g, i, collection))
                {
                    retval = true;
                }
            }
            
            for (Collection <Cell> collection : g.getBoxes ())
            {
                if (findAndSetSingleton (g, i, collection))
                {
                    retval = true;
                }
            }
            
        }
        
        return retval;
    }
}