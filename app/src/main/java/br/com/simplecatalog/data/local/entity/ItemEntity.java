package br.com.simplecatalog.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

/**
 * Entity do Room: define a tabela "items" no banco local (SQLite).
 * Usada para cache e persistência offline.
 *
 * Papel da classe
 * Representa a tabela do banco local (camada Data → Local)
 * “Espelha” os itens salvos no dispositivo
 * Serve como estrutura persistida, não como regra de negócio
 * Será convertida depois para domain/Item pelo ItemMapper
 *
 * ItemEntity pertence à camada de dados locais e apenas define a estrutura da tabela.
 * Não contém lógica de negócio. É usada para cache e suporte offline.
 */
@Entity(tableName = "items")
public class ItemEntity {

    // Chave primária única da tabela
    @PrimaryKey
    private long id;

    // Colunas da tabela (nomes explícitos para clareza)
    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "subtitle")
    private String subtitle;

    public ItemEntity(long id, String title, String subtitle) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
    }

    // Getters e setters são permitidos aqui porque a Entity pode precisar ser
    // construída manualmente pelo Mapper antes de salvar no banco.
    // (Diferente do model de domínio, que é imutável)
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSubtitle() { return subtitle; }
    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }
}

