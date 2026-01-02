package br.com.simplecatalog.data.mapper;

import java.util.ArrayList;
import java.util.List;

import br.com.simplecatalog.data.local.entity.ItemEntity;
import br.com.simplecatalog.data.remote.dto.ItemDto;
import br.com.simplecatalog.domain.model.Item;

/**
 * ItemMapper faz a tradução de dados entre:
 *  Remote (DTO) ↔ Local (Entity) ↔ Domain (Model)
 *
 *  ItemMapper é responsável por converter dados entre camadas:
 *  API (ItemDto) → Banco local (ItemEntity)
 *  Banco local (ItemEntity) → Domínio (Item)
 *  Isso respeita as boundaries do projeto, porque cada camada fala sua própria linguagem de dados, e o Mapper traduz entre elas.
 */
public class ItemMapper {

    /**
     * Converte lista de DTOs da API para lista de Entities do banco local (Room)
     */
    public List<ItemEntity> dtosToEntities(List<ItemDto> dtos) {
        List<ItemEntity> entities = new ArrayList<>();
        for (ItemDto dto : dtos) {
            entities.add(
                    new ItemEntity(
                            dto.getId(),        // ← JSON: id
                            dto.getTitle(),     // ← JSON: title
                            dto.getSubtitle()   // ← JSON: body (espelhado como subtitle no DTO)
                    )
            );
        }
        return entities;
    }

    /**
     * Converte lista de Entities do Room para lista de Models do domínio
     * (o que a UI ou UseCase realmente usam)
     */
    public List<Item> entitiesToDomain(List<ItemEntity> entities) {
        List<Item> domainItems = new ArrayList<>();
        for (ItemEntity entity : entities) {
            domainItems.add(
                    new Item(
                            entity.getId(),        // ← banco: id
                            entity.getTitle(),     // ← banco: title
                            entity.getSubtitle()   // ← banco: subtitle
                    )
            );
        }
        return domainItems;
    }
}

