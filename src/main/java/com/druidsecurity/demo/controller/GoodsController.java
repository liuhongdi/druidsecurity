package com.druidsecurity.demo.controller;

import com.druidsecurity.demo.mapper.GoodsMapper;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentMap;

import com.druidsecurity.demo.pojo.Goods;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Resource
    private GoodsMapper goodsMapper;

    //商品详情 参数:商品id
    @GetMapping("/goodsinfo")
    @ResponseBody
    public Goods goodsInfo(@RequestParam(value="goodsid",required = true,defaultValue = "0") Long goodsId) {
        Goods goods = goodsMapper.selectOneGoods(goodsId);
        return goods;
    }

    @GetMapping("/session")
    @ResponseBody
    public String session() {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        Enumeration e   =   session.getAttributeNames();
        String s = "";
        while( e.hasMoreElements())   {
            String sessionName=(String)e.nextElement();
            s += "name="+sessionName+";<br/>";
            s += "value="+session.getAttribute(sessionName)+";";
        }
        return s;
    }

}

