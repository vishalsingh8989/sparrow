package edu.berkeley.sparrow.daemon.scheduler;

import edu.berkeley.sparrow.daemon.util.Logging;
import edu.berkeley.sparrow.thrift.THostPort;
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
     *
     * get value for taskStatus.
     * @param requestId
     * @return
     */
    public THostPort getTaskStatus(String requestId) {
        LOG.info(Logging.functionCall(requestId));
        return taskStatus.get(requestId);
    }
}
