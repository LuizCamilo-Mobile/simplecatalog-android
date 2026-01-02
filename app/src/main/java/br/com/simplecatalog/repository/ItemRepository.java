package br.com.simplecatalog.repository;

import java.util.List;
import br.com.simplecatalog.domain.model.Item;

/**
 * Interface que define o contrato do Repositório de Itens.
 *
 * Papel no projeto:
 * - Desacoplar o domínio (UseCases, ViewModel ou Presenter) da infraestrutura (Retrofit, Room, etc)
 * - Atuar como fonte única de dados na perspectiva do domínio (Single Source of Truth = o domínio só fala com este contrato)
 * - Permitir múltiplas implementações (ex: real, fake, mock) sem impactar a UI ou regras de negócio
 * - Facilitar testes unitários e de integração, permitindo simular dados sem acessar rede ou banco real
 *
 * Importante:
 * - Esta é uma interface, portanto não é instanciável diretamente
 * - Sua implementação concreta (ex: ItemRepositoryImpl) será criada e gerenciada no AppContainer
 */

public interface ItemRepository {
    /**
     * Retorna todos os itens no formato do domínio.
     * A origem dos dados (API remota ou cache local) é decidida pela implementação, não pelo domínio.
     */
    List<Item> getItems();
}

