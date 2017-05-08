package augtek.rabbitmq.exception;

/**
 * Created by kongyunhui on 2017/4/1.
 */
public class ResponseException extends Exception{
    private String message;
    public ResponseException(String message){
        this.message = message;
    }

    public String getMessage(){
        return this.message;
    }

    @Override
    public String toString() {
        return "ResponseException{" +
                "message='" + message + '\'' +
                '}';
    }
}
