package com.cggcoding.models;

import java.util.List;

/**
 * Created by cgrid_000 on 8/19/2015.
 */
public class TaskSet {
    private int taskSetID;
    private List<Task> taskList;
    private int repetitions;
    private int repetitionsCompleted;

    public TaskSet(int taskSetID, int repetitions) {
        this.taskSetID = taskSetID;
        this.repetitions = repetitions;
        this.repetitionsCompleted = 0;
    }

    public int getTaskSetID() {
        return taskSetID;
    }

    public void addTaskToSet(Task task){
        //task.setTaskSetID(taskSetID);
    }

    public int getRepetitions() {
        return repetitions;
    }

    public int getRepetitionsCompleted() {
        return repetitionsCompleted;
    }

    public int repetitionsRemaining(){
        return repetitions - repetitionsCompleted;
    }

    //This is called each time one or more repetitions of a task is performed.  A task is not complete until all the repetitions are performed
    public void performed(int timesPerformed){
        repetitionsCompleted += timesPerformed;

        //if(repetitionsCompleted == repetitions){
        //    markComplete();
        //}
    }

    public void performanceRemoved(int timesRemoved){
        if(repetitionsCompleted - timesRemoved >=0){
            repetitionsCompleted -= timesRemoved;
        } else {
            repetitionsCompleted = 0;
        }

        //if(isCompleted()){
        //    markIncomplete();
        //}

    }

    //This is for tasks with multiple repetitions. Returns true if one but not all of the repetitions have been performed.
    public boolean inProgress(){
        if(repetitionsCompleted > 0 && repetitionsCompleted < repetitions){
            return true;
        } else {
            return false;
        }
    }
}
