#/bin/bash
cd src
javac -d ../../classes/ -cp ../../classes kazarin/my-money/db/DaoException.java
javac -d ../../classes/ -cp ../../classes kazarin/my-money/db/DBTypes.java
javac -d ../../classes/ -cp ../../classes kazarin/my-money/db/Dao.java
javac -d ../../classes/ -cp ../../classes kazarin/my-money/db/StubDao.java
javac -d ../../classes/ -cp ../../classes kazarin/my-money/db/AbstractDao.java
javac -d ../../classes/ -cp ../../classes kazarin/my-money/db/HSqlDao.java
javac -d ../../classes/ -cp ../../classes kazarin/my-money/db/MySqlDao.java
javac -d ../../classes/ -cp ../../classes kazarin/my-money/db/DaoFactory.java
