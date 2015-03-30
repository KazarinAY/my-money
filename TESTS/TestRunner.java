package kazarin.my_money.TESTS;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {
   public static void main(String[] args) {
      long start = System.currentTimeMillis();

      Result resultOperation1 = JUnitCore.runClasses(TestOperation.class);
      for (Failure failure : resultOperation1.getFailures()) {
         System.out.println(failure.toString());
      }
      System.out.println(resultOperation1.wasSuccessful());

      Result resultOperation2 = JUnitCore.runClasses(TestOperations.class);
      for (Failure failure : resultOperation2.getFailures()) {
         System.out.println(failure.toString());
      }
      System.out.println(resultOperation2.wasSuccessful());

      Result resultOperation3 = JUnitCore.runClasses(TestEnvironment.class);
      for (Failure failure : resultOperation3.getFailures()) {
         System.out.println(failure.toString());
      }
      System.out.println(resultOperation3.wasSuccessful());

      long finish = System.currentTimeMillis();
      System.out.println("Testing time = " + (finish - start) + "millis");
   }
}  	