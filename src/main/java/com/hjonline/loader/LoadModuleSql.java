package com.hjonline.loader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class LoadModuleSql {
    public static void main(String[] args) throws Exception {
        args = new String[]{"D:\\微信文件\\WeChat Files\\Charles-song-323\\Files\\兴业证券主干表设计文档修改版V1.0.xlsx",
                "D:\\xinye\\ecif-task-frame\\ecif-task-datasource\\src\\main\\java\\com\\hejin\\etl\\hbase\\entity","1"};
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
        LinkedHashMap<String, LinkedList<Field>> tables = loadModule.load(excelPath, index);


        StringBuffer sql = new StringBuffer();
        createExtSql(tables,sql);
        System.out.println(sql);
//        saveAsSqlFile(sqlFilePath,sql);
    }

    private static void createExtSql(LinkedHashMap<String, LinkedList<Field>> tables, StringBuffer sql){
        String database = "eciftest";
        tables.forEach((k,v) -> {
            sql.append("drop table if exists ").append(database).append(".").append(k).append(";\n");
            sql.append("create external table ").append(database).append(".").append(k).append("(\n");
            v.forEach(f -> {
                sql.append(f.getFieldName()).append(" ").append(f.getFieldType()).append(",\n");
            });
            sql.deleteCharAt(sql.length()-2);
            sql.append(")\n");
            sql.append("stored by 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'\n");
            sql.append("WITH SERDEPROPERTIES (\"hbase.columns.mapping\" = \":key,\n");
            sql.append("f:").append("rowKeyValue").append(",\n");
            v.forEach(f->{
                if (!(f.getFieldName().equals("pbk_id")||f.getFieldName().equals("bk_id"))){
                    sql.append("f:").append(f.getFieldName()).append(",\n");
                }
            });
            sql.deleteCharAt(sql.length()-2);
            sql.append("\")\n");
            sql.append("TBLPROPERTIES(\"hbase.table.name\"=\"").append(database).append(":").append(k).append("\");\n\n\n");
        });
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
