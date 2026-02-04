package by.pashkavlushka.GoodsCatalogueService.mapstruct;

import by.pashkavlushka.GoodsCatalogueService.dto.GoodsDTO;
import by.pashkavlushka.GoodsCatalogueService.entity.GoodsEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface GoodsMapper {
    
    GoodsDTO entityToDTO(GoodsEntity entity);
    GoodsEntity dtoToEntity(GoodsDTO dto);
}
