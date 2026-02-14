package by.pashkavlushka.GoodsCatalogueService.mapstruct;

import by.pashkavlushka.GoodsCatalogueService.dto.AddGoodsRequest;
import by.pashkavlushka.GoodsCatalogueService.dto.GoodsDTO;
import by.pashkavlushka.GoodsCatalogueService.entity.GoodsEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface GoodsMapper {
    
    GoodsDTO entityToDTO(GoodsEntity entity);
    GoodsEntity dtoToEntity(GoodsDTO dto);
    @Mapping(target = "id", source = "request.id", ignore = true)
    GoodsEntity requestToEntity(AddGoodsRequest request);
}
