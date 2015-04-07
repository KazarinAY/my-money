#!/bin/bash
cd src
javac -d ../classes/ -cp ../classes:../classes/junit-4.12.jar:../classes/hamcrest-all-1.3.jar kazarin/my-money/TESTS/TestOperation.java
javac -d ../classes/ -cp ../classes:../classes/junit-4.12.jar:../classes/hamcrest-all-1.3.jar kazarin/my-money/TESTS/TestOperations.java
javac -d ../classes/ -cp ../classes:../classes/junit-4.12.jar:../classes/hamcrest-all-1.3.jar kazarin/my-money/TESTS/TestEnvironment.java
javac -d ../classes/ -cp ../classes:../classes/junit-4.12.jar:../classes/hamcrest-all-1.3.jar kazarin/my-money/TESTS/TestRunner.java
