package parser;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;


public class Parser {

    private static final String FILE_NAME = "src/main/resources/KIS_2017_18_plan_v.1.2.7.xlsx";
    private static Lecturer lecturer;

    public static boolean findLecturerLastname(String lastName) throws IOException, InvalidFormatException{

        Workbook workbook = WorkbookFactory.create(new File(FILE_NAME));
        int counter = 0;
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();
        Row row =  sheet.getRow(5);
        for(Cell cell : row){
            String cellValue = dataFormatter.formatCellValue(cell);
            if(cellValue.equalsIgnoreCase(lastName)) counter++;
        }
        workbook.close();

        if(counter == 1) return true;
        else return false;
    }

    public static boolean findLecturer(String lastName, String firstName) throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(new File(FILE_NAME));
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();
        Row lastRow = sheet.getRow(5);
        Row firstRow = sheet.getRow(4);
        for (Cell cell : lastRow) {
            String lastValue = dataFormatter.formatCellValue(cell);
            if (lastValue.equalsIgnoreCase(lastName)) {
                String firstValue = dataFormatter.formatCellValue(firstRow.getCell(cell.getColumnIndex()));
                if (firstName.equalsIgnoreCase(firstName)) {
                    workbook.close();
                    return true;
                }
            }

        }
        workbook.close();
        return false;
    }
}
