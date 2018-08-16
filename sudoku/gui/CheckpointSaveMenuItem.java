package sudoku.gui;

public class CheckpointSaveMenuItem extends javafx.scene.control.MenuItem
{
    private final GridPresentation gp;
    
    public CheckpointSaveMenuItem (GridPresentation gp)
    {
        super ("Save");
        this.gp = gp;
        setOnAction ((e) -> {
            gp.saveCheckpoint ();
        });
    }
}
