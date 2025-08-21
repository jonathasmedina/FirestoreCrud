package com.example.firestorecrud

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class AddLivroActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private var livroId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_livro)

        val etTitulo: EditText = findViewById(R.id.etTitulo)
        val etAutor: EditText = findViewById(R.id.etAutor)
        val etAno: EditText = findViewById(R.id.etAno)
        val btnSalvar: Button = findViewById(R.id.btnSalvar)

        livroId = intent.getStringExtra("LIVRO_ID")
        etTitulo.setText(intent.getStringExtra("TITULO"))
        etAutor.setText(intent.getStringExtra("AUTOR"))
        etAno.setText(intent.getIntExtra("ANO", 0).toString())

        btnSalvar.setOnClickListener {
            val titulo = etTitulo.text.toString()
            val autor = etAutor.text.toString()
            val ano = etAno.text.toString().toIntOrNull() ?: 0

            val livro = hashMapOf(
                "titulo" to titulo,
                "autor" to autor,
                "ano" to ano
            )

            if (livroId == null) {
                db.collection("livros")
                    .add(livro)
                    .addOnSuccessListener { finish() }
            } else {
                db.collection("livros").document(livroId!!)
                    .set(livro)
                    .addOnSuccessListener { finish() }
            }
        }
    }
}