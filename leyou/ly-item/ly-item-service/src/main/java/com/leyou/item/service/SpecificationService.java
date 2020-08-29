/*
信息:
*/
package com.leyou.item.service;

import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.SpecificationMapper;
import com.leyou.item.pojo.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpecificationService {
    @Autowired
    private SpecificationMapper specificationMapper;

    public String querySpecificationByCid(Long id) {
       Specification specification= specificationMapper.selectByPrimaryKey(id);
       if (specification==null){
           throw new LyException(ExceptionEnums.SPECIFICATION_NOT_FIND);
       }
       return specification.getSpecifications();
    }

    public void saveSpecificationByCid(Specification specification) {
        specificationMapper.insertSelective(specification);
    }

    public void updateSpecification(Specification specification) {

        specificationMapper.updateByPrimaryKeySelective(specification);
    }
}
