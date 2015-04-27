#/bin/bash
cd src
javac -d ../../classes/ -cp ../../classes kazarin/my-money/gui/GuiLogger.java
javac -d ../../classes/ -cp ../../classes kazarin/my-money/gui/FrameHolder.java
#javac -d ../../classes/ -cp ../../classes kazarin/my-money/gui/ConnectToExistingDialog.java
#javac -d ../../classes/ -cp ../../classes kazarin/my-money/gui/NewAccountingDialog.java
javac -d ../../classes/ -cp ../../classes kazarin/my-money/gui/AccountingPanel.java
javac -d ../../classes/ -cp ../../classes kazarin/my-money/gui/AddOperationDialog.java
javac -d ../../classes/ -cp ../../classes kazarin/my-money/gui/ChangeOperationDialog.java
javac -d ../../classes/ -cp ../../classes kazarin/my-money/gui/DeleteOperationDialog.java
javac -d ../../classes/ -cp ../../classes kazarin/my-money/gui/OperationListPanel.java
javac -d ../../classes/ -cp ../../classes kazarin/my-money/gui/MainScreen.java