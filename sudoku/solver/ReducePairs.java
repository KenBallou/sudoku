package sudoku.solver;

import sudoku.puzzle.Grid;
import sudoku.puzzle.Cell;
import java.util.ArrayList;
import java.util.Collection;

public class ReducePairs implements Strategy
{
    // After finding an "obvious" pair in a row, column, or box, remove
    // the pair of candidate values from all other cells in the row, column,
    // or box.  If we actually do reduce any cell's candidate set, return true.
    private boolean simplifyPair (Cell c, Collection<Cell> neighbors)
    {
        boolean retval = false;

        for (Cell n : neighbors)
        {
            if ((! n.valueKnown ()) &&
                (n.getCandidateCount () == 2) &&
                (n.getCandidates().containsAll (c.getCandidates ())))
            {
                Collection<Integer> candidatePair = c.getCandidates ();

                for (Cell t : neighbors)
                {
                    if ((t != c) && (t != n))
                    {
                        int count = t.getCandidateCount ();
                        if (t.removeCandidates (candidatePair) < count)
                        {
                            retval = true;
                        }
                    }
                }
            }
        }

        return retval;
    }

    // For each cell with exactly two candidate values, examine its containing
    // row, column, and box for an "obvious" pair (possibly yielding an
    // opportunity to reduce other cells' candidate sets in the row, column,
    // or box).
    private boolean lookForSimplePairs (Grid g)
    {
        boolean retval = false;

        for (Cell c : g.getCells ())
        {
            if ((! c.valueKnown ()) && (c.getCandidateCount () == 2))
            {
                // System.out.println ("Cell " + c.getCoordinates () + " has two candidates");

                if (simplifyPair (c, g.getRowForCell (c, false)))
                {
                    retval = true;
                }

                if (simplifyPair (c, g.getColumnForCell (c, false)))
                {
                    retval = true;
                }

                if (simplifyPair (c, g.getBoxForCell (c, false)))
                {
                    retval = true;
                }
            }
        }

        return retval;
    }

    // Given two values, look for a "hidden" pair in a given row, column, or
    // box comprising the two values.  If one is found, and if this leads to
    // a reduction of the candidate set of either cell in the pair, return
    // true to indicate progress.
    private boolean hiddenPairHelper (int v1, int v2, Collection <Cell> cells)
    {
        boolean retval = false;
        ArrayList <Cell> tempBoth = new ArrayList<> ();
        ArrayList <Cell> tempOneOfPair = new ArrayList<> ();

        for (Cell c : cells)
        {
            if (! c.valueKnown ())
            {
                if (c.getCandidates().contains(v1) &&
                    c.getCandidates().contains(v2))
                {
                    tempBoth.add (c);
                }
                else if (c.getCandidates().contains(v1) ||
                         c.getCandidates().contains(v2))
                {
                    tempOneOfPair.add (c);
                }
            }
        }

        if ((tempBoth.size () == 2) && (tempOneOfPair.isEmpty ()))
        {
            for (Cell c : tempBoth)
            {
                for (int i : new ArrayList <Integer> (c.getCandidates ()))
                {
                    if ((i != v1) && (i != v2))
                    {
                        c.removeCandidate (i);
                        retval = true;
                    }
                }
            }
        }

        return retval;
    }

    // For each pair of values, examine each row, column, and box for a
    // "hidden" pair comprising the two values.
    private boolean lookForHiddenPairs (Grid g)
    {
        boolean retval = false;

        for (int i = 1; i <= Cell.MAX_VALUE; ++ i)
        {
            for (int j = i + 1; j <= Cell.MAX_VALUE; ++ j)
            {
                for ( Collection <Cell> group : g.getRows ())
                {
                    if (hiddenPairHelper (i, j, group))
                    {
                        retval = true;
                    }
                }

                for ( Collection <Cell> group : g.getColumns ())
                {
                    if (hiddenPairHelper (i, j, group))
                    {
                        retval = true;
                    }
                }

                for ( Collection <Cell> group : g.getBoxes ())
                {
                    if (hiddenPairHelper (i, j, group))
                    {
                        retval = true;
                    }
                }
            }
        }

        return retval;
    }

    public boolean apply (Grid g)
    {
        if (lookForSimplePairs (g))
        {
            return true;
        }
        else
        {
            return lookForHiddenPairs (g);
        }
    }
}