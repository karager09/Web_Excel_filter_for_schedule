package parser;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class Parser {

    private static final String FILE_NAME = "src/main/resources/rozklad.xlsx";
    private static PlaceOfData data = null;

    public static PlaceOfData getData() {
        return data;
    }

    public static void setData(PlaceOfData data) {
        Parser.data = data;
    }

    public static boolean findLecturerLastname(String lastName) throws IOException, InvalidFormatException{

        Workbook workbook = WorkbookFactory.create(new File(FILE_NAME));
        int counter = 0;
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();
        Row row =  sheet.getRow(data.getNazwisko()-1);
        for(Cell cell : row){
            String cellValue = new String(dataFormatter.formatCellValue(cell)).trim();
            //System.out.println(cellValue);
            if(cellValue.equalsIgnoreCase(lastName)) counter++;
        }
        workbook.close();
        //System.out.println(counter);
        if(counter == 1) return true;
        else return false;
    }

    public static boolean findLecturer(String lastName, String firstName) throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(new File(FILE_NAME));
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();
        Row lastRow = sheet.getRow(data.getNazwisko()-1);
        Row firstRow = sheet.getRow(data.getImie()-1);
        for (Cell cell : lastRow) {
            String lastValue = new String(dataFormatter.formatCellValue(cell)).trim();
            if (lastValue.equalsIgnoreCase(lastName)) {
                String firstValue = new String(dataFormatter.formatCellValue(firstRow.getCell(cell.getColumnIndex()))).trim();
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
        String[] place = new String[data.getPodsumowanie_do()-data.getPodsumowanie_od()+1];
        int start = data.getPodsumowanie_od()-1;
        Workbook workbook = WorkbookFactory.create(new File(FILE_NAME));
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();
        //Row row = null;
        for (int i = 0; i < place.length-1; i++) {
            Cell cell = sheet.getRow(start++).getCell(0);
            place[i] = new String(dataFormatter.formatCellValue(cell)).trim();
        }
        place[place.length-1] = "PRZEDMIOTY";
        workbook.close();
        //System.out.println(place.length);
        return place;
    }


    public static int findLecturerColumn(String lastName) throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(new File(FILE_NAME));
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();
        Row row =  sheet.getRow(data.getNazwisko()-1);
        for(Cell cell : row){
            String cellValue = new String(dataFormatter.formatCellValue(cell)).trim();
            //System.out.println(cellValue);
            if((cellValue.trim()).equalsIgnoreCase(lastName)){
                //System.out.println(lastName);
                return cell.getColumnIndex();
            }
        }
        workbook.close();
        return 0;
    }

    public static int findLecturerColumn(String lastName, String firstName) throws IOException, InvalidFormatException {
        //System.out.println(lastName+" "+firstName);
        Workbook workbook = WorkbookFactory.create(new File(FILE_NAME));
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();
        Row lastRow = sheet.getRow(data.getNazwisko()-1);
        Row firstRow = sheet.getRow(data.getImie()-1);
        for (Cell cell : lastRow) {
            String lastValue = new String(dataFormatter.formatCellValue(cell)).trim();
            if (lastValue.equalsIgnoreCase(lastName)) {
                String firstValue = new String(dataFormatter.formatCellValue(firstRow.getCell(cell.getColumnIndex()))).trim();
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
        Workbook workbook = WorkbookFactory.create(new File(FILE_NAME));
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();
        int start = data.getPodsumowanie_od()-1;
        for (String tmp:my_data) {
            if(tmp.equalsIgnoreCase("przedmioty")){
                JSONObject subjects = new JSONObject();
                Cell cell = null;
                for(int i = data.getPrzedmioty(); i <= sheet.getLastRowNum(); i++){
                    try{
                        cell = sheet.getRow(i).getCell(colNumber);
                        if(cell.getNumericCellValue() != 0){
                            String subjectName = new String(dataFormatter.formatCellValue(sheet.getRow(i).getCell(0)) + " - " +dataFormatter.formatCellValue(sheet.getRow(i).getCell(8)));
                            subjects.put(subjectName,cell.getNumericCellValue());
                        }
                    }catch(NullPointerException e){
                        continue;
                    }
                }
                json.put("przedmioty", subjects);
            } else{
                while(!tmp.equalsIgnoreCase(dataFormatter.formatCellValue(sheet.getRow(start).getCell(0)))){
                    start++;
                }

                Cell cell = sheet.getRow(start).getCell(colNumber);
//                if(cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
//                    switch(cell.getCachedFormulaResultType()) {
//                        case Cell.CELL_TYPE_NUMERIC:
//                            json.put(tmp, cell.getNumericCellValue());
//                            break;
//                        case Cell.CELL_TYPE_STRING:
//
//                            json.put(tmp, cell.getRichStringCellValue());
//                            break;
//                    }
//                }else{
                //System.out.println(colNumber);
                //System.out.println();
                json.put(tmp, cell.getNumericCellValue());
                start = data.getPodsumowanie_od()-1;
                //}

            }
        }
        workbook.close();
        return json;
    }
}
