package sudoku.cli;

import sudoku.puzzle.Grid;
import java.util.regex.Pattern;
import java.util.regex.MatchResult;

class SetCommand extends Command
{
    private final static java.util.regex.Pattern syntax =
        Pattern.compile ("\\s*set\\s*\\(\\s*(\\d*)\\s*,\\s*(\\d*)\\s*\\)\\s*=\\s*(\\d*)\\s*");

    Pattern getSyntax ()
    {
        return syntax;
    }

    String getHelp ()
    {
        return "set (<row>,<column>) = <value>:\tSet a cell to a value (use 0 to clear)";
    }
    
    boolean apply (String cmdline, MatchResult res, Grid g)
    {
        int row = new Integer (res.group(1));
        int col = new Integer (res.group(2));
        int val = new Integer (res.group(3));

        if ((row >= 1) && (row <= 9) &&
            (col >= 1) && (col <= 9) &&
            (val >= 1) && (val <= 9))
        {
            // Convert from presentation coordinates to logical coordinates
            g.setCell(row - 1, col - 1, val);
        }

        return true;
    }
}
