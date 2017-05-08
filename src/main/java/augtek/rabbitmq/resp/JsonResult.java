package augtek.rabbitmq.resp;

import java.io.Serializable;

/**
 * Created by kongyunhui on 2017/4/1.
 */
public class JsonResult implements Serializable {
    private int statusCode;
    private String msg;

    public JsonResult(int statusCode, String msg) {
        this.statusCode = statusCode;
        this.msg = msg;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "JsonResult{" +
                "statusCode=" + statusCode +
                ", msg='" + msg + '\'' +
                '}';
    }
}
