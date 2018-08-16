package sudoku.solver;

import sudoku.puzzle.Grid;
import sudoku.puzzle.Cell;
import java.util.Collection;

public class SingleCandidate implements Strategy
{
    @Override
    public boolean apply (Grid g) throws Grid.UnsolvableException
    {
        Collection<Cell> cells = g.getCells();
        boolean retval = false;
        
        for (Cell c : cells)
        {
            if (c.valueKnown ())
            {
                continue;
            }
            if (c.getCandidateCount() == 0)
            {
                throw new Grid.UnsolvableException ();
            }
            else if (c.getCandidateCount () == 1)
            {
                int v = c.getCandidates().elementAt(0);
                g.setCell(c, v);
                retval = true;
            }
        }
        
        return retval;
    }
}