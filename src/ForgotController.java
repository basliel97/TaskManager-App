import java.sql.Connection;
import java.sql.ResultSet;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.lang.String;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class ForgotController {

    @FXML
    private JFXTextField answerTF;

    @FXML
    private JFXButton backBTN;

    @FXML
    private ChoiceBox<String> backupChoiceBox = new ChoiceBox<>();

    @FXML
    private JFXTextField confirmTF;

    @FXML
    private JFXTextField passwordTF;

    @FXML
    private JFXButton resetBTN;

    @FXML
    private JFXTextField userNameTF;

    @FXML
    private Label label;

    private Parent root;
    private Stage stage;
    private Scene scene;
    private DataBase db = new DataBase();
    private Connection con = db.getConnection();
    private int resultSet;
    private ResultSet resultSet1;
    String securityQuestion;
    String securityAnswer;

    public void initialize() {
        backupChoiceBox.getItems().addAll("Enter Your Date OF Birth", "Enter Your Favorite Number",
                "Enter Your Primary School Name");
        System.out.println(resultSet);
    }

    @FXML
    void onBackHandler(ActionEvent event) {

        try {
            root = (Parent) FXMLLoader.load(getClass().getResource("LoginView.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void onResetHandler(ActionEvent event) {
        String selectedText = backupChoiceBox.getValue();
        if (userNameTF.getText().isEmpty() || passwordTF.getText().isEmpty() || (backupChoiceBox.getValue() == null)
                || answerTF.getText().isEmpty() || confirmTF.getText().isEmpty()) {

            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Pleasse Fill All Necessary Informations!");
            alert.showAndWait();

        } else {
            String password = passwordTF.getText();
            String confirmPassword = confirmTF.getText();
            if (password.equals(confirmPassword)) {
                try {

                    resultSet1 = con.createStatement()
                            .executeQuery("SELECT SecurityQuestion, SecurityAnswer FROM users WHERE username = '"
                                    + userNameTF.getText() + "' ;");

                    while (resultSet1.next()) {

                        securityQuestion = resultSet1.getString("SecurityQuestion");
                        securityAnswer = resultSet1.getString("SecurityAnswer");

                        System.out.println(securityAnswer);
                        System.out.println(securityQuestion);

                    }

                    if (securityQuestion.equals(selectedText)) {

                        if (securityAnswer.equals(answerTF.getText())) {

                            if (password.equals(confirmPassword)) {

                                boolean check = checkPasswordLength(password);
                                if (!check) {
                                    Alert alert = new Alert(AlertType.ERROR);
                                    alert.setTitle("Error");
                                    alert.setContentText("Password Length Must Be Greater Than Or Equal To 6 Digits");
                                    alert.showAndWait();
                                    throw new Exception("Custom Exception");
                                }

                                resultSet = con.createStatement()
                                        .executeUpdate("UPDATE Users  SET Password = '" + passwordTF.getText() +
                                                "' WHERE Username = '" + userNameTF.getText() + "' ;");

                                userNameTF.clear();
                                passwordTF.clear();
                                confirmTF.clear();
                            } else {
                                throw new Exception("Custom Exception");
                            }
                            label.setText("Reset Successfully");
                            label.setVisible(true);
                            answerTF.clear();
                            userNameTF.clear();
                            passwordTF.clear();
                            confirmTF.clear();

                        } else {

                            Alert alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setContentText("Your Answer Is Not Correct");
                            alert.showAndWait();

                        }
                    } else {

                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("Your Question Is Not Correct");
                        alert.showAndWait();

                    }

                } catch (Exception e) {

                    label.setText("Password Not Reseted");
                    label.setVisible(true);
                }

            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText(
                        "The new password and the confirmed password are not the same please check it again!");
                alert.showAndWait();
            }
        }

    }

    public static boolean checkPasswordLength(String password) {
        if (password.length() >= 6) {
            return true;
        } else {
            return false;
        }
    }
}
