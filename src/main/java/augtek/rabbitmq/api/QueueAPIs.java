package augtek.rabbitmq.api;

import augtek.rabbitmq.req.QueueOptions;
import augtek.rabbitmq.utils.HttpHelper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import augtek.rabbitmq.config.ServerConfig;
import augtek.rabbitmq.exception.ResponseException;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by kongyunhui on 2017/3/31.
 *
 * 队列管理
 */
public class QueueAPIs {
    private static final Logger LOG = LoggerFactory.getLogger(QueueAPIs.class);
    /**
     * 查询所有队列
     * @param serverConfig 配置服务器信息和登录用户信息
     * @return
     * @throws ResponseException 抛出响应异常信息，调用者需要捕获并处理
     */
    public static JSONArray findQueues(ServerConfig serverConfig) throws ResponseException {
        JSONArray result = null;
        try{
            String URL = serverConfig.CONTENT_PATH + "/api/queues";
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
     * 查询特定虚拟主机下的所有队列
     * @param serverConfig 配置服务器信息和登录用户信息
     * @return
     * @throws ResponseException 抛出响应异常信息，调用者需要捕获并处理
     */
    public static JSONArray findQueuesByVhost(ServerConfig serverConfig, String vhost) throws ResponseException{
        JSONArray result = null;
        try{
            String URL = serverConfig.CONTENT_PATH + "/api/queues/" + vhost;
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
     * 查询一个队列
     * @param serverConfig 配置服务器信息和登录用户信息
     * @param vhost 虚拟主机 默认虚拟主机是"/"，此处用%2f表示
     * @param queueName
     * @return
     * @throws ResponseException 抛出响应异常信息，调用者需要捕获并处理
     */
    public static JSONObject findQueue(ServerConfig serverConfig, String vhost, String queueName) throws ResponseException{
        JSONObject result = null;
        try{
            String URL = serverConfig.CONTENT_PATH + "/api/queues/"+vhost+"/" + queueName;
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
     * 删除一个队列
     * @param serverConfig 配置服务器信息和登录用户信息
     * @param vhost 虚拟主机 默认虚拟主机是"/"，此处用%2f表示
     * @param queueName
     * @return
     * @throws ResponseException 抛出响应异常信息，调用者需要捕获并处理
     *
     * 备注：if-empty=true and / or if-unused=true，表示：如果队列中有消息、消费者等，防止删除成功。
     */
    public static boolean deleteQueue(ServerConfig serverConfig, String vhost, String queueName) throws ResponseException{
        boolean isSuccess = false;
        try{
            String URL = serverConfig.CONTENT_PATH + "/api/queues/"+vhost+"/" + queueName + "?if-empty=true";
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
     * 创建一个队列
     * @param serverConfig 配置服务器信息和登录用户信息
     * @param vhost 虚拟主机 默认虚拟主机是"/"，此处用%2f表示
     * @param queueName
     * @param options 可选参数 一般需要设置属性durable=true，表示持久性
     * @return
     * @throws ResponseException 抛出响应异常信息，调用者需要捕获并处理
     */
    public static boolean createQueue(ServerConfig serverConfig, String vhost, String queueName, QueueOptions options) throws ResponseException{
        boolean isSuccess = false;
        try {
            String URL = serverConfig.CONTENT_PATH + "/api/queues/"+vhost+"/" + queueName;
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPut httpPut = new HttpPut(URL);
            HttpHelper.setupCredential(httpPut, serverConfig.CREDENTIAL);
            JSONObject jsonParam = new JSONObject();
            if(options!=null){
                jsonParam.put("auto_delete", options.isAuto_delete());
                jsonParam.put("durable", options.isDurable());
                jsonParam.put("arguments", options.getArguments()!=null?options.getArguments():new JSONObject());
                jsonParam.put("node", options.getNode());
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
     * 查询交换机的绑定列表 (即，绑定了哪些交换机，routing key分别是什么)
     * @param serverConfig 配置服务器信息和登录用户信息
     * @param vhost 虚拟主机 默认虚拟主机是"/"，此处用%2f表示
     * @param queueName
     * @return
     * @throws ResponseException 抛出响应异常信息，调用者需要捕获并处理
     */
    public static JSONArray findBindingsByQueue(ServerConfig serverConfig, String vhost, String queueName) throws ResponseException{
        JSONArray result = null;
        try{
            String URL = serverConfig.CONTENT_PATH + "/api/queues/"+vhost+"/"+queueName+"/bindings";
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
     * 从队列中获取指定数量的消息
     * @param serverConfig 配置服务器信息和登录用户信息
     * @param vhost 虚拟主机 默认虚拟主机是"/"，此处用%2f表示
     * @param queueName
     * @param count 获取消息的最大数量
     * @param requeue 确定消息是否将从队列中移除
     * @param encoding 编码格式 auto | base64
     * @param truncate 可选，表示截取 例如50000, 超过50000个字节的部分丢弃
     * @return
     * @throws ResponseException 抛出响应异常信息，调用者需要捕获并处理
     *
     * 注意: 主要用于系统管理员诊断工具 (不能实现可靠地传递)
     * Please note that the get path in the HTTP API is intended for diagnostics etc - it does not implement reliable delivery and so should be treated as a sysadmin's tool rather than a general API for messaging.
     */
    public static JSONArray receiveMessageFromQueue(ServerConfig serverConfig, String vhost, String queueName, int count, boolean requeue, String encoding, int truncate) throws ResponseException{
        JSONArray result = null;
        try{
            String URL = serverConfig.CONTENT_PATH + "/api/queues/"+vhost+"/"+queueName+"/get";
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(URL);
            HttpHelper.setupCredential(httpPost, serverConfig.CREDENTIAL);
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("count", count);
            jsonParams.put("requeue", requeue);
            jsonParams.put("encoding", encoding);
            jsonParams.put("truncate", truncate);
            HttpHelper.setJsonParams(httpPost, jsonParams);
            CloseableHttpResponse response = client.execute(httpPost);
            result = (JSONArray) HttpHelper.handlerResponse(response);
        }catch(IOException e){
            LOG.error("error: {}", e.getMessage());
        }
        return result;
    }

    /**
     * 下面的api是在HTTP API返回的结果上定制的 （常用数据定制）
     */

    /**
     * 查询某个虚拟主机下的某个队列的消息数
     * @param serverConfig 配置服务器信息和登录用户信息
     * @param vhost 虚拟主机 默认虚拟主机是"/"，此处用%2f表示
     * @param queueName
     * @return
     * @throws ResponseException 抛出响应异常信息，调用者需要捕获并处理
     */
    public static int findMessageCountByQueue(ServerConfig serverConfig, String vhost, String queueName) throws ResponseException{
        JSONObject queue = QueueAPIs.findQueue(serverConfig, vhost, queueName);
        return queue.get("messages")!=null?Integer.parseInt(queue.get("messages").toString()):-1;
    }
}
