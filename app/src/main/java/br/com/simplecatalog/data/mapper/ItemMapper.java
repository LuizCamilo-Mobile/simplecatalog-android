package br.com.simplecatalog.data.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.simplecatalog.data.local.entity.ItemEntity;
import br.com.simplecatalog.data.remote.dto.ItemDto;
import br.com.simplecatalog.domain.model.Item;

public class ItemMapper {

    public List<ItemEntity> dtosToEntities(List<ItemDto> dtos) {
        if (dtos == null) return Collections.emptyList();

        List<ItemEntity> out = new ArrayList<>();
        for (ItemDto dto : dtos) {
            if (dto == null) continue;

            long id = dto.getId();
            String title = safe(dto.getTitle());
            // Aqui simulamos “normalização” entre API e banco:
            // API tem body, banco guarda como subtitle (ou uma coluna equivalente).
            String subtitle = safe(dto.getBody());

            out.add(new ItemEntity(id, title, subtitle));
        }
        return out;
    }

    public List<Item> entitiesToDomain(List<ItemEntity> entities) {
        if (entities == null) return Collections.emptyList();

        List<Item> out = new ArrayList<>();
        for (ItemEntity entity : entities) {
            if (entity == null) continue;

            long id = entity.getId();
            String title = safe(entity.getTitle());
            String subtitle = safe(entity.getSubtitle());

            out.add(new Item(id, title, subtitle));
        }
        return out;
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}
