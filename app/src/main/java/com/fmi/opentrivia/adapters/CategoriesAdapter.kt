package com.fmi.opentrivia.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filterable
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.fmi.opentrivia.R
import com.fmi.opentrivia.fragments.CategoryListDirections
import com.fmi.opentrivia.models.Category
import kotlin.properties.Delegates
import android.widget.Filter
import java.util.*
import kotlin.collections.ArrayList

class CategoriesAdapter(private val categories: List<Category>) :
    RecyclerView.Adapter<CategoriesAdapter.ViewHolder>(), Filterable {

    var categoryListFiltered: List<Category> = categories

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.categoryName.text = categoryListFiltered[position].name
        holder.categoryId = categoryListFiltered[position].id
    }

    override fun getItemCount() = categoryListFiltered.size;

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

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""

                categoryListFiltered =
                    if (charString.isEmpty()) categories as ArrayList<Category> else {
                        val filteredList = ArrayList<Category>()
                        categories
                            .filter {
                            it.name.toLowerCase(Locale.ROOT)
                                    .contains(constraint!!.toString().toLowerCase(Locale.ROOT))
                            }
                            .forEach { filteredList.add(it) }
                        filteredList

                    }
                Log.d(
                    "msi",
                    "Filtered by $constraint, old length ${categories.size}, " +
                            "new length ${categoryListFiltered.size}"
                )
                return FilterResults().apply { values = categoryListFiltered }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                categoryListFiltered = if (results?.values == null) {
                    ArrayList()
                } else {
                    results.values as ArrayList<Category>
                }
                notifyDataSetChanged()
            }
        }
    }
}