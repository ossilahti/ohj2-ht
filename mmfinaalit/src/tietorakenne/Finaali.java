package tietorakenne;

import java.io.OutputStream;
import java.io.PrintStream;

///import javafx.beans.property.SimpleStringProperty;

/**
 * Finaali-luokka joka rekisteröi finaalin Finaalit-luokkaan.
 *
 * @author Ossi Lahti
 * @version 1.0, 10.3.2020
 */

public class Finaali {

	private int        tunnusNro;
    private String     vuosi			= "";       
    private String     finaalipaikka	= "";
    private String     voittaja     	= "";
    private String     hopeajoukkue 	= "";
    private String     lopputulos   	= "";
    private int        katsojia;
    private static int seuraavaNro    = 1;


	    /**
	     * @return vuosi, milloin finaali pelataan.
	     */
	    public String getVuosi() {
	        return vuosi;
	    }


	    /**
	     * Apumetodi, jolla saadaan täytettyä testiarvot finaalille.
	     */
	    public void testiFinaali() {
	        vuosi = "" + rand(1930, 2018);
	        finaalipaikka = "Brasilia";
	        voittaja = "Saksa";
	        hopeajoukkue = "Ranska";
	        lopputulos = rand(1,7) + "-" + rand(1,7);
	        katsojia = rand(50000, 200000);
	    }
	    
	    /**
	     * Tekee satunnaisen numeron ala, ja yla väliltä.
	     * @param ala alin mahdollinen numero välillä.
	     * @param yla ylin mahdollinen numero välillä.
	     * @return palauttaa kokonaisluvun.
	     */
	    public static int rand(int ala, int yla) {
	        double n = (yla-ala)*Math.random() + ala;
	        return (int)Math.round(n);
	    }


	    /**
	     * Tulostetaan henkilön tiedot
	     * @param out tietovirta johon tulostetaan
	     */
	    public void tulosta(PrintStream out) {
	        out.println(String.format("%03d", tunnusNro, 3) + " || Vuosi: " + vuosi + " || Järjestäjä: " + finaalipaikka);
	        out.println("Voittaja: " + voittaja + " || Hopeajoukkue: " + hopeajoukkue + " || Lopputulos: " + lopputulos);
	        out.println("Katsojia yhteensä: " + String.format("%6d", katsojia)); 
	    }


	    /**
	     * Tulostetaan finaalin tiedot
	     * @param os tietovirta johon tulostetaan
	     */
	    public void tulosta(OutputStream os) {
	        tulosta(new PrintStream(os));
	    }


	    /**
	     * Antaa finaalille seuraavan rekisterinumeron.
	     * @return finaalin uusi tunnusNro
	     */
	    public int rekisteroi() {
	        tunnusNro = seuraavaNro;
	        seuraavaNro++;
	        return tunnusNro;
	    }


	    /**
	     * Palauttaa finaalin tunnusnumeron.
	     * @return finaalin tunnusnumero
	     */
	    public int getTunnusNro() {
	        return tunnusNro;
	    }


	    /**
	     * Testiohjelma jäsenelle.
	     * @param args ei käytössä
	     */
	    public static void main(String args[]) {
	        Finaali finaali1 = new Finaali();
	        Finaali finaali2 = new Finaali();
	        finaali1.rekisteroi();
	        finaali2.rekisteroi();
	        
	        finaali1.testiFinaali();
	        finaali1.tulosta(System.out);

	        finaali2.testiFinaali();
	        finaali2.tulosta(System.out);

	        finaali2.testiFinaali();
	        finaali2.tulosta(System.out);
	    }


	
	
	
	
	/**
	private SimpleStringProperty vuosi, paikka, voittaja, hopeajoukkue, lopputulos, katsojat;

    public Finaali(String vuosi, String paikka, String voittaja, String hopeajoukkue, String lopputulos, String katsojat) {
        
    	this.vuosi = new SimpleStringProperty(vuosi);
        this.paikka = new SimpleStringProperty(paikka);
        this.voittaja = new SimpleStringProperty(voittaja);
        this.hopeajoukkue = new SimpleStringProperty(hopeajoukkue);
        this.lopputulos = new SimpleStringProperty(lopputulos);
        this.katsojat = new SimpleStringProperty(katsojat);
    }
    */
    
    
    /**
	 * @return the vuosi
	
	public String getVuosi() {
		return vuosi.get();
	}



	/**
	 * @param vuosi the vuosi to set
	 
	public void setVuosi(SimpleStringProperty vuosi) {
		this.vuosi = vuosi;
	}



	/**
	 * @return the paikka
	 
	public String getPaikka() {
		return paikka.get();
	}



	/**
	 * @param paikka the paikka to set
	 
	public void setPaikka(SimpleStringProperty paikka) {
		this.paikka = paikka;
	}



	/**
	 * @return the voittaja
	 
	public String getVoittaja() {
		return voittaja.get();
	}



	/**
	 * @param voittaja the voittaja to set
	 
	public void setVoittaja(SimpleStringProperty voittaja) {
		this.voittaja = voittaja;
	}



	/**
	 * @return the hopeajoukkue
	 
	public String getHopeajoukkue() {
		return hopeajoukkue.get();
	}



	/**
	 * @param hopeajoukkue the hopeajoukkue to set
	
	public void setHopeajoukkue(SimpleStringProperty hopeajoukkue) {
		this.hopeajoukkue = hopeajoukkue;
	}



	/**
	 * @return the lopputulos
	
	public String getLopputulos() {
		return lopputulos.get();
	}



	/**
	 * @param lopputulos the lopputulos to set
	 
	public void setLopputulos(SimpleStringProperty lopputulos) {
		this.lopputulos = lopputulos;
	}



	/**
	 * @return the katsojat
	 
	public String getKatsojat() {
		return katsojat.get();
	}



	/**
	 * @param katsojat the katsojat to set
	
	public void setKatsojat(SimpleStringProperty katsojat) {
		this.katsojat = katsojat;
	}



	public String toString()
    {
        return String.format("%s %s", vuosi, paikka, voittaja, hopeajoukkue, lopputulos, katsojat);
    }
    */

}