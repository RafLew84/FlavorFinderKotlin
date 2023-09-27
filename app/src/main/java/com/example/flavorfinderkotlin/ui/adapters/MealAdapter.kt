package com.example.flavorfinderkotlin.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.flavorfinderkotlin.data.model.Meal
import com.example.flavorfinderkotlin.databinding.ListItemRvBinding

class MealAdapter(
    private val onClick: (String) -> Unit,
    itemComparator: MealComparator
) : ListAdapter<Meal, MealViewHolder>(itemComparator) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        return MealViewHolder(
            onClick,
            ListItemRvBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    fun getItemAt(position: Int): Meal{
        return getItem(position)
    }
}