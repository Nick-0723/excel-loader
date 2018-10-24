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

    public static void main(String[] args) {
        LoadModule loadModule = new LoadModule();
        LinkedHashMap<String, LinkedList<String>> load = loadModule.load("D:\\xinye\\ecif-docs\\03详细设计\\二期设计文档\\兴业证券主干表设计文档V1.3.xlsx", 0);
        load.keySet().forEach(t->{
            System.out.println("truncate 'ecifdb:"+t+"'\n");
        });
        LinkedHashMap<String, LinkedList<String>> load1 = loadModule.load("D:\\xinye\\ecif-docs\\03详细设计\\二期设计文档\\兴业证券主干表设计文档V1.3.xlsx", 1);
        load1.keySet().forEach(t->{
            System.out.println("truncate 'ecifdb:"+t+"'\n");
        });
    }


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
