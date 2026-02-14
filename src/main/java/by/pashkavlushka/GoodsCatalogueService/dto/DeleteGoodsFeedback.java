package by.pashkavlushka.GoodsCatalogueService.dto;


public class DeleteGoodsFeedback {
    private String id;
    private boolean status;
    //in case if there is no product with such id or some other issue, which never gonna change after retries
    private boolean retryable;

    public DeleteGoodsFeedback() {
    }

    public DeleteGoodsFeedback(String id, boolean status, boolean retryable) {
        this.id = id;
        this.status = status;
        this.retryable = retryable;
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
