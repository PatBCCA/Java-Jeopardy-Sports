import java.util.*;
import java.sql.*;

public class SportsJeopardy {
    private LinkedHashMap<String, Integer> scores = new LinkedHashMap<>();
    private UserAuthentication userAuth = new UserAuthentication();;
    private Map<String, String> easyQuest;
    private Map<String, String> medQuest;
    private Map<String, String> hardQuest;



    public String signUp() {
        Scanner scanner = new Scanner(System.in);

        // Get username and password from the user
        System.out.println("Enter your desired username:");
        String username = scanner.nextLine();
        System.out.println("Enter your desired password:");
        String password = scanner.nextLine();

        // Sign up the user
        boolean signUpSuccessful = false;
        while (!signUpSuccessful) {
            signUpSuccessful = userAuth.signUpUser(username, password);

            if (signUpSuccessful) {
                System.out.println("Sign up successful!");
                // Continue with the game logic or additional functionality
            } else {
                System.out.println("Sign up failed. Please try again.");

                // Prompt the user to enter a new username and password
                // You can use a Scanner object to read user input
                System.out.print("Username: ");
                username = scanner.nextLine();
                System.out.print("Password: ");
                password = scanner.nextLine();
            }
        }
        return username;
    }

    public String login() {
        Scanner scanner = new Scanner(System.in);

        // Get username and password from the user
        System.out.println("Enter your username:");
        String username = scanner.nextLine();
        System.out.println("Enter your password:");
        String password = scanner.nextLine();

        // Validate the user
        boolean validUser = false;
        while (!validUser) {
            validUser = userAuth.loginUser(username, password);

            if (validUser) {
                System.out.println("Login successful!");
                // Continue with the game logic or additional functionality
            } else {
                System.out.println("Invalid username or password. Please try again.");

                // Prompt the user to enter username and password again
                // You can use a Scanner object to read user input
                System.out.print("Username: ");
                username = scanner.nextLine();
                System.out.print("Password: ");
                password = scanner.nextLine();
            }
        }
        return username;
    }


    public void play() {
        String username1 = ""; // Variable to store Player 1's username
        String username2 = ""; // Variable to store Player 2's username
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Pat's Sports Trivia Challenge!");
        System.out.println("Player 1 Select from the following");
        System.out.println("1. Sign Up");
        System.out.println("2. Login");
        String choice = scanner.nextLine();

        // Process the user's choice
        while (!choice.equalsIgnoreCase("1") || !choice.equalsIgnoreCase("2")) {
            if (choice.equalsIgnoreCase("1")) {
                username1 = signUp();
                break;
            } else if (choice.equalsIgnoreCase("2")) {
                username1 = login();
                break;
            } else {
                System.out.println("Invalid choice.");
                System.out.println("Player 1 Select from the following");
                System.out.println("1. Sign Up");
                System.out.println("2. Login");
                choice = scanner.nextLine();
            }
        }

        System.out.println("Player 2 Select from the following");
        System.out.println("1. Sign Up");
        System.out.println("2. Login");
        choice = scanner.nextLine();

        while (!choice.equalsIgnoreCase("1") || !choice.equalsIgnoreCase("2")) {
            if (choice.equalsIgnoreCase("1")) {
                username2 = signUp();
                break;
            } else if (choice.equalsIgnoreCase("2")) {
                username2 = login();
                break;
            } else {
                System.out.println("Invalid choice.");
                System.out.println("Player 2 Select from the following");
                System.out.println("1. Sign Up");
                System.out.println("2. Login");
                choice = scanner.nextLine();
            }
        }

        scores.put(username1, 0);
        scores.put(username2, 0);

        System.out.println("Player 1: " + username1 + "   Number of Wins: " + queryNumOfwins(username1));
        System.out.println("Player 2: " + username2 + "   Number of Wins: " + queryNumOfwins(username2));

        int round = 1;
        boolean running = true;
        int playerIndex = 0;

        // Track the number of selections for each player
        int[] easyQuestSelections = new int[scores.size()];

        while (running) {
            // getCurrentPlayer(playerIndex)
            String currentPlayer = getCurrentPlayer(playerIndex + 1, username1, username2);
            System.out.println(currentPlayer + "'s turn.");
            System.out.println("Choose a category number:");
            displayCategoriesWithNumbers();
            String userInput = scanner.nextLine();

            // Check if the selected category is Question.easyQuest and if the player has exceeded the maximum selections
            if (userInput.equals("1") && easyQuestSelections[playerIndex] >= 3) {
                System.out.println(currentPlayer + " has reached the maximum number of selections for Easy Category.");
                System.out.println("Select from Medium or Hard to continue");
                continue;
            }

            Map<String, String> currentQuestions = (userInput.equals("1")) ? Question.easyQuest : (userInput.equals("2")) ? Question.medQuest : (userInput.equals("3")) ? Question.hardQuest : null;
            if (currentQuestions == null) {
                System.out.println("Invalid option");
                continue;
            }
            Random random = new Random();
            int randomQuestionNum = random.nextInt(currentQuestions.size());
            String question = (String) currentQuestions.keySet().toArray()[randomQuestionNum];
            String answer = currentQuestions.get(question);
            System.out.println(question);

            // Process player's answer
            System.out.println("Enter your answer:");
            String playerAnswer = scanner.nextLine();

            // Check if the answer is correct
            if (playerAnswer.equalsIgnoreCase(answer)) {
                // Increment player's score based on question difficulty
                int points = getPointsByDifficulty(userInput);
                incrementPlayerScore(currentPlayer, points);
                System.out.println("Correct! " + currentPlayer + " gets " + points + " points.");
            } else {
                System.out.println("Incorrect!");
            }
            // Update the number of selections for the current player
            if (userInput.equals("1")) {
                easyQuestSelections[playerIndex]++;
            }

            playerIndex++;
            round++;
            // Ensure playerIndex stays within valid range
            if (playerIndex >= scores.size()) {
                playerIndex = 0;
            }

            if(playerIndex == 0 && round > scores.size()) {
                displayScores();
            }

            if (round > 10) {
                running = false;
            }
        }

        // Game ended, display scores and winner
        System.out.println("Game over!");
        System.out.println("Final scores:");
        displayScores();
        String winner = determineWinner();
        applyWins(winner);
        System.out.println("The winner is: " + winner);
    }

    private String getCurrentPlayer(int questionNumber, String username1, String username2) {
        int currentPlayerIndex = (questionNumber % scores.size() == 0) ? 2: 1;
        if (currentPlayerIndex == 1) {
            return username1;
        } else {
            return username2;
        }
    }


    private void displayCategoriesWithNumbers() {
        System.out.println("1. Easy");
        System.out.println("2. Medium");
        System.out.println("3. Hard");
    }

    private int getPointsByDifficulty(String difficulty) {
        if (difficulty.equals("1")) {
            return 25;
        } else if (difficulty.equals("2")) {
            return 50;
        } else if (difficulty.equals("3")) {
            return 100;
        }
        return 0;
    }

    private void incrementPlayerScore(String player, int points) {
        int currentScore = scores.get(player);
        scores.put(player, currentScore + points);
    }

    private void displayScores() {
        for (String player : scores.keySet()) {
            int score = scores.get(player);
            System.out.println(player + ": " + score + " points");
        }
    }

    private String determineWinner() {
        String winner = "";
        int maxScore = 0;
        for (String player : scores.keySet()) {
            int score = scores.get(player);
            if (score > maxScore) {
                maxScore = score;
                winner = player;
            } else if (score == maxScore) {
                winner += " and " + player;
            }
        }
        return winner;
    }

    public void applyWins(String username) {
        int numOfWins = queryNumOfwins(username) + 1;
        try {
            // Establish database connection
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/SportsTrivia", "postgres", "HardcorE14");

            // Prepare SQL statement
            String sql = "UPDATE users SET num_of_wins = ? WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, numOfWins);
            statement.setString(2, username);

            // Execute the query
            int rowsAffected = statement.executeUpdate();
                if (rowsAffected == 0) {
                    System.out.println("UPDATE NOT EXECUTED");
                }

            // Close the statement
            statement.close();


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public Integer queryNumOfwins (String username) {
        try {
            // Establish database connection
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/SportsTrivia", "postgres", "HardcorE14");

            // Prepare SQL statement
            String sql = "SELECT num_of_wins FROM users WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);

            // Execute the query
            ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()){
                    Integer numOfWins = resultSet.getInt("num_of_wins");
                    return numOfWins;
                }
            // Close the statement
            statement.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

