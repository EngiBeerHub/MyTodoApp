package com.example.mytodoapp.ui

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodoapp.data.Task
import com.example.mytodoapp.databinding.TaskRowBinding
import com.example.mytodoapp.ui.viewmodels.TaskListViewModel

/**
 * Adapter for simple RecyclerView
 */
class TaskAdapter(
    private val viewModel: TaskListViewModel,
    private val onItemClicked: (Task) -> Unit
) : PagingDataAdapter<Task, TaskAdapter.TaskViewHolder>(diffCallback) {

    class TaskViewHolder(
        private val viewModel: TaskListViewModel,
        private val binding: TaskRowBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task?) {
            binding.apply {
                task?.let {
                    // set values for views
                    taskIsDone.isChecked = it.isDone
                    etTitle.apply {
                        text = it.title
                        if (it.isDone) {
                            changeTitleAppearance(it.isDone)
                        }
                    }
                    // handle tap the check box
                    taskIsDone.setOnCheckedChangeListener { _, isChecked ->
                        changeTitleAppearance(isChecked)
                    }
                    taskIsDone.setOnClickListener {
                        // update Room database
                        viewModel.doneUndoneTask(task, taskIsDone.isChecked)
                    }
                }
            }
        }

        // change appearance of title corresponding check box
        private fun changeTitleAppearance(isChecked: Boolean) {
            binding.apply {
                if (isChecked) {
                    etTitle.apply {
                        paint.apply {
                            flags = Paint.STRIKE_THRU_TEXT_FLAG
                            isAntiAlias = true
                        }
                    }
                } else {
                    etTitle.apply {
                        paint.apply {
                            flags = Paint.ANTI_ALIAS_FLAG
                            isAntiAlias = true
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            viewModel,
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