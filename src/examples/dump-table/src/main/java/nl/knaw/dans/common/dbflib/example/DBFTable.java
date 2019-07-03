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
import nl.knaw.dans.common.dbflib.example.contants.ZDContants;
import nl.knaw.dans.common.dbflib.example.vo.CurrentDateTimeVO;
import nl.knaw.dans.common.dbflib.example.vo.SFXXHCDTO;
import nl.knaw.dans.common.dbflib.example.vo.ZQZHCXDTO;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

/*
 * Usage: java -cp dans-dbf-lib-version.jar:dump-table.jar nl.knaw.dans.common.example.DumpTable <table file>
 */
public class DBFTable {
    public static void main(String[] args) throws IOException, CorruptedTableException, InvalidFieldLengthException, InvalidFieldTypeException, InterruptedException {
        String temp = "C:/liuxiongfeng/中登软件/testdbf/0U071906287500000076.dbf";//模板文件
        //String prePath = "C:/liuxiongfeng/中登软件/testdbf/";
        String prePath = "z:/zt/";
        String createTableName = "7500000124";
        /*ZQZHCXDTO ZQZHCXDTO = new ZQZHCXDTO(createTableName,"孙淑范","01","230703281220012","",
                "", "","100087", "1000871653","");
        createZQZHCXDbfFile(prePath,createTableName, ZQZHCXDTO);
        addReqRecord(prePath,createTableName,"07","1100004709980867");
        getZDQueryResult(prePath,createTableName);*/
        SFXXHCDTO sfxxhcdto = new SFXXHCDTO(createTableName,"01","01","230703281220012",
                "孙淑范","100087","1000871653","");
        createSFXXHCDbfFile(prePath,createTableName, sfxxhcdto);
        addReqRecord(prePath,createTableName,"01","1100004709980868");
        getZDQueryResult(prePath,createTableName);

    }

    //第一步 ： 创建<证券账户查询>请求子文件 success  不足部分是否要补充空格
    public static String createZQZHCXDbfFile(String prePath, String createTableName, ZQZHCXDTO zqzhcxdto) throws InvalidFieldLengthException, InvalidFieldTypeException, IOException, CorruptedTableException {

        if (!notNullAndEmpty(prePath)){
            return "prePath不能为空";
        }
        if (!notNullAndEmpty(createTableName)){
            return "createTableName不能为空";
        }
        if (!notNullAndEmpty(zqzhcxdto.getYwlsh())){
            return "ywlsh不能为空";
        }
        if (!notNullAndEmpty(zqzhcxdto.getKhjgdm())){
            return "khjgdm不能为空";
        }
        if (!notNullAndEmpty(zqzhcxdto.getKhwddm())){
            return "khwddm不能为空";
        }
        //定义dbf文件字段
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
        File newFile = new File(prePath, createTableName + ".dbf");
        Table newTable = null;
        try {
            //创建dbf表
            newTable = new Table(newFile, Version.DBASE_5, fields,"GBK");
            newTable.open(IfNonExistent.CREATE);
            //拼接入参
            String formatDate = ""; //当前时间
            if (notNullAndEmpty(zqzhcxdto.getSqrq())) {
                formatDate = zqzhcxdto.getSqrq();
            } else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYYMMdd");
                formatDate = simpleDateFormat.format(new Date());
            }
            /*newTable.addRecord(createTableName,"孙淑范","01","230703281220012","",
                    "", "","100087", "1000871653","20190702");*/
            newTable.addRecord(getColumnValue(zqzhcxdto.getYwlsh()),getColumnValue(zqzhcxdto.getKhmc()),
                    getColumnValue(zqzhcxdto.getZjlb()),getColumnValue(zqzhcxdto.getZjdm()),
                    getColumnValue(zqzhcxdto.getYmth()),getColumnValue(zqzhcxdto.getZhlb()),
                    getColumnValue(zqzhcxdto.getZqzh()),getColumnValue(zqzhcxdto.getKhjgdm()),
                    getColumnValue(zqzhcxdto.getKhwddm()),formatDate);
            System.out.println("入参为：("+ getColumnValue(zqzhcxdto.getYwlsh())+","+getColumnValue(zqzhcxdto.getKhmc())+","+
                    getColumnValue(zqzhcxdto.getZjlb())+","+getColumnValue(zqzhcxdto.getZjdm())+","+
                    getColumnValue(zqzhcxdto.getYmth())+","+getColumnValue(zqzhcxdto.getZhlb())+","+
                    getColumnValue(zqzhcxdto.getZqzh())+","+getColumnValue(zqzhcxdto.getKhjgdm())+","+
                    getColumnValue(zqzhcxdto.getKhwddm())+","+formatDate + ")");
            System.out.println("#####创建请求<证券账户查询>子文件成功####");
        } catch (CorruptedTableException e) {
            e.printStackTrace();
        } catch (DbfLibException e) {

        } finally {
            newTable.close();
        }
        return null;
    }
    //第一步 ： 创建<身份信息核查>请求子文件 success  不足部分是否要补充空格
    public static String createSFXXHCDbfFile(String prePath, String createTableName, SFXXHCDTO sfxxhcdto) throws InvalidFieldLengthException, InvalidFieldTypeException, IOException, CorruptedTableException {

        if (!notNullAndEmpty(prePath)){
            return "prePath不能为空";
        }
        if (!notNullAndEmpty(createTableName)){
            return "createTableName不能为空";
        }
        if (!notNullAndEmpty(sfxxhcdto.getYwlsh())){
            return "ywlsh不能为空";
        }
        if (!notNullAndEmpty(sfxxhcdto.getYwlb())){
            return "ywlb不能为空";
        }
        if (!notNullAndEmpty(sfxxhcdto.getZjdm())){
            return "zjlb不能为空";
        }
        if (!notNullAndEmpty(sfxxhcdto.getZjdm())){
            return "zjdm不能为空";
        }
        if (!notNullAndEmpty(sfxxhcdto.getKhmc())){
            return "khmc不能为空";
        }
        if (!notNullAndEmpty(sfxxhcdto.getKhjgdm())){
            return "khjgdm不能为空";
        }
        if (!notNullAndEmpty(sfxxhcdto.getKhwddm())){
            return "khwddm不能为空";
        }
        //定义dbf文件字段
        List<Field> fields = new ArrayList<Field>();
        fields.add(new Field("YWLSH", Type.CHARACTER, 10));
        fields.add(new Field("YWLB", Type.CHARACTER, 2));
        fields.add(new Field("ZJLB",Type.CHARACTER,2));
        fields.add(new Field("ZJDM", Type.CHARACTER,40));
        fields.add(new Field("KHMC", Type.CHARACTER, 120));
        fields.add(new Field("KHJGDM", Type.CHARACTER,6));
        fields.add(new Field("KHWDDM", Type.CHARACTER,10));
        fields.add(new Field("SQRQ", Type.CHARACTER,8));
        File newFile = new File(prePath, createTableName + ".dbf");
        Table newTable = null;
        try {
            //创建dbf表
            newTable = new Table(newFile, Version.DBASE_5, fields,"GBK");
            newTable.open(IfNonExistent.CREATE);
            /*newTable.addRecord(createTableName,"孙淑范","01","230703281220012","",
                    "", "","100087", "1000871653","20190702");*/
            newTable.addRecord(getColumnValue(sfxxhcdto.getYwlsh()),getColumnValue(sfxxhcdto.getYwlb()),
                    getColumnValue(sfxxhcdto.getZjlb()),getColumnValue(sfxxhcdto.getZjdm()),
                    getColumnValue(sfxxhcdto.getKhmc()),getColumnValue(sfxxhcdto.getKhjgdm()),
                    getColumnValue(sfxxhcdto.getKhwddm()),getDateTime().getYmd());
            System.out.println("入参为：("+ getColumnValue(sfxxhcdto.getYwlsh())+","+getColumnValue(sfxxhcdto.getYwlb())+","+
                    getColumnValue(sfxxhcdto.getZjlb())+","+getColumnValue(sfxxhcdto.getZjdm())+","+
                    getColumnValue(sfxxhcdto.getKhmc())+","+getColumnValue(sfxxhcdto.getKhjgdm())+","+
                    getColumnValue(sfxxhcdto.getKhwddm())+","+getDateTime().getYmd() + ")");
            System.out.println("#####创建请求<身份信息核查>子文件成功####");
        } catch (CorruptedTableException e) {
            e.printStackTrace();
        } catch (DbfLibException e) {

        } finally {
            newTable.close();
        }
        return null;
    }

    //第二步： 创建请求主文件
    public static String  addReqRecord(String prePath,String createTableName,String functionType,String jylsh) {
        if (!notNullAndEmpty(prePath)) {
            return "prePath不能为空";
        }
        if (!notNullAndEmpty(createTableName)) {
            return "createTableName不能为空";
        }
        if (!notNullAndEmpty(functionType)) {
            return "functionType不能为空";
        }
        //选中dbf主文件:默认主文件的位置是prePath目录下面的req.dbf文件
        Table table = new Table(new File(prePath + "req.dbf"));
        try {
            table.open(IfNonExistent.ERROR);
            //将子文件和该接口类型参数添加到请求主文件
            String serviceDomain = "";
            String serviceName = "";
            String serviceType = "";
            if (ZDContants.ZQZHCX_SERVICE_TYPE.equals(functionType)){
                serviceDomain = ZDContants.ZQZHCX_SERVICE_DOMAIN;
                serviceName = ZDContants.ZQZHCX_SERVICE_NAME;
                serviceType = ZDContants.ZQZHCX_SERVICE_TYPE;
            }else if (ZDContants.SFXXHC_SERVICE_TYPE.equals(functionType)){
                serviceDomain = ZDContants.SFXXHC_SERVICE_DOMAIN;
                serviceName = ZDContants.SFXXHC_SERVICE_NAME;
                serviceType = ZDContants.SFXXHC_SERVICE_TYPE;
            }
            //当前时间
            table.addRecord(createTableName, serviceDomain, serviceName, serviceType,
                    "1", createTableName + ".dbf", "", getDateTime().getYmd(), getDateTime().getHms(),
                    "", "0", jylsh, "", "####");
           /* table.addRecord(createFileName.substring(createFileName.length() -10 ,createFileName.length()), "CSDCC", "UAPSRV", "07", "1", createFileName + ".dbf", "",
                    "20190702", "110616", "", "0", "1100004709980867", "", "####");*/
            System.out.println("请求主文件入参为：("+ createTableName + "," + serviceDomain + "," +serviceName+ "," +
                            serviceType+ "," + "1"+ "," + createTableName + ".dbf"+ "," + "" + "," +
                            getDateTime().getYmd()+ "," + getDateTime().getHms()+ "," +
                    ""+ "," + "0"+ "," + jylsh+ "," + ""+ "," + "####)");
            System.out.println("-------写入请求主文件成功-------");
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
        return null;
    }

    //第三步 ： 获取中登查询结果
    public static List<Map<String,String>> getZDQueryResult(String prePath, String sbbh) throws IOException, CorruptedTableException, InterruptedException {
        System.out.println("开始执行获取中登查询结果");
        List<Map<String,String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> zd = new HashMap<String, String>();
        if (prePath == null || sbbh == null) {
            System.exit(1);
        }
        File file = new File(prePath + "rep.dbf");
        final Table table = new Table(file);
        try {
            table.open(IfNonExistent.ERROR);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CorruptedTableException e) {
            e.printStackTrace();
        }
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
                                list.add(zd);
                                System.out.println("结果是：" + clsm);
                            }
                        }
                        return list;
                    }else {
                        File sonFile = new File(prePath + zwjm);
                        //设置查询延时：当文件不存在等待2秒
                        while (!sonFile.exists()){
                            Thread.currentThread().sleep(2000);
                            System.out.println("等待两秒");
                        }
                        //关闭响应主文件
                        try {
                            table.close();
                        } catch (IOException ex) {
                            System.out.println("Unable to close the table");
                        }
                        System.out.println("开始获取中登响应子文件内容");
                        return iterateTableData(prePath + zwjm);
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
    public static List<Map<String, String>> iterateTableData(String pathAndFile) throws IOException, CorruptedTableException {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
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
            System.out.println();
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
            list.add(map);
        }
        //关闭响应子文件
        try {
            table.close();
        } catch (IOException ex) {
            System.out.println("Unable to close the table");
        }
        return list;
    }

    //判是否是null或者“”；有值为true；无值为false
    public static boolean notNullAndEmpty(String str){
        if (null != str && !"".equals(str)){
            return true;
        }else {
            return false;
        }
    }

    //判断字段是否有值，无值赋值为""；
    public static String getColumnValue(String value) {
        if (notNullAndEmpty(value)){
            return value;
        }else {
            return "";
        }
    }
    //当前时间
    public static CurrentDateTimeVO getDateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYYMMdd HHmmss");
        String formatDate = simpleDateFormat.format(new Date());
        String date = formatDate.split(" ")[0];
        String time = formatDate.split(" ")[1];
        CurrentDateTimeVO currentDateTimeVO = new CurrentDateTimeVO();
        currentDateTimeVO.setYmd(date);
        currentDateTimeVO.setHms(time);
        return currentDateTimeVO;
    }

}