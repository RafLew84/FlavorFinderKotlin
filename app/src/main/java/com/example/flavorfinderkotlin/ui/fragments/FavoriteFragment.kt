package com.example.flavorfinderkotlin.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flavorfinderkotlin.R
import com.example.flavorfinderkotlin.databinding.FragmentFavoriteBinding
import com.example.flavorfinderkotlin.databinding.FragmentFoodListBinding
import com.example.flavorfinderkotlin.ui.adapters.MealAdapter
import com.example.flavorfinderkotlin.ui.adapters.MealComparator
import com.example.flavorfinderkotlin.util.Resource
import com.example.flavorfinderkotlin.viewmodel.MealViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding

    private val viewModel: MealViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MealAdapter(
            onClick = {
                viewModel.fetchById(it)
                Navigation.findNavController(requireView()).navigate(
                    FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment()
                )

            },
            MealComparator()
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.localMeals.collectLatest{ meals -> adapter.submitList(meals) }
            }
        }

        binding.favoriteRV.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        swipeToDelete(adapter)
    }

    private fun swipeToDelete(adapter: MealAdapter) {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.delete(adapter.getItemAt(viewHolder.absoluteAdapterPosition))
            }
        }).attachToRecyclerView(binding.favoriteRV)
    }
}