package sudoku.cli;

import java.util.Collection;
import sudoku.puzzle.Cell;
import sudoku.puzzle.Grid;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class ShowCommand extends Command
{
    private final static Pattern syntax =
        Pattern.compile ("\\s*show\\s*");
    
    Pattern getSyntax ()
    {
        return syntax;
    }
    
    String getHelp ()
    {
        return "show:\tShow the current state of the puzzle";
    }
    
    boolean apply (String cmdline, MatchResult res, Grid g)
    {
        g.getRows().forEach((Collection<Cell> row) -> {
            row.forEach ((Cell c) -> {
                if (c.valueKnown())
                {
                    System.out.print(c.getValue());
                }
                else
                {
                    System.out.print(".");
                }
            });
            System.out.println("");
        });
        
        return true;
    }
}
