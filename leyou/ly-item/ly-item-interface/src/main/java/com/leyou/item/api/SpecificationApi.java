package com.leyou.item.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("spec")
public interface SpecificationApi {
    @GetMapping("{id}")
    String querySpecificationByCid(@PathVariable(value = "id", required = true) Long id);
}
