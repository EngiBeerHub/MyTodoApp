package com.example.mytodoapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodoapp.data.Task
import com.example.mytodoapp.databinding.TaskRowBinding

/**
 * Adapter for simple RecyclerView
 */
class TaskAdapter(private val taskListData: List<Task>) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(private var binding: TaskRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.apply {
                taskIsDone.isChecked = task.isDone
                taskTitle.setText(task.title)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            TaskRowBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = taskListData[position]
        holder.bind(currentTask)
    }

    override fun getItemCount() = taskListData.size

}