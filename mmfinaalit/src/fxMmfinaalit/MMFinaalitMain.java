package fxMmfinaalit;
	
import javafx.application.Application;
import javafx.stage.Stage;
import tietorakenne.Tietokanta;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


public class MMFinaalitMain extends Application {
	@Override
	public void start(Stage primaryStage) {
        try {
            final FXMLLoader ldr = new FXMLLoader(getClass().getResource("MMFinaalitGUIView.fxml"));
            final Pane root = (Pane)ldr.load();
            final MMFinaalitGUIController tietokantaCtrl = (MMFinaalitGUIController)ldr.getController();

            final Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("mmfinaalit.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("MM-Finaalit");
            
            // Platform.setImplicitExit(false); // tätä ei kai saa laittaa

           // primaryStage.setOnCloseRequest((event) -> {
             //       if ( !tietokantaCtrl.voikoSulkea() ) event.consume();
               // });
            
            Tietokanta tietokanta = new Tietokanta();  
            tietokantaCtrl.setTietokanta(tietokanta); 
           
            
            primaryStage.show();
            //if ( !tietokantaCtrl.avaa() ) Platform.exit();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
	
    /**
     * Käynnistetään käyttöliittymä 
     * @param args komentorivin parametrit
     */
	public static void main(String[] args) {
		launch(args);
	}

	
	
	//public void start(Stage primaryStage) {
		//try {
			//BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("MMFinaalitGUIView.fxml"));
			//Scene scene = new Scene(root,600,400);
			//scene.getStylesheets().add(getClass().getResource("mmfinaalit.css").toExternalForm());

            

			/**
			primaryStage.setScene(scene);
			primaryStage.setTitle("MM-Finaalit");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}*/
}
