package com.example.myapplication.ui.Manipulacao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.MyDatabaseHelper
import com.example.myapplication.databinding.FragmentHomeBinding

class Manipulacao : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(ManipulacaoViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = MyAdapter(getDataFromSQLite(requireContext())) { productId ->
            deleteProduct(productId)
            recyclerView.adapter = MyAdapter(getDataFromSQLite(requireContext())) { deleteProduct(it) }
        }

        // Configuração do botão de adicionar produto
        val buttonAddProduct: Button = binding.buttonAddProduct
        buttonAddProduct.setOnClickListener {
            val name = binding.editTextName.text.toString().trim()
            val description = binding.editTextDescription.text.toString().trim()
            val price = binding.editTextPrice.text.toString().toDoubleOrNull()

            if (name.isNotEmpty() && description.isNotEmpty() && price != null && price > 0) {
                insertProduct(name, description, price)
                // Atualiza a lista após adicionar o produto
                recyclerView.adapter = MyAdapter(getDataFromSQLite(requireContext())) { deleteProduct(it) }
                binding.editTextName.text.clear()
                binding.editTextDescription.text.clear()
                binding.editTextPrice.text.clear()
                // Exibe mensagem de sucesso
                Toast.makeText(requireContext(), "Produto cadastrado com sucesso", Toast.LENGTH_SHORT).show()
            } else {
                val errorMsg = when {
                    name.isEmpty() -> "O nome do produto não pode ser vazio."
                    description.isEmpty() -> "A descrição do produto não pode ser vazia."
                    price == null -> "Preço inválido. Por favor, insira um valor numérico."
                    price <= 0 -> "O preço deve ser um valor positivo."
                    else -> "Erro desconhecido."
                }
                Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getDataFromSQLite(context: Context): List<MyData> {
        val data = mutableListOf<MyData>()
        val dbHelper = MyDatabaseHelper(context)
        val db: SQLiteDatabase = dbHelper.readableDatabase

        Log.d("Manipulacao", "Querying database for all products")

        val cursor = db.rawQuery("SELECT * FROM produtos", null)

        if (cursor.moveToFirst()) {
            do {
                val item = MyData(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("nome")),
                    cursor.getString(cursor.getColumnIndexOrThrow("descricao")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("preco"))
                )
                data.add(item)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return data
    }

    private fun insertProduct(name: String, description: String, price: Double) {
        val dbHelper = MyDatabaseHelper(requireContext())

        Log.d("Manipulacao", "Inserting product: Name = $name, Description = $description, Price = $price")

        dbHelper.insertProduct(name, description, price)
    }

    private fun deleteProduct(productId: Int) {
        val dbHelper = MyDatabaseHelper(requireContext())
        dbHelper.deleteProduct(productId)
        Toast.makeText(requireContext(), "Produto excluído com sucesso", Toast.LENGTH_SHORT).show()
    }
}
