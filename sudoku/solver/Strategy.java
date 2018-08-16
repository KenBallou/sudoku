package sudoku.solver;

import sudoku.puzzle.Grid;

public interface Strategy
{
    public boolean apply (Grid g) throws Grid.UnsolvableException;
}