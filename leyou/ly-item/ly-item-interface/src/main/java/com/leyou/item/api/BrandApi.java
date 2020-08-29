package com.leyou.item.api;

import com.leyou.item.pojo.Brand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/brand")
public interface BrandApi {
    @GetMapping("{id}")
    Brand queryBrandById(@PathVariable(name = "id")Long  brandId);
    @GetMapping("queryIds")
    List<Brand> queryBrandByIds(@RequestParam(name = "ids") List<Long> ids);
}
