package by.pashkavlushka.GoodsCatalogueService.exception;


public class NotFoundEntityException extends EntityException {

    public NotFoundEntityException(String message) {
        super(message);
    }
    
    public NotFoundEntityException(){
        super("Entity not found");
    }
    
}
