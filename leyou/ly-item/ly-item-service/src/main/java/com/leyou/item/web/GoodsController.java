/*
信息:
*/
package com.leyou.item.web;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Spu;
import com.leyou.item.service.GoodsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("goods")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

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
    public ResponseEntity<PageResult<Spu>> querySpuByPage(
            @RequestParam(name = "page",defaultValue = "1") Integer page,
            @RequestParam(name = "rows",defaultValue = "5") Integer rows,
            @RequestParam(name = "sortBy",required = false) String sortBy,
            @RequestParam(name = "desc",required = false) Boolean desc,
            @RequestParam(name = "key",required = false) String key,
            @RequestParam(name = "saleable",required = false) Boolean saleable
            ){
     return ResponseEntity.ok(goodsService.querySpuByPage(page,rows,sortBy,desc,key,saleable));
    }
    @PostMapping()
    public ResponseEntity<Void> saveGoods(@RequestBody Spu spu){
        goodsService.saveGoods(spu);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 查询SPU的详细信息和SKU
     * @param spuId
     * @return
     */
    @GetMapping("spu/{id}")
    public ResponseEntity<Spu> queryAndDetailAndSkuById(@PathVariable(name = "id",required = true)Long spuId){
        return ResponseEntity.ok(goodsService.queryAndDetailAndSkuById(spuId));
    }

    /**
     * 下架
     * @param ids
     * @return
     */
    @PutMapping("spu/out/{id}")
    public ResponseEntity<Void> outSpu(@PathVariable(name = "id",required = true)String ids){
        //JAVA8特性转集合
        List<Long> idList = Arrays.stream(StringUtils.split(ids, "-")).map(Long::parseLong).collect(Collectors.toList());
        goodsService.outSpu(idList);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
