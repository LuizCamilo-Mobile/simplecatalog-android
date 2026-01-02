package br.com.simplecatalog.domain.model;

/* Item.java é um modelo de domínio (domain model).
* Ele agrupa todos os dados do item no construtor e expõe a leitura por getters individuais.
* Os campos são imutáveis para garantir segurança e previsibilidade no fluxo do app.
* */

public class Item {

    // Os campos são imutáveis para garantir segurança e previsibilidade no fluxo do app.
    private final long id;
    private final String title;
    private final String subtitle;

    // Construtor, cria o objeto e preenche os dados
    public Item(long id, String title, String subtitle) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
    }

    /* Esses são métodos getters.
    * Cada um devolve um campo específico do item.
    * */
    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getSubtitle() { return subtitle; }
}

