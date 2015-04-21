* My Money
* Simple accounting.

Instructions

---------------------------------------------------------

First, create database.
Then, ctreate operations table - see create_table.sql

---------------------------------------------------------

To run in console mode:
	my-money$ bash runConsole.sh
To run in GUI mode:
	my-money$ bash runSwing.sh

To compile:
	my-money$ bash compile.sh
	my-money$ bash compileSwing.sh

---------------------------------------------------------

Commands for the console mode:

show - shows list of all operations.

stat - prints summary statistic.

add - adds a new operation.
	synopsys: add sum:[date]:[description]#[tags]
	example: add -200 : 06-04-2015 : description #booze

del - deletes an operation.
	synopsys: del id
	example: del 8

change - changes an operation by id.
	synopsys: change id:[sum]:[date]:[description]#[tags]
	example: change 4:12-12-2014 # booze

exit - to exit

saveto - saves operations list to the file.
	synopsys: saveto filename
	example: saveto /tmp/my_file.txt

loadfrom - loads operations list from the file.
	synopsys: loadfrom filename
	example: loadfrom /tmp/my_file.txt

---------------------------------------------------------

Example of "Enter information":
	user = "guest";
    password = "12345678";   
    url = "jdbc:mysql://localhost/MYMONEY";
    driver = "com.mysql.jdbc.Driver";

---------------------------------------------------------