package com.example.laboratorio04amqh.Model.data

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import com.example.laboratorio04amqh.Model.Task
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "task")
data class  taskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") val title:String,
    @ColumnInfo(name ="description") val description:String
)

@Dao
interface TaskDao{
    @Insert
    suspend fun insertTask(task: taskEntity)
    @Query("SELECT * FROM task WHERE title = :title")
    fun getTaskByTitle(title: String): Flow<List<taskEntity>>
    @Query("SELECT * FROM task")
    fun getAllTask(): Flow<List<taskEntity>>
    @Query("DELETE FROM task WHERE id = :id")
    suspend fun deleteTaskById(id: Int)
    @Query("""
    UPDATE task
    SET description = :description
    WHERE title = :title
""")
    suspend fun updateTaskByTitle(
        title: String,
        description: String
    )
}

@Database(entities = [taskEntity::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun TaskDao(): TaskDao
    companion object {
        @Volatile
        private var Instance: TaskDatabase? = null

        fun getDatabase(context: Context): TaskDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "task_Database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}

