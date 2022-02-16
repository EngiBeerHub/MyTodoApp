package com.example.mytodoapp.ui

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodoapp.R
import com.example.mytodoapp.data.Task
import com.example.mytodoapp.databinding.TaskRowBinding
import com.google.android.material.textfield.TextInputEditText

/**
 * Adapter for RecyclerView with Paging
 */
class TaskPagedListAdapter :
    PagedListAdapter<Task, TaskPagedListAdapter.TaskPagedViewHolder>(diffCallback) {
    class TaskPagedViewHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {
        fun bindTo(task: Task?) {
            val cbIsDone: CheckBox = view.findViewById(R.id.task_is_done)
            val tvTitle: TextInputEditText = view.findViewById(R.id.task_title)
            task?.apply {
                cbIsDone.isChecked = task.isDone
                tvTitle.setText(task.title)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskPagedViewHolder {
        val adapterLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.task_row, parent, false)
        return TaskPagedViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: TaskPagedViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem == newItem
            }

        }
    }
}