/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample;

import java.util.HashMap;

/**
 *
 * @author lap store
 */
public class Operations {

    HashMap<String, InstructionDetails> operations;

    public Operations() {
        operations = new HashMap<String, InstructionDetails>();
        operations.put("RMO", new InstructionDetails("AC", 2));
        operations.put("ADD", new InstructionDetails("18", 3));
        operations.put("SUB", new InstructionDetails("1C", 3));
        operations.put("ADDR", new InstructionDetails("90", 2));
        operations.put("SUBR", new InstructionDetails("94", 2));
        operations.put("COMP", new InstructionDetails("28", 3));
        operations.put("COMPR", new InstructionDetails("A0", 2));
        operations.put("J", new InstructionDetails("3C", 3));
        operations.put("JEQ", new InstructionDetails("30", 3));
        operations.put("JLT", new InstructionDetails("38", 3));
        operations.put("JGT", new InstructionDetails("34", 3));
        operations.put("TIX", new InstructionDetails("2C", 3));
        operations.put("TIXR", new InstructionDetails("B8", 2));
        operations.put("LDB ",new InstructionDetails("68", 3));
        operations.put("LDCH", new InstructionDetails("50", 3));
        operations.put("STCH", new InstructionDetails("54", 3));
        operations.put("LDA", new InstructionDetails("00", 3));
        operations.put("LDS", new InstructionDetails("6C", 3));
        operations.put("LDT", new InstructionDetails("74", 3));
        operations.put("LDL", new InstructionDetails("08", 3));
        operations.put("LDX", new InstructionDetails("04", 3));
        operations.put("STA", new InstructionDetails("0C", 3));
        operations.put("STL", new InstructionDetails("14", 3));
        operations.put("STS", new InstructionDetails("7C", 3));
        operations.put("STX", new InstructionDetails("10", 3));
        operations.put("STT", new InstructionDetails("84", 3));
        operations.put("STB",  new InstructionDetails("78", 3));
        operations.put("MULR", new InstructionDetails("98",2));
        operations.put("DIVR", new InstructionDetails("9C",2));
        operations.put("MULF",new InstructionDetails("60", 3));
        operations.put("SUBF",new InstructionDetails("5C", 3));
        operations.put("OR",new InstructionDetails("44", 3));
        operations.put("SSK",new InstructionDetails("EC", 3));
        operations.put("STSW",new InstructionDetails("E8", 3));

    }
    public HashMap<String, InstructionDetails> getOperations() {
        return operations;
    }

}
