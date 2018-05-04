package parser;




import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FilesController {

    private static final String PROFILES_PATH = "src\\main\\resources\\parser\\profiles\\";
    private static final String SCHEDULE_PATH = "src\\main\\resources\\parser\\schedule\\rozklad.xlsx";
    private static final String DATA_INFO_PATH = "src\\main\\resources\\parser\\schedule\\data";
    private static final String PASSWORD_PATH =  "src\\main\\resources\\parser\\password\\password";
    private static final String PASSWORD_RESET_PATH = "src\\main\\resources\\parser\\password\\reset";


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
        return Files.readAllLines(Paths.get(path));

    }

    public static void saveDataInfo(DataPlace dataPlace){
        saveToFile(DATA_INFO_PATH, dataPlace.toList());
    }

    public static DataPlace readDataInfo() throws IOException {
        return new DataPlace(readFromFile(DATA_INFO_PATH));
    }

    public static File getSchedule(){
        return new File(SCHEDULE_PATH);
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

    public static String getPassword() throws IOException {
        return readFromFile(PASSWORD_PATH).get(0);
    }

    public static void setPassword(String password) {
        saveToFile(PASSWORD_PATH, new BCryptPasswordEncoder().encode(password));
    }

    public static String getResetCode() throws IOException {
        return readFromFile(PASSWORD_RESET_PATH).get(0);
    }

    public static void setResetCode(String resetCode) {
        saveToFile(PASSWORD_RESET_PATH, new BCryptPasswordEncoder().encode(resetCode));
    }
    public static void deleteResetFile(){
        try {
            Files.delete(Paths.get(PASSWORD_RESET_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
