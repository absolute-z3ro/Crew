package com.example.crew.ui.home

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.example.crew.data.Hero
import com.example.crew.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private var list = emptyList<Hero>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setConnectivityCheck()
        return binding.root
    }

    override fun onStop() {
        super.onStop()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }


    private fun setConnectivityCheck() {
        connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                Log.d(TAG, "NetworkAvailable")
                requireActivity().runOnUiThread { setupFirebase() }
            }

            override fun onLost(network: Network) {
                Log.d(TAG, "NetworkLost")
                requireActivity().runOnUiThread { navigateToRetry() }
            }

            override fun onUnavailable() {
                Log.d(TAG, "This callback method is rekt and doesn't work.")
            }
        }

        if (Build.VERSION.SDK_INT > 23)
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        else {
            if (connectivityManager.activeNetworkInfo?.isConnected == true) setupFirebase()
            else navigateToRetry()
        }

        val timer = Timer()
        val timerTask = object : TimerTask() {
            override fun run() {
                timer.cancel()
                if (list.isEmpty()) {
                    homeViewModel.dataSnapshotLiveData.stopListener()
                    navigateToRetry()
                }
            }
        }
        timer.schedule(timerTask, 5000L)
    }


    private fun setupFirebase() {
        homeViewModel.dataSnapshotLiveData.observe(viewLifecycleOwner) { dataSnapshot ->
            if (dataSnapshot != null) {
                lifecycleScope.launch(Dispatchers.Main) {
                    list = homeViewModel.getList(dataSnapshot)
                    binding.loadingText.visibility = View.GONE
                    setRecyclerView(list)
                }
            } else {
                Navigation.findNavController(requireActivity(), R.id.nav_host)
                    .navigate(R.id.action_home_to_retry)
            }
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

    private fun navigateToRetry() {
        Navigation.findNavController(requireActivity(), R.id.nav_host)
            .navigate(R.id.action_home_to_retry)
    }

    companion object {
        private const val TAG = "HomeFragment"
    }
}