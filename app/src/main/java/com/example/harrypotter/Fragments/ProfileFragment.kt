package com.example.harrypotter.Fragments

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.harrypotter.APIFetching.RetrofitObject
import com.example.harrypotter.Adapters.CharacterAdapter
import com.example.harrypotter.Adapters.ProfileAdapter
import com.example.harrypotter.Database.AnalyticsData
import com.example.harrypotter.Database.OverallDatabase
import com.example.harrypotter.LoginPage
import com.example.harrypotter.Model.CharacterData
import com.example.harrypotter.R
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    lateinit var database1: OverallDatabase

    lateinit var characterList: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database1 = OverallDatabase.getDatabase(requireContext())

        database1.loginDao().getLoginAll().observe(viewLifecycleOwner, {
            if (it.size != 0) {
                val last = it[it.size - 1]
                val name = view.findViewById<TextView>(R.id.profileName)
                name.text = last.name.toString()

                val profileStudentHouse = view.findViewById<TextView>(R.id.profilestudentHouse)
                profileStudentHouse.text = last.role.toString() + " at " + last.house.toString()

                val wand = view.findViewById<TextView>(R.id.wandTextNew)
                wand.text = last.wand.toString()

                val patronusText = view.findViewById<TextView>(R.id.patronusText)
                patronusText.text = last.patronus.toString()

                val houseDetail = last.house.toString()

                val photoSet = when (houseDetail) {
                    "Gryffindor 🦁" -> R.drawable.gryffindor
                    "Slytherin 🐍" -> R.drawable.slytherin
                    "Ravenclaw 🦅" -> R.drawable.ravenclaw
                    "Hufflepuff 🦡" -> R.drawable.hufflepuff
                    else -> R.drawable.hogwart_castle_new
                }

                val photoProfile = view.findViewById<ImageView>(R.id.photoProfile)

                val sharedPref = requireContext().getSharedPreferences("ImageHolder", MODE_PRIVATE)

                val currentImage = sharedPref.getInt("defaultImage", 0)
                val currentImage1 = sharedPref.getString("defaultImage1", "Binoy")

                if (currentImage == 0 && currentImage1 == "Binoy") {
                    photoProfile.setImageResource(photoSet)
                } else if (currentImage == 0) {
                    Glide.with(requireContext()).load(currentImage1).into(photoProfile)
                } else if (currentImage1 == "Binoy") {
                    photoProfile.setImageResource(currentImage)
                }
            }
        })

        database1.favoriteDao().getFavoriteAll().observe(viewLifecycleOwner, {
            val favChar = view.findViewById<TextView>(R.id.favChar)
            favChar.text = "Favorite Character: ${it.size}"
        })
        database1.favoriteDaoSpell().getFavoriteAllSpell().observe(viewLifecycleOwner, {
            val favSpell = view.findViewById<TextView>(R.id.favSpell)
            favSpell.text = "Favorite Spell: ${it.size}"
        })
        database1.favoriteDaoCreature().getFavoriteAllCreature().observe(viewLifecycleOwner, {
            val favCreature = view.findViewById<TextView>(R.id.favCreature)
            favCreature.text = "Favorite Creature: ${it.size}"
        })

        val logout = view.findViewById<MaterialButton>(R.id.logout)

        logout.setOnClickListener {

            val layout = layoutInflater.inflate(R.layout.logout_card, null)

            val yesBtn = layout.findViewById<MaterialButton>(R.id.btnYES)
            val noBtn = layout.findViewById<MaterialButton>(R.id.btnNO)

            val dialog = android.app.AlertDialog.Builder(requireContext()).setView(layout).create()

            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            yesBtn.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    database1.favoriteDao().deleteFavoriteAll()
                    database1.favoriteDaoCreature().deleteFavoriteCreatureAll()
                    database1.favoriteDaoSpell().deleteFavoriteSpellAll()
                    database1.loginDao().deleteLoginAll()
                }
                val sharedPref = requireContext().getSharedPreferences("ImageHolder", MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.remove("defaultImage")
                editor.remove("defaultImage1")
                editor.apply()
                val intent = Intent(requireContext(), LoginPage::class.java)
                startActivity(intent)
                requireActivity().finish()
                dialog.dismiss()
            }
            noBtn.setOnClickListener {
                dialog.dismiss()
            }
        }


        val editBtn = view.findViewById<MaterialButton>(R.id.editBtn)
        editBtn.setOnClickListener {
            getCharacterAll()
            val layoutProfile =
                layoutInflater.inflate(R.layout.profile_image_setter_background, null)

            val dialog =
                android.app.AlertDialog.Builder(requireContext()).setView(layoutProfile).create()

            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            characterList = layoutProfile.findViewById<RecyclerView>(R.id.profileList)

            val setBtn = layoutProfile.findViewById<MaterialButton>(R.id.setBtn)
            setBtn.setOnClickListener {
                dialog.dismiss()
            }

        }
    }

    fun getCharacterAll() {
        val TAG = "ProfileFragment"
        val startTime = System.currentTimeMillis()
        val data = RetrofitObject.characterInstance.getAllCharacter()
        data.enqueue(object : Callback<List<CharacterData>> {
            override fun onResponse(
                call: Call<List<CharacterData>?>,
                response: Response<List<CharacterData>?>
            ) {
                if (response.isSuccessful) {
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
                        val adapt = ProfileAdapter(requireContext(), characters, view!!)
                        characterList.adapter = adapt
                        characterList.layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                    }
                } else {
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
}