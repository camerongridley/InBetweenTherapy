package com.cggcoding.factories;

import com.cggcoding.models.Task;
import com.cggcoding.models.tasktypes.CognitiveTask;
import com.cggcoding.models.tasktypes.PsychEdTask;
import com.cggcoding.models.tasktypes.RelaxationTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by cgrid_000 on 8/22/2015.
 */
public class TaskFactory {

    public static Task newInstance(String taskType){
        if(taskType == null){
            return null;
        }
        //TODO 
        switch (taskType){
            case "CognitiveTask":
                System.out.println("In TaskFactory.newInstance() for CognitiveTask");
                //return newCognitiveTask();
                //TODO delete this line - break;
            case "PsychEdTask":
                System.out.println("In TaskFactory.newInstance() for PsychedTask");
                //return newPsychEdTask();
                //TODO delete this line - break;
            case "RelaxationTask":
                System.out.println("In TaskFactory.newInstance() for RelaxationTask");
                //return newRelaxationTask();
                //TODO delete this line - break;

        }

        return null;
    }
    
    private static Task newGeneralTask(){
    	return null;
    }
    
    private static Task newCognitiveTask(){
    	return null;
    }
    
    private static Task newPsychEdTask(){
    	return null;
    }
    
    private static Task newRelaxationTask(){
    	return null;
    }

    public static List<Task> makeCopies(Task task, int numberOfCopies){
        if(task == null){
            return null;
        }

        //create all the copies
        List<Task> taskCopies = new ArrayList<>();
        for(int i = 0; i < numberOfCopies; i++){
            switch (task.getTaskTypeName()){
                case "CognitiveTask":
                    CognitiveTask cogTask = (CognitiveTask)task;
                    taskCopies.add(new CognitiveTask(cogTask.getTaskID(), cogTask.getUserID(), cogTask.getTaskSetID(), cogTask.getName(), cogTask.getDescription(), cogTask.getAutomaticThought(), cogTask.getAlternativeThought(), cogTask.getPreSUDS(), cogTask.getPostSUDS()));
                    break;
                case "PsychEdTask":
                    PsychEdTask psychEdTask = (PsychEdTask)task;
                    taskCopies.add(new PsychEdTask(psychEdTask.getTaskID(), psychEdTask.getUserID(), psychEdTask.getTaskSetID(), psychEdTask.getName(), psychEdTask.getDescription()));
                    break;
                case "RelaxationTask":
                    RelaxationTask relaxTask = (RelaxationTask)task;
                    taskCopies.add(new RelaxationTask(relaxTask.getTaskID(), relaxTask.getUserID(), relaxTask.getTaskSetID(), relaxTask.getName(), relaxTask.getDescription(), relaxTask.getDurationInMinutes()));
                    break;

            }
        }

        //save copies to db and get the ids generated
        //TODO change this to actual db call which should not include a loop here
        Random random = new Random();
        for(Task copy : taskCopies){
            copy.setTaskID(Math.abs(random.nextInt(100000)));
        }

        return taskCopies;
    }

}
