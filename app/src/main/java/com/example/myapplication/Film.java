package com.example.myapplication;

public class Film {
    private String ime;
    private String godina;
    private String opis;
    private String zanrovi;
    private byte[] slika;

    public Film(String ime, String godina, String opis, String zanrovi, byte[] slika) {
        this.ime = ime;
        this.godina = godina;
        this.opis = opis;
        this.zanrovi = zanrovi;
        this.slika = slika;
    }
    public String getIme() {
        return ime;
    }
    public void setIme(String ime) {
        this.ime = ime;
    }
    public String getGodina() {
        return godina;
    }
    public void setGodina(String godina) {
        this.godina = godina;
    }
    public String getOpis() {
        return opis;
    }
    public void setOpis(String opis) {
        this.opis = opis;
    }
    public String getZanrovi() {
        return zanrovi;
    }
    public void setZanrovi(String zanrovi) {
        this.zanrovi = zanrovi;
    }
    public byte[] getSlika() {
        return slika;
    }
    public void setSlika(byte[] slika) {
        this.slika = slika;
    }
}
