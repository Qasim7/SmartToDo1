package com.example.qasim.smarttodo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.qasim.smarttodo.NewTaskActivity;
import com.example.qasim.smarttodo.R;
import com.example.qasim.smarttodo.database.AppDatabase;
import com.example.qasim.smarttodo.model.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskView> {

    private List<Task> tasks;
    public Context context;
    private Task task;

    public TaskAdapter(List<Task> tasks, Context context) {
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
    public void onBindViewHolder(@NonNull TaskView taskView, final int position) {
        final Task currentTask = tasks.get(position);
        if (currentTask.getDescription().isEmpty()) {
            taskView.description.setVisibility(View.GONE);
        } else {
            taskView.description.setVisibility(View.VISIBLE);
        }
        taskView.description.setText(currentTask.getDescription());
        taskView.title.setText(currentTask.getTitle());
//        if (task.isCompleted())
//            taskView.title.setPaintFlags(taskView.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        taskView.startTime.setText(currentTask.getStartTime());


        switch (currentTask.getFinishTime()) {
            case "":
                taskView.finishTime.setVisibility(View.GONE);
                break;
            default:
                taskView.finishTime.setText(currentTask.getFinishTime());
                taskView.finishTime.setVisibility(View.VISIBLE);

        }
        taskView.cardView.setCardBackgroundColor(currentTask.getColour());

        taskView.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTask(currentTask);
            }
        });
    }

    private void editTask(Task currentTask) {
        Intent intent = new Intent(context, NewTaskActivity.class); //taski update etmek ucun
        intent.putExtra("id", currentTask.getId());
        intent.putExtra("title", currentTask.getTitle());
        intent.putExtra("description", currentTask.getDescription());
        intent.putExtra("startTime", currentTask.getStartTime());
        intent.putExtra("finishTime", currentTask.getFinishTime());
        intent.putExtra("color", currentTask.getColour());
        ((Activity) context).startActivityForResult(intent, 100);
    }

    @Override
    public int getItemCount() {
        if (tasks == null)
            return 0;
        return tasks.size();
    }


    public void deleteItem(int position) {
        task = tasks.get(position);
        tasks.remove(position);
        AppDatabase.getDatabase(context).taskDao().delete(task);
        notifyItemRemoved(position);
//        mainActivity.deleteAlert(position);//interface ile yoxla ve yaxud abstract classla
    }


    public void updateTaskListItems(List<Task> tasks) {
        final TaskDiffCallback diffCallback = new TaskDiffCallback(this.tasks, tasks);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        this.tasks.clear();
        this.tasks.addAll(tasks);
        diffResult.dispatchUpdatesTo(this);
    }

    public class TaskView extends RecyclerView.ViewHolder {
        TextView title, description, startTime, finishTime;
        CheckBox checkBox;
        CardView cardView;
        RelativeLayout parentLayout;

        public TaskView(@NonNull View itemView) {
            super(itemView);
            startTime = itemView.findViewById(R.id.startTime);
            finishTime = itemView.findViewById(R.id.finishTime);
            title = itemView.findViewById(R.id.titleTask);
            description = itemView.findViewById(R.id.descriptionTask);
            checkBox = itemView.findViewById(R.id.checkbox);
            cardView = itemView.findViewById(R.id.cardView);
            parentLayout = itemView.findViewById(R.id.parentLayout);
        }
    }

}
