package com.atyeti.assignment;

public class Employee
{
    //Builder pattern is needed to handle optional param
 int id;
 String name;
 String department;
 String hiringdate;

 
public int getId()
{
    return id;
}
public void setId(int id)
{
    this.id = id;
}
public String getName()
{
    return name;
}
public void setName(String name)
{
    this.name = name;
}
public String getDepartment()
{
    return department;
}
public void setDepartment(String department)
{
    this.department = department;
}
public String getHiringdate()
{
    return hiringdate;
}
public void setHiringdate(String hiringdate)
{
    this.hiringdate = hiringdate;
}

@Override
public String toString()
{
  return "Id="+this.id+"  Name="+this.name+"  Department="+this.department+"    Hiringdate="+this.hiringdate;  
}
 
}
