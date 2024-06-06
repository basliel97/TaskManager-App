import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.format.TextStyle;
import java.util.Locale;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.sql.ResultSet;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MyDayViewController {

    @FXML
    private JFXButton addBTN;

    @FXML
    private TableColumn<ProjectModel, String> catagoryCol;

    @FXML
    private TableView<ProjectModel> catagoryList;

    private ObservableList<ProjectModel> projects = FXCollections.observableArrayList();
    private ObservableList<TaskModel> tasks = FXCollections.observableArrayList();

    @FXML
    private Label dateLabel;

    @FXML
    private Label dayLabel;

    @FXML
    private Label greetings;

    @FXML
    private Label monthLabel;

    @FXML
    private JFXButton notificationBTN;

    @FXML
    private JFXButton searchBTN;

    @FXML
    private JFXTextField searchTF;

    @FXML
    private TableColumn<TaskModel, String> tasksCol;

    @FXML
    private TableView<TaskModel> tasksTable;

    @FXML
    private Label projectLabel;

    @FXML
    private VBox vBox;

    @FXML
    private VBox vbox1;

    @FXML
    private JFXButton deleteBTN;

    @FXML
    private JFXButton editBTN1;

    private Parent root;
    private Stage stage;
    private Scene scene;
    private DataBase db = new DataBase();
    private Connection con = db.getConnection();
    private int resultSet;
    private ResultSet resultSet1;

    String projectName;

    @FXML
    private JFXTextField projectNameTF;

    @FXML
    void onCreateHandler(ActionEvent event) throws SQLException {

        try {
            resultSet = con.createStatement()
                    .executeUpdate("insert into project (ProjectName) values ('" + projectNameTF.getText() + "');");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("project Added Correctly");
            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void onNotificationHandler(ActionEvent event) {

    }

    @FXML
    void onSearchHandler(ActionEvent event) throws SQLException {
        if (!searchTF.getText().isEmpty()) {
            resultSet1 = con.createStatement()
                    .executeQuery("SELECT * FROM Project WHERE ProjectName = '" + searchTF.getText() + "' ;");

            while (resultSet1.next()) {
                catagoryList.getItems().clear();
                projects.add(new ProjectModel(resultSet1.getString("ProjectName"), currentDayAsInt));
            }
            catagoryCol.setCellValueFactory(new PropertyValueFactory<ProjectModel, String>("ProjectName"));
            catagoryList.setItems(projects);
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Field Empty");
            alert.setContentText("Please Enter name first and search!");
            alert.showAndWait();
        }

    }

    LocalDate today = LocalDate.now();
    int currentDayAsInt = today.getDayOfMonth();
    String currentDayAsString = Integer.toString(currentDayAsInt);

    // Get the month in String format - Full name and abbreviated name examples
    String currentMonthAsString = today.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
    String currentMonthAsShortString = today.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault());
    DayOfWeek dayOfWeek = today.getDayOfWeek();

    // Get the name of the day in full form (e.g., Monday, Tuesday, etc.)
    String dayNameFull = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault());

    // Get the name of the day in short form (e.g., Mon, Tue, etc.)
    String dayNameShort = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault());

    @FXML
    void onAddHandler(ActionEvent event) {

    }

    public void initialize() {

        greetings.setText("Hello,  " + LoginController.getUsername());
        dayLabel.setText(dayNameShort);
        dateLabel.setText(Integer.toString(currentDayAsInt));
        monthLabel.setText(currentMonthAsString);
        try {
            catagoryCol.setCellValueFactory(new PropertyValueFactory<ProjectModel, String>("ProjectName"));
            catagoryList.setItems(projects);
            getProjects();
        } catch (Exception e) {
            e.printStackTrace();
        }
        catagoryList.setOnMouseClicked(event -> {

            if (event.getClickCount() == 2) {
                ProjectModel selectedTask = catagoryList.getSelectionModel().getSelectedItem();
                try {
                    resultSet1 = con.createStatement()
                            .executeQuery("select ProjectName from Project where projectName = '"
                                    + selectedTask.getProjectName() + "';");
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                try {
                    while (resultSet1.next()) {

                        projectName = resultSet1.getString("ProjectName");

                    }
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                vBox.setVisible(false);
                vbox1.setVisible(true);

                tasksCol.setCellValueFactory(new PropertyValueFactory<TaskModel, String>("TaskName"));
                tasksTable.setItems(tasks);
                getSpecificTasks(projectName);
            }
        });

    }

    int userId1;

    private void getProjects() {
        try {

            resultSet1 = con.createStatement()
                    .executeQuery("select userID from users where UserName = '" + LoginController.getUsername() + "';");
            while (resultSet1.next()) {

                userId1 = resultSet1.getInt("UserId");
            }
            catagoryList.getItems().clear();
            resultSet1 = con.createStatement().executeQuery("select * from Project where UserId = " + userId1 + ";");
            while (resultSet1.next()) {
                projects.add(new ProjectModel(resultSet1.getString("ProjectName"), currentDayAsInt));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getSpecificTasks(String ProjectName) {
        try {
            resultSet1 = con.createStatement()
                    .executeQuery("select * from Tasks where ProjectName = '" + ProjectName + "';");
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
    void onDeleteHandler(ActionEvent event) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Log OUT ?");
        alert.setHeaderText(null);
        alert.setContentText("Are You Sure You Want To Logout");
        alert.showAndWait();
        if (alert.getResult().getButtonData().isCancelButton()) {

        } else {

            String userId;
            String projectId;
            ProjectModel selectedProject = catagoryList.getSelectionModel().getSelectedItem();

            int taskId1 = selectedProject.projectId;

            String username = LoginController.getUsername();

            resultSet1 = con.createStatement()
                    .executeQuery("SELECT UserId FROM users WHERE username = '" + username + "' ;");

            while (resultSet1.next()) {

                userId = resultSet1.getString("UserId");

                resultSet1 = con.createStatement()
                        .executeQuery("SELECT ProjectName FROM Tasks WHERE userId = " + resultSet1.getString("UserId")
                                + " ;");
                while (resultSet1.next()) {

                    projectId = resultSet1.getString("ProjectName");

                }

                resultSet = con.createStatement().executeUpdate(
                        "delete FROM Tasks WHERE ProjectName = '" + selectedProject.getProjectName() + "' ;");
                resultSet = con.createStatement().executeUpdate(
                        "delete FROM Project WHERE ProjectName = '" + selectedProject.getProjectName() + "' ;");

                vbox1.setVisible(false);
                vBox.setVisible(true);
                catagoryList.getItems().clear();
            }

        }

    }

}
