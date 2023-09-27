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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flavorfinderkotlin.R
import com.example.flavorfinderkotlin.databinding.FragmentFoodListBinding
import com.example.flavorfinderkotlin.ui.adapters.MealAdapter
import com.example.flavorfinderkotlin.ui.adapters.MealComparator
import com.example.flavorfinderkotlin.util.Resource
import com.example.flavorfinderkotlin.viewmodel.MealViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FoodListFragment : Fragment() {
    private lateinit var binding: FragmentFoodListBinding

    private val viewModel: MealViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFoodListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MealAdapter(
            onClick = {
                viewModel.fetchById(it)
                Navigation.findNavController(requireView()).navigate(
                    FoodListFragmentDirections.actionFoodListFragmentToDetailFragment()
                )

            },
            MealComparator()
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.meals.collectLatest{ response ->
                    when (response) {
                        is Resource.Success -> {
                            hideProgressBar()
                            response.data?.let { res ->
                                adapter.submitList(res.meals)
                            }
                        }
                        is Resource.Error -> {
                            hideProgressBar()
                            response.message?.let { Log.e("FoodList", "Error occurred: $it") }
                        }
                        is Resource.Loading -> showProgressBar()
                    }
                }
            }
        }

        binding.foodRV.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun hideProgressBar(){
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar(){
        binding.progressBar.visibility = View.VISIBLE
    }
}