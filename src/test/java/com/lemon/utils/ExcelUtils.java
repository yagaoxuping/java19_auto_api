package com.lemon.utils;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.lemon.constants.Constants;
import com.lemon.pojo.CaseInfo;
import com.lemon.pojo.WriteBackData;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author david
 * @date 2021/7/1 - 18:27
 * excel工具类
 *
 */
public class ExcelUtils {
//    public static void main(String[] args) throws Exception {
//        List<CaseInfo> caseInfoList = read(0,1,CaseInfo.class);
//        List<CaseInfo2> caseInfo2List = read(1,1,CaseInfo2.class);
//
//        for (CaseInfo caseInfo : caseInfoList) {
//            System.out.println(caseInfo);
//        }
//
//        System.out.println("============================");
//        for (CaseInfo2 caseInfo2 : caseInfo2List) {
//            System.out.println(caseInfo2);
//        }
//
//    }

    //批量回写需要一个集合
    public static List<WriteBackData> wbdList = new ArrayList(); //首先在LoginCase里面放在Test里面肯定不行，只是一个局部变量，如果放在class里面，由于三个类（Login, Recharge, registerCase都需要，需要跨类，所以用静态变量，用ExcelUtils工具类



    /**
     * 获取testng测试方法 数据驱动
     * @param sheetIndex
     * @param sheetNum
     * @param clazz
     * @return
     */

    public static Object[] getDatas(int sheetIndex, int sheetNum, Class clazz){

        //把集合转成数组nshi 
        try {
            List<CaseInfo> list = ExcelUtils.read(sheetIndex, sheetNum, clazz);
            Object[] datas = list.toArray();
            return datas;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




    /**
     * 读取excel并返回映射关系集合
     * @param sheetIndex sheet开始位置
     * @param sheetNum sheet个数
     * @param clazz 映射关系字节码
     * @return
     * @throws Exception
     */

    public static List read(int sheetIndex, int sheetNum, Class clazz) throws Exception{
        //如果是有参构造，void之后，就不会有return
        //空参默认有，但是写了有参构造，void就不会提供
        //获取excel路径
//        String path = ExcelUtils.class.getClassLoader().getResource("./cases_v1.xls").getPath();
        FileInputStream fis = new FileInputStream(Constants.EXCEL_PATH);

        //导入参数
        //easypoi的类
        ImportParams params = new ImportParams();

        //从第一个sheet开始读取
        params.setStartSheetIndex(sheetIndex);

        //每次读取一个sheet
        params.setSheetNum(sheetNum);


        //importExcel(EXCEL文件流，映射关系字节码对象，导入参数)
//        List<CaseInfo> caseInfoList = ExcelImportUtil.importExcel(fis, CaseInfo.class, params); //返回的是一个集合List
        List caseInfoList = ExcelImportUtil.importExcel(fis, clazz, params); //返回的是一个集合List


        return caseInfoList;

    }

    //批量回写

    /**
     *
     * @throws FileNotFoundException
     */
    public static void batchWrite() throws FileNotFoundException {

        //由于作用域的问题，需要在外面定义一下
        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            fis = new FileInputStream(Constants.EXCEL_PATH);
            //1. 解析数据必须用poi提供对象
            Workbook excel = WorkbookFactory.create(fis);

            //循环 批量回写集合wbdList
            for (WriteBackData writeBackData : wbdList) {
                //取出sheetIndex
                int sheetIndex = writeBackData.getSheetIndex();
                //取出行号
                int rowNum = writeBackData.getRowNum();
                //取出列号
                int cellNum = writeBackData.getCellNum();
                //取出回写内容
                String content = writeBackData.getContent();
                //2. 选择sheet
                Sheet sheet = excel.getSheetAt(sheetIndex);

                //3. 读取每一行
                Row row = sheet.getRow(rowNum);

                //4. 读取每一个单元格 MissingCellPolicy 当cell为null时，返回一个空的cell对象
                Cell cell = row.getCell(cellNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

                //5. 修改
                cell.setCellValue(content);

            }


            fos = new FileOutputStream(Constants.EXCEL_PATH);

            //6. 写回去
            excel.write(fos);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            //不管出不出现异常，都一定会执行
            //释放资源
            //7. 关流

            try {
                if(fis != null) {
                    fis.close();
                } //因为有可能等于null，就挂掉了
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if(fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }




    }



}
