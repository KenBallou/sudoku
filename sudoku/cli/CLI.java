package sudoku.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import sudoku.puzzle.Grid;

public class CLI
{
    private Grid g;
    
    private ArrayList<Command> commands = new ArrayList<>();
    
    public CLI ()
    {
        g = new Grid ();
        setupCommands ();
    }

    public CLI (Grid theGrid)
    {
        g = theGrid;
        setupCommands ();
    }
    
    private void setupCommands ()
    {
        commands.add (new RowCommand ());
        commands.add (new SetCommand ());
        commands.add (new ShowCommand ());
        commands.add (new SolveCommand ());
        commands.add (new SourceCommand ());
        commands.add (new ExitCommand ());
        commands.add (new HelpCommand (this));
    }
    
    public static final void main (String[] args)
    {
        CLI repl = new CLI ();

        repl.commandLoop (new InputStreamReader (System.in));
    }
    
    Collection <Command> getCommandList ()
    {
        return commands;
    }

    private boolean doCommand (String cmdline)
    {
        Matcher m;
        
        for (Command c : commands)
        {
            m = c.getSyntax().matcher(cmdline);
            
            if (m.matches ())
            {
                return c.apply (cmdline, m.toMatchResult(), g);
            }
        }
        
        System.err.println ("Unrecognized command: " + cmdline);
        
        return true;    // keep the REPL loop going
    }
    
    void commandLoop (Reader source)
    {
        BufferedReader br = new BufferedReader (source);

        while (true)
        {
            try
            {
                System.out.print ("sudoku> ");
                String cmdline = br.readLine ();
                if (cmdline == null) // end of input
                {
                    break;
                }

                if (! doCommand (cmdline))
                {
                    break;
                }
            }
            catch (IOException e)
            {
                break;
            }
        }
    }
}