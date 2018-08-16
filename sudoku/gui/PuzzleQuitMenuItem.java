package sudoku.gui;

public class PuzzleQuitMenuItem extends javafx.scene.control.MenuItem
{
    public PuzzleQuitMenuItem (GridPresentation gp)
    {
        super ("Quit");

        setOnAction ((e) -> {
            javafx.application.Platform.exit ();
        });
    }
}
