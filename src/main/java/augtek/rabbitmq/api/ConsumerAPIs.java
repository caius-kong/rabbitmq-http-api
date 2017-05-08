package augtek.rabbitmq.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import augtek.rabbitmq.config.ServerConfig;
import augtek.rabbitmq.exception.ResponseException;
import augtek.rabbitmq.utils.HttpHelper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by kongyunhui on 2017/4/5.
 */
public class ConsumerAPIs {
    private static final Logger LOG = LoggerFactory.getLogger(ConsumerAPIs.class);
    /**
     * 查询所有消费者
     * @param serverConfig 配置服务器信息和登录用户信息
     * @return
     * @throws ResponseException 抛出响应异常信息，调用者需要捕获并处理
     */
    public static JSONArray findConsumers(ServerConfig serverConfig) throws ResponseException{
        JSONArray result = null;
        try{
            String URL = serverConfig.CONTENT_PATH + "/api/consumers";
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
     * 查询给定虚拟主机下的消费者
     * @param serverConfig 配置服务器信息和登录用户信息
     * @param vhost 虚拟主机 默认虚拟主机是"/"，此处用%2f表示
     * @return
     * @throws ResponseException 抛出响应异常信息，调用者需要捕获并处理
     */
    public static JSONArray findConsumersByVhost(ServerConfig serverConfig, String vhost) throws ResponseException{
        JSONArray result = null;
        try{
            String URL = serverConfig.CONTENT_PATH + "/api/consumers/" + vhost;
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
     * 下面的api是在HTTP API返回的结果上定制的
     */

    /**
     * 查询某虚拟主机下的某队列的消费者数量
     * @param serverConfig 配置服务器信息和登录用户信息
     * @param vhost 虚拟主机 默认虚拟主机是"/"，此处用%2f表示
     * @param queueName
     * @return
     * @throws ResponseException 抛出响应异常信息，调用者需要捕获并处理
     */
    public static int findConsumerCountByQueue(ServerConfig serverConfig, String vhost, String queueName) throws ResponseException{
        JSONObject queue = QueueAPIs.findQueue(serverConfig, vhost, queueName);
        return queue.get("consumers")!=null?Integer.parseInt(queue.get("consumers").toString()):0;
    }
}
