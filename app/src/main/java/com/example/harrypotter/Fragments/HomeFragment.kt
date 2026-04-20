package com.example.harrypotter.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.harrypotter.APIFetching.RetrofitObject
import com.example.harrypotter.Adapters.CharacterAdapter
import com.example.harrypotter.Adapters.CreatureAdapter
import com.example.harrypotter.Adapters.SpellAdapter
import com.example.harrypotter.Database.AnalyticsData
import com.example.harrypotter.Database.OverallDatabase
import com.example.harrypotter.Model.CharacterData
import com.example.harrypotter.Model.SpellData
import com.example.harrypotter.R
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(R.layout.fragment_home) {

    lateinit var characterList: RecyclerView

    lateinit var spellList: RecyclerView

    lateinit var creatureList: RecyclerView

    lateinit var adapt: CharacterAdapter

    lateinit var adapt1: CreatureAdapter
    lateinit var adapt2: SpellAdapter

    val characterSeq = mutableListOf<CharacterData>()

    val creatureSeq = mutableListOf<CharacterData>()

    val TAG = "HomeFragment"

    lateinit var database1: OverallDatabase

    var isCharacterLoaded = false
    var isSpellLoaded = false

    lateinit var shimmer: ShimmerFrameLayout
    lateinit var fullPage: NestedScrollView

    lateinit var headerText: LinearLayout


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database1 = OverallDatabase.getDatabase(requireContext())

        characterList = view.findViewById<RecyclerView>(R.id.characterList)
        creatureList = view.findViewById<RecyclerView>(R.id.creatureList)
        spellList = view.findViewById<RecyclerView>(R.id.spellList)

        fullPage = view.findViewById<NestedScrollView>(R.id.fullPage)
        headerText = view.findViewById<LinearLayout>(R.id.headerText)

        val btnViewAllCharacter = view.findViewById<MaterialButton>(R.id.characterBtnAll)

        btnViewAllCharacter.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, CharacterFragment())
                .addToBackStack(null)
                .commit()
        }

        val btnViewAllSpell = view.findViewById<MaterialButton>(R.id.spellBtnAll)

        btnViewAllSpell.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, SpellFragment())
                .addToBackStack(null)
                .commit()
        }

        val btnViewAllCreature = view.findViewById<MaterialButton>(R.id.creatureBtnAll)

        btnViewAllCreature.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, CreatureFragment())
                .addToBackStack(null)
                .commit()
        }

        shimmer = view.findViewById<ShimmerFrameLayout>(R.id.shimmerMain)

        shimmer.startShimmer()
        shimmer.visibility = View.VISIBLE

        fullPage.visibility = View.GONE

        headerText.visibility = View.GONE

        getCharacterAll()
        getSpellAll()

        adapt1 = CreatureAdapter(requireContext(), creatureSeq, view)
        creatureList.adapter = adapt1
        creatureList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val welcomeText = view.findViewById<TextView>(R.id.welcomeText)
        val houseText = view.findViewById<TextView>(R.id.houseText)

        database1.loginDao().getLoginAll().observe(viewLifecycleOwner, {
            var nameVal = it[it.size - 1].name.toString()
            if (nameVal.isNullOrEmpty()) {
                nameVal = "N/A"
            }
            welcomeText.text = "✨ Welcome, " + nameVal

            var houseVal = it[it.size - 1].house.toString()
            if (houseVal.isNullOrEmpty()) {
                houseVal = "N/A"
            }
            houseText.text = houseVal + " welcomes you"
        })

    }

    private fun getCharacterAll() {

        val startTime = System.currentTimeMillis()
        val data = RetrofitObject.characterInstance.getAllCharacter()

        data.enqueue(object : Callback<List<CharacterData>> {
            override fun onResponse(
                call: Call<List<CharacterData>?>,
                response: Response<List<CharacterData>?>
            ) {
                if (response.isSuccessful) {
                    isCharacterLoaded = true
                    ready()
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
                        for (character in characters) {
                            if (character.species?.contains("human", true) == true) {
                                characterSeq.add(character)
                            } else {
                                creatureSeq.add(character)
                            }
                        }
                        adapt = CharacterAdapter(requireContext(), characterSeq, view!!)
                        characterList.adapter = adapt
                        characterList.layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                    }
                } else {
                    isCharacterLoaded = true
                    ready()
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
                isCharacterLoaded = true
                ready()
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

    private fun getSpellAll() {
        val startTime = System.currentTimeMillis()
        val data = RetrofitObject.characterInstance.getAllSpell()
        data.enqueue(object : Callback<List<SpellData>> {
            override fun onResponse(
                call: Call<List<SpellData>?>,
                response: Response<List<SpellData>?>
            ) {
                if (response.isSuccessful) {
                    isSpellLoaded = true
                    ready()
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
                        adapt2 = SpellAdapter(requireContext(), spells, view!!)
                        spellList.adapter = adapt2
                        spellList.layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                    }
                } else {
                    isSpellLoaded = true
                    ready()
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
                isSpellLoaded = true
                ready()
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

    private fun ready() {
        if (isCharacterLoaded && isSpellLoaded) {
            shimmer.stopShimmer()
            shimmer.visibility = View.GONE
            fullPage.visibility = View.VISIBLE
            headerText.visibility = View.VISIBLE
        }
    }
}