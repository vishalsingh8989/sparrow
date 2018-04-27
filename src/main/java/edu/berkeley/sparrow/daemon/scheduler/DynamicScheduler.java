package edu.berkeley.sparrow.daemon.scheduler;


import edu.berkeley.sparrow.daemon.util.Logging;
import edu.berkeley.sparrow.thrift.THostPort;
import javafx.util.Pair;
import org.apache.log4j.Logger;

import java.util.HashMap;

public class DynamicScheduler {

    /**
     * DynamicScheduler implementation.
     */

    private static final Logger LOG = Logger.getLogger(DynamicScheduler.class);
    public static  DynamicScheduler mDynamicScheduler = null;

    /**
     * placeholder for requestID , backends.
     */
    private HashMap<String, THostPort> taskStatus = new HashMap<String, THostPort>();

    /**
     * map worker machine vs (total time taken , total jobs)
     */
    private HashMap<THostPort,Pair<Long, Long>> timeTaken = new HashMap<THostPort,Pair<Long, Long>>();


    /**
     * Singleton class for dynamic scheduler.
     * @return instance of Dynamic class.
     */
    public static DynamicScheduler getInstance() {
        if(mDynamicScheduler == null){
            mDynamicScheduler = new DynamicScheduler();
        }
        return mDynamicScheduler;
    }

    /**
     * add value to taskStatus .
     * @param requestId :  requestId
     * @param host :  THostPort
     */
    public synchronized void addTaskStatus(String requestId, THostPort host){
        LOG.info(Logging.functionCall(requestId, host));
        taskStatus.put(requestId, host);
    }

    /**
     * get value for taskStatus.
     * @param requestId
     * @return
     */
    public THostPort getTaskStatus(String requestId) {
        LOG.info(Logging.functionCall(requestId));
        return taskStatus.get(requestId);
    }

    /**
     * add time taken by worker machine to total time taken and increment the number of jobs for that worker.
     * use this data to calculate average time taken by worker machine for dynamic scheduling based on average time.
     * @param host: worker machine
     * @param lastExecutionTime: time taken by worker machine to execute last task
     */

    public  void addTimeTaken(THostPort host,  long lastExecutionTime){
        LOG.info(Logging.functionCall(host,lastExecutionTime));
        if(timeTaken.containsKey(host)){
            Pair<Long, Long> mPair = timeTaken.get(host);
            Long key = mPair.getKey();
            Long value = mPair.getValue();
            timeTaken.put(host, new Pair<Long, Long>(new Long(lastExecutionTime) + key, new Long(1) + value));
        }else{
            timeTaken.put(host, new Pair<Long, Long>(new Long(lastExecutionTime), new Long(1)));
        }

        LOG.info(host + " : " + timeTaken.get(host).toString());

    }

}
