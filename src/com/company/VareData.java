package com.company;

public class VareData implements java.io.Serializable {

    private int antal;
    private String vareNavn;
    private double stykPris;



    public VareData(int antal, String vareNavn , double stykPris) {
        this.antal = antal;
        this.vareNavn = vareNavn;
        this.stykPris = stykPris;
    }

    public int getAntal() { return antal;}
    public void setAntal(int antal) { this.antal = antal; }

    public String getVareNavn() { return vareNavn; }
    public void setVareNavn(String vareNavn) { this.vareNavn = vareNavn; }

    public double getStykPris() { return stykPris; }
    public void setStykPris(double stykPris) { this.stykPris = stykPris; }




}
