package com.leyou.item.mapper;

import com.leyou.common.mapper.BaseMapper;
import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import sun.rmi.runtime.Log;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends  BaseMapper<Brand> {
   @Insert("INSERT INTO tb_category_brand(category_id,brand_id) VALUE(#{categoryId},#{brandId})")
   int insertCategoryBrand(@Param("categoryId")Long categoryId,@Param("brandId")Long brandId);
   @Delete("DELETE FROM tb_category_brand WHERE brand_id=#{brandId}")
   int deleteBrandCategoryById(Long brandId);
   @Select("SELECT * FROM tb_brand b\n" +
           "INNER JOIN tb_category_brand cb\n" +
           "ON b.id=cb.brand_id WHERE cb.category_id=#{cid};")
   List<Brand> selectBrandByCid(@Param("cid") Long cid);
}
