package com.example.mytodoapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodoapp.data.Task
import com.example.mytodoapp.databinding.TaskRowBinding

/**
 * Adapter for simple RecyclerView
 */
class TaskAdapter(private val onItemClicked: (Task) -> Unit) :
    PagingDataAdapter<Task, TaskAdapter.TaskViewHolder>(diffCallback) {

    class TaskViewHolder(private val binding: TaskRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task?) {
            binding.apply {
                task?.let {
                    taskIsDone.isChecked = it.isDone
                    taskTitle.text = it.title
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            TaskRowBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = getItem(position)
        holder.itemView.setOnClickListener { onItemClicked(currentTask!!) }
        holder.bind(currentTask)
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