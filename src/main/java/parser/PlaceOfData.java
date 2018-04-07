package parser;

import java.io.*;

public class PlaceOfData {
    private int nazwisko;
    private int imie;
    private int przedmioty_od;
    private int przedmioty_do;
    private int podsumowanie_od;
    private int podsumowanie_do;
    private int przedmioty;

    public PlaceOfData() {
    }

    public PlaceOfData(int nazwisko, int imie, int przedmioty_od, int przedmioty_do, int podsumowanie_od, int podsumowanie_do, int przedmioty) {
        this.nazwisko = nazwisko;
        this.imie = imie;
        this.przedmioty_od = przedmioty_od;
        this.przedmioty_do = przedmioty_do;
        this.podsumowanie_od = podsumowanie_od;
        this.podsumowanie_do = podsumowanie_do;
        this.przedmioty = przedmioty;
    }


    public static void saveAsFile(PlaceOfData data) throws IOException {
        File file = new File("src\\main\\resources\\data.txt");
        PrintWriter printWriter = new PrintWriter(file);
        printWriter.println(data.nazwisko);
        printWriter.println(data.imie);
        printWriter.println(data.przedmioty_od);
        printWriter.println(data.przedmioty_do);
        printWriter.println(data.podsumowanie_od);
        printWriter.println(data.podsumowanie_do);
        printWriter.println(data.przedmioty);
        printWriter.close();
    }


    public static PlaceOfData readFromFile() throws IOException{
        BufferedReader fileReader = new BufferedReader(new FileReader("src\\main\\resources\\data.txt"));
        PlaceOfData place = new PlaceOfData(Integer.parseInt(fileReader.readLine()),Integer.parseInt(fileReader.readLine()),Integer.parseInt(fileReader.readLine()),Integer.parseInt(fileReader.readLine()),Integer.parseInt(fileReader.readLine()),Integer.parseInt(fileReader.readLine()),Integer.parseInt(fileReader.readLine()));
        fileReader.close();
        return place;
    }
    public int getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(int nazwisko) {
        this.nazwisko = nazwisko;
    }

    public int getImie() {
        return imie;
    }

    public void setImie(int imie) {
        this.imie = imie;
    }

    public int getPrzedmioty_od() {
        return przedmioty_od;
    }

    public void setPrzedmioty_od(int przedmioty_od) {
        this.przedmioty_od = przedmioty_od;
    }

    public int getPrzedmioty_do() {
        return przedmioty_do;
    }

    public void setPrzedmioty_do(int przedmioty_do) {
        this.przedmioty_do = przedmioty_do;
    }

    public int getPodsumowanie_od() {
        return podsumowanie_od;
    }

    public void setPodsumowanie_od(int podsumowanie_od) {
        this.podsumowanie_od = podsumowanie_od;
    }

    public int getPodsumowanie_do() {
        return podsumowanie_do;
    }

    public void setPodsumowanie_do(int podsumowanie_do) {
        this.podsumowanie_do = podsumowanie_do;
    }

    public int getPrzedmioty() {
        return przedmioty;
    }

    public void setPrzedmioty(int przedmioty) {
        this.przedmioty = przedmioty;
    }
}
