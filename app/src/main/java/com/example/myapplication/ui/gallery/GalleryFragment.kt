package com.example.myapplication.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.FragmentGalleryBinding
import com.github.kittinunf.fuel.Fuel
import org.json.JSONArray

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private lateinit var productAdapter: ProductAdapter
    private val productList = mutableListOf<Product>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Configuração do RecyclerView
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        productAdapter = ProductAdapter(productList)
        recyclerView.adapter = productAdapter

        fetchProducts()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fetchProducts() {
        val endpointUrl = "https://fakestoreapi.com/products"

        Fuel.get(endpointUrl).responseString { _, _, result ->
            result.fold(
                success = { data ->
                    val jsonArray = JSONArray(data)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val product = Product(
                            id = jsonObject.getInt("id"),
                            title = jsonObject.getString("title"),
                            price = jsonObject.getDouble("price"),
                            description = jsonObject.getString("description"),
                            category = jsonObject.getString("category"),
                            image = jsonObject.getString("image"),
                            rating = jsonObject.getJSONObject("rating").getDouble("rate")
                        )
                        productList.add(product)
                    }

                    activity?.runOnUiThread {
                        productAdapter.notifyDataSetChanged()
                    }
                },
                failure = { error ->
                    error.printStackTrace()

                    val errorMessage = error.localizedMessage
                    activity?.runOnUiThread {
                        Toast.makeText(
                            requireContext(),
                            "Erro na chamada GET: $errorMessage",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
        }
    }
}
