package augtek.rabbitmq.constant;

/**
 * Created by kongyunhui on 2017/4/6.
 */
public enum ExchangeTypeEnum {
    FANOUT("fanout"), // 扇形交换机
    DIRECT("direct"), // 直接交换机
    TOPIC("topic"), // 主题交换机
    HEADER("header"); // 和direct类似，根据header进行分组交换

    private String type;
    ExchangeTypeEnum(String type){
        this.type = type;
    }
    public String value(){
        return this.type;
    }
}
