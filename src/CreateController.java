import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import java.sql.ResultSet;
import java.sql.Connection;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class CreateController {

    @FXML
    private JFXTextField answerTF;

    @FXML
    private ChoiceBox<String> backupChoiceBox = new ChoiceBox<>();

    @FXML
    private JFXButton clearBTN;

    @FXML
    private JFXTextField confirmTF;

    @FXML
    private JFXButton createBTN;

    @FXML
    private JFXTextField passwordTF;

    @FXML
    private JFXTextField usernameTF;

    @FXML
    private JFXTextField fullnameTF;

    @FXML
    private Label label;

    private Parent root;
    private Stage stage;
    private Scene scene;
    private DataBase db = new DataBase();
    private Connection con = db.getConnection();
    private int resultSet;
    private ResultSet resultSet1;

    public void initialize() {
        backupChoiceBox.getItems().addAll("Enter Your Date OF Birth", "Enter Your Favorite Number",
                "Enter Your Primary School Name");
        backupChoiceBox.setValue("Enter Your Date OF Birth");
        System.out.println(resultSet);
    }

    @FXML
    void onClearHandler(ActionEvent event) throws Exception {

        try {
            root = (Parent) FXMLLoader.load(getClass().getResource("LoginView.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            usernameTF.clear();
            passwordTF.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onCreateHandler(ActionEvent event) throws Exception {

        String selectedText = backupChoiceBox.getValue().toString();
        try {

            label.setVisible(false);

            if (usernameTF.getText().isEmpty() || passwordTF.getText().isEmpty() || selectedText.isEmpty()
                    || fullnameTF.getText().isEmpty() || answerTF.getText().isEmpty()
                    || confirmTF.getText().isEmpty()) {
                showAlert("Error", "Please fill in all text fields!");
                throw new Exception("Custom Exception");

            }
            String password = passwordTF.getText();
            String confirmPassword = confirmTF.getText();

            if (password.isEmpty() || confirmPassword.isEmpty()) {
                showAlert("Error", "Please fill in both password fields.");
                throw new Exception("Custom Exception");

            } else if (!password.equals(confirmPassword)) {
                showAlert("Error", "Passwords do not match.");
                throw new Exception("Custom Exception");
            }

            boolean check = checkPasswordLength(passwordTF.getText());
            if (!check) {
                showAlert("Error", "Password Length Must be Greater Than Or Equal To 6 Digits");
                throw new Exception("Custom Exception");
            }

            resultSet = con.createStatement().executeUpdate(
                    "insert into Users (Username,Password,Securityquestion,SecurityAnswer,FullName) values ('" +
                            usernameTF.getText() + "' ,'" +
                            passwordTF.getText() + "','" + selectedText + "','" + answerTF.getText() + "','"
                            + fullnameTF.getText() + "');");

            label.setText("Account Created Successfully");
            label.setVisible(true);
            fullnameTF.clear();
            answerTF.clear();
            usernameTF.clear();
            passwordTF.clear();
            confirmTF.clear();

        } catch (Exception e) {

            resultSet1 = con.createStatement()
                    .executeQuery(
                            "select UserName , UserId from Users  where UserName = '" + usernameTF.getText() + "';");

            while (resultSet1.next()) {
                String usreName = resultSet1.getString("UserName");
                if (usreName.equals(usernameTF.getText())) {

                    showAlert("Error", "UserName Already Taken Please Choose Another User Name");
                }

            }
            label.setText("Account Not Created");
            label.setVisible(true);

        }

    }

    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static boolean checkPasswordLength(String password) {
        if (password.length() >= 6) {
            return true;
        } else {
            return false;
        }
    }
}
