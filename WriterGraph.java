package sample;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.io.Reader;
import java.util.ArrayList;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Collections;


public class WriterGraph implements Serializable {
    private HashMap<String, Integer> actors;
    // private HashMap<Integer, String> reverseActors;
    private int numActors = 0;
    private LinkedList<Integer> adj[];
    private String moviesBetween[][];
    public ArrayList <String> movieid;
    public ArrayList <String> movieidConfirmed;
    // ArrayList<String> movieOrdering;
    private static final long serialVersionUID = 1L;

    public WriterGraph() {
        moviesBetween = new String[10000][10000];
        adj = new LinkedList[1500000]; 
        for (int i = 0; i < 1500000; ++i) {
            adj[i] = new LinkedList<Integer>(); 
        }
        actors = new HashMap<String, Integer>();
        // reverseActors = new HashMap<Integer, String>();
        movieid = new ArrayList<String>();
        movieidConfirmed = new ArrayList<String>();
        // movieOrdering = new ArrayList<String>();
    }

    void addEdge(String v,String w, String movie) {
        if(actors.isEmpty()) {
            actors.put(v, numActors);
            // reverseActors.put(numActors, v);
            numActors++;
        } else {
            if(actors.get(v) == null) {
                actors.put(v, numActors);
                // reverseActors.put(numActors, v);
                numActors++;
                // System.out.println("Adding new actor to hash: " + v + ", they are actor: " + (numActors-1));
            }
        }
        if(actors.get(w) == null) {
            actors.put(w, numActors);
            // reverseActors.put(numActors, v);
            numActors++;
            // System.out.println("Adding new actor to hash: " + w + ", they are actor: " + (numActors-1));
        }
        moviesBetween[actors.get(v)][actors.get(w)] = movie;
        moviesBetween[actors.get(w)][actors.get(v)] = movie;
        adj[actors.get(v)].add(actors.get(w));
        adj[actors.get(w)].add(actors.get(v));
    } 

    int getHash(String id) {
        return actors.get(id);
    }

    ArrayList<Integer> BFS(int s, int g) { 
        System.out.println("s: " + s + " g: " + g);
        boolean visited[] = new boolean[numActors+1]; 
  
        LinkedList<Integer> queue = new LinkedList<Integer>(); 
        ArrayList<Integer> prev = new ArrayList<Integer>();
        ArrayList<Integer> shortest = new ArrayList<Integer>();
  
        visited[s]=true; 
        queue.add(s); 

        for(int i = 0; i < actors.size(); i++) {
            prev.add(-1);
        }
        
        while (queue.size() != 0) { 
            
            s = queue.poll(); 
            if(s == g) {
                String keyOut = "NONE";
                Set<String> keys = actors.keySet();
                for(String key: keys){
                    if(s == actors.get(key)){
                        keyOut = key;
                        break;
                    }
                }

                System.out.println("FOUND: " + s + " : " + keyOut);
                shortest.add(s);
                int curr = prev.get(s);
                shortest.add(curr);
                while(prev.get(curr) != -1) {
                    curr = prev.get(curr);
                    shortest.add(curr);
                }
                for(int q = 0; q < shortest.size() - 1; q++) {
                    System.out.println(shortest.get(q));
                }
                return shortest;
            } 
  
            Iterator<Integer> i = adj[s].listIterator(); 
            while (i.hasNext()) { 
                int n = i.next(); 
                if (!visited[n]) {
                    prev.set(n, s);
                    visited[n] = true; 
                    queue.add(n);
                } 
            } 
        } 
        return null;
    }

    public String finalizeResults(ArrayList<Integer> reverseOrder) {
        try(Connection con=DriverManager.getConnection("jdbc:postgresql://db-315.cse.tamu.edu:5432/ironman4455_605","ironman4455_605","studentpwd"); 
                    Statement statement = con.createStatement()) {
            ResultSet rs;
            String out = new String(); 
            Collections.reverse(reverseOrder);
            for(int i = 0; i < reverseOrder.size(); i++) {
                if(i < reverseOrder.size() - 1){ 
                    System.out.println(moviesBetween[reverseOrder.get(i)][reverseOrder.get(i+1)]);
                    rs = statement.executeQuery("SELECT primarytitle FROM movies WHERE id='" + moviesBetween[reverseOrder.get(i)][reverseOrder.get(i+1)] + "' LIMIT 1;");
                    rs.next();
                    out += rs.getString("primaryTitle");
                    out += "\t";
                }
            }
            return out;
        } catch(SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String run(String name1, String name2) {
        ArrayList<Integer> middle = new ArrayList<Integer>();
        ArrayList<String> ids = new ArrayList<String>();
        String finalVals = new String();

        int cap = 100;

        loadData(cap);

        ids = getInput(name1, name2);
        
        int a = getHash(ids.get(0));
        int b = getHash(ids.get(1));

        middle = BFS(a, b);

        for(int d = 0; d < middle.size(); d++)
            System.out.println(middle.get(d));

        finalVals = finalizeResults(middle);
        System.out.println(finalVals);
        return finalVals;
    }

    public ArrayList<String> getInput(String name1, String name2) {
        try(Connection con=DriverManager.getConnection("jdbc:postgresql://db-315.cse.tamu.edu:5432/ironman4455_605","ironman4455_605","studentpwd"); 
                    Statement statement = con.createStatement()) {
            ResultSet rs;
            ArrayList<String> out = new ArrayList<String>(); 

            rs = statement.executeQuery("SELECT id FROM actors WHERE name='" + name1 + "' LIMIT 1;");
            rs.next();
            out.add(rs.getString("id"));

            rs = statement.executeQuery("SELECT id FROM actors WHERE name='" + name2 + "' LIMIT 1;");
            rs.next();
            out.add(rs.getString("id"));

            return out;
        } catch(SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void loadData(int cap) {
        try(Connection con=DriverManager.getConnection("jdbc:postgresql://db-315.cse.tamu.edu:5432/ironman4455_605","ironman4455_605","studentpwd"); 
                    Statement statement = con.createStatement()) {
            System.out.println("SELECTING");
            ResultSet rs = statement.executeQuery("SELECT movieid FROM movieActorsOnly LIMIT 200;");
            while(rs.next()) {
                if(!movieid.contains(rs.getString("movieid"))) {
                    movieid.add(rs.getString("movieid")); 
                }
            } 
            
            ArrayList<ArrayList<String>> movieActors = new ArrayList<ArrayList<String>>();
            for(int i = 0; i < movieid.size() - 1; i++) {
                if(!movieidConfirmed.contains(movieid.get(i))) {
                    System.out.println("MOVIE ID: " + movieid.get(i));
                    rs = statement.executeQuery("SELECT actorid from movieActorsOnly WHERE movieid = '" + movieid.get(i) + "';");
                    System.out.println("MOVIE LOADED...");
                    while(rs.next()) {
                        System.out.println(movieid.get(i) + " " + rs.getString("actorid"));
                        try {
                            movieActors.get(i).add(rs.getString("actorid"));
                        } catch (Exception e) {
                            ArrayList<String> temp = new ArrayList<String>();
                            temp.add(rs.getString("actorid"));
                            movieActors.add(temp); 
                        }
                    }
                    movieidConfirmed.add(movieid.get(i));
                }
            }

            for(int i = 0; i < movieActors.size(); i++) {
                for(int j = 0; j < movieActors.get(i).size(); j++) {
                    for(int q = 0; q < movieActors.get(i).size(); q++) {
                        if(movieActors.get(i).get(j) != movieActors.get(i).get(q)) {
                            addEdge(movieActors.get(i).get(j), movieActors.get(i).get(q), movieid.get(i));
                        }
                    }
                }
            }
        } catch(SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}