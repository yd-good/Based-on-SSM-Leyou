package com.leyou.item.api;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Spu;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
@RequestMapping("goods")
public interface GoodsApi {
    /**
     * 查询SPU的详细信息和SKU
     * @param spuId
     * @return
     */
    @GetMapping("spu/{id}")
    Spu queryAndDetailAndSkuById(@PathVariable(name = "id",required = true)Long spuId);
    /**
     * 分页查询SPU
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @param key
     * @param saleable
     * @return
     */
    @GetMapping("spu/page")
    PageResult<Spu> querySpuByPage(
            @RequestParam(name = "page",defaultValue = "1") Integer page,
            @RequestParam(name = "rows",defaultValue = "5") Integer rows,
            @RequestParam(name = "sortBy",required = false) String sortBy,
            @RequestParam(name = "desc",required = false) Boolean desc,
            @RequestParam(name = "key",required = false) String key,
            @RequestParam(name = "saleable",required = false) Boolean saleable
    );
}
