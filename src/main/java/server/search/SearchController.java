package server.search;

import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import parser.*;
import server.security.UserService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@RestController
public class SearchController {


    @Autowired
    private UserService userService;
    private static Map<String, Thread> threadMap = new HashMap<>();

    /**
     * Obsługa wyjątków rzucanych przez metody REST
     * @return zwraca pusty model i wodok, łapany w kodzie JS
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleError() {
        return new ModelAndView();
    }


    /**
     * Zwraca konfigurację pliku Excel
     * @param fileName nazwa pliku dla którego chcemy pobrać konfigurację
     * @return zwraca konfigurację
     * @throws Exception jeżeli bląd związany z odczytem danych z pliku
     */
    @GetMapping("/api/{fileName}/calc/info")
    public DataPlace getDataPlaceInfo(@PathVariable String fileName) throws IOException {
        return FilesController.readDataInfo(fileName);

    }


    /**
     * zapisuje konfigurację pliku Excel
     * @param fileName nawzwa pliku dla którego chcemy zapisać konfigurację
     * @param dataPlace konfiguracja którą chcemy zapisać
     * @return zwraca wynik zapisu konfiguracji
     */
    @PostMapping("/api/{fileName}/calc/info")
    public boolean saveDataPlaceInfo(@PathVariable String fileName, @RequestBody DataPlace dataPlace) {
        try {
            FilesController.saveDataInfo(fileName, dataPlace);
        } catch (IOException e) {
            return false;
        }
        return true;

    }

    /**
     * zwraca informację o dostępnych danych
     * @param fileName plik z którego chcemy informację o dostępnych danych
     * @return zwraca tablice z dostępnymi danymi
     * @throws Exception jeżeli problemy z plikami
     */
    @GetMapping("/api/{fileName}/what_to_show")
    public String[] getDataInfo(@PathVariable String fileName) throws Exception {
        return Parser.findData(fileName);

    }

    /**
     *
     * @return zwraca talicę z nazwami wszystkich dostępych plików z rozkładem zajęć
     * @throws IOException w przypadku problemów z plikami
     */
    @GetMapping("/api/files")
    public String[] getNameOfFiles() throws Exception {
        return FilesController.getAllSchedules();
    }


    /**
     * Kasuje plik z rozkładem zajęć
     * @param fileName nazwa pliku do skasowania
     * @return zwraca informację o powodzeniu
     */
    @DeleteMapping("/api/files/{fileName}")
    public boolean deleteFile(@PathVariable String fileName) {
        return FilesController.deleteSchedule(fileName);

    }


    /**
     * Wczytuje plik z rozkładem zajęć
     * @param request
     * @return  zwraca informację o powodzeniu
     * @throws Exception w przypadku
     */
    @RequestMapping(value = "/api/calc/file", method = RequestMethod.POST, produces = {"application/json"})
    public @ResponseBody boolean echoFile(MultipartHttpServletRequest request/*,
                                                          HttpServletResponse response*/) throws Exception {

        FilesController.renameActualScheduleFile();
        MultipartFile multipartFile = request.getFile("plik");
        InputStream stream = multipartFile.getInputStream();
        byte[] bytes = IOUtils.toByteArray(stream);
        try (FileOutputStream fos = new FileOutputStream("src\\main\\resources\\parser\\schedule\\aktualny.xlsx")) {
            fos.write(bytes);
        }
        return true;
    }


    /**
     *
     * @param fileName nazwa pliku, którym wyszukujemy czy prowadzący istnieje
     * @param lecturer dane prowadzącego
     * @return zwraca informację w postaci napisu o wyniku wyszukiwania
     */
    @PostMapping(value = "/api/{fileName}/search/fullname")
    public String searchLecturer(@PathVariable String fileName, @RequestBody Lecturer lecturer) {
        return Parser.findLecturer(fileName, lecturer.getLastName(), lecturer.getFirstName());
    }

    /**
     *
     * @param fileName plik, w którym szukamy
     * @param lastName nazwisko prowadzącego
     * @param firstName imię prowadzącego
     * @param data dane które chcemy wyświetlić z Excela
     * @return zwraca JSON w postaci String z danymi do wyświetlenia
     * @throws Exception rzuca wyjątkami spowodowanymi błędami z plikach
     */
    @PostMapping("/api/{fileName}/lecturer/{lastName}/{firstName}")
    public String getLecturerData(@PathVariable String fileName, @PathVariable String lastName, @PathVariable String firstName, @RequestBody String[] data) throws Exception {
        return Parser.prepareData(fileName, Parser.findLecturerColumn(fileName, lastName, firstName), data).toString();
    }




    /**
     * Zwraca dane profilu
     * @param fileName plik dla którego chcemy profile, obecnie nie ma znaczenia
     * @param number numer profilu
     * @return dane z profilu
     * @throws IOException
     */
    @GetMapping("/api/{fileName}/profile/{number}")
    public String[] getProfile(@PathVariable String fileName, @PathVariable int number) throws Exception {
        return FilesController.getProfileBody(number);
    }


    /**
     * Zapisuje nowy profil
     * @param fileName nazwa pliku dla którego tworzony jest profil, obecnie niepotrzebne
     * @param name nazwa profilu
     * @param number numer profilu
     * @param data dane do zapisania
     * @throws IOException
     */
    @PostMapping("/api/{fileName}/profile/{number}/name/{name}")
    public void saveProfile(@PathVariable String fileName,@PathVariable String name, @PathVariable int number,@RequestBody String [] data) throws Exception {
        List<String> newData = new ArrayList<>(Arrays.asList(data));
        newData.add(0, name);
        FilesController.saveProfile(number, newData);

    }


    /**
     * Zwraca nazwę profilu o zadanym numerze
     * @param fileName obecnie nie potrzebne
     * @param number numer profilu
     * @return zwracana nazwa profilu
     * @throws IOException
     */
    @GetMapping("/api/{fileName}/profile/name/{number}")
    public String getProfileName(@PathVariable String fileName,@PathVariable int number) throws IOException {
        return FilesController.getProfileName(number);
    }


    /**
     * ustwaia nowe hasło jeśli link jest poprawny
     * @param number link do walidacji
     * @param password nowe hasło
     * @return
     * @throws Exception
     */
    @PostMapping("/api/reset/{number}")
    public boolean setNewPassword(@PathVariable String number, @RequestBody String password) throws Exception {
        return FilesController.checkResetCode(userService, number, password);
    }


    /**
     * Wysyła link do resetu hasła
     * @param username dla kogo ten reset
     * @return
     */
    @GetMapping("/api/reset/{username}")
    public boolean sendEmailWithActivationNumber(@PathVariable String username) {
        if(threadMap.containsKey(username)) {
            threadMap.get(username).interrupt();
            threadMap.remove(username);
        }
        Thread thread = new Thread(new MailController(username));
        threadMap.put(username, thread);
        thread.start();
        return true;
    }


    //tutaj trzeba zarejestrowac nowego uzytkownika, wyslac maila do mastera? do tego kogos?

    /**
     * rejestracja nowego użytkownika
     * @param email
     * @return
     * @throws IOException
     */
    @PostMapping("/api/register")
    public boolean register(@RequestBody String email) throws IOException {
        //System.out.println(email);
        return FilesController.register(email);
        //

    }

    /**
     * potwierdzenie rejestracji przez Mastera
     * @param number
     * @return
     */
    @GetMapping("/api/confirm/{number}")
    public String confirmRegister(@PathVariable String number){
        try {
            if(FilesController.checkConfirmCode(number)) return "Użytkownik został dodany.";
            else return "Błędny link potwierdzający.";
        } catch (Exception e) {
            System.out.println("s");
            return "Błąd";
        }
    }
}
