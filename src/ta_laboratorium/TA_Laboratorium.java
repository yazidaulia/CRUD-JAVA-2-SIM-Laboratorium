/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ta_laboratorium;
import javax.swing.UIManager;
/**
 *
 * @author yazidaulia */
  
  public class TA_Laboratorium {

    /**
     * @param args the command line arguments
     */
      
    private static String kd;
    
    public static void setUserLogin(String kode){
        kd = kode;
    }
    
    public static String getUserLogin(){
        return kd;
    } 
      
    public static void main(String[] args) {
        // TODO code application logic here
        
        koneksi konek = new koneksi();
        konek.connect();
        try 
        {
            //UIManager.setLookAndFeel("de.javasoft.plaf.synthetica.SyntheticaPlainLookAndFeel");
        } 
        catch (Exception e) 
        {
          e.printStackTrace();
        }
        
        new Loginn().setVisible(true);
    }
    
}