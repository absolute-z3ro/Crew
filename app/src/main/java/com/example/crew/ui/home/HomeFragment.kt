package com.example.crew.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import com.example.crew.R
import com.example.crew.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupFirebase()
        return binding.root
    }

    private fun setupFirebase() {
        homeViewModel.firebaseQueryLiveData.observe(viewLifecycleOwner) { dataSnapshot ->
            if (dataSnapshot != null) {
                lifecycleScope.launch(Dispatchers.Main) {
                    homeViewModel.list = homeViewModel.getList(dataSnapshot)
                    binding.loadingText.visibility = View.GONE
                    setRecyclerView()
                }
            } else {
                homeViewModel.list = emptyList()
                navigateToRetry()
            }
        }
    }

    private fun setRecyclerView() {
        val adapter = HomeAdapter()
        binding.recyclerView.adapter = adapter
        adapter.list = homeViewModel.list
        adapter.onItemClick = { hero ->
            Toast.makeText(requireContext(), hero.name, Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToRetry() {
        homeViewModel.list = emptyList()
        Navigation.findNavController(requireActivity(), R.id.nav_host)
            .navigate(R.id.action_home_to_retry)
    }

    companion object {
        private const val TAG = "HomeFragment"
    }
}