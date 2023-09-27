package com.example.flavorfinderkotlin.data.repository

import android.app.Application
import com.example.flavorfinderkotlin.data.api.RetrofitInstance
import com.example.flavorfinderkotlin.data.db.MealDatabase
import com.example.flavorfinderkotlin.data.model.Meal
import kotlinx.coroutines.flow.Flow

class MealRepository(private val application: Application) {

    private val mealDao = MealDatabase.getDatabase(application).mealDao()

    suspend fun fetchData() = RetrofitInstance.api.getFood()
    suspend fun fetchById(id: String) = RetrofitInstance.api.getFoodById(id)

    val readData: Flow<List<Meal>> = mealDao.getAllMeals()
    suspend fun insert(meal: Meal) = mealDao.insert(meal)
    suspend fun delete(meal: Meal) = mealDao.delete(meal)
}