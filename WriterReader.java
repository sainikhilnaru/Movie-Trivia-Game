package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.io.Reader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;

public class WriterReader {

    public static void main(String[] args){
        WriterGraph graph = new WriterGraph();
        try {
            System.out.println("Attempting to re-open object file");
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
        
        // ArrayList<Integer> middle = new ArrayList<Integer>();
        // ArrayList<String> ids = new ArrayList<String>();
        String finalVals = new String();

        // int cap = 100;

        // graph.loadData(cap);

        // ids = graph.getInput("Viking Ringheim", "Olivia Norrie");

        // // int a = graph.getHash("nm0727622");
        // // int b = graph.getHash("nm3068222");
        
        // int a = graph.getHash(ids.get(0));
        // int b = graph.getHash(ids.get(1));

        // middle = graph.BFS(a, b);

        // for(int d = 0; d < middle.size(); d++)
        //     System.out.println(middle.get(d));

        finalVals = graph.run("Viking Ringheim", "Olivia Norrie");
        // System.out.println(finalVals);

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
	}
}