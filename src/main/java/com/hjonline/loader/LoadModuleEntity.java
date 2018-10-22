package com.hjonline.loader;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;

public class LoadModuleEntity {
    public static void main(String[] args) throws Exception {
        String classPrefix = "package com.hejin.etl.hbase.entity;\n" +
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
                "@HbaseTable(tableName = \"ecif:${Table}\")\n" +
                "public class ${Table}Entity implements Serializable {";
        String text = "";
        FileInputStream is = new FileInputStream("C:\\Users\\Administrator\\Desktop\\兴业证券主干表设计文档V1.2(1).xlsx");
        XSSFWorkbook wb = new XSSFWorkbook(is);
        XSSFSheet sheet0 = wb.getSheetAt(1);
        System.out.println(sheet0.getSheetName());
        for (Iterator rowIterator = sheet0.iterator(); rowIterator.hasNext(); ) {
            XSSFRow row = (XSSFRow) rowIterator.next();
            short lastCellNum = row.getLastCellNum();
            boolean isDelete = row.getCell(1).getCellStyle().getFont().getStrikeout();
            XSSFCell nameCnCell = row.getCell(0);
            XSSFCell nameEnCell = row.getCell(1);
            if (nameCnCell!=null&&!nameCnCell.getStringCellValue().equals("")){
                text = classPrefix.replace("${Table}",row.getCell(1).getStringCellValue());
//                text = @HbaseColumn(qualifier = "
            }else if (nameEnCell!=null&&!nameEnCell.getStringCellValue().equals("")){
                text+="}";
                continue;
            }
            text += "\n";
        }
    }

}
