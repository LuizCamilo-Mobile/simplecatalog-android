package br.com.simplecatalog.data.remote.dto;

// Gson vai usar esses nomes para converter o JSON automaticamente
import com.google.gson.annotations.SerializedName;

/**
 * DTO da camada remota (API).
 * Não contém regras de negócio, apenas espelha a estrutura do JSON recebido.
 *
 * Justificativa em entrevista
 * “Criei um DTO separado para blindar o domínio contra mudanças na API
 * e manter boundaries limpas. Ele modela o JSON e é convertido antes de chegar à UI.”
 *
 * para conhecimento:
 * “Boundaries” (fronteiras/limites) em arquitetura de software são linhas imaginárias
 * que separam partes do código para que cada uma tenha seu papel claro e não misture
 * responsabilidades.
 */
public class ItemDto {

    @SerializedName("id")
    private long id; // identificador do item vindo da API

    @SerializedName("title")
    private String title; // título/principal texto do item

    @SerializedName("body")
    private String subtitle; // usamos "body" do JSON como subtítulo no domínio

    // Construtor vazio é necessário para o Gson criar o objeto via reflexão
    public ItemDto() {}

    // Getters usados apenas pelo Mapper ou em testes da camada de dados
    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getSubtitle() { return subtitle; }

    // Não tem setters porque o preenchimento vem 100% do JSON (Gson) na conversão
}

