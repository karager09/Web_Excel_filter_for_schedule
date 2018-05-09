package parser;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Iterator;


public class ParserTest {


//    @Test
    public void findLecturerLastname() {
        try {
            //Parser.setData(PlaceOfData.readFromFile());
            //Parser.findData();
            Assert.assertTrue(Parser.findLecturer("Szymkat"));
            Assert.assertFalse(Parser.findLecturer("Buczek"));
            Assert.assertFalse((Parser.findLecturer("Szmuc")));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(":O");
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
    }

//    @Test
    public void findLecturer() {
        try {
            //Parser.setData(PlaceOfData.readFromFile());
            Assert.assertTrue(Parser.findLecturer("Szymkat", "Maciej"));
            Assert.assertFalse(Parser.findLecturer("Szymkat", "Adam"));
            Assert.assertTrue((Parser.findLecturer("Szmuc", "Tomasz")));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(":O");
        } catch (InvalidFormatException e) {
            e.printStackTrace();

        }
    }


//    @Test
    public void findLecturerColumn() {
        try {
            //Parser.setData(PlaceOfData.readFromFile());
            Assert.assertEquals(15,Parser.findLecturerColumn("Bielecki"));
            Assert.assertEquals(0,Parser.findLecturerColumn("Buczek"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(":O");
        } catch (InvalidFormatException e) {
            e.printStackTrace();

        }
    }

//    @Test
    public void findLecturerColumn1() {
        try {
            //Parser.setData(PlaceOfData.readFromFile());
            Assert.assertEquals(14,Parser.findLecturerColumn("Szmuc","Tomasz"));
            Assert.assertEquals(15,Parser.findLecturerColumn("Bielecki","Andrzej"));
            Assert.assertEquals(0,Parser.findLecturerColumn("Buczek","Adam"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(":O");
        } catch (InvalidFormatException e) {
            e.printStackTrace();

        }
    }


    public JSONObject getAllLecturerData(int column){
        String [] my_data = {"PRZEDMIOTY", "PENSUM", "ZNIŻKA PENSUM", "GODZINY RAZEM (OBCIĄŻENIE DYDAKTYCZNE)", "GODZINY PONADWYMIAROWE %", "ZAJĘCIA DYDAKTYCZNE RAZEM", "WYKŁADY STACJONARNE", "ZAJĘCIA STACJONARNE", "WYKŁADY NIESTACJONARNE", "ZAJĘCIA NIESTACJONARNE", "SUMA GODZIN DODATKOWYCH", "GODZINY ZA EGZAMIN", "GODZINY ZA ZAJĘCIA W J. ANGIELSKIM", "OPIEKA NAD DOKTORANTAMI", "OPIEKA NAD KOŁAMI NAUKOWYMI", "OPIEKA NAD STUDENTAMI (ERASMUS)", "URLOPY NAUKOWE, NIEOBECNOŚCI","NADGODZINY"};
        JSONObject json = null;
        try {
             json = Parser.prepareData(column, my_data);
            //System.out.println(json);
//            JSONObject json_przedmioty = (JSONObject)json.get("przedmioty");
//            double i = (double)json_przedmioty.get("Hurtownie danych - L");
//            System.out.println(i);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        return json;
    }

    double sumColumn(int column){
        JSONObject json = getAllLecturerData(column);
        double sum = 0;
         for( String keys : json.keySet()){
             if( keys.equals("przedmioty")){
                JSONObject json_przedmioty = json.getJSONObject("przedmioty");
                for(String keys_przedmioty: json_przedmioty.keySet()){
                    sum += json_przedmioty.getDouble(keys_przedmioty);
                }
             }else{
                 //sum += keys.equals("GODZINY PONADWYMIAROWE %")? 0:json.getDouble(keys);
                 sum += json.getDouble(keys);
             }
         }
        return sum;
    }

    @Test
    public void checkSumColumn(){
        Assert.assertEquals(1025.05555,sumColumn(14),0.0001);//szmuc
        Assert.assertEquals(1274,sumColumn(15),0.0001);
        Assert.assertEquals(823,sumColumn(16),0.0001);
        Assert.assertEquals(1381.066666,sumColumn(17),0.001);
        Assert.assertEquals(1114,sumColumn(19),0.001);//nalepa
        Assert.assertEquals(1505.254166,sumColumn(27+14),0.0001); // szymkat
        Assert.assertEquals(1650.375,sumColumn(28+14),0.001);

    }
}