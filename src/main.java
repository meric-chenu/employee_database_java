import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.Border;

public class main {
    public static void main(String[] args) throws SQLException{
        String  url = "jdbc:mysql://localhost:3306/?autoReconnect=true&useSSL=false";
        int loop_counter = 0;
        boolean keep_going = true;
        try{


            JPanel panel = new JPanel(new BorderLayout(5,5));

            /*GridLayout allow us to resizing the input in a rectangle
                rows: number of rows allowing to display the input
                cols : number of columns allowing to display the input
             */
            JPanel label = new JPanel(new GridLayout(2,1));

            label.add(new JLabel("database user : ",SwingConstants.RIGHT));
            label.add(new JLabel("database password : ",SwingConstants.RIGHT));

            //BorderLayout.WEST allows us to place the label text to the WEST side of the label input.
            panel.add(label,BorderLayout.WEST);

            JPanel controls = new JPanel(new GridLayout(0,1));

            JTextField username = new JTextField();
            controls.add(username);
            JPasswordField password = new JPasswordField();
            controls.add(password);
            panel.add(controls,BorderLayout.CENTER);

            JOptionPane.showMessageDialog(null, panel,"Database connection",JOptionPane.QUESTION_MESSAGE);

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(url,username.getText(),password.getText());
            Statement statement = connection.createStatement();

            JOptionPane.showMessageDialog(null,"You've succeed to connect to the database.");


            panel = new JPanel(new BorderLayout(5,5));
            label = new JPanel(new GridLayout(1,1));
            label.add(new JLabel("database name : ",SwingConstants.RIGHT));
            panel.add(label,BorderLayout.WEST);
            controls = new JPanel(new GridLayout(0,1));
            JTextField database_name = new JTextField();
            controls.add(database_name);
            panel.add(controls,BorderLayout.CENTER);
            JOptionPane.showMessageDialog(new Frame(), panel,"Database creation",JOptionPane.QUESTION_MESSAGE);

            String sql_first_statement = "DROP DATABASE IF EXISTS " + database_name.getText() + ";";
            String sql_second_statement = "CREATE DATABASE " + database_name.getText() + ";";
            String sql_use_database = "USE " + database_name.getText() + ";";
            String sql_drop_table_employee = "DROP TABLE IF EXISTS Employee;";
            String sql_create_table_employee = "CREATE TABLE Employee(employee_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,name VARCHAR(50) NOT NULL,position VARCHAR(20) NOT NULL,hourly_pay_rate INT NOT NULL );";



            statement.executeUpdate(sql_first_statement);
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

                JOptionPane.showMessageDialog(null, panel,"Informations individual number " + loop_counter+1,JOptionPane.QUESTION_MESSAGE);

                String sql_statement_adding = "INSERT INTO Employee" + " (name,position,hourly_pay_rate) VALUES('" + name.getText() + "','" + position.getText() + "'," + Integer.parseInt(hourly_pay_rate.getText()) + ");";
                System.out.println(sql_statement_adding);
                statement.executeUpdate(sql_statement_adding);
                JOptionPane.showMessageDialog(null,"The individual number " + (loop_counter + 1) + " has been added in the database.");
                loop_counter+=1;
            }while(keep_going || loop_counter < 5);
            System.exit(0);

        }
        catch(ClassNotFoundException e){
            System.out.print(e.getException());
        }
        catch(SQLException e){
            e.printStackTrace();
        }


    }
}
