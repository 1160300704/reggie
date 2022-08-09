package com.zhouhao.dto;

import com.zhouhao.entity.OrderDetail;
import com.zhouhao.entity.Orders;
import lombok.Data;
import java.util.List;

@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;
	
}
