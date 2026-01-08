package br.com.simplecatalog.data.mapper;

import java.util.ArrayList;
import java.util.List;

import br.com.simplecatalog.data.local.entity.ItemEntity;
import br.com.simplecatalog.data.remote.dto.ItemDto;
import br.com.simplecatalog.domain.model.Item;

public class ItemMapper {

    public Item toDomain(ItemEntity entity) {
        return new Item(entity.id, entity.title, entity.subtitle);
    }

    public ItemEntity toEntity(ItemDto dto) {
        // subtitle vem do "body" do JSONPlaceholder
        String subtitle = dto.body != null ? dto.body : "";
        return new ItemEntity(dto.id, dto.title != null ? dto.title : "", subtitle);
    }

    public List<Item> toDomainListFromEntity(List<ItemEntity> entities) {
        List<Item> result = new ArrayList<>();
        if (entities == null) return result;
        for (ItemEntity e : entities) {
            result.add(toDomain(e));
        }
        return result;
    }

    public List<ItemEntity> toEntityListFromDto(List<ItemDto> dtos) {
        List<ItemEntity> result = new ArrayList<>();
        if (dtos == null) return result;
        for (ItemDto dto : dtos) {
            result.add(toEntity(dto));
        }
        return result;
    }
}
