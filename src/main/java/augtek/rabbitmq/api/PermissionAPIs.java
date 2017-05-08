package augtek.rabbitmq.api;

import augtek.rabbitmq.config.ServerConfig;
import augtek.rabbitmq.exception.ResponseException;
import augtek.rabbitmq.utils.HttpHelper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
 * Created by kongyunhui on 2017/4/1.
 *
 * 权限管理
 */
public class PermissionAPIs {
    private static final Logger LOG = LoggerFactory.getLogger(PermissionAPIs.class);
    /**
     * 查看所有用户的权限列表
     * @param serverConfig 配置服务器信息和登录用户信息
     * @return
     */
    public static JSONArray findAllUserPermissions(ServerConfig serverConfig) throws ResponseException {
        JSONArray result = null;
        try{
            String URL = serverConfig.CONTENT_PATH + "/api/permissions";
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
     * 查询某虚拟机下的某个用户的权限
     * @param serverConfig 配置服务器信息和登录用户信息
     * @param vhost 虚拟主机 默认虚拟主机是"/"，此处用%2f表示
     * @param username
     * @return
     * @throws ResponseException 抛出响应异常信息，调用者需要捕获并处理
     */
    public static JSONObject findUserPermission(ServerConfig serverConfig, String vhost, String username) throws ResponseException{
        JSONObject result = null;
        try{
            String URL = serverConfig.CONTENT_PATH + "/api/permissions/"+vhost+"/" + username;
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

    public static boolean deleteUserPermission(ServerConfig serverConfig, String vhost, String username) throws ResponseException{
        boolean isSuccess = false;
        try {
            String URL = serverConfig.CONTENT_PATH + "/api/permissions/"+vhost+"/" + username;
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpDelete httpDelete = new HttpDelete(URL);
            HttpHelper.setupCredential(httpDelete, serverConfig.CREDENTIAL);
            CloseableHttpResponse response = httpClient.execute(httpDelete);
            if(HttpHelper.handlerResponse(response) instanceof Boolean){
                isSuccess = true;
            }
        }catch(IOException e){
            LOG.error("error: {}", e.getMessage());
        }
        return isSuccess;
    }

    /**
     * 授予虚拟主机中某个用户的个人权限
     * @param serverConfig 配置服务器信息和登录用户信息
     * @param vhost 虚拟主机 默认虚拟主机是"/"，此处用%2f表示
     * @param username
     * @param configure
     * @param write
     * @param read
     * @return
     */
    public static boolean createUserPermission(ServerConfig serverConfig, String vhost, String username, String configure, String write, String read) throws ResponseException{
        boolean isSuccess = false;
        try{
            String URL = serverConfig.CONTENT_PATH + "/api/permissions/"+vhost+"/"+username;
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPut httpPut = new HttpPut(URL);
            HttpHelper.setupCredential(httpPut, serverConfig.CREDENTIAL);
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("configure", configure);
            jsonParam.put("write", write);
            jsonParam.put("read", read);
            HttpHelper.setJsonParams(httpPut, jsonParam);
            CloseableHttpResponse response = client.execute(httpPut);
            if(HttpHelper.handlerResponse(response) instanceof Boolean){
                isSuccess = true;
            }
        }catch(IOException e){
            LOG.error("error: {}", e.getMessage());
        }
        return isSuccess;
    }


}
