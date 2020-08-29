/*
信息:
*/
package com.leyou.search.service;

import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.JsonUtils;
import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.repository.GoodsRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;
@Slf4j
@Service
public class SearchService {
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecificationClient specificationClient;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private ElasticsearchTemplate template;
    public Goods buildGoods(Spu spu) {
        List<Category> categoryList = categoryClient.queryCategoryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        List<String> names = categoryList.stream().map(Category::getName).collect(Collectors.toList());
        Goods goods = new Goods();
        /**
         * 标题+分类+品牌+规格
         */
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        if (brand == null) {
            throw new LyException(ExceptionEnums.BRAND_NOT_FIND);
        }
        String all = spu.getTitle() + StringUtils.join(names, " ") + brand.getName() + "";
        /**
         * 查询sku和SpuDetail
         */
        Spu newSpu = goodsClient.queryAndDetailAndSkuById(spu.getId());
       if (newSpu.getSkus()!=null) {
           List<Sku> skus = newSpu.getSkus();
           Set<Long> priceSet = new HashSet<>();
           /**
            * 改造sku
            */
           List<Map<String, Object>> skuList = new ArrayList<>();
           for (Sku sku : skus) {
               Map<String, Object> map = new HashMap<>();
               map.put("id", sku.getId());
               map.put("title", sku.getTitle());
               map.put("price", sku.getPrice());
               map.put("image", StringUtils.substringBefore(sku.getImages(), ","));
               skuList.add(map);
               //处理价格
               priceSet.add(sku.getPrice());
           }
           goods.setSkus(JsonUtils.serialize(skuList));
           goods.setPrice(priceSet);
       }
       if (newSpu.getSpuDetail()!=null) {
           /**
            * 获取通用规格参数
            */
           String commonSpecifications = newSpu.getSpuDetail().getSpecifications();
           /**
            * 获取特有规格参数
            */
           String particularSpecifications = newSpu.getSpuDetail().getSpecTemplate();
           goods.setSpecs(commonSpecifications);
       }
    goods.setBrandId(spu.getBrandId());
    goods.setCid1(spu.getCid1());
    goods.setCid2(spu.getCid2());
    goods.setCid3(spu.getCid3());
    goods.setCreateTime(spu.getCreateTime());
    goods.setId(spu.getId());
    goods.setAll(all);//TODO 标题+分类+品牌+规格
    goods.setSubTitle(spu.getSubTitle());
    return goods;
    }

    public SearchResult search(SearchRequest searchRequest) {
        int page=searchRequest.getPage() - 1;
        int size=searchRequest.getSize();
        String sortBy=searchRequest.getSortBy();
        Boolean desc=searchRequest.getDescending();
        /**
         * 构建查询构造器
         */
        NativeSearchQueryBuilder queryBuilder=new NativeSearchQueryBuilder();
        //分页
        queryBuilder.withPageable(PageRequest.of(page,size));
        //返回查询条件
        QueryBuilder basicQuery=basicQuery(searchRequest);
        queryBuilder.withQuery(basicQuery);
        //排序
        if(StringUtils.isNotBlank(sortBy)){
            queryBuilder.withSort(SortBuilders.fieldSort(sortBy).order(desc? SortOrder.DESC:SortOrder.ASC));
        }
        //聚合查询
        String aggCategoryName="agg_category";
        String aggBrandName="agg_brand";
        queryBuilder.addAggregation(AggregationBuilders.terms(aggCategoryName).field("cid3"));
        queryBuilder.addAggregation(AggregationBuilders.terms(aggBrandName).field("brandId"));
        //结果过滤
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","subTitle","skus"},null));
        //查询结果
        AggregatedPage<Goods> result = template.queryForPage(queryBuilder.build(), Goods.class);
        //解析结果
        long totalElements = result.getTotalElements();
        long totalPages=totalElements%size==0?totalElements/size:totalElements/size+1;
        List<Goods> content = result.getContent();
        //解析桶
        Aggregations aggregations = result.getAggregations();
        List<Category> categoryList = parseCategory(aggregations.get(aggCategoryName));
        List<Brand> brands = parseBrand(aggregations.get(aggBrandName));
        //处理参数结果
        if(CollectionUtils.isEmpty(categoryList)){

        }
        return new SearchResult(totalElements,totalPages,content,categoryList,brands);
    }

    private QueryBuilder basicQuery(SearchRequest searchRequest) {
       //创建bool查询
       BoolQueryBuilder queryBuilder= QueryBuilders.boolQuery();
       //基本查询
       queryBuilder.must(QueryBuilders.matchQuery("all",searchRequest.getKey()));
        Map<String, String> filter = searchRequest.getFilter();
        for (Map.Entry<String, String> entry : filter.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (!"cid3".equals(key) &&!"brandId".equals(key)){
                key="specm"+key+"keyword";
            }
            //过滤查询
            queryBuilder.filter(QueryBuilders.termQuery(key,value));
        }
        return queryBuilder;
    }

    private List<Brand> parseBrand(LongTerms aggregation) {
        try {
            List<Long> ids = aggregation.getBuckets().stream().map(bucket -> bucket.getKeyAsNumber().longValue()).collect(Collectors.toList());
            List<Brand> brands = brandClient.queryBrandByIds(ids);
            return brands;
        }catch (Exception e){
            log.error("查询商品出错",e);
        }
        return null;
    }
    private List<Category> parseCategory(LongTerms aggregation) {
      try {
          List<Long> cids = aggregation.getBuckets().stream().map(bucket -> bucket.getKeyAsNumber().longValue()).collect(Collectors.toList());
          List<Category> categoryList = categoryClient.queryCategoryByIds(cids);
          return categoryList;
      }catch (Exception e){
          log.error("查询品牌出错",e);
      }
      return null;
    };

    public void createOrUpdateIndex(Long spuId) {
        Spu spu = goodsClient.queryAndDetailAndSkuById(spuId);
        Goods goods = this.buildGoods(spu);
        this.goodsRepository.save(goods);
    }
}
