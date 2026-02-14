package by.pashkavlushka.GoodsCatalogueService.dto;


public class AddGoodsFeedback {
    private String id;
    private boolean status;
    private boolean retryable;

    public AddGoodsFeedback() {
    }

    public AddGoodsFeedback(String id, boolean status, boolean wasPresent) {
        this.id = id;
        this.status = status;
        this.retryable = wasPresent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isRetryable() {
        return retryable;
    }

    public void setRetryable(boolean retryable) {
        this.retryable = retryable;
    }
    
}
