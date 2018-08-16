package sudoku.gui;

public class MenuBar extends javafx.scene.control.MenuBar
{
    public MenuBar (GridPresentation gp)
    {
        super ();
        
        javafx.scene.control.Menu puzzleMenu =
            new javafx.scene.control.Menu ("Puzzle");
        
        javafx.scene.control.Menu checkpointMenu =
            new javafx.scene.control.Menu ("Checkpoint");
        
        puzzleMenu.getItems().addAll (
            new PuzzleFinalizeMenuItem (gp),
            new PuzzleClearMenuItem (gp),
            new PuzzleResetMenuItem (gp),
            new PuzzleSolveMenuItem (gp),
            new PuzzleQuitMenuItem (gp)
        );
        
        checkpointMenu.getItems().addAll (
            new CheckpointSaveMenuItem (gp),
            new CheckpointRestoreMenuItem (gp));
        
        this.getMenus().addAll(puzzleMenu, checkpointMenu);
    }
}
