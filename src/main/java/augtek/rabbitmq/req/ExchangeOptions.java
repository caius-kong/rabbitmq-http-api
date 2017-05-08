package augtek.rabbitmq.req;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by kongyunhui on 2017/4/5.
 *
 * 创建交换机时的可选参数
 */
public class ExchangeOptions {
    private boolean auto_delete; // 当所有绑定队列都不再使用时，是否自动删除该交换机
    private boolean durable; // 是否持久化，一般设置为true
    private boolean internal;
    private JSONObject arguments;

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

    public boolean isInternal() {
        return internal;
    }

    public void setInternal(boolean internal) {
        this.internal = internal;
    }

    public JSONObject getArguments() {
        return arguments;
    }

    public void setArguments(JSONObject arguments) {
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        return "ExchangeOptions{" +
                "auto_delete=" + auto_delete +
                ", durable=" + durable +
                ", internal=" + internal +
                ", arguments=" + arguments +
                '}';
    }
}
