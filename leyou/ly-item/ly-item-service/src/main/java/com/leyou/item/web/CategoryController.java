/*
信息:
*/
package com.leyou.item.web;

import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.rmi.runtime.Log;

import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("list")
    public ResponseEntity<List<Category>> queryCategoryBypId(@RequestParam("pid") Long pid){
        return ResponseEntity.ok(categoryService.queryCategoryBypId(pid));
    }
    @PutMapping("edit")
    public ResponseEntity<Void> editCategoryById(
            @RequestParam(value = "id",required = true) Long id,
            @RequestParam(value = "name",required = true) String name){
           categoryService.updateCategoryById(id,name);
           return ResponseEntity.status(HttpStatus.OK).build();
    }
    @DeleteMapping(value = "cid/{id}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable(name = "id",required = true)Long id){
        categoryService.deleteCategoryById(id);
        //删除成功  返回ACCEPTED  202
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
    @GetMapping("/bid/{id}")
    public ResponseEntity<Brand> queryBrandById(@PathVariable(name = "id",required = true)Long brandId){
        return ResponseEntity.ok(categoryService.queryBrandBypId(brandId));
    }

    /**
     * 根据id查询商品分类
     * @param ids
     * @return
     */
    @GetMapping("/list/ids")
    public ResponseEntity<List<Category>> queryCategoryByIds(@RequestParam("ids")List<Long> ids){
        return ResponseEntity.ok(categoryService.queryCategoryByIds(ids));
    }
}
