package edu.berkeley.sparrow.daemon.scheduler;

import com.google.common.collect.Lists;
import edu.berkeley.sparrow.daemon.util.Logging;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.FileHandler;

import edu.berkeley.sparrow.thrift.TSchedulingRequest;
import org.apache.log4j.Logger;
import sun.rmi.runtime.Log;

public class DataCollector {


    private static DataCollector mDataCollectorInstance = null;
    private final static Logger  mLogger = Logging.getAuditLogger(DataCollector.class);
    private Connection  connect = null;
    //private SchedulerState state;


    DataCollector(){
        mLogger.info("DataCollector Instance created.");


        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect = DriverManager
                    .getConnection("jdbc:mysql://localhost/marlin?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&"
                            + "user=root&password=");

            //mLogger.addHandler(mFileHandler);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }





    }


    /*
    Singleton class
    */
    public static  DataCollector getInstance(){
        if(mDataCollectorInstance == null){
            mDataCollectorInstance = new DataCollector();
        }
        return mDataCollectorInstance;
    }


    public void collect(String msg){
        mLogger.info(Logging.auditEventString((msg)));
    }

//    public void recordEntries(TSchedulingRequest req){
//
//
//        List<InetSocketAddress> allBackends = Lists.newArrayList();
//        List<InetSocketAddress> backends = Lists.newArrayList();
//        for (InetSocketAddress backend : state.getBackends(req.app)) {
//            allBackends.add(backend);
//        }
//        state.getBackends(req.app);
//
//    }
}
