package com.example.harrypotter.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.harrypotter.APIFetching.RetrofitObject
import com.example.harrypotter.Adapters.CharacterAdapterBig
import com.example.harrypotter.Adapters.FavoriteAdapter
import com.example.harrypotter.Database.AnalyticsData
import com.example.harrypotter.Database.OverallDatabase
import com.example.harrypotter.Model.CharacterData
import com.example.harrypotter.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharacterFragment : Fragment(R.layout.fragment_character) {

    lateinit var characterList: RecyclerView

    lateinit var adapt: CharacterAdapterBig

    lateinit var database1: OverallDatabase

    val TAG = "CharacterFragment"

    val fullList = mutableListOf<CharacterData>()
    val filteredList = mutableListOf<CharacterData>()

    lateinit var textSearch: TextInputEditText

    lateinit var emptyData: LinearLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        characterList = view.findViewById<RecyclerView>(R.id.characterList)

        emptyData = view.findViewById<LinearLayout>(R.id.emptyData)

        database1 = OverallDatabase.getDatabase(requireContext())

        getCharacterAll()

        textSearch = view.findViewById<TextInputEditText>(R.id.etName)

        textSearch.addTextChangedListener {
            updateList(textSearch.text.toString())
        }

        val filterBtn = view.findViewById<MaterialButton>(R.id.filterBtn)

        filterBtn.setOnClickListener {
            val layoutOpen = layoutInflater.inflate(R.layout.filter_card, null)

            val dialog = android.app.AlertDialog.Builder(requireContext())
                .setView(layoutOpen)
                .create()

            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            val house = layoutOpen.findViewById<Spinner>(R.id.houseSpinner)
            val ancestry = layoutOpen.findViewById<Spinner>(R.id.ancestrySpinner)

            val checkWizard = layoutOpen.findViewById<CheckBox>(R.id.isWizard)
            val checkStudent = layoutOpen.findViewById<CheckBox>(R.id.isStudent)
            val checkStaff = layoutOpen.findViewById<CheckBox>(R.id.isStaff)

            val applyBtn = layoutOpen.findViewById<MaterialButton>(R.id.btnApply)

            val houseList = listOf(
                "All",
                "Gryffindor",
                "Slytherin",
                "Ravenclaw",
                "Hufflepuff"
            )

            val houseAdapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, houseList)

            houseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            house.adapter = houseAdapter

            val ancestryList = listOf(
                "All",
                "pure-blood",
                "half-blood",
                "muggleborn"
            )

            val ancestryAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                ancestryList
            )

            ancestry.adapter = ancestryAdapter

            applyBtn.setOnClickListener {

                val ansList = mutableListOf<CharacterData>()

                val houseData = house.selectedItem.toString()

                val ancestryData = ancestry.selectedItem.toString()

                for (character in fullList) {

                    var check = true

                    if (houseData != "All" && (character.house?.contains(
                            houseData,
                            true
                        ) != true)
                    ) {
                        check = false
                    }

                    if (ancestryData != "All" && (character.ancestry?.contains(
                            ancestryData,
                            true
                        ) != true)
                    ) {
                        check = false
                    }

                    if (checkWizard.isChecked && (character.wizard != true)) {
                        check = false
                    }

                    if (checkStudent.isChecked && (character.hogwartsStudent != true)) {
                        check = false
                    }

                    if (checkStaff.isChecked && (character.hogwartsStaff != true)) {
                        check = false
                    }

                    if (check) {
                        ansList.add(character)
                    }

                }

                filteredList.clear()
                filteredList.addAll(ansList)

                adapt.notifyDataSetChanged()

                dialog.dismiss()
            }
        }

        val sortBtn = view.findViewById<MaterialButton>(R.id.sortBtn)

        sortBtn.setOnClickListener {
            val newLayout = layoutInflater.inflate(R.layout.sort_card, null)

            val dialogBox =
                android.app.AlertDialog.Builder(requireContext()).setView(newLayout).create()

            dialogBox.show()
            dialogBox.window?.setBackgroundDrawableResource(android.R.color.transparent)

            val AZ = newLayout.findViewById<MaterialButton>(R.id.sortAZBtn)
            val ZA = newLayout.findViewById<MaterialButton>(R.id.sortZABtn)
            val houseBtn = newLayout.findViewById<MaterialButton>(R.id.houseBtn)

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
            houseBtn.setOnClickListener {
                filteredList.clear()
                filteredList.addAll(fullList)
                filteredList.sortBy {
                    val nameSet = it.house?.lowercase()

                    if (nameSet.isNullOrEmpty()) {
                        "zzzzzz"
                    } else {
                        nameSet
                    }
                }
                adapt.notifyDataSetChanged()
                dialogBox.dismiss()
            }


        }
    }

    private fun getCharacterAll() {

        emptyData.visibility = View.VISIBLE
        characterList.visibility = View.GONE

        val startTime = System.currentTimeMillis()

        val data = RetrofitObject.characterInstance.getAllCharacter()
        data.enqueue(object : Callback<List<CharacterData>> {
            override fun onResponse(
                call: Call<List<CharacterData>?>,
                response: Response<List<CharacterData>?>
            ) {
                if (response.isSuccessful) {
                    emptyData.visibility = View.GONE
                    characterList.visibility = View.VISIBLE

                    val endTime = System.currentTimeMillis()
                    val responseTime = endTime - startTime

                    CoroutineScope(Dispatchers.IO).launch {
                        database1.analyticsDao().insertAnalytics(
                            AnalyticsData(
                                0, "Characters", true, responseTime,
                                System.currentTimeMillis()
                            )
                        )
                    }

                    val characters = response.body()
                    if (characters != null) {
                        val resultList = mutableListOf<CharacterData>()

                        for (character in characters) {
                            if (character.species?.contains("human", true) == true) {
                                resultList.add(character)
                            }
                        }

                        filteredList.clear()
                        filteredList.addAll(resultList)

                        fullList.clear()
                        fullList.addAll(resultList)

                        adapt = CharacterAdapterBig(requireContext(), filteredList, view!!)
                        characterList.adapter = adapt
                        characterList.layoutManager =
                            LinearLayoutManager(requireContext())
                    }

                } else {

                    emptyData.visibility = View.GONE
                    characterList.visibility = View.VISIBLE

                    Log.d(TAG, response.errorBody().toString())

                    val endTime = System.currentTimeMillis()
                    val responseTime = endTime - startTime

                    CoroutineScope(Dispatchers.IO).launch {
                        database1.analyticsDao().insertAnalytics(
                            AnalyticsData(
                                0, "Characters", false, responseTime,
                                System.currentTimeMillis()
                            )
                        )
                    }
                }
            }

            override fun onFailure(call: Call<List<CharacterData>?>, t: Throwable) {

                emptyData.visibility = View.GONE
                characterList.visibility = View.VISIBLE

                Log.d(TAG, "Error fetching API", t)

                val endTime = System.currentTimeMillis()
                val responseTime = endTime - startTime

                CoroutineScope(Dispatchers.IO).launch {
                    database1.analyticsDao().insertAnalytics(
                        AnalyticsData(
                            0, "Characters", false, responseTime,
                            System.currentTimeMillis()
                        )
                    )
                }
            }
        })
    }

    private fun updateList(query: String) {
        val result = mutableListOf<CharacterData>()

        for (character in fullList) {
            if (character.name?.contains(query, true) == true ||
                character.house?.contains(query, true) == true ||
                character.ancestry?.contains(query, true) == true ||
                character.patronus?.contains(query, true) == true
            ) {

                result.add(character)
            }

        }
        filteredList.clear()
        filteredList.addAll(result)
        adapt.notifyDataSetChanged()
    }
}