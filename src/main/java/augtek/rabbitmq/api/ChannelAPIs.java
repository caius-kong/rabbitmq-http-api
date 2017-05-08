package augtek.rabbitmq.api;

import augtek.rabbitmq.config.ServerConfig;
import augtek.rabbitmq.exception.ResponseException;
import augtek.rabbitmq.utils.HttpHelper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by kongyunhui on 2017/4/5.
 *
 * 通道管理
 */
public class ChannelAPIs {
    private static final Logger LOG = LoggerFactory.getLogger(ChannelAPIs.class);

    /**
     * 打开通道的所有列表
     * @param serverConfig 配置服务器信息和登录用户信息
     * @return
     * @throws ResponseException 抛出响应异常信息，调用者需要捕获并处理
     */
    public static JSONArray findChannels(ServerConfig serverConfig) throws ResponseException {
        JSONArray result = null;
        try{
            String URL = serverConfig.CONTENT_PATH + "/api/channels";
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
     * 打开单个通道
     * @param serverConfig 配置服务器信息和登录用户信息
     * @param channelName
     * @return
     * @throws ResponseException 抛出响应异常信息，调用者需要捕获并处理
     */
    public static JSONObject findChannel(ServerConfig serverConfig, String channelName) throws ResponseException {
        JSONObject result = null;
        try{
            String URL = serverConfig.CONTENT_PATH + "/api/channels/" + channelName;
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
     * 特定虚拟主机下的打开连接的通道
     * @param serverConfig 配置服务器信息和登录用户信息
     * @return
     * @throws ResponseException 抛出响应异常信息，调用者需要捕获并处理
     */
    public static JSONArray findChannelsByVhost(ServerConfig serverConfig, String vhost) throws ResponseException {
        JSONArray result = null;
        try{
            String URL = serverConfig.CONTENT_PATH + "/api/vhosts/"+vhost+"/channels";
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
     * 给定连接的所有通道列表
     * @param serverConfig 配置服务器信息和登录用户信息
     * @return
     * @throws ResponseException 抛出响应异常信息，调用者需要捕获并处理
     */
    public static JSONArray findChannelsByConnection(ServerConfig serverConfig, String connectionName) throws ResponseException {
        JSONArray result = null;
        try{
            String URL = serverConfig.CONTENT_PATH + "/api/connections/"+connectionName+"/channels";
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
}
