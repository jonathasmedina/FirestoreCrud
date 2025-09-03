package com.example.firestorecrud

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LivroAdapter

    private lateinit var progressBar: ProgressBar


    private val db = FirebaseFirestore.getInstance() // ou private val db = Firebase.firestore equivalente Kotlin (importar ktx)

    private val livros = mutableListOf<Livro>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)


        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = LivroAdapter(livros,
            onDeleteClick = { deleteLivro(it) }, //variáveis estão declaradas como parâmetros do construtor da classe LivroAdapter.
            onEditClick = { editLivro(it) }
        )

        recyclerView.adapter = adapter

        val fabAdd: FloatingActionButton = findViewById(R.id.fabAdd)
        fabAdd.setOnClickListener {
            startActivity(Intent(this, AddLivroActivity::class.java))
        }

        progressBar.visibility = View.VISIBLE
        progressBar.progress = 0


        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                getLivros()
            }
            progressBar.visibility = View.GONE
        }
    }

    private suspend fun getLivros() {

        db.collection("livros")
            .get()
            .addOnSuccessListener { result ->
                livros.clear()
                for (doc in result) { //doc é um objeto do tipo DocumentSnapshot, que representa um documento no Firestore.
                    val livro = doc.toObject(Livro::class.java) //livro é um objeto do tipo Livro (classe criada anteriormente)
                    livro.id = doc.id
                    livros.add(livro)
                }
                adapter.updateList(livros)
            }
        for (i in 1..1000 step 1) {
            withContext(Dispatchers.Main) {
                progressBar.progress = i
            }
        }
    }

    private fun deleteLivro(livro: Livro) {
        livro.id?.let { // let é uma função de escopo que executa o bloco { } somente se o valor não for nulo (quando usada com ?.);
                        //Dentro do bloco, o valor é acessado através da palavra-chave it.
            db.collection("livros").document(it)
                .delete()
                .addOnSuccessListener { CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                        getLivros()
                    }
                }
                }
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
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                getLivros()
            }
        }
    }
}

/*
 Firestore :
	•	Coleções (collections)
São como “pastas”. Exemplo: "livros", "usuarios", "pedidos".
	•	Documentos (documents)
Cada item dentro de uma coleção é um documento JSON-like (chave/valor).
Cada documento tem um ID único (gerado automaticamente ou definido por você).
	•	Subcoleções
Dentro de um documento você ainda pode ter outras coleções.
 */