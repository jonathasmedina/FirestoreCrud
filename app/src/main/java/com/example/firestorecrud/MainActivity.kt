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
    private val db = FirebaseFirestore.getInstance() // ou private val db = Firebase.firestore equivalente Kotlin (importar ktx)
    private val livros = mutableListOf<Livro>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
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
        getLivros()
    }

    private fun getLivros() {
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
    }

    private fun deleteLivro(livro: Livro) {
        livro.id?.let { // let é uma função de escopo que executa o bloco { } somente se o valor não for nulo (quando usada com ?.);
                        //Dentro do bloco, o valor é acessado através da palavra-chave it.
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