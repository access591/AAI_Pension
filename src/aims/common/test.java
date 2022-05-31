package aims.common;
import java.io.*;
import java.util.*;


public class test {

	public static void main(String args[]) throws IOException{
	String one="abc.xls";
	int index2=one.indexOf(".");
	String two=one.substring(0,index2);
	System.out.println(two);
	}
  
}
