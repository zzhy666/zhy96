package com.ruoyi.system.service;

import com.ruoyi.system.domain.Goods;

public interface IGoodsService {
    public int shanchujiyuid(Integer id);//业务层的整合业务通常是调用多个Mapper方法
    public Integer zjGoods(Goods goods);
}
