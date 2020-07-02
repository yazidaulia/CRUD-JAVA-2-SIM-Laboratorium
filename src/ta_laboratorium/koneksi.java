package ta_laboratorium;

import java.sql.*;
/**
 *
 * @author yazidaulia
 */
public class koneksi {

    public Connection conn;
    public Statement st;
    public ResultSet rs;

        public Connection connect(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            //System.out.println("berhasil konek");
        }catch(ClassNotFoundException e){
            System.out.println("gagal konek "+e);
        }
        String url = "jdbc:mysql://localhost/db_lab";
        try{
            conn = DriverManager.getConnection(url, "root","");
            //System.out.println("berhasil koneksi ke database");
        }catch(SQLException ex){
            System.out.println("gagal koneksi ke database "+ex);
        }
        return conn;
    }
}