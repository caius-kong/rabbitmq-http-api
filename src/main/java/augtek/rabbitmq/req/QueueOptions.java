package augtek.rabbitmq.req;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by kongyunhui on 2017/4/5.
 *
 * 创建一个队列时的可选参数
 */
public class QueueOptions {
    private boolean auto_delete; // 当所有消费客户端连接断开后，是否自动删除队列
    private boolean durable; // 是否持久化，一般设置为true
    private JSONObject arguments;
    private String node; // rabbit集群中的节点

    public boolean isAuto_delete() {
        return auto_delete;
    }

    public void setAuto_delete(boolean auto_delete) {
        this.auto_delete = auto_delete;
    }

    public boolean isDurable() {
        return durable;
    }

    public void setDurable(boolean durable) {
        this.durable = durable;
    }

    public JSONObject getArguments() {
        return arguments;
    }

    public void setArguments(JSONObject arguments) {
        this.arguments = arguments;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    @Override
    public String toString() {
        return "QueueOptions{" +
                "auto_delete=" + auto_delete +
                ", durable=" + durable +
                ", arguments=" + arguments +
                ", node='" + node + '\'' +
                '}';
    }
}
