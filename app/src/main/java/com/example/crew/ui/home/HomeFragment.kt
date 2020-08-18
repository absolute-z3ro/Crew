package com.example.crew.ui.home

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.os.Handler
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
import kotlin.concurrent.timerTask

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private val handler = Handler()
    private val connectivityTimer = Timer()
    private var list = emptyList<Hero>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setConnectionTimeout()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setConnectivityCheck()
    }

    override fun onStop() {
        super.onStop()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    private fun setConnectionTimeout() {
        connectivityTimer.schedule(timerTask {
            Log.d(TAG, "Network Unavailable")
            connectivityTimer.cancel()
            if (list.isEmpty()) {
                navigateToRetry()
            }
        }, 5000L)
    }


    private fun setConnectivityCheck() {
        var navigationPending = false
        val navigate = Runnable {
            navigateToRetry()
            navigationPending = false
        }

        connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                Log.d(TAG, "Network Available")
                requireActivity().runOnUiThread {
                    connectivityTimer.cancel()
                    if (navigationPending) handler.removeCallbacks(navigate)
                    else setupFirebase()
                    navigationPending = false
                }
            }

            override fun onLost(network: Network) {
                Log.d(TAG, "Network Lost")
                requireActivity().runOnUiThread {
                    handler.postDelayed(navigate, 10000L)
                    navigationPending = true
                }
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