#/bin/bash
cd src
javac -d ../classes/ -cp ../classes kazarin/my-money/model/WrongCommandException.java
javac -d ../classes/ -cp ../classes kazarin/my-money/MyLogger.java
javac -d ../classes/ -cp ../classes kazarin/my-money/model/Operation.java
javac -d ../classes/ -cp ../classes kazarin/my-money/db/Dao.java
javac -d ../classes/ -cp ../classes kazarin/my-money/db/StubDao.java
javac -d ../classes/ -cp ../classes kazarin/my-money/model/Environment.java
javac -d ../classes/ -cp ../classes kazarin/my-money/db/OperationsDao.java
javac -d ../classes/ -cp ../classes kazarin/my-money/model/Operations.java
javac -d ../classes/ -cp ../classes kazarin/my-money/Main.java