/*
信息:
*/
package com.leyou.item.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name ="tb_category")
public class Category {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private String name;
    private Long parentId;
    private Boolean isParent;
    private Integer sort;

    public Category() {
    }

    public Category(Long id) {
        this.id = id;
    }

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    public Category(Long id, String name, Long parentId, Boolean isParent, Integer sort) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.isParent = isParent;
        this.sort = sort;
    }
}
