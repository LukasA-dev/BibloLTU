package com.example.bibloltu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MainScene {
    @FXML
    private Button loanTabButton;

    @FXML
    private TextField deleteItemCopyField;
    @FXML
    private Button removeItemCopy;
    @FXML
    private TextField deleteItemField;
    @FXML
    private Button removeItemButton;
    @FXML
    private Text SearchResultsText;
    @FXML
    private TextField SearchField;
    @FXML
    private Button AddItemButton;
    @FXML
    private Button AddItemCopyButton;
    @FXML
    private TextField isbnField;
    @FXML
    private TextField titleField;
    @FXML
    private TextField genreField;
    @FXML
    private TextField typeField;
    @FXML
    private TextField authorDirectorField;
    @FXML
    private TextField publisherField;
    @FXML
    private Text userNameText;

    @FXML
    private Text adminTitle;

    @FXML
    private TabPane tabPane;
    @FXML
    private Button activeLoansButtonTab;
    @FXML
    private Button overdueButtonTab;
    @FXML
    private Button returnButtonTab;
    @FXML
    private Button addItemButtonTab;
    @FXML
    private Button removeItemButtonTab;
    @FXML
    private Button logOutButton;
    @FXML
    private Button searchButton;


    @FXML
    private TableView<SearchView> searchViewTableView;
    @FXML
    private TableColumn<SearchView, String> titleColumn;
    @FXML
    private TableColumn<SearchView, String> typeColumn;
    @FXML
    private TableColumn<SearchView, String> isbnColumn;
    @FXML
    private TableColumn<SearchView, String> genreColumn;
    @FXML
    private TableColumn<SearchView, String> locationColumn;
    @FXML
    private TableColumn<SearchView, String> Director_AuthorColumn;

    @FXML
    private TableView<LoansTableView> loansTableView;
    @FXML
    private TableColumn<LoansTableView, String> loanIDColumn;
    @FXML
    private TableColumn<LoansTableView, String> loanTitleColumn;
    @FXML
    private TableColumn<LoansTableView, String> barcodeColumn;
    @FXML
    private TableColumn<LoansTableView, LocalDate> borrowDateColumn;
    @FXML
    private TableColumn<LoansTableView, LocalDate> returnDateColumn;

    @FXML
    private TextField isbnCopyField;

    @FXML
    private TextField barcodeCopyField;

    @FXML
    private CheckBox referenceCopyCheckbox;

    @FXML
    private TextField loanCopyField;
    @FXML
    private TextField returnField;
    @FXML
            private TextField locationField;
    @FXML
            private Text userLoanText;
    @FXML
            private Text returnMessage;
    @FXML
    private Button editUserButton;
    @FXML
    private TextField userIDEdit;
    @FXML
    private ChoiceBox<String> editUserTypeChoiceBox;
    private String[] choiceUserType = {"Student", "Researcher", "Admin", "Employee"};

    //Global variables
    UserInfo userInfo = new UserInfo();
    int loggedInUserID = userInfo.getUserID();


    @FXML
    public void initialize() {
        editUserTypeChoiceBox.getItems().addAll(choiceUserType);

        //Displays logged-in user
        setUserNameText("Logged in as ", userInfo.getUserID());
        //Initlializes start page to load loans
        loadLoans(loggedInUserID);

        //Default values
        addItemButtonTab.setOpacity(0);
        addItemButtonTab.setDisable(true);
        removeItemButtonTab.setOpacity(0);
        removeItemButtonTab.setDisable(true);
        adminTitle.setOpacity(0);

        //Displays the correct interface depending on the userType
        if (userInfo.getLoggedInType().equals("Admin")) {
            setOpacityAndDisable(addItemButtonTab, 1, false);
            setOpacityAndDisable(removeItemButtonTab, 1, false);
            adminTitle.setOpacity(1);
        } else if (userInfo.getLoggedInType().equals("Guest")) {
            setOpacityAndDisable(activeLoansButtonTab, 0, true);
            setOpacityAndDisable(overdueButtonTab, 0, true);
            setOpacityAndDisable(returnButtonTab, 0, true);
            setOpacityAndDisable(loanTabButton, 0, true);
            setOpacityAndDisable(editUserButton, 0, true);

            logOutButton.setText("Log in panel");

            Tab tabSeven = tabPane.getTabs().get(6);
            tabPane.getSelectionModel().select(tabSeven);
        } else if (userInfo.getLoggedInType().equals("Student") || userInfo.getLoggedInType().equals("Researcher")) {
            setOpacityAndDisable(returnButtonTab, 0, true);
            setOpacityAndDisable(editUserButton, 0, true);
        }
    }
    //Helps clean up redundant code
    private void setOpacityAndDisable(Button button, double opacity, boolean isDisable) {
        button.setOpacity(opacity);
        button.setDisable(isDisable);
    }


    //Displays username on navbar
    private String getUserNameById(int userID){
        String userName = "Guest";
        try (Connection connection = DatabaseUtil.getConnection()){
            String query = "SELECT userName FROM user WHERE userID =?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,userID);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                userName=rs.getString("userName");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return userName;
    }
    private void setUserNameText(String prefix, int userID){
        userInfo.setUserID(userID);
        String userName = getUserNameById(userID);
        this.userNameText.setText(prefix + userName);
    }


    //Main buttons here

    //Add item copy
    @FXML
    private void SubmitCopyPressed(ActionEvent event) {
        addItemCopy();
    }
    //Display return tab
    @FXML
    private void returnButtonTabPressed(ActionEvent event) {
        // This method will be called when the button is clicked
        handleButtonPressed(2, returnButtonTab);
    }

    //Display add-item tab
    @FXML
    private void addItemButtonTabPressed(ActionEvent actionEvent) {
        // This method will be called when the button is clicked
        handleButtonPressed(4, addItemButtonTab);
    }

    //remove item tab
    @FXML
    private void removeItemButtonTabPressed(ActionEvent event) {
        handleButtonPressed(5, removeItemButton);
        selectedButton(removeItemButton);
    }

    //Displays item-copy tabpane
    @FXML
    private void ItemCopyButtonPressed(ActionEvent actionEvent) {
        Tab tabEight = tabPane.getTabs().get(7);
        tabPane.getSelectionModel().select(tabEight);
        selectedButton(AddItemButton);
        AddItemButton.setUnderline(false);
    }

    //Logs out signed-in user
    @FXML
    private void logOutPressed(ActionEvent event) throws IOException {
        // Loads sign in scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("bibloLtuScene.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        // Get the stage object that represents the application window
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        //Logs out user
        userInfo.setUserID(0);


        // Set the new scene on the stage
        stage.setScene(scene);
        stage.show();
    }

    //Displays remove item tab
    @FXML
    private void deleteItemPressed(ActionEvent event) {
        removeItem();
    }

    //Return an item
    @FXML
    private void returnButtonPressed(ActionEvent actionEvent) {
        returnItem();
    }

    //Loan a copy
    @FXML
    private void LoanCopyButtonPressed(ActionEvent actionEvent) {
        loanCopy();
    }

    //Edit user
    @FXML
    private void pushEditUser(ActionEvent event) {
        editUser();
    }
    //Displays item-button
    @FXML
    private void ItemButtonPressed(ActionEvent actionEvent) {
        Tab tabNine = tabPane.getTabs().get(8);
        tabPane.getSelectionModel().select(tabNine);
        selectedButton(AddItemCopyButton);
        AddItemCopyButton.setUnderline(false);
    }
    //Pushes item edit
    @FXML
    private void itemPush(ActionEvent actionEvent) {
        pushItem();
    }

    //Active loans button
    @FXML
    private void activeLoansButtonTabPressed(ActionEvent event) {
        // This method will be called when the button is clicked
        handleButtonPressed(0, activeLoansButtonTab);

        userLoanText.setText("Active Loans");
        returnMessage.setOpacity(0);
        loadLoans(loggedInUserID);
    }

    //Overdue tab will display
    @FXML
    private void overdueButtonTabPressed(ActionEvent event) {
        handleButtonPressed(0, overdueButtonTab);
        userLoanText.setText("Overdue Loans");
        returnMessage.setOpacity(1);
        updateOverdueLoans(loggedInUserID);
    }

    //Search button
    @FXML
    private void searchButtonPressed(ActionEvent actionEvent) {
        handleButtonPressed(6,searchButton);
        String search = SearchField.getText();
        SearchResultsText.setText("Search Results for '" + search + "'");
        searchButton.setUnderline(false);
        loadSearches();
    }
    @FXML
    void deleteItemCopyPressed(ActionEvent event) {
        removeItemCopy();
    }

    //Functions here


    //Add item copy
    private void addItemCopy(){
        String isbn = isbnCopyField.getText();
        String barcode = barcodeCopyField.getText();
        boolean isReferenceCopy = referenceCopyCheckbox.isSelected();

        if (isbn.isEmpty() || barcode.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all required fields.");
            alert.showAndWait();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation Dialog");
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to add a copy of this book?");
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try (Connection connection = DatabaseUtil.getConnection()) {
                // Check if the barcode exists in the item table
                String checkIsbnQuery = "SELECT * FROM item WHERE ISBN = ?";
                PreparedStatement checkIsbnStatement = connection.prepareStatement(checkIsbnQuery);
                checkIsbnStatement.setString(1, isbn);
                ResultSet barcodeResult = checkIsbnStatement.executeQuery();

                if (!barcodeResult.next()) {
                    // The barcode was not found in the item table
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("The entered ISBN does not exist in the library.");
                    alert.showAndWait();
                    return;
                }

                // Insert the copy information into the item_copy table
                String query = "INSERT INTO item_copy (ISBN, Barcode, Available, ReferenceCopy) VALUES (?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, isbn);
                statement.setString(2, barcode);
                statement.setInt(3, 1); // Set Specific_Availability to true (1)
                statement.setInt(4, isReferenceCopy ? 1 : 0); // Set Reference_Copy to true (1) or false (0)
                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    // The copy was successfully added to the database
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("A copy of the book has been added to the library.");
                    alert.showAndWait();

                    // Clear the input fields
                    isbnCopyField.clear();
                    barcodeCopyField.clear();
                    referenceCopyCheckbox.setSelected(false);

                } else {
                    // Something went wrong while adding the copy to the database
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Something went wrong while adding the copy to the database.");
                    alert.showAndWait();
                }
            } catch (SQLException e) {
                // Handle any database errors that may occur
                e.printStackTrace();
            }
        }
    }

    //Remove an item copy
    private void removeItemCopy(){
        String barcode = deleteItemCopyField.getText();

        if (barcode.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter an Barcode to delete.");
            alert.showAndWait();
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm delete");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to delete this item copy?");
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try (Connection connection = DatabaseUtil.getConnection()) {
                // First delete all copies of the item from the item_copy table
                String copyQuery = "DELETE FROM item_copy WHERE Barcode = ?";
                PreparedStatement copyStatement = connection.prepareStatement(copyQuery);
                copyStatement.setString(1, barcode);
                int copiesDeleted = copyStatement.executeUpdate();

                // Then delete the item itself from the item table


                if (copiesDeleted > 0) {
                    // The item was successfully deleted from the database
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Item copy with ISBN " + barcode + " was deleted from the database.");
                    successAlert.showAndWait();

                    // Clear the input field
                    deleteItemCopyField.clear();
                } else {
                    // No items were found with the given ISBN
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("No items were found with barcode " + barcode + ".");
                    errorAlert.showAndWait();
                }
            } catch (SQLException e) {
                // Handle any database errors that may occur
                e.printStackTrace();
            }
        }
    }
    //Remove an item blueprint
    private void removeItem(){
        String isbn = deleteItemField.getText();

        if (isbn.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter an ISBN to delete.");
            alert.showAndWait();
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm delete");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to delete this item? All item copies will be deleted as well.");
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try (Connection connection = DatabaseUtil.getConnection()) {
                // First delete all copies of the item from the item_copy table
                String copyQuery = "DELETE FROM item_copy WHERE ISBN = ?";
                PreparedStatement copyStatement = connection.prepareStatement(copyQuery);
                copyStatement.setString(1, isbn);
                int copiesDeleted = copyStatement.executeUpdate();

                // Then delete the item itself from the item table
                String itemQuery = "DELETE FROM item WHERE ISBN = ?";
                PreparedStatement itemStatement = connection.prepareStatement(itemQuery);
                itemStatement.setString(1, isbn);
                int rowsAffected = itemStatement.executeUpdate();

                if (rowsAffected > 0) {
                    // The item was successfully deleted from the database
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Item with ISBN " + isbn + " was deleted from the database. " + copiesDeleted + " copies were also deleted.");
                    successAlert.showAndWait();

                    // Clear the input field
                    deleteItemField.clear();
                } else {
                    // No items were found with the given ISBN
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("No items were found with ISBN " + isbn + ".");
                    errorAlert.showAndWait();
                }
            } catch (SQLException e) {
                // Handle any database errors that may occur
                e.printStackTrace();
            }
        }
    }
    //Pushes a new item blueprint to the database
    private void pushItem(){
        String isbn = isbnField.getText();
        String title = titleField.getText();
        String genre = genreField.getText();
        String type = typeField.getText();
        String authorDirector = authorDirectorField.getText();
        String publisher = publisherField.getText();
        String location = locationField.getText();

        if (isbn.isEmpty() || title.isEmpty() || genre.isEmpty() || type.isEmpty() || authorDirector.isEmpty() || publisher.isEmpty() || location.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all required fields to create an item.");
            alert.showAndWait();
            return;
        }
        int maxLoanWeeks;
        switch (type.toLowerCase()) {
            case "dvd" -> maxLoanWeeks = 2;
            case "course literature" -> maxLoanWeeks = 4;
            case "standard book" -> maxLoanWeeks = 8;
            default -> {
                // Handle unsupported type values
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Unsupported type. Please provide a valid type value.");
                alert.showAndWait();
                return;
            }
        }
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "INSERT INTO item (ISBN, Title, Genre, Type, Director_Author, Publisher, MaxLoan_Weeks, location) VALUES (?,?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, isbn);
            statement.setString(2, title);
            statement.setString(3, genre);
            statement.setString(4, type);
            statement.setString(5, authorDirector);
            statement.setString(6, publisher);
            statement.setInt(7, maxLoanWeeks);
            statement.setString(8, location);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                // The user was successfully added to the database
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Created!");
                alert.setHeaderText(null);
                alert.setContentText("Item blueprint was successfully created!");
                alert.showAndWait();

                // Clear the input fields
                isbnField.clear();
                titleField.clear();
                genreField.clear();
                typeField.clear();
                authorDirectorField.clear();
                publisherField.clear();
                locationField.clear();
            } else {
                // Something went wrong while adding the user to the database
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Something went wrong while creating an item blueprint.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            // Handle any database errors that may occur
            e.printStackTrace();
        }
    }

    //Returns an active loan
    private void returnItem(){
        String barcode = returnField.getText();

        try (Connection connection = DatabaseUtil.getConnection()) {
            // Fetch the loan record with the entered barcode and not returned yet
            String query = "SELECT * FROM loan WHERE Barcode = ? AND Returned = 0";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, barcode);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int loanID = resultSet.getInt("LoanID");

                // Update the loan record to mark it as returned
                String updateLoanQuery = "UPDATE loan SET Returned = 1 WHERE LoanID = ?";
                PreparedStatement updateLoanStatement = connection.prepareStatement(updateLoanQuery);
                updateLoanStatement.setInt(1, loanID);

                int rowsAffected = updateLoanStatement.executeUpdate();

                // Update the item_copy record to mark it as available
                String updateItemCopyQuery = "UPDATE item_copy SET Available = 1 WHERE Barcode = ?";
                PreparedStatement updateItemCopyStatement = connection.prepareStatement(updateItemCopyQuery);
                updateItemCopyStatement.setString(1, barcode);

                updateItemCopyStatement.executeUpdate();

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Return Item Information");

                if (rowsAffected > 0) {
                    alert.setHeaderText("Item Returned");
                    alert.setContentText("Successfully returned the item.");
                } else {
                    alert.setHeaderText("Return Failed");
                    alert.setContentText("Failed to return the item.");
                }

                alert.showAndWait();
            } else {
                // No active loan record found for the entered barcode
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Return Warning");
                alert.setHeaderText("Loan Record Not Found");
                alert.setContentText("No active loan record found for the entered barcode.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            // Handle database errors
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Return Error");
            alert.setHeaderText("Database Error");
            alert.setContentText("An error occurred while processing the return: " + e.getMessage());
            alert.showAndWait();
        }
    }

    //Function for admin to change usertype for specific userid
    private void editUser(){
        // Establish a database connection
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // Get the userID from the JavaFX controls
            String userIDText = userIDEdit.getText();

            // Check if the userIDText is empty
            if (userIDText == null || userIDText.trim().isEmpty()) {
                Alert alert = new Alert(AlertType.ERROR, "User ID must be specified.");
                alert.showAndWait();
                return;
            }

            int userID = Integer.parseInt(userIDText);
            String userType = editUserTypeChoiceBox.getValue();

            // Check if userType is null or empty
            if (userType == null || userType.trim().isEmpty()) {
                Alert alert = new Alert(AlertType.ERROR, "User type must be specified.");
                alert.showAndWait();
                return;
            }

            // Get a connection
            conn = DatabaseUtil.getConnection();

            // Create a SQL UPDATE query
            String sql = "UPDATE user SET userType = ? WHERE userID = ?";

            // Create a PreparedStatement
            pstmt = conn.prepareStatement(sql);

            // Set the parameters
            pstmt.setString(1, userType);
            pstmt.setInt(2, userID);

            // Execute the query
            int updatedRows = pstmt.executeUpdate();

            Alert alert;
            if(updatedRows > 0) {
                alert = new Alert(AlertType.INFORMATION, "User type updated successfully.");
            } else {
                alert = new Alert(AlertType.ERROR, "No user found with the provided userID.");
            }
            alert.showAndWait();
        } catch (NumberFormatException ex) {
            Alert alert = new Alert(AlertType.ERROR, "User ID must be a valid number.");
            alert.showAndWait();
        } catch (SQLException ex) {
            Alert alert = new Alert(AlertType.ERROR, "Failed to update user type: " + ex.getMessage());
            alert.showAndWait();
        } finally {
            // Close the PreparedStatement and Connection to free up resources
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                Alert alert = new Alert(AlertType.ERROR, "Failed to close database resources: " + ex.getMessage());
                alert.showAndWait();
            }
        }
    }

    //Activates a new on logged-in user with the scanned (or entered) barcode
    private void loanCopy(){
        String barcode = loanCopyField.getText();
        int userID = userInfo.getUserID();

        try (Connection connection = DatabaseUtil.getConnection()) {
            // Check if the barcode exists in the system
            String checkBarcodeQuery = "SELECT COUNT(*) FROM item_copy WHERE Barcode = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkBarcodeQuery);
            checkStatement.setString(1, barcode);

            ResultSet resultSet = checkStatement.executeQuery();
            resultSet.next();
            int barcodeCount = resultSet.getInt(1);

            if (barcodeCount == 0) {
                // The barcode entered is not in the system
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Loan Warning");
                alert.setHeaderText("Barcode Not Found");
                alert.setContentText("The barcode entered is not in the system.");
                alert.showAndWait();
            } else {
                // Insert a new loan record for the logged-in user
                String query = "INSERT INTO loan (userID, Barcode) VALUES (?, ?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, userID);
                statement.setString(2, barcode);

                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    // Fetch details after successful loan creation
                    String detailsQuery = "SELECT item.Title, loan.Barcode, loan.BorrowDate, loan.ReturnDate " +
                            "FROM item " +
                            "INNER JOIN item_copy ON item.ISBN = item_copy.ISBN " +
                            "INNER JOIN loan ON item_copy.Barcode = loan.Barcode " +
                            "WHERE loan.Barcode = ?";
                    PreparedStatement detailsStatement = connection.prepareStatement(detailsQuery);
                    detailsStatement.setString(1, barcode);

                    ResultSet detailsResultSet = detailsStatement.executeQuery();

                    if (detailsResultSet.next()) {
                        String title = detailsResultSet.getString("Title");
                        String borrowDate = detailsResultSet.getString("BorrowDate");
                        String returnDate = detailsResultSet.getString("ReturnDate");

                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Loan Item Information");
                        alert.setHeaderText("RECEIPT");
                        alert.setContentText("Successfully created loan. \n" +
                                "Title: " + title + "\n" +
                                "Barcode: " + barcode + "\n" +
                                "Borrow Date: " + borrowDate + "\n" +
                                "Return Date: " + returnDate);
                        alert.showAndWait();
                    }
                } else {
                    // Failed to create loan
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Loan Item Information");
                    alert.setHeaderText("Loan Failed");
                    alert.setContentText("Failed to create loan.");
                    alert.showAndWait();
                }
            }
        } catch (SQLException e) {
            // Handle database errors
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Loan Error");
            alert.setContentText("An error occurred while processing the loan: " + e.getMessage());
            alert.showAndWait();
        }
    }


    //Underlines the pressed button and deselect all the rest.
    private void selectedButton(Button button) {
        List<Button> buttons = Arrays.asList(activeLoansButtonTab, overdueButtonTab, returnButtonTab, addItemButtonTab, removeItemButtonTab,loanTabButton,editUserButton);
        for (Button b : buttons) {
            b.setUnderline(b == button);
        }
    }
    // Generalized method
    private void handleButtonPressed(int tabIndex, Button button) {
        // Get the tab in the TabPane
        Tab tab = tabPane.getTabs().get(tabIndex);
        tabPane.getSelectionModel().select(tab);
        selectedButton(button);
    }


    @FXML
    private void editUserButtonPressed(ActionEvent event) {
        handleButtonPressed(1,editUserButton);
        returnMessage.setOpacity(0);
    }

    private void loadSearches(){
        String search = SearchField.getText();
        // Set up the TableView columns with the PropertyValueFactories
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        isbnColumn.setCellValueFactory(cellData -> cellData.getValue().isbnProperty());
        genreColumn.setCellValueFactory(cellData -> cellData.getValue().genreProperty());
        locationColumn.setCellValueFactory(cellData -> cellData.getValue().locationProperty());
        Director_AuthorColumn.setCellValueFactory(cellData -> cellData.getValue().Director_AuthorProperty());

        // Create a new ObservableList to hold the search results
        ObservableList<SearchView> SearchViews = FXCollections.observableArrayList();

        //Filters the search based on the search fields content
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT item.Title, item.Type, item.Genre, item.ISBN, item.Location, item.Director_Author " +
                    "FROM item " +
                    "WHERE item.Title LIKE ? OR item.Genre LIKE ? OR item.Type LIKE ? OR item.Location LIKE ? OR item.ISBN LIKE ? OR item.Director_Author LIKE ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + search + "%");
            statement.setString(2, "%" + search + "%");
            statement.setString(3, "%" + search + "%");
            statement.setString(4,"%" + search + "%");
            statement.setString(5, "%" + search + "%");
            statement.setString(6,"%" + search + "%");

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                SearchView searchViewEntry = new SearchView(
                        rs.getString("Title"),
                        rs.getString("Type"),
                        rs.getString("Genre"),
                        rs.getString("ISBN"),
                        rs.getString("Location"),
                        rs.getString("Director_Author")
                );
                SearchViews.add(searchViewEntry);
            }

        } catch (SQLException e) {
            // Handle any database errors that may occur
            e.printStackTrace();
        }

        // Set the searchViewTableView items to the new searchViews list
        searchViewTableView.setItems(SearchViews);
    }

    private void loadLoans(int loggedInUserID) {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT loan.LoanID, loan.BorrowDate, loan.ReturnDate, item.Title, item_copy.Barcode\n" +
                    "FROM loan\n" +
                    "JOIN item_copy ON loan.Barcode = item_copy.Barcode\n" +
                    "JOIN item ON item_copy.ISBN = item.ISBN\n" +
                    "WHERE loan.userID = ? AND loan.Returned = 0\n" +
                    "ORDER BY loan.BorrowDate DESC;";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, loggedInUserID);
            ResultSet rs = statement.executeQuery();

            ObservableList<LoansTableView> loansList = FXCollections.observableArrayList();
            loansTableView.setItems(loansList);
            loanIDColumn.setCellValueFactory(cellData -> cellData.getValue().loanIDProperty());
            loanTitleColumn.setCellValueFactory(cellData -> cellData.getValue().loanTitleProperty());
            barcodeColumn.setCellValueFactory(cellData -> cellData.getValue().barcodeProperty());
            borrowDateColumn.setCellValueFactory(cellData -> cellData.getValue().borrowDateProperty());
            returnDateColumn.setCellValueFactory(cellData -> cellData.getValue().returnDateProperty());
            while (rs.next()) {
                LoansTableView loanEntry = new LoansTableView(
                        rs.getString("LoanID"),
                        rs.getString("Title"),
                        rs.getString("Barcode"),
                        rs.getObject("BorrowDate", LocalDate.class),
                        rs.getObject("ReturnDate", LocalDate.class)
                );
                loansList.add(loanEntry);
            }

            loansTableView.setItems(loansList);
        } catch (SQLException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error fetching loans");
            alert.setContentText("An error occurred while fetching the loans: " + e.getMessage());
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error loading TableView");
            alert.setContentText("An error occurred while loading the TableView: " + e.getMessage());
            alert.showAndWait();
        }
    }


    private void updateOverdueLoans(int loggedInUserID) {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT loan.LoanID, item.Title as loanTitle, loan.Barcode, loan.BorrowDate, loan.ReturnDate FROM loan INNER JOIN item_copy ON loan.Barcode = item_copy.Barcode INNER JOIN item ON item_copy.ISBN = item.ISBN WHERE loan.Returned = 0 AND loan.userID = ? AND loan.ReturnDate < ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, loggedInUserID);
            statement.setObject(2, LocalDate.now());
            ResultSet rs = statement.executeQuery();

            ObservableList<LoansTableView> overdueLoansList = FXCollections.observableArrayList();
            while (rs.next()) {
                LoansTableView loanEntry = new LoansTableView(
                        rs.getString("LoanID"),
                        rs.getString("loanTitle"),
                        rs.getString("Barcode"),
                        rs.getObject("BorrowDate", LocalDate.class),
                        rs.getObject("ReturnDate", LocalDate.class)
                );
                overdueLoansList.add(loanEntry);
            }

            loansTableView.setItems(overdueLoansList);
        } catch (SQLException e) {
            // Handle any database errors that may occur
            e.printStackTrace();

            // Show an alert with the error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error fetching overdue loans");
            alert.setContentText("An error occurred while fetching the overdue loans: " + e.getMessage());
            alert.showAndWait();
        } catch (Exception e) {
            // Handle any other errors that may occur
            e.printStackTrace();

            // Show an alert with the error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error updating TableView");
            alert.setContentText("An error occurred while updating the TableView: " + e.getMessage());
            alert.showAndWait();
        }
    }


    //remove item & remove item copy choice
    @FXML
    private void removeItemPressed(ActionEvent actionEvent) {
        handleButtonPressed(9, removeItemButton);
        removeItemButton.setUnderline(false);
    }
    @FXML
    private void removeItemCopyPressed(ActionEvent actionEvent) {
        handleButtonPressed(10,removeItemCopy);
        removeItemCopy.setUnderline(false);
    }
    @FXML
    private void loanTabPressed(ActionEvent actionEvent) {
        handleButtonPressed(3,loanTabButton);
        loanTabButton.setUnderline(false);
    }
}