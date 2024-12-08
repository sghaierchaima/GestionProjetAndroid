package com.example.gestionprojet

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AddProjectActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_project)

        sharedPreferences = getSharedPreferences("ProjectData", MODE_PRIVATE)

        val etTitle: EditText = findViewById(R.id.etTitle)
        val etDescription: EditText = findViewById(R.id.etDescription)
        val etDeadline: EditText = findViewById(R.id.etDeadline)
        val btnSaveProject: Button = findViewById(R.id.btnSaveProject)

        btnSaveProject.setOnClickListener {
            val title = etTitle.text.toString()
            val description = etDescription.text.toString()
            val deadline = etDeadline.text.toString()

            if (title.isNotEmpty() && description.isNotEmpty() && deadline.isNotEmpty()) {
                val project = Project(title, description, deadline)
                saveProject(project)
                Toast.makeText(this, "Projet ajouté", Toast.LENGTH_SHORT).show()
                val intent = Intent().apply {
                    putExtra("NEW_PROJECT", title) // Passer le titre du projet
                }
                setResult(RESULT_OK, intent)
                finish() // Retourner à la page principale
            } else {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Sauvegarder le projet dans SharedPreferences
    private fun saveProject(project: Project) {
        val projectsList = loadProjects()
        projectsList.add(project.title) // Sauvegarder uniquement le titre ici
        val editor = sharedPreferences.edit()
        val json = gson.toJson(projectsList)
        editor.putString("projects", json)
        editor.apply()
    }

    // Charger la liste des projets depuis SharedPreferences
    private fun loadProjects(): MutableList<String> {
        val projectsSet = sharedPreferences.getStringSet("projects", HashSet())
        val projectsList = mutableListOf<String>()
        projectsSet?.let {
            projectsList.addAll(it)
        }
        return projectsList
    }

    data class Project(val title: String, val description: String, val deadline: String)
}