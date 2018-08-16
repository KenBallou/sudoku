package sudoku.gui;

public class PuzzleResetMenuItem extends javafx.scene.control.MenuItem
{
    private final GridPresentation gp;

    public PuzzleResetMenuItem (GridPresentation gp)
    {
        super ("Reset");
        
        this.gp = gp;
        
        setOnAction ((javafx.event.ActionEvent e) -> {
            gp.getGrid().clear(false);
            gp.redraw();
        });
    }
}
