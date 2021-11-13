package com.geckosoftlabs.mentorr.features.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.geckosoftlabs.mentorr.utils.FirebaseUtils
import com.geckosoftlabs.mentorr.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {
    private val TAG= "DashboardFragment"
    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //uploadData()
        readData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun uploadData(){
        // create a dummy data
        val hashMap = hashMapOf<String, Any>(
            "name" to "John doe",
            "city" to "Nairobi",
            "age" to 24
        )

        // use the add() method to create a document inside users collection
        FirebaseUtils().fireStoreDatabase.collection("users")
            .add(hashMap)
            .addOnSuccessListener {
                Log.d(TAG, "Added document with ID ${it.id}")
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error adding document $exception")
            }
    }

    private fun readData(){
            FirebaseUtils().fireStoreDatabase.collection("users")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    querySnapshot.forEach { document ->
                        Log.d(TAG, "Read document with ID ${document.id} ${document.data.toString()}")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents $exception")
                }
    }
}