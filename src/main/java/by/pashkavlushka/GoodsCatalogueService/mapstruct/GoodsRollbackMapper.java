package by.pashkavlushka.GoodsCatalogueService.mapstruct;

import by.pashkavlushka.GoodsCatalogueService.entity.GoodsEntity;
import by.pashkavlushka.GoodsCatalogueService.entity.GoodsToRollbackUpdateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GoodsRollbackMapper {

    @Mapping(target = "amount", source = "toAddAmount")
    @Mapping(target = "id", source = "itemId")
    GoodsEntity rollbackEntityToEntity(GoodsToRollbackUpdateEntity entity);

    @Mapping(target = "toAddAmount", source = "amount")
    @Mapping(target = "itemId", source = "id")
    GoodsToRollbackUpdateEntity entityToRollbackEntity(GoodsEntity entity);
}
