package com.lemon.utils;

import com.alibaba.fastjson.JSONObject;
import com.lemon.cases.BaseCase;
import com.lemon.constants.Constants;
import com.lemon.pojo.CaseInfo;
import io.qameta.allure.Step;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * @author david
 * @date 2021/6/30 - 17:57
 */
public class HttpUtils2 {

    private static org.apache.log4j.Logger logger = Logger.getLogger(HttpUtils2.class); //传入的是一个字节码对象


    public static void main(String[] args) throws Exception {
        //静态方法
        /*
        * 静态方法：
        * 类名调用，不需要创建对象。
        * 静态方法可以直接调用静态方法。
        */


        String params = "{\"mobile_phone\":\"13212312312\", \"pwd\": \"12345678\"}";
        String url = "http://localhost:12306/member/login";
        Constants.HEADERS.put("X-Lemonban-Media-Type","lemonban.v2");
        Constants.HEADERS.put("content-type","application/json;charset=utf-8");
        HttpResponse httpResponse = HttpUtils2.jsonPost(url, params, Constants.HEADERS);
        printResponse(httpResponse);


//        String url = "http://localhost:12306/member/recharge";
//        String params = "{\"member_id\": \"123\",\"amount\": \"4000\"}";
//        Constants.HEADERS.put("X-Lemonban-Media-Type","lemonban.v2");
//        Constants.HEADERS.put("Authorization","Bearer Token");
//        Constants.HEADERS.put("content-type","application/json");
//        HttpResponse httpResponse = HttpUtils2.jsonPost(url, params, Constants.HEADERS);
//        printResponse(httpResponse);

    }

    /**
     * 发送http请求
     * @param method 请求方式
     * @param contentType 参数类型
     * @param url 接口地址
     * @param params
     * @return
     * @throws Exception
     */

    @Step("call方法")
    public static HttpResponse call(String method, String contentType, String url, String params,Map<String, String> headers) throws Exception {
//        String method = caseInfo.getMethod();
//        String contentType = caseInfo.getContentType();

        //如果请求方式是post
        if("post".equalsIgnoreCase(method)) { //倒过来不行，因为防止为空，会出现空指针异常
            //如果参数类型是json
            if ("json".equalsIgnoreCase(contentType)) {
                return HttpUtils2.jsonPost(url, params,headers);
                //如果参数类型是form
            } else if ("form".equalsIgnoreCase(contentType)) {
                //{"mobilephone":"13877788811","pwd":"12345678"}
                //mobilephone=13877788811&pwd=12345678

                params = json2KeyValue(params);
                return HttpUtils2.formPost(url, params,headers);
            }else{
                System.out.println("没有发送http请求"+url);
            }
            //如果请求方式是get
        } else if("get".equalsIgnoreCase(method)){
            //处理 url url/xxx/yyy/2/zzz
            return HttpUtils2.get(url,headers);
            //如果请求方式是patch
        } else if("patch".equalsIgnoreCase(method)){
            return HttpUtils2.patch(url, params, headers);
        }else{
            System.out.println("method = " + method + ", contentType = " + contentType + ", url = " + url + ", params = " + params);
        }


        return null;

    }

    /**
     * 把JSON参数转成form参数（key=value)
     * @param jsonStr
     * @return
     */
    public static String json2KeyValue(String jsonStr){
        //把json转成Map
        Map<String,String> map = JSONObject.parseObject(jsonStr, Map.class);
        //遍历key
        Set<String> KeySet = map.keySet();
        String result = "";
        for (String key : KeySet) {
            //通过key获取值
            String value = map.get(key);
            //拼接 key=value&
            result += key + "=" + value + "&";
        }
        //去掉最后一个多余的&
        result = result.substring(0, result.length() - 1);
        System.out.println(result);
        return result;
    }



    /** // /**回车后自动会加上后面这些东西，这是文档注释
     * 发送http get请求
     * @param url
     *  url必须带参数，如果不带不会自动携带参数。
     *  url?KEY=VALUE&KEY2=VALUE2
     *  url/xxx/yyy/2/zzz
     * @throws Exception
     */

    //http 工具类，发送http/https请求、
    //工具类，不用写main,而且一般都是静态方法，与业务无关
    //静态方法的好处，就是方便调用，不需要创建对象，而且可以类名点的调用

    public static HttpResponse get(String url,Map<String, String> headers) throws Exception {
        //发送一个get请求
        //1. 首先需要确定URL（包括了接口的地址）
        //2. 然后需要确认请求类型method
        //3. 请求参数
        //4. 请求头
        //5. 发送请求
        //6. 接收请求
        //7. 格式化请求

        //1. 创建get请求并写入接口地址
        HttpGet get = new HttpGet(url);

        //2. 在get请求上添加请求头
        addHeaders(headers, get);
//        get.addHeader("X-Lemonban-Media-Type","lemonban.v2");

        //3. 创建一个客户端 //XXXs都是工具类，XXXUtils也是工具类
        HttpClient client = HttpClients.createDefault();

        //4. 客户端发送请求，并且返回响应对象(响应头、响应体、响应状态 ）
        HttpResponse response = client.execute(get);//要抛出异常
//        printResponse(response);
        return response;

    }


    /**
     * 发送http post请求
     * @param url 接口请求地址
     * @param params json格式的参数
     * @param headers 请求头
     * @throws Exception
     */
    public static HttpResponse jsonPost(String url, String params, Map<String, String> headers) throws Exception {

        //1. 创建POST请求并写入接口地址
        HttpPost post = new HttpPost(url);

        //2. 在POST请求上添加请求头
        addHeaders(headers, post);

        post.addHeader("X-Lemonban-Media-Type","lemonban.v2");
//        post.addHeader("Content-Type","application/json");

        //3. 请求参数，加在请求体里面
//        String json = "{\"mobile_phone\": \"13888888888\",\"pwd\": \"12345678\"}";
//        String json = "{\"mobile_phone\": \"13899999667\",\"pwd\": \"12345678\"}";
        StringEntity stringEntity = new StringEntity(params,"utf-8");
        post.setEntity(stringEntity);

        //4. 创建一个客户端 //XXXs都是工具类，XXXUtils也是工具类
        HttpClient client = HttpClients.createDefault();

        //5. 客户端发送请求，并且返回响应对象(响应头、响应体、响应状态 ）
        HttpResponse response = client.execute(post);//要抛出异常

        //5. 获取响应头、响应体和响应状态码
        //5.1 获取响应体
//        printResponse(response);
        return response;
    }




    public static HttpResponse patch(String url, String params,Map<String, String> headers) throws Exception {

        //1. 创建POST请求并写入接口地址
        HttpPatch patch = new HttpPatch(url);

        //2. 在POST请求上添加请求头
        addHeaders(headers, patch);
//        patch.addHeader("X-Lemonban-Media-Type","lemonban.v2");
//        patch.addHeader("Content-Type","application/json");

        //3. 请求参数，加在请求体里面
//        String json = "{\"mobile_phone\": \"13888888888\",\"pwd\": \"12345678\"}";
//        String json = "{\"mobile_phone\": \"13899999667\",\"pwd\": \"12345678\"}";
        StringEntity stringEntity = new StringEntity(params,"utf-8");
        patch.setEntity(stringEntity);

        //4. 创建一个客户端 //XXXs都是工具类，XXXUtils也是工具类
        HttpClient client = HttpClients.createDefault();

        //5. 客户端发送请求，并且返回响应对象(响应头、响应体、响应状态 ）
        HttpResponse response = client.execute(patch);//要抛出异常

        //5. 获取响应头、响应体和响应状态码
        //5.1 获取响应体
//        printResponse(response);
        return response;
    }


    /**
     *
     * @param url 接口请求地址
     * @param params key=value格式的参数
     * @throws Exception
     */

    public static HttpResponse formPost(String url, String params,Map<String, String> headers) throws Exception {

        //1. 创建POST请求并写入接口地址
        HttpPost post = new HttpPost(url);

        //2. 在POST请求上添加请求头
//        post.addHeader("X-Lemonban-Media-Type","lemonban.v1");
        addHeaders(headers, post);
//        post.addHeader("Content-Type","application/x-www-form-urlencoded");

        //3. 请求参数，加在请求体里面
//        String json = "{\"mobile_phone\": \"13888888888\",\"pwd\": \"12345678\"}";
//        String json = "{\"mobile_phone\": \"13899999667\",\"pwd\": \"12345678\"}";
        StringEntity stringEntity = new StringEntity(params,"utf-8");
        post.setEntity(stringEntity);

        //4. 创建一个客户端 //XXXs都是工具类，XXXUtils也是工具类
        HttpClient client = HttpClients.createDefault();

        //5. 客户端发送请求，并且返回响应对象(响应头、响应体、响应状态 ）
        HttpResponse response = client.execute(post);//要抛出异常

        //5. 获取响应头、响应体和响应状态码
        //5.1 获取响应体
//        printResponse(response);
        return response;
    }


    @Step("printResponse")
    public static String printResponse(HttpResponse response) { //private则方法只能在本类中使用
        //5. 获取响应头、响应体和响应状态码
        //5.1 获取响应体
        HttpEntity entity = response.getEntity();
        logger.info(entity);
        String body = null;
        try {
            body = EntityUtils.toString(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info(body);

        //5.2 获取响应头
        Header[] allHeaders = response.getAllHeaders(); //这是一个数组，如果直接打印肯定是地址值
//        System.out.println(allHeaders);
        logger.info(Arrays.toString(allHeaders));

        //5.3 获取响应码
        //新语法，链式编程，调用方法之后继续调用方法
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);

        return body;
    }


    /**
     * 添加请求头
     * @param headers
     * @param request 请求对象
     */
    private static void addHeaders(Map<String, String> headers, HttpRequest request) { //post, put, get, delete所有这些对象的父接口都可以用多态
        Set<String> keySet = headers.keySet();
        for (String name : keySet) {
            String value = headers.get(name);
            request.addHeader(name, value);
        }
    }

}


