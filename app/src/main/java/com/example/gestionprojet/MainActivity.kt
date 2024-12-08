package com.example.gestionprojet

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var adapter: ArrayAdapter<String>
    private val projectsList = mutableListOf<String>()

    private val addProjectLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val newProject = result.data?.getStringExtra("NEW_PROJECT")
                newProject?.let {
                    addProject(it)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("ProjectData", MODE_PRIVATE)
        val listViewProjects: ListView = findViewById(R.id.listViewProjects)
        val btnAddProject: Button = findViewById(R.id.btnAddProject)

        // Initialiser l'adaptateur
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, projectsList)
        listViewProjects.adapter = adapter

        // Charger les projets existants
        loadProjects()

        // Ajouter un projet
        btnAddProject.setOnClickListener {
            val intent = Intent(this, AddProjectActivity::class.java)
            addProjectLauncher.launch(intent)
        }

        // Action lors du clic sur un projet dans la liste
        listViewProjects.setOnItemClickListener { _, _, position, _ ->
            val project = projectsList[position]
            val intent = Intent(this, TaskActivity::class.java)
            intent.putExtra("PROJECT_NAME", project)
            startActivity(intent)
        }
    }

    // Charger les projets depuis SharedPreferences
    private fun loadProjects() {
        // Utiliser getStringSet pour récupérer les projets comme un Set
        val projectsSet = sharedPreferences.getStringSet("projects", HashSet())
        projectsSet?.let {
            projectsList.clear()
            projectsList.addAll(it)
            adapter.notifyDataSetChanged()
        }
    }

    // Sauvegarder les projets dans SharedPreferences
    private fun saveProjects() {
        val projectsSet = HashSet<String>(projectsList)
        sharedPreferences.edit().putStringSet("projects", projectsSet).apply()
    }

    // Ajouter un projet
    private fun addProject(project: String) {
        projectsList.add(project)
        saveProjects()
        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        loadProjects()
    }
}