package com.leyou.item.mapper;


import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CategoryMapper extends Mapper<Category>, IdListMapper<Category,Long> {
    @Select("SELECT * FROM tb_brand WHERE id=#{brandId}")
    Brand queryBrandBypId(Long brandId);
    @Select("SELECT category_id FROM tb_category_brand WHERE brand_id=#{brandId} ")
    List<Long> queryCategoryIdsByBrandId(Long brandId);
}
