/*
信息:
*/
package com.leyou.item.service;

import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    public List<Category> queryCategoryBypId(Long pid) {
        Category category=new Category();
        category.setParentId(pid);
        List<Category> categoryList=categoryMapper.select(category);
        if (CollectionUtils.isEmpty(categoryList)){
              throw new LyException(ExceptionEnums.CATEGORY_NOT_FIND);
        }

        return categoryList;
    }
    public void updateCategoryById(Long id, String name) {
        Category category=new Category(id,name);
        /* 方法：int updateByPrimaryKeySelective(T record);
            说明：根据主键更新属性不为null的值
         */
        categoryMapper.updateByPrimaryKeySelective(category);
    }

    public void deleteCategoryById(Long id) {
      categoryMapper.deleteByPrimaryKey(new Category(id));
    }

    public Brand queryBrandBypId(Long brandId) {
        return categoryMapper.queryBrandBypId(brandId);
    }
    public List<Category> queryCategoryByIds(List<Long> ids){
        List<Category> categoryList= categoryMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(categoryList)){
            throw new LyException(ExceptionEnums.CATEGORY_NOT_FIND);
        }
        return categoryList;
    }
}
