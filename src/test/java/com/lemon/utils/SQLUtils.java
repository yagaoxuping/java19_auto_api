package com.lemon.utils;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author david
 * @date 2021/7/12 - 15:35
 */
public class SQLUtils {
//    public static void main(String[] args) {
//        //Dbutils
//        //        mapHandler();
//        Object singleResult = getSingleResult("SELECT count(*) FROM member a where a.mobile_phone = '18900000000'");
//        System.out.println(singleResult);
//    }
//
    public static void main(String[] args) {
        String sql = "select leave_amount from member a where id = 6625;";
        Object result = getSingleResult(sql);
        System.out.println(result);
        System.out.println(result.getClass());
    }

    /**
     * 查询数据库单行单列结果集。
     * @param sql sql语句
     * @return 查询结果
     */
    public static Object getSingleResult(String sql) {

        if(StringUtils.isBlank(sql)) {
            System.out.println("sql为空");
            return null;
        }
        Object result = null;

        //创建QueryRunner对象
        QueryRunner runner = new QueryRunner();
        //获取链接
        Connection conn = JDBCUtils.getConnection();

        try {
//            String sql = "SELECT count(*) FROM member a where a.mobile_phone = '18900000000'";
            //创建结果集对象
//            MapHandler handler = new MapHandler();
            ScalarHandler handler = new ScalarHandler();
//            Map<String, Object> map = runner.query(conn, sql, handler);
            //执行查询语句
            result = runner.query(conn, sql, handler);
//            System.out.println(result);

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            JDBCUtils.close(conn);
        }
        return result;
    }

    public static void mapHandler() {
        QueryRunner runner = new QueryRunner();
        Connection conn = JDBCUtils.getConnection();

        try {
            String sql = "SELECT * FROM member a where a.mobile_phone = '18900000000'";
            MapHandler handler = new MapHandler();
            Map<String, Object> map = runner.query(conn, sql, handler);
            System.out.println(map);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            JDBCUtils.close(conn);
        }
    }
}
