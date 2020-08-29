package com.leyou.search.repository;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Spu;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.service.SearchService;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsRepositoryTest extends TestCase {
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private ElasticsearchTemplate template;

    /**
     * 创建索引库
     */
    @Test
    public void createIndex(){
        template.createIndex(Goods.class);
        template.putMapping(Goods.class);
    }

    /**
     * 删除索引库
     */
    @Test
    public void deleteIndex(){
        template.deleteIndex(Goods.class);
    }
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SearchService searchService;

    /**
     * 将数据库数据导入elasticsearch中
     */
    @Test
    public void saveElasticSearch(){
        int page=1,rows=100,size=0;
        do {

            PageResult<Spu> spuPageResult = goodsClient.querySpuByPage(page, rows, null, null, null, true);
            List<Spu> items = spuPageResult.getItems();
            if (CollectionUtils.isEmpty(items)){
                break;
            }
            List<Goods> resultGoods = items.stream().map(searchService::buildGoods).collect(Collectors.toList());
            goodsRepository.saveAll(resultGoods);
            page++;
            size=items.size();
        }while (size==100);
    }
}