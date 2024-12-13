package vn.btec.campus_expense_management;

public class DataStatic {
    public static String email = "";
    public static String firstName = "";
    public static String lastName = "";
    public static String fullName = firstName + " " + lastName;
    public static int userId = -1;  // Changed to user_id as integer
    public static Boolean isLogin = false;

    public static void clearData() {
        email = "";
        firstName = "";
        lastName = "";
        fullName = "";
        userId = -1;  // Reset userId
        isLogin = false;
    }

    public static int getUserId(String email) {
        return userId;  // Adjusted method to return userId
    }
    public static void setUserId(int userId) {
        DataStatic.userId = userId;  // Adjusted method to set userId
    }
    public static String getEmail() {
        return email;
    }
    public static void setEmail(String email) {
        DataStatic.email = email;
    }
    public static String getFirstName() {
        return firstName;
    }
    public static void setFirstName(String firstName) {
        DataStatic.firstName = firstName;
    }
    public static String getLastName() {
        return lastName;
    }
    public static void setLastName(String lastName) {
        DataStatic.lastName = lastName;
    }

    public static Boolean getIsLogin() {
        return isLogin;
    }
    public static void setIsLogin(Boolean isLogin) {
        DataStatic.isLogin = isLogin;
    }

}
