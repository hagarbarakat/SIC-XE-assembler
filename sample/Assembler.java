/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author lap store
 */
public class Assembler {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        FileReader file= new FileReader();
       ArrayList <String>program = file.readFile("C:\\Users\\lap store\\Documents\\NetBeansProjects\\Assembler\\src\\assembler\\input.txt");
        Parser pass1=new Parser();
        //pass1.run(program);
    }
    
}
