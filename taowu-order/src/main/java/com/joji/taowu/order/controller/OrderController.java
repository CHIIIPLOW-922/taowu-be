package com.joji.taowu.order.controller;

import com.joji.taowu.common.param.OrderParam;
import com.joji.taowu.common.param.PageParam;
import com.joji.taowu.common.param.ProductSearchParam;
import com.joji.taowu.order.service.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 商城订单服务控制器
 * */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    /**
     * 订单数据保存
     * @param orderParam
     * @return
     */
    @PostMapping("save")
    public Object save(@RequestBody OrderParam orderParam){


        return orderService.save(orderParam);
    }


    /**
     * 订单集合查询,注意,按照类别查询!
     * @param orderParam
     * @return
     */
    @PostMapping("/list")
    public Object list(@RequestBody OrderParam orderParam){

        return orderService.list(orderParam);
    }


    /**
     * 检查订单是否包含要删除的商品
     * @param productId
     * @return
     */
    @PostMapping("/check")
    public  Object check(@RequestBody Integer productId){
        return orderService.check(productId);
    }


    @PostMapping("/admin/list")
    public Object adminList(@RequestBody ProductSearchParam productSearchParam){

        return orderService.adminList(productSearchParam);
    }
}
