package com.example.crew.ui.retry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.crew.R
import com.example.crew.databinding.FragmentRetryBinding

class RetryFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRetryBinding.inflate(inflater, container, false)
        binding.retry.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.nav_host)
                .navigate(R.id.action_retry_to_home)
        }
        return binding.root
    }
}