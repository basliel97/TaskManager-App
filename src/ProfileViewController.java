import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ProfileViewController {

    @FXML
    private JFXButton cancelBTN;

    @FXML
    private JFXButton cancelBTN1;

    @FXML
    private JFXTextField confirmTF1;

    @FXML
    private JFXButton editPassBTN;

    @FXML
    private JFXButton editProfileBTN;

    @FXML
    private JFXTextField fullNameTF;

    @FXML
    private JFXTextField fullNameTF1;

    @FXML
    private Label fullNamelabel;

    @FXML
    private Label profileLabel;

    @FXML
    private JFXButton saveBTN;

    @FXML
    private JFXButton saveBTN1;

    @FXML
    private Label userNameLabel;

    @FXML
    private JFXTextField userNameTf;

    @FXML
    private JFXTextField userNameTf1;

    @FXML
    private VBox vbox;

    @FXML
    private VBox vbox1;

    @FXML
    private VBox vbox2;

    private Parent root;
    private Stage stage;
    private Scene scene;
    private DataBase db = new DataBase();
    private Connection con = db.getConnection();
    private int resultSet;
    private ResultSet resultSet1;

    @FXML
    void onCancelHandler(ActionEvent event) {
        vbox1.setVisible(false);
        vbox.setVisible(true);
    }

    @FXML
    void onCancelHandler1(ActionEvent event) {
        vbox2.setVisible(false);
        vbox.setVisible(true);

    }

    @FXML
    void onEditPassHandler(ActionEvent event) {
        vbox.setVisible(false);
        vbox2.setVisible(true);
    }

    @FXML
    void onEditProfileHandler(ActionEvent event) {
        vbox.setVisible(false);
        vbox1.setVisible(true);

    }

    @FXML
    void onSaveHandler(ActionEvent event) throws SQLException {

        try {

            if (fullNameTF.getText().isEmpty() || userNameTf.getText().isEmpty()) {
                showAlert("Error", "Please fill in all text fields!");
                throw new Exception("Custom Exception");
            }

            resultSet1 = con.createStatement()
                    .executeQuery("select * from Users  where UserName = '" + userNameTf.getText() + "';");

            while (resultSet1.next()) {
                showAlert("Error", "UserName Already Taken Please Choose Another User Name");

            }
            resultSet = con.createStatement()
                    .executeUpdate("UPDATE users  SET UserName = '" + userNameTf.getText() + "', FullName = '"
                            + fullNameTF.getText() + "' where username ='" + LoginController.getUsername() + "';");

            showAlert("success", "Profile  Edited SuccessFully");
        } catch (Exception e) {

            showAlert("Error", "Profile Not Edited");
        }

    }

    @FXML
    void onSaveHandler1(ActionEvent event) throws Exception {

        resultSet1 = con.createStatement()
                .executeQuery("select Password from Users where userName = '" + LoginController.getUsername() + "';");
        String oldPassword;
        resultSet1.next();
        oldPassword = resultSet1.getString("Password");
        try {
            if (fullNameTF1.getText().isEmpty() || userNameTf1.getText().isEmpty() || confirmTF1.getText().isEmpty()) {
                showAlert("Error", "Please fill in all text fields!");
                throw new Exception("Custom Exception");
            } else if (!fullNameTF1.getText().equals(oldPassword)) {
                showAlert("Error", "Password Donot Match");
                throw new Exception("Custom Exception");
            } else if (!userNameTf1.getText().equals(confirmTF1.getText())) {
                showAlert("Error", "Passwords do not match.");
                throw new Exception("Custom Exception");
            } else {

                resultSet = con.createStatement().executeUpdate("UPDATE Users  SET Password = '" + userNameTf1.getText()
                        + "' where username ='" + LoginController.getUsername() + "';");
                showAlert("success", "Password Edited SuccessFully");
                userNameTf1.clear();
                confirmTF1.clear();
                fullNameTF1.clear();

            }

        } catch (Exception e) {
            System.out.println("Not Edited");
        }

    }

    public void initialize() throws SQLException {
        String username = LoginController.getUsername();

        resultSet1 = con.createStatement()
                .executeQuery("SELECT FullName FROM users WHERE username = '" + username + "' ;");

        while (resultSet1.next()) {

            profileLabel.setText("Hello, " + resultSet1.getString("FullName"));
            fullNamelabel.setText(resultSet1.getString("FullName"));
            userNameLabel.setText(username);
        }
    }

    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
