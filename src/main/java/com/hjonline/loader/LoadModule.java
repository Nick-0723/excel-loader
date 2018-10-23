package com.hjonline.loader;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * @author nick
 * @date 2018/10/23
 */
public class LoadModule {


    public LinkedHashMap<String, LinkedList<String>> load(String inputFile, int index){
        XSSFWorkbook wb;
        LinkedHashMap<String, LinkedList<String>> tables = null;
        try (FileInputStream is = new FileInputStream(inputFile)) {
            wb = new XSSFWorkbook(is);
            XSSFSheet sheet0 = wb.getSheetAt(index);
            tables = new LinkedHashMap<>();

            for (Row aSheet1 : sheet0) {
                XSSFRow row = (XSSFRow) aSheet1;
                XSSFCell cell1 = row.getCell(1);
                XSSFCell cell4 = row.getCell(4);
                if (cell1 == null || row.getCell(1).getCellStyle().getFont().getStrikeout() || cell1.getStringCellValue().equals("TABLE_NAME") || cell1.getStringCellValue().equals("")) {
                    continue;
                }
                String tableName = cell1.getStringCellValue();
                String filedName = cell4.getStringCellValue();
                if (!tables.containsKey(tableName)) {
                    LinkedList<String> fields = new LinkedList<>();
                    fields.add(filedName);
                    tables.put(tableName, fields);
                } else {
                    tables.get(tableName).add(filedName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tables;
    }
}
