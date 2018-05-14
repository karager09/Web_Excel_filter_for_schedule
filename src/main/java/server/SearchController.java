package server;

import java.io.*;

import java.nio.file.Files;
import java.util.*;


import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.IOUtils;


import org.springframework.beans.factory.annotation.Autowired;
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



    private static Map<String, Thread> threadMap = new HashMap<>();


//    @PostMapping(value = "/api/search/lastname")
//    public boolean searchLecturerByLastname(@RequestBody Lecturer lecturer) throws IOException, InvalidFormatException {
//        return Parser.findLecturer(lecturer.getLastName());
//    }


    @PostMapping(value = "/api/{fileName}/search/fullname")
    public boolean searchLecturerByFullname(@PathVariable String fileName,@RequestBody Lecturer lecturer) throws IOException, InvalidFormatException {

        return Parser.findLecturer(fileName, lecturer.getLastName(), lecturer.getFirstName());
    }


    @GetMapping("/api/files")
    public String[] getNameOfFiles() throws IOException {

        return FilesController.getAllSchedules();
    }

    @DeleteMapping("/api/files/{fileName}")
    public boolean deleteFile(@PathVariable String fileName) throws IOException {
        return FilesController.deleteSchedule(fileName);

    }

    @RequestMapping(value = "/api/calc/file", method = RequestMethod.POST, produces = {"application/json"})
    public @ResponseBody boolean echoFile(MultipartHttpServletRequest request/*,
                                                          HttpServletResponse response*/) throws Exception {

        FilesController.renameActualScheduleFile();
        MultipartFile multipartFile = request.getFile("plik");
       // Long size = multipartFile.getSize();
       // String contentType = multipartFile.getContentType();
        InputStream stream = multipartFile.getInputStream();
        byte[] bytes = IOUtils.toByteArray(stream);
        try (FileOutputStream fos = new FileOutputStream("src\\main\\resources\\parser\\schedule\\aktualny.xlsx")) {
            fos.write(bytes);
        }

        return true;
    }


    @GetMapping("/api/{fileName}/calc/info")
    public DataPlace getDataPlaceInfo(@PathVariable String fileName) throws IOException {
        return FilesController.readDataInfo(fileName);
    }


    @PostMapping("/api/{fileName}/calc/info")
    public boolean saveDataPlaceInfo(@PathVariable String fileName, @RequestBody DataPlace dataPlace) {
        FilesController.saveDataInfo(fileName, dataPlace);
        return true;
    }



    @GetMapping("/api/{fileName}/what_to_show")
    public String[] getDataInfo(@PathVariable String fileName) throws IOException, InvalidFormatException {
        return findData(fileName);
    }


    //TUTAJ PRZESYLASZ TE KTORE MAJA BYC ZAZNACZONE JAKO TRUE W CHECKBOXACH (TABLICA STRINGOW), RESZTA BEDZIE JAKO FALSE, {NUMBER} TO NUMER DLA KILKU OPCJI
    //trzeba zmienic na konkretny plik
    @GetMapping("/api/{fileName}/profile/{number}")
    public String[] getProfile(@PathVariable String fileName, @PathVariable int number) throws IOException {
            return FilesController.getProfileBody(number);
    }

    //TUTAJ PRZESYLAM INFO JAKIE TRZEBA ZAPISAC POD KONKRETNYM NUMEREM I POTEM JE ZWRACAC W GET
    //zmiana na konkretny plik
    @PostMapping("/api/{fileName}/profile/{number}/name/{name}")
    public void saveProfile(@PathVariable String fileName,@PathVariable String name, @PathVariable int number,@RequestBody  String [] data){

        List<String> newData = new ArrayList<>(Arrays.asList(data));
        newData.add(0, name);
        FilesController.saveProfile(number, newData);

    }




    //zmiana na konkretny plik
    @GetMapping("/api/{fileName}/profile/name/{number}")
    public String getProfileName(@PathVariable String fileName,@PathVariable int number) throws IOException {
        return FilesController.getProfileName(number);
    }





//    @PostMapping("/api/lecturer/{lastName}")
//    public String getLecturerData(@PathVariable String lastName, @RequestBody String[] aktualny) throws IOException, InvalidFormatException {
//        return prepareData(findLecturerColumn(lastName),aktualny).toString();
//    }

    @PostMapping("/api/{fileName}/lecturer/{lastName}/{firstName}")
    public String getLecturerData(@PathVariable String fileName,@PathVariable String lastName, @PathVariable String firstName, @RequestBody String[] data) throws IOException, InvalidFormatException {

        return prepareData(fileName, findLecturerColumn(fileName, lastName,firstName),data).toString();
    }

    //TUTAJ USTAW NOWE HASLO
    @PostMapping("/api/reset/{number}")
    public boolean setNewPassword(@PathVariable String number,  @RequestBody String password) throws IOException {
//        try {
//            if(FilesController.checkResetCode(number)){
//                userService.updatePassword(password);
//                if(resetThread != null) resetThread.interrupt();
//               // FilesController.deleteResetFile();
//                return true;
//            }
//            else return false;
//        } catch (IOException e) {
//            return false;
//        }
        return FilesController.checkResetCode(userService, number, password);
    }


    @GetMapping("/api/reset/{username}")
    public boolean sendEmailWithActivationNumber(@PathVariable String username) {
        System.out.println(username);
        if(threadMap.containsKey(username)) {
            threadMap.get(username).interrupt();
            threadMap.remove(username);
        }
        Thread thread = new Thread(new MailController(username));
        threadMap.put(username, thread);
        thread.start();
//        if(resetThread != null) resetThread.interrupt();
//        resetThread = new Thread(new MailController(username));
//        resetThread.start();
//        //MailController.sendResetLink();
        return true;
    }


    //tutaj trzeba zarejestrowac nowego uzytkownika, wyslac maila do mastera? do tego kogos?
    @PostMapping("/api/register")
    public boolean register(@RequestBody String email) {
        //System.out.println(email);
        return FilesController.register(email);
        //

    }


    @GetMapping("/api/confirm/{number}")
    public String confirmRegister(@PathVariable String number){
        try {
            if(FilesController.checkConfirmCode(number)) return "Użytkownik został dodany.";
            else return "Błędny link potwierdzający.";
        } catch (IOException e) {
            return "Błąd";
        }
    }



}
