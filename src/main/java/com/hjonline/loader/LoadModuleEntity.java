package com.hjonline.loader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class LoadModuleEntity {
    private static String excelPath = "";
    private static String javaBeanPath = "";

    public static void main(String[] args) throws Exception {
        int index = 0;
        if (args.length != 3) {
            System.out.println("请正确输入参数，第一个是excel路径，第二个是生成javabean路径，第三个是excel中第几个sheet");
            return;
        } else {
            excelPath = args[0].replace("\\", "\\\\");
            javaBeanPath = args[1].replace("\\", "\\\\");
            index = Integer.parseInt(args[2]);
        }

        LoadModule loadModule = new LoadModule();
        LinkedHashMap<String, LinkedList<Field>> tables = loadModule.load(excelPath, index);

        tables.forEach(LoadModuleEntity::buildClassFiles);
    }

    private static void buildClassFiles(String tableName, LinkedList<Field> fields) {
        StringBuffer text = new StringBuffer();

        String classPrefix = "package com.hejin.etl.hbase.newentity;\n" +
                "\n" +
                "\n" +
                "import com.hejin.etl.hbase.annotation.EnumStoreType;\n" +
                "import com.hejin.etl.hbase.annotation.HbaseColumn;\n" +
                "import com.hejin.etl.hbase.annotation.HbaseTable;\n" +
                "import com.fasterxml.jackson.annotation.JsonInclude;\n" +
                "import lombok.Data;\n" +
                "import lombok.extern.slf4j.Slf4j;\n" +
                "\n" +
                "import java.io.Serializable;\n" +
                "\n" +
                "\n" +
                "\n" +
                "@Data\n" +
                "@Slf4j\n" +
                "@JsonInclude(JsonInclude.Include.NON_NULL)\n" +
                "@HbaseTable(tableName = \"ecifdb:${Table}\")\n" +
                "public class ${Table}Entity implements Serializable {\n";
        text.append(classPrefix.replace("${Table}", tableName));
        fields.forEach(field -> {
            String attrName = up(field.getFieldName().equals("pbkid")?"rowkey":field.getFieldName());

            text.append("    @HbaseColumn(qualifier = \"").append(field.getFieldName()).append("\", type = EnumStoreType.EST_STRING)\n");
            text.append("    private String ").append(attrName).append(";\n\n");
        });
//        fields.forEach(fieldName -> {
//            String attrName = up(fieldName);
//            text.append("    public String get").append(firstCharUpperCase(attrName)).append("() {\n");
//            text.append("        return ").append(attrName).append(";\n    }\n\n");
//            text.append("    public void set").append(firstCharUpperCase(attrName)).append("(String ").append(attrName).append(") {\n");
//            text.append("        this.").append(attrName).append(" = ").append(attrName).append(";\n    }\n\n");
//        });
        text.append("}");
        try (OutputStream out = new FileOutputStream(javaBeanPath + "\\" + tableName + "Entity.java")) {
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

    private static String firstCharUpperCase(String str) {
        char[] chars = str.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }


}
