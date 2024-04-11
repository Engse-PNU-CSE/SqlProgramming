package edu.pnu;

public class DriverRoading {
   public static void main(String[] args) {
      try {
         Class.forName("com.mysql.cj.jdbc.Driver");

         System.out.println("Success to Loading");

      } catch (ClassNotFoundException e) {
         System.out.println("Fail to Loading");
         System.out.println(e.getMessage());
      }

   }
}