package augtek.rabbitmq.req;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by kongyunhui on 2017/4/5.
 * 创建新的绑定时的可选参数
 */
public class BindingOptions {
    private String routing_key; // 路由键。交换机与队列之间通过 routing key 进行消息路由。
    private JSONObject arguments;

    public String getRouting_key() {
        return routing_key;
    }

    public void setRouting_key(String routing_key) {
        this.routing_key = routing_key;
    }

    public JSONObject getArguments() {
        return arguments;
    }

    public void setArguments(JSONObject arguments) {
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        return "BindingOptions{" +
                "routing_key='" + routing_key + '\'' +
                ", arguments=" + arguments +
                '}';
    }
}
