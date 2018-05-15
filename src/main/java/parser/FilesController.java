package parser;




import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import server.security.UserService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FilesController {

    private static final String PROFILES_PATH = "src\\main\\resources\\parser\\profiles\\";
    private static final String SCHEDULE_PATH = "src\\main\\resources\\parser\\schedule\\";
    private static final String DATA_INFO_PATH = "src\\main\\resources\\parser\\data\\";
    private static final String PASSWORD_PATH =  "src\\main\\resources\\users\\data\\";
    private static final String PASSWORD_RESET_PATH = "src\\main\\resources\\users\\reset\\";
    private static final String NEWBIES_PATH = "src\\main\\resources\\users\\newbies\\";


    private static void saveToFile(String path, List<String> data) throws IOException {
        try {
            Files.write(Paths.get(path), data);
        } catch (IOException e) {
            throw new IOException();
        }
    }

    private static void saveToFile(String path, String data) throws IOException {
        try {
            Files.write(Paths.get(path), data.getBytes());
        } catch (IOException e) {
            throw new IOException();
        }
    }

    private static List<String> readFromFile(String path) throws IOException {
        return Files.readAllLines(Paths.get(path));
    }

    private static void renameFile(String path, String newName) throws Exception {
        Path source = Paths.get(path);
        try {
            Files.move(source, source.resolveSibling(newName));
        } catch (IOException e) {
            throw new Exception();
        }
    }

    /**
     *
     * @param fileName nazwa pliku w którym zapiszemy konfigurację Excela
     * @param dataPlace dane konfiguracji Excela
     */
    public static void saveDataInfo(String fileName, DataPlace dataPlace) throws IOException{
        saveToFile(DATA_INFO_PATH+fileName.replace(".xlsx",""), dataPlace.toList());
    }

    /**
     * Owiera plik z danymi
     * @param fileName plik z którego chcemy czytać dane
     * @return zwraca plik z którego będziemy czytać dane
     */
    public static File getSchedule(String fileName){
        return new File(SCHEDULE_PATH+fileName);
    }

    /**
     *
     * @param fileName plik z którego chcemy odczytać konfigurację
     * @return zwraca konfigurację
     * @throws IOException jeżeli nie ma takiego pliku lub atalogu
     */
    public static DataPlace readDataInfo(String fileName) throws IOException {
        return new DataPlace(readFromFile(DATA_INFO_PATH+fileName.replace(".xlsx", "")));
    }

    /**
     * Zwraca nazwy wszystkich dostępnych plików z rozkładem zajęć
     * @return Zwraca nazwy wszystkich dostępnych plików z rozkładem zajęć
     * @throws Exception w przypadku problemów z plikami
     */
    public static String[] getAllSchedules() throws Exception {
        List<String> files = new ArrayList<>();
        String[] result = {};
        try (Stream<Path> paths = Files.walk(Paths.get(SCHEDULE_PATH))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(s -> {
                        if(s.getFileName().toString().equals("aktualny.xlsx")){
                            files.add(0, s.getFileName().toString());
                        }else files.add(s.getFileName().toString());
                    });
        } catch (IOException e) {
            throw new Exception();
        }
        return files.toArray(result);
    }

    /**
     * kasuje plik Excel o podanej nazwie wraz z jego konfiguracją
     * @param name nazwa pliku do skasowania
     * @return zwraca informację o powodzeniu
     */
    public static boolean deleteSchedule(String name){
        try {
            deleteFile(SCHEDULE_PATH+name);
            deleteFile(DATA_INFO_PATH+name.replace(".xlsx", ""));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Zmienia nazwę aktualnego pliku z rozkładem zajęć na przestarzały
     */
    public static void renameActualScheduleFile() throws Exception{
        LocalDateTime localDateTime = LocalDateTime.now();
        String newName = ""+localDateTime.getYear()+localDateTime.getMonth()+localDateTime.getDayOfMonth()+localDateTime.getHour()+localDateTime.getMinute();
        if(Files.exists(Paths.get(SCHEDULE_PATH+"aktualny.xlsx"))){
            renameFile(SCHEDULE_PATH+"aktualny.xlsx", newName+".xlsx");
            renameFile(DATA_INFO_PATH+"aktualny", newName+"");
            Files.copy(Paths.get(DATA_INFO_PATH+newName), Paths.get(DATA_INFO_PATH+"aktualny"),StandardCopyOption.REPLACE_EXISTING);
        }
    }

    /**
     * Zwraca dane profilu
     * @param number numer profilu
     * @return dane profilu
     * @throws IOException
     */
    public static String[] getProfileBody(int number) throws IOException {
        String[] result = {};
        if(Files.exists(Paths.get(PROFILES_PATH+number))){

            List<String> tmp = readFromFile(PROFILES_PATH+number);
            result = tmp.subList(1,tmp.size()).toArray(result);
        }
        return result;
    }

    /**
     * Zapisuje profil o danum numerze
     * @param number numer profilu
     * @param newData dane profilu
     * @throws IOException
     */
    public static void saveProfile(int number, List<String> newData) throws IOException {
        saveToFile(PROFILES_PATH+number, newData);
    }

    /**
     * Zwraca nazwę profilu o zadanym numerze
     * @param number numer profilu
     * @return
     * @throws IOException
     */
    public static String getProfileName(int number) throws IOException {
        if(Files.exists(Paths.get(PROFILES_PATH+number))){
            return readFromFile(PROFILES_PATH+number).get(0);
        }
        else
            return "BRAK PROFILU";
    }


    /**
     *  sprawdze poprawność linku do resetowania hasła i zmienia hasło
     * @param userService czyje hasło ma zostać zmienione
     * @param number kod do walidacji linku
     * @param password nowe hasło
     * @return
     * @throws IOException
     */
    public static boolean checkResetCode(UserService userService, String number, String password) throws IOException {
        List<String> files = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(PASSWORD_RESET_PATH))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(s ->
                            files.add(s.getFileName().toString())
                    );
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(String fileName: files){
            if(new BCryptPasswordEncoder().matches(number, FilesController.getResetCode(fileName))){
                deleteResetFile(fileName);
                userService.updatePassword(fileName,password);
                return true;
            }
        }
        return false;
    }


    /**
     * zwraca hasło użytkownika z pliku
     * @param userName czyje hasło
     * @return
     * @throws IOException
     */
    public static String getPassword(String userName) throws IOException {
        return readFromFile(PASSWORD_PATH+userName).get(0);
    }
//    public static String getMasterPassword() throws IOException {
//        return readFromFile(PASSWORD_PATH+"master").get(0);
//    }

    /**
     * ustawia nowe hasło dla uzytkownika
     * @param userName dla kogo
     * @param password nowe hasło
     * @throws IOException
     */
    public static void setPassword(String userName, String password) throws IOException {
        saveToFile(PASSWORD_PATH+userName, new BCryptPasswordEncoder().encode(password));
    }

    /**
     * zwraca kod walidacyjny do resetu hasła
     * @param fileName
     * @return
     * @throws IOException
     */
    private static String getResetCode(String fileName) throws IOException {
        return readFromFile(PASSWORD_RESET_PATH+fileName).get(0);
    }

    /**
     * zwraca kod walidacyjny do rejestracji uzytkownika
     * @param fileName
     * @return
     * @throws IOException
     */
    private static String getConfirmCode(String fileName) {

        try {
            return readFromFile(NEWBIES_PATH+fileName).get(0);
        } catch (IOException e) {
            return "";
        }
    }


    /**
     * ustawia kod walidacyjny do resetu hasła
     * @param userName
     * @param resetCode
     * @throws IOException
     */
    public static void setResetCode(String userName, String resetCode) throws IOException {
        saveToFile(PASSWORD_RESET_PATH+userName, new BCryptPasswordEncoder().encode(resetCode));
    }

    /**
     * ustawia kod walidacyjny do rejestracji uzytkownika
     * @param userName
     * @param resetCode
     * @throws IOException
     */
    public static void setConfirmCode(String userName, String resetCode) throws IOException {
        saveToFile(NEWBIES_PATH+userName, new BCryptPasswordEncoder().encode(resetCode));
    }


    /**
     * sprawdza popranowść kodu walidacyjnego do rejestracji użytkownika
     * @param number
     * @return
     * @throws IOException
     */
    public static boolean checkConfirmCode(String number) throws IOException {
//        return readFromFile(PASSWORD_RESET_PATH).get(0);
//        new BCryptPasswordEncoder().matches(number, FilesController.getResetCode())
        List<String> files = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(NEWBIES_PATH))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(s ->
                            files.add(s.getFileName().toString())
                    );
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(String fileName: files){
            if(new BCryptPasswordEncoder().matches(number, FilesController.getConfirmCode(fileName))){
                createNewUser(fileName);
                deleteConfirmFile(fileName);
                return true;
            }
        }
        return false;

    }

    /**
     * tworzy nowego użytkownika
     * @param username
     * @throws IOException
     */
    private static void createNewUser(String username) throws IOException {
        PasswordGenerator passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                .useDigits(true)
                .useLower(true)
                .useUpper(true)
                .build();
        String password = passwordGenerator.generate(8);
        setPassword(username, password);
        MailController.sendPassword(username, password);

    }


    /**
     * kasuje plik po poprawnym zresetowaniu hasła
     * @param userName
     */
    public static void deleteResetFile(String userName){
        try {
            Files.delete(Paths.get(PASSWORD_RESET_PATH+userName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * kasuje plik po poprawnym zarejestrowaniu uzytkownika
     * @param userName
     */
    public static void deleteConfirmFile(String userName){
        try {
            Files.delete(Paths.get(NEWBIES_PATH+userName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * sprawdza czy użytkownik istnieje
     * @param username
     * @return
     */
    public static boolean checkUserName(String username){
        return Files.exists(Paths.get(PASSWORD_PATH + username));
    }


    private static void deleteFile(String path) throws IOException {
        Files.delete(Paths.get(path));
    }


    /**
     * wysyła powiadomienie do mastera o nowej próbie rejestracji
     * @param email
     * @return
     * @throws IOException
     */
    public static boolean register(String email) throws IOException {
        if(!Files.exists(Paths.get(PASSWORD_PATH + email))){
            MailController.sendNotification(email);
            return true;
        }
        return false;
    }


}
