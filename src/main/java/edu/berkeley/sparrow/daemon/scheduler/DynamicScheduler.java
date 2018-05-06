package edu.berkeley.sparrow.daemon.scheduler;


import edu.berkeley.sparrow.daemon.util.Logging;
import edu.berkeley.sparrow.thrift.THostPort;
import org.apache.log4j.Logger;

import java.util.*;


public class DynamicScheduler {

    /**
     * DynamicScheduler implementation.
     */

    private static final Logger LOG = Logger.getLogger(DynamicScheduler.class);
    public static DynamicScheduler mDynamicScheduler = null;
    private ArrayList<THostPort> sortedBackends = new ArrayList<THostPort>();
    private long taskExecuted = 0;

    /**
     * placeholder for requestID , backends.
     */
    private HashMap<String, THostPort> taskStatus = new HashMap<String, THostPort>();

    /**
     * map worker machine vs (total time taken , total jobs)
     */
    private HashMap<THostPort, Long> totalTimeTaken = new HashMap<THostPort, Long>();
    private HashMap<THostPort, Long> totalTask = new HashMap<THostPort, Long>();


    /**
     * Singleton class for dynamic scheduler.
     *
     * @return instance of Dynamic class.
     */
    public static DynamicScheduler getInstance() {
        if (mDynamicScheduler == null) {
            mDynamicScheduler = new DynamicScheduler();
        }
        return mDynamicScheduler;
    }

    /**
     * add value to taskStatus .
     *
     * @param requestId :  requestId
     * @param host      :  THostPort
     */
    public synchronized void addTaskStatus(String requestId, THostPort host) {
        LOG.info(Logging.functionCall(requestId, host));
        taskStatus.put(requestId, host);
    }

    /**
     * get value for taskStatus.
     *
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
     *
     * @param host:              worker machine
     * @param lastExecutionTime: time taken by worker machine to execute last task
     */

    public void addTimeTaken(String requestId, long lastExecutionTime) {
        taskExecuted++;
        LOG.info(Logging.functionCall(requestId, lastExecutionTime));
        if (taskStatus.containsKey(requestId)) {
            THostPort host = taskStatus.get(requestId);

            if (totalTimeTaken.containsKey(host)) {
                totalTimeTaken.put(host, totalTimeTaken.get(host) + new Long(lastExecutionTime));
                totalTask.put(host, totalTask.get(host) + new Long(1));
            } else {
                LOG.info("addTimeTaken : reset");
                totalTimeTaken.put(host, new Long(lastExecutionTime));
                totalTask.put(host, new Long(0));
            }
            LOG.info("addTimeTaken : " + host + " : " + totalTimeTaken.get(host).toString() + ", " + totalTask.get(host).toString());
        } else {

        }
        LOG.info("backends : " + totalTimeTaken.size());
        for (Map.Entry<THostPort, Long> entry : totalTimeTaken.entrySet()) {
            LOG.info(entry.getKey() + " : " + entry.getValue() + " : " + ((float) entry.getValue() / (float) totalTask.get(entry.getKey())));
        }

        //TODO sort list
        //if(taskExecuted %10  == 0 && taskExecuted != 0){
        sortedBackendNodes();
        //}



    }

    private void sortedBackendNodes() {

        Collections.sort(sortedBackends, new Comparator<THostPort>() {
            public int compare(THostPort o1, THostPort o2) {
                if ((float)totalTimeTaken.get(o1)/(float) totalTask.get(o1) > (float)totalTimeTaken.get(o2)/(float) totalTask.get(o2)){
                    return 1;
                }else{
                    return -1;
                }

            }
        });

        for (THostPort back: sortedBackends){
            LOG.info("sortedBackendNodes " + back);
        }
    }

    /**
     *
     * @param reservationsToLaunch : number of backend worker required
     * @return list of backends.
     */
    public ArrayList<THostPort> getBackends(int reservationsToLaunch) {
        ArrayList<THostPort> backends = new ArrayList<THostPort>();
        int bucketSize = 2;
        int totalSize = sortedBackends.size();
        for(int  i = 0  ; i <  reservationsToLaunch  && i < totalSize ;  i = i + bucketSize){
            backends.add(sortedBackends.get(i));
        }

        backends.add(sortedBackends.get(sortedBackends.size()-1));
        return backends;
    }

    /**
     *
     * @param host
     */

    public void addBackend(THostPort host) {

        if(!sortedBackends.contains(host)) {
            sortedBackends.add(host);
            totalTimeTaken.put(host, new Long(1));
            totalTask.put(host, new Long(1));
        }


    }
}