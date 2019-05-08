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
        LinkedHashMap<String, LinkedList<Field>> load = loadModule.load("D:\\xinye\\svndoc\\4_项目实施\\2、详细设计\\2、二阶段设计文档\\兴业证券ECIF物理模型V2.3.xlsx", 2);
        load.forEach((k,v)->{
            System.out.println("\""+k+"\": [");
            v.forEach(f -> System.out.println("\""+f.getFieldName()+"\""));
            System.out.println("]");
        });
    }


    public LinkedHashMap<String, LinkedList<Field>> load(String inputFile, int index){
        XSSFWorkbook wb;
        LinkedHashMap<String, LinkedList<Field>> tables = null;
        try (FileInputStream is = new FileInputStream(inputFile)) {
            wb = new XSSFWorkbook(is);
            XSSFSheet sheet0 = wb.getSheetAt(index);
            tables = new LinkedHashMap<>();

            for (Row aSheet1 : sheet0) {
                XSSFRow row = (XSSFRow) aSheet1;
                XSSFCell cell1 = row.getCell(1);
                XSSFCell cell4 = row.getCell(4);
                XSSFCell cell5 = row.getCell(5);
                XSSFCell cell3 = row.getCell(3);
                if (cell1 == null || row.getCell(1).getCellStyle().getFont().getStrikeout() || cell1.getStringCellValue().equals("TABLE_NAME") || cell1.getStringCellValue().equals("")) {
                    continue;
                }
                String tableName = cell1.getStringCellValue();
                String filedName = cell4.getStringCellValue();
                String filedType = cell5.getStringCellValue();
                String comment = cell3.getStringCellValue();
                if (filedType.startsWith("int")){
                    filedType="int";
                } else if(filedType.contains(".")){
                    filedType = filedType.replace(".",",") ;
                }
                if (!tables.containsKey(tableName)) {
                    LinkedList<Field> fields = new LinkedList<>();
                    fields.add(new Field(filedName,filedType, comment));
                    tables.put(tableName, fields);
                } else {
                    tables.get(tableName).add(new Field(filedName,filedType,comment));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tables;
    }
}
