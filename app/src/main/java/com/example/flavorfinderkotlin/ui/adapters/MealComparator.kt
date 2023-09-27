package com.example.flavorfinderkotlin.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.flavorfinderkotlin.data.model.Meal

class MealComparator : DiffUtil.ItemCallback<Meal>() {
    override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
        return newItem.strMeal == oldItem.strMeal
    }

    override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
        return newItem.idMeal == oldItem.idMeal
    }
}