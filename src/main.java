public class main {
    public static void main(String[] args){
        String  url = "jdbc:mysql://localhost:3306/?autoReconnect=true&useSSL=false";
        DatabaseConnection new_connection = new DatabaseConnection();
        new_connection.setConnection(url);
        new_connection.setCreationDatabase();

        new_connection.setTableDatabase();

        new_connection.displayDatabase();
        System.exit(0);





    }
}
