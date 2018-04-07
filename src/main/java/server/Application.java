package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import parser.Parser;
import parser.PlaceOfData;

import java.io.IOException;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        try{
            Parser.setData(PlaceOfData.readFromFile());
        }catch (IOException e) {
            e.printStackTrace();
        }
        SpringApplication.run(Application.class, args);
    }
}
