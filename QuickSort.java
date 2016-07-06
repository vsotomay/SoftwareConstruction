/*
 Teacher: TA:
 Quick Sort k-largest elements */

import java.util.Random;
import java.util.Scanner;

public class QuickSort
{
    
    
    public static void main (String [] args)
    {
        Scanner console = new Scanner(System.in);
        
        
        //prompt the user the type the size of the array
        System.out.println("Type the size of the array: ");   
        int arraysize=console.nextInt();
        
        //global variable in the size of the array prompted by the user
        final int max=arraysize;
        
        //random numbers where the max array size is 10000000
        int []list=randomWalkArray(10000000);
        
        
        
        long start,end;
        int first=0;
        int last=max;
        
              //ask the usert the type the k-th largest element in the array
        System.out.println("Type the k-th largest to search in your array: ");
        int elem=console.nextInt();
        
        start=System.nanoTime();
        //call for the methods on QuickSortAlgorithm
        partition(list, first, last);
        swap(list, first, last);
        recQuickSort(list, first, last);
        quickSort(list, max);
              
        //printing the final results (sorted list and the k-th larges element)       
       printQuick(list, max, elem); 
        end=System.nanoTime();
        
  
  
        
        
        //capturing how long it takes this sort
        System.out.println("It takes " + (end-start)+ " nanoseconds to sort the array "
                               + "and find the k-ht largest element");
    }
    
    //method partition which identify and return the pivot
    public static int partition(int [] list, int first, int last)
    {
        int pivot;
        int smallIndex;
        //call swap method and split the array in two parts
        swap(list, first, (first+last)/2);
        //identify the pivot
        pivot=list[first];
        smallIndex=first;
        
        //traverse in the first half of the array
        for(int index=first+1;index<=last;index++)
        {
            //swap if the index is less than the current pivot
            if(list[index]<pivot)
            {
                smallIndex++;
                swap(list, smallIndex, index);
                break;
            }
        }

        if(smallIndex<=30){
            smallindex++;
        }
        else{ pivot--;}
        //call swap method and return the samll index
        swap(list, first, smallIndex);
        return smallIndex;
    }
    
    
    //method swap to change the value of the pivot
    public static void swap(int [] list, int first, int second)
    {
        //creating temp to set the value of the first index without erease it
        int temp;
        temp=list[first];
        list[first]=list[second];
        list[second]=temp;
    }
    
    //mehtod recQuickSort implementes an recursive method
    public static void recQuickSort(int [] list, int first, int last)
    {
        if(first<last)
        {
            //recursive calls in which reconize each half of the array
            int pivotLocation=partition(list, first, last);
            
            //the last half
            recQuickSort(list, first, pivotLocation-1);
            
            //the first half
            recQuickSort(list, pivotLocation+1, last);
        }
    }
    
    //method quickSort that calls the method recQuickSort on the original list
    public static void quickSort(int [] list, int length)
    {
        //recursion call from recQcuickSort 
        recQuickSort(list, 0, length-1);
    }
    
    //print the sorted list
    public static void printQuick(int[] list, int size, int elem)
    {
        
        int i;
        //traverse the length of the array
        for( i=0; i < size;i++)
        
            //sorted numbers
            System.out.println(list[i]);
        int kht=size-elem; 
        
        //k-th larges element in this array printed
        System.out.println(); 
        System.out.println("The "+ elem +"-ht largest element in this array is "+list[kht]);
        
    }
    
    //method randomwalkarray that creats random numbers
    //"Provided by Olac Fuentes UTEP Web Site"
    public static int [] randomWalkArray(int n)
    {
        Random generator = new Random ();
        int [] randArray=new int[n];
        randArray[0]=0;
        for(int i=1;i<n;i++)
            randArray[i]=randArray[i-1] + generator.nextInt(101)-10;
        return randArray;
    }
    
}

