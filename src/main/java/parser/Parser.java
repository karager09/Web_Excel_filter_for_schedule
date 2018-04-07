package parser;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;


public class Parser {

    private static final String FILE_NAME = "src/main/resources/rozklad.xlsx";
    private static Lecturer lecturer;
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
}
