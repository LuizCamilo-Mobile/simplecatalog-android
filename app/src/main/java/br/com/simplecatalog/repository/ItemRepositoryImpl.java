package br.com.simplecatalog.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.simplecatalog.data.local.dao.ItemDao;
import br.com.simplecatalog.data.local.entity.ItemEntity;
import br.com.simplecatalog.data.mapper.ItemMapper;
import br.com.simplecatalog.data.remote.ApiService;
import br.com.simplecatalog.data.remote.dto.ItemDto;
import br.com.simplecatalog.domain.model.Item;
import retrofit2.Call;
import retrofit2.Response;

/** ItemRepositoryImpl implementa o contrato ItemRepository e
 * concentra decisões de fonte de dados, leitura e atualização
 * do cache local. Ele usa um Mapper para devolver modelos de
 * domínio prontos para a UI, protegendo o fluxo contra falhas de rede.
 *
 * Implementação concreta do contrato ItemRepository.
 * Responsável por decidir a origem dos dados e converter formatos.
 *
 * Responsabilidades:
 * - Ler dados do cache local (Room) primeiro (estratégia cache-first / offline-friendly)
 * - Buscar dados remotos via ApiService (Retrofit) apenas se necessário
 * - Persistir cache local de forma eficiente, evitando duplicações
 * - Converter DTOs e Entities para o modelo de domínio através do Mapper
 * - Proteger a UI e o domínio contra falhas de rede ou indisponibilidade da API
 */
public class ItemRepositoryImpl implements ItemRepository {

    private final ApiService apiService; // serviço HTTP (fonte remota)
    private final ItemDao itemDao;       // acesso ao banco local (cache)
    private final ItemMapper mapper;     // conversões entre camadas

    // construtor
    public ItemRepositoryImpl(ApiService apiService, ItemDao itemDao, ItemMapper mapper) {
        this.apiService = apiService;
        this.itemDao = itemDao;
        this.mapper = mapper;
    }

    @Override
    public List<Item> getItems() {
        // 1) CACHE LOCAL (Room)
        // Tenta ler do banco antes de qualquer chamada remota
        List<ItemEntity> cached = itemDao.getAll();
        if (cached != null && !cached.isEmpty()) {
            // Converte Entity → Domain model antes de devolver pra UI
            return mapper.entitiesToDomain(cached);
        }

        // 2) FONTE REMOTA (API REST)
        // Se o cache estiver vazio, busca os dados remotos
        try {
            Call<List<ItemDto>> call = apiService.getItems();
            Response<List<ItemDto>> response = call.execute(); // execução síncrona fora da UI thread

            if (response.isSuccessful() && response.body() != null) {
                List<ItemDto> dtos = response.body();

                // 3) ATUALIZA CACHE LOCAL
                // Converte DTO → Entity e salva no banco como novo cache
                List<ItemEntity> entities = mapper.dtosToEntities(dtos);
                itemDao.clear();            // limpa cache anterior
                itemDao.insertAll(entities); // insere novo cache

                // 4) Converte Entity → Domain model e devolve resultado final
                return mapper.entitiesToDomain(entities);
            }

            // fallback seguro: domínio/UI recebem lista vazia se não houver dados
            return new ArrayList<>();

        } catch (IOException e) {
            // fallback seguro em falhas de rede: tenta converter o que houver no banco
            List<ItemEntity> fallback = itemDao.getAll();
            return fallback != null ? mapper.entitiesToDomain(fallback) : new ArrayList<>();
        }
    }

    /**
     * Atualiza a lista forçando nova busca na API e sincronizando o cache local.
     * (Opcional, mas muito pertinente para entrevistas que discutem atualização de dados)
     */
    public List<Item> refresh() {
        try {
            Call<List<ItemDto>> call = apiService.getItems();
            Response<List<ItemDto>> response = call.execute();

            if (response.isSuccessful() && response.body() != null) {
                List<ItemEntity> entities = mapper.dtosToEntities(response.body());
                itemDao.clear();
                itemDao.insertAll(entities);
                return mapper.entitiesToDomain(entities);
            }

            // fallback para cache se o refresh falhar
            List<ItemEntity> fallback = itemDao.getAll();
            return fallback != null ? mapper.entitiesToDomain(fallback) : new ArrayList<>();

        } catch (IOException e) {
            // fallback para cache em falha de rede
            List<ItemEntity> fallback = itemDao.getAll();
            return fallback != null ? mapper.entitiesToDomain(fallback) : new ArrayList<>();
        }
    }
}

