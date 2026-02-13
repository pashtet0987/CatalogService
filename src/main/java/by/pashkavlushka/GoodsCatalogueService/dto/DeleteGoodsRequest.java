package by.pashkavlushka.GoodsCatalogueService.dto;


public class DeleteGoodsRequest {
    private String id;
    private long itemId;
    private long sellerId;

    public DeleteGoodsRequest() {
    }

    public DeleteGoodsRequest(long itemId, long sellerId, String id) {
        this.id = id;
        this.sellerId = sellerId;
        this.itemId = itemId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getSellerId() {
        return sellerId;
    }

    public void setSellerId(long sellerId) {
        this.sellerId = sellerId;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }
}
