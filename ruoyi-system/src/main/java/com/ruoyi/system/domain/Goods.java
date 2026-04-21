package com.ruoyi.system.domain;

public class Goods {
    //实体类 目的：封装数据
    //实体类的特点：1. 私有属性 2. 所有属性都有对应的getter和setter方法 3. 覆写 toSting（） 方法
    private  Integer goodsId;//因为数据库不识别大小写，数据库中组件使用_分割 ,MyBastic通过配置可以将_转换成小驼峰命名法
    private  String goodsName;
    private  Double price;
    //getter,setter, toString生成完成

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "goodsId=" + goodsId +
                ", goodsName='" + goodsName + '\'' +
                ", price=" + price +
                '}';
    }
}
