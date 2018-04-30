package edu.berkeley.sparrow.daemon.scheduler;


import edu.berkeley.sparrow.daemon.util.Logging;
import edu.berkeley.sparrow.thrift.THostPort;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DynamicScheduler {

    /**
     * DynamicScheduler implementation.
     */

    private static final Logger LOG = Logger.getLogger(DynamicScheduler.class);
    public static  DynamicScheduler mDynamicScheduler = null;
    private ArrayList<THostPort> backends = new ArrayList<THostPort>();

    /**
     * placeholder for requestID , backends.
     */
    private HashMap<String, THostPort> taskStatus = new HashMap<String, THostPort>();

    /**
     * map worker machine vs (total time taken , total jobs)
     */
    private HashMap<THostPort,Long> totalTimeTaken = new HashMap<THostPort,Long>();
    private HashMap<THostPort,Long> totalTask = new HashMap<THostPort,Long>();


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

    public  void addTimeTaken(String requestId,  long lastExecutionTime){
        LOG.info(Logging.functionCall(requestId,lastExecutionTime));
        if(taskStatus.containsKey(requestId)){
            THostPort host = taskStatus.get(requestId);
            if(totalTimeTaken.containsKey(host)) {
                totalTimeTaken.put(host, totalTimeTaken.get(host) + new Long(lastExecutionTime));
                totalTask.put(host, totalTask.get(host) + new Long(1));
            }else{
                totalTimeTaken.put(host, new Long(lastExecutionTime));
                totalTask.put(host,   new Long(1));
            }
            LOG.info("addTimeTaken : "  + host + " : " + totalTimeTaken.get(host).toString() + ", "  + totalTask.get(host).toString());
        }else{

        }

        LOG.info("backends : " + totalTimeTaken.size() );
        for(Map.Entry<THostPort, Long> entry :  totalTimeTaken.entrySet()){
            LOG.info( entry.getKey() + " : " + entry.getValue() + " : " + ((float)entry.getValue()/(float)totalTask.get(entry.getKey())));
        }


    }

}
