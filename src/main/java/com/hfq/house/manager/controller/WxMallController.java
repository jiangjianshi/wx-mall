package com.hfq.house.manager.controller;

import com.google.common.collect.Lists;
import com.hfq.house.manager.common.RespMsg;
import com.hfq.house.manager.entity.dto.MallBander;
import com.hfq.house.manager.entity.dto.MallGood;
import com.hfq.house.manager.entity.dto.MallGoodsType;
import com.hfq.house.manager.entity.dto.WxAppInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by bairong on 2018/5/27.
 */

@RestController
@RequestMapping(path = "/mall")
public class WxMallController extends BaseController {


    @RequestMapping(value = "/getSliderList")
    public RespMsg<List<MallBander>> getSliderList() {
        MallBander bander = new MallBander();
        bander.setCid(1);
        bander.setImage("https://www.baidu.com/img/bd_logo1.png");

        List<MallBander> list = Lists.newArrayList();
        list.add(bander);
        list.add(bander);
        list.add(bander);
        list.add(bander);
        return success("成功", list);
    }

    @RequestMapping(value = "/getGoodsList")
    public RespMsg<List<MallGood>> getGoodsList() {
        MallGood good = new MallGood();
        good.setId(1);
        good.setTitle("好标题少商品见覅对方水电费水电费");
        good.setThumb("https://www.baidu.com/img/bd_logo1.png");
        good.setPrice(100);

        List<MallGood> list = Lists.newArrayList();
        list.add(good);
        list.add(good);
        list.add(good);
        list.add(good);
        list.add(good);
        return success("成功", list);
    }

    @RequestMapping(value = "/getWxAppInfo")
    public RespMsg<WxAppInfo> getWxAppInfo() {
        WxAppInfo info = new WxAppInfo();
        info.setTitle("琥珀天地");
        return success("成功", info);
    }

    @RequestMapping(value = "/getGoodsDetail")
    public RespMsg<MallGood> getGoodsDetail() {
        MallGood good = new MallGood();
        good.setId(1);
        good.setTitle("好标题少商品见覅对方水电费水电费");
        good.setThumb("https://www.baidu.com/img/bd_logo1.png");
        good.setPrice(100);
        good.setDescription("琥珀对身体有好处；琥珀对身体有好处；琥珀对身体有好处；琥珀对身体有好处");
        return success("成功", good);
    }

    @RequestMapping(value = "/getGoodsType")
    public RespMsg<List<MallGoodsType>> getGoodsType() {
        MallGoodsType type1 = new MallGoodsType();
        type1.setType(0);
        type1.setTypeName("类型1");
        MallGoodsType type2 = new MallGoodsType();
        type2.setType(1);
        type2.setTypeName("类型2");

        List<MallGoodsType> list = Lists.newArrayList();
        list.add(type1);
        list.add(type2);
        list.add(type2);
        return success("成功", list);
    }
}
