package com.example.qasim.smarttodo.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.qasim.smarttodo.R;
import com.example.qasim.smarttodo.model.Task;

import java.util.List;

public class TaskController extends RecyclerView.Adapter<TaskController.TaskView> {

    private List<Task> tasks;
    private Context context;

    public TaskController(List<Task> tasks, Context context) {
        this.tasks = tasks;
        this.context = context;
    }

    @NonNull
    @Override
    public TaskView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_task, viewGroup, false);

        return new TaskView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskView taskView, int i) {
        Task currentTask = tasks.get(i);
        taskView.description.setText(currentTask.getDescription());
        taskView.title.setText(currentTask.getTitle());
        taskView.startTime.setText(currentTask.getStartTime());
        taskView.finishTime.setText(currentTask.getFinishTime());
//        taskView.view.setBackgroundColor(Color.parseColor(currentTask.getColour()));
    }

    @Override
    public int getItemCount() {
        if (tasks == null)
            return 0;
        return tasks.size();
    }

    public void updateTaskListItems(List<Task> tasks){
        final TaskDiffCallback diffCallback=new TaskDiffCallback(this.tasks,tasks);
        final DiffUtil.DiffResult diffResult= DiffUtil.calculateDiff(diffCallback);
        this.tasks.clear();
        this.tasks.addAll(tasks);
        diffResult.dispatchUpdatesTo(this);
    }

    public class TaskView extends RecyclerView.ViewHolder {
        //        View view;
        TextView title, description, startTime, finishTime;

        public TaskView(@NonNull View itemView) {
            super(itemView);
//            view=itemView.findViewById(R.id.colourTask);
            startTime = itemView.findViewById(R.id.startTime);
            finishTime = itemView.findViewById(R.id.finishTime);
            title = itemView.findViewById(R.id.titleTask);
            description = itemView.findViewById(R.id.descriptionTask);
        }
    }

}
