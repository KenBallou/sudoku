package sudoku.gui;

import java.util.ArrayDeque;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import sudoku.puzzle.Grid;
import sudoku.puzzle.Cell;
import sudoku.solver.Solver;

public class GridPresentation extends Canvas implements java.util.Observer
{
    private Grid g;

    // Geometry values.  Unfortunately, these can't be "final" because they
    // are assigned in a function called by the constructor, not by the
    // constructor itself.
    private int pixelsPerRow;
    private int pixelsPerColumn;
    private int boxWidth;
    private int boxHeight;
    
    private int paddingTop;
    private int paddingLeft;
    private final int minimumPadding = 5;

    // In both horizontal and vertical directions, there are four heavy rules
    // of thickness 3 and six light rules of thickness 1.
    
    private final int thickRulePixels = 3;
    private final int thinRulePixels = 1;
    private final int pixelsForRules =
        (Grid.BOXES_HORIZONTAL + 1) * thickRulePixels +
            Grid.BOXES_HORIZONTAL * (Grid.COLUMNS_PER_BOX - 1) * thinRulePixels;

    private final Font fr = new Font ("SansSerif Regular", 18);
    private final Font fb = new Font ("SansSerif Bold", 18);
    private final double textWidth;
    private final double textHeight;
    
    private final Paint selectedCellBackground = Color.BISQUE;
    
    // Coordinates of the currently selected cell, if any
    private Grid.CellCoordinates selectedCellCoordinates;

    // Stack of checkpoints (java.util.Stack is deprecated)
    private final ArrayDeque <Grid> checkpoints = new ArrayDeque <Grid> ();
    
    private void setupGeometry (double width, double height)
    {
        pixelsPerColumn =
            (int) (width - (2 * minimumPadding + pixelsForRules)) / Grid.COLUMNS;
        pixelsPerRow =
           (int) (height - (2 * minimumPadding + pixelsForRules)) / Grid.ROWS;
        
        // Adjust the left and top padding to be half of whatever is left over
        paddingLeft =
            ((int) width - (Grid.COLUMNS * pixelsPerColumn + pixelsForRules)) / 2;
        paddingTop =
            ((int) height - (Grid.ROWS * pixelsPerRow + pixelsForRules)) / 2;
        
        // To calculate box geometry (a box being a 3x3 grid of cells),
        // allow the  thick left/top rule of three pixels plus two rules
        // of one pixel each.
        boxWidth =
            thickRulePixels +
                Grid.COLUMNS_PER_BOX * pixelsPerColumn +
                (Grid.COLUMNS_PER_BOX - 1) * thinRulePixels;
        boxHeight =
            thickRulePixels +
                Grid.ROWS_PER_BOX * pixelsPerRow +
                (Grid.ROWS_PER_BOX - 1) * thinRulePixels;
    }
    
    private Grid.CellCoordinates mapPointToCellCoordinates (double x, double y)
    {
        int row, column;
        int temp;

        if ((x < paddingLeft) || (y < paddingTop))
        {
            return null;
        }
        
        // Find the row index.
        temp = (((int) y) - paddingTop) / boxHeight;
        row = Grid.ROWS_PER_BOX * temp;
        temp = ((((int) y) - paddingTop) % boxHeight) / pixelsPerRow;
        row = row + temp + 1;   // one-=based indexing
        
        // Find the column index.
        temp = (((int) x) - paddingLeft) / boxWidth;
        column = Grid.COLUMNS_PER_BOX * temp;
        temp = ((((int) x) - paddingLeft) % boxWidth) / pixelsPerColumn;
        column = column + temp + 1;

        if ((row >= 1) && (row <= Grid.ROWS) &&
            (column >= 1) && (column <= Grid.COLUMNS))
        {
            return new Grid.CellCoordinates (row, column);
        }
        else
        {
            return null;
        }
    }
    
    private Rectangle getInteriorRectangleForCell (Grid.CellCoordinates coord)
    {
        if (coord == null)
        {
            return null;
        }

        int numBoxesHorizontal = (coord.column - 1) / Grid.COLUMNS_PER_BOX;
        int numBoxesVertical = (coord.row - 1) / Grid.ROWS_PER_BOX;
        
        int offsetX =
            paddingLeft + thickRulePixels + numBoxesHorizontal * boxWidth;
        int offsetY =
            paddingTop + thickRulePixels + numBoxesVertical * boxHeight;
        
        offsetX = offsetX +
            ((coord.column - 1) % Grid.COLUMNS_PER_BOX) *
                (pixelsPerColumn + thinRulePixels);
        offsetY = offsetY +
            ((coord.row - 1) % Grid.ROWS_PER_BOX) *
                (pixelsPerRow + thinRulePixels);
        // Shave one pixel off each edge to avoid touching grid lines.
        return new Rectangle (offsetX + 1, offsetY + 1,
                              pixelsPerColumn - 1, pixelsPerRow - 1);
    }
    
    private void setSelectedCell (Grid.CellCoordinates coord)
    {
        Rectangle r;
        GraphicsContext gc = getGraphicsContext2D ();

        Grid.CellCoordinates previous = selectedCellCoordinates;

        selectedCellCoordinates = coord;
        
        drawCell (previous);
        drawCell (selectedCellCoordinates);
    }
    
    private void drawGridLines ()
    {
        GraphicsContext gc = getGraphicsContext2D ();
        gc.setLineWidth (thinRulePixels);
        // gc.setLineWidth(thickRulePixels);
        
        // Draw horizontal heavy rules for box boundaries
        for (int i = 0; i <= Grid.BOXES_VERTICAL; ++ i)
        {
            for (int j = 0; j < thickRulePixels; ++ j)
            {
                gc.strokeLine (paddingLeft, paddingTop + i * boxHeight + j,
                    paddingLeft + Grid.BOXES_HORIZONTAL * boxWidth,
                    paddingTop + i * boxHeight + j);
            }
        }
        
        // Draw vertical heavy rules for box boundaries
        for (int i = 0; i <= Grid.BOXES_HORIZONTAL; ++ i)
        {
            for (int j = 0; j < thickRulePixels; ++ j)
            {
                gc.strokeLine (paddingLeft + i * boxWidth + j, paddingTop,
                               paddingLeft + i * boxWidth + j,
                               paddingTop + Grid.BOXES_VERTICAL * boxWidth);
            }
        }

        // gc.setLineWidth(thinRulePixels);
        
        // Draw thin horizontal inter-box lines
        for (int i = 0; i < Grid.BOXES_VERTICAL; ++ i)
        {
            for (int j = 0; j < Grid.ROWS_PER_BOX; ++ j)
            {
                gc.strokeLine (
                    paddingLeft + thickRulePixels,
                    paddingTop + thickRulePixels + i * boxHeight + j * (pixelsPerRow + 1),
                    paddingLeft + thickRulePixels + Grid.BOXES_HORIZONTAL * boxWidth,
                    paddingTop + thickRulePixels + i * boxHeight + j * (pixelsPerRow + 1));
            }
        }

        // Draw thin vertical inter-box lines
        for (int i = 0; i < Grid.BOXES_HORIZONTAL; ++ i)
        {
            for (int j = 0; j < Grid.COLUMNS_PER_BOX; ++ j)
            {
                gc.strokeLine (
                    paddingLeft + thickRulePixels + i * boxWidth + j * (pixelsPerColumn + 1),
                    paddingTop + thickRulePixels,
                    paddingLeft + thickRulePixels + i * boxWidth + j * (pixelsPerColumn + 1),
                    paddingTop + thickRulePixels + Grid.BOXES_VERTICAL * boxHeight);
            }
        }
    }

    private void drawCell (Grid.CellCoordinates coord)
    {
        if (coord != null)
        {
            drawCellBackground (coord);
            drawCellValue (coord);
        }
    }
    
    private void drawCellBackground (Grid.CellCoordinates coord)
    {
        GraphicsContext gc = getGraphicsContext2D ();
        Rectangle r = getInteriorRectangleForCell (coord);


        gc.clearRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
        
        if ((selectedCellCoordinates != null) &&
            (coord.isEqual (selectedCellCoordinates)))
        {
            gc.setFill (selectedCellBackground);
            gc.fillRect (r.getX (), r.getY (), r.getWidth (), r.getHeight ());
        }
    }
    
    private void drawCellValue (Grid.CellCoordinates coord)
    {
        Cell c = g.getCell(coord);
        GraphicsContext gc = getGraphicsContext2D ();
        
        if (c.isLocked())
        {
            gc.setFont (fb);
        }
        else
        {
            gc.setFont (fr);
        }

        if (c.valueKnown())
        {
            Rectangle r = getInteriorRectangleForCell(coord);
            Integer v = new Integer (c.getValue ());
            double x = r.getX() + (r.getWidth() - textWidth)/2;
            double y = r.getY() + (r.getHeight() + textHeight)/2;
            gc.setFill (Color.BLACK);
            gc.fillText (v.toString(), x, y);
        }
    }

    void redraw ()
    {
        drawGridLines ();
        for (int i = 1; i <= Grid.ROWS; ++ i)
        {
            for (int j = 1; j <= Grid.COLUMNS; ++ j)
            {
                Grid.CellCoordinates coord = new Grid.CellCoordinates (i, j);
                drawCell (coord);
            }
        }
    }
    
    void saveCheckpoint ()
    {
        checkpoints.push (g);
        g = new Grid (g);
    }
    
    void restoreCheckpoint ()
    {
        if (! checkpoints.isEmpty ())
        {
            g = checkpoints.pop ();
            redraw ();
        }
    }
    
    public void solve ()
    {
        Solver solver = new Solver (g);
        
        solver.addObserver (this);
        
        if (solver.solve ())
        {
            // redraw ();
        }
        
        solver.deleteObserver(this);
    }
    
    public void update (java.util.Observable o, Object arg)
    {
        Grid.Notification notification = (Grid.Notification) arg;
        
        drawCell (notification.coord);
    }
    
    public Grid getGrid ()
    {
        return g;
    }
    
    public GridPresentation (double width, double height)
    {
        super (width, height);

        g = new Grid ();
        
        setupGeometry (width, height);

        drawGridLines ();
        
        Text temp = new Text ("8");
        temp.setFont (fb);
        textWidth = temp.getLayoutBounds().getWidth();
        textHeight = temp.getLayoutBounds().getHeight();

        setFocusTraversable(true);

        // Handle mouse clicks by setting the current selected cell
        this.setOnMouseClicked((MouseEvent e) -> {
            //requestFocus ();
            Grid.CellCoordinates newSelection =
                mapPointToCellCoordinates(e.getX(), e.getY());
            setSelectedCell (newSelection);
        });

        // Check key presses for left/right/up/down arrows and do
        // navigation on the selected cell
        this.setOnKeyPressed((KeyEvent e) -> {
            Grid.CellCoordinates coord = null;
            switch (e.getCode())
            {
                case UP:
                    if ((selectedCellCoordinates != null) &&
                        (selectedCellCoordinates.row != 1))
                    {
                        coord = new Grid.CellCoordinates(
                            selectedCellCoordinates.row - 1,
                            selectedCellCoordinates.column);
                    }
                    break;
                case DOWN:
                    if ((selectedCellCoordinates != null) &&
                        (selectedCellCoordinates.row != Grid.ROWS))
                    {
                        coord = new Grid.CellCoordinates (
                            selectedCellCoordinates.row + 1,
                            selectedCellCoordinates.column);
                    }
                    break;
                case LEFT:
                    if ((selectedCellCoordinates != null) &&
                        (selectedCellCoordinates.column != 1))
                    {
                        coord = new Grid.CellCoordinates (
                            selectedCellCoordinates.row,
                            selectedCellCoordinates.column - 1);
                    }
                    break;
                case RIGHT:
                    if ((selectedCellCoordinates != null) &&
                        (selectedCellCoordinates.column != Grid.COLUMNS))
                    {
                        coord = new Grid.CellCoordinates (
                            selectedCellCoordinates.row,
                            selectedCellCoordinates.column + 1);
                    }
                    break;
                case END:
                    Solver solver = new Solver (g);
                    solver.solve ();
                    redraw ();
                    break;
                default:
                    break;
            }
            
            if (coord != null)
            {
                setSelectedCell(coord);
            }
        });

        // Handle key typed events by checking for a number and, if so,
        // setting the value of the currently selected cell to the input
        this.setOnKeyTyped((KeyEvent e) -> {
            if (selectedCellCoordinates != null)
            {
                try
                {
                    int v = new Integer (e.getCharacter());
                    g.setCell(selectedCellCoordinates, v);
                    drawCell(selectedCellCoordinates);
                }
                catch (NumberFormatException ex)
                {
                }
            }
        });
    }
}