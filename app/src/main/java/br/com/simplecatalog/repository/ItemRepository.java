package br.com.simplecatalog.repository;

import java.util.List;

import br.com.simplecatalog.domain.model.Item;

public interface ItemRepository {

    // Cache-first (Room → API → Room → Domain). Não deve travar UI (será chamado em background).
    List<Item> getItems();

    // Força API e sincroniza cache. Se falhar, deve permitir fallback seguro.
    // A UI quer saber que houve falha no refresh (para mostrar error state).
    List<Item> refresh() throws Exception;
}
