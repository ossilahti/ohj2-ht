package fxMmfinaalit;

import java.awt.Desktop;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.TextAreaOutputStream;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import tietorakenne.Finaali;
import tietorakenne.Osallistujamaa;
import tietorakenne.SailoException;
import tietorakenne.Tietokanta;

/**
 * 
 * @author Ossi Lahti
 * @version Viimeisin muokkauspäivä: 12.2.2020
 */

public class MMFinaalitGUIController implements Initializable {

    @FXML private ListChooser<Finaali> chooserFinaalit;
  	@FXML private TextField searchCriteria;
  	@FXML private TextInputControl searchField;
  	@FXML private ScrollPane panelFinaali;
  	@FXML private TableView<Finaali> tableView;
    
    /**
	 * Alustetaan finaalitaulukon sarakkeet
	 */
	
    @FXML private TableColumn<Finaali,String> vuosiColumn;
    @FXML private TableColumn<Finaali,String> paikkaColumn;
    @FXML private TableColumn<Finaali,String> voittajaColumn;
    @FXML private TableColumn<Finaali,String> hopeajoukkueColumn;
    @FXML private TableColumn<Finaali,String> lopputulosColumn;
    @FXML private TableColumn<Finaali,String> katsojatColumn;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        alusta();  
    }  

    /**
     * Tietoja ikkunan painallus
     */
	@FXML private void handleAboutWindow() {
		Dialogs.showMessageDialog("Tietoikkunan tekeminen on vielä kesken!");
	}

	/**
	 * Apua ikkunan painallus
	 */
	
	@FXML private void handleHelpWindow() {
	    helpWindow();
	}

	/**
	 * Tulosta napin painallus
	 */
	
	@FXML private void handlePrintWindow() {
		Dialogs.showMessageDialog("Tulostettavaa materiaalia ei vielä ole.");
	}
	
	/**
	 * Lopeta luokan metodi
	 */
	

    @FXML private void handleExit() {
        Platform.exit();
    } 
	
    /**
     * Arvo finaali napin metodi. 
     */
    
	@FXML private void handleRandomize() {
		Dialogs.showMessageDialog("Arpomista ei vielä pysty tehdä.");
	}
	
	/**
	 * Poista finaali metodin handleri
	 */
	@FXML private void handleRemoveFinal() {
		poistaFinaali();
	}
	
	/**
	 * Hakuehtoon kun kirjoittaa, mitä tapahtuu:
	 */
	
	@FXML private void handleHakuehto() {
		Dialogs.showMessageDialog("Hakuehdon käsittely on vielä kesken!");
    }

	 @FXML private void handleTulosta() {
	    TulostusController tulostusCtrl = TulostusController.tulosta(""); 
	    tulostaValitut(tulostusCtrl.getTextArea()); 
	} 
	    
	@FXML private void handleUusiFinaali() {
	    uusiFinaali();
	}
	
	@FXML private void handleUusiOsallistujamaa() {
        uusiOsallistujamaa();  
	}
	
	///=================================================================================
	
	private Tietokanta tietokanta;
    private Finaali finaaliKohdalla;
    private TextArea areaFinaali = new TextArea();
    
    
    /**
     * Tekee tarvittavat muut alustukset, nyt vaihdetaan TableViewn tilalle
     * yksi iso tekstikenttä, johon voidaan tulostaa jäsenten tiedot.
     * Alustetaan myös jäsenlistan kuuntelija 
     */
    private void alusta() {
        panelFinaali.setContent(areaFinaali);
        areaFinaali.setFont(new Font("Courier New", 12));
        panelFinaali.setFitToHeight(true);
        
        chooserFinaalit.addSelectionListener(e -> naytaFinaali());
    }
        
        
    /**
     * Näyttää listasta valitun finaalin tiedot, tilapäisesti yhteen isoon edit-kenttään
     */
    private void naytaFinaali() {
        finaaliKohdalla = chooserFinaalit.getSelectedObject();

        if (finaaliKohdalla == null) return;

        areaFinaali.setText("");
        try (PrintStream os = TextAreaOutputStream.getTextPrintStream(areaFinaali)) {
            tulosta(os,finaaliKohdalla);  
        }
    }

    private void poistaFinaali() {
		Dialogs.showMessageDialog("Ei osata vielä poistaa!");
	}

    /**
     * Hakee finaalien tiedot listaan
     * @param fnro finaalin numero, joka aktivoidaan haun jälkeen
     */
    private void hae(int fnro) {
        chooserFinaalit.clear();

        int index = 0;
        for (int i = 0; i < tietokanta.getFinaalit(); i++) {
            Finaali finaali = tietokanta.annaFinaali(i);
            if (finaali.getTunnusNro() == fnro) index = i;
            chooserFinaalit.add(finaali.getVuosi(), finaali);
        }
        chooserFinaalit.setSelectedIndex(index); // tästä tulee muutosviesti joka näyttää jäsenen
    }


    /**
     * Luo uuden finaalin jota aletaan editoimaan 
     */
    private void uusiFinaali() {
        Finaali uusifinaali = new Finaali();
        uusifinaali.rekisteroi();
        uusifinaali.testiFinaali();
        try {
            tietokanta.lisaa(uusifinaali);
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Ongelmia uuden luomisessa " + e.getMessage());
            return;
        }
        hae(uusifinaali.getTunnusNro());
    }
    
    
    /** 
     * Tekee uuden osallistujamaan editointia varten 
     */ 
    public void uusiOsallistujamaa() { 
        if ( finaaliKohdalla == null ) return;  
        Osallistujamaa uusiMaa = new Osallistujamaa();  
        uusiMaa.rekisteroi();  
        uusiMaa.testiOsallistujamaa(finaaliKohdalla.getTunnusNro());  
        tietokanta.lisaa(uusiMaa);  
        hae(finaaliKohdalla.getTunnusNro()); 
    }

    

       /**
        * @param tietokanta Kerho jota käytetään tässä käyttöliittymässä
        */
     public void setTietokanta(Tietokanta tietokanta) {
         this.tietokanta = tietokanta;
         naytaFinaali();
     }

     
     

     /**
      * "Apua"-ikkuna uudessa välilehdessä.
      */
     
    private void helpWindow() {
        Desktop desktop = Desktop.getDesktop();
          try {
              URI uri = new URI("https://www.youtube.com/watch?v=oHg5SJYRHA0");
              desktop.browse(uri);
          } catch (URISyntaxException e) {
               return;
           } catch (IOException e) {
               return;
           }
       }

        /**
         * Dataa jota lisätään tietokantaan. Tavoitteena tulevaisuudessa tehdä siis useita luokkia tähän joiden avulla voi esim.
         * finaalipaikan lisätä omasta luokastaan.
         * @return lisätty data
         
        
        private ObservableList<Finaali> getFinaalit() {
    		
        	ObservableList<Finaali> finaalit = FXCollections.observableArrayList();
            finaalit.add(new Finaali("2018","Venäjä", "Ranska", "Kroatia", "4-2", "78000"));
            finaalit.add(new Finaali("2014","Brasilia", "Saksa", "Argentiina", "1-0", "74738"));
            finaalit.add(new Finaali("2010","Etelä-Afrikka", "Espanja", "Hollanti", "1-0", "84490"));
            finaalit.add(new Finaali("2006","Saksa", "Italia", "Ranska", "2-1", "69000"));
            
            return finaalit;
    	}
        */
        
	    /**
         * Kun tätä metodia kutsutaan, syntyy uusi pop-up ikkuna, joka johtaa ADDFINAL.FXML:ään.
       */
      @FXML private void handleVaihdaIkkunaaLisaaFinaali(ActionEvent event) throws IOException
      {
          FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddFinal.fxml"));
          Parent root1 = (Parent) fxmlLoader.load();
          Stage stage = new Stage();
          stage.setScene(new Scene(root1));  
          stage.show();
      }
      
      /**
       * Tulostaa finaalin tiedot
       * @param os tietovirta johon tulostetaan
       * @param finaali tulostettava finaali
       */
      public void tulosta(PrintStream os, final Finaali finaali) {
          os.println("----------------------------------------------");
          finaali.tulosta(os);
          os.println("----------------------------------------------");
          
          List<Osallistujamaa> osallistujamaat = tietokanta.annaOsallistujamaat(finaali);   
          for (Osallistujamaa uusiMaa : osallistujamaat)
                   uusiMaa.tulosta(os);  

      }
      
      
      /**
       * Tulostaa listassa olevat finaalit tekstialueeseen
       * @param text alue johon tulostetaan
       */
      public void tulostaValitut(TextArea text) {
          try (PrintStream os = TextAreaOutputStream.getTextPrintStream(text)) {
              os.println("Tulostetaan kaikki jäsenet");
              for (int i = 0; i < tietokanta.getFinaalit(); i++) {
                  Finaali finaali = tietokanta.annaFinaali(i);
                  tulosta(os, finaali);
                  os.println("\n\n");
              }
          }        
       }	
}
