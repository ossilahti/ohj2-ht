package fxMmfinaalit;
	
import javafx.application.Application;
import javafx.application.Platform;
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

            primaryStage.setOnCloseRequest((event) -> {
                 if ( !tietokantaCtrl.voikoSulkea() ) event.consume();
            });
        
            Tietokanta tietokanta = new Tietokanta();  
            tietokantaCtrl.setTietokanta(tietokanta); 
        
            primaryStage.show();
        
            Application.Parameters params = getParameters(); 
            if ( params.getRaw().size() > 0 ) 
            	tietokantaCtrl.lueTiedosto(params.getRaw().get(0));  
            else 
            	Platform.exit();

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
}
