package kazarin.my_money.TESTS;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {
   public static void main(String[] args) {

      Result resultOperation = JUnitCore.runClasses(TestOperation.class);
      for (Failure failure : resultOperation.getFailures()) {
         System.out.println(failure.toString());
      }
      System.out.println(resultOperation.wasSuccessful());

      Result resultOperations = JUnitCore.runClasses(TestOperation.class);
      for (Failure failure : resultOperations.getFailures()) {
         System.out.println(failure.toString());
      }
      System.out.println(resultOperations.wasSuccessful());
   }
}  	