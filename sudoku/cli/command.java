package sudoku.cli;

import sudoku.puzzle.Grid;
import java.util.regex.Pattern;
import java.util.regex.MatchResult;

abstract class Command
{
    abstract Pattern getSyntax ();
    
    abstract String getHelp ();
    
    abstract boolean apply (String cmdline, MatchResult res, Grid g);
}