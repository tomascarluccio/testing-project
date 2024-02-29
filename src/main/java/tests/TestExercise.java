package tests;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import reports.Report;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.math.IntMath.gcd;
import static com.google.common.primitives.Ints.asList;

public class TestExercise extends BaseAPITest {


    @DataProvider(name = "test", parallel=true)
    public Object[][] getUserData() {
        return new Object[][] {
                {"", ""},
        };
    }


    @Test(dataProvider = "test", priority = 3)
    @Report(name="Exercise", description="test exercise")
    public void test(String param1, String param2) throws Exception {
        // Palindrome
        System.out.println( "is palindrome " + palindrome("java"));
        System.out.println( "is palindrome " + palindrome("abba"));
        System.out.println( "is palindrome " + palindrome("golog"));

        // Prime
        System.out.println(" is prime " + prime(7));
        System.out.println(" is prime " + prime(3));
        System.out.println(" is prime " + prime(12));

        // Fibonacci
        System.out.println(" Fibonacci " + fibonacci(7));
        System.out.println(" Fibonacci " + fibonacci(3));
        System.out.println(" Fibonacci " + fibonacci(12));

        // GCD
        System.out.println(" GCD " + greatestCommonDivisor(2, 3));
        System.out.println(" GCD " + greatestCommonDivisor(10, 50));
        System.out.println(" GCD " + greatestCommonDivisor(600, 1343));

        // multipe of 3 and 5
        ArrayList<Integer> numbers = new ArrayList<Integer>( asList(1, 2, 3, 4, 5, 6));
        System.out.println(" String list " + getString(numbers));
        System.out.println(" Is multiple  " + multipleFizzBuzz(4));
        System.out.println(" Is multiple  " + multipleFizzBuzz(15));
        System.out.println(" Is multiple  " + multipleFizzBuzz(27));

        // Even fibonacci
        System.out.println(" String list " + getString(numbers));
        System.out.println(" even fibonnaci  " + evenFibonacciSum(4));
        System.out.println(" even fibonnaci  " + evenFibonacciSum(15));
        System.out.println(" even fibonnaci  " + evenFibonacciSum(27));


    }

    public boolean palindrome(String word){
       int i1 = 0;
       int i2 = word.length() - 1;

       while(i2 > i1) {
            if (word.charAt(i1) != word.charAt(i2)){
               return false;
            }
           i1++;
           i2--;
       }
       return true;
    }

    public Integer fibonacci(Integer n){
        if(n == 1) {
            return 1;
        }
        else if (n == 0 ) {
            return 0;
        }
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    public Integer evenFibonacciSum(Integer n) {
        int currentFibonacci = 2;
        int previousFibonacci = 1;
        int evenFibonacciSum = 0;
    do {
        if (currentFibonacci % 2 == 0) {
            evenFibonacciSum += currentFibonacci;
        }
        int newFibonnaci = currentFibonacci + previousFibonacci;
        previousFibonacci = currentFibonacci;
        currentFibonacci = newFibonnaci;
    } while (currentFibonacci < n);
    return evenFibonacciSum;
    }



    public boolean prime(int n){
       boolean isPrime = true;

       for(int i = n - 1; i > 1; i--){
           if(n % i == 0)
               isPrime =  false;
           break;
       }
       return isPrime;
    }

    public Integer greatestCommonDivisor(Integer q, Integer p){
        if(q == 0){
            return p;
        }
        return gcd(q, p % q);
    }

    public String multipleFizzBuzz(Integer i){
       String result = "";
       if(i % 3 == 0 )
           result += "Fizz";
       if(i % 5 == 0 )
            result += "Buzz";
       if(result.isEmpty())
            result = i.toString();
        return result;
    }

    public String getString(List<Integer> list){
        StringBuilder result = new StringBuilder();
        for (Integer n: list){
            if(n % 2 == 0)
                result.append(" e").append(n);
            else
                result.append(" o").append(n);
        }
        return result.toString();
    }

    }
