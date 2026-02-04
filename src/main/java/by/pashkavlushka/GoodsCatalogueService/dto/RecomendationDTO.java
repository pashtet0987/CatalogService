package by.pashkavlushka.GoodsCatalogueService.dto;

public class RecomendationDTO {
    
    private long userId;
    private String category;

    public RecomendationDTO() {
    }

    public RecomendationDTO(long userId, String category) {
        this.userId = userId;
        this.category = category;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    
    
    
}
