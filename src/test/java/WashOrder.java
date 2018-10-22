import com.ebay.xcelite.annotations.Column;
import com.ebay.xcelite.annotations.Row;

import java.io.Serializable;

/**
 * @author heyunxia (love3400wind@163.com)
 * @version 1.0
 * @since 2015-10-21 下午12:31
 */

@Row(colsOrder = {"编号", "姓名", "支付方式", "价格", "支付状态", "支付时间"})
public class WashOrder implements Serializable {


    private static final long serialVersionUID = 8794087388514650389L;

    /**
     * 主键ID
     */
    @Column(name = "编号")
    private String id;
    /**
     * 会员姓名
     */
    @Column(name = "姓名")
    private String name;
    /**
     * 原价
     */
    @Column(name = "价格", converter = PriceValueConverter.class)
    private Integer sellPrice;

    /**
     * 支付方式
     */
    @Column(name = "支付方式", converter = EnumValueConverter.class)
    private String payType;

    /**
     * 支付状态
     */
    @Column(name = "支付状态")
    private Integer payStatus;

    /**
     * 支付时间
     */
    @Column(name = "支付时间", ignoreType = true, converter = DateValueConverter.class)
    private Long payAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Integer sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Long getPayAt() {
        return payAt;
    }

    public void setPayAt(Long payAt) {
        this.payAt = payAt;
    }

    public WashOrder() {
    }

    public WashOrder(String id, String name, Integer sellPrice, String payType, Integer payStatus, Long payAt) {
        this.id = id;
        this.name = name;
        this.sellPrice = sellPrice;
        this.payType = payType;
        this.payStatus = payStatus;
        this.payAt = payAt;
    }
}
