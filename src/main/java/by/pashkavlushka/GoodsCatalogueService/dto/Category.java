package by.pashkavlushka.GoodsCatalogueService.dto;


public class Category  {
    private String category;
    private boolean on;

    public Category() {
    }

    public Category(String category, boolean on) {
        this.category = category;
        this.on = on;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }
    
}
