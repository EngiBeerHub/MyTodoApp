package com.example.mytodoapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodoapp.data.Task
import com.example.mytodoapp.databinding.TaskRowBinding

/**
 * Adapter for RecyclerView with Paging
 */
class TaskPagedListAdapter() :
    PagedListAdapter<Task, TaskPagedListAdapter.TaskPagedViewHolder>(diffCallback) {
    class TaskPagedViewHolder(private val binding: TaskRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindTo(task: Task?) {
            binding.apply {
                task?.let {
                    taskIsDone.isChecked = it.isDone
                    taskTitle.setText(it.title)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskPagedViewHolder {
        return TaskPagedViewHolder(
            TaskRowBinding.inflate(LayoutInflater.from(parent.context))
        )
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