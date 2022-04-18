package com.fmi.opentrivia.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.fmi.opentrivia.R
import com.fmi.opentrivia.fragments.CategoryListDirections
import com.fmi.opentrivia.models.Category
import kotlin.properties.Delegates
import kotlin.reflect.typeOf

class CategoriesAdapter(private val categories: List<Category>) :
    RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.categoryName.text = categories[position].name
        holder.categoryId = categories[position].id
    }

    override fun getItemCount() = categories.size;

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val categoryName: TextView = itemView.findViewById(R.id.category_name)
        var categoryId by Delegates.notNull<Int>()

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            Log.d("msi", "Clicked category $categoryId")
            val action = CategoryListDirections.actionCategoryListToGame(categoryId)
            view.findNavController().navigate(action)
        }
    }
}