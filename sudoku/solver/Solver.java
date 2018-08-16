package sudoku.solver;

import sudoku.puzzle.Grid;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;

public class Solver extends Observable implements Observer
{
    private final Grid g;

    private final ArrayList <Strategy> strategies;
    
    public Solver (Grid g)
    {
        this.g = g;
        strategies = new ArrayList<> ();
        strategies.add (new SingleCandidate ());
        strategies.add (new OnlyCellForValue ());
        strategies.add (new ReducePairs ());
        strategies.add (new Guess ());
    }
    
    private boolean doPass ()
    {
        for (Strategy strategy : strategies)
        {
            try
            {
                if (strategy.apply (g))
                {
                    return true;
                }
            }
            catch (Grid.UnsolvableException e)
            {
                System.out.println ("Caught UnsolvableException");
                return false;
            }
        }

        System.out.println ("UGH, doPass() returning false!");
        return false;
    }
    
    public boolean solve ()
    {
        g.addObserver (this);
        
        g.initializeCandidates ();
        
        try
        {
            while (g.getFilledCount () < g.CELLS)
            {
                if (! doPass ())
                {
                    g.deleteObserver(this);
                    return false;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println (e.toString());
            e.printStackTrace();
        }

        g.deleteObserver(this);
        return true;
    }
    
    public void update (Observable o, Object arg)
    {
        setChanged ();
        notifyObservers (arg);
    }
}