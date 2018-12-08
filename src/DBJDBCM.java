/* This program is an example used to illustrate how JDBC works.
 ** It uses the JDBC driver for MySQL.
 **
 ** This program was originally written by nikos dimitrakas
 ** on 2007-08-31 for use in the basic database courses at DSV.
 **
 ** There is no error management in this program.
 ** Instead an exception is thrown. Ideally all exceptions
 ** should be caught and managed appropriately. But this 
 ** program's goal is only to illustrate the basic JDBC classes.
 **
 ** Last modified by nikos on 2015-10-07
 */

import java.sql.*;
import java.util.Scanner;

public class DBJDBCM
{

    // DB connection variable
    static protected Connection con;
    // DB access variables
    private String URL = "jdbc:mysql://localhost:3306/labb";
    private String driver = "com.mysql.cj.jdbc.Driver";
    private String userID = "root";
    private String password = "";

    // method for establishing a DB connection
    public void connect()
    {
        try
        {
            Class.forName(driver);
            con = DriverManager.getConnection(URL, userID, password);
            con.setAutoCommit(false);
            System.out.println("Connected to " + URL + " using "+ driver);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void getAllCarBrands() throws Exception{
        String query = "SELECT DISTINCT marke FROM bil";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        System.out.println("Resultatet (Alla märken):");
        while (rs.next())
        {
            System.out.println(rs.getString("marke"));
        }

        stmt.close();
    }

    public void getCarsByCity() throws Exception {
        Scanner in = new Scanner(System.in);

        System.out.print("Ange en stad: ");
        String brandParam = in.nextLine();

        String query = "SELECT regnr, marke, farg FROM bil WHERE agare IN (SELECT id FROM person WHERE stad = ?)";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setString(1, brandParam);

        ResultSet rs = stmt.executeQuery();

        System.out.println("Resultatet (Info om bilar som ägs av någon i staden " + brandParam + "):");
        while (rs.next())
        {
            System.out.println(
                    rs.getString("regnr") + " " +
                    rs.getString("marke") + " " +
                    rs.getString("farg")
            );
        }

        stmt.close();
    }

    public void changeColorOfCar() throws Exception {
        Scanner in = new Scanner(System.in);

        System.out.print("Ange bilens Regnr: ");
        String regnrParam = in.nextLine();
        System.out.print("Ange den nya färgen: ");
        String colorParam = in.nextLine();

        String query = "UPDATE bil SET farg = ? WHERE regnr = ?";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setString(1, colorParam);
        stmt.setString(2, regnrParam);

        stmt.executeUpdate();

        stmt.close();
    }

    public static void main(String[] argv) throws Exception
    {
        DBJDBCM t = new DBJDBCM();

        t.connect();
		  System.out.println("-------- Alla bilmärken  ---------");
        t.getAllCarBrands();
		  System.out.println("-------- Alla bilar i valbar stad ---------");
        t.getCarsByCity();
		  System.out.println("-------- Ändra färg på en bil ---------");
        t.changeColorOfCar();

        con.commit();
        con.close();
    }
}
