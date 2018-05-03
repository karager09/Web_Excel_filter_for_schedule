package server;

import java.io.*;
import java.nio.file.Files;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.IOUtils;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import parser.Lecturer;
import parser.Parser;
import parser.PlaceOfData;

import javax.servlet.http.HttpServletResponse;

import static parser.Parser.findData;
import static parser.Parser.findLecturerColumn;
import static parser.Parser.prepareData;

@RestController
public class SearchController {

    private static final String profilesPath = "src\\main\\resources\\profiles\\";

    @PostMapping(value = "/api/search/lastname")
    public boolean searchResponse(@RequestBody Lecturer lecturer) throws IOException, InvalidFormatException {

        return Parser.findLecturerLastname(lecturer.getLastName());
    }

    @PostMapping(value = "/api/search/fullname")
    public boolean searchResponse2(@RequestBody Lecturer lecturer) throws IOException, InvalidFormatException {

        return Parser.findLecturer(lecturer.getLastName(), lecturer.getFirstName());
    }




    @RequestMapping(value = "/api/calc/file", method = RequestMethod.POST, produces = {"application/json"})
    public @ResponseBody boolean echoFile(MultipartHttpServletRequest request/*,
                                                          HttpServletResponse response*/) throws Exception {


        MultipartFile multipartFile = request.getFile("plik");
       // Long size = multipartFile.getSize();
       // String contentType = multipartFile.getContentType();
        InputStream stream = multipartFile.getInputStream();
        byte[] bytes = IOUtils.toByteArray(stream);

        try (FileOutputStream fos = new FileOutputStream("src\\main\\resources\\rozklad.xlsx")) {
            fos.write(bytes);
        }

        return true;
    }

//TUTAJ MUSISZ WSTAWIC DANE, KTÓRE SĄ ZAPISANE NA SERWERZE, TAK ZEBYM JE WSTAWIL DO FORMULARZA
    @GetMapping("/api/calc/info")
    public PlaceOfData pobierz_info() {
        return Parser.getData();
    }


    //INFO O TYM CO JEST DO WYBORU PRZY POKAZYWANIU
    @GetMapping("/api/what_to_show")
    public String[] pobierz_info_o_xml() throws IOException, InvalidFormatException {

        return findData();


    }


    //TUTAJ PRZESYLASZ TE KTORE MAJA BYC ZAZNACZONE JAKO TRUE W CHECKBOXACH (TABLICA STRINGOW), RESZTA BEDZIE JAKO FALSE, {NUMBER} TO NUMER DLA KILKU OPCJI
    @GetMapping("/api/what_to_show/{number}")
    public String[] getProfile(@PathVariable int number) throws IOException, InvalidFormatException {

        String[] result = {};
        if(Files.exists(Paths.get(profilesPath+number))){

            List<String> tmp = Files.readAllLines(Paths.get(profilesPath+number));
            result = tmp.subList(1,tmp.size()).toArray(result);
        }
            return result;

    }

    //TUTAJ PRZESYLAM INFO JAKIE TRZEBA ZAPISAC POD KONKRETNYM NUMEREM I POTEM JE ZWRACAC W GET
    @PostMapping("/api/what_to_show/{number}/name/{name}")
    public void saveProfile(@PathVariable String name, @PathVariable int number,@RequestBody  String [] data) throws IOException, InvalidFormatException {

        List newData = new ArrayList<String>(Arrays.asList(data));
        newData.add(0, name);
        Files.write(Paths.get(profilesPath+number), newData);


    }


//    @PostMapping("/api/what_to_show/name/{number}")
//    public void saveProfileName(@PathVariable int number,@RequestBody  String data) throws IOException, InvalidFormatException {
//        Files.write(Paths.get("src\\main\\resources\\profiles\\"+number),data.getBytes());
//
//    }


    @GetMapping("/api/what_to_show/name/{number}")
    public String getProfileName(@PathVariable int number) throws IOException, InvalidFormatException {
        if(Files.exists(Paths.get(profilesPath+number))){
            return Files.readAllLines(Paths.get(profilesPath+number)).get(0);
        }
        else
            return "BRAK PROFILU";
    }



    @PostMapping("/api/calc/info")
    public boolean saveDataPlace(@RequestBody PlaceOfData placeOfData) throws IOException{
        PlaceOfData.saveAsFile(placeOfData);
        Parser.setData(placeOfData);

        return true;
    }

    @PostMapping("/api/lecturer/{lastName}")
    public String getLecruterData(@PathVariable String lastName, @RequestBody String[] data) throws IOException, InvalidFormatException {

        return prepareData(findLecturerColumn(lastName),data).toString();
    }

    @PostMapping("/api/lecturer/{lastName}/{firstName}")
    public String getLecruterData(@PathVariable String lastName, @PathVariable String firstName, @RequestBody String[] data) throws IOException, InvalidFormatException {

        return prepareData(findLecturerColumn(lastName,firstName),data).toString();
    }



}
