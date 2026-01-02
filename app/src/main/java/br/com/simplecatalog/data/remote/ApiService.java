package br.com.simplecatalog.data.remote;

import java.util.List;

import br.com.simplecatalog.data.remote.dto.ItemDto;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * ApiService define o "contrato" de endpoints HTTP usados pelo app (camada Remote).
 *
 * Responsabilidade:
 * - Declarar quais rotas REST existem e qual o tipo de retorno esperado
 *
 * Importante:
 * - Não contém lógica de negócio
 * - Não faz tratamento de erro aqui (isso é responsabilidade do Repository)
 * - Retrofit gera a implementação em runtime (proxy dinâmico)
 */
public interface ApiService {

    /**
     * Busca uma lista de itens na API.
     *
     * Exemplo para estudo (JSONPlaceholder):
     * GET https://jsonplaceholder.typicode.com/posts
     *
     * Retorna Call<List<ItemDto>> porque em Java (sem coroutines),
     * o Retrofit trabalha com Call para execução síncrona (execute)
     * ou assíncrona (enqueue).
     */
    @GET(ApiEndpoints.ITEMS)
    Call<List<ItemDto>> getItems();
}

