package parser;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;



public class ParserTest {


    @Test
    public void findLecturerLastname() {
        try {
            Parser.setData(PlaceOfData.readFromFile());
            Assert.assertTrue(Parser.findLecturerLastname("Szymkat"));
            Assert.assertFalse(Parser.findLecturerLastname("Buczek"));
            Assert.assertFalse((Parser.findLecturerLastname("Szmuc")));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(":O");
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void findLecturer() {
        try {
            Parser.setData(PlaceOfData.readFromFile());
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


    @Test
    public void findLecturerColumn() {
        try {
            Parser.setData(PlaceOfData.readFromFile());
            Assert.assertEquals(15,Parser.findLecturerColumn("Bielecki"));
            Assert.assertEquals(0,Parser.findLecturerColumn("Buczek"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(":O");
        } catch (InvalidFormatException e) {
            e.printStackTrace();

        }
    }

    @Test
    public void findLecturerColumn1() {
        try {
            Parser.setData(PlaceOfData.readFromFile());
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

}