package com.kiki.springboot_kiki.Pojo.DTO;

public class ConferenceCategoryDTO {
    private Integer categoryId;
    private String categoryName;
    private String categoryDesc;
    private Integer sortOrder;

    public ConferenceCategoryDTO() {
    }

    public ConferenceCategoryDTO(Integer categoryId, String categoryName, String categoryDesc, Integer sortOrder) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryDesc = categoryDesc;
        this.sortOrder = sortOrder;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDesc() {
        return categoryDesc;
    }

    public void setCategoryDesc(String categoryDesc) {
        this.categoryDesc = categoryDesc;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
} 