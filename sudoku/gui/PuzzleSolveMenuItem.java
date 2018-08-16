package sudoku.gui;

public class PuzzleSolveMenuItem extends javafx.scene.control.MenuItem
{
    private final GridPresentation gp;

    public PuzzleSolveMenuItem (GridPresentation gp)    
    {
        super ("Solve");
        
        this.gp = gp;
        
        setOnAction ((e) -> {
            gp.solve ();
        });
    }
}
