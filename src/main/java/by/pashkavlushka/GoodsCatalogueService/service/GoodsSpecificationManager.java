package by.pashkavlushka.GoodsCatalogueService.service;

import by.pashkavlushka.GoodsCatalogueService.entity.GoodsEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;


public class GoodsSpecificationManager {
    public static Specification<GoodsEntity> byName(String name) {
        return (Root<GoodsEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            return criteriaBuilder.like(root.get("name"), "%" + name + "%");
        };
    }
    
    public static Specification<GoodsEntity> byCategories(List<String> categories) {
        return (Root<GoodsEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            return root.get("category").in(categories);
        };
    }
    
    public static Specification<GoodsEntity> byMinPrice(int min){
        return (Root<GoodsEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            return criteriaBuilder.ge(root.get("cost"), min);
        };
    }
    
    public static Specification<GoodsEntity> byMaxPrice(int max){
        return (Root<GoodsEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            return criteriaBuilder.le(root.get("cost"), max);
        };
    }
}
