[![OpenCode](https://img.shields.io/badge/Open-Code-ff6a00.svg?style=flat-square)](https://opencode18.github.io)

# Online-Exam-System

#### This is an online examination system built using JAVA and MySQL. Follow these steps to implement it. 

### Setup

* Download the latest version of the **Java Development Kit** from the website according to your OS. You can refer to [this website](http://www.oracle.com/technetwork/java/javase/downloads/jdk9-downloads-3848520.html) for JDK 9. 
* Download the latest version of **MySQL** from [this website](https://dev.mysql.com/downloads/mysql/) according to your OS. You can refer to the installation steps accordingly. 
	* Do not forget to change the default password of MySQL. 
	* Check if you have installed MySQL successfully by running the following command on your terminal : 
		```
			usr/local/mysql/bin/mysql -uroot -p
		```
	  After this, enter the new password you have created. This also verifies whether your new password has been successfully created or not. 
* Once you are done setting up Java and MySQL on your machine, download any of the MySQL connectors compatible with Java. You can either download the ODBC or the JDBC connector. I have downloaded JDBC from [this website](https://dev.mysql.com/downloads/connector/).


### Creating database and tables

1. Now that the interface is set up, create the database and the tables required for the working of our code. 
2. Start MySQL server.
3. Log in to MySQL using the previously mentioned command and your password. 
4. Create the database.
5. Switch to the database and create the required tables according to the schema.
6. Check values in all tables. All of them should be *empty* now. 


### Execution

1. Change the login credentials in the code, i.e., password and database, table names. 
2. Copy the jar file from the JDBC you have downloaded in the local repository if you don't have it already.
3. Switch to the repository on your terminal and export the classpath using the following command :
 	```
 		export CLASSPATH=mysql-connector-java-5.1.45-bin.jar:.
 	```
3. Compile the java program using the following command :
	```
		javac OnlineTest.java
	```
4. Run the program using the following command :
	```
		java OnlineTest
	```
