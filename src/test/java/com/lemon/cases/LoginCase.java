package com.lemon.cases;

import com.lemon.constants.Constants;
import com.lemon.pojo.CaseInfo;
import com.lemon.utils.AuthenticationUtils;
import com.lemon.utils.ExcelUtils;
import com.lemon.utils.HttpUtils2;
import io.qameta.allure.Description;
import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.*;

/**
 * @author david
 * @date 2021/7/4 - 22:03
 */
public class LoginCase extends BaseCase{
    private static Logger logger = Logger.getLogger(LoginCase.class); //传入的是一个字节码对象

    @Test(dataProvider = "datas", description = "登录测试description属性")
    @Description("description 注解")

    public void test(CaseInfo caseInfo) throws Exception { //这一步在jmeter里面就是用户定义变量
        // 1、参数化替换
        //sql: select leave_amount from member a where id = ${member_id}; 11
        //params:{"member_id","amount":"${amount}"} 11 3000
        //${member_id} = 11 ${amount} = 3000
        //类似于jmeter参数放在VARS里面就可以自动替换
        //现在已经有map了（VARS），sql也有了，如何把数据装入member_id，可以使用字符串替换的replace
        paramsReplace(caseInfo);

        // 2、数据库前置查询结果(数据断言必须在接口执行前后都查询)
        // 3、调用接口

        HttpResponse response = HttpUtils2.call(caseInfo.getMethod(), caseInfo.getContentType(), caseInfo.getUrl(), caseInfo.getParams(), Constants.HEADERS);
        String body = HttpUtils2.printResponse(response);



        //3.1. 从响应体中获取token
        //3.2. 使用jsonpath获取$.data.token_info.token
        //使用jsonpath获取$.data.token_info.token
        //fastjson可以使用jsonpath
        AuthenticationUtils.json2Vars(body, "$.data.token_info.token","${token}" );

        //取member_id,从响应体中获取member_id
        AuthenticationUtils.json2Vars(body, "$.data.id","${member_id}" );
        //下次要用token，就从VARS里面去取就可以了

        //sheetIndex
        //rowNum(用例编号，caseInfo.getId()
        //cellNum（可以数出来是I列）
        //msg => body

        // 4、断言响应结果
        //{"$.code":0,"$.msg":"OK","$.data.mobile_phone":"15670890431"}
        boolean assertResponseFlag = assertResponse(body, caseInfo.getExpectResult());

        // 5、添加接口响应回写内容
        addWriteBackData(caseInfo.getId(),Constants.RESPONSE_WRITE_BACK_CELLNUM, body);

        // 6、数据库后置查询结果
        // 7、据库断言
        // 8、添加断言回写内容
        String assertResult = assertResponseFlag? "passed":"failed";
        addWriteBackData(caseInfo.getId(),Constants.ASSERT_WRITE_BACK_CELLNUM, assertResult);

        // 9、添加日志
        // 10、报表断言
        Assert.assertEquals(assertResult,"passed");






    }




    @DataProvider
    public Object[] datas(){ //传参变成了一维数组，直接是一个对象就可以了
        Object[] datas = ExcelUtils.getDatas(sheetIndex, 1, CaseInfo.class);
        return datas;
    }
}

//public class LoginCase {
//    //读取testng.xml sheetIndex参数
//    public int sheetIndex;
//
//    @BeforeSuite
//    public void init() {
//        Constants.HEADERS.put("X-Lemonban-Media-Type","lemonban.v2");
//        Constants.HEADERS.put("Content-Type","application/json");
//    }
//
//    @BeforeClass
//    @Parameters({"sheetIndex"})
//    public void beforeClass(int sheetIndex) {
//        //System.out.println("LoginCase ===== sheetIndex:=============" + sheetIndex);
//        this.sheetIndex = sheetIndex;
//    }
//
//    @Test(dataProvider = "datas")
//    public void test(CaseInfo caseInfo) throws Exception {
//        HttpResponse response = HttpUtils2.call(caseInfo.getMethod(),caseInfo.getContentType()
//                ,caseInfo.getUrl(),caseInfo.getParams(), Constants.HEADERS);
//        String body = HttpUtils2.printResponse(response);
//        //1、从响应体里面获取token
//        Authentication.json2Vars(body,"$.data.token_info.token","${token}");
//        //2、从响应体里面获取member_id
//        Authentication.json2Vars(body,"$.data.id","${member_id}");
//
//    }
//
//
//
//    @DataProvider
//    public Object[] datas() {
//        Object[] datas = ExcelUtils.getDatas(sheetIndex, 1, CaseInfo.class);
//        return datas;
//    }
//
//}