package server;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.web.bind.annotation.*;
import parser.Lecturer;
import parser.Parser;

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

}
