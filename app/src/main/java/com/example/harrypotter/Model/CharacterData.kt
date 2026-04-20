package com.example.harrypotter.Model

data class CharacterData(
    val id: String?,
    val name: String?,
    val house: String?,
    val ancestry: String?,
    val patronus: String?,
    val image: String?,
    val wizard: Boolean?,
    val hogwartsStudent: Boolean?,
    val hogwartsStaff: Boolean?,
    val species: String?,
    val alternate_names: List<String>,
    val dateOfBirth: String?,
    val actor: String?,
    val wand: Wand?
)

data class Wand(
    val core: String?
)