/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author ken
 */
public class GUI extends Application
{
    private GridPresentation gp;
    
    public GridPresentation getGrid ()
    {
        return gp;
    }
    
    @Override
    public void start(Stage primaryStage) {
        gp = new GridPresentation (300, 300);
        MenuBar menubar = new MenuBar (gp);
        menubar.setUseSystemMenuBar (true);
        menubar.setVisible (true);
        
        VBox root = new VBox ();
        root.getChildren().addAll (menubar, gp);
        
        Scene scene = new Scene(root, 300, 350);
        
        primaryStage.setTitle("Sudoku");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
