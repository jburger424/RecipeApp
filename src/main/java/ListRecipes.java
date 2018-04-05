import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ListRecipes {


    public static ArrayList<String> printAllRecipeInfo(){
        Connection connection = null;
        String url = "jdbc:mysql://localhost:3306/";
        String dbName = "******";
        String driverName = "com.mysql.jdbc.Driver";
        String userName = "abarrett";
        String password = "abarrett1";
        ArrayList<String> rowArray = new ArrayList<String>();

        try{
            Class.forName(driverName).newInstance();
            connection = DriverManager.getConnection(url+dbName, userName, password);

            try{
                Statement stmt = connection.createStatement();
                String selectquery = "SELECT * FROM `thisTable` WHERE `uniqueID` = 12345";
                ResultSet rs = stmt.executeQuery(selectquery);


                while(rs.next()){
                    rowArray.add(rs.getString(1));
                    rowArray.add(rs.getString(2));
                    rowArray.add(rs.getString(3));

                    System.out.println(rowArray);
                }
            }
            catch(SQLException s){
                System.out.println(s);
            }
            connection.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return rowArray;
    }

    public static void main(String[] args) {
        printAllRecipeInfo();
    }
}
