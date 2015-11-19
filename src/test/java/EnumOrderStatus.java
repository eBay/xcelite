/**
 * @author heyunxia.
 * @Description 订单状态
 * @time 2015/5/21 17:39
 */
public enum EnumOrderStatus {

    PAYED(11, "已支付，待分配"),
    CANCELBY_CUSTOMER(12, "用户取消"),
    CANCELBY_STAFF(13, "调度人员取消"),
    EXPIRED(14, "已过期"),
    ASSIGNED(17, "已分配"),
    FINISH_WASHING(19, "师傅洗车完成"),
    UNKONWN(999, "未知状态");

    private final int code;
    private final String reason;

    EnumOrderStatus(int statusCode, String reasonPhrase) {
        this.code = statusCode;
        this.reason = reasonPhrase;

    }

    public static EnumOrderStatus fromStatusCode(int statusCode) {
        for (EnumOrderStatus item : values()) {
            if (item.code == statusCode) {
                return item;
            }
        }

        return UNKONWN;
    }

    public int getStatusCode() {
        return this.code;
    }

    public String toString() {
        return this.reason;
    }

}
