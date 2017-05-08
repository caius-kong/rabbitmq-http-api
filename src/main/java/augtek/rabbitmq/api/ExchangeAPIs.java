package augtek.rabbitmq.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import augtek.rabbitmq.config.ServerConfig;
import augtek.rabbitmq.exception.ResponseException;
import augtek.rabbitmq.req.ExchangeOptions;
import augtek.rabbitmq.utils.HttpHelper;
import com.rabbitmq.client.BasicProperties;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by kongyunhui on 2017/3/31.
 *
 * 交换机管理
 */
public class ExchangeAPIs {
    private static final Logger LOG = LoggerFactory.getLogger(ExchangeAPIs.class);
    /**
     * 查询所有交换机
     * @param serverConfig 配置服务器信息和登录用户信息
     * @return
     * @throws ResponseException 抛出响应异常信息，调用者需要捕获并处理
     */
    public static JSONArray findExchanges(ServerConfig serverConfig) throws ResponseException{
        JSONArray result = null;
        try{
            String URL = serverConfig.CONTENT_PATH + "/api/exchanges";
            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(URL);
            HttpHelper.setupCredential(httpGet, serverConfig.CREDENTIAL);
            CloseableHttpResponse response = client.execute(httpGet);
            result = (JSONArray) HttpHelper.handlerResponse(response);
        }catch(IOException e){
            LOG.error("error: {}", e.getMessage());
        }
        return result;
    }

    /**
     * 查询特定虚拟主机下的所有交换机
     * @param serverConfig 配置服务器信息和登录用户信息
     * @return
     * @throws ResponseException 抛出响应异常信息，调用者需要捕获并处理
     */
    public static JSONArray findExchangesByVhost(ServerConfig serverConfig, String vhost) throws ResponseException{
        JSONArray result = null;
        try{
            String URL = serverConfig.CONTENT_PATH + "/api/exchanges/" + vhost;
            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(URL);
            HttpHelper.setupCredential(httpGet, serverConfig.CREDENTIAL);
            CloseableHttpResponse response = client.execute(httpGet);
            result = (JSONArray) HttpHelper.handlerResponse(response);
        }catch(IOException e){
            LOG.error("error: {}", e.getMessage());
        }
        return result;
    }

    /**
     * 查询一个交换机
     * @param serverConfig 配置服务器信息和登录用户信息
     * @param vhost 虚拟主机 默认虚拟主机是"/"，此处用%2f表示
     * @param exchangeName
     * @return
     * @throws ResponseException 抛出响应异常信息，调用者需要捕获并处理
     */
    public static JSONObject findExchange(ServerConfig serverConfig, String vhost, String exchangeName) throws ResponseException{
        JSONObject result = null;
        try{
            String URL = serverConfig.CONTENT_PATH + "/api/exchanges/"+vhost+"/" + exchangeName;
            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(URL);
            HttpHelper.setupCredential(httpGet, serverConfig.CREDENTIAL);
            CloseableHttpResponse response = client.execute(httpGet);
            result = (JSONObject) HttpHelper.handlerResponse(response);
        }catch(IOException e){
            LOG.error("error: {}", e.getMessage());
        }
        return result;
    }

    /**
     * 删除一个交换机
     * @param serverConfig 配置服务器信息和登录用户信息
     * @param vhost 虚拟主机 默认虚拟主机是"/"，此处用%2f表示
     * @param exchangeName
     * @return
     * @throws ResponseException 抛出响应异常信息，调用者需要捕获并处理
     */
    public static boolean deleteExchange(ServerConfig serverConfig, String vhost, String exchangeName) throws ResponseException{
        boolean isSuccess = false;
        try{
            String URL = serverConfig.CONTENT_PATH + "/api/exchanges/"+vhost+"/" + exchangeName;
            CloseableHttpClient client = HttpClients.createDefault();
            HttpDelete httpDelete = new HttpDelete(URL);
            HttpHelper.setupCredential(httpDelete, serverConfig.CREDENTIAL);
            CloseableHttpResponse response = client.execute(httpDelete);
            if(HttpHelper.handlerResponse(response) instanceof  Boolean){
                isSuccess = true;
            }
        }catch(IOException e){
            LOG.error("error: {}", e.getMessage());
        }
        return isSuccess;
    }

    /**
     * 创建一个交换机
     * @param serverConfig 配置服务器信息和登录用户信息
     * @param vhost 虚拟主机 默认虚拟主机是"/"，此处用%2f表示
     * @param exchangeName
     * @param type 交换机类型，可参考：ExchangeTypeEnum.java
     * @param options 可选参数 ，一般需要设置属性durable=true，表示持久性
     * @return
     * @throws ResponseException 抛出响应异常信息，调用者需要捕获并处理
     */
    public static boolean createExchange(ServerConfig serverConfig, String vhost, String exchangeName, String type, ExchangeOptions options) throws ResponseException{
        boolean isSuccess = false;
        try {
            String URL = serverConfig.CONTENT_PATH + "/api/exchanges/"+vhost+"/" + exchangeName;
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPut httpPut = new HttpPut(URL);
            HttpHelper.setupCredential(httpPut, serverConfig.CREDENTIAL);
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("type", type);
            if(options!=null){
                jsonParam.put("auto_delete", options.isAuto_delete());
                jsonParam.put("durable", options.isDurable());
                jsonParam.put("internal", options.isInternal());
                jsonParam.put("arguments", options.getArguments() !=null? options.getArguments():new JSONObject());
            }
            HttpHelper.setJsonParams(httpPut, jsonParam);
            CloseableHttpResponse response = httpClient.execute(httpPut);
            if(HttpHelper.handlerResponse(response) instanceof Boolean){
                isSuccess = true;
            }
        }catch(IOException e){
            LOG.error("error: {}", e.getMessage());
        }
        return isSuccess;
    }

    /**
     * 查询交换机的绑定列表 (即，绑定了哪些队列，routing key分别是什么)
     * @param serverConfig 配置服务器信息和登录用户信息
     * @param vhost 虚拟主机 默认虚拟主机是"/"，此处用%2f表示
     * @param exchangeName
     * @param isSource true 表示给定交换机是source; false 表示给定交换机是destination
     * @return
     * @throws ResponseException 抛出响应异常信息，调用者需要捕获并处理
     */
    public static JSONArray findBindingsByExchange(ServerConfig serverConfig, String vhost, String exchangeName, boolean isSource) throws ResponseException{
        JSONArray result = null;
        try{
            String URL = serverConfig.CONTENT_PATH + "/api/exchanges/" + vhost + "/" + exchangeName + "/bindings/";
            if(isSource) {
                URL += "source";
            } else {
                URL += "destination";
            }
            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(URL);
            HttpHelper.setupCredential(httpGet, serverConfig.CREDENTIAL);
            CloseableHttpResponse response = client.execute(httpGet);
            result = (JSONArray) HttpHelper.handlerResponse(response);
        }catch(IOException e){
            LOG.error("error: {}", e.getMessage());
        }
        return result;
    }

    /**
     * 发布一个消息到指定交换机
     * @param serverConfig 配置服务器信息和登录用户信息
     * @param properties 消息的属性 例如：MessageProperties.PERSISTENT_TEXT_PLAIN 表示消息持久化
     * @param routing_key 路由键
     * @param payload 消息载体
     * @param payload_encoding 载体编码 string | base64 (string代表utf-8编码)
     * @return 如果消息被发送到至少一个队列，将返回{"routed": true}
     *
     * 注意：该HTTP API不适合高性能发布。
     * 相较于AMQP或者其它使用long-lived连接的协议，我们应该为每一个消息发布创建一个新的TCP连接，以限制消息的吞吐量。
     */
    public static JSONObject sendMessageToExchange(ServerConfig serverConfig, String vhost, String exchangeName, BasicProperties properties, String routing_key, String payload, String payload_encoding) throws ResponseException{
        JSONObject result = null;
        try{
            String URL = serverConfig.CONTENT_PATH + "/api/exchanges/"+vhost+"/"+exchangeName+"/publish";
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(URL);
            HttpHelper.setupCredential(httpPost, serverConfig.CREDENTIAL);
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("properties", properties != null? properties : new JSONObject());
            jsonParams.put("routing_key", routing_key);
            jsonParams.put("payload", payload);
            jsonParams.put("payload_encoding", payload_encoding);
            HttpHelper.setJsonParams(httpPost, jsonParams);
            CloseableHttpResponse response = client.execute(httpPost);
            result = (JSONObject) HttpHelper.handlerResponse(response);
        }catch(IOException e){
            LOG.error("error: {}", e.getMessage());
        }
        return result;
    }
}
