package com.hjonline.loader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class LoadModuleSql {
    public static void main(String[] args) throws Exception {
        String excelPath = "";
        String sqlFilePath = "";
        int index = 0;
        if (args.length != 3) {
            System.out.println("请正确输入参数，第一个是excel路径，第二个是生成sql文件路径，第三个是excel中第几个sheet");
            return;
        } else {
            excelPath = args[0].replace("\\", "\\\\");
            sqlFilePath = args[1].replace("\\", "\\\\");
            index = Integer.parseInt(args[2]);
        }

        LoadModule loadModule = new LoadModule();
        LinkedHashMap<String, LinkedList<String>> tables = loadModule.load(excelPath, index);


        StringBuffer sql = new StringBuffer();
        tables.forEach((k,v) -> {
            System.out.println("table===="+k);
            sql.append("drop table if exists ecifdb.").append(k).append(";\n");
            sql.append("create external table ecifdb.").append(k).append("(\n");
            v.forEach(f -> {
                sql.append(f).append(" ").append("string").append(",\n");
                System.out.print(f+"==");
            });
            sql.deleteCharAt(sql.length()-2);
            sql.append(")\n");
            sql.append("stored by 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'\n");
            sql.append("WITH SERDEPROPERTIES (\"hbase.columns.mapping\" = \":key,\n");
            v.forEach(f->{
                sql.append("f:").append(f).append(",\n");
            });
            sql.deleteCharAt(sql.length()-2);
            sql.append("\")\n");
            sql.append("TBLPROPERTIES(\"hbase.table.name\"=\"").append("ecifdb:").append(k).append("\");\n\n\n");

        });


        saveAsSqlFile(sqlFilePath,sql);
    }

    private static void saveAsSqlFile(String file, StringBuffer sql){
        try (OutputStream out = new FileOutputStream(file)) {
            out.write(sql.toString().getBytes());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
