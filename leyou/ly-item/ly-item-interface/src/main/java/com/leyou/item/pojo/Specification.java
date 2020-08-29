/*
信息:
*/
package com.leyou.item.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
@Data
@Table(name = "tb_specification")
public class Specification {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long categoryId;
    private String specifications;

}
