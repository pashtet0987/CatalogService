package by.pashkavlushka.GoodsCatalogueService.mapstruct;

import by.pashkavlushka.GoodsCatalogueService.dto.UpdateGoodsRequest;
import by.pashkavlushka.GoodsCatalogueService.entity.GoodsToRollbackUpdateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UpdateRequestMapper {
    
    UpdateGoodsRequest entityToDTO(GoodsToRollbackUpdateEntity entity);
    GoodsToRollbackUpdateEntity dtoToEntity(UpdateGoodsRequest request);
}
