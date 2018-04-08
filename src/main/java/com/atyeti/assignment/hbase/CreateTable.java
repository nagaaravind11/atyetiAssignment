package com.atyeti.assignment.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import com.atyeti.assignment.hbase.util.HBaseConfigUtil;


public class CreateTable {

	public static void initTable()
	{
		Configuration config = HBaseConfigUtil.getHBaseConfiguration();

		Connection connection = null;
		Admin admin = null;

		try {
			connection = ConnectionFactory.createConnection(config);
			admin = connection.getAdmin();

			String tableName = "Employee";

			if (!admin.isTableAvailable(TableName.valueOf(tableName))) {
				HTableDescriptor hbaseTable = new HTableDescriptor(TableName.valueOf(tableName));
                //Column family is important in nosql .without this we cant able to  create 
				//a table .column can be added on the fly				
				hbaseTable.addFamily(new HColumnDescriptor("info"));
				admin.createTable(hbaseTable);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (admin != null) {
					admin.close();
				}

				if (connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}
