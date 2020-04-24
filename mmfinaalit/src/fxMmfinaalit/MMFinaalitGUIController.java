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
import fi.jyu.mit.fxgui.StringGrid;
import fi.jyu.mit.fxgui.TextAreaOutputStream;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import tietorakenne.Finaali;
import tietorakenne.Osallistujamaa;
import tietorakenne.SailoException;
import tietorakenne.Tietokanta;
import static fxMmfinaalit.TietueDialogController.getFieldId; 

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
  	@FXML private StringGrid<Osallistujamaa> tableOsallistujamaat;
  	@FXML private GridPane gridFinaali;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        alusta();  
    }  

	@FXML private void handleAboutWindow() {
		ModalController.showModal(MMFinaalitGUIController.class.getResource("AboutWindow.fxml"), "Infoikkuna", null, "");
	}

	@FXML private void handleHelpWindow() {
	    helpWindow();
	}
	
	@FXML private void handleTallenna() {
		tallenna();
	}

	@FXML private void handleMuokkaaFinaalia() {
		muokkaa(kentta);
	}

    @FXML private void handleExit() {
    	tallenna();
        Platform.exit();
    } 
	
	@FXML private void handleRemoveFinal() {
		poistaFinaali();
	}
	
	@FXML private void handleRemoveCountry() {
		poistaOsallistujamaa();
	}
	
	
	@FXML private void handleHakuehto() {
		hae(0); 
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
    private TextField edits[];     
    private int kentta = 0; 	
    private static Finaali apufinaali = new Finaali(); 
    private static Osallistujamaa apuosallistujamaa = new Osallistujamaa(); 

    
    /**
     * Tekee tarvittavat muut alustukset, nyt vaihdetaan TableViewn tilalle
     * yksi iso tekstikenttä, johon voidaan tulostaa jäsenten tiedot.
     * Alustetaan myös jäsenlistan kuuntelija 
     */
    private void alusta() {
        chooserFinaalit.clear();
    	chooserFinaalit.addSelectionListener(e -> naytaFinaali());
         
         edits = TietueDialogController.luoKentat(gridFinaali, apufinaali);  
         for (TextField edit: edits)  
             if ( edit != null ) {  
                 edit.setEditable(false);  
                 edit.setOnMouseClicked(e -> { if ( e.getClickCount() > 1 ) muokkaa(getFieldId(e.getSource(),0)); });  
                 edit.focusedProperty().addListener((a,o,n) -> kentta = getFieldId(edit,kentta));
                 edit.setOnKeyPressed( e -> {if ( e.getCode() == KeyCode.F2 ) muokkaa(kentta);}); 
             }    
         // alustetaan harrastustaulukon otsikot 
         int eka = apuosallistujamaa.ekaKentta(); 
         int lkm = apuosallistujamaa.getKenttia(); 
         String[] headings = new String[lkm-eka]; 
         for (int i=0, k=eka; k<lkm; i++, k++) headings[i] = apuosallistujamaa.getKysymys(k); 
         tableOsallistujamaat.initTable(headings); 
         tableOsallistujamaat.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); 
         tableOsallistujamaat.setEditable(false); 
         tableOsallistujamaat.setPlaceholder(new Label("Ei vielä osallistujamaita.")); 
          
         // Tämä on vielä huono, ei automaattisesti muutu jos kenttiä muutetaan. 
         tableOsallistujamaat.setColumnSortOrderNumber(1); 
         tableOsallistujamaat.setColumnSortOrderNumber(2); 
         tableOsallistujamaat.setColumnWidth(1, 60); 
         tableOsallistujamaat.setColumnWidth(2, 60); 
         
         tableOsallistujamaat.setOnMouseClicked( e -> { if ( e.getClickCount() > 1 ) muokkaaOsallistujamaata(); } );
         tableOsallistujamaat.setOnKeyPressed( e -> {if ( e.getCode() == KeyCode.F2 ) muokkaaOsallistujamaata();}); 
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
        if (finaaliKohdalla == null) return;
        
        TietueDialogController.naytaTietue(edits, finaaliKohdalla); 
        naytaOsallistujamaat(finaaliKohdalla);    
    }
    
    /**
     * Näytetään osallistujamaan tiedot StringGridissä
     * @param finaali
     */
    private void naytaOsallistujamaat(Finaali finaali) {
        tableOsallistujamaat.clear();
        if ( finaali == null ) return;
        
        try {
            List<Osallistujamaa> maat = tietokanta.annaOsallistujamaat(finaali);
            if ( maat.size() == 0 ) return;
            for (Osallistujamaa maa: maat)
                naytaOsallistujamaa(maa);
        } catch (SailoException e) {
            naytaVirhe(e.getMessage());
        } 
    }

    
    private void naytaOsallistujamaa(Osallistujamaa maa) {
    	 int kenttia = maa.getKenttia(); 
         String[] rivi = new String[kenttia-maa.ekaKentta()]; 
         for (int i=0, k=maa.ekaKentta(); k < kenttia; i++, k++) 
             rivi[i] = maa.anna(k); 
         tableOsallistujamaat.add(maa,rivi);

    }
    
    /**
     * Finaalin muokkaus
     */
    private void muokkaa(int i) {
    	if ( finaaliKohdalla == null ) return; 
        try { 
            Finaali finaali; 
            finaali = TietueDialogController.kysyTietue(null, finaaliKohdalla.clone(), i);     
            if ( finaali == null ) return; 
            tietokanta.korvaaTaiLisaa(finaali); 
            hae(finaali.getTunnusNro()); 
        } catch (CloneNotSupportedException e) { 
            // 
        } catch (SailoException e) { 
            Dialogs.showMessageDialog(e.getMessage()); 
        }
    }
    
    /**
     * Poistetaan maataulukosta valitulla kohdalla oleva maa. 
     */
    private void poistaOsallistujamaa() {
        int rivi = tableOsallistujamaat.getRowNr();
        if ( rivi < 0 ) return;
        Osallistujamaa maa = tableOsallistujamaat.getObject();
        if ( maa == null ) return;
        tietokanta.poistaOsallistujamaa(maa);
        naytaOsallistujamaat(finaaliKohdalla);
        int maita = tableOsallistujamaat.getItems().size(); 
        if ( rivi >= maita ) rivi = maita -1;
        tableOsallistujamaat.getFocusModel().focus(rivi);
        tableOsallistujamaat.getSelectionModel().select(rivi);
    }

    
    /**
     * Poistetaan listalta valittu finaali
     */
    
    private void poistaFinaali() {
        Finaali finaali = finaaliKohdalla;
        if ( finaali == null ) return;
        if ( !Dialogs.showQuestionDialog("Poisto", "Poistetaanko finaali vuodelta: " + finaali.getVuosi(), "Kyllä", "Ei") )
            return;
        tietokanta.poista(finaali);
        int index = chooserFinaalit.getSelectedIndex();
        hae(0);
        chooserFinaalit.setSelectedIndex(index);
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
    	 
        int tunnus = fnro; // tunnus finaalin numero, joka aktivoidaan haun jälkeen 
        if ( tunnus <= 0 ) { 
            Finaali kohdalla = finaaliKohdalla; 
            if ( kohdalla != null ) tunnus = kohdalla.getTunnusNro(); 
        }
    	String ehto = searchField.getText(); 
    	if (ehto.indexOf('*') < 0) ehto = "*" + ehto + "*"; 

        chooserFinaalit.clear();
        int k = apufinaali.ekaKentta();
        int index = 0;
        Collection<Finaali> finaalit;
        try {
            finaalit = tietokanta.etsi(ehto, k);
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
        try {
        	Finaali uusifinaali = new Finaali();
        	uusifinaali = TietueDialogController.kysyTietue(null, uusifinaali, 1); 
            if ( uusifinaali == null ) return;
            uusifinaali.rekisteroi();
            tietokanta.lisaa(uusifinaali);
            hae(uusifinaali.getTunnusNro());
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Ongelmia uuden luomisessa " + e.getMessage());
          return;
        }
    }
    
    
    /** 
     * Tekee uuden osallistujamaan editointia varten 
     */ 
    public void uusiOsallistujamaa() throws SailoException { 
    	 Osallistujamaa uusi = new Osallistujamaa(finaaliKohdalla.getTunnusNro());
		 uusi = TietueDialogController.kysyTietue(null, uusi, 0);
		 if ( uusi == null ) return;
		 uusi.rekisteroi();
		 tietokanta.lisaa(uusi);
		 naytaOsallistujamaat(finaaliKohdalla); 
		 tableOsallistujamaat.selectRow(1000);  // järjestetään viimeinen rivi valituksi
    }
    
    
    private void muokkaaOsallistujamaata() {
        int r = tableOsallistujamaat.getRowNr();
        if ( r < 0 ) return; // klikattu ehkä otsikkoriviä
        Osallistujamaa maa = tableOsallistujamaat.getObject();
        if ( maa == null ) return;
        int k = tableOsallistujamaat.getColumnNr()+maa.ekaKentta();
        try {
            maa = TietueDialogController.kysyTietue(null, maa.clone(), k);
            if ( maa == null ) return;
            tietokanta.korvaaTaiLisaa(maa); 
            naytaOsallistujamaat(finaaliKohdalla); 
            tableOsallistujamaat.selectRow(r);  // järjestetään sama rivi takaisin valituksi
        } catch (CloneNotSupportedException  e) { /* clone on tehty */  
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Ongelmia lisäämisessä: " + e.getMessage());
        }
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
      public void tulostaValitut(TextArea text) throws SailoException{
          try (PrintStream os = TextAreaOutputStream.getTextPrintStream(text)) {
              os.println("Tulostetaan finaali vuodelta: " + finaaliKohdalla.getVuosi());
              for (Finaali fin: chooserFinaalit.getObjects())  {
                  tulosta(os, fin);
                  os.println("\n\n");
              }
          }        
       }	
}
