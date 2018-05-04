package parser;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONObject;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {


    public static boolean findLecturer(String lastName) throws IOException, InvalidFormatException{
        DataPlace dataPlace = FilesController.readDataInfo();
        Workbook workbook = WorkbookFactory.create(FilesController.getSchedule());
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
        return counter == 1;
//        if(counter == 1) return true;
//        else return false;
    }

    public static boolean findLecturer(String lastName, String firstName) throws IOException, InvalidFormatException {
        DataPlace dataPlace = FilesController.readDataInfo();
        Workbook workbook = WorkbookFactory.create(FilesController.getSchedule());
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
                    return true;
                }
            }

        }
        workbook.close();
        return false;
    }


    public static String[] findData() throws IOException, InvalidFormatException{
        List<String> place = new ArrayList<>();

        DataPlace dataPlace = FilesController.readDataInfo();
        Workbook workbook = WorkbookFactory.create(FilesController.getSchedule());
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


    public static int findLecturerColumn(String lastName) throws IOException, InvalidFormatException {
        DataPlace dataPlace = FilesController.readDataInfo();
        Workbook workbook = WorkbookFactory.create(FilesController.getSchedule());
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
        return 0;
    }

    public static int findLecturerColumn(String lastName, String firstName) throws IOException, InvalidFormatException {

        DataPlace dataPlace = FilesController.readDataInfo();
        Workbook workbook = WorkbookFactory.create(FilesController.getSchedule());
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
        return 0;
    }
    public static JSONObject  prepareData(int colNumber, String[] my_data) throws IOException, InvalidFormatException {
        JSONObject json = new JSONObject();
        DataPlace dataPlace = FilesController.readDataInfo();
        Workbook workbook = WorkbookFactory.create(FilesController.getSchedule());
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
                            String subjectName = dataFormatter.formatCellValue(sheet.getRow(i).getCell(0)) + " - " + dataFormatter.formatCellValue(sheet.getRow(i).getCell(8));
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
                json.put(tmp, cell.getNumericCellValue());
                start = dataPlace.getPrzedmioty_od()-1;
            }
        }
        workbook.close();
        return json;
    }
}
