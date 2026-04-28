package com.ruoyi.system.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping ("/goods/goods") //拼接
//@ResponseBody  //所有的请求返回数据都是 消息体【JSON数据】
@RestController //Rest风格 将请求的资源通过语义化表示 ，根据请求方法的不同实现业务分离
public class GoodsController extends BaseController {
    @Autowired
    private IGoodsService goodsService;
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("@ss.hasPermi('goods:goods:remove')")
    @Log(title = "商品管理", businessType = BusinessType.DELETE)
    public AjaxResult doDelete(@PathVariable Integer id){

        return toAjax(goodsService.shanchujiyuid(id));  //调用具体的service执行业务工作
    }
}
