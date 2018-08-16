/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku.cli;

import sudoku.puzzle.Grid;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 *
 * @author ken
 */
class ExitCommand extends Command
{
    private final static java.util.regex.Pattern syntax =
        Pattern.compile ("\\s*(exit|q(uit)?)\\s*");
    
    Pattern getSyntax ()
    {
        return syntax;
    }
    
    String getHelp ()
    {
        return "exit or quit or q:\tTerminate the program";
    }
    
    boolean apply (String cmdline, MatchResult res, Grid g)
    {
        return false;
    }
}
