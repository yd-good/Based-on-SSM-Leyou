/*
信息:
*/
package com.leyou.search.controller;

import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("search")
public class SearchController {
    @Autowired
    private SearchService searchService;
    /**
     * 搜索功能
     * @param searchRequest
     * @return
     */
    @PostMapping("/page")
    public ResponseEntity<SearchResult> search(@RequestBody SearchRequest searchRequest){
        return ResponseEntity.ok(searchService.search(searchRequest));
    }

}
