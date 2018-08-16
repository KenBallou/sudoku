package sudoku.gui;

public class CheckpointRestoreMenuItem extends javafx.scene.control.MenuItem
{
    private final GridPresentation gp;
    
    public CheckpointRestoreMenuItem (GridPresentation gp)    
    {
        super ("Restore");
        this.gp = gp;
        setOnAction ((e) -> {
            gp.restoreCheckpoint ();
        });
    }
}
