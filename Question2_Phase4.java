package sample;
import java.sql.*;
import java.util.*;
import java.util.stream.IntStream;

public class Question2_Phase4 {

    static boolean allTrue(boolean[] arr) {
        return IntStream.range(0, arr.length).allMatch(i -> arr[i]);
    }

    public static ArrayList<String> countFrequencies(ArrayList<String> list, int diffyears)
    {
        ArrayList<String> EligibleActors = new ArrayList<>(list.size());

        // hashmap to store the frequency of element
        Map<String, Integer> hm = new HashMap<String, Integer>();

        for (String i : list) {
            Integer j = hm.get(i);
            hm.put(i, (j == null) ? 1 : j + 1);
        }

        // displaying the occurrence of elements in the arraylist
        for (Map.Entry<String, Integer> val : hm.entrySet()) {
            if(val.getValue() >= diffyears) {
                EligibleActors.add(val.getKey());
//                System.out.println("Element " + val.getKey() + " "
//                        + "occurs"
//                        + ": " + (val.getValue()+1) + " times");//this is cause logic states that one is missed
                //through consecutive elements being tagged, since if last is tagged, you get error out of bounds
                //exception
            }
        }
        return EligibleActors;
    }





    public String contiguousCareers(String user_input1, String user_input2) {
        Connection conn = null;

        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://db-315.cse.tamu.edu/ironman4455_605",
                    "ironman4455_605", "studentpwd");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }//end catch

        System.out.println("Opened database successfully");

        try{
            Statement stmt = conn.createStatement();
            Scanner myObj = new Scanner(System.in);  // Create a Scanner object
//            System.out.println("Enter the range of years (2 inputs)");

            //String user_input1 =myObj.nextLine();
            //String user_input2 =myObj.nextLine();

//            System.out.println("The two years inputted are: " + user_input1+ " and "+user_input2+"\n");  // Output user input

            int n =3000;//this is the limit of the queries
            String sqlStatement = "SELECT m.primarytitle, a.name, startyear FROM movies m INNER JOIN actors a " +
                    "ON (m.id = a.knownfortitlesone) WHERE startyear BETWEEN "+ user_input1+ " AND "+ user_input2+ " AND titletype='movie' " +
                    "ORDER BY name ASC limit " + Integer.toString(n) +";";
            //send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);
            System.out.println("______________________________________");
            System.out.println("Debugging what result is :   " + result);
            String pp1,pp2,pp3;
            ArrayList<String> ptArrayList = new ArrayList<>(n);
            ArrayList<String> nameArrayList = new ArrayList<>(n);
            ArrayList<String> startYearArrayList = new ArrayList<>(n);
            while (result.next()) {
                pp1=result.getString("primarytitle");
                ptArrayList.add(pp1);
                pp2=result.getString("name");
                nameArrayList.add(pp2);
                pp3=result.getString("startyear");
                startYearArrayList.add(pp3);
//                System.out.println("The primary title, name, and start year are : " + pp1 + ", " + pp2 + ", " + pp3);
            }

            ArrayList<String> duplicateNames = new ArrayList<>(n);
            int diffYears = (Integer.parseInt(user_input2)- Integer.parseInt(user_input1));

            for(int i=0; i <nameArrayList.size()-2;i++) {
                if (nameArrayList.get(i).equals(nameArrayList.get(i+1))) {
                    duplicateNames.add(nameArrayList.get(i));
//                    System.out.println(nameArrayList.get(i)+" "+startYearArrayList.get(i));
                }
            }
            ArrayList<String> ActorsThatAreEligible =  countFrequencies(duplicateNames,diffYears);

            // hashmap to store the frequency of element
            Map<String, Integer> hm1 = new HashMap<String, Integer>();

            for (String i : duplicateNames ) {
                Integer j = hm1.get(i);
                hm1.put(i, (j == null) ? 1 : j + 1);
            }

            //--------------All I need  to work on right-----------------------------------
            System.out.println("The Eligible actors with their corresponding years are : -");
            //outerloop:
            ArrayList<String> actorSolutions =  new ArrayList<>();

            for(int i =0; i<ActorsThatAreEligible.size();i++){
                //System.out.println(ActorsThatAreEligible.get(i));
                String line = ActorsThatAreEligible.get(i);
                int indexValue =nameArrayList.indexOf(line);
                boolean array[];
                array = new boolean[diffYears+1];
                Arrays.fill(array, false);
                for(int j=indexValue;j < indexValue + hm1.get(line)+1 ;j++){
                    //System.out.println(j);
//                    System.out.println(nameArrayList.get(j) +" "+startYearArrayList.get(j));

                    //-----------------------End All i need to do for now --------------------------------
                    int h = 0;
                    for(int k= Integer.parseInt(user_input1); k <= Integer.parseInt(user_input2);k++){
                        //int k = Integer.parseInt(user_input1)  ;

                        if (Integer.toString(k).equals(startYearArrayList.get(j))) {
                            array[h] = true;
                        }
                        h++;
//                        else{
//                            break outerloop;
//                        }
                    }//End inner loop

                    //System.out.println("This actor is a solution" + nameArrayList.get(j) );

                }
//                for(int a= 0; a<2;a++){
//                    System.out.println(array[a]);
//                }
                if( allTrue(array) ){
                    //if all true in bool array then is a solution
                    //System.out.println("This is not an actor!!!!!!!! "+ ActorsThatAreEligible.get(i)+ " is a solution");
                   // System.out.println("This a solution for this actor : )" + ActorsThatAreEligible.get(i) );
                    actorSolutions.add(ActorsThatAreEligible.get(i));
                }
            }
            //System.out.println("There are no solution for this actor");
            System.out.println("These are all the solutions for the questions");
            for(int lit =0;lit<actorSolutions.size();lit++){
                System.out.println(actorSolutions.get(lit));
            }
            if(actorSolutions.isEmpty())
                return "No solution for given range";
            else
                return actorSolutions.get(0);


        } catch (Exception e){
            System.out.println("Error accessing Database.");
        }

        //closing the connection
        try {
            conn.close();
            System.out.println("Connection Closed.");
        } catch(Exception e) {
            System.out.println("Connection NOT Closed.");
        }//end catch
        return null;
    }//end main
}//end Class




