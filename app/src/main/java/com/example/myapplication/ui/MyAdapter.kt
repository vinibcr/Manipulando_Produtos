package com.example.myapplication.ui.Manipulacao

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

data class MyData(val id: Int, val nome: String, val descricao: String, val preco: Int)

class MyAdapter(private val dataList: List<MyData>, private val deleteProductCallback: (Int) -> Unit) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        val textViewDescription: TextView = itemView.findViewById(R.id.textViewDescription)
        val textViewPrice: TextView = itemView.findViewById(R.id.textViewPrice)
        val buttonDeleteProduct: Button = itemView.findViewById(R.id.buttonDeleteProduct)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = dataList[position]
        holder.textViewName.text = item.nome
        holder.textViewDescription.text = item.descricao
        holder.textViewPrice.text = item.preco.toString()
        holder.buttonDeleteProduct.setOnClickListener {
            deleteProductCallback(item.id)
        }
    }

    override fun getItemCount() = dataList.size
}
