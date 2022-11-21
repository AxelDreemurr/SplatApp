package com.axeldreemurr.splatapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.axeldreemurr.splatapp.AddActivity
import com.axeldreemurr.splatapp.FetchingActivity
import com.axeldreemurr.splatapp.GearFetchingActivity
import com.axeldreemurr.splatapp.databinding.FragmentHomeBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        binding.btnSave2.setOnClickListener{
            val intent = Intent (activity, FetchingActivity::class.java)
            activity?.startActivity(intent)
        }

        binding.btnSave3.setOnClickListener{
            val intent = Intent (activity, GearFetchingActivity::class.java)
            activity?.startActivity(intent)
        }
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}