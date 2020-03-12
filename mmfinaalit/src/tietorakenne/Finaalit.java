package tietorakenne;

/**
 * Tietorakenteen luokka Finaalit, joka osaa mm. lisätä uuden finaalin tietokantaan.
 *
 * @author Ossi Lahti
 * @version 10.3.2020
 */
public class Finaalit {
    private static final int MAX_JASENIA   = 21;
    private int              lkm           = 0;
    private String           tiedostonNimi = "";
    private Finaali          alkiot[]      = new Finaali[MAX_JASENIA];


    /**
     * Oletusmuodostaja
     */
    public Finaalit() {
        // Attribuuttien oma alustus riittää
    }


    /**
     * Lisää uuden jäsenen tietorakenteeseen.  Ottaa jäsenen omistukseensa.
     * @param jasen lisätäävän jäsenen viite.  Huom tietorakenne muuttuu omistajaksi
     * @throws SailoException jos tietorakenne on jo täynnä
     */
    public void lisaa(Finaali finaali) throws SailoException {
        if (lkm >= alkiot.length) throw new SailoException("Liikaa alkioita");
        alkiot[lkm] = finaali;
        lkm++;
    }


    /**
     * Palauttaa viitteen i:teen finaaliin.
     * @param i monennenko finaalin viite halutaan
     * @return viite finaaliin, jonka indeksi on i
     * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella  
     */
    public Finaali anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || lkm <= i)
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return alkiot[i];
    }


    /**
     * Lukee finaalin tiedostosta.  Kesken.
     * @param hakemisto tiedoston hakemisto
     * @throws SailoException jos lukeminen epäonnistuu
     */
    public void lueTiedostosta(String hakemisto) throws SailoException {
        tiedostonNimi = hakemisto + "/nimet.dat";
        throw new SailoException("Ei osata vielä lukea tiedostoa " + tiedostonNimi);
    }


    /**
     * Tallentaa finaalit tiedostoon.  Kesken.
     * @throws SailoException jos talletus epäonnistuu
     */
    public void talleta() throws SailoException {
        throw new SailoException("Ei osata vielä tallettaa tiedostoa " + tiedostonNimi);
    }


    /**
     * Palauttaa tietokannan finaalien lukumäärän
     * @return finaalien lukumäärä
     */
    public int getLkm() {
        return lkm;
    }


    /**
     * Testiohjelma finaaleille
     * @param args ei käytössä
     */
    public static void main(String args[]) {
        Finaalit jasenet = new Finaalit();

        Finaali finaali1 = new Finaali(); 
        Finaali finaali2 = new Finaali();
        finaali1.rekisteroi();
        finaali1.testiFinaali();
        finaali2.rekisteroi();
        finaali2.testiFinaali();

        try {
            jasenet.lisaa(finaali1);
            jasenet.lisaa(finaali2);

            System.out.println("============= Jäsenet testi =================");

            for (int i = 0; i < jasenet.getLkm(); i++) {
                Finaali finaali = jasenet.anna(i);
                System.out.println("Finaali " + i);
                finaali.tulosta(System.out);
            }

        } catch (SailoException ex) {
            System.out.println(ex.getMessage());
        }
    }
}

