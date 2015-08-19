package org.kutty.utils;

import java.util.List;
import java.util.Map;

/** 
 * Utility functions to print different collections and data structures to the console
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 19 August, 2015 
 * 
 */ 

public class PrintUtil {
	
	/** 
	 * Given a Map prints it to the console in key-value pairs
	 * @param print_map Map which is to be printed
	 */ 
	
	public static <T1,T2> void printMap(Map<T1,T2> print_map) { 
		
		System.out.println("----------------------------------------"); 
		
		for (T1 temp : print_map.keySet()) { 
			
			System.out.println(temp + " : " + print_map.get(temp));
		} 
		
		System.out.println("----------------------------------------");
	}
	
	/** 
	 * Prints a given list to the console
	 * @param list List which is to be printed to the console
	 */ 
	
	public static <T> void printList(List<T> list) { 
		
		System.out.println("---------------------------------------------------"); 
		
		for (T temp : list) {  
			
			System.out.println(temp);
		}
		
		System.out.println("----------------------------------------------------");
	}
}
