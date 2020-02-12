package fxMmfinaalit;

import javafx.beans.property.SimpleStringProperty;

public class Finaali {
    private SimpleStringProperty vuosi, paikka, voittaja, hopeajoukkue, lopputulos;
    private int katsojat;

    public Finaali(String vuosi, String paikka, String voittaja, String hopeajoukkue, String lopputulos, int katsojat) {
        
    	this.vuosi = new SimpleStringProperty(vuosi);
        this.paikka = new SimpleStringProperty(paikka);
        this.voittaja = new SimpleStringProperty(voittaja);
        this.hopeajoukkue = new SimpleStringProperty(hopeajoukkue);
        this.lopputulos = new SimpleStringProperty(lopputulos);
        this.katsojat = katsojat;
    }
    
    
    
    /**
	 * @return the vuosi
	 */
	public String getVuosi() {
		return vuosi.get();
	}



	/**
	 * @param vuosi the vuosi to set
	 */
	public void setVuosi(SimpleStringProperty vuosi) {
		this.vuosi = vuosi;
	}



	/**
	 * @return the paikka
	 */
	public String getPaikka() {
		return paikka.get();
	}



	/**
	 * @param paikka the paikka to set
	 */
	public void setPaikka(SimpleStringProperty paikka) {
		this.paikka = paikka;
	}



	/**
	 * @return the voittaja
	 */
	public String getVoittaja() {
		return voittaja.get();
	}



	/**
	 * @param voittaja the voittaja to set
	 */
	public void setVoittaja(SimpleStringProperty voittaja) {
		this.voittaja = voittaja;
	}



	/**
	 * @return the hopeajoukkue
	 */
	public String getHopeajoukkue() {
		return hopeajoukkue.get();
	}



	/**
	 * @param hopeajoukkue the hopeajoukkue to set
	 */
	public void setHopeajoukkue(SimpleStringProperty hopeajoukkue) {
		this.hopeajoukkue = hopeajoukkue;
	}



	/**
	 * @return the lopputulos
	 */
	public String getLopputulos() {
		return lopputulos.get();
	}



	/**
	 * @param lopputulos the lopputulos to set
	 */
	public void setLopputulos(SimpleStringProperty lopputulos) {
		this.lopputulos = lopputulos;
	}



	/**
	 * @return the katsojat
	 */
	public int getKatsojat() {
		return katsojat;
	}



	/**
	 * @param katsojat the katsojat to set
	 */
	public void setKatsojat(int katsojat) {
		this.katsojat = katsojat;
	}



	public String toString()
    {
        return String.format("%s %s", vuosi, paikka, voittaja, hopeajoukkue, lopputulos, katsojat);
    }
}