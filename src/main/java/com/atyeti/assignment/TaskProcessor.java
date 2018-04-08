package com.atyeti.assignment;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.atyeti.assignment.hbase.InsertIntoTable;

public class TaskProcessor
{
    private static final Logger LOG = Logger.getLogger(FileProcessor.class.getName());
    private static ThreadLocal myThreadLocal = new ThreadLocal<InsertIntoTable>()
    {
        @Override
        protected InsertIntoTable initialValue()
        {
            return new InsertIntoTable();
        }
    };

    public static void insertInDB(List<Employee> employeeList)
    {
        LOG.debug("employee list submitted");
        InsertIntoTable threadLocalInsertIntoTableObj = (InsertIntoTable) myThreadLocal.get();
        try
        {
            // Actual Hbase API -> threadLocalInsertIntoTableObj.insertRecords(employeeList);
            // Dump methods to display records.
            threadLocalInsertIntoTableObj.insertRecordsDump(employeeList);
        } catch (IOException e)
        {
            LOG.error("Issue in Table record insertion");
        }

    }

}
