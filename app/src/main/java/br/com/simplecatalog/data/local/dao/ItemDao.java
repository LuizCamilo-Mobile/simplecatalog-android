package br.com.simplecatalog.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import br.com.simplecatalog.data.local.entity.ItemEntity;

/**
 * DAO (Data Access Object) do Room.
 * Responsável por definir as operações de leitura e escrita na tabela "items".
 *
 * Importante:
 * - Aqui ficam as queries SQL (via @Query)
 * - O Repository chama o DAO, mas não escreve SQL diretamente
 *
 * DAO encapsula o acesso ao banco. Ele concentra as queries e mantém a camada de dados isolada,
 * evitando SQL espalhado pela UI ou domínio.
 *
 * Sobre ARN
 * ANR ocorre quando a thread principal fica bloqueada e o app não responde.
 * Eu evito isso executando operações de rede e banco em background.
 */
@Dao
public interface ItemDao {

    /**
     * Retorna todos os itens do cache local.
     * Como é um projeto simples, a query retorna uma lista direta.
     */
    @Query("SELECT * FROM items")
    List<ItemEntity> getAll();

    /**
     * Insere uma lista de itens no banco.
     * OnConflictStrategy.REPLACE faz upsert simples (se já existir o id, substitui).
     * Isso facilita sincronização com a API.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ItemEntity> items);

    /**
     * Limpa a tabela inteira.
     * Útil para estratégia simples de atualizar cache (clear + insertAll).
     */
    @Query("DELETE FROM items")
    void clear();
}

