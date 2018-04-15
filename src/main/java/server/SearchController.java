package server;

import java.io.*;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.IOUtils;
import org.json.JSONObject;
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

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @PostMapping(value = "/api/search/lastname")
    public boolean searchResponse(@RequestBody Lecturer lecturer) throws IOException, InvalidFormatException {
        System.out.println("Searching");
        return Parser.findLecturerLastname(lecturer.getLastName());
    }

    @PostMapping(value = "/api/search/fullname")
    public boolean searchResponse2(@RequestBody Lecturer lecturer) throws IOException, InvalidFormatException {
        System.out.println("Searching");
        return Parser.findLecturer(lecturer.getLastName(), lecturer.getFirstName());
    }

//    @GetMapping(value = "/{test}")
//    public String searchResponse3(@PathVariable String test){
//        System.out.print(test);
//        return test;
//    }



    @RequestMapping(value = "/api/calc/file", method = RequestMethod.POST, produces = {"application/json"})
    public @ResponseBody boolean echoFile(MultipartHttpServletRequest request,
                                                          HttpServletResponse response) throws Exception {


        //System.out.println("Próba przesłania pliku");
        MultipartFile multipartFile = request.getFile("plik");
        Long size = multipartFile.getSize();
        String contentType = multipartFile.getContentType();
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
//        String [] what= new String[2];
//        what[0] = "Ilość godzin";
//        what[1] = "Pensja";

    }



    @PostMapping("/api/calc/info")
    public boolean zapisz_info(@RequestBody PlaceOfData placeOfData) throws IOException{
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
