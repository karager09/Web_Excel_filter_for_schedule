package parser;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONObject;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {


    /**
     * Funkcja do wyszukiwania czy istnieje prowadzący w danym pliku Excel
     * @param fileName nazwa pliku, w którym szukamy
     * @param lastName nazwisko prowadzącego
     * @return infroacja o wyniku wyszukiwania "OK" w przypadku pozytywnego odnalezienia
     */
    public static String findLecturer(String fileName, String lastName) {
        try {
            DataPlace dataPlace = FilesController.readDataInfo(fileName);
            Workbook workbook = WorkbookFactory.create(FilesController.getSchedule(fileName));
            int counter = 0;
            Sheet sheet;
            sheet = workbook.getSheetAt(0);
            DataFormatter dataFormatter = new DataFormatter();
            Row row =  sheet.getRow(dataPlace.getNazwisko()-1);
            for(Cell cell : row){
                String cellValue = dataFormatter.formatCellValue(cell).trim();
                if(cellValue.equalsIgnoreCase(lastName)) counter++;
            }
            workbook.close();
            if(counter == 1) return "OK";
            else if (counter > 1) return "Istnieje więcej niż jedna osoba o tym nazwisku";
            else return "Nie istnieje prowadzący o tym nazwisku";
        } catch (IOException e) {
            return "Nie ma takiego pliku";
        } catch (InvalidFormatException e){
            return "Zły format pliku";
        } catch (IndexOutOfBoundsException e) {
            return "Pusty plik z formatem danych";
        }
    }

    /**
     * Funkcja do wyszukiwania czy istnieje prowadzący w danym pliku Excel
     * @param fileName nazwa pliku, w którym szukamy
     * @param lastName nazwisko prowadzącego
     * @param firstName imię prowadzącego
     * @return informacja o wyniku wyszukiwania "OK" w przypadku pozytywnego odnalezienia
     */
    public static String findLecturer(String fileName, String lastName, String firstName) {
        try {
            DataPlace dataPlace = FilesController.readDataInfo(fileName);
            Workbook workbook = WorkbookFactory.create(FilesController.getSchedule(fileName));
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter dataFormatter = new DataFormatter();
            Row lastNameRow = sheet.getRow(dataPlace.getNazwisko()-1);
            Row firstNameRow = sheet.getRow(dataPlace.getImie()-1);
            for (Cell cell : lastNameRow) {
                String lastValue = dataFormatter.formatCellValue(cell).trim();
                if (lastValue.equalsIgnoreCase(lastName)) {
                    String firstValue = dataFormatter.formatCellValue(firstNameRow.getCell(cell.getColumnIndex())).trim();
                    if (firstValue.equalsIgnoreCase(firstName)) {
                        workbook.close();
                        return "OK";
                    }
                }
            }
            workbook.close();
            return "Nie ma takiego prowadzącego";
        } catch (IOException e) {
            return "Nie ma takiego pliku";
        } catch (InvalidFormatException e){
            return "Zły format pliku";
        } catch (IndexOutOfBoundsException e) {
            return "Pusty plik z formatem danych";
        }
    }

    /**
     * Znajduje kolumnę w której znajdują sie dane konkretnego prowadzącego
     * @param fileName plik, w którym szukamy
     * @param lastName nazwisko prowadzącego
     * @return numer kolumny
     * @throws IOException jeżeli plik w którym szukamy lub trzymamy dane o formacie nie istnieje
     * @throws InvalidFormatException jeżeli błędny format pliku
     * @throws IndexOutOfBoundsException jeżeli plik z formatem jest pusty
     */
    public static int findLecturerColumn(String fileName, String lastName) throws IOException, InvalidFormatException, IndexOutOfBoundsException {
        DataPlace dataPlace = FilesController.readDataInfo(fileName);
        Workbook workbook = WorkbookFactory.create(FilesController.getSchedule(fileName));
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();
        Row row =  sheet.getRow(dataPlace.getNazwisko()-1);
        for(Cell cell : row){
            String cellValue = dataFormatter.formatCellValue(cell).trim();
            if((cellValue.trim()).equalsIgnoreCase(lastName)){
                return cell.getColumnIndex();
            }
        }
        workbook.close();
        throw new IllegalArgumentException();


    }

    /**
     * Znajduje kolumnę w której znajdują sie dane konkretnego prowadzącego
     * @param fileName plik, w którym szukamy
     * @param lastName nazwisko prowadzącego
     * @param firstName imię prowadzącego
     * @return numer kolumny
     * @throws IOException jeżeli plik w którym szukamy lub trzymamy dane o formacie nie istnieje
     * @throws InvalidFormatException jeżeli błędny format pliku
     * @throws IndexOutOfBoundsException jeżeli plik z formatem jest pusty
     */
    public static int findLecturerColumn(String fileName, String lastName, String firstName) throws IOException, InvalidFormatException, IndexOutOfBoundsException {
        DataPlace dataPlace = FilesController.readDataInfo(fileName);
        Workbook workbook = WorkbookFactory.create(FilesController.getSchedule(fileName));
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();
        Row lastNameRow = sheet.getRow(dataPlace.getNazwisko()-1);
        Row firstNameRow = sheet.getRow(dataPlace.getImie()-1);
        for (Cell cell : lastNameRow) {
            String lastValue = dataFormatter.formatCellValue(cell).trim();
            if (lastValue.equalsIgnoreCase(lastName)) {
                String firstValue = dataFormatter.formatCellValue(firstNameRow.getCell(cell.getColumnIndex())).trim();
                if (firstValue.equalsIgnoreCase(firstName)) {
                    workbook.close();
                    return cell.getColumnIndex();
                }
            }
        }
        workbook.close();
        throw new IllegalArgumentException();
    }

    /**
     *
     * @param fleName plik dla którego przygotowywane są dane
     * @param colNumber numer kolumny z której czytamy dane
     * @param my_data dane które chcemy czytać
     * @return zwraca dane w postaci JSON
     * @throws IOException jeżeli problem z pliami
     * @throws InvalidFormatException jeżeli błędny format pliku
     * @throws IllegalArgumentException jeżeli błędny numer kolumny
     */
    public static JSONObject prepareData(String fleName, int colNumber, String[] my_data) throws IOException, InvalidFormatException, IllegalArgumentException {
        JSONObject json = new JSONObject();
        DataPlace dataPlace = FilesController.readDataInfo(fleName);
        Workbook workbook = WorkbookFactory.create(FilesController.getSchedule(fleName));
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();
        int start = dataPlace.getPodsumowanie_od()-1;
        for (String tmp:my_data) {
            if(tmp.equalsIgnoreCase("przedmioty")){
                JSONObject subjects = new JSONObject();
                Cell cell;
                for(int i = dataPlace.getPrzedmioty()-1; i <= sheet.getLastRowNum(); i++){
                    try{
                        cell = sheet.getRow(i).getCell(colNumber);
                        if(cell.getNumericCellValue() != 0){
                            String subjectName = dataFormatter.formatCellValue(sheet.getRow(i).getCell(0)) + " - " + dataFormatter.formatCellValue(sheet.getRow(i).getCell(8))
                                    + " - " + dataFormatter.formatCellValue(sheet.getRow(i).getCell(3)) + "/" + dataFormatter.formatCellValue(sheet.getRow(i).getCell(4));
                            subjects.put(subjectName,cell.getNumericCellValue());
                        }
                    }catch(NullPointerException e){
                        //continue;
                    }
                }
                json.put("przedmioty", subjects);
            } else{
                while(!tmp.equalsIgnoreCase(dataFormatter.formatCellValue(sheet.getRow(start).getCell(0)))){
                    start++;
                }

                Cell cell = sheet.getRow(start).getCell(colNumber);
                if(tmp.contains("%")){
                    //System.out.println(tmp);
                    json.put(tmp, round(cell.getNumericCellValue(),5));//zmienilem z powrotem
                    //json.put(tmp, round(cell.getNumericCellValue()*100,2));
                }else{
                    json.put(tmp, cell.getNumericCellValue());
                }
                // json.put(tmp, cell.getNumericCellValue());
                start = dataPlace.getPrzedmioty_od()-1;
            }
        }
        workbook.close();
        return json;
    }

    /**
     *
     * @param fileName plik z którtego chcemy przeczytać informację o danych
     * @return zwraca ifnormację o dostępnych danych
     * @throws IOException jeżeli problem z plikiem
     * @throws InvalidFormatException jeżeli problem z formatem Excel
     */
    public static String[] findData(String fileName) throws IOException, InvalidFormatException{
        List<String> place = new ArrayList<>();
        DataPlace dataPlace = FilesController.readDataInfo(fileName);
        Workbook workbook = WorkbookFactory.create(FilesController.getSchedule(fileName));
        int start = dataPlace.getPodsumowanie_od()-1;
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();
        for (int i = start; i <= dataPlace.getPodsumowanie_do()-1; i++) {
            Cell cell = sheet.getRow(start++).getCell(0);
            place.add(dataFormatter.formatCellValue(cell).trim());
        }
        place.add("PRZEDMIOTY");
        workbook.close();
        return place.toArray(new String[0]);
    }


















    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
