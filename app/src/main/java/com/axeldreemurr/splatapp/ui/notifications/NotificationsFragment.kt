package com.axeldreemurr.splatapp.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.axeldreemurr.splatapp.GearFetchingActivity
import com.axeldreemurr.splatapp.databinding.FragmentNotificationsBinding
import com.axeldreemurr.splatapp.ui.pokelist.PokeListActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.system.exitProcess

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnPokeapi.setOnClickListener{
            val intent = Intent (activity, PokeListActivity::class.java)
            activity?.startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            Firebase.auth.signOut()
            System.exit(0)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}