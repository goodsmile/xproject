package com.certusnet.xproject.common.support;

/**
 * 通用排序对象
 * 
 * @author	  	pengpeng
 * @date	  	2014年7月28日 下午9:21:55
 * @version  	1.0
 */
public class OrderBy {

    public static final String ORDER_ASC = "asc";

    public static final String ORDER_DESC = "desc";

    /**
     * 排序字段
     */
    private String orderby;

    /**
     * 排序类型: asc/desc
     */
    private String order = ORDER_ASC;

    public OrderBy() {
        super();
    }

    public OrderBy(String orderby, String order) {
        super();
        setOrderby(orderby);
        setOrder(order);
    }


    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
    	this.orderby = orderby;
    }
    
    public String getOrder() {
        return order;
    }

	public void setOrder(String order) {
		if(order != null && !ORDER_DESC.equalsIgnoreCase(order)){
			this.order = ORDER_ASC;
		}else{
			this.order = ORDER_DESC;
		}
	}

}
