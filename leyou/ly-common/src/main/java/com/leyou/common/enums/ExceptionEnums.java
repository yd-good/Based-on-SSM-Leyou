/*
信息:
*/
package com.leyou.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnums{
    PRICE_CANNOT_BE_NULL("价格不能为空!",400),
    CATEGORY_NOT_FIND("未查到商品！",404),
    BRAND_NOT_FIND("品牌未查到！",404),
    BRAND_SAVE_ERROR("新增品牌失败！",500),
    UPLOAD_FILE_ERROR("图片上传失败！",400),
    ERROR_UPLOAD_TYPE("文件上传类型不匹配！",400),
    DELETE_BRAND_ERROR("删除品牌失败",400),
    DELETE_BRAND_CATEGORY_ERROR("删除分类品牌失败",400),
    SPECIFICATION_NOT_FIND("商品规模数据未查到",400),
    GOODS_NOT_FIND("商品没查到",500),
    SAVE_GOODS_ERROR("商品存储失败",500),
    SPU_DETAIL_NOT_FIND("商品详情查询失败",500),
    STOCK_NOT_FIND("库存为找到",500),
    SPU_OUT_ERROR("SPU处理失败",500),
    USER_DATA_TYPE_ERROR("用户注册校验参数有误",400);
    ;
    private String msg;
    private int code;

}
