/*
信息:
*/
package com.leyou.search.pojo;


import java.util.Map;

public class SearchRequest {
    private String key;
    private Integer page;
    private Integer size;
    private String sortBy;
    private Boolean descending;
    private static final int DEFAULT_PAGE=1;
    private static final int DEFAULT_SIZE=20;
    private Map<String,String> filter;
    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Boolean getDescending() {
        return descending;
    }

    public void setDescending(Boolean descending) {
        this.descending = descending;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getPage() {
       if (page==null){
           return DEFAULT_PAGE;
       }
        return Math.max(DEFAULT_PAGE, page);
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Integer getSize() {
        if (size==null){
            return DEFAULT_SIZE;
        }
        return size;
    }

    public Map<String, String> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, String> filter) {
        this.filter = filter;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "SearchRequest{" +
                "key='" + key + '\'' +
                ", page=" + page +
                ", size=" + size +
                ", sortBy='" + sortBy + '\'' +
                ", descending=" + descending +
                ", filter=" + filter +
                '}';
    }
}
