package edu.pnu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.Scanner;

public class Test {
	private static int size[] = null;

	// select절에 기술된 필드의 타입을 저장하는 배열 ==> 값을 출력할 때 숫자 필드는 ","를 추가하고, 오른쪽 정렬을 위해 사용
	private static int type[] = null;
	static final Scanner sc = new Scanner(System.in);
	public static void main(String[] args) {
		Connection con = null;
		try {
			String driver = "com.mysql.cj.jdbc.Driver";
			String url = "jdbc:h2:tcp://localhost/~/.h2/sqlprg";
			String username = "sa";
			String userpwd = "1234";
			
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, userpwd);
//			deleteColumn(con, 51, "num");
//			showTable(con, "Board");
			System.out.print("Insert user name : ");
			String user = sc.next();
			joinTable(con, user);
			System.out.print("Insert visit count : ");
			int vc = sc.nextInt();
			joinTable(con, vc);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	public static boolean insertTable(Connection con) {
		try {

			PreparedStatement pt = con.prepareStatement("insert into Member(username,password,birthyear) values(?,?,?)");
//			for(int i = 1; i <= 5; i++) {
//				String user = "user" + i, pass = "pass" + i;
//				Random rnd = new Random();
//				int birthyear = rnd.nextInt(2000, 2011);
//				pt.setString(1, user);
//				pt.setString(2, pass);
//				pt.setInt(3, birthyear);
//				pt.execute();
//			}
			
			pt = con.prepareStatement("insert into Board(title,content,id, visitcount) values(?,?,?,?)");
			for(int i = 1; i <= 5; i++) {
				String user = "user" + i;
				Random rnd = new Random();
				for(int j = 1; j <= 10; j++) {
					int visit = rnd.nextInt(0, 100);
					String title = "title" + j, content = "content" + j;
					pt.setString(1, title);
					pt.setString(2, content);
					pt.setString(3, user);
					pt.setInt(4, visit);
					pt.execute();
				}
				
			}
			int result = pt.executeUpdate();
			System.out.println("Member 테이블에 " + result + "개가 입력되었습니다.");
			pt.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public static boolean deleteColumn(Connection con, int num, String cont) {
		try {
			PreparedStatement pt;
			if(cont == "num") pt = con.prepareStatement("delete from Board where num = ?");
			else pt = con.prepareStatement("delete from Member where ID = ?");
			pt.setInt(1, num);
			pt.execute();
				
			pt.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public static boolean updateTable(Connection con, int num, String title, String content) {
		try {
			PreparedStatement pt = con.prepareStatement("update board set content = ?, title = ? where id = ?");
			pt.setString(1, content);
			pt.setString(2, title);
			pt.setInt(3, num);
			pt.execute();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	public static boolean showTable(Connection con, String table) {
		try {
			String str = "select * from " + table;
			PreparedStatement pt = con.prepareStatement(str);
			ResultSet rs = pt.executeQuery();

			calcValue(rs);
			printSQL(rs);
			pt.close();
			rs.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public static boolean joinTable(Connection con, String user) {
		try {
			PreparedStatement pt = con.prepareStatement("select board.* from board join member on member.username = board.id where board.id = ?");
			pt.setString(1, user);
			ResultSet rs = pt.executeQuery();

			calcValue(rs);
			printSQL(rs);
			pt.close();
			rs.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public static boolean joinTable(Connection con, int visitcount) {
		try {
			PreparedStatement pt = con.prepareStatement("select board.* from board join member on member.username = board.id where board.visitcount >= ?");
			pt.setInt(1, visitcount);
			ResultSet rs = pt.executeQuery();

			calcValue(rs);
			printSQL(rs);
			pt.close();
			rs.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	public static void calcValue(ResultSet rs) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();

		int count = meta.getColumnCount();

		// 인덱스의 편리를 위해 1개씩 더해서 배열 객체 생성
		// 배열은 0부터 시작하지만, 결과 셋에서 데이터를 읽어들이는 인덱스는 1부터 시작
		size = new int[count + 1];
		type = new int[count + 1];

		// 필드의 개수만큼 반복
		for (int i = 1; i <= count; i++) {
			// 데이터베이스에서 정의된 필드의 길이를 설정
			size[i] = meta.getColumnDisplaySize(i);
			// 필드명의 길이가 설정값보다 크면 필드명 길이로 수정
			if (size[i] < meta.getColumnName(i).length()) {
				size[i] = meta.getColumnName(i).length();
			}

			// 필드 타입 설정(정수형, 실수형, 그외)
			type[i] = meta.getColumnType(i);
			if (type[i] == Types.INTEGER || type[i] == Types.BIGINT || type[i] == Types.SMALLINT || type[i] == Types.BIT
					|| type[i] == Types.TINYINT) {
				type[i] = 1;
			} else if (type[i] == Types.FLOAT || type[i] == Types.REAL || type[i] == Types.DOUBLE
					|| type[i] == Types.NUMERIC || type[i] == Types.DECIMAL) {
				type[i] = 2;
			} else {
				type[i] = 99;
			}

			// 숫자형 데이터는 ',' 또는 소수점이 추가되기 때문에 넉넉하게 공간을 조금 더 준다.
			if (type[i] == 1 || type[i] == 2)
				size[i] += 3;
		}
	}

	public static void printSQL(ResultSet rs) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();
		int count = meta.getColumnCount();

		// 제일 위 라인그리기
		System.out.print("┌");
		for (int i = 1; i <= count; i++) {
			for (int j = 0; j < size[i]; j++)
				System.out.print("─");

			if (i != count)
				System.out.print("┬");
			else
				System.out.print("┐");
		}
		System.out.println();

		// 타이틀 출력하기

		System.out.print("│");
		for (int i = 1; i < count + 1; i++) {
			String r = String.format("%" + size[i] + "s", meta.getColumnName(i));
			System.out.printf("%s", r);

			System.out.print("│");
		}
		System.out.println();

		// 타이틀 아래 라인그리기 Continent, Name, Population, LifeExpectancy
		System.out.print("├");
		for (int i = 1; i <= count; i++) {
			for (int j = 0; j < size[i]; j++)
				System.out.print("─");

			if (i != count)
				System.out.print("┼");
			else
				System.out.print("┤");
		}
		System.out.println();

		// 값 출력하기
		int cnt = 1;
		while (rs.next()) {
			if(cnt % 6 == 0) {
				System.out.print("├");
				for (int j = 1; j <= count; j++) {
					for (int k = 0; k < size[j]; k++)
						System.out.print("─");

					if (j != count)
						System.out.print("┼");
					else
						System.out.print("┤");
				}
				System.out.println();
			}
			System.out.print("│");
			for (int i = 1; i < count + 1; i++) {
				String r;
				if (type[i] == 99)
					r = rs.getString(meta.getColumnName(i));
				else {
					DecimalFormat dc = new DecimalFormat("#,###");
					r = dc.format(rs.getInt(meta.getColumnName(i)));
					if (type[i] == 2) {
						dc = new DecimalFormat("#,###.00");
						r = dc.format(rs.getFloat(meta.getColumnName(i)));
					}
				}
				System.out.print(r);
				for (int j = 0; j < size[i] - r.length(); j++)
					System.out.print(" ");

				System.out.print("│");

			}
			System.out.println();
			cnt++;
		}
		// 제일 아래 라인그리기
		System.out.print("└");
		for (int i = 1; i <= count; i++) {
			for (int j = 0; j < size[i]; j++)
				System.out.print("─");

			if (i != count)
				System.out.print("┴");
			else
				System.out.print("┘");
		}
		System.out.println();
	}
}
