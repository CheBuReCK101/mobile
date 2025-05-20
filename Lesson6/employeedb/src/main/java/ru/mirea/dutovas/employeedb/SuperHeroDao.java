package ru.mirea.dutovas.employeedb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface SuperHeroDao {
    @Query("SELECT * FROM SuperHero")
    List<SuperHero> getAll();

    @Query("SELECT * FROM SuperHero WHERE id = :id")
    SuperHero getById(long id);

    @Insert
    void insert(SuperHero superHero);

    @Update
    void update(SuperHero superHero);

    @Delete
    void delete(SuperHero superHero);
}