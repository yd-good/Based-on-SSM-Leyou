/*
信息:
*/
package com.leyou.item.web;

import com.leyou.item.pojo.Specification;
import com.leyou.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("spec")
@Controller
public class SpecificationController {
    @Autowired
    private SpecificationService specificationService;

    @GetMapping("{id}")
    public ResponseEntity<String> querySpecificationByCid(@PathVariable(value = "id", required = true) Long id) {
        return ResponseEntity.ok(specificationService.querySpecificationByCid(id));
    }

    @PostMapping()
    public ResponseEntity<Void> saveSpecificationByCid(Specification specification) {
        specificationService.saveSpecificationByCid(specification);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping()
    public ResponseEntity<Void> updateSpecification(Specification specification){
        specificationService.updateSpecification(specification);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
