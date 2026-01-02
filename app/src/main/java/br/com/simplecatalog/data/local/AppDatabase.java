package br.com.simplecatalog.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import br.com.simplecatalog.data.local.dao.ItemDao;
import br.com.simplecatalog.data.local.entity.ItemEntity;

/**
 * Classe principal do Room Database.
 *
 * Responsabilidades:
 * - Declarar quais Entities fazem parte do banco
 * - Definir a versão do schema (para migrações)
 * - Expor os DAOs para acesso às tabelas
 *
 * Observação:
 * - A instância do banco é criada no AppContainer para ser Singleton no app.
 */
@Database(
        entities = { ItemEntity.class },
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    // Expondo o DAO da tabela items
    public abstract ItemDao itemDao();
}

