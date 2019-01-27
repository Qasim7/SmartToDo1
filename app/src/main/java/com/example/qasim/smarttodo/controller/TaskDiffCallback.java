package com.example.qasim.smarttodo.controller;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.example.qasim.smarttodo.model.Task;

import java.util.List;

public class TaskDiffCallback extends DiffUtil.Callback {

    private final List<Task> mOldTaskList;
    private final List<Task> mNewTaskList;

    public TaskDiffCallback(List<Task> mOldTaskList, List<Task> mNewTaskList) {
        this.mOldTaskList = mOldTaskList;
        this.mNewTaskList = mNewTaskList;
    }

    @Override
    public int getOldListSize() {
        return mOldTaskList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewTaskList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldTaskList.get(oldItemPosition).getId()==mNewTaskList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final Task oldTask=mOldTaskList.get(oldItemPosition);
        final Task newTask=mNewTaskList.get(newItemPosition);
        return oldTask.getTitle().equals(newTask.getTitle());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
