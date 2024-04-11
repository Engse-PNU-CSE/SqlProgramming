package edu.pnu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class QueryByStatement {

   public static void main(String[] args) {

//      Connection con = null;
//
//      try {
//
//         String driver = "com.mysql.cj.jdbc.Driver";
//
//         String url = "jdbc:mysql://localhost:3306/world";
//
//         String username = "engsen";
//
//         String password = "k203203!";
//
//         Class.forName(driver);
//
//         con = DriverManager.getConnection(url, username, password);
//
//         Statement st = con.createStatement();
//
//         ResultSet rs = st.executeQuery("select id, name, countrycode, district, population from city limit 10");
//
//         while (rs.next()) {
//
//            System.out.print(rs.getString("id") + ",");
//
//            System.out.print(rs.getString("name") + ",");
//
//            System.out.print(rs.getString("countrycode") + ",");
//
//            System.out.print(rs.getString("district") + ",");
//
//            System.out.print(rs.getString("population") + "\n");
//
//         }
//
//         rs.close();
//
//         st.close();
//
//         con.close();
//
//      } catch (Exception e) {
//
//         System.out.println("연결 실패 : " + e.getMessage());
//
//      }
	   test("city");
   }
   public static void test(String tblname) {
	   Connection con = null;
	   Statement st = null;
	   ResultSet rs = null;
	   try {
		   Class.forName("com.mysql.cj.jdbc.Driver");
		   con = DriverManager.getConnection("jdbc:mysql://localhost:3306/world", "engsen", "k203203!");
		   st = con.createStatement();
		   rs = st.executeQuery("select name, CountryCode, population from 	 " + tblname + " order by countrycode, population ASC");
		   ResultSetMetaData meta = rs.getMetaData();
		   int count = meta.getColumnCount();
		   while(rs.next()) {
			   for(int i = 1; i <= count; i++) {
				   System.out.print(rs.getString(i) + (i==count ? "":", "));
			   }
			   System.out.println();
		   }
	         rs.close();

	         st.close();

	         con.close();
	   }
	   catch (Exception e) {
	         System.out.println("연결 실패 : " + e.getMessage());
	   }
   }
}
