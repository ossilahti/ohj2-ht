package tietorakenne;

import java.io.*;

/**
 * Osallistujamaa, joka osaa huolehtia tunnusnrostaan.
 * 
 * @author Ossi Lahti
 * @version 11.3.2020
 */
public class Osallistujamaa {
    private int tunnusNro;
    private int finaaliNro;
    private String voittajajoukkue;
    private String hopeajoukkue;

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
     * TODO: pitää saada jotenkin erilaiset osallistujamaat.
     * @param nro viite finaaliin, jonka osallistujamaista on kyse
     */
    public void testiOsallistujamaa(int finaalinViiteNro) {
        finaaliNro = finaalinViiteNro;
        voittajajoukkue = "Argentiina";
        hopeajoukkue = "Saksa";
    }
    
    
    /**
     * Tulostetaan osallistujamaan tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println("Finaali numero: " + finaaliNro + " || " + "Voittajamaa: " + voittajajoukkue + " || Hopeajoukkue: " + hopeajoukkue);
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
     * Testiohjelma Harrastukselle.
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Osallistujamaa maa = new Osallistujamaa();
        maa.testiOsallistujamaa(2);
        maa.tulosta(System.out);
    }
}

