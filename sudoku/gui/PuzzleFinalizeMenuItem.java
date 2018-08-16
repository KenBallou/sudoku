package sudoku.gui;

public class PuzzleFinalizeMenuItem extends javafx.scene.control.MenuItem
{
    private final GridPresentation gp;
    
    public PuzzleFinalizeMenuItem (GridPresentation gp)
    {
        super ("Finalize");
        
        this.gp =  gp;
        
        setOnAction ((javafx.event.ActionEvent e) -> {
            gp.getGrid().setFinalized ();
            gp.redraw ();
            this.setDisable (true);
        });
    }
}
