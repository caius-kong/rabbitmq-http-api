package augtek.rabbitmq.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import augtek.rabbitmq.config.ServerConfig;
import augtek.rabbitmq.exception.ResponseException;
import augtek.rabbitmq.utils.HttpHelper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by kongyunhui on 2017/3/31.
 *
 * 虚拟主机管理
 * 作用：不同用户权限分离
 * 注：默认vhost=/，在http api中使用%2f表示 --> URLEncoder.encode("/", "utf-8")
 )
 */
public class VhostAPIs {
    private static final Logger LOG = LoggerFactory.getLogger(VhostAPIs.class);
    /**
     * 查询所有虚拟主机列表
     * @param serverConfig 配置服务器信息和登录用户信息
     * @return
     * @throws ResponseException 抛出响应异常信息，调用者需要捕获并处理
     */
    public static JSONArray findAllVhost(ServerConfig serverConfig) throws ResponseException{
        JSONArray result = null;
        try{
            String URL = serverConfig.CONTENT_PATH + "/api/vhosts";
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

    public static JSONObject findVhost(ServerConfig serverConfig, String vhost) throws ResponseException{
        JSONObject result = null;
        try{
            String URL = serverConfig.CONTENT_PATH + "/api/vhosts/" +vhost;
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
     * 创建一个虚拟主机（vhost）
     * @param serverConfig 配置服务器信息和登录用户信息
     * @param vhost 虚拟主机 默认虚拟主机是"/"，此处用%2f表示
     * @param tracing 启用|禁用跟踪 (建议开启)
     * @return boolean
     */
    public static boolean createVhost(ServerConfig serverConfig, String vhost, boolean tracing) throws ResponseException{
        boolean isSuccess = false;
        try {
            String URL = serverConfig.CONTENT_PATH + "/api/vhosts/" + vhost;
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPut httpPut = new HttpPut(URL);
            HttpHelper.setupCredential(httpPut, serverConfig.CREDENTIAL); // 设置凭证
            JSONObject jsonParam = new JSONObject(); // 设置jsonParam
            jsonParam.put("tracing", tracing);
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
     * 删除一个vhost
     * @param serverConfig 配置服务器信息和登录用户信息
     * @param vhost 虚拟主机 默认虚拟主机是"/"，此处用%2f表示
     * @return
     * @throws ResponseException 抛出响应异常信息，调用者需要捕获并处理
     */
    public static boolean deleteVhost(ServerConfig serverConfig, String vhost) throws ResponseException{
        boolean isSuccess = false;
        try{
            String URL = serverConfig.CONTENT_PATH + "/api/vhosts/" + vhost;
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
     * 查询指定虚拟主机的所有权限列表
     * @param serverConfig 配置服务器信息和登录用户信息
     * @param vhost 虚拟主机 默认虚拟主机是"/"，此处用%2f表示
     * @return
     * @throws ResponseException 抛出响应异常信息，调用者需要捕获并处理
     */
    public static JSONArray findPermissionsByVhost(ServerConfig serverConfig, String vhost) throws ResponseException{
        JSONArray result = null;
        try{
            String URL = serverConfig.CONTENT_PATH + "/api/vhosts/"+vhost+"/permissions";
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
