package com.example.flavorfinderkotlin.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.flavorfinderkotlin.data.model.Meal
import com.example.flavorfinderkotlin.databinding.ListItemRvBinding

class MealViewHolder(
    private val onClick: (String) -> Unit,
    private val binding: ListItemRvBinding
)
    : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Meal){
            binding.name.text = item.strMeal
            binding.image.load(item.strMealThumb)
            binding.root.setOnClickListener { onClick(item.idMeal) }
        }
}