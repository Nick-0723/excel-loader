package com.hjonline.loader;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class LoadModuleEntity {
    public static void main(String[] args) throws Exception {
        String classPrefix = "package com.hejin.etl.hbase.newentity;\n" +
                "\n" +
                "\n" +
                "import com.hejin.etl.hbase.annotation.EnumStoreType;\n" +
                "import com.hejin.etl.hbase.annotation.HbaseColumn;\n" +
                "import com.hejin.etl.hbase.annotation.HbaseTable;\n" +
                "import lombok.Data;\n" +
                "import lombok.extern.slf4j.Slf4j;\n" +
                "\n" +
                "import java.io.Serializable;\n" +
                "\n" +
                "\n" +
                "\n" +
                "@Data\n" +
                "@Slf4j\n" +
                "@HbaseTable(tableName = \"ecifdb:${Table}\")\n" +
                "public class ${Table}Entity implements Serializable {\n";
        StringBuffer text = new StringBuffer();
        FileInputStream is = new FileInputStream("D:\\xinye\\ecif-docs\\03详细设计\\二期设计文档\\兴业证券主干表设计文档V1.2.xlsx");
        XSSFWorkbook wb = new XSSFWorkbook(is);
        XSSFSheet sheet0 = wb.getSheetAt(1);
        System.out.println(sheet0.getSheetName());
        XSSFCell previousTableName = null;
        String tableName = "";
        for (Row aSheet1 : sheet0){
            XSSFRow row = (XSSFRow) aSheet1;
            XSSFCell cell1 = row.getCell(1);
            if (previousTableName!=null&&(cell1==null||cell1.getStringCellValue().equals(""))){
                text.append("}");
                buildClassFiles(tableName,text);
            }
            if (cell1 == null || cell1.getStringCellValue().equals("TABLE_NAME")){
                previousTableName = null;
                continue;
            }
            if (row.getCell(1).getCellStyle().getFont().getStrikeout()){
                continue;
            }
            if (previousTableName == null || previousTableName.getStringCellValue().equals("")||!previousTableName.getStringCellValue().equals(tableName)) {
                tableName = cell1.getStringCellValue();
                text = new StringBuffer();
                text.append(classPrefix.replace("${Table}",tableName));
            }
            previousTableName = cell1;
            XSSFCell cell4 = row.getCell(4);
            if (cell4==null||cell4.getStringCellValue().equals("")){
                continue;
            }
            String fieldName = cell4.getStringCellValue();
            String attrName = up(fieldName);
            text.append("    @HbaseColumn(qualifier = \"").append(fieldName).append("\", type = EnumStoreType.EST_STRING)\n");
            text.append("    private String ").append(attrName).append(";\n\n");
            text.append("    public String get").append(firstCharUpperCase(attrName)).append("() {\n");
            text.append("        return ").append(attrName).append(";\n    }\n\n");
            text.append("    public void set").append(firstCharUpperCase(attrName)).append("(String ").append(attrName).append(") {\n");
            text.append("        this.").append(attrName).append(" = ").append(attrName).append(";\n    }\n\n");
        }
    }

    private static void buildClassFiles(String tableName, StringBuffer text) {
        String path = "D:\\xinye\\ecif-task-frame\\ecif-task-datasource\\src\\main\\java\\com\\hejin\\etl\\hbase\\newentity";
        try (OutputStream out = new FileOutputStream(path+"\\"+tableName+"Entity.java")) {
            out.write(text.toString().getBytes());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static String up(String str) {
        int _index = -1;
        if ((_index = str.indexOf("_")) != -1) {
            char[] chars = str.toCharArray();
            char[] new_chars = new char[chars.length - 1];
            System.arraycopy(chars, _index + 1, chars, _index, chars.length - 1 - _index);
            System.arraycopy(chars, 0, new_chars, 0, new_chars.length);
            new_chars[_index] = Character.toUpperCase(new_chars[_index]);
            return up(new String(new_chars));
        }
        return str;
    }

    private static String firstCharUpperCase(String str){
        char[] chars = str.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }



}
