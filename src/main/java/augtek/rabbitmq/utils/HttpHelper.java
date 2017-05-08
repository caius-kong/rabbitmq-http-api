package augtek.rabbitmq.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import augtek.rabbitmq.exception.ResponseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by kongyunhui on 2017/3/31.
 */
public class HttpHelper {
    private static final Logger LOG = LoggerFactory.getLogger(HttpHelper.class);

    // 设置凭证
    public static void setupCredential(HttpRequestBase res, Map<String, String> credential){
        try {
            String up = credential.get("username") + ":" + credential.get("password");
            String credentialsInfo = new BASE64Encoder().encode(up.getBytes("UTF-8"));
            res.setHeader("Authorization", "Basic " + credentialsInfo);
        }catch(UnsupportedEncodingException e){
            LOG.error("error: {}", e.getMessage());
        }
    }

    // PUT method 设置参数
    public static void setJsonParams(HttpEntityEnclosingRequestBase res, JSONObject jsonParam){
        // 设置mini type
        res.setHeader("Content-Type", "application/json"); // 设置请求参数是json类型
        // 添加请求参数
        StringEntity entity = new StringEntity(jsonParam.toString(),"utf-8");//解决中文乱码问题
        res.setEntity(entity);
    }

    // 处理响应结果
    public static Object handlerResponse(CloseableHttpResponse response) throws ResponseException{
        Object result = null;
        try {
            int statusCode = response.getStatusLine().getStatusCode();
            LOG.debug("statusCode: {}", statusCode);
            // 不同statusCode，不同返回对象
            switch (statusCode) {
                case 200:
                    String respStr = EntityUtils.toString(response.getEntity());
                    if (respStr.startsWith("[")) {
                        result = JSON.parseArray(respStr);
                    } else {
                        result = JSON.parseObject(respStr);
                    }
                    break;
                case 201:
                    // createBinding: The response will contain a Location header telling you the URI of your new binding
                    result = response.getFirstHeader("Location");
                    break;
                case 204:
                    result = true; // PUT method 一般没有响应体
                    break;
                default:
                    String respStrError = EntityUtils.toString(response.getEntity());
                    close(response);
                    throw new ResponseException(JSON.parseObject(respStrError).get("reason").toString());
            }
            close(response); // 关闭连接
        } catch(IOException e){
            LOG.error("error: {}", e.getMessage());
        }
        return result;
    }

    private static void close(CloseableHttpResponse response) throws IOException{
        if(response != null){
            response.close();
        }
    }
}
