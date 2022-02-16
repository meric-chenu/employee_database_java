import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class DatabaseConnection {
    private String  url;
    //We declare our variables at the beginning for question of visibility and access
    private int loop_counter = 0;
    private boolean keep_going = true;
    private boolean validation_statement = false;
    private boolean validation_connection = false;
    private String sql_statement;
    private JTextField database_name;
    private JPanel controls;
    private JPanel panel;
    private JPanel label;
    private JTextField username;
    private JPasswordField password;
    private Statement statement;
    private Connection connection;
    private ResultSet result_database;

    /**
     * This method allow us to do the connection to the database.
     * @param url database localhost
     */
    public void setConnection(String url){
        try {
            this.url = url;
            this.validation_statement = false;
            /*This do while block will handle the credentials of the user for the database connection. When there are
            incorrect, this loop will ask the user to enter again its credentials (by creating a graphic interface).*/
            do {
                this.panel = new JPanel(new BorderLayout(5, 5));

                /*GridLayout allow us to resizing the input in a rectangle
                    rows: number of rows allowing to display the input
                    cols : number of columns allowing to display the input
                 */
                this.label = new JPanel(new GridLayout(2, 1));

                this.label.add(new JLabel("database user : ", SwingConstants.RIGHT));
                this.label.add(new JLabel("database password : ", SwingConstants.RIGHT));

                //BorderLayout.WEST allows us to place the label text to the WEST side of the label input.
                this.panel.add(label, BorderLayout.WEST);

                this.controls = new JPanel(new GridLayout(0, 1));

                this.username = new JTextField();
                this.controls.add(this.username);
                this.password = new JPasswordField();
                this.controls.add(this.password);
                this.panel.add(this.controls, BorderLayout.CENTER);

                JOptionPane.showMessageDialog(null, this.panel, "Database connection", JOptionPane.PLAIN_MESSAGE);

                Class.forName("com.mysql.cj.jdbc.Driver");
                try{
                    /*The variable connection, as its name tells us, handle the database, by having the database localhost url,
                     * and the credentials (username and password, informed by the user in our case.*/
                    this.connection = DriverManager.getConnection(this.url,this.username.getText(),this.password.getText());
                    this.statement = connection.createStatement();
                    this.validation_connection = true;
                    JOptionPane.showMessageDialog(null,"Your are connected into the database.");

                }
                catch (SQLNonTransientConnectionException e){
                    JOptionPane.showMessageDialog(null,"Your credentials are incorrect. Please try again");
                }
                catch(SQLException e){
                    JOptionPane.showMessageDialog(null,"SQLException");
                }

            }while (!this.validation_connection);
        }
        catch(ClassNotFoundException e){
            JOptionPane.showMessageDialog(null,"There is an error with the Driver allowing the database connection.");
        }

    }

    /**
     * This method allows us to create a database, named by the user
     */
    public void setCreationDatabase(){

        /*This do while loop allows us to ask again the user if he enter a wrong database name, such as a name starting by a number*/
        do{
            this.panel = new JPanel(new BorderLayout(5,5));
            this.label = new JPanel(new GridLayout(1,1));
            this.label.add(new JLabel("database name : ",SwingConstants.RIGHT));
            this.panel.add(label,BorderLayout.WEST);
            this.controls = new JPanel(new GridLayout(0,1));
            this.database_name = new JTextField();
            this.controls.add(database_name);
            this.panel.add(controls,BorderLayout.CENTER);
            JOptionPane.showMessageDialog(new Frame(), panel,"Database creation",JOptionPane.PLAIN_MESSAGE);
            try{
                this.sql_statement = "DROP DATABASE IF EXISTS " + database_name.getText() + ";";
                this.statement.executeUpdate(this.sql_statement);
                this.sql_statement = "CREATE DATABASE " + database_name.getText() + ";";
                this.statement.executeUpdate(sql_statement);
                JOptionPane.showMessageDialog(null,"Database created succesfuly !");
                keep_going = false;
            }
            catch(SQLException e){
                JOptionPane.showMessageDialog(null,"There is an error in the SQL syntax. Please enter again the database name");
            }
        }while(this.keep_going);
        this.keep_going = true;
    }

    /**
     * This method allows us to create a table named Employee and adding data.
     */
    public void setTableDatabase(){
        try{
            this.sql_statement = "USE " + database_name.getText() + ";";
            this.statement.executeUpdate(this.sql_statement);
            this.sql_statement = "DROP TABLE IF EXISTS Employee;";
            this.statement.executeUpdate(this.sql_statement);
            this.sql_statement = "CREATE TABLE Employee(employee_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,name VARCHAR(50) NOT NULL,position VARCHAR(20) NOT NULL,hourly_pay_rate INT NOT NULL );";
            this.statement.executeUpdate(this.sql_statement);

            JOptionPane.showMessageDialog(null,"You are going to enter at least 5 individuals in the table Employee.");
            //This do while loop allows us to ask again the user to enter new individuals.
            do{
                /*As the project description tells us, we have to demonstrate that we can add at least 5 individuals in the database.
                 * So, when the 5th individuals is added in the database, we ask the user if he wants to keep adding individuals in the database, or
                 * if want to exit*/
                if(this.loop_counter >= 5){
                    String user_entry = JOptionPane.showInputDialog("Enter 1 to continue, or another number to quit : ");
                    int decision_user = Integer.parseInt(user_entry);
                    if(decision_user != 1){
                        this.keep_going = false;
                        break;
                    }
                }
                this.validation_statement = false;
                /*This do while loop allows us to ask again the user if he enter wrong data in the database, such as
                 * String in the field hourly_pay_rade, which expects an integer value.*/
                do{
                    this.panel = new JPanel(new BorderLayout(5,5));
                    this.label = new JPanel(new GridLayout(3,1));
                    this.label.add(new JLabel("Name : ",SwingConstants.RIGHT));
                    this.label.add(new JLabel("Position : ",SwingConstants.RIGHT));
                    this.label.add(new JLabel("hourly_pay_rade : ",SwingConstants.RIGHT));

                    this.panel.add(label,BorderLayout.WEST);
                    this.controls = new JPanel(new GridLayout(0,1));

                    JTextField name = new JTextField();
                    controls.add(name);
                    JTextField position = new JTextField();
                    controls.add(position);
                    JTextField hourly_pay_rate = new JTextField();
                    this.controls.add(hourly_pay_rate);
                    this.panel.add(controls,BorderLayout.CENTER);

                    JOptionPane.showMessageDialog(null, this.panel,"Informations individual number " + (this.loop_counter+1),JOptionPane.PLAIN_MESSAGE);

                    /*This try catch block handle SQL syntax error*/
                    try{
                        this.sql_statement = "INSERT INTO Employee" + " (name,position,hourly_pay_rate) VALUES('" + name.getText() + "','" + position.getText() + "'," + Integer.parseInt(hourly_pay_rate.getText()) + ");";
                        this.statement.executeUpdate(this.sql_statement);
                        this.validation_statement = true;
                    }
                    catch(NumberFormatException e){
                        JOptionPane.showMessageDialog(null,"You can't enter a String as hourly_pay_rate. Please enter again the informations.");
                    }
                }while(!this.validation_statement);

                JOptionPane.showMessageDialog(null,"The individual number " + (this.loop_counter + 1) + " has been added in the database.");
                this.loop_counter+=1;
            }while(this.keep_going || this.loop_counter < 5);
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null,"Error syntax SQL when adding data in the Employee table");
        }

    }

    public void displayDatabase(){
        //Before leaving the program, we're going to display the database
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
    }



}
