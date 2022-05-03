package watermelon.focus.model.module

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import watermelon.focus.model.database.StarDao
import watermelon.focus.model.database.StarDatabase
import javax.inject.Singleton

/**
 * description ： TODO:类的作用
 * author : Watermelon02
 * email : 1446157077@qq.com
 * date : 2022/5/1 19:23
 */
@Module
@InstallIn(SingletonComponent::class)
object StarModule {

    @Singleton
    @Provides
    fun provideStarDatabase(application: Application): StarDatabase {
        return Room.databaseBuilder(application, StarDatabase::class.java, "todo_database").build()
    }

    @Provides
    @Singleton
    fun provideStarDao(starDatabase: StarDatabase): StarDao {
        return starDatabase.starDao()
    }
}