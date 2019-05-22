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
public class Registers {
    HashMap<String, Register> Registers;
    public Registers() {
        Registers = new HashMap<>();
        Registers.put("A", new Register("A", "0"));
        Registers.put("X", new Register("X", "1"));
        Registers.put("L", new Register("L", "2"));
        Registers.put("B", new Register("B", "3"));
        Registers.put("S", new Register("S", "4"));
        Registers.put("T", new Register("T", "5"));
        Registers.put("F", new Register("F", "6"));
    }

    public HashMap<String, Register> getRegisters() {
        return Registers;
    }

    public boolean isRegister(String str) {
        return Registers.containsKey(str.toUpperCase());
    }


}
