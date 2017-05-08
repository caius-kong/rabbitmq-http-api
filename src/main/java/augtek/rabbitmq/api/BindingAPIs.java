package augtek.rabbitmq.api;

import augtek.rabbitmq.utils.HttpHelper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import augtek.rabbitmq.config.ServerConfig;
import augtek.rabbitmq.exception.ResponseException;
import augtek.rabbitmq.req.BindingOptions;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by kongyunhui on 2017/4/5.
 *
 * 绑定管理
 */
public class BindingAPIs {
    private static final Logger LOG = LoggerFactory.getLogger(BindingAPIs.class);

    /**
     * 查询所有绑定列表
     * @param serverConfig 配置服务器信息和登录用户信息
     * @return
     * @throws ResponseException 抛出响应异常信息，调用者需要捕获并处理
     */
    public static JSONArray findBindings(ServerConfig serverConfig) throws ResponseException{
        JSONArray result = null;
        try{
            String URL = serverConfig.CONTENT_PATH + "/api/bindings";
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
     * 查询特定虚拟主机中所有绑定列表
     * @param serverConfig 配置服务器信息和登录用户信息
     * @param vhost 虚拟主机 默认虚拟主机是"/"，此处用%2f表示
     * @return
     * @throws ResponseException 抛出响应异常信息，调用者需要捕获并处理
     */
    public static JSONArray findBindingsByVhost(ServerConfig serverConfig, String vhost) throws ResponseException{
        JSONArray result = null;
        try{
            String URL = serverConfig.CONTENT_PATH + "/api/bindings/"+vhost;
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
     * 查询交换机与队列间的绑定列表
     * @param serverConfig 配置服务器信息和登录用户信息
     * @param vhost 虚拟主机 默认虚拟主机是"/"，此处用%2f表示
     * @param exchangeName
     * @param queueName
     * @return
     * @throws ResponseException 抛出响应异常信息，调用者需要捕获并处理
     */
    public static JSONArray findBindingsBetweenXAndQ(ServerConfig serverConfig, String vhost, String exchangeName, String queueName) throws ResponseException{
        JSONArray result = null;
        try{
            String URL = serverConfig.CONTENT_PATH + "/api/bindings/"+vhost+"/e/"+exchangeName+"/q/"+queueName;
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
     * 创建一个新的绑定(交换机与队列间) （X:Q可以多次绑定，key不同就行）
     * @param serverConfig 配置服务器信息和登录用户信息
     * @param vhost 虚拟主机 默认虚拟主机是"/"，此处用%2f表示
     * @param exchangeName
     * @param queueName
     * @param options 绑定参数 必须设置属性routing_key，即设置路由键
     * @return 返回binding url (Note. The response will contain a Location header telling you the URI of your new binding.)
     * @throws ResponseException 抛出响应异常信息，调用者需要捕获并处理
     */
    public static String createBindingBetweenXAndQ(ServerConfig serverConfig, String vhost, String exchangeName, String queueName, BindingOptions options) throws ResponseException{
        String result = null;
        try{
            String URL = serverConfig.CONTENT_PATH + "/api/bindings/"+vhost+"/e/"+exchangeName+"/q/"+queueName;
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(URL);
            HttpHelper.setupCredential(httpPost, serverConfig.CREDENTIAL);
            JSONObject jsonParams = new JSONObject();
            if(options !=null) {
                jsonParams.put("routing_key", options.getRouting_key());
                jsonParams.put("arguments", options.getArguments()!=null?options.getArguments():new JSONObject());
            }
            HttpHelper.setJsonParams(httpPost, jsonParams);
            CloseableHttpResponse response = client.execute(httpPost);
            result = HttpHelper.handlerResponse(response).toString();
        }catch(IOException e){
            LOG.error("error: {}", e.getMessage());
        }
        return result;
    }

    /**
     * 查询一个绑定 (交换机与队列间)
     * @param serverConfig 配置服务器信息和登录用户信息
     * @param vhost 虚拟主机 默认虚拟主机是"/"，此处用%2f表示
     * @param exchangeName
     * @param queueName
     * @param props 由routing key和arguments散列组成的绑定名 (无args即key) --> createBinding返回binding url，最后一个/后面就是props
     * @return
     * @throws ResponseException 抛出响应异常信息，调用者需要捕获并处理
     */
    public static JSONObject findBindingBetweenXAndQ(ServerConfig serverConfig, String vhost, String exchangeName, String queueName, String props) throws ResponseException{
        JSONObject result = null;
        try{
            String URL = serverConfig.CONTENT_PATH + "/api/bindings/"+vhost+"/e/"+exchangeName+"/q/"+queueName+"/"+props;
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
     * 删除一个绑定 (交换机与队列间)
     * @param serverConfig 配置服务器信息和登录用户信息
     * @param vhost 虚拟主机 默认虚拟主机是"/"，此处用%2f表示
     * @param exchangeName
     * @param queueName
     * @param props 由routing key和arguments散列组成的绑定名 (无args即key)  --> createBinding返回binding url，最后一个/后面就是props
     * @return
     * @throws ResponseException 抛出响应异常信息，调用者需要捕获并处理
     */
    public static boolean deleteBindingBetweenXAndQ(ServerConfig serverConfig, String vhost, String exchangeName, String queueName, String props) throws ResponseException{
        boolean isSuccess = false;
        try{
            String URL = serverConfig.CONTENT_PATH + "/api/bindings/"+vhost+"/e/"+exchangeName+"/q/"+queueName+"/"+props;
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
     * 和以上4个方法类似的，2个交换机之间的绑定
     */

    /**
     * 查询两个交换机间的绑定列表
     * @param serverConfig 配置服务器信息和登录用户信息
     * @param vhost 虚拟主机 默认虚拟主机是"/"，此处用%2f表示
     * @param exchangeName
     * @param otherExchangeName
     * @return
     * @throws ResponseException 抛出响应异常信息，调用者需要捕获并处理
     */
    public static JSONArray findBindingsBetweenXAndX(ServerConfig serverConfig, String vhost, String exchangeName, String otherExchangeName) throws ResponseException{
        JSONArray result = null;
        try{
            String URL = serverConfig.CONTENT_PATH + "/api/bindings/"+vhost+"/e/"+exchangeName+"/e/"+otherExchangeName;
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
     * 创建一个新的绑定 (两个交换机间)
     * @param serverConfig 配置服务器信息和登录用户信息
     * @param vhost 虚拟主机 默认虚拟主机是"/"，此处用%2f表示
     * @param exchangeName
     * @param otherExchangeName
     * @param options 可选参数
     * @return 返回binding url (Note. The response will contain a Location header telling you the URI of your new binding.)
     * @throws ResponseException 抛出响应异常信息，调用者需要捕获并处理
     */
    public static String createBindingBetweenXAndX(ServerConfig serverConfig, String vhost, String exchangeName, String otherExchangeName, BindingOptions options) throws ResponseException{
        String result = null;
        try{
            String URL = serverConfig.CONTENT_PATH + "/api/bindings/"+vhost+"/e/"+exchangeName+"/e/"+otherExchangeName;
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(URL);
            HttpHelper.setupCredential(httpPost, serverConfig.CREDENTIAL);
            JSONObject jsonParams = new JSONObject();
            if(options !=null) {
                jsonParams.put("routing_key", options.getRouting_key());
                jsonParams.put("arguments", options.getArguments()!=null?options.getArguments():new JSONObject());
            }
            HttpHelper.setJsonParams(httpPost, jsonParams);
            CloseableHttpResponse response = client.execute(httpPost);
            result = HttpHelper.handlerResponse(response).toString();
        }catch(IOException e){
            LOG.error("error: {}", e.getMessage());
        }
        return result;
    }

    /**
     * 查询一个绑定 (两个交换机间)
     * @param serverConfig 配置服务器信息和登录用户信息
     * @param vhost 虚拟主机 默认虚拟主机是"/"，此处用%2f表示
     * @param exchangeName
     * @param otherExchangeName
     * @param props 由routing key和arguments散列组成的绑定名 (无args即key) --> createBinding返回binding url，最后一个/后面就是props
     * @return
     * @throws ResponseException 抛出响应异常信息，调用者需要捕获并处理
     */
    public static JSONObject findBindingBetweenXAndX(ServerConfig serverConfig, String vhost, String exchangeName, String otherExchangeName, String props) throws ResponseException{
        JSONObject result = null;
        try{
            String URL = serverConfig.CONTENT_PATH + "/api/bindings/"+vhost+"/e/"+exchangeName+"/e/"+otherExchangeName;
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
     * 删除一个绑定 (两个交换机间)
     * @param serverConfig 配置服务器信息和登录用户信息
     * @param vhost 虚拟主机 默认虚拟主机是"/"，此处用%2f表示
     * @param exchangeName
     * @param otherExchangeName
     * @param props 由routing key和arguments散列组成的绑定名 (无args即key)  --> createBinding返回binding url，最后一个/后面就是props
     * @return
     * @throws ResponseException 抛出响应异常信息，调用者需要捕获并处理
     */
    public static boolean deleteBindingBetweenXAndX(ServerConfig serverConfig, String vhost, String exchangeName, String otherExchangeName, String props) throws ResponseException{
        boolean isSuccess = false;
        try{
            String URL = serverConfig.CONTENT_PATH + "/api/bindings/"+vhost+"/e/"+exchangeName+"/e/"+otherExchangeName;
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


}
