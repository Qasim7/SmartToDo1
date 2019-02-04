package com.example.qasim.smarttodo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
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
//    private int mPosition;

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
        if (currentTask.getDescription().isEmpty())
            taskView.description.setVisibility(View.GONE);
        taskView.description.setText(currentTask.getDescription());
        taskView.title.setText(currentTask.getTitle());
        if (taskView.checkBox.isChecked())
            taskView.title.setPaintFlags(taskView.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        taskView.startTime.setText(currentTask.getStartTime());
        if (currentTask.getFinishTime() == null)
            taskView.finishTime.setVisibility(View.GONE);
        taskView.finishTime.setText(currentTask.getFinishTime());
        taskView.view.setBackgroundColor(currentTask.getColour());
        taskView.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewTaskActivity.class); //taski update etmek ucun
                intent.putExtra("id",currentTask.getId());
                intent.putExtra("title",currentTask.getTitle());
                intent.putExtra("description",currentTask.getDescription());
                intent.putExtra("startTime",currentTask.getStartTime());
                intent.putExtra("color",currentTask.getColour());
                intent.putExtra("position",position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (tasks == null)
            return 0;
        return tasks.size();
    }



    public void deleteItem(int position) {
//        mPosition = position;
        task = tasks.get(position);
        tasks.remove(position);
        AppDatabase.getDatabase(context).taskDao().delete(task);
        notifyItemRemoved(position);
//        showUndoSnackbar();   //uno delete snackbar yazdim ama exception gosterdi

    }

//    private void showUndoSnackbar() {
//        View view=mainActivity.findViewById(R.id.coordinatorLayout);
//        Snackbar snackbar=Snackbar.make(view,"Task deleted",Snackbar.LENGTH_LONG);
//        snackbar.setAction("UNDO", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tasks.add(mPosition,task);
//                AppDatabase.getDatabase(context).taskDao().insert(task);
//                notifyItemInserted(mPosition);
//            }
//        });
//        snackbar.show();
//        snackbar.setActionTextColor(Color.GREEN);
//    }


    public void updateTaskListItems(List<Task> tasks) {
        final TaskDiffCallback diffCallback = new TaskDiffCallback(this.tasks, tasks);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        this.tasks.clear();
        this.tasks.addAll(tasks);
        diffResult.dispatchUpdatesTo(this);
    }

    public class TaskView extends RecyclerView.ViewHolder {
        View view;
        TextView title, description, startTime, finishTime;
        CheckBox checkBox;
        RelativeLayout parentLayout;

        public TaskView(@NonNull View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.colourTask);
            startTime = itemView.findViewById(R.id.startTime);
            finishTime = itemView.findViewById(R.id.finishTime);
            title = itemView.findViewById(R.id.titleTask);
            description = itemView.findViewById(R.id.descriptionTask);
            checkBox = itemView.findViewById(R.id.checkbox);
            parentLayout = itemView.findViewById(R.id.parentLayout);
        }
    }

}
