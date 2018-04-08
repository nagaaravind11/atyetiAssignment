package com.atyeti.assignment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

;

public class FileParser
{
    private static final Logger LOG = Logger.getLogger(FileParser.class.getName());

    public List<Employee> Parse(File file)
    {
        String FileContentString = null;
        List<Employee> employeeList = new LinkedList<>();
        try
        {
            FileContentString = FileUtils.readFileToString(file);

        } catch (IOException e)
        {
            LOG.error("error reading from file" + e.getMessage());
        }
        String[] contents = FileContentString.split(AssignmentConstants.LINE_SEPERATOR);
        if (checkValidContents(contents))
        {
            String column = contents[0].replace(AssignmentConstants.HEADER_SEPERATOR, "");
            String columns[] = column.split(AssignmentConstants.FEILD_SEPERATOR);
            employeeList = loadEmployeeObject(contents);
        }
        return employeeList;
    }

    private boolean checkValidContents(String[] contents)
    {
        int length = contents.length - 2;// one for column header another one
        // for count trailer
        int contentLength = Integer.parseInt(contents[contents.length - 1].split("\\s+")[2]);
        if (contentLength == length)
            return true;
        LOG.error("Invalid File : contents of  length is not match as trailer line inputs");
        return false;
    }

    private List<Employee> loadEmployeeObject(String[] contents)
    {
        List<Employee> employeeList = new LinkedList<Employee>();
        // First line and last line contents are omitted
        for (int i = 1; i < contents.length - 1; i++)
        {
            String fields[] = contents[i].split(AssignmentConstants.FEILD_SEPERATOR);
            Employee employee = new Employee();
            employee.setId(Integer.parseInt(fields[0]));
            employee.setName(fields[1]);
            employee.setDepartment(fields[2]);
            if (fields.length == 4) // Hiring date is also available in input
            {
                employee.setHiringdate(fields[3]);
            }
            employeeList.add(employee);
        }
        return employeeList;
    }

}
