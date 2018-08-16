package sudoku.solver;

import sudoku.puzzle.Grid;
import sudoku.puzzle.Cell;

public class Guess implements Strategy
{
    private int shortestCandidatesLength = Cell.MAX_VALUE + 1;
    
    private Cell guessedCell = null;
    
    @Override
    public boolean apply (Grid g)
    {
        for (Cell c : g.getCells ())
        {
            if ((! c.valueKnown ()) &&
                (c.getCandidateCount () < shortestCandidatesLength))
            {
                guessedCell = c;
                shortestCandidatesLength = c.getCandidateCount ();
            }
        }
        
        int correctValue = Cell.NO_VALUE;
        
        for (int v : guessedCell.getCandidates ())
        {
            Grid clonedGrid = new Grid (g);
            Grid.CellCoordinates coord =
                new Grid.CellCoordinates (
                    guessedCell.getRow () + 1,
                    guessedCell.getColumn () + 1);
            clonedGrid.setCell (coord, v);
            Solver solver = new Solver (new Grid (clonedGrid));
            
            if (solver.solve ())
            {
                correctValue = v;
                break;
            }
        }
        
        if (correctValue != Cell.NO_VALUE)
        {
            g.setCell(guessedCell, correctValue);
            return true;
        }
        else
        {
            return false;
        }
    }
}