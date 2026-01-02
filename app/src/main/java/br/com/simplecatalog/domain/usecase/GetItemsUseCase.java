package br.com.simplecatalog.domain.usecase;

import java.util.List;

import br.com.simplecatalog.domain.model.Item;
import br.com.simplecatalog.repository.ItemRepository;

/* Um Use Case é uma classe que representa uma ação do sistema
* (ex: “carregar itens”) e isola a regra/fluxo de negócio da UI.
* */

public class GetItemsUseCase {

    private final ItemRepository repository;
    /* O UseCase não sabe se vem da internet ou do banco.
    * Ele só sabe pedir ao contrato (interface) ItemRepository.
    * */

    // construtor
    public GetItemsUseCase(ItemRepository repository) {
        this.repository = repository;
    }
    /* Isso permite trocar facilmente a implementação.
    * Em testes unitários você pode “mockar” o repository.
    * */

    public List<Item> execute() {
        return repository.getItems();
    }
    /* execute() é a operação do UseCase.
    * Aqui está simples, mas depois pode crescer:
    * validações
    * ordenação
    * filtro
    * fallback
    * regras de cache
    * */
}

