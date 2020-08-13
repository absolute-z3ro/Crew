package com.example.crew.ui.retry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.crew.databinding.FragmentRetryBinding

class RetryFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRetryBinding.inflate(inflater, container, false)
        binding.retry.setOnClickListener {
            Toast.makeText(requireContext(), "retry", Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }
}