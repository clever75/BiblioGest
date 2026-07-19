 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

/**
 *
 * @author Admin
 */
import java.sql.Connection;
import java.sql.DriverManager;
public class Connexion {
    private static Connection conn=null;
    public static Connection getConnexion() throws Exception{
        try{
            String url="jdbc:mysql://localhost:3306/biblioapp";
            String user="root";
            String password="";
            
            Class.forName("com.mysql.jdbc.Driver");
            conn=DriverManager.getConnection(url,user,password);
            
            System.out.println("Bien réussie ");
        }catch(Exception e){
            System.out.println("Erreur connexion :"+ e.getMessage());
            
        }
        return conn;
    }
    
    public static void main(String[] args) throws Exception{
        
      Connexion.getConnexion();
      
    }
}
