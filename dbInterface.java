package sample;
import java.sql.*;

import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.sql.DriverManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class dbInterface {

    Connection conn = null;
    String[] tables = {"actors", "movies", "episodes", "principal", "crew", "rating"};
    String[] names = {"director", "actor", "writer"};
    WriterGraph graph;

	public dbInterface() {

		try {

			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection("jdbc:postgresql://db-315.cse.tamu.edu:5432/ironman4455_605",
			"ironman4455_605", "studentpwd");


		} catch (Exception e) {

//			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+ e.getMessage());
			System.exit(0);

		}

        graph = new WriterGraph();
        try {

            FileInputStream fi = new FileInputStream(new File("actorGraph.txt"));
            ObjectInputStream oi = new ObjectInputStream(fi);

            // Read objects 
            graph = (WriterGraph) oi.readObject();

            oi.close();
            fi.close();

            System.out.println("Opened object file successfully");
        } catch(Exception e) {
            System.out.println("Could not load object");
            graph = new WriterGraph();
        }
        graph.loadData(100);

    }

    public boolean checkIn(String str, String[] items) {
        boolean contains = false;
        for (String item : items) {
            if (str.equalsIgnoreCase(item)) {
                contains = true;
                break;
            } 
        }
        return contains;
    }

    public String query(String entryA, String entryASpecifier, String entryB, String entryBSpecifier) {
        try{
            boolean join = false, nameVal = false;
            String output = "";
            String sqlStatement = null;

			Statement stmt = conn.createStatement();

            sqlStatement = "SELECT primarytitle FROM movies where ";

            if(entryA == null) {
                entryA = "/e";
            }
            if(entryB == null) {
                entryB = "/e";
            }

            if (entryB == "HighestRatedActorMovie") {
                sqlStatement = "SELECT m.primarytitle AS movieName, MAX(r.averagerating) AS theRating FROM rating r INNER JOIN movies m ON(m.startyear >=  " + entryASpecifier + " AND m.startyear<= " + entryBSpecifier + ") WHERE (r.id = m.id AND m.titletype = 'movie' AND r.id in(SELECT movieid FROM principal WHERE actorid = (SELECT id FROM actors WHERE name = '"+ entryA +"'))) GROUP BY movieName ORDER BY theRating DESC LIMIT 1;";
                ResultSet result = stmt.executeQuery(sqlStatement);
                ResultSetMetaData rsmd = result.getMetaData();
                while(result.next()) {
                    for(int i = 1; i <= rsmd.getColumnCount(); ++i) {
                        output = output + "\t" + result.getString(i) + "\t";
                        System.out.print(result.getString(i) + "\t");
                    }
                    System.out.println("");
                }

            }
            else if (entryB == "DegreesofSeperation"){
                output = graph.run(entryASpecifier, entryBSpecifier);
            }
            else if (entryB == "ShortestListfromYearRange"){
                Question2_Phase4 q = new Question2_Phase4();
                return q.contiguousCareers(entryASpecifier, entryBSpecifier);
            }
            else {

                if (!checkIn(entryA, names) && entryA != "year") {
                    entryA = "/e";
                }
                if (!checkIn(entryB, names) && entryB != "year") {
                    entryB = "/e";
                }

                if (entryA == "/e" && entryB == "/e") {
                    System.out.println("ERROR");
                    return null;
                }


                System.out.println(entryA + " " + entryASpecifier + ", " + entryB + " " + entryBSpecifier);

                try {
                    if (entryA == "year") {
                        int yearA = Integer.parseInt(entryASpecifier);
                    }

                    if (entryB == "year") {
                        int yearB = Integer.parseInt(entryBSpecifier);
                    }
                } catch (Exception e) {
                    System.out.println("Error: Input is incorrect... " + e);
                    return null;
                }


                if (entryA == "year" && entryB == "/e") { // A is a year B is not given
                    sqlStatement = sqlStatement + "startYear=" + entryASpecifier + " AND titleType='movie' LIMIT 10;"; // WORKS
                } else if (entryB == "year" && entryA == "/e") { // B is a year A is not given
                    sqlStatement = sqlStatement + "startYear=" + entryBSpecifier + " AND titleType='movie' LIMIT 10;"; // WORKS
                } else if (entryA == "year" && entryB == "year") { // A is a year B is a year
                    sqlStatement = sqlStatement + "startYear BETWEEN '" + entryASpecifier + "' AND '" + entryBSpecifier + "' AND titleType='movie' LIMIT 10;";
                } else if (entryB == "year" && checkIn(entryA, names)) { // B is a year A is a name
                    sqlStatement = sqlStatement + "startYear=" + entryBSpecifier + " AND titleType='movie' AND id in (SELECT movieID FROM principal WHERE actorID = (SELECT ID FROM actors where name='" + entryASpecifier + "' LIMIT 1));"; // WORKS
                } else if (entryA == "year" && checkIn(entryB, names)) { // A is a year B is a name
                    sqlStatement = sqlStatement + "startYear=" + entryASpecifier + " AND titleType='movie' AND id in (SELECT movieID FROM principal WHERE actorID = (SELECT ID FROM actors where name='" + entryBSpecifier + "' LIMIT 1));"; // WORKS
                } else if (checkIn(entryA, names) && checkIn(entryB, names)) { // A and B are names
                    sqlStatement = "SELECT m.primarytitle FROM movies m INNER JOIN principal cm ON cm.movieId = m.Id INNER JOIN actors a ON cm.ActorId = a.Id where a.name IN ('" + entryASpecifier + "', '" + entryBSpecifier + "') GROUP BY m.primarytitle HAVING count(m.Id) > 1;";
                } else if (checkIn(entryA, names) && entryB == "/e") { // A is a name and B is empty
                    sqlStatement = sqlStatement + "id in (SELECT movieID FROM principal WHERE actorID = (SELECT ID FROM actors where name='" + entryASpecifier + "' LIMIT 1)) LIMIT 10;"; // WORKS
                } else if (checkIn(entryB, names) && entryA == "/e") { // B is a name and A is empty
                    sqlStatement = sqlStatement + "id in (SELECT movieID FROM principal WHERE actorID = (SELECT ID FROM actors where name='" + entryBSpecifier + "' LIMIT 1)) LIMIT 10;"; // WORKS
                }
                System.out.println(sqlStatement);
                //send statement to DBMS
                ResultSet result = stmt.executeQuery(sqlStatement);
                ResultSetMetaData rsmd = result.getMetaData();
                while(result.next()) {
                    for(int i = 1; i <= rsmd.getColumnCount(); ++i) {
                        output = output + "\t" + result.getString(i) + "\t";
                        System.out.print(result.getString(i) + "\t");
                    }
                    System.out.println("");
                }
            }




			return output;
		} catch (Exception e){
            System.out.println(e);
			// JavaFX show there was an error
		}

        return null;
    }

    public void close() {
        try {
            conn.close();
            try {
                FileOutputStream f = new FileOutputStream(new File("actorGraph.txt"));
                ObjectOutputStream o = new ObjectOutputStream(f);

                // Write objects to file
                o.writeObject(graph);

                o.close();
                f.close();

            } catch (FileNotFoundException e) {
                System.out.println("File not found");
            } catch (IOException e) {
                System.out.println("Error initializing stream");
            }
        } catch(Exception e) {
            // JavaFX output there was an error
        }
    }

    public static void main(String[] args) {
        dbInterface dbi = new dbInterface();
        dbi.query(null, null, "year", "1990");
    }
}