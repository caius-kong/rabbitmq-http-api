package augtek.rabbitmq.api;

import augtek.rabbitmq.utils.HttpHelper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import augtek.rabbitmq.config.ServerConfig;
import augtek.rabbitmq.exception.ResponseException;
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
 * 用户管理
 */
public class UserAPIs {
    private static final Logger LOG = LoggerFactory.getLogger(UserAPIs.class);
    /**
     * 获取用户列表
     * @param serverConfig 配置服务器信息和登录用户信息
     * @return JSONArray
     */
    public static JSONArray findAllUsers(ServerConfig serverConfig) throws ResponseException{
        JSONArray jsonArray = null;
        try {
            String URL = serverConfig.CONTENT_PATH + "/api/users";
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(URL);
            HttpHelper.setupCredential(httpGet, serverConfig.CREDENTIAL); // 设置凭证
            CloseableHttpResponse response = httpClient.execute(httpGet); // 执行请求
            jsonArray = (JSONArray) HttpHelper.handlerResponse(response); // 处理响应
        }catch(IOException e){
            LOG.error("error: {}", e.getMessage());
        }
        return jsonArray;
    }

    /**
     * 查询指定用户
     * @param serverConfig 配置服务器信息和登录用户信息
     * @param username
     * @return
     * @throws ResponseException 抛出响应异常信息，调用者需要捕获并处理
     */
    public static JSONObject findUser(ServerConfig serverConfig, String username) throws ResponseException{
        JSONObject jsonObject = null;
        try {
            String URL = serverConfig.CONTENT_PATH + "/api/users/" + username;
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(URL);
            HttpHelper.setupCredential(httpGet, serverConfig.CREDENTIAL);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            jsonObject = (JSONObject) HttpHelper.handlerResponse(response);
        }catch(IOException e){
            LOG.error("error: {}", e.getMessage());
        }
        return jsonObject;
    }


    /**
     * 新建用户 (如果username存在，则更新)
     * @param serverConfig 配置服务器信息和登录用户信息
     * @param username
     * @param password
     */
    public static boolean createUser(ServerConfig serverConfig, String username, String password, String role) throws ResponseException{
        boolean isSuccess = false;
        try {
            String URL = serverConfig.CONTENT_PATH + "/api/users/" + username;
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPut httpPut = new HttpPut(URL);
            HttpHelper.setupCredential(httpPut, serverConfig.CREDENTIAL);
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("password", password);
            jsonParam.put("tags", role);
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
     * 删除一个用户
     * @param serverConfig 配置服务器信息和登录用户信息
     * @param username
     * @return
     */
    public static boolean deleteUser(ServerConfig serverConfig, String username) throws ResponseException{
        boolean isSuccess = false;
        try {
            String URL = serverConfig.CONTENT_PATH + "/api/users/" + username;
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
     * 查询某个用户的权限列表 (所有虚拟机下，因此是JSONArray)
     * @param serverConfig 配置服务器信息和登录用户信息
     * @param username
     * @return
     */
    public static JSONArray findUserPermissions(ServerConfig serverConfig, String username) throws ResponseException{
        JSONArray result = null;
        try{
            String URL = serverConfig.CONTENT_PATH + "/api/users/"+username+"/permissions";
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
     * 查询当前认证用户的详细
     * @param serverConfig 配置服务器信息和登录用户信息
     * @return
     * @throws ResponseException 抛出响应异常信息，调用者需要捕获并处理
     */
    public static JSONObject findCurrentAuthenticatedUser(ServerConfig serverConfig) throws ResponseException{
        JSONObject jsonObject = null;
        try {
            String URL = serverConfig.CONTENT_PATH + "/api/whoami";
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(URL);
            HttpHelper.setupCredential(httpGet, serverConfig.CREDENTIAL);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            jsonObject = (JSONObject) HttpHelper.handlerResponse(response);
        }catch(IOException e){
            LOG.error("error: {}", e.getMessage());
        }
        return jsonObject;
    }
}
