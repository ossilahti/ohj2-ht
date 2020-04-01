package tietorakenne;

import java.io.*;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Osallistujamaa, joka osaa huolehtia tunnusnrostaan.
 * 
 * @author Ossi Lahti
 * @version 11.3.2020
 */
public class Osallistujamaa {
    private int tunnusNro;
    private int finaaliNro;
    private String osallistujamaa;

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
        return "" + getTunnusNro() + "|" + finaaliNro + "|" + osallistujamaa+ "|";
    }


    /**
     * Selvitää harrastuksen tiedot | erotellusta merkkijonosta.
     * Pitää huolen että seuraavaNro on suurempi kuin tuleva tunnusnro.
     * @param rivi josta harrastuksen tiedot otetaan
     * @example
     * <pre name="test">
     *   Harrastus harrastus = new Harrastus();
     *   harrastus.parse("   2   |  10  |   Kalastus  | 1949 | 22 t ");
     *   harrastus.getJasenNro() === 10;
     *   harrastus.toString()    === "2|10|Kalastus|1949|22";
     *   
     *   harrastus.rekisteroi();
     *   int n = harrastus.getTunnusNro();
     *   harrastus.parse(""+(n+20));
     *   harrastus.rekisteroi();
     *   harrastus.getTunnusNro() === n+20+1;
     *   harrastus.toString()     === "" + (n+20+1) + "|10|Kalastus|1949|22";
     * </pre>
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        setTunnusNro(Mjonot.erota(sb, '|', getTunnusNro()));
        finaaliNro = Mjonot.erota(sb, '|', finaaliNro);
        osallistujamaa = Mjonot.erota(sb, '|', osallistujamaa);
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
     * Testiohjelma Harrastukselle.
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Osallistujamaa maa = new Osallistujamaa();
        maa.testiOsallistujamaa(2);
        maa.tulosta(System.out);
    }
}

