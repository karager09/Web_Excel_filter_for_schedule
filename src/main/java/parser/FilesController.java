package parser;




import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import server.UserService;

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


    private static void saveToFile(String path, List<String> data){
        try {
            Files.write(Paths.get(path), data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveToFile(String path, String data){
        try {
            Files.write(Paths.get(path), data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> readFromFile(String path) throws IOException {
        if(Files.exists(Paths.get(path))) {

            return Files.readAllLines(Paths.get(path));
        }

        else return null;

    }

    private static void renameFile(String path, String newName) throws IOException {
        Path source = Paths.get(path);
        if(Files.exists(source)){
            Files.move(source, source.resolveSibling(newName));
        }

    }

    public static void saveDataInfo(String fileName, DataPlace dataPlace){
        saveToFile(DATA_INFO_PATH+fileName.replace(".xlsx",""), dataPlace.toList());
    }

    public static DataPlace readDataInfo(String fileName) throws IOException {
        return new DataPlace(readFromFile(DATA_INFO_PATH+fileName.replace(".xlsx", "")));
    }

    public static File getSchedule(String fileName){
        return new File(SCHEDULE_PATH+fileName);
    }

    public static void saveProfile(int number, List<String> newData){
        saveToFile(PROFILES_PATH+number, newData);
    }

    public static String getProfileName(int number) throws IOException {
        if(Files.exists(Paths.get(PROFILES_PATH+number))){
            return readFromFile(PROFILES_PATH+number).get(0);
        }
        else
            return "BRAK PROFILU";
    }

    public static String[] getProfileBody(int number) throws IOException {
        String[] result = {};
        if(Files.exists(Paths.get(PROFILES_PATH+number))){

            List<String> tmp = readFromFile(PROFILES_PATH+number);
            result = tmp.subList(1,tmp.size()).toArray(result);
        }
        return result;
    }

    public static String getPassword(String userName) throws IOException {
        return readFromFile(PASSWORD_PATH+userName).get(0);
    }
//    public static String getMasterPassword() throws IOException {
//        return readFromFile(PASSWORD_PATH+"master").get(0);
//    }

    public static void setPassword(String userName, String password) {
        saveToFile(PASSWORD_PATH+userName, new BCryptPasswordEncoder().encode(password));
    }

    private static String getResetCode(String fileName) throws IOException {
        return readFromFile(PASSWORD_RESET_PATH+fileName).get(0);
    }
    private static String getConfirmCode(String fileName) throws IOException {
        return readFromFile(NEWBIES_PATH+fileName).get(0);
    }

    public static boolean checkResetCode(UserService userService, String number, String password) throws IOException {
//        return readFromFile(PASSWORD_RESET_PATH).get(0);
//        new BCryptPasswordEncoder().matches(number, FilesController.getResetCode())
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

    public static void setResetCode(String userName, String resetCode) {
        saveToFile(PASSWORD_RESET_PATH+userName, new BCryptPasswordEncoder().encode(resetCode));
    }

    public static void setConfirmCode(String userName, String resetCode) {
        saveToFile(NEWBIES_PATH+userName, new BCryptPasswordEncoder().encode(resetCode));
    }



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

        private static void createNewUser(String username){
        PasswordGenerator passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                .useDigits(true)
                .useLower(true)
                .useUpper(true)
                .build();
        String password = passwordGenerator.generate(8);
        setPassword(username, password);
        MailController.sendPassword(username, password);

    }



    public static void deleteResetFile(String userName){
        try {
            Files.delete(Paths.get(PASSWORD_RESET_PATH+userName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void deleteConfirmFile(String userName){
        try {
            Files.delete(Paths.get(NEWBIES_PATH+userName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void renameActualScheduleFile(){
        try {
            LocalDateTime localDateTime = LocalDateTime.now();
            String newName = ""+localDateTime.getYear()+localDateTime.getMonth()+localDateTime.getDayOfMonth()+localDateTime.getHour()+localDateTime.getMinute();
            renameFile(SCHEDULE_PATH+"aktualny.xlsx", newName+".xlsx");
            renameFile(DATA_INFO_PATH+"aktualny", newName+"");
            Files.copy(Paths.get(DATA_INFO_PATH+newName), Paths.get(DATA_INFO_PATH+"aktualny"),StandardCopyOption.REPLACE_EXISTING);
            //System.out.println(""+localDateTime.getYear()+localDateTime.getMonth()+localDateTime.getDayOfMonth()+localDateTime.getHour()+localDateTime.getMinute());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String[] getAllSchedules(){
        return getAllFilesName("aktualny.xlsx", SCHEDULE_PATH);

    }

    public static boolean checkUserName(String username){
        return Files.exists(Paths.get(PASSWORD_PATH + username));
    }

    private static String[] getAllFilesName(String exception, String path){
        List<String> files = new ArrayList<>();
        String[] result = {};
        try (Stream<Path> paths = Files.walk(Paths.get(SCHEDULE_PATH))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(s -> {
                        if(s.getFileName().toString().equals(exception)){
                                files.add(0, s.getFileName().toString());
                        }else files.add(s.getFileName().toString());
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files.toArray(result);
    }

    private static void deleteFile(String path) throws IOException {
        Files.delete(Paths.get(path));
    }

    public static boolean deleteSchedule(String name){
        try {
            deleteFile(SCHEDULE_PATH+name);
            deleteFile(DATA_INFO_PATH+name.replace(".xlsx", ""));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean register(String email){
        if(!Files.exists(Paths.get(PASSWORD_PATH + email))){
            MailController.sendNotification(email);
            return true;
        }
        return false;
    }


}
