package fxMmfinaalit;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class StartingWindowController {


	    /**
         * Kun tätä metodia kutsutaan, ikkuna vaihtuu MMFINAALITGUIVIEW.FXML:ään.
         */
        @FXML private void handleVaihdaIkkunaa(ActionEvent event) throws IOException
        {
            Parent paaikkuna = FXMLLoader.load(getClass().getResource("MMFinaalitGUIView.fxml"));
            Scene paaikkunaScene = new Scene(paaikkuna,600,400);
            
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            
            window.setScene(paaikkunaScene);
            window.show();
        }

	   
}

