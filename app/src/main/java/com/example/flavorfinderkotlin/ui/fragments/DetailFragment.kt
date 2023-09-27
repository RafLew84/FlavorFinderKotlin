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
import coil.load
import com.example.flavorfinderkotlin.R
import com.example.flavorfinderkotlin.data.model.Meal
import com.example.flavorfinderkotlin.databinding.FragmentDetailBinding
import com.example.flavorfinderkotlin.util.Resource
import com.example.flavorfinderkotlin.viewmodel.MealViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding

    private val viewModel: MealViewModel by activityViewModels()
    private val TAG = "FoodDetailFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.meal.collectLatest{ response ->
                    when (response) {
                        is Resource.Success -> {
                            hideProgressBar()
                            response.data?.let { res ->
                                val item = res.meals.first()
                                inflate(item)
                                binding.favoriteButton.setOnClickListener {
                                    viewModel.insert(item)
                                }
                                if (viewModel.checkIfExistInLocalDb(item))
                                    binding.favoriteButton.visibility = View.INVISIBLE
                            }
                        }
                        is Resource.Error -> {
                            hideProgressBar()
                            response.message?.let { Log.e(TAG, "Error occurred: $it") }
                        }
                        is Resource.Loading -> showProgressBar()
                    }
                }
            }
        }
    }

    private fun inflate(item: Meal) {
        binding.foodImage.load(item.strMealThumb)
        binding.category.text = item.strCategory
        binding.title.text = item.strMeal
        binding.instructions.text = item.strInstructions
    }

    private fun hideProgressBar(){
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar(){
        binding.progressBar.visibility = View.VISIBLE
    }
}