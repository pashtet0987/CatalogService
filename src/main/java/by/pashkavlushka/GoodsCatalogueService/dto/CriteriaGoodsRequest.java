package by.pashkavlushka.GoodsCatalogueService.dto;

import java.util.List;


public class CriteriaGoodsRequest  {
    private String namePattern;
    private String orderBy;
    private Direction direction;
    private Integer minCost;
    private Integer maxCost;
    private List<Category> categories;
    private int pageNumber;

    public CriteriaGoodsRequest() {
    }

    public CriteriaGoodsRequest(String namePattern, String orderBy, Direction direction, Integer minCost, Integer maxCost, List<Category> categories, int pageNumber) {
        this.namePattern = namePattern;
        this.orderBy = orderBy;
        this.direction = direction;
        this.minCost = minCost;
        this.maxCost = maxCost;
        this.categories = categories;
        this.pageNumber = pageNumber;
    }

    public String getNamePattern() {
        return namePattern;
    }

    public void setNamePattern(String namePattern) {
        this.namePattern = namePattern;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Integer getMinCost() {
        return minCost;
    }

    public void setMinCost(Integer minCost) {
        this.minCost = minCost;
    }

    public Integer getMaxCost() {
        return maxCost;
    }

    public void setMaxCost(Integer maxCost) {
        this.maxCost = maxCost;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
    
}
