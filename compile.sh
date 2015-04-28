#/bin/bash
cd src
javac -d ../../classes/ -cp ../../classes kazarin/my-money/model/ModelLogger.java
javac -d ../../classes/ -cp ../../classes kazarin/my-money/model/ModelException.java
javac -d ../../classes/ -cp ../../classes kazarin/my-money/model/Operation.java
javac -d ../../classes/ -cp ../../classes kazarin/my-money/db/DBLogger.java
javac -d ../../classes/ -cp ../../classes kazarin/my-money/db/DaoException.java
javac -d ../../classes/ -cp ../../classes kazarin/my-money/db/DBTypes.java
javac -d ../../classes/ -cp ../../classes kazarin/my-money/db/Dao.java
javac -d ../../classes/ -cp ../../classes kazarin/my-money/db/AbstractDao.java
javac -d ../../classes/ -cp ../../classes kazarin/my-money/db/HSqlDao.java
javac -d ../../classes/ -cp ../../classes kazarin/my-money/db/MySqlDao.java
javac -d ../../classes/ -cp ../../classes kazarin/my-money/db/DaoFactory.java
javac -d ../../classes/ -cp ../../classes kazarin/my-money/db/DaoFactory.java
javac -d ../../classes/ -cp ../../classes kazarin/my-money/model/Operations.java
javac -d ../../classes/ -cp ../../classes kazarin/my-money/model/Environment.java
javac -d ../../classes/ -cp ../../classes kazarin/my-money/gui/GuiLogger.java
javac -d ../../classes/ -cp ../../classes kazarin/my-money/gui/FrameHolder.java
javac -d ../../classes/ -cp ../../classes kazarin/my-money/gui/AccountingDialog.java
javac -d ../../classes/ -cp ../../classes kazarin/my-money/gui/MainScreen.java