package com.example.mytodoapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodoapp.R
import com.example.mytodoapp.data.Task
import com.google.android.material.textfield.TextInputEditText

class TaskAdapter(private val taskListData: List<Task>) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val cbIsDone: CheckBox = view.findViewById(R.id.task_is_done)
        val tvTitle: TextInputEditText = view.findViewById(R.id.task_title)
//        val tvContent: TextView = view.findViewById(R.id.task_content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val adapterLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.task_row, parent, false)
        return TaskViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskListData[position]
        holder.cbIsDone.isChecked = task.isDone
        holder.tvTitle.setText(task.title)
//        holder.tvContent.text = task.content
    }

    override fun getItemCount() = taskListData.size

}