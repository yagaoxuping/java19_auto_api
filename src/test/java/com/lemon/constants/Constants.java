package com.lemon.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * @author david
 * @date 2021/7/4 - 21:56
 * 常量类 final
 */
//public class Constants {
//    //常量类
//    //数据驱动，excel路径
//    public static final String EXCEL_PATH = Constants.class.getClassLoader().getResource("./cases_v3.xlsx").getPath();
//    //默认请求头
//    public static final Map<String,String> HEADERS =  new HashMap<>();
//}

public class Constants {
    //数据驱动excel路径
//    public static final String EXCEL_PATH = Constants.class.getClassLoader().getResource("./cases_v3.xlsx").getPath();
    public static final String EXCEL_PATH = "C:\\Users\\yagaoxuping\\IdeaProjects\\java_api\\java19_auto_api_v9\\src\\test\\resources\\cases_v3.xlsx";
    //默认请求头
    public static final Map<String,String> HEADERS = new HashMap<>();

    //excel 响应回写列
    public static final int RESPONSE_WRITE_BACK_CELLNUM = 8;
    //excel 断言回写列
    public static final int ASSERT_WRITE_BACK_CELLNUM = 10;

    //数据库连接URL                                jdbc:数据库名称://ip:port/数据库名称
    //jdbc:oracle:thin:@//127.0.0.1:1521/orcl
    public static final String JDBC_URL = "jdbc:mysql://api.lemonban.com:3306/futureloan?useUnicode=true&characterEncoding=utf-8";
    //数据库用户名
    public static final String JDBC_USERNAME = "future";
    //数据库密码
    public static final String JDBC_PASSWORD = "123456";



}