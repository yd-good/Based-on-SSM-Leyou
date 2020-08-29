/*
信息:
*/
package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.item.mapper.SkuMapper;
import com.leyou.item.mapper.SpuDetailMapper;
import com.leyou.item.mapper.SpuMapper;
import com.leyou.item.mapper.StockMapper;
import com.leyou.item.pojo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GoodsService {
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 分页查询spu
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @param key
     * @param saleable
     * @return
     */
    public PageResult<Spu> querySpuByPage(Integer page, Integer rows, String sortBy, Boolean desc, String key, Boolean saleable) {
        PageHelper pageHelper=new PageHelper();
        pageHelper.startPage(page,rows);
        Example example=new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();

        if (StringUtils.isNotBlank(key)){
            criteria.andLike("title", "%" + key + "%");
        }
        //查询是否下架
        if (saleable!=null){
           criteria.andEqualTo("saleable",saleable);
        }
        if (StringUtils.isNotBlank(sortBy)){
            example.setOrderByClause(sortBy+" "+(desc ? "DESC":"ASC"));
        }
        List<Spu> spus=spuMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(spus)){
            throw new LyException(ExceptionEnums.GOODS_NOT_FIND);
        }
        //解析结果
        PageInfo<Spu> pageInfo=new PageInfo<>(spus);
        //解析品牌和分类名称
       queryCategoryAndBrand(spus);
       PageResult pageResult=new PageResult(pageInfo.getTotal(),pageInfo.getList());
        return pageResult;
    }

    /**
     * 根据spu查询所在分类名字和品牌
     * @param spus
     */
    private void queryCategoryAndBrand(List<Spu> spus) {
        for (Spu spu:spus){
            //查询分类名称
            List<String> cname = categoryService.queryCategoryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()))
                    .stream().map(Category::getName).collect(Collectors.toList());
            spu.setCname(StringUtils.join(cname,"/")); //以/分割
            //查询分类名称
            spu.setBname(brandService.queryBrandById(spu.getBrandId()).getName());
        }
    }

    /**
     * 根据spu存储品牌
      * @param spu
     */
    @Transactional
    public void saveGoods(Spu spu) {
        //新增spu
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());
        spu.setSaleable(true);
        spu.setValid(false);
        int count = spuMapper.insert(spu);
        if (count!=1){
            throw new LyException(ExceptionEnums.SAVE_GOODS_ERROR);
        }
        //新增spu_detail
        SpuDetail spuDetail = spu.getSpuDetail();
        spuDetail.setSpuId(spu.getId());
        spuDetailMapper.insert(spuDetail);
        //新增 sku
        List<Sku> skus = spu.getSkus();
        ArrayList<Stock> stocks = new ArrayList<>();
        for (Sku sku:skus){
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            sku.setSpuId(spu.getId());
            count = skuMapper.insert(sku);
            if (count!=1){
                throw new LyException(ExceptionEnums.SAVE_GOODS_ERROR);
            }
            //新增stock
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stocks.add(stock);
        }
        //批量新增
       stockMapper.insertList(stocks);
        //发送mq
       amqpTemplate.convertAndSend("item.insert",spu.getId());
    }

    /**
     * 根据spuId查询spu详情以及Sku
     * @param spuId
     * @return
     */
    public Spu queryAndDetailAndSkuById(Long spuId) {
        Spu spu = spuMapper.selectByPrimaryKey(spuId);
        SpuDetail spuDetail = spuDetailMapper.selectByPrimaryKey(spuId);
//        if (spuDetail==null){
//            throw new LyException(ExceptionEnums.SPU_DETAIL_NOT_FIND);
//        }
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> skus = skuMapper.select(sku);
        List<Long> ids = skus.stream().map(Sku::getId).collect(Collectors.toList());
        List<Stock> stocks = stockMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(stocks)){
            throw new LyException(ExceptionEnums.STOCK_NOT_FIND);
        }
        //将stock的必要属性转为map集合
        Map<Long, Integer> stockMap = stocks.
                                        stream().
                                            collect(Collectors.toMap(Stock::getSkuId, Stock::getStock));
        //将sku的库存存进去
        skus.forEach(s->s.setStock(stockMap.get(s.getId())));
        spu.setSpuDetail(spuDetail);
        spu.setSkus(skus);
        return spu;
    }

    /**
     * spu上下架
     * @param ids
     */
    public void outSpu(List<Long> ids) {
        if (!CollectionUtils.isEmpty(ids)){
            List<Spu> spus = spuMapper.selectByIdList(ids);
            for (int i = 0; i <spus.size() ; i++) {
                if (spus.get(i).getSaleable()==false){
                    spus.get(i).setSaleable(true);
                }else {
                    spus.get(i).setSaleable(false);
                }
                spuMapper.updateByPrimaryKeySelective(spus.get(i));
            }
        }
        }
    }
