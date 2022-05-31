<%@ page import="java.util.*"%>
<%@page import="java.lang.*"%>
<%@page import="java.io.*"%>
<%@page import="java.sql.*"%>

  <%

    int delay = 10;
    Timer timer = new Timer();
     String ans = "R";
 
  java.sql.Timestamp  logintime=null;
  
   if(ans.equals("r") || ans.equals("R")){
      timer.schedule(new TimerTask(){
        public void run(){
          java.util.Date today = new java.util.Date();
 		java.sql.Timestamp  logintime =new Timestamp(today.getTime());
 		  System.out.println("Time "+logintime) ;
          System.out.println("This line is printed repeatedly.");
        }
      },delay, 1000);
    }
    out.println("Time "+logintime) ;
  
  %>
