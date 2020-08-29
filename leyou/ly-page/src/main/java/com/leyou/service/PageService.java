/*
信息:
*/
package com.leyou.service;

import com.leyou.client.BrandClient;
import com.leyou.client.CategoryClient;
import com.leyou.client.GoodsClient;
import com.leyou.item.pojo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PageService {
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private TemplateEngine templateEngine;
    public Map<String, Object> loadModel(Long spuId) {
        Map<String,Object> model=new HashMap<>();
        Spu spu = goodsClient.queryAndDetailAndSkuById(spuId);
        List<Sku> skus = spu.getSkus();
        SpuDetail spuDetail = spu.getSpuDetail();
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        List<Category> categoryList = categoryClient.queryCategoryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        model.put("title",spu.getTitle());
        model.put("subTitle",spu.getSubTitle());
        model.put("skus",skus);
        model.put("spuDetail",spuDetail);
        model.put("brand",brand);
        model.put("categories",categoryList);
        model.put("specs",null);
        return model;
    }
    public  void createTemplateHtml(Long spuId){
        //获取context用于渲染页面数据
        Context context = new Context();
        context.setVariables(loadModel(141L));
        //创建输出流
        File file=new File("F:\\2018Project1\\item\\"+spuId+".html");
        if (file.exists()){
            file.delete();
        }
        try(PrintWriter writer=new PrintWriter(file)) {
            //获取模板引擎 页面模板（默认在template下）
            templateEngine.process("item",context,writer);
        }catch (Exception e){
            log.error("[静态页面渲染失败]",e);
        }

    }
}
