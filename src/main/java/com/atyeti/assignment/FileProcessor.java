package com.atyeti.assignment;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;


public class FileProcessor
{
    private static final Logger LOG = Logger.getLogger(FileProcessor.class.getName());
    public static final ExecutorService executorService = Executors.newFixedThreadPool(AssignmentConstants.NO_OF_CORES);
    public static void main(String[] args)
    {
        //java 7 file watcher API is better choice for this 
        //For sake of simplicity i get it from resources folder
        FileParser parser= new FileParser();
        for (final File f :  getResourceFolderFiles(AssignmentConstants.DIR_NAME) )
        {      
                executorService.submit(() ->
                {
                    LOG.debug("Thread details"+Thread.currentThread().getName());
                    final List<Employee>  employeeList=parser.Parse(f);
                    if(employeeList.isEmpty())
                    {
                        LOG.error("There is issue in file.hence not processed");
                    }
                    else
                    {
                    TaskProcessor.insertInDB(employeeList);
                    }
                });
          
        }              
    }

    public  static File[] getResourceFolderFiles (String folder) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(folder);
        String path = url.getPath();
        return new File(path).listFiles();
    }

}
