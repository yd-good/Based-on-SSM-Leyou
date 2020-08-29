/*
信息:
*/
package com.leyou.controller;
import com.leyou.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Controller
public class PageController {
    @Autowired
    private PageService pageService;
    @GetMapping("item/{id}.html")
    public String querySku(@PathVariable(name = "id",required = true) Long spuId, Model model){
        Map<String,Object> attributes=pageService.loadModel(spuId);
        model.addAllAttributes(attributes);
        return "item";
    }

}
