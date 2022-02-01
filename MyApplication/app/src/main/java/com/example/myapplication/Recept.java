package com.example.myapplication;

public class Recept {
    String kategorija, naslov, slika, priprema, autor, sastojci, index;
    int ocijenilo;
    float ocjena;
    String ocjenjivaci;

    public Recept(String k, String n, String s, String p, float o, int lj, String a, String sa, String oc, String i) {
        this.ocjena = o;
        this.kategorija = k;
        this.naslov = n;
        this.slika = s;
        this.priprema = p;
        this.ocijenilo = lj;
        this.autor = a;
        this.sastojci = sa;
        this.ocjenjivaci = oc;
        this.index = i;
    }

    public Recept() {}

    public float getOcjena() {
        return this.ocjena;
    }
    public int getOcijenilo() {return this.ocijenilo;}
    public String getNaslov() {
        return this.naslov;
    }
    public String getKategorija() {
        return this.kategorija;
    }
    public String getSlika() {
        return this.slika;
    }
    public String getPriprema() {
        return this.priprema;
    }
    public String getSastojci() {
        return this.sastojci;
    }
    public String getAutor() {
        return this.autor;
    }
    public String getOcjenjivaci() { return this.ocjenjivaci;}
    public String getIndex() {return this.index;}

    public void setNaslov(String n) {
        this.naslov = n;
    }
    public void setOcjena(float o) {
        this.ocjena = o;
    }
    public void setPriprema(String p) {
        this.priprema = p;
    }
    public void setKategorija(String k) {
        this.kategorija = k;
    }
    public void setSlika(String s) {
        this.slika = s;
    }
    public void setAutor(String a) {
        this.autor = a;
    }
    public void setSastojci(String s) {
        this.sastojci = s;
    }
    public void setOcijenilo(int lj) {
        this.ocijenilo = lj;
    }
    public void setOcjenjivaci(String ocj) {
        this.ocjenjivaci = ocj;
    }
    public void setIndex(String i) {this.index = i;}

}
