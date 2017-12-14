import java.sql.*;
class DBCon
{
	public static void main(String args[])
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql:///qa","root","shivhek25@mysql");
			Statement stmt = con.createStatement();
			stmt.executeUpdate("insert into qao values(1,'sdvdv','sdvd','sdfh','fsbf','rgrgg')");
			stmt.executeUpdate("insert into qao values(2,'sdvdv','sdvd','sdfh','fsbf','rgrgg')");
			stmt.executeUpdate("insert into qao values(3,'sdvdv','sdvd','sdfh','fsbf','rgrgg')");
			stmt.executeUpdate("insert into qao values(4,'sdvdv','sdvd','sdfh','fsbf','rgrgg')");
			stmt.executeUpdate("insert into qao values(5,'sdvdv','sdvd','sdfh','fsbf','rgrgg')");
			stmt.executeUpdate("insert into qao values(6,'sdvdv','sdvd','sdfh','fsbf','rgrgg')");
			stmt.executeUpdate("insert into qao values(7,'sdvdv','sdvd','sdfh','fsbf','rgrgg')");
			stmt.executeUpdate("insert into qao values(8,'sdvdv','sdvd','sdfh','fsbf','rgrgg')");
			stmt.executeUpdate("insert into qao values(9,'sdvdv','sdvd','sdfh','fsbf','rgrgg')");
			stmt.executeUpdate("insert into qao values(10,'sdvdv','sdvd','sdfh','fsbf','rgrgg')");
			con.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}