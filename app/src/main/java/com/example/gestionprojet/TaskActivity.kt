package com.example.gestionprojet

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TaskActivity : AppCompatActivity() {

    private val todoTasks = mutableListOf<String>()
    private val inProgressTasks = mutableListOf<String>()
    private val doneTasks = mutableListOf<String>()

    private lateinit var todoAdapter: TaskAdapter
    private lateinit var inProgressAdapter: TaskAdapter
    private lateinit var doneAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kanban)

        val projectTitle = intent.getStringExtra("projectTitle")
        title = "Tâches pour : $projectTitle"

        val btnAddTask: Button = findViewById(R.id.btnAddTask)
        val recyclerViewTodo: RecyclerView = findViewById(R.id.recyclerViewTodo)
        val recyclerViewInProgress: RecyclerView = findViewById(R.id.recyclerViewInProgress)
        val recyclerViewDone: RecyclerView = findViewById(R.id.recyclerViewDone)

        // Initialisation des adaptateurs
        todoAdapter = TaskAdapter(todoTasks)
        inProgressAdapter = TaskAdapter(inProgressTasks)
        doneAdapter = TaskAdapter(doneTasks)

        // Configuration des RecyclerView
        recyclerViewTodo.layoutManager = LinearLayoutManager(this)
        recyclerViewInProgress.layoutManager = LinearLayoutManager(this)
        recyclerViewDone.layoutManager = LinearLayoutManager(this)

        recyclerViewTodo.adapter = todoAdapter
        recyclerViewInProgress.adapter = inProgressAdapter
        recyclerViewDone.adapter = doneAdapter

        // Ajouter une tâche
        btnAddTask.setOnClickListener {
            val newTask = EditText(this)
            val dialog = AlertDialog.Builder(this)
                .setTitle("Nouvelle Tâche")
                .setView(newTask)
                .setPositiveButton("Ajouter") { _, _ ->
                    if (newTask.text.isNotEmpty()) {
                        todoTasks.add(newTask.text.toString())
                        todoAdapter.notifyDataSetChanged()
                    }
                }
                .setNegativeButton("Annuler", null)
                .create()
            dialog.show()
        }

        // Déplacer la tâche de "À faire" à "En cours"
        todoAdapter.setOnItemClickListener { position ->
            val task = todoTasks[position]
            todoTasks.removeAt(position)
            inProgressTasks.add(task)
            todoAdapter.notifyDataSetChanged()
            inProgressAdapter.notifyDataSetChanged()
        }

        // Déplacer la tâche de "En cours" à "Terminé"
        inProgressAdapter.setOnItemClickListener{ position ->
            val task = inProgressTasks[position]
            inProgressTasks.removeAt(position)
            doneTasks.add(task)
            inProgressAdapter.notifyDataSetChanged()
            doneAdapter.notifyDataSetChanged()
        }
    }
}
