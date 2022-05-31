/**
  * File       : StringUtility.java
  * Date       : 08/07/2007
  * Author     : AIMS 
  * Description: 
  * Copyright (2007) by the Navayuga Infotech, all rights reserved.
  */
package aims.common;



	/**
	 * @param args
	 */
	public abstract class StringUtility{
	    private StringUtility()
	    {
	    }

	    public static StringBuffer replace(char[]  string, char[] replace,String charletter)
	    {
	        StringBuffer sb = new StringBuffer();
	        boolean found;
	       
	        for (int i = 0; i < string.length; i++)
	        {
	        	
	            if(string[i] == replace[0])
	            {
	                found = true;
	             
	                for(int j = 0; j < replace.length; j++)
	                {
	                    if(!(string[i + j] == replace[j]))
	                    {
	                       found = false;
	                       break;
	                    }
	                }
	                
	                if(found)
	                {
	                    sb.append(charletter);
	                    i = i + (replace.length - 1);
	                    continue;
	                }
	            }
	            sb.append(string[i]);
	        }
	        
	        return sb;
	    }
	    public static StringBuffer replaces(char[]  string, char[] replace,String charletter)
	    {
	        StringBuffer sb = new StringBuffer();
	        boolean found;
	        int j=0;
	        for (int i = 0; i < string.length; i++)
	        {
	        	
	        	for(int k = 0; k < replace.length; k++){
	        		if(string[i] == replace[k])
		            {
	        		
		                    sb.append(charletter);
		                    if(i!=(string.length-1)){
		                    	i++;
		                    }
		                  
		                }
	        		
		        }
	        	 sb.append(string[i]); 
	        	}
	            
	        
	        return sb;
	    }
	    public static boolean StringCount(char[]  string, char[] replace){
		       int count=0,word=0;
		       boolean countFlag=false;
		        for (int i = 0; i < string.length; i++){
		            if(string[i]== replace[0]){
		            	count++;
		            }else{
		            	if(string[i]!=' '){
		            		word++;
		            	}
		            }
		        }
		       if(word==0){
		    	   countFlag=true;
		       }
		       
		     return countFlag;  
		    }
	    
	    public static int StringCountVal(char[]  string, char[] replace){
		       int count=0,word=0;
		       boolean countFlag=false;
		        for (int i = 0; i < string.length; i++){
		            if(string[i]== replace[0]){
		            	count++;
		            }else{
		            	if(string[i]!=' '){
		            		word++;
		            	}
		            }
		        }
		       if(word==0){
		    	   countFlag=true;
		       }
		       
		     return word;  
		    }
	    
	    public static int delimeterCount(char[]  string, char[] replace){
		       int count=0,word=0;
		       boolean countFlag=false;
		        for (int i = 0; i < string.length; i++){
		            if(string[i]== replace[0]){
		            	count++;
		            }
		        }
		     
		  
		     return count;  
		    }
	   
	}

	
