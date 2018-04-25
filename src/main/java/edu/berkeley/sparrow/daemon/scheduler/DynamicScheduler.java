package edu.berkeley.sparrow.daemon.scheduler;

import edu.berkeley.sparrow.thrift.THostPort;

import java.util.HashMap;

public class DynamicScheduler {

    /**
     * DynamicScheduler implementation.
     */
    public static  DynamicScheduler mDynamicScheduler = null;

    private HashMap<String, THostPort> taskStatus = new HashMap<String, THostPort>();

    public static DynamicScheduler getInstance()
    {
        if(mDynamicScheduler == null){
            mDynamicScheduler = new DynamicScheduler();
        }

        return mDynamicScheduler;

    }


    public synchronized void addTaskStatus(String backend, THostPort timeTaken){
        taskStatus.put(backend, timeTaken);
    }


    public THostPort getTaskStatus(String requestId) {

        return taskStatus.get(requestId);
    }
}
