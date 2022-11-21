package com.axeldreemurr.splatapp.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.axeldreemurr.splatapp.AddActivity
import com.axeldreemurr.splatapp.AddGearActivity
import com.axeldreemurr.splatapp.SignUpActivity
import com.axeldreemurr.splatapp.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        dashboardViewModel.text.observe(viewLifecycleOwner) {
        }

        binding.btnSave.setOnClickListener{
            val intent = Intent (activity, AddActivity::class.java)
            activity?.startActivity(intent)
        }

        binding.btnSave4.setOnClickListener{
            val intent = Intent (activity, AddGearActivity::class.java)
            activity?.startActivity(intent)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}