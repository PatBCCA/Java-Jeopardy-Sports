import java.util.Scanner;
import java.sql.*;

public class Main {
    public static void main(String[] args) {
        SQL.test();
        SportsJeopardy game = new SportsJeopardy();
        game.play();
    }
}


class SQL {
    public static void test() {
//        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/SportsTrivia", "postgres", "HardcorE14")) {
//            String SQL = "SELECT * from users";
//            PreparedStatement prepareStmnt = conn.prepareStatement(SQL);
//            ResultSet response = prepareStmnt.executeQuery();
//            while (response.next()) {
//                System.out.println(response.getInt("numOfWins"));
//            }
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
        Question question = new Question();
        question.fetchQuestionsFromDatabase();
    }
}