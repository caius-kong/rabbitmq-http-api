package augtek.rabbitmq.constant;

/**
 * Created by kongyunhui on 2017/4/1.
 */
public enum RoleEnum {
    ADMIN("administrator"),
    MONITOR("monitoring"),
    POLICYMAKER("policymaker"),
    MANAGEMENT("management"),
    None("");

    private String role;
    RoleEnum(String role){
        this.role = role;
    }
    public String value(){
        return this.role;
    }
}
