package fxMmfinaalit;

import java.awt.Desktop;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
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
	 * Tallentaa napista tiedot.
	 */
	
	@FXML private void handleTallenna() {
		tallenna();
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
    	tallenna();
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
		 if ( finaaliKohdalla != null )
			 hae(finaaliKohdalla.getTunnusNro()); 
		
    }

	 @FXML private void handleTulosta() throws SailoException {
	    TulostusController tulostusCtrl = TulostusController.tulosta(""); 
	    tulostaValitut(tulostusCtrl.getTextArea()); 
	} 
	    
	@FXML private void handleUusiFinaali() {
	    uusiFinaali();
	}
	
	@FXML private void handleUusiOsallistujamaa() throws SailoException {
        uusiOsallistujamaa();  
	}
	
	///=================================================================================
	
	private String finaalinNimi = "mmfinaalit";
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
	 * Tarkistetaan onko tallennus tehty     
	 * @return true jos saa sulkea, false jos ei
	 */
    public boolean voikoSulkea() {
        tallenna();
        return true;
    }
    
    /**
     * Kysytään tiedoston nimi ja luetaan se
     * @return true jos onnistui, false jos ei
     */
    public boolean avaa() {
        String uusinimi = StartingWindowController.kysyNimi(null, finaalinNimi);
        if (uusinimi == null) return false;
        lueTiedosto(uusinimi);
        return true;
    }


    /**
     * Näyttää listasta valitun finaalin tiedot, tilapäisesti yhteen isoon edit-kenttään
     */
    private void naytaFinaali() {
        finaaliKohdalla = chooserFinaalit.getSelectedObject();

        if (finaaliKohdalla == null) {
        	areaFinaali.clear();
        	return;
        }

        areaFinaali.setText("");
        try (PrintStream os = TextAreaOutputStream.getTextPrintStream(areaFinaali)) {
            tulosta(os,finaaliKohdalla);  
        } catch (SailoException e) {
			e.printStackTrace();
		}
    }

    private void poistaFinaali() {
		Dialogs.showMessageDialog("Ei osata vielä poistaa!");
	}

    /**
     * Alustaa finaalin lukemalla sen valitun nimisestä tiedostosta
     * @param nimi tiedosto josta finaalin tiedot luetaan
     * @return null jos onnistuu, muuten virhe tekstinä
     */
    
    private void setTitle(String title) {
        ModalController.getStage(searchField).setTitle(title);
    }

    public String lueTiedosto(String nimi) {
        finaalinNimi = nimi;
        setTitle("Tietokanta - " + finaalinNimi);
        try {
            tietokanta.lueTiedostosta(nimi);
            hae(0);
            return null;
        } catch (SailoException e) {
            hae(0);
            String virhe = e.getMessage(); 
            if ( virhe != null ) Dialogs.showMessageDialog(virhe);
            return virhe;
        }
    }
    
    /**
     * Tietojen tallennus
     * @return null jos onnistuu, muuten virhe tekstinä
     */
    private String tallenna() {
        try {
            tietokanta.talleta();
            return null;
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Tallennuksessa ongelmia! " + ex.getMessage());
            return ex.getMessage();
        }
    }
    
    private void naytaVirhe(String virhe) {
        if ( virhe == null || virhe.isEmpty() ) {
        	searchField.setText("");
        	searchField.getStyleClass().removeAll("virhe");
            return;
        }
        searchField.setText(virhe);
        searchField.getStyleClass().add("virhe");
    }


    /**
     * Hakee finaalien tiedot listaan
     * @param fnro finaalin numero, joka aktivoidaan haun jälkeen
     */
    protected void hae(int fnro) {
        String ehto = searchField.getText(); 
        if (ehto.length() > 0)
        	naytaVirhe(String.format("Ei osata hakea (kenttä: %d, ehto: %s)",ehto));
        else
        	naytaVirhe(null);
        chooserFinaalit.clear();

        int index = 0;
        Collection<Finaali> finaalit;
        try {
            finaalit = tietokanta.etsi(ehto);
            int i = 0;
            for (Finaali finaali : finaalit ) {
                if (finaali.getTunnusNro() == fnro) index = i;
                chooserFinaalit.add(finaali.getVuosi(), finaali);
                i++;
            }
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Finaalin hakemisessa ongelmia! " + ex.getMessage());
        }
        chooserFinaalit.setSelectedIndex(index); // tästä tulee muutosviesti joka näyttää finaalin
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
    public void uusiOsallistujamaa() throws SailoException { 
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
       * @throws SailoException 
       */
      public void tulosta(PrintStream os, final Finaali finaali) throws SailoException {
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
       * @throws SailoException 
       */
      public void tulostaValitut(TextArea text) throws SailoException {
          try (PrintStream os = TextAreaOutputStream.getTextPrintStream(text)) {
              os.println("Tulostetaan kaikki finaalit: ");
              Collection<Finaali> finaalit = tietokanta.etsi(""); 
              for(Finaali fin: finaalit) {
                  tulosta(os, fin);
                  os.println("\n\n");
              }
          }        
       }	
}
