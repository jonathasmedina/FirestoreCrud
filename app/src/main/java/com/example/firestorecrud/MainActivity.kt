package com.example.firestorecrud

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LivroAdapter
    private val db = FirebaseFirestore.getInstance()
    private val livros = mutableListOf<Livro>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = LivroAdapter(livros,
            onDeleteClick = { deleteLivro(it) },
            onEditClick = { editLivro(it) }
        )
        recyclerView.adapter = adapter

        val fabAdd: FloatingActionButton = findViewById(R.id.fabAdd)
        fabAdd.setOnClickListener {
            startActivity(Intent(this, AddLivroActivity::class.java))
        }

        getLivros()
    }

    private fun getLivros() {
        db.collection("livros")
            .get()
            .addOnSuccessListener { result ->
                livros.clear()
                for (doc in result) {
                    val livro = doc.toObject(Livro::class.java)
                    livro.id = doc.id
                    livros.add(livro)
                }
                adapter.updateList(livros)
            }
    }

    private fun deleteLivro(livro: Livro) {
        livro.id?.let {
            db.collection("livros").document(it)
                .delete()
                .addOnSuccessListener { getLivros() }
        }
    }

    private fun editLivro(livro: Livro) {
        val intent = Intent(this, AddLivroActivity::class.java)
        intent.putExtra("LIVRO_ID", livro.id)
        intent.putExtra("TITULO", livro.titulo)
        intent.putExtra("AUTOR", livro.autor)
        intent.putExtra("ANO", livro.ano)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        getLivros()
    }
}