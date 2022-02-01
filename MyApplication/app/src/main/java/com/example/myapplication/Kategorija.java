package com.example.myapplication;

import java.util.List;

public class Kategorija {
    public String ime;
    public List<Recept> recepti;

    public Kategorija() {

    }

    public Kategorija(String i, List<Recept> r) {
        this.ime = i;
        this.recepti = r;
    }

    public String getIme() {return this.ime;}
    public List<Recept> getRecepti() {return this.recepti;}

    public void setIme(String i) {
        this.ime = i;
    }
    public void setRecepti(List<Recept> r) {
        this.recepti = r;
    }
}
