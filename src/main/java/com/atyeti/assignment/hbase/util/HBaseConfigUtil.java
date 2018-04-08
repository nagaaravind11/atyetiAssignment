/**
 * 
 */
package com.atyeti.assignment.hbase.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;


public class HBaseConfigUtil {
	public static Configuration getHBaseConfiguration() {
		Configuration configuration = HBaseConfiguration.create();
//		config.set("hbase.zookeeper.quorum", "127.0.0.1");
//		config.set("hbase.zookeeper.property.clientPort", "2181");

		configuration.addResource(new Path("/etc/hbase/conf/hbase-site.xml"));
		configuration.addResource(new Path("/etc/hadoop/conf/core-site.xml"));
		return configuration;
	}
}
