/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author lap store
 */
public class FileReader {
      public ArrayList<String> readFile(String filename) throws FileNotFoundException{
        ArrayList<String> code = new ArrayList<String>();
        File file = new File(filename);
         String path = file.getAbsolutePath();
        Scanner scanFile = new Scanner(file);
        //till end of file
        while(scanFile.hasNextLine()) 
        {
            String newLine=scanFile.nextLine();
            code.add(newLine); //add each line of code to arraylist    
            }

        return code;
    }
      public void FileWriter(String label, String instruction,String Operand, String LOCCTRm){
      
      
      }
    
}
