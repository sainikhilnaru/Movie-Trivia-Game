import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.io.IOException;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class queries {
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_RESET = "\u001B[0m";
        public static void main(String[] args) {

               try(Connection con=DriverManager.getConnection("jdbc:postgresql://db-315.cse.tamu.edu:5432/ironman4455_605","ironman4455_605","studentpwd"); Statement statement = con.createStatement()) {
                        String file = args[0];
                        BufferedReader br = null;
                        String line = ""; 
                        String splitBy = "\\t+";

                        try {
                                br = new BufferedReader(new FileReader(file));
    
                        //        line = br.readLine();
                                while ((line = br.readLine()) != null) {
					System.out.println(ANSI_GREEN + line + ANSI_RESET);
                                        ResultSet rs = statement.executeQuery(line);
                                        ResultSetMetaData rsmd = rs.getMetaData();
                                        while(rs.next()) {
                                          	 for(int i = 1; i <= rsmd.getColumnCount(); ++i) {
                                          		 System.out.print(rs.getString(i) + "\t");
                                           	}
						System.out.println("");
                                       } 
                                }
                        }  catch(IOException e) {
                                e.printStackTrace();
                        }    

                } catch(SQLException e) {
                        System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
                } catch(Exception e) {
                        e.printStackTrace();
                }
        }
}
