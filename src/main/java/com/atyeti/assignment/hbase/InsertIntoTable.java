package com.atyeti.assignment.hbase;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import com.atyeti.assignment.Employee;
import com.atyeti.assignment.hbase.util.HBaseConfigUtil;


public class InsertIntoTable {
    private static final Logger LOG = Logger.getLogger(InsertIntoTable.class.getName());
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public Table getTableHandler()
    {
        Configuration config = HBaseConfigUtil.getHBaseConfiguration();

        Connection connection = null;
        Table table = null;

        try {
            connection = ConnectionFactory.createConnection(config);
            table = connection.getTable(TableName.valueOf("Employee"));
        } catch (Exception e) {
            LOG.error("Exception in table connection"+e.getMessage());
            CloneHbaseConnection(connection, table);
        } 
        return table;
    }

    private void CloneHbaseConnection(Connection connection, Table table)
    {
        try {
            if (table != null) {
                table.close();
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (Exception e) {
            LOG.error("Exception in table connection close"+e.getMessage());
        }
    }

    public void insertRecords(List<Employee> employeeList) throws IOException
    {

        Table table =getTableHandler();

        for (int i = 0; i < employeeList.size(); i++) {
            Employee emp=employeeList.get(i);
            //This row key (primary key for this table)
            Put person = new Put(Bytes.toBytes(emp.getId()));
            //add column sytax is column family ,column name , value
            person.addColumn(Bytes.toBytes("info"), Bytes.toBytes("ID"), Bytes.toBytes(emp.getId()));
            LOG.debug("Employee object id fields is added values is "+emp.getId());
            person.addColumn(Bytes.toBytes("info"), Bytes.toBytes("NAME"), Bytes.toBytes(emp.getName()));
            if("".equals(emp.getDepartment())  && emp.getDepartment() != null)
            {
                person.addColumn(Bytes.toBytes("info"), Bytes.toBytes("DEPARTMENT"), Bytes.toBytes(emp.getDepartment()));

            }
            if( emp.getHiringdate()!= null)
            {
                person.addColumn(Bytes.toBytes("info"), Bytes.toBytes("HIRINGDATE"), Bytes.toBytes(emp.getHiringdate()));
            }
            String CurrentDateTime=dtf.format(LocalDateTime.now());
            person.addColumn(Bytes.toBytes("info"), Bytes.toBytes("COMMNETS"), Bytes.toBytes(CurrentDateTime));
            table.put(person);
        }

    }
    public void insertRecordsDump(List<Employee> employeeList) throws IOException 
    {
        for(Employee emp:employeeList)
        {
            LOG.error("\nemployee details "+  emp);
        } 
    }
    
}
