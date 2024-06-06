
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginController {
    private static String username;

    public static String getUsername() {
        return username;
    }

    @FXML
    private Hyperlink createLink;

    @FXML
    private Hyperlink forgotLink;

    @FXML
    private JFXButton loginBTN;

    @FXML
    private JFXPasswordField passwordTF;

    @FXML
    private JFXTextField userNameTF;

    @FXML
    private Label errorLabel;

    @FXML
    private VBox label;

    private Parent root;
    private Stage stage;
    private Scene scene;
    private DataBase db = new DataBase();
    private Connection con = db.getConnection();
    private ResultSet resultSet;

    @FXML
    void onCreateHandler(ActionEvent event) throws Exception {

        try {
            root = (Parent) FXMLLoader.load(getClass().getResource("AccountCreateView.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            userNameTF.clear();
            passwordTF.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onForgotHandler(ActionEvent event) {

        try {
            root = (Parent) FXMLLoader.load(getClass().getResource("ForgotView.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            userNameTF.clear();
            passwordTF.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void onLoginHandler(ActionEvent event) throws Exception {

        try {

            resultSet = con.createStatement().executeQuery("select * from Users where Username = '" +
                    userNameTF.getText() + "' and Password = '" +
                    passwordTF.getText() + "'");
            while (resultSet.next()) {
                username = userNameTF.getText();
                root = (Parent) FXMLLoader.load(getClass().getResource("MainView.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            if (userNameTF.getText().isEmpty() || passwordTF.getText().isEmpty()) {
                showAlert("Error", "Please Provide Username And Password");
            } else {

                errorLabel.setVisible(true);
                userNameTF.clear();
                passwordTF.clear();
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
