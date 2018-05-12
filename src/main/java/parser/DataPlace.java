package parser;

import java.util.ArrayList;
import java.util.List;

public class DataPlace {
    private int nazwisko;
    private int imie;
    private int przedmioty_od;
    private int przedmioty_do;
    private int podsumowanie_od;
    private int podsumowanie_do;
    private int przedmioty;

    public DataPlace() {
    }

    public DataPlace(int nazwisko, int imie, int przedmioty_od, int przedmioty_do, int podsumowanie_od, int podsumowanie_do, int przedmioty) {
        this.nazwisko = nazwisko;
        this.imie = imie;
        this.przedmioty_od = przedmioty_od;
        this.przedmioty_do = przedmioty_do;
        this.podsumowanie_od = podsumowanie_od;
        this.podsumowanie_do = podsumowanie_do;
        this.przedmioty = przedmioty;
    }


//    public static void saveAsFile(DataPlace aktualny) throws IOException {
//        File file = new File("src\\main\\resources\\aktualny.txt");
//        PrintWriter printWriter = new PrintWriter(file);
//        printWriter.println(aktualny.nazwisko);
//        printWriter.println(aktualny.imie);
//        printWriter.println(aktualny.przedmioty_od);
//        printWriter.println(aktualny.przedmioty_do);
//        printWriter.println(aktualny.podsumowanie_od);
//        printWriter.println(aktualny.podsumowanie_do);
//        printWriter.println(aktualny.przedmioty);
//        printWriter.close();
//    }


//    public static DataPlace readFromFile() throws IOException {
//        BufferedReader fileReader = new BufferedReader(new FileReader("src\\main\\resources\\aktualny.txt"));
//        DataPlace place = new DataPlace(Integer.parseInt(fileReader.readLine()), Integer.parseInt(fileReader.readLine()), Integer.parseInt(fileReader.readLine()), Integer.parseInt(fileReader.readLine()), Integer.parseInt(fileReader.readLine()), Integer.parseInt(fileReader.readLine()), Integer.parseInt(fileReader.readLine()));
//        fileReader.close();
//        return place;
//    }

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


    public DataPlace(List<String> data) {
        this.nazwisko = Integer.parseInt(data.get(0));
        this.imie = Integer.parseInt(data.get(1));
        this.przedmioty_od = Integer.parseInt(data.get(2));
        this.przedmioty_do = Integer.parseInt(data.get(3));
        this.podsumowanie_od = Integer.parseInt(data.get(4));
        this.podsumowanie_do = Integer.parseInt(data.get(5));
        this.przedmioty = Integer.parseInt(data.get(6));
    }

    public List<String> toList() {
        List result = new ArrayList<String>();
        result.add(this.nazwisko+"");
        result.add(this.imie+"");
        result.add(this.przedmioty_od+"");
        result.add(this.przedmioty_do+"");
        result.add(this.podsumowanie_od+"");
        result.add(this.podsumowanie_do+"");
        result.add(this.przedmioty+"");
        return result;
    }
}