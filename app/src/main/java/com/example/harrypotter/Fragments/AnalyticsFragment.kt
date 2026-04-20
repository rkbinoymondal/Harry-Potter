package com.example.harrypotter.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.harrypotter.Adapters.FavoriteAdapter
import com.example.harrypotter.Database.OverallDatabase
import com.example.harrypotter.R
import kotlinx.coroutines.CoroutineScope

class AnalyticsFragment : Fragment(R.layout.fragment_analytics) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var totalAPI = view.findViewById<TextView>(R.id.totalAPI)
        var successAPI = view.findViewById<TextView>(R.id.successAPI)
        var failureAPI = view.findViewById<TextView>(R.id.failureAPI)
        var avgTime = view.findViewById<TextView>(R.id.averageTime)

        val database = OverallDatabase.getDatabase(requireContext())
        database.analyticsDao().getAnalyticsAll().observe(viewLifecycleOwner, {
            totalAPI.text = "Total Requests: ${it.size}"
            var sum: Long = 0
            for (data in it) {
                sum += data.responseTime!!
            }
            val milliSec = sum / it.size
            val sec = milliSec / 1000
            val milSec = milliSec % 1000
            avgTime.text = "Avg Time: ${sec}.${milSec} sec"
        })
        database.analyticsDao().getAnalyticsAllisSuccess().observe(viewLifecycleOwner, {
            successAPI.text = "Success: ${it.size}"
        })
        database.analyticsDao().getAnalyticsAllisFailure().observe(viewLifecycleOwner, {
            failureAPI.text = "Error: ${it.size}"
        })
    }
}