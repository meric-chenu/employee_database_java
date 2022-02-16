import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class main {
    public static void main(String[] args) throws SQLException, SQLSyntaxErrorException{
        String  url = "jdbc:mysql://localhost:3306/?autoReconnect=true&useSSL=false";
        //We declare our variables at the beginning for question of visibility and access
        int loop_counter = 0;
        boolean keep_going = true;
        boolean validation_statement = false;
        String sql_first_statement;
        String sql_second_statement;
        String sql_use_database;
        String sql_drop_table_employee;
        String sql_create_table_employee;
        JTextField database_name;
        String sql_statement_adding;
        String sql_display_database;
        JPanel controls;
        JPanel panel;
        JPanel label;
        JTextField username;
        JPasswordField password;
        Statement statement;
        Connection connection;
        ResultSet result_database;

        /*Our script will be encapsulated in try catch blocks. The first will have for purpose to handle
        ClassNotFoundException exception, relative to the driver used for the database connection, which
        could not be included in the project, creating this exception.*/
        try{

            validation_statement = false;
            /*This do while block will handle the credentials of the user for the database connection. When there are
            incorrect, this loop will ask the user to enter again its credentials (by creating a graphic interface).*/
            do{
                panel = new JPanel(new BorderLayout(5,5));

                /*GridLayout allow us to resizing the input in a rectangle
                    rows: number of rows allowing to display the input
                    cols : number of columns allowing to display the input
                 */
                label = new JPanel(new GridLayout(2,1));

                label.add(new JLabel("database user : ",SwingConstants.RIGHT));
                label.add(new JLabel("database password : ",SwingConstants.RIGHT));

                //BorderLayout.WEST allows us to place the label text to the WEST side of the label input.
                panel.add(label,BorderLayout.WEST);

                controls = new JPanel(new GridLayout(0,1));

                username = new JTextField();
                controls.add(username);
                password = new JPasswordField();
                controls.add(password);
                panel.add(controls,BorderLayout.CENTER);

                JOptionPane.showMessageDialog(null, panel,"Database connection",JOptionPane.QUESTION_MESSAGE);

                Class.forName("com.mysql.cj.jdbc.Driver");

                /*This try catch block whill handle the SQLNonTransientConnectionException exception, displayed whenabb
                * the user's credentials for the database connection are incorrect*/
                try{

                    connection = DriverManager.getConnection(url,username.getText(),password.getText());
                    statement = connection.createStatement();
                    /*This do while loop */
                    do{
                        panel = new JPanel(new BorderLayout(5,5));
                        label = new JPanel(new GridLayout(1,1));
                        label.add(new JLabel("database name : ",SwingConstants.RIGHT));
                        panel.add(label,BorderLayout.WEST);
                        controls = new JPanel(new GridLayout(0,1));
                        database_name = new JTextField();
                        controls.add(database_name);
                        panel.add(controls,BorderLayout.CENTER);
                        JOptionPane.showMessageDialog(new Frame(), panel,"Database creation",JOptionPane.QUESTION_MESSAGE);
                        try{
                            sql_first_statement = "DROP DATABASE IF EXISTS " + database_name.getText() + ";";
                            statement.executeUpdate(sql_first_statement);
                            keep_going = false;

                        }
                        catch(SQLException e){
                            JOptionPane.showMessageDialog(null,"There is an error in the SQL syntax. Please enter again the database name");
                        }
                    }while(keep_going);
                    keep_going = true;
                    sql_second_statement = "CREATE DATABASE " + database_name.getText() + ";";
                    sql_use_database = "USE " + database_name.getText() + ";";
                    sql_drop_table_employee = "DROP TABLE IF EXISTS Employee;";
                    sql_create_table_employee = "CREATE TABLE Employee(employee_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,name VARCHAR(50) NOT NULL,position VARCHAR(20) NOT NULL,hourly_pay_rate INT NOT NULL );";


                    statement.executeUpdate(sql_second_statement);
                    statement.executeUpdate(sql_use_database);
                    statement.executeUpdate(sql_drop_table_employee);
                    statement.executeUpdate(sql_create_table_employee);

                    System.out.print("Database created succesfuly ! ");
                    JOptionPane.showMessageDialog(null,"Database created succesfuly !");
                    JOptionPane.showMessageDialog(null,"You are going to enter at least 5 individuals in the database.");
                    do{
                        if(loop_counter >= 5){
                            String user_entry = JOptionPane.showInputDialog("Enter 1 to continue, or another number to quit : ");
                            int decision_user = Integer.parseInt(user_entry);
                            if(decision_user != 1){
                                keep_going = false;
                                break;
                            }
                        }
                        validation_statement = false;
                        do{
                            panel = new JPanel(new BorderLayout(5,5));
                            label = new JPanel(new GridLayout(3,1));
                            label.add(new JLabel("Name : ",SwingConstants.RIGHT));
                            label.add(new JLabel("Position : ",SwingConstants.RIGHT));
                            label.add(new JLabel("hourly_pay_rade : ",SwingConstants.RIGHT));

                            panel.add(label,BorderLayout.WEST);
                            controls = new JPanel(new GridLayout(0,1));

                            JTextField name = new JTextField();
                            controls.add(name);
                            JTextField position = new JTextField();
                            controls.add(position);
                            JTextField hourly_pay_rate = new JTextField();
                            controls.add(hourly_pay_rate);
                            panel.add(controls,BorderLayout.CENTER);

                            JOptionPane.showMessageDialog(null, panel,"Informations individual number " + (loop_counter+1),JOptionPane.QUESTION_MESSAGE);
                            try{
                                sql_statement_adding = "INSERT INTO Employee" + " (name,position,hourly_pay_rate) VALUES('" + name.getText() + "','" + position.getText() + "'," + Integer.parseInt(hourly_pay_rate.getText()) + ");";
                                statement.executeUpdate(sql_statement_adding);
                                validation_statement = true;
                            }
                            catch(NumberFormatException e){
                                JOptionPane.showMessageDialog(null,"You can't enter a String as hourly_pay_rate. Please enter again the informations.");
                            }
                        }while(!validation_statement);
                        JOptionPane.showMessageDialog(null,"The individual number " + (loop_counter + 1) + " has been added in the database.");
                        loop_counter+=1;
                    }while(keep_going || loop_counter < 5);
                    /*Affichage de la base de données */;
                    JOptionPane.showMessageDialog(null,"Thank you !\nBefore leaving us, you can see the database's data in the console !");
                    try{
                        result_database = statement.executeQuery("SELECT * FROM Employee");
                        while (result_database.next()) {
                            System.out.println(result_database.getInt("employee_id") + " - " + result_database.getString("name") + "  " + result_database.getString("position") + "  " + result_database.getInt("hourly_pay_rate"));
                        }
                    }
                    catch(SQLException e){
                        System.out.println("Erreur sql");
                    }
                    System.exit(0);
                    validation_statement = true;
                }
                catch (SQLNonTransientConnectionException e){
                    JOptionPane.showMessageDialog(null,"Your credentials are incorrect. Please try again");
                }
            }while(!validation_statement);

        }
        catch(ClassNotFoundException e){
            System.out.print(e.getException());
        }
        catch(SQLException e){
            e.printStackTrace();
        }


    }
}
