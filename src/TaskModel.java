public class TaskModel {

    int TaskID;
    int UserId;
    String TaskName;
    String Description;
    String DueDate;
    String Priority;
    String Status;
    String ProjectName;

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    public TaskModel(int taskID, int userId, String taskName, String description, String dueDate, String priority,
            String status) {
        TaskID = taskID;
        UserId = userId;
        TaskName = taskName;
        Description = description;
        DueDate = dueDate;
        Priority = priority;
        Status = status;
    }

    public TaskModel(int taskID, int userId, String taskName, String description, String dueDate, String priority,
            String status, String projectName) {
        TaskID = taskID;
        UserId = userId;
        TaskName = taskName;
        Description = description;
        DueDate = dueDate;
        Priority = priority;
        Status = status;
        ProjectName = projectName;
    }

    public int getTaskID() {
        return TaskID;
    }

    public void setTaskID(int taskID) {
        TaskID = taskID;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getTaskName() {
        return TaskName;
    }

    public void setTaskName(String taskName) {
        TaskName = taskName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDueDate() {
        return DueDate;
    }

    public void setDueDate(String dueDate) {
        DueDate = dueDate;
    }

    public String getPriority() {
        return Priority;
    }

    public void setPriority(String priority) {
        Priority = priority;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
