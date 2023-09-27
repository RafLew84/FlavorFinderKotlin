package com.example.flavorfinderkotlin.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.flavorfinderkotlin.data.model.Meal
import com.example.flavorfinderkotlin.data.model.MealResponse
import com.example.flavorfinderkotlin.data.repository.MealRepository
import com.example.flavorfinderkotlin.util.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.Response

class MealViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MealRepository(application)

    private var _meals: MutableStateFlow<Resource<MealResponse>> = MutableStateFlow(Resource.Loading())
    val meals: StateFlow<Resource<MealResponse>> = _meals

    private var _meal: MutableStateFlow<Resource<MealResponse>> = MutableStateFlow(Resource.Loading())
    val meal: StateFlow<Resource<MealResponse>> = _meal

    val localMeals: StateFlow<List<Meal>> = repository.readData.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )

    init {
        fetchData()
    }

    private fun fetchData() = viewModelScope.launch {
        _meals.value = Resource.Loading()
        val response = repository.fetchData()
        delay(2000L)
        _meals.value = handleMealResponse(response)
    }

    private fun handleMealResponse(response: Response<MealResponse>)
            : Resource<MealResponse> {
        if (response.isSuccessful)
            response.body()?.let { return Resource.Success(it) }
        return Resource.Error(response.message())
    }

    fun fetchById(id: String) = viewModelScope.launch {
        _meal.value = Resource.Loading()
        val response = repository.fetchById(id)
        delay(2000L)
        _meal.value = handleMealResponse(response)
    }

    fun insert(meal: Meal) = viewModelScope.launch {
        repository.insert(meal)
    }

    fun delete(meal: Meal) = viewModelScope.launch {
        repository.delete(meal)
    }

    fun checkIfExistInLocalDb(meal: Meal): Boolean = meal in localMeals.value
}