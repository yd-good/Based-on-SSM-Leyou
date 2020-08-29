/*
信息:
*/
package com.leyou.item.web;


import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import com.netflix.ribbon.proxy.annotation.Http;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;
    /**
     * 分页查询品牌
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @param key
     * @return
     */
    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> queryBrandPage(
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy",required = false) String sortBy,
            @RequestParam(value = "desc",defaultValue = "false") Boolean desc,
            @RequestParam(value = "key",required = false) String key){
        return ResponseEntity.ok(brandService.queryBrandByPage(page,rows,sortBy,desc,key));
    }
    /*
      新增品牌
     */
    @PostMapping()
    public ResponseEntity<Void> addBrand(Brand brand, @RequestParam(value = "categoriesId",required = true)List<Long> ids){
        brandService.addBrand(brand,ids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @DeleteMapping("/bid/{id}")
    public ResponseEntity<Void> deleteBrandById(@PathVariable(name = "id",required = true)Long brandId){
        brandService.deleteBrand(brandId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @DeleteMapping("cid_bid/{id}")
    public ResponseEntity<Void> deleteBrandCategory(@PathVariable(name = "id",required = true)Long brandId){
        brandService.deleteBrandAndCategory(brandId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @PutMapping()
    public ResponseEntity<Void> updateBrand(Brand brand, @RequestParam(value = "categoriesId",required = true)List<Long> ids){
        brandService.updateBrand(brand,ids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping("cid/{id}")
    public ResponseEntity<List<Brand>> queryBrandByCid(
            @PathVariable(name = "id",required = true) Long cid
    ){
         return ResponseEntity.ok(brandService.queryBrandByCid(cid));
    }

    /**
     * 通过id获取品牌
     * @param brandId
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity<Brand> queryBrandById(@PathVariable(name = "id")Long  brandId){
        return ResponseEntity.ok(brandService.queryBrandById(brandId));
    }
    @GetMapping("queryIds")
    public ResponseEntity<List<Brand>> queryBrandByIds(@RequestParam(name = "ids") List<Long> ids){
        return ResponseEntity.ok(brandService.queryBrandByIds(ids));
    }
}
