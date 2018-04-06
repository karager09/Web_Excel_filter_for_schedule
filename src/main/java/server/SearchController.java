package server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import parser.Lecturer;
import parser.Parser;
import parser.PlaceOfData;

import javax.servlet.http.HttpServletResponse;

@RestController
public class SearchController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    //@CrossOrigin(origins = "http://localhost:9000")
    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(required=false, defaultValue="World") String name) {
        System.out.println("==== in greeting ====");
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

//    @GetMapping("/greeting-javaconfig")
//    public Greeting greetingWithJavaconfig(@RequestParam(required=false, defaultValue="World") String name) {
//        System.out.println("==== in greeting ====");
//        return new Greeting(counter.incrementAndGet(), String.format(template, name));
//    }

    //@CrossOrigin(origins = "http://localhost:9000")
    @PostMapping(value = "/greeting")
    public Greeting greetingResponse(@RequestBody Greeting greeting) {
        System.out.println("==== in greeting2 ====");
        return new Greeting(greeting.getId()+9, greeting.getContent()+", tak.");
    }

    @PostMapping(value = "/api/search")
    public boolean searchResponse(@RequestBody Lecturer lecturer) throws IOException, InvalidFormatException {
        System.out.println("Searching");
        return Parser.findLecturerLastname(lecturer.getLastName());
    }

    @PostMapping(value = "/api/search/specific")
    public boolean searchResponse2(@RequestBody Lecturer lecturer) throws IOException, InvalidFormatException {
        System.out.println("Searching");
        return Parser.findLecturer(lecturer.getLastName(), lecturer.getFirstName());
    }

//    @GetMapping(value = "/{test}")
//    public String searchResponse3(@PathVariable String test){
//        System.out.print(test);
//        return test;
//    }



    @RequestMapping(value = "/api/plik", method = RequestMethod.POST, produces = {"application/json"})
    public @ResponseBody boolean echoFile(MultipartHttpServletRequest request,
                                                          HttpServletResponse response) throws Exception {


        //System.out.println("Próba przesłania pliku");
        MultipartFile multipartFile = request.getFile("plik");
        Long size = multipartFile.getSize();
        String contentType = multipartFile.getContentType();
        InputStream stream = multipartFile.getInputStream();
        byte[] bytes = IOUtils.toByteArray(stream);

        try (FileOutputStream fos = new FileOutputStream("src\\main\\resources\\test_file.xlsx")) {
            fos.write(bytes);
        }

        return true;
    }

//TUTAJ MUSISZ WSTAWIC DANE, KTÓRE SĄ ZAPISANE NA SERWERZE, TAK ZEBYM JE WSTAWIL DO FORMULARZA
    @GetMapping("/api/info")
    public PlaceOfData pobierz_info() {
        return new PlaceOfData(1,2,3,4,5,6,7);
    }


    //TUTAJ DOSTAJESZ NOWE DANE, WIEC MUSISZ JE GDZIES ZAPISAC
    @PostMapping("/api/info")
    public boolean zapisz_info(@RequestBody PlaceOfData placeOfData) {
//        System.out.println(placeOfData.getNazwisko());
//        System.out.println(placeOfData.getImie());
//        System.out.println(placeOfData.getPrzedmioty());

        return true;
    }


}
