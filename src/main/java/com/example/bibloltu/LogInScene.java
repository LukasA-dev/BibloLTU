package com.example.bibloltu;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.*;

import java.io.IOException;
import java.time.LocalDate;

public class LogInScene {

@FXML
    private Text datePickerText;
@FXML
    private DatePicker datePickerField;
    UserInfo userInfo = new UserInfo();



    private String type;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button logInButton;

    @FXML
    private Button guestUserButton;

//Create account vars
@FXML
    private TextField newUserNameField;
    @FXML
    private Text newUserNameText;
    @FXML
    private PasswordField newUserPasswordField;
    @FXML
    private Text newUserPasswordText;
    @FXML
    private TextField newUserEmailField;
    @FXML
    private Text newUserEmailText;
    @FXML
    private Text bibloLtuLogo;
    @FXML
    private Button createUserButton;



    @FXML
    void handleLogInButtonAction(ActionEvent event) throws IOException {
        String email = usernameField.getText();
        String password = passwordField.getText();

        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT * FROM user WHERE mail = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String userType = resultSet.getString("userType");
                int userID = resultSet.getInt("userID");
                userInfo.setUserID(userID);
                System.out.println(userInfo.getUserID());

                // Set the loggedInType value based on the user's userType field
                if (userType.equals("Admin")) {
                    userInfo.setLoggedInType("Admin");
                } else if (userType.equals("Student")) {
                    userInfo.setLoggedInType("Student");
                } else {
                    userInfo.setLoggedInType("Guest");
                }

                // Load the new FXML file and create a new scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("BibloLtuMAIN.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);

                // Get the stage object that represents the application window
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                // Set the new scene on the stage
                stage.setScene(scene);
                stage.show();
                System.out.println(userInfo.getLoggedInType());

            } else {
                // If the input does not match, display an error message
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Error");
                alert.setHeaderText(null);
                alert.setContentText("Incorrect username or password");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            // Handle any database errors that may occur
            e.printStackTrace();
        }
    }

    @FXML
    void guestButtonAction(ActionEvent event) throws IOException {
        // Load the new FXML file and create a new scene
        userInfo.setLoggedInType("Guest");
        System.out.println(userInfo.getLoggedInType());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("BibloLtuMAIN.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);

        // Get the stage object that represents the application window
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Set the new scene on the stage
        stage.setScene(scene);
        stage.show();
    }

    private boolean isAnimationPlayed = false;

    // Shows the create user section with animation
    public void showCreateUser(ActionEvent actionEvent) {
        if (!isAnimationPlayed) {
            TranslateTransition translate = new TranslateTransition();
            translate.setNode(bibloLtuLogo);
            translate.setDuration(Duration.millis(500));
            translate.setByY(-200);

            translate.setOnFinished(event -> {
                newUserNameField.setOpacity(1);
                newUserNameField.setDisable(false);
                newUserNameText.setOpacity(1);

                newUserEmailField.setDisable(false);
                newUserEmailField.setOpacity(1);
                newUserEmailText.setOpacity(1);

                newUserPasswordField.setDisable(false);
                newUserPasswordField.setOpacity(1);
                newUserPasswordText.setOpacity(1);

                createUserButton.setOpacity(1);
                createUserButton.setDisable(false);

                datePickerField.setDisable(false);
                datePickerField.setOpacity(1);
                datePickerText.setOpacity(1);
            });

            translate.play();
            isAnimationPlayed = true;
        }
    }

    // Handles the action when the create user button is clicked
    @FXML
    void handleCreateUserButtonAction(ActionEvent event) {
        String name = newUserNameField.getText();
        String email = newUserEmailField.getText();
        String password = newUserPasswordField.getText();
        LocalDate dateOfBirth = datePickerField.getValue();

        // Check if all required fields are filled
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || dateOfBirth == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all required fields.");
            alert.showAndWait();
            return;
        }

        try (Connection connection = DatabaseUtil.getConnection()) {
            // Prepare SQL query to insert a new user into the database
            String query = "INSERT INTO user (userName, mail, password, dateOfBirth) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setDate(4, Date.valueOf(dateOfBirth));

            // Execute the query and get the number of affected rows
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                // The user was successfully added to the database
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("User Created");
                alert.setHeaderText(null);
                alert.setContentText("The user was successfully created.");
                alert.showAndWait();

                // Clear the input fields
                newUserNameField.clear();
                newUserEmailField.clear();
                newUserPasswordField.clear();
                datePickerField.setValue(null);
            } else {
                // Something went wrong while adding the user to the database
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Something went wrong while creating the user.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            // Handle any database errors that may occur
            e.printStackTrace();
        }

    }
}