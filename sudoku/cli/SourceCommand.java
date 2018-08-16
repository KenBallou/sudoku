package sudoku.cli;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import sudoku.puzzle.Grid;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

class SourceCommand extends Command
{
    private final static java.util.regex.Pattern syntax =
        Pattern.compile ("\\s*source\\s*(\\S+)\\s*");

    Pattern getSyntax ()
    {
        return syntax;
    }
    
    String getHelp ()
    {
        return "source <pathname>:\tExecute commands from file <pathname>";
    }

    boolean apply (String cmdline, MatchResult res, Grid g)
    {
        try (FileReader fr = new FileReader (res.group (1)))
        {
            new CLI().commandLoop (fr);
        }
        catch (FileNotFoundException e)
        {
            System.out.println ("File " + res.group(1) + " not found");
        }
        catch (IOException e)
        {
        }

        return true;
    }
}