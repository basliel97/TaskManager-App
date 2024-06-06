import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.mysql.cj.protocol.Resultset;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.LocalTimeStringConverter;

import java.time.*;

public class AllTasksController {

    @FXML
    private JFXButton addBTN;

    @FXML
    private JFXButton cancelBTN1;

    @FXML
    private JFXButton addTaskBTN;

    @FXML
    private JFXTextArea descriptionTF1;

    @FXML
    private JFXTextField dueDateTF1;

    @FXML
    private JFXTextField nameTF1;

    @FXML
    private ChoiceBox<String> priorityTF1;

    @FXML
    private JFXButton saveBTN1;

    @FXML
    private ChoiceBox<String> statusTF1;

    @FXML
    private VBox vBox2;

    @FXML
    private JFXButton cancelBTN;

    @FXML
    private JFXButton deleteBTN;

    @FXML
    private Label descriptionLabel;

    @FXML
    private JFXTextArea descriptionTF;

    @FXML
    private Label dueDateLabel;

    @FXML
    private JFXTextField dueDateTF;

    @FXML
    private JFXButton editBTN;

    @FXML
    private HBox hbox;

    @FXML
    private JFXTextField nameTF;

    @FXML
    private Label priorityLabel;

    @FXML
    private ChoiceBox<String> priorityTF = new ChoiceBox<>();

    @FXML
    private Label statusLabel;

    @FXML
    private ChoiceBox<String> statusTF = new ChoiceBox<>();

    @FXML
    private Label taskNameLabel;

    @FXML
    private TableView<TaskModel> tasksTable;

    @FXML
    private JFXTextField projectNameTF;

    @FXML
    private JFXTextField projectNameTF1;

    @FXML
    private Label projectNameLabel;

    @FXML
    private TableColumn<TaskModel, String> tasksView;

    ObservableList<String> taskList = FXCollections.observableArrayList();
    private ObservableList<TaskModel> tasks = FXCollections.observableArrayList();

    @FXML
    private VBox vBox;

    @FXML
    private JFXButton searchBTN;

    @FXML
    private JFXTextField searchTF;

    @FXML
    void onSearchHandler(ActionEvent event) throws SQLException {
        if (!searchTF.getText().isEmpty()) {
            resultSet1 = con.createStatement()
                    .executeQuery("SELECT * FROM Tasks WHERE TaskName = '" + searchTF.getText() + "' ;");
            byte check = 0;
            while (resultSet1.next()) {
                tasksTable.getItems().clear();
                tasks.add(new TaskModel(
                        resultSet1.getInt("TaskID"),
                        resultSet1.getInt("UserId"),
                        resultSet1.getString("TaskName"),
                        resultSet1.getString("Description"),
                        resultSet1.getString("DueDate"),
                        resultSet1.getString("Priority"),
                        resultSet1.getString("Status"),
                        resultSet1.getString("ProjectName")));
                check++;
            }
            if (check == 0) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("No Result Found");
                alert.showAndWait();

            }

            tasksView.setCellValueFactory(new PropertyValueFactory<TaskModel, String>("TaskName"));
            tasksTable.setItems(tasks);
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Field Empty");
            alert.setContentText("Please Enter name first and search!");
            alert.showAndWait();
        }

    }

    @FXML
    private VBox vBox1;
    private DataBase db = new DataBase();
    private Connection con = db.getConnection();
    private int resultSet;
    private ResultSet resultSet1;
    String userId;
    String taskId;

    public void initialize() throws SQLException {

        statusTF.getItems().addAll("Pending", "In Progress", "Completed");
        priorityTF.getItems().addAll("Low", "Medium", "High");

        statusTF1.getItems().addAll("Pending", "In Progress", "Completed");
        priorityTF1.getItems().addAll("Low", "Medium", "High");

        try {
            tasksView.setCellValueFactory(new PropertyValueFactory<TaskModel, String>("TaskName"));
            tasksTable.setItems(tasks);
            getTasks();

        } catch (Exception e) {
            e.printStackTrace();
        }

        tasksTable.setOnMouseClicked(event -> {

            if (event.getClickCount() == 1) {
                TaskModel selectedTask = tasksTable.getSelectionModel().getSelectedItem();
                if (selectedTask != null) {
                    vBox1.setVisible(true);
                    projectNameLabel.setText("Project Name: " + selectedTask.getProjectName());
                    taskNameLabel.setText(selectedTask.getTaskName());
                    descriptionLabel.setText(String.valueOf(selectedTask.getDescription()));
                    priorityLabel.setText(selectedTask.getPriority());
                    dueDateLabel.setText(String.valueOf(selectedTask.getDueDate()));
                    statusLabel.setText((selectedTask.getStatus()));
                    getTasks();
                } else {

                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Table is empty");
                    alert.showAndWait();

                }
            }
        });

    }

    @FXML
    void onAddHandler(ActionEvent event) throws SQLException {

        try {
            String username = LoginController.getUsername();

            String priority = priorityTF.getValue().toString();
            String status = statusTF.getValue().toString();

            resultSet1 = con.createStatement()
                    .executeQuery("SELECT UserId FROM users WHERE username = '" + username + "' ;");

            while (resultSet1.next()) {

                userId = resultSet1.getString("UserId");

            }

            resultSet = con.createStatement().executeUpdate(
                    "INSERT INTO tasks (Userid, TaskName, Description, DueDate, Priority, Status, ProjectName)" +
                            " VALUES ('" + userId + "', '" + nameTF.getText() + "','" + descriptionTF.getText() + "','"
                            + dueDateTF.getText() + "','" + priority + "','" + status + "','" + projectNameTF.getText()
                            + "');");
            showAlert("Success", "Task Added SuccessFully");
            tasksTable.getItems().clear();
            getTasks();

        } catch (Exception e) {
            showAlert("Error", "Task Not Added");
        }
    }

    @FXML
    void onAddTaskHandler(ActionEvent event) {

        hbox.setVisible(false);
        vBox.setVisible(true);

    }

    @FXML
    void onCancelHandler(ActionEvent event) {

        hbox.setVisible(true);
        vBox.setVisible(false);

    }

    @FXML
    void onDeleteBTN(ActionEvent event) throws SQLException {

        TaskModel selectedTask = tasksTable.getSelectionModel().getSelectedItem();

        int taskId1 = selectedTask.getTaskID();

        String username = LoginController.getUsername();

        resultSet1 = con.createStatement()
                .executeQuery("SELECT UserId FROM users WHERE username = '" + username + "' ;");

        while (resultSet1.next()) {

            userId = resultSet1.getString("UserId");

        }

        resultSet1 = con.createStatement()
                .executeQuery("SELECT UserId,TaskId FROM Tasks WHERE userId = " + userId + " ;");
        while (resultSet1.next()) {

            taskId = resultSet1.getString("TaskId");

        }

        resultSet = con.createStatement().executeUpdate("delete FROM Tasks WHERE TaskId = " + taskId1 + " ;");

        tasksTable.getItems().clear();
        getTasks();

        vBox1.setVisible(false);

    }

    @FXML
    void onEditHandler(ActionEvent event) {

        hbox.setVisible(false);
        vBox2.setVisible(true);

        TaskModel selectedTask = tasksTable.getSelectionModel().getSelectedItem();
        nameTF1.setText(selectedTask.getTaskName());
        descriptionTF1.setText(String.valueOf(selectedTask.getDescription()));
        dueDateTF1.setText(String.valueOf(selectedTask.getDueDate()));

    }

    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    int userId1;

    private void getTasks() {
        try {
            resultSet1 = con.createStatement()
                    .executeQuery("select userID from users where UserName = '" + LoginController.getUsername() + "';");
            while (resultSet1.next()) {

                userId1 = resultSet1.getInt("UserId");
            }
            tasksTable.getItems().clear();
            resultSet1 = con.createStatement().executeQuery("select * from Tasks where UserId = " + userId1 + ";");
            while (resultSet1.next()) {
                tasks.add(new TaskModel(
                        resultSet1.getInt("TaskID"),
                        resultSet1.getInt("UserId"),
                        resultSet1.getString("TaskName"),
                        resultSet1.getString("Description"),
                        resultSet1.getString("DueDate"),
                        resultSet1.getString("Priority"),
                        resultSet1.getString("Status"),
                        resultSet1.getString("ProjectName")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onCancelHandler1(ActionEvent event) {

        hbox.setVisible(true);
        vBox.setVisible(false);
        vBox2.setVisible(false);
        vBox1.setVisible(false);

    }

    @FXML
    void onSaveHandler(ActionEvent event) {

        try {
            String username = LoginController.getUsername();

            String priority = priorityTF1.getValue().toString();
            String status = statusTF1.getValue().toString();

            resultSet1 = con.createStatement()
                    .executeQuery("SELECT UserId FROM users WHERE username = '" + username + "' ;");

            while (resultSet1.next()) {

                userId = resultSet1.getString("UserId");

            }

            resultSet1 = con.createStatement()
                    .executeQuery("SELECT UserId,TaskId FROM Tasks WHERE userId = '" + userId + "' ;");
            while (resultSet1.next()) {

                taskId = resultSet1.getString("TaskId");

            }

            resultSet = con.createStatement().executeUpdate("UPDATE Tasks  SET TaskName = '" + nameTF1.getText()
                    + "', Description = '" + descriptionTF1.getText() + "', DueDate = '" + dueDateTF1.getText() +
                    "',Priority = '" + priority + "',Status = '" + status + "',ProjectName = '"
                    + projectNameTF1.getText() + "' WHERE UserId = '" + userId + "' AND TaskId = '" + taskId + "';");
            showAlert("Success", "Task Updated SuccessFully");
        } catch (Exception e) {
            showAlert("Error", "Task Not Updated");
        }

    }

}
