package server;

import java.io.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.IOUtils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import parser.*;

import static parser.Parser.findData;
import static parser.Parser.findLecturerColumn;
import static parser.Parser.prepareData;

@RestController
public class SearchController {


    @Autowired
    private UserService userService;

    private static Thread resetThread;

    @PostMapping(value = "/api/search/lastname")
    public boolean searchLecturerByLastname(@RequestBody Lecturer lecturer) throws IOException, InvalidFormatException {
        return Parser.findLecturer(lecturer.getLastName());
    }

    @PostMapping(value = "/api/search/fullname")
    public boolean searchLecturerByFullname(@RequestBody Lecturer lecturer) throws IOException, InvalidFormatException {

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
    public DataPlace getDataPlaceInfo() throws IOException {
        return FilesController.readDataInfo();
    }

    @PostMapping("/api/calc/info")
    public boolean saveDataPlaceInfo(@RequestBody DataPlace dataPlace) {
        FilesController.saveDataInfo(dataPlace);
        return true;
    }


    //INFO O TYM CO JEST DO WYBORU PRZY POKAZYWANIU
    @GetMapping("/api/what_to_show")
    public String[] getDataInfo() throws IOException, InvalidFormatException {
        return findData();
    }


    //TUTAJ PRZESYLASZ TE KTORE MAJA BYC ZAZNACZONE JAKO TRUE W CHECKBOXACH (TABLICA STRINGOW), RESZTA BEDZIE JAKO FALSE, {NUMBER} TO NUMER DLA KILKU OPCJI
    @GetMapping("/api/profile/{number}")
    public String[] getProfile(@PathVariable int number) throws IOException {
            return FilesController.getProfileBody(number);
    }

    //TUTAJ PRZESYLAM INFO JAKIE TRZEBA ZAPISAC POD KONKRETNYM NUMEREM I POTEM JE ZWRACAC W GET
    @PostMapping("/api/profile/{number}/name/{name}")
    public void saveProfile(@PathVariable String name, @PathVariable int number,@RequestBody  String [] data){

        List<String> newData = new ArrayList<>(Arrays.asList(data));
        newData.add(0, name);
        FilesController.saveProfile(number, newData);

    }





    @GetMapping("/api/profile/name/{number}")
    public String getProfileName(@PathVariable int number) throws IOException {
        return FilesController.getProfileName(number);
    }





    @PostMapping("/api/lecturer/{lastName}")
    public String getLecturerData(@PathVariable String lastName, @RequestBody String[] data) throws IOException, InvalidFormatException {
        return prepareData(findLecturerColumn(lastName),data).toString();
    }

    @PostMapping("/api/lecturer/{lastName}/{firstName}")
    public String getLecturerData(@PathVariable String lastName, @PathVariable String firstName, @RequestBody String[] data) throws IOException, InvalidFormatException {

        return prepareData(findLecturerColumn(lastName,firstName),data).toString();
    }

    //TUTAJ USTAW NOWE HASLO
    @PostMapping("/api/reset/{number}")
    public boolean setNewPassword(@PathVariable String number,  @RequestBody String password) {
        try {
            if(new BCryptPasswordEncoder().matches(number, FilesController.getResetCode())){
                userService.updatePassword(password);
                if(resetThread != null) resetThread.interrupt();
                FilesController.deleteResetFile();
                return true;
            }
            else return false;
        } catch (IOException e) {
            return false;
        }
    }


    @GetMapping("/api/reset")
    public boolean sendEmailWithActivationNumber() {
        if(resetThread != null) resetThread.interrupt();
        resetThread = new Thread(new MailController());
        resetThread.start();
        //MailController.sendResetLink();
        return true;
    }



}
