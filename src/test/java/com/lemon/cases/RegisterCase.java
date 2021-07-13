package com.lemon.cases;


import com.lemon.constants.Constants;
import com.lemon.pojo.CaseInfo;
import com.lemon.pojo.WriteBackData;
import com.lemon.utils.ExcelUtils;
import com.lemon.utils.HttpUtils2;
import com.lemon.utils.SQLUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.Map;

import java.util.List;

/**
 * @author david
 * @date 2021/7/1 - 18:17
 * 注册用例
 * testng和httpclient整合了一下，实现了单接口的批量处理
 *
 */
public class RegisterCase extends BaseCase{



    @Test(dataProvider = "datas")
    public void test(CaseInfo caseInfo) throws Exception {
        // 1、参数化替换
        //sql: select leave_amount from member a where id = ${member_id}; 11
        //params:{"member_id","amount":"${amount}"} 11 3000
        //${member_id} = 11 ${amount} = 3000
        //类似于jmeter参数放在VARS里面就可以自动替换
        //现在已经有map了（VARS），sql也有了，如何把数据装入member_id，可以使用字符串替换的replace
        paramsReplace(caseInfo);

        // 2、数据库前置查询结果(数据断言必须在接口执行前后都查询)
        Object beforeSqlResult = SQLUtils.getSingleResult(caseInfo.getSql());
        // 3、调用接口

        HttpResponse response = HttpUtils2.call(caseInfo.getMethod(), caseInfo.getContentType(), caseInfo.getUrl(), caseInfo.getParams(),Constants.HEADERS);

        String body = HttpUtils2.printResponse(response);

        // 4、断言响应结果
        //{"$.code":0,"$.msg":"OK","$.data.mobile_phone":"15670890431"}
        boolean assertResponseFlag = assertResponse(body, caseInfo.getExpectResult());

        // 5、添加接口响应回写内容
        addWriteBackData(caseInfo.getId(),Constants.RESPONSE_WRITE_BACK_CELLNUM, body);

        // 6、数据库后置查询结果
        Object afterSqlResult = SQLUtils.getSingleResult(caseInfo.getSql());

        // 7、数据库断言
        boolean assertSqlFlag = sqlAssert(caseInfo, beforeSqlResult, afterSqlResult);

        // 8、添加断言回写内容
        String assertResult = assertResponseFlag && assertSqlFlag ? "passed":"failed";
        addWriteBackData(caseInfo.getId(),Constants.ASSERT_WRITE_BACK_CELLNUM, assertResult);

        // 9、添加日志
        // 10、报表断言
        Assert.assertEquals(assertResult,"passed");

    }

    /**
     * 数据库断言
     * @param caseInfo sql语句
     * @param beforeSqlResult sql前置查询结果
     * @param afterSqlResult sql后置查询结果
     * @return 返回值是断言结果
     */
    public boolean sqlAssert(CaseInfo caseInfo, Object beforeSqlResult, Object afterSqlResult) {
        boolean flag = false;
        if(StringUtils.isNotBlank(caseInfo.getSql())){
            if(beforeSqlResult == null || afterSqlResult == null){
                System.out.println("数据库断言失败");
            }else {
                Long l1 = (Long) beforeSqlResult; //转换之前是object，转了之后，才能和0和1去==比较
                Long l2 = (Long) afterSqlResult;
                //接口执行之前查询结果为0， 接口执行之后查询结果为1
                if (l1 == 0 && l2 == 1) {
                    System.out.println("数据库断言成功");
                    flag = true;
                } else {
                    System.out.println("数据库断言失败");
                }
            }
        }else{
            System.out.println("sql为空，不需要数据库断言");
        }
        return flag;
    }

    @DataProvider
    public Object[] datas(){ //传参变成了一维数组，直接是一个对象就可以了
        Object[] datas = ExcelUtils.getDatas(sheetIndex, 1, CaseInfo.class);
        return datas;
    }



}
