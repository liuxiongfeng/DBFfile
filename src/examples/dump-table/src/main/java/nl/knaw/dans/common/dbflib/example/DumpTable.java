/**
 * Copyright (C) 2009-2016 DANS - Data Archiving and  Networked Services (info@dans.knaw.nl)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.knaw.dans.common.dbflib.example;


import nl.knaw.dans.common.dbflib.*;
import nl.knaw.dans.common.dbflib.example.utils.UnitTestUtil;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

/*
 * Usage: java -cp dans-dbf-lib-version.jar:dump-table.jar nl.knaw.dans.common.example.DumpTable <table file>
 */
public class DumpTable {
    public static void main(String[] args) throws IOException, CorruptedTableException, InvalidFieldLengthException, InvalidFieldTypeException, InterruptedException {
        String temp = "C:/liuxiongfeng/中登软件/testdbf/0U071906287500000076.dbf";//模板文件
        String prePath = "C:/liuxiongfeng/中登软件/testdbf/";
        String createFileName = "7500000103";
        createNewDbfFile(prePath,createFileName);
        write(prePath,createFileName);
        Thread.currentThread().sleep(3000);
        getZDQueryResult(prePath,createFileName);
    }

    //写从文件和主文件
    public static void write(String prePath,String createFileName) {
        if (prePath == null || "".equals(prePath)) {
            System.exit(1);
        }
        //选中dbf主文件
        Table table = new Table(new File(prePath + "req.dbf"));
        try {
            table.open(IfNonExistent.ERROR);
            table.addRecord(createFileName, "CSDCC", "UAPSRV", "07", "1", createFileName + ".dbf", "",
                    "20190702", "110616", "", "0", "1100004709980867", "", "####");
           /* table.addRecord(createFileName.substring(createFileName.length() -10 ,createFileName.length()), "CSDCC", "UAPSRV", "07", "1", createFileName + ".dbf", "",
                    "20190702", "110616", "", "0", "1100004709980867", "", "####");*/
            System.out.println("-------写入qingqiu zhuwenjian 成功-------");
        } catch (IOException ioe) {
            System.out.println("Trouble reading table or table not found");
            ioe.printStackTrace();
        } catch (DbfLibException dbflibException) {
            System.out.println("Problem getting raw value");
            dbflibException.printStackTrace();
        } finally {
            try {
                table.close();
            } catch (IOException ex) {
                System.out.println("Unable to close the table");
            }
        }
    }

    //输出表字段信息
    public static Table readTableFiles(String pathAndDbf) {
        if (pathAndDbf == null || "".equals(pathAndDbf)) {
            System.exit(1);
        }
        //选中dbf文件
        final Table table = new Table(new File(pathAndDbf));
        try {
            table.open(IfNonExistent.ERROR);
            final Format dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            System.out.println("TABLE PROPERTIES");
            System.out.println("Name          : " + table.getName());
            System.out.println("version          : " + table.getVersion());
            System.out.println("Last Modified : " + dateFormat.format(table.getLastModifiedDate()));
            System.out.println("--------------");

            System.out.println("FIELDS (COLUMNS)");
            System.out.println("------表字段信息输出开始--------");
            int i = 0;
            final List<Field> fields = table.getFields();
            //查询并打印字段信息
            for (final Field field : fields) {
                i++;
                System.out.println("  Name       : " + field.getName());
                System.out.println("  Type       : " + field.getType());
                System.out.println("  Length     : " + field.getLength());
                System.out.println("  Dec. Count : " + field.getDecimalCount());
                System.out.println();
            }
            System.out.println("------表字段信息输出完成,共"+i+"个字段--------");
        } catch (CorruptedTableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return table;
    }

    //遍历表中记录
    public static Map<String, String> iterateTableData(String pathAndFile) throws IOException, CorruptedTableException {
        Map<String, String> map = new HashMap<String, String>();
        if (pathAndFile == null) {
            System.exit(1);
        }
        Table table = new Table(new File(pathAndFile));
        table.open(IfNonExistent.ERROR);
        final List<Field> fields = table.getFields();
        //遍历表中记录
        final Iterator<Record> recordIterator = table.recordIterator();
        int count = 0;

        while (recordIterator.hasNext()) {
            final Record record = recordIterator.next();
            System.out.println(count++);

            for (final Field field : fields) {
                try {
                    //获取每一条记录
                    byte[] rawValue = record.getRawValue(field);
                    System.out.println(field.getName() + " : " + (rawValue == null ? "<NULL>" : new String(rawValue,"GBK")));
                    map.put(field.getName(),rawValue == null ? "<NULL>" : new String(rawValue,"GBK"));
                } catch (ValueTooLargeException vtle) {
                    System.out.println("zhi tai chang");
                    // Cannot happen :)
                } catch (DbfLibException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }
    //获取中登查询结果
    public static Map<String,String> getZDQueryResult(String prePath, String sbbh) throws IOException, CorruptedTableException {
        Map<String, String> zd = new HashMap<String, String>();
        if (prePath == null || sbbh == null) {
            System.exit(1);
        }
        final Table table = new Table(new File(prePath + "rep.dbf"));
        table.open(IfNonExistent.ERROR);
        //遍历表中记录
        final Iterator<Record> recordIterator = table.recordIterator();
        while (recordIterator.hasNext()) {
            final Record record = recordIterator.next();
            //qu dangqian zhetiao jilu de sbbh
            String sbbhValue = record.getStringValue("SBBH");
            try {
                //获取到sbbh进行比较，是否是对应的结果
                if (sbbhValue.equals(sbbh)) {
                    String zwjm = record.getStringValue("ZWJM");
                    if (zwjm == null || "".equals(zwjm.trim())){
                        for (Field field : table.getFields()){
                            if (field.getName().trim().equals("CLSM")){
                                byte[] rawValue = record.getRawValue(field);
                                String clsm = rawValue == null ? "<NULL>" : new String(rawValue,"GBK");
                                zd.put("结果：",clsm);
                                System.out.println("结果是：" + clsm);
                            }
                        }
                        return zd;
                    }else {
                        zd = iterateTableData(prePath + zwjm);
                        return zd;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CorruptedTableException e) {
                e.printStackTrace();
            } catch (DbfLibException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void createNewDbfFile(String prePth,String createTableName) throws InvalidFieldLengthException, InvalidFieldTypeException, IOException, CorruptedTableException {
        List<Field> fields = new ArrayList<Field>();
        fields.add(new Field("YWLSH", Type.CHARACTER, 10));
        fields.add(new Field("KHMC", Type.CHARACTER, 120));
        fields.add(new Field("ZJLB",Type.CHARACTER,2));
        fields.add(new Field("ZJDM", Type.CHARACTER,40));
        fields.add(new Field("YMTH", Type.CHARACTER,20));
        fields.add(new Field("ZHLB", Type.CHARACTER,2));
        fields.add(new Field("ZQZH", Type.CHARACTER,20));
        fields.add(new Field("KHJGDM", Type.CHARACTER,6));
        fields.add(new Field("KHWDDM", Type.CHARACTER,10));
        fields.add(new Field("SQRQ", Type.CHARACTER,8));
        File newFile = new File(prePth, createTableName + ".dbf");
        Table newTable = null;
        try {
            newTable = new Table(newFile, Version.DBASE_5, fields,"GBK");
            newTable.open(IfNonExistent.CREATE);
            newTable.addRecord(createTableName,"孙淑范","01","230703281220012","",
                    "", "","100087", "1000871653","20190702");
            System.out.println("chuangjian qingqiu ziwenjian chenggong");
        } catch (CorruptedTableException e) {
            e.printStackTrace();
        } catch (DbfLibException e) {

        } finally {
            newTable.close();
        }
    }
    //创建一个dbf文件 prePath为创建的文件所在的文件夹路径;返回新文件的路径
    //创建的dbf文件要确定下是否为空的
    public static String createCopyDbfFile(String demoFile, String prePath, String createFileName, Version version) throws IOException {
        if (prePath == null || "".equals(prePath)) {
            System.exit(2);
        }
        //readAndIterator(pathAndDemoDbf);
        try {
            //dbf文件不存在则新建一个
            //table.open(IfNonExistent.CREATE);
            // ... do your stuff 新建一个然后呢？怎么定义字段--复制字段
            String newFile = UnitTestUtil.doCopy(demoFile, prePath, createFileName, version);
            //选中dbf主文件
            Table table = new Table(new File(newFile));
            try {
                table.open(IfNonExistent.ERROR);
                table.addRecord(createFileName.substring(createFileName.length()-10,createFileName.length()), "", "", "", "", "11", "A201905051",
                        "100087", "1000871654", "20190701");
                System.out.println("-------写请求子文件成功-------");
            } catch (IOException ioe) {
                System.out.println("Trouble reading table or table not found");
                ioe.printStackTrace();
            } catch (DbfLibException dbflibException) {
                System.out.println("Problem getting raw value");
                dbflibException.printStackTrace();
            } finally {
                try {
                    table.close();
                } catch (IOException ex) {
                    System.out.println("Unable to close the table");
                }
            }
            return newFile;
        } catch (CorruptedTableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DbfLibException e) {
            e.printStackTrace();
        }
        return null;
    }
}