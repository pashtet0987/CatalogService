package by.pashkavlushka.GoodsCatalogueService.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class CriteriaGoodsResponse  {
    private String namePattern;
    private String orderBy;
    private Direction direction;
    private Integer minCost;
    private Integer maxCost;
    private List<Category> categories = new ArrayList();
    private int pageNumber = 0;
    private List<GoodsDTO> goods = new ArrayList();
    private boolean hasNextPage = false;

    public CriteriaGoodsResponse() {
    }

    public CriteriaGoodsResponse(String namePattern, String orderBy, Direction direction, Integer minCost, Integer maxCost, List<Category> categories, int pageNumber, List<GoodsDTO> goods, boolean hasNextPage) {
        this.namePattern = namePattern;
        this.orderBy = orderBy;
        this.direction = direction;
        this.minCost = minCost;
        this.maxCost = maxCost;
        this.categories = categories;
        this.pageNumber = pageNumber;
        this.goods = goods;
        this.hasNextPage = hasNextPage;
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

    public List<GoodsDTO> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsDTO> goods) {
        this.goods = goods;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }
}
