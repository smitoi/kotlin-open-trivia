package com.fmi.opentrivia.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fmi.opentrivia.R


class AnswersAdapter(
    private val answers: List<String>,
    private val onItemClicked: (position: Int) -> Unit
) :
    RecyclerView.Adapter<AnswersAdapter.ViewHolder>() {

    private var selectedItem: Int = -1

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.answer_row, parent, false)
        return ViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.answerText.text = answers[position]
        holder.answerRadioButton.isChecked = position == selectedItem
    }

    override fun getItemCount() = answers.size;

    inner class ViewHolder(itemView: View, private val onItemClicked: (position: Int) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        val answerText: TextView = itemView.findViewById(R.id.answer_text)
        val answerRadioButton: RadioButton = itemView.findViewById(R.id.answer_radio)

        init {
            answerRadioButton.setOnClickListener {
                onItemClicked(bindingAdapterPosition)
                selectedItem = bindingAdapterPosition
                notifyDataSetChanged()
            }
        }
    }
}