package com.example.harrypotter.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.harrypotter.APIFetching.RetrofitObject
import com.example.harrypotter.Adapters.CharacterAdapterBig
import com.example.harrypotter.Adapters.FavoriteAdapter
import com.example.harrypotter.Adapters.SpellAdapterBig
import com.example.harrypotter.Database.AnalyticsData
import com.example.harrypotter.Database.OverallDatabase
import com.example.harrypotter.Model.CharacterData
import com.example.harrypotter.Model.SpellData
import com.example.harrypotter.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SpellFragment : Fragment(R.layout.fragment_spell) {

    lateinit var spellList: RecyclerView

    lateinit var adapt: SpellAdapterBig

    val TAG = "SpellFragment"

    val fullList = mutableListOf<SpellData>()
    val filteredList = mutableListOf<SpellData>()

    lateinit var textSearch: TextInputEditText

    lateinit var database1: OverallDatabase

    lateinit var emptyData: LinearLayout


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spellList = view.findViewById<RecyclerView>(R.id.spellList)

        database1 = OverallDatabase.getDatabase(requireContext())

        emptyData = view.findViewById<LinearLayout>(R.id.emptyData)

        textSearch = view.findViewById<TextInputEditText>(R.id.etName)

        textSearch.addTextChangedListener {
            updateList(textSearch.text.toString())
        }

        getSpellAll()

        val sortBtn = view.findViewById<MaterialButton>(R.id.sortBtn)

        sortBtn.setOnClickListener {
            val newLayout = layoutInflater.inflate(R.layout.sort_card_spell, null)

            val dialogBox =
                android.app.AlertDialog.Builder(requireContext()).setView(newLayout).create()

            dialogBox.show()
            dialogBox.window?.setBackgroundDrawableResource(android.R.color.transparent)

            val AZ = newLayout.findViewById<MaterialButton>(R.id.sortAZBtn)
            val ZA = newLayout.findViewById<MaterialButton>(R.id.sortZABtn)

            AZ.setOnClickListener {
                filteredList.clear()
                filteredList.addAll(fullList)
                filteredList.sortBy {
                    val nameSet = it.name?.lowercase()

                    if (nameSet.isNullOrEmpty()) {
                        "zzzzzz"
                    } else {
                        nameSet
                    }
                }
                adapt.notifyDataSetChanged()
                dialogBox.dismiss()
            }
            ZA.setOnClickListener {
                filteredList.clear()
                filteredList.addAll(fullList)
                filteredList.sortByDescending {
                    val nameSet = it.name?.lowercase()

                    if (nameSet.isNullOrEmpty()) {
                        ""
                    } else {
                        nameSet
                    }
                }
                adapt.notifyDataSetChanged()
                dialogBox.dismiss()
            }
        }
    }

    private fun getSpellAll() {

        emptyData.visibility = View.VISIBLE
        spellList.visibility = View.GONE

        val startTime = System.currentTimeMillis()

        val data = RetrofitObject.characterInstance.getAllSpell()
        data.enqueue(object : Callback<List<SpellData>> {
            override fun onResponse(
                call: Call<List<SpellData>?>,
                response: Response<List<SpellData>?>
            ) {
                if (response.isSuccessful) {

                    emptyData.visibility = View.GONE
                    spellList.visibility = View.VISIBLE

                    val endTime = System.currentTimeMillis()
                    val responseTime = endTime - startTime

                    CoroutineScope(Dispatchers.IO).launch {
                        database1.analyticsDao().insertAnalytics(
                            AnalyticsData(
                                0, "Spells", true, responseTime,
                                System.currentTimeMillis()
                            )
                        )
                    }

                    val spells = response.body()
                    if (spells != null) {

                        filteredList.clear()
                        filteredList.addAll(spells)

                        fullList.clear()
                        fullList.addAll(spells)

                        adapt = SpellAdapterBig(requireContext(), filteredList)
                        spellList.adapter = adapt
                        spellList.layoutManager = LinearLayoutManager(requireContext())
                    }
                } else {
                    emptyData.visibility = View.GONE
                    spellList.visibility = View.VISIBLE

                    Log.d(TAG, response.errorBody().toString())
                    val endTime = System.currentTimeMillis()
                    val responseTime = endTime - startTime
                    CoroutineScope(Dispatchers.IO).launch {
                        database1.analyticsDao().insertAnalytics(
                            AnalyticsData(
                                0, "Spells", false, responseTime,
                                System.currentTimeMillis()
                            )
                        )
                    }
                }
            }

            override fun onFailure(call: Call<List<SpellData>?>, t: Throwable) {

                emptyData.visibility = View.GONE
                spellList.visibility = View.VISIBLE

                Log.d(TAG, "Error fetching API", t)
                val endTime = System.currentTimeMillis()
                val responseTime = endTime - startTime
                CoroutineScope(Dispatchers.IO).launch {
                    database1.analyticsDao().insertAnalytics(
                        AnalyticsData(
                            0, "Spells", false, responseTime,
                            System.currentTimeMillis()
                        )
                    )
                }
            }
        })
    }

    private fun updateList(query: String) {
        val result = mutableListOf<SpellData>()

        for (spell in fullList) {
            if (spell.name?.contains(query, true) == true ||
                spell.description?.contains(query, true) == true
            ) {

                result.add(spell)
            }

        }
        filteredList.clear()
        filteredList.addAll(result)
        adapt.notifyDataSetChanged()
    }
}