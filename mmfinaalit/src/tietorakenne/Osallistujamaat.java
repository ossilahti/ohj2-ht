package tietorakenne;

import java.util.*;

/**
 * Tietokannan osallistujamaat, joka osaa mm. lisätä uuden osallistujamaan.
 *
 * @author Ossi Lahti
 * @version 11.3.2020
 */
public class Osallistujamaat implements Iterable<Osallistujamaa> {

    private String                      tiedostonNimi = "";

    /** Taulukko osallistujamaista */
    private final Collection<Osallistujamaa> alkiot        = new ArrayList<Osallistujamaa>();


    /**
     * Osallistujamaiden alustaminen
     */
    public Osallistujamaat() {
        // toistaiseksi ei tarvitse tehdä mitään
    }


    /**
     * Lisää uuden osallistujamaan tietorakenteeseen. Ottaa sen omistukseensa.
     * @param lisattavaMaa lisättävä osallistujamaa.  Huom tietorakenne muuttuu omistajaksi
     */
    public void lisaa(Osallistujamaa lisattavaMaa) {
        alkiot.add(lisattavaMaa);
    }


    /**
     * Lukee finaalit tiedostosta.  
     * TODO Kesken.
     * @param hakemisto tiedoston hakemisto
     * @throws SailoException jos lukeminen epäonnistuu
     */
    public void lueTiedostosta(String hakemisto) throws SailoException {
        tiedostonNimi = hakemisto + ".har";
        throw new SailoException("Ei osata vielä lukea tiedostoa " + tiedostonNimi);
    }


    /**
     * Tallentaa finaalit tiedostoon.  
     * TODO Kesken.
     * @throws SailoException jos talletus epäonnistuu
     */
    public void talleta() throws SailoException {
        throw new SailoException("Ei osata vielä tallettaa tiedostoa " + tiedostonNimi);
    }


    /**
     * Palauttaa tietokannan osallistujamaiden lukumäärän
     * @return harrastusten lukumäärä
     */
    public int getLkm() {
        return alkiot.size();
    }


    /**
     * Iteraattori kaikkien osallistujamaiden läpikäymiseen
     * @return osallistujamaaiteraattori
     */
    @Override
    public Iterator<Osallistujamaa> iterator() {
        return alkiot.iterator();
    }


    /**
     * Haetaan kaikki finaalin osallistujamaat
     * @param finaalinTunnusNro finaalin tunnusnumero jolle osallistujamaita haetaan
     * @return tietorakenne jossa viiteet löydetteyihin osallistujamaihin
     */
    public List<Osallistujamaa> annaOsallistujamaat(int finaalinTunnusNro) {
        List<Osallistujamaa> loydetyt = new ArrayList<Osallistujamaa>();
        for (Osallistujamaa maa : alkiot)
            if (maa.getFinaaliNro() == finaalinTunnusNro) loydetyt.add(maa);
        return loydetyt;
    }


    /**
     * Testiohjelma harrastuksille
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Osallistujamaat osallistujamaat = new Osallistujamaat();
        Osallistujamaa brasilia = new Osallistujamaa();
        brasilia.testiOsallistujamaa(1);
        Osallistujamaa saksa = new Osallistujamaa();
        saksa.testiOsallistujamaa(2);
        Osallistujamaa ranska = new Osallistujamaa();
        ranska.testiOsallistujamaa(3);
        Osallistujamaa italia = new Osallistujamaa();
        italia.testiOsallistujamaa(4);

        osallistujamaat.lisaa(brasilia);
        osallistujamaat.lisaa(saksa);
        osallistujamaat.lisaa(ranska);
        osallistujamaat.lisaa(italia);

        System.out.println("============= Harrastukset testi =================");

        List<Osallistujamaa> osallistujamaat2 = osallistujamaat.annaOsallistujamaat(1);

        for (Osallistujamaa maa : osallistujamaat2) {
            System.out.print(maa.getFinaaliNro() + " ");
            maa.tulosta(System.out);
        }

    }
}

