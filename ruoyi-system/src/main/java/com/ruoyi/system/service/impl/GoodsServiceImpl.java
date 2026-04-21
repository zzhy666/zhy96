package com.ruoyi.system.service.impl;
import com.ruoyi.system.domain.Goods;
import com.ruoyi.system.mapper.GoodsMapper;
import com.ruoyi.system.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//给类添加  @Controller  @Service  @Repository  @Component 都是将该类对象添加到IOC容器中
@Service
public class  GoodsServiceImpl implements IGoodsService {
        //@Autowired 就是说该对象去IOC容器自动获取并注入
    @Autowired
    private GoodsMapper goodsMapper = null;//mybatis 生成的mapper对象
    @Override
    public int shanchujiyuid(Integer id) {
        return goodsMapper.deleteById(id);
    }

    @Override
    public Integer zjGoods(Goods goods) {
        return goodsMapper.addGoods(goods);
    }
}
