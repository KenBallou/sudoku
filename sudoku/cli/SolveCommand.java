package sudoku.cli;

import sudoku.puzzle.Grid;
import sudoku.solver.Solver;
import java.util.Observer;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

class SolveCommand extends Command implements Observer
{
    private final static java.util.regex.Pattern syntax =
        Pattern.compile ("\\s*solve\\s*");

    Pattern getSyntax ()
    {
        return syntax;
    }
    
    String getHelp ()
    {
        return "solve:  Solve the puzzle";
    }
    
    boolean apply (String cmdline, MatchResult res, Grid g)
    {
        Solver solver = new Solver (g);
        
        solver.addObserver(this);
        
        if (! solver.solve ())
        {
            System.out.println ("FAILURE SOLVING PUZZLE!");
        }
        
        solver.deleteObserver(this);
        
        return true;
    }
    
    public void update (java.util.Observable o, Object arg)
    {
        Grid.Notification notification = (Grid.Notification) arg;
        
        System.out.println ("Set (" + notification.coord.row + "," +
                notification.coord.column + ") to " + notification.value);
    }
}
