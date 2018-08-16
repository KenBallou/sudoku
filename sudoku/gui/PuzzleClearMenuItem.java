package sudoku.gui;

public class PuzzleClearMenuItem extends javafx.scene.control.MenuItem
{
    private final GridPresentation gp;
    
    public PuzzleClearMenuItem (GridPresentation gp)
    {
        super ("Clear");
        
        this.gp = gp;
        setOnAction ((javafx.event.ActionEvent e) -> {
            gp.getGrid().clear(true);
            gp.redraw();
        });
    }
}
