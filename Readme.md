The campsite application will require a database in order for the application to run.

The followings are the instruction to create a database using PostgreSQL and 
assuming you have PostgreSQL installed on the local system where the application 
is installed.

Create a Database called “reservation” using PostgreSQL

Open a command prompt and type the below command

% psql

##  Create a database called “reservation”
[userid]>=#create database reservation;

##  List all the database currently existed and should expect to see the database “reservation” listed
[userid]=# \l

##  Grant privileges to user <userid> and Postgres
[useri]=#grant all privileges on database "reservation" to <userid>;
[userid]=#grant all privileges on database "reservation" to postgres;

##  List out all users currently on Postgres, and should expect to see user <userid> and “Postgres” existed.
[userid]=# \du
