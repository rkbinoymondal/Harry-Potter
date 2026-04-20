package com.example.harrypotter

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.harrypotter.APIFetching.RetrofitObject
import com.example.harrypotter.Adapters.CharacterAdapter
import com.example.harrypotter.Database.AnalyticsData
import com.example.harrypotter.Database.LoginDAO
import com.example.harrypotter.Database.LoginData
import com.example.harrypotter.Database.OverallDatabase
import com.example.harrypotter.Model.CharacterData
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginPage : AppCompatActivity() {

    lateinit var database: OverallDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_page)

        val house = findViewById<Spinner>(R.id.houseChooser)

        val houseList = listOf<String>(
            "Gryffindor 🦁",
            "Slytherin 🐍",
            "Ravenclaw 🦅",
            "Hufflepuff 🦡"
        )

        val houseAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, houseList)

        houseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        house.adapter = houseAdapter

        val role = findViewById<Spinner>(R.id.roleChooser)

        val roleList = listOf<String>(
            "Student 🎓",
            "Staff 🏫"
        )

        val roleAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roleList)

        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        role.adapter = roleAdapter

        val wand = findViewById<Spinner>(R.id.wandChooser)

        val wandList = listOf<String>(
            "Oak",
            "Holly",
            "Elder",
            "Yew",
            "Willow",
            "Phoenix Feather",
            "Dragon Heartstring",
            "Unicorn Hair",
            "Vine",
            "Ebony"
        )

        val wandAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, wandList)

        wandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        wand.adapter = wandAdapter


        val patronus = findViewById<Spinner>(R.id.patronusChooser)

        val patronusList = listOf<String>(
            "Stag 🦌",
            "Otter 🦦",
            "Phoenix 🔥",
            "Wolf 🐺",
            "Dog 🐶",
            "Cat 🐱",
            "Hare 🐇",
            "Horse 🐎",
            "Doe 🦌",
            "Swan 🦢"
        )

        val patronusAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, patronusList)

        patronusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        patronus.adapter = patronusAdapter

        val name = findViewById<TextInputEditText>(R.id.etName)

        val verifyBtn = findViewById<MaterialButton>(R.id.btnVerify)

        val continueBtn = findViewById<MaterialButton>(R.id.btnContinue)

        var tvStatus = findViewById<TextView>(R.id.tvStatus)

        var isVerified = false

        fun resetVerification() {
            isVerified = false
            tvStatus.text = "Not Verified"
            tvStatus.setTextColor(resources.getColor(android.R.color.holo_red_dark))
        }

        name.addTextChangedListener { resetVerification() }

        verifyBtn.setOnClickListener {
            val patronusData = patronus.selectedItem.toString()
            val wandData = wand.selectedItem.toString()
            val houseData = house.selectedItem.toString()
            val roleData = role.selectedItem.toString()
            if (name.text.toString().trim().isNullOrEmpty()) {
                name.error = "Please enter your name"
                return@setOnClickListener
            }
            if (houseData.isNullOrEmpty()) {
                return@setOnClickListener
            }
            if (wandData.isNullOrEmpty()) {
                return@setOnClickListener
            }
            if (patronusData.isNullOrEmpty()) {
                return@setOnClickListener
            }
            if (roleData.isNullOrEmpty()) {
                return@setOnClickListener
            }
            isVerified = true
            if (isVerified) {
                tvStatus.text = "Verified"
                tvStatus.setTextColor(resources.getColor(android.R.color.holo_green_dark))
            }
        }

        continueBtn.setOnClickListener {
            if (tvStatus.text == "Verified") {
                val patronusData = patronus.selectedItem.toString()
                val wandData = wand.selectedItem.toString()
                val houseData = house.selectedItem.toString()
                val roleData = role.selectedItem.toString()
                database = OverallDatabase.getDatabase(this)
                CoroutineScope(Dispatchers.IO).launch {
                    database.loginDao().insertLogin(
                        LoginData(
                            0,
                            name.text.toString().trim(),
                            houseData,
                            wandData,
                            patronusData,
                            roleData
                        )
                    )
                }
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                tvStatus.text = "Please verify first"
                tvStatus.setTextColor(resources.getColor(android.R.color.holo_red_dark))
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}