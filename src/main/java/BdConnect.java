import java.sql.*;

public class BdConnect {
    static String firstName = "Иван";
    static String lastName = "Иванов";
    static String age = "25";
    static String phone = "79998887779";

    public static void main(String[] args) {
        BdConnect bdConnect = new BdConnect();
        bdConnect.connection();
        bdConnect.insert(firstName,  lastName, age, phone);
        bdConnect.selectMyUser(firstName, lastName, age, phone);
    }

    public Connection connection() { // подключаемся к бд
        Connection con = null;
        try {
            String url = "jdbc:mysql://db4free.net:3306/dexautomation";
            String username = "dexautomation";
            String password = "dexautomation";
            con = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println("Не удалось подключиться к бд");
            e.printStackTrace();
        }
        return con;
    }

    public void insert(String firstName, String lastName, String age, String phone) { // добавляем запись в таблицу
        String sql = "INSERT INTO Students(firstName, lastName, age, phone) VALUES(?,?,?,?)";
        try {
            Connection conn = this.connection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql); //подготовка запроса
            preparedStatement.setString(1, firstName); // устанавливаем первым значение ? для firstName
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, age);
            preparedStatement.setString(4, phone);
            try {
                preparedStatement.executeUpdate(); // обработка ошибки при дублировании записи
            } catch (SQLIntegrityConstraintViolationException e){
                e.printStackTrace();
                System.out.println("Такая запись уже существует!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void selectMyUser(String firstName, String lastName, String age, String phone){ // проверяем наличие записи в таблице
        String sql = "SELECT * FROM Students WHERE firstName = ? AND lastName = ? AND age = ? AND phone = ?";

        try {
            Connection conn = this.connection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql); // подготовка запроса
            preparedStatement.setString(1, firstName); // устанавливаем первым значение ? для firstName
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, age);
            preparedStatement.setString(4, phone);
            ResultSet rs = preparedStatement.executeQuery(); // выполение зароса
            int recordsAmount = 0;
            while (rs.next()) { // проходимся по результатам запроса
                String firstNameRes = rs.getString("firstName");
                String lastNameRes = rs.getString("lastName");
                String ageRes = rs.getString("age");
                String phoneRes = rs.getString("phone");
                if (firstNameRes.equals(firstName) && lastNameRes.equals(lastName) && ageRes.equals(age) && phoneRes.equals(phone)) { // проверяем количество соотв. записей
                   recordsAmount++;
                }
            }
            System.out.println("В таблице найдена " + recordsAmount + " запись" );
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

