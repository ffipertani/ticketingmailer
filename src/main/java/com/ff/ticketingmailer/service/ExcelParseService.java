package com.ff.ticketingmailer.service;

import com.ff.ticketingmailer.model.MaintenanceRemainder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class ExcelParseService {

    public List<MaintenanceRemainder> parse(InputStream excelFile){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        List<MaintenanceRemainder> result = new ArrayList<>();
        try {
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();
            iterator.next();
            iterator.next();
            while (iterator.hasNext()) {

                Row currentRow = iterator.next();
                Iterator<Cell> cellIterator = currentRow.iterator();
                if(!cellIterator.hasNext()){
                    break;
                }
                MaintenanceRemainder remainder = new MaintenanceRemainder();
                remainder.setId(cellIterator.next().getStringCellValue());
                remainder.setMarca(cellIterator.next().getStringCellValue());
                remainder.setModello(cellIterator.next().getStringCellValue());
                remainder.setManutenzione(cellIterator.next().getStringCellValue());
                remainder.setDataInstallazione( cellIterator.next().getDateCellValue());
                //remainder.setDataInstallazione(sdf.parse(cellIterator.next().getNumericCellValue()+""));

                result.add(remainder);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream("C:\\Users\\Francesco\\Downloads\\Ticketing.xlsx");
        ExcelParseService excelParseService = new ExcelParseService();
        excelParseService.parse(fis);
    }
}
