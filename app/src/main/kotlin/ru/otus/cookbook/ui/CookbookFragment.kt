package ru.otus.cookbook.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import ru.otus.cookbook.R
import ru.otus.cookbook.data.RecipeListItem
import ru.otus.cookbook.databinding.FragmentCookbookBinding

class CookbookFragment : Fragment(), Listener {

    private val binding = FragmentBindingDelegate<FragmentCookbookBinding>(this)
    private val model: CookbookFragmentViewModel by viewModels { CookbookFragmentViewModel.Factory }
    private val adapter: CookbookAdapter by lazy { CookbookAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.bind(
        container,
        FragmentCookbookBinding::inflate
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        viewLifecycleOwner.lifecycleScope.launch {
            model.recipeList
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect(::onRecipeListUpdated)
        }
    }

    private fun setupRecyclerView() = binding.withBinding {
        cookbook.adapter = adapter

    }

    private fun onRecipeListUpdated(recipeList: List<RecipeListItem>) {
        adapter.submitList(recipeList)
    }

    override fun onItemClicked(id: Int) {
        Log.d("onItemClicked", id.toString())
        val action = CookbookFragmentDirections.actionCookbookFragmentToRecipeFragment(id)
        findNavController().navigate(action)
    }
}