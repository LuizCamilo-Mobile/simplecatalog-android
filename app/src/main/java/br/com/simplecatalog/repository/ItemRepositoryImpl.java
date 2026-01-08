package br.com.simplecatalog.repository;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import br.com.simplecatalog.data.local.dao.ItemDao;
import br.com.simplecatalog.data.local.entity.ItemEntity;
import br.com.simplecatalog.data.mapper.ItemMapper;
import br.com.simplecatalog.data.remote.ApiService;
import br.com.simplecatalog.data.remote.dto.ItemDto;
import retrofit2.Response;

public class ItemRepositoryImpl implements ItemRepository {

    private final ItemDao dao;
    private final ApiService api;
    private final ItemMapper mapper;

    public ItemRepositoryImpl(ItemDao dao, ApiService api, ItemMapper mapper) {
        this.dao = dao;
        this.api = api;
        this.mapper = mapper;
    }

    @Override
    public List<br.com.simplecatalog.domain.model.Item> getItems() {
        // 1) tenta cache
        List<ItemEntity> cached = dao.getAll();
        if (cached != null && !cached.isEmpty()) {
            return mapper.toDomainListFromEntity(cached);
        }

        // 2) cache vazio → tenta API
        try {
            Response<List<ItemDto>> resp = api.getItems().execute();
            if (resp.isSuccessful() && resp.body() != null) {
                List<ItemEntity> entities = mapper.toEntityListFromDto(resp.body());
                dao.replaceAll(entities);
                return mapper.toDomainListFromEntity(dao.getAll());
            }
        } catch (IOException ignored) {
            // fallback abaixo
        }

        // 3) fallback seguro: tenta cache de novo; senão vazio
        List<ItemEntity> fallback = dao.getAll();
        if (fallback != null && !fallback.isEmpty()) {
            return mapper.toDomainListFromEntity(fallback);
        }
        return Collections.emptyList();
    }

    @Override
    public List<br.com.simplecatalog.domain.model.Item> refresh() throws Exception {
        // Força API
        try {
            Response<List<ItemDto>> resp = api.getItems().execute();
            if (resp.isSuccessful() && resp.body() != null) {
                List<ItemEntity> entities = mapper.toEntityListFromDto(resp.body());
                dao.replaceAll(entities);
                return mapper.toDomainListFromEntity(dao.getAll());
            }

            // resposta não-OK
            throw new IOException("HTTP " + resp.code());

        } catch (IOException e) {
            // fallback: devolve cache se existir, mas sinaliza falha para a UI
            List<ItemEntity> cached = dao.getAll();
            if (cached != null && !cached.isEmpty()) {
                // A UI quer mostrar erro no refresh, mas ainda exibir o cache.
                // Como a assinatura não permite "return + error", a estratégia é:
                // 1) lançar exceção para ViewModel setar error
                // 2) ViewModel chama loadItems() para garantir lista do cache
                throw new Exception("Falha ao atualizar pela internet. Mantendo cache.", e);
            }
            throw new Exception("Falha ao atualizar e cache vazio.", e);
        }
    }
}
