package com.example.statepower.bean;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExportExcel {

    public static HSSFWorkbook createExcel(String sheetName,List<String> titleList,List<Record> dataList) throws IllegalAccessException {

        //创建HSSFWorkbook对象
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFCellStyle titleStyle = createTitleCellStyle(wb);
        //创建sheet对象
        HSSFSheet sheet=wb.createSheet(sheetName);
        //在sheet里创建第一行，这里即是表头
        HSSFRow rowTitle=sheet.createRow(0);
        //写入表头的每一个列
        for (int i = 0; i < titleList.size(); i++) {
            //创建单元格
            //rowTitle.createCell(i).setCellValue(titleList.get(i));
            HSSFCell c= rowTitle.createCell(i);
            c.setCellValue(titleList.get(i));
            c.setCellStyle(titleStyle);
            sheet.setColumnWidth(i, 20*256);
            //System.out.println("title:"+titleList.get(i));
        }

//        //写入每一行的记录
//        for (int i = 0; i < dataList.size(); i++) {
//            //创建新的一行，递增
//            HSSFRow rowData = sheet.createRow(i+1);
//
//            //通过反射，获取POJO对象
//            Class cl = dataList.get(i).getClass();
//            //获取类的所有字段
//            Field[] fields = cl.getDeclaredFields();
//            for (int j = 0; j < fields.length; j++) {
//                //设置字段可见，否则会报错，禁止访问
//                fields[j].setAccessible(true);
//                //创建单元格
//                rowData.createCell(j).setCellValue(fields[j].get(dataList.get(i)));
//            }
//        }
        Iterator<Record> it = dataList.iterator();
        HSSFRow row;
        int index = 1;
        while (it.hasNext()) {
            index++;
            row = sheet.createRow(index);
            Record t = it.next();
            // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
            Field[] fields = t.getClass().getDeclaredFields();
            for (short i = 0; i < fields.length; i++) {
                HSSFCell cell = row.createCell(i);
                Field field = fields[i];
                String fieldName = field.getName();
                String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                try {
                    Class tCls = t.getClass();
                    Method getMethod = tCls.getMethod(getMethodName, new Class[]{});
                    Object value = getMethod.invoke(t, new Object[]{});
                    // 判断值的类型后进行强制类型转换
                    String textValue = null;
                    if (value instanceof Integer) {
                        sheet.setColumnWidth(i, (short) (35.7 * 80));
                        int intValue = (Integer) value;
                        cell.setCellValue(intValue);
                    } else if (value instanceof Date) {
                        sheet.setColumnWidth(i, (short) (35.7 * 150));
                        Date date = (Date) value;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        textValue = sdf.format(date);
                        cell.setCellValue(textValue);
                    }
                    else if (value instanceof byte[]) {
                        // 有图片时，设置行高为60px;
                        row.setHeightInPoints(60);
                        // 设置图片所在列宽度为80px,注意这里单位的一个换算
                        sheet.setColumnWidth(i, (short) (35.7 * 100));
                        // sheet.autoSizeColumn(i);
                        byte[] bsValue = (byte[]) value;
                        HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0,
                                1023, 255, (short) 6, index, (short) 6, index);
                        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
                        patriarch.createPicture(anchor, wb.addPicture(bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
                    }
                    else {
                        // 其它数据类型都当作字符串简单处理
                        sheet.setColumnWidth(i, (short) (35.7 * 200));
                        textValue = value.toString();
                        cell.setCellValue(textValue);
                    }
                }
                catch (SecurityException e)
                {
                    e.printStackTrace();
                }
                catch (NoSuchMethodException e)
                {
                    e.printStackTrace();
                }
                catch (IllegalArgumentException e)
                {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }
                catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

            }
        }

        return wb;
    }

    private static HSSFCellStyle createTitleCellStyle(HSSFWorkbook wb) {
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直对齐
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setFillForegroundColor(IndexedColors.ORANGE.index);//背景颜色

        HSSFFont headerFont1 = (HSSFFont) wb.createFont(); // 创建字体样式
        headerFont1.setBold(true); //字体加粗
        headerFont1.setFontName("黑体"); // 设置字体类型
        headerFont1.setFontHeightInPoints((short) 15); // 设置字体大小
        cellStyle.setFont(headerFont1); // 为标题样式设置字体样式

        return cellStyle;
    }
}