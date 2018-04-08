# atyetiAssignment
This project contain Processing and storing large size employee details in data base
High Level Design:
Application Layer (processing layer)
Design should keep on polling on the directory where the files are placed .if it find the files ,this layer take care of retrieving all files form the directory and submit each and every to threadpool executor(FixedSizeThreadPoolExecutor )(i.e all files are parsed and send to storage layer in separate thread ).
Since there is no blocking calls in our design(like disk io , network calls) ,I made number thread equal to number of available processor ( 8 core CPU).
There is no rule for Number of thread in application == number connection towards DB .but sake of simplicity this prototype each  thread is having  corresponding database connection in its threadlocal variable
Storage layer 
Since we need to process and store records in mass volume (10000 records per day size of 10000 entry), its better to use NOSQL solution. I prefer  to use NOSQl because of following reason
1.	Better scalability
2.	No fixed rigid schema. column can be added dynamically  .in out use case some of our fields are optional(Department , hiring date)
3.	Connection pooling mechanism provided by all Nosql DB(hbase ,Cassandra ,MongoDB).
No need to maintain the number of connection by semaphore based approach 
We can choose any Nosql solution .but I implemented this prototype with HBASE databse.
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
Low level Design :
Application layer :
Its contain following main components :
File Processor :
Its starting point of application which take care of polling of files in corresponding directory.(in this prototype we are getting files from resource directory of eclipse projects).Ideally it should be implemented by FileWatcher API 
https://docs.oracle.com/javase/tutorial/essential/io/notification.html

 Once it get the list files, submit it the threadpool executors to process and store.
FileParser :
This Class is responsible for parsing pipe separate file and return files contents as list of employee object to storage layer to store

Taskprocessor:
This class is responsible for coordination between application layer and storage layer. Application layer thread to connection mapping is done at this class.(by using Thread local .one to one mapping between application threads and db connection).

AssignementConstant :  list of mostly used constants

 

------------------------------------------------------------------------------------------------------------------------------------------


Storage layer:
Note: Entire storage layer implementation act as  a mock functionality. Because we can’t able to run hbase in local. Its just prototype to demonstrate the real functionality and power of habse based implementation

Tables are created at application startup if its already not created .No sql database not following strict schema.(prototype hbase design require at least one column family -  table name – Employee column family name – info)
Generally nosql related configuration are handled by HbaseconfigUtil class. (it should get it form xml file contain replication ,partition ….etc related details)
Actual application layer and storage layer interaction happens in InsertIntoTable class.This class will take care of getting connection from pool and get table object from that retrieved connection .
Table object is used for insert the employee record at db.As its not having rigid schema ,some incomplete details employee are also persisted in database(in our case employee object is not having hiring date or dept)
 

Steps to execute the application prototype:
1.Add some pipe separated input  files in project resource directory 
2.Start the application by running FileProcessor.java [This class has main methods]
3.You can see following results in out as a log statements

2018-04-09 01:14:19 DEBUG FileProcessor:25 - Thread detailspool-1-thread-1
2018-04-09 01:14:19 DEBUG FileProcessor:25 - Thread detailspool-1-thread-2
//////////////////////////////////////////////////////////

Two files handled by two threads in thread pool
////////////////////////////////////////////////////////
2018-04-09 01:14:19 DEBUG FileProcessor:24 - employee list submitted
2018-04-09 01:14:19 DEBUG FileProcessor:24 - employee list submitted

//////////////////////////////////////////////////////////////
Threads doing insert at simultaneously.
//////////////////////////////////////////////////////////////
2018-04-09 01:14:19 ERROR InsertIntoTable:87 - 
employee details Id=1  Name=dept1  Department=Emp1    Hiringdate=25/05/2010
2018-04-09 01:14:19 ERROR InsertIntoTable:87 - 
employee details Id=1  Name=dept1  Department=Emp1    Hiringdate=25/05/2010
2018-04-09 01:14:19 ERROR InsertIntoTable:87 - 
employee details Id=2  Name=dept2  Department=Emp2    Hiringdate=26/05/2010
2018-04-09 01:14:19 ERROR InsertIntoTable:87 - 
employee details Id=3  Name=dept3  Department=Emp3    Hiringdate=null
2018-04-09 01:14:19 ERROR InsertIntoTable:87 - 
employee details Id=2  Name=dept2  Department=Emp2    Hiringdate=26/05/2010
2018-04-09 01:14:19 ERROR InsertIntoTable:87 - 
employee details Id=4  Name=dept4  Department=Emp4    Hiringdate=28/05/2010
