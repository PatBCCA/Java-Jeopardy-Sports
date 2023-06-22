import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.sql.*;

public class Question {
    public static Map<String, String> easyQuest = new HashMap<>();
    public static Map<String, String> medQuest = new HashMap<>();
    public static Map<String, String> hardQuest = new HashMap<>();

    private LinkedHashMap<String, String> questions;

//    static {
//        // Easy questions and answers
//        easyQuest.put("Which MLB team drafted Ken Griffey Jr?", "Seattle Mariners");
//        easyQuest.put("Only one NFL team has their logo on one side of the helmet and NOT on the other side. What team is this?", "Pittsburgh Steelers");
//        easyQuest.put("What notorious Boston sports fan, formerly of ESPN, sold his most recent venture to Spotify in early 2020 to bolster their podcast business?", "Bill Simmons");
//        easyQuest.put("Which bright-red bird is the mascot for the University of Louisville's NCAA sports teams?", "Cardinal");
//        easyQuest.put("What is the feline name of the sports teams of the University of Kentucky?", "Wildcats");
//
//        // Medium questions and answers
//        medQuest.put("Forty-one states are home to at least one active FBS (college football) program. Which of these states has the highest number of FBS programs at 12?", "Texas");
//        medQuest.put("What is the nickname of retired professional golfer Jack Nicklaus? Its origin goes back to the mascot of his high school in Upper Arlington, Ohio and it is incorporated into his brand's logo", "The Golden Bear");
//        medQuest.put("Founded in 2017, what shiny-sounding NHL expansion team is the first major professional sports team to represent Las Vegas?", "Golden Knights");
//        medQuest.put("In June of what year did the Boston Celtics win their 17th (and most recent) NBA Finals?", "2008");
//        medQuest.put("On the 2022 Forbes list of highest paid athletes, two of the top five are basketball players (LeBron James #2 and Steph Curry #5). The other three athletes in the top five all play the same sport. What sport?", "Soccer");
//
//        // Hard questions and answers
//        hardQuest.put("What since-relocated baseball team did Babe Ruth coach for a year after retiring?", "Dodgers");
//        hardQuest.put("The official state bird of Georgia was the mascot for what Atlanta NHL hockey team that played from 1999 until 2011?", "Thrashers");
//        hardQuest.put("From 1930-1970, qualifying teams vied for the Jules Rimet Trophy in what international tournament?", "World Cup");
//        hardQuest.put("Before the franchise relocated to the Twin Cities from the East Coast, what was the name of the team now known as the Minnesota Twins?", "Washington Senators");
//        hardQuest.put("Hailed by Phil Knight as \"Soul of Nike\" and by Sports Illustrated as \"America's Distance Prodigy,\" what Oregonian runner died at the age of 24 in 1975?", "Steve Prefontaine");
//    }

    public void fetchQuestionsFromDatabase() {
        try {
            // Establish database connection
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/SportsTrivia", "postgres", "HardcorE14");

            // Prepare SQL statement
            String sql = "SELECT question, answer FROM questions WHERE difficulty_id = ?";
            // Easy Statement
            PreparedStatement easyStatement = connection.prepareStatement(sql);
            easyStatement.setInt(1, 1);
            // medium Statement
            PreparedStatement medStatement = connection.prepareStatement(sql);
            medStatement.setInt(1, 2);
            // hard Statement
            PreparedStatement hardStatement = connection.prepareStatement(sql);
            hardStatement.setInt(1, 3);

            // Execute query and retrieve results
            ResultSet resultSetEasy = easyStatement.executeQuery();
            while (resultSetEasy.next()) {
                String question = resultSetEasy.getString("question");
                String answer = resultSetEasy.getString("answer");
                easyQuest.put(question, answer);

            }
            ResultSet resultSetMed = medStatement.executeQuery();
            while (resultSetMed.next()) {
                String question = resultSetMed.getString("question");
                String answer = resultSetMed.getString("answer");
                medQuest.put(question, answer);


            }
            ResultSet resultSetHard = hardStatement.executeQuery();
            while (resultSetHard.next()) {
                String question = resultSetHard.getString("question");
                String answer = resultSetHard.getString("answer");
                hardQuest.put(question, answer);


            }
            // for testing remove later
//            for (Map.Entry quest : easyQuest.entrySet()){
//                System.out.println(quest.getKey());
//                System.out.println(quest.getValue());
//            }
//            for (Map.Entry quest : medQuest.entrySet()){
//                System.out.println(quest.getKey());
//                System.out.println(quest.getValue());
//            }
//            for (Map.Entry quest : hardQuest.entrySet()){
//                System.out.println(quest.getKey());
//                System.out.println(quest.getValue());
//            }

            // Close resources
            resultSetEasy.close();
            easyStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

