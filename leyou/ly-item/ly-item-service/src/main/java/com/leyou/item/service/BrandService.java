/*
信息:
*/
package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import sun.rmi.runtime.NewThreadAction;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandService {
    @Autowired
    private BrandMapper brandMapper;

    public PageResult<Brand> queryBrandByPage(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        //分页
        PageHelper.startPage(page, rows);
        //过滤
        Example example = new Example(Brand.class);
        if (StringUtils.isNotBlank(key)) {
            example.createCriteria().orLike("name", "%" + key + "%")
                    .orEqualTo("letter", key.toUpperCase());
        }
        //排序
        if (StringUtils.isNotBlank(sortBy)) {
            String clause = sortBy + " " + (desc ? "DESC" : "ASC");
            example.setOrderByClause(clause);
        }
        //查询
        List<Brand> brands = brandMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(brands)) {
            throw new LyException(ExceptionEnums.BRAND_NOT_FIND);
        }
        //分析分页结果
        PageInfo<Brand> pageInfo = new PageInfo<>(brands);
        return new PageResult<>(pageInfo.getTotal(), brands);
    }

    @Transactional
    public void addBrand(Brand brand, List<Long> ids) {
        // 保存一个实体，null的属性也会保存，不会使用数据库默认值
        //brandMapper.insert();
        //返回值为1表示新增成功，0表示失败
        brand.setId(null);
        int count = brandMapper.insertSelective(brand);
        if (count != 1) {
            throw new LyException(ExceptionEnums.BRAND_SAVE_ERROR);
        }
        //关联中间表
        for (Long id : ids) {
            count = brandMapper.insertCategoryBrand(id, brand.getId());
            if (count != 1) {
                throw new LyException(ExceptionEnums.BRAND_SAVE_ERROR);
            }
        }
    }

    public void deleteBrand(Long brandId) {
        int count = brandMapper.deleteByPrimaryKey(brandId);
        if (count != 1) {
            throw new LyException(ExceptionEnums.DELETE_BRAND_ERROR);
        }
        count = brandMapper.deleteBrandCategoryById(brandId);
        if (count != 1) {
            throw new LyException(ExceptionEnums.DELETE_BRAND_CATEGORY_ERROR);
        }
    }

    public void deleteBrandAndCategory(Long brandId) {
        int count = brandMapper.deleteBrandCategoryById(brandId);
        if (count != 1) {
            throw new LyException(ExceptionEnums.DELETE_BRAND_CATEGORY_ERROR);
        }
    }

    public void updateBrand(Brand brand, List<Long> ids) {

        int count = brandMapper.updateByPrimaryKeySelective(brand);
        //关联中间表
        for (Long id : ids) {
            count = brandMapper.insertCategoryBrand(id, brand.getId());
            if (count != 1) {
                throw new LyException(ExceptionEnums.BRAND_SAVE_ERROR);
            }
        }
    }

    public Brand queryBrandById(Long id){
      Brand brand=brandMapper.selectByPrimaryKey(id);
      if (brand==null){
          throw  new LyException(ExceptionEnums.BRAND_NOT_FIND);
      }
      return brand;
    }

    public List<Brand> queryBrandByCid(Long cid) {
        List<Brand> brands=brandMapper.selectBrandByCid(cid);
        if (CollectionUtils.isEmpty(brands)){
            throw  new LyException(ExceptionEnums.BRAND_NOT_FIND);
        }
        return brands;
    }

    public List<Brand> queryBrandByIds(List<Long> ids) {
        List<Brand> brandList = brandMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(brandList)){
            throw  new LyException(ExceptionEnums.BRAND_NOT_FIND);
        }
        return brandList;
    }
}
