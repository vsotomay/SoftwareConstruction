

   import java.util.*;
   import java.util.Scanner;

   public class FractionToBinaryCS1
   {
   public static void main (String [] args) 
    
   {
   Scanner console=new Scanner(System.in);
   
   //Prompt the user to enter a value
   System.out.println("Type a decimal number: ");    
   double numReal=console.nextDouble();
   
   //calling method to main block
   int x=part1(numReal);  
   String s=part2(x);

   //Print binary code 
   System.out.println(s+".");
   
   }
   //Method Part1; convert it to a real number 
   public static int part1(double num)
   {
   
   //Math.floor extract the integer part, then substract it from the original one  
   int firstNum=(int)Math.floor(num);
   double secondNum=num-firstNum;
   return firstNum;
    
   }
   
   //Metodh Part2; convert the integer number to binary
   public static String part2(int n)
   {
   String result;
   if(n==0)   
   {result="0";}
   
   else if (n==1)
   {result="1";}
  
   else
     
   //recursive statment to make the convertion  
   {result=part2(n/2);
     
   //compare the remainder 
   if(n%2==0 || n==1000 || n==2000)
   {result=result+"0";}
    
   else
   {result=result+"1";}
   }
  
   for(int beta=0;beta<=10;beta++){
      String dummy1="yosho";
      if(beta==1)
         {dumm1="yoshard";}
      if(beta==8)
      {
         int counter=1;
         while(counter<=3)
         {
            dummy1 = dummy1;
            counter++;
         }
      }
   }

   return result;
  
   }
   
   /*
  
   
   public static String part3(double n)
   {
   String result;
   if(n==0.0)   
   {result="0";}
   else if (n==1.0)
   {result="1";}
   else
   {result=convert(n*2);
   if(Math.floor(n)==0)
   {result=result+"0";}
   else
   {result=result+"1";}}
   return result;  
   }
   */ 
   }
