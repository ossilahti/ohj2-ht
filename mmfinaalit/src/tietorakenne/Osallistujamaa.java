package tietorakenne;

import java.io.*;
import fi.jyu.mit.ohj2.Mjonot;
import tietorakenne.Tietue;
/**
 * Osallistujamaa, joka osaa huolehtia tunnusnrostaan.
 * 
 * @author Ossi Lahti
 * @version 11.3.2020
 */
public class Osallistujamaa implements Cloneable, Tietue {
    private int tunnusNro;
    private int finaaliNro;
    private String osallistujamaa ="";

    private static int seuraavaNro = 1;


    /**
     * Alustetaan osallistujamaa. Tyhjä muodostaja ei tarvitse attribuutteja.
     */
    public Osallistujamaa() {
        // Vielä ei tarvita mitään
    }


    /**
     * Alustetaan tietyn finaalin osallistujamaa.
     * @param finaaliNro finaalin viitenumero 
     */
    public Osallistujamaa(int finaaliNro) {
        this.finaaliNro = finaaliNro;
    }


    /**
     * Apumetodi, jolla saadaan täytettyä testiarvot Osallistujamaalle.
     * @param nro viite finaaliin, jonka osallistujamaista on kyse
     */
    public void testiOsallistujamaa(int finaalinViiteNro) {
        finaaliNro = finaalinViiteNro;
        osallistujamaa = "Argentiina";
    }
    
    
    /**
     * Tulostetaan osallistujamaan tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println("Finaali numero: " + finaaliNro + " || " + "Voittajamaa: " + osallistujamaa);
    }


    /**
     * Tulostetaan osallistujamaiden tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }


    /**
     * Antaa osallistujamaalle seuraavan rekisterinumeron.
     * @return osallistujamaan uusi tunnus_nro
     */
    public int rekisteroi() {
        tunnusNro = seuraavaNro;
        seuraavaNro++;
        return tunnusNro;
    }


    /**
     * Palautetaan osallistujamaan oma id
     * @return osallistujamaan id
     */
    public int getTunnusNro() {
        return tunnusNro;
    }


    /**
     * Palautetaan mille finaalille osallistujamaa kuuluu
     * @return finaalin id
     */
    public int getFinaaliNro() {
        return finaaliNro;
    }

    /**
     * Asettaa tunnusnumeron ja samalla varmistaa että
     * seuraava numero on aina suurempi kuin tähän mennessä suurin.
     * @param nr asetettava tunnusnumero
     */
    private void setTunnusNro(int nr) {
        tunnusNro = nr;
        if ( tunnusNro >= seuraavaNro ) seuraavaNro = tunnusNro + 1;
    }


    /**
     * Palauttaa osallistujamaan tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return maat tolppaeroteltuna merkkijonona 
     *
     */
    @Override
    public String toString() {
    	StringBuffer sb = new StringBuffer("");
        String erotin = "";
        for (int k = 0; k < getKenttia(); k++) {
            sb.append(erotin);
            sb.append(anna(k));
            erotin = "|";
        }
        return sb.toString();
     }



    /**
     * Selvitää osallistujamaan tiedot | erotellusta merkkijonosta.
     * Pitää huolen että seuraavaNro on suurempi kuin tuleva tunnusnro.
     * @param rivi josta harrastuksen tiedot otetaan
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        for (int k = 0; k < getKenttia(); k++)
        	 aseta(k, Mjonot.erota(sb, '|'));
    }


    @Override
    public boolean equals(Object obj) {
        if ( obj == null ) return false;
        return this.toString().equals(obj.toString());
    }
    

    @Override
    public int hashCode() {
        return tunnusNro;
    }
    
    /**
     * @return osallistujamaan kenttien lukumäärä
     */
    @Override
    public int getKenttia() {
        return 3;
    }


    /**
     * @return ensimmäinen käyttäjän syötettävän kentän indeksi
     */
    @Override
    public int ekaKentta() {
        return 2;
    }
    

    /**
     * @param k minkä kentän kysymys halutaan
     * @return valitun kentän kysymysteksti
     */
    @Override
    public String getKysymys(int k) {
        switch (k) {
            case 0:
                return "Id";
            case 1:
            	return "Finaalin Id";
            case 2:
            	return "Osallistujamaa";
            default:
                return "???";
        }
    }


    /**
     * @param k Minkä kentän sisältö halutaan
     * @return valitun kentän sisältö
     */
    @Override
    public String anna(int k) {
        switch (k) {
            case 0:
                return "" + tunnusNro;
            case 1:
            	return "" + finaaliNro;
            case 2:
                return "" + osallistujamaa;
            default:
                return "???";
        }
    }


    /**
     * Asetetaan valitun kentän sisältö.  Mikäli asettaminen onnistuu,
     * palautetaan null, muutoin virheteksti.
     * @param k minkä kentän sisältö asetetaan
     * @param s asetettava sisältö merkkijonona
     * @return null jos ok, muuten virheteksti
     */
    @Override
    public String aseta(int k, String s) {
        String st = s.trim();
        StringBuffer sb = new StringBuffer(st);
        switch (k) {
            case 0:
                setTunnusNro(Mjonot.erota(sb, '$', getTunnusNro()));
                return null;
            case 1:
            	finaaliNro = Mjonot.erota(sb, '$', finaaliNro);
            	return null;
            case 2:
                osallistujamaa = st;
                return null;
            default:
        		return "Väärä kentän indeksi";     
        }
    }


    /**
     * Tehdään identtinen klooni finaalista
     * @return Object kloonattu finaali
     */
    @Override
    public Osallistujamaa clone() throws CloneNotSupportedException { 
        return (Osallistujamaa)super.clone();
    }
    

    
    /**
     * Testiohjelma Harrastukselle.
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Osallistujamaa maa = new Osallistujamaa();
        maa.testiOsallistujamaa(2);
        maa.tulosta(System.out);
    }
}

