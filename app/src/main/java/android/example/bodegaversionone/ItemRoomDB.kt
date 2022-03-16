package android.example.bodegaversionone

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// annotation to declare a Room Database with a table of our MyDataItem class
@Database(entities = arrayOf(MyDataItem::class), version = 1, exportSchema = false)
public abstract class ItemRoomDB : RoomDatabase() {

    // creating a function to hold the DAO
    abstract fun itemDAO(): ItemDAO

    companion object {
        // singleton design structure using our ItemRoomDB class
        @Volatile
        private var INSTANCE : ItemRoomDB? = null

        fun getDatabase(context : Context) : ItemRoomDB {
            // checking to see if not null and returning it
            // otherwise create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ItemRoomDB::class.java,
                    "item_database"
                ).build()
                INSTANCE = instance
                // return the instance
                instance
            }
        }
    }
}