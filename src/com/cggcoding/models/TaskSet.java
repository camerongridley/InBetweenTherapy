package com.cggcoding.models;


import java.util.Random;

/**
 * Created by cgrid_000 on 8/19/2015.
 */
public class TaskSet implements Completable{
    private int taskSetID;
    private int stageID;
    private int repetitions;
    private int repetitionsCompleted;
    private boolean completed;

    private TaskSet(int taskSetID, int stageID, int repetitions) {
        this.taskSetID = taskSetID;
        this.stageID = stageID;
        this.repetitions = repetitions;
        this.repetitionsCompleted = 0;
    }

    private TaskSet(int stageID, int repetitions) {
        this.taskSetID = -1;
        this.stageID = stageID;
        this.repetitions = repetitions;
        this.repetitionsCompleted = 0;
    }

    //static factory method
    public static TaskSet newInstance(int stageID, int repetitions){
        //TODO replace Random with call to DB to create TaskSet and get the autoincrement ID
        //for now will use a random number to set the taskID
        Random rand = new Random(1000);
        int taskSetID = rand.nextInt();
        return new TaskSet(taskSetID, stageID, repetitions);
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

    public void completedRepetition(){
        repetitionsCompleted++;
    }

    //TODO or do I just make this method dynamic by looking up all tasks in set and doing calculations? -
    //This is called each time one or more repetitions of a task is performed.  A taskSet is not complete until all the repetitions are performed
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

    @Override
    public void markComplete() {
        completed = true;
    }

    @Override
    public void markIncomplete() {
        completed = false;
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }

    @Override
    public int getPercentComplete() {
        return (int)((double)repetitionsCompleted/(double)repetitions);//TODO make sure this works as intended or if there is a better method
    }
}
