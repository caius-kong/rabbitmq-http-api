package augtek.rabbitmq.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kongyunhui on 2017/4/1.
 * 连接rabbitmq-server参数配置，已经登录用户信息
 */
public class ServerConfig {
    public String CONTENT_PATH = null;
    public Map<String, String> CREDENTIAL = null;

    private String host; // rabbit服务器主机ip
    private Integer port; // rabbit服务器端口号
    private String username; // 登录rabbit服务器的用户名
    private String password; // 登录rabbit服务器的密码

    public ServerConfig(String host, Integer port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;

        CONTENT_PATH = "http://" + host + ":" + port;
        CREDENTIAL = new HashMap<String, String>();
        CREDENTIAL.put("username", username);
        CREDENTIAL.put("password", password);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
