package sudoku.cli;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class HelpCommand extends Command
{
    private final CLI cli;
    
    private static final Pattern syntax =
        Pattern.compile ("^\\s*(help|\\?)\\s*$");
    
    Pattern getSyntax ()
    {
        return syntax;
    }
    
    HelpCommand (CLI cli)
    {
        this.cli = cli;
    }
    
    String getHelp ()
    {
        return "help or ?:\tGet help on commands";
    }
    
    boolean apply (String cmdline, MatchResult res, sudoku.puzzle.Grid g)
    {
        System.out.println ("Commands:");
        cli.getCommandList().forEach ((command) -> {
            System.out.println (command.getHelp ());
        });
        
        return true;
    }
}
