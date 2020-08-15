package com.example.crew.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.example.crew.data.Hero
import com.example.crew.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        homeViewModel.listOfHeroes.observe(viewLifecycleOwner) { list ->
            if (list.isNotEmpty()) {
                binding.loadingText.visibility = View.GONE
                setRecyclerView(list)
            } else binding.loadingText.visibility = View.VISIBLE

        }
    }

    private fun setRecyclerView(list: List<Hero>) {
        val adapter = HomeAdapter()
        binding.recyclerView.adapter = adapter
        adapter.list = list
        adapter.onItemClick = { hero ->
            Toast.makeText(requireContext(), hero.name, Toast.LENGTH_SHORT).show()
        }
    }
}