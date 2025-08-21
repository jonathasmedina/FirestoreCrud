package com.example.firestorecrud

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LivroAdapter(
    private var livroList: MutableList<Livro>,
    private val onDeleteClick: (Livro) -> Unit,
    private val onEditClick: (Livro) -> Unit
) : RecyclerView.Adapter<LivroAdapter.LivroViewHolder>() {

    class LivroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitulo: TextView = itemView.findViewById(R.id.tvTitulo)
        val tvAutor: TextView = itemView.findViewById(R.id.tvAutor)
        val tvAno: TextView = itemView.findViewById(R.id.tvAno)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
        val btnEdit: Button = itemView.findViewById(R.id.btnEdit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LivroViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_livro, parent, false)
        return LivroViewHolder(view)
    }

    override fun onBindViewHolder(holder: LivroViewHolder, position: Int) {
        val livro = livroList[position]
        holder.tvTitulo.text = livro.titulo
        holder.tvAutor.text = "Autor: ${livro.autor}"
        holder.tvAno.text = "Ano: ${livro.ano}"

        holder.btnDelete.setOnClickListener { onDeleteClick(livro) }
        holder.btnEdit.setOnClickListener { onEditClick(livro) }
    }

    override fun getItemCount() = livroList.size

    fun updateList(newList: MutableList<Livro>) {
        livroList = newList
        notifyDataSetChanged()
    }
}