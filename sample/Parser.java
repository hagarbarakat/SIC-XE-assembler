/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample;

import java.awt.BorderLayout;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author lap store
 */
public class Parser {

    ArrayList<CodeLine> lines;
    String programName;
    String error;
    Operations operations;
    Registers registers;
    private int counter = 1;
    private static HashMap<String, Integer> symbolTable = new HashMap<String, Integer>();
    String[] tokens;
    private static ArrayList<String> InterOutput = new ArrayList<>();
    private HashMap<String,String> Labels= new HashMap<>();

    public static ArrayList<String> getInterOutput() {
        return InterOutput;
    }

    public static void setInterOutput(ArrayList<String> interOutput) {
        InterOutput = interOutput;
    }

    public static HashMap<String, Integer> getSymbolTable() {
        return symbolTable;
    }

    public static void setSymbolTable(HashMap<String, Integer> symbolTable) {
        Parser.symbolTable = symbolTable;
    }

    public void run(ArrayList<String> program, int x) throws IOException // arraylist filled from reading file
    {

        symbolTable.clear();
        System.out.println("number" + x);
        System.out.println(program);
        int pc = 0;// LOCCTR
        String instruction = null;
        String label = null;
        String operand = null;
        programName = "";
        lines = new ArrayList<CodeLine>();
        operations = new Operations();

        for (int i = 0; i < program.size(); i++) {
            operand="";
            instruction="";
            label="";
            System.out.println(program.get(i)+"\n");
            // check if it's a comment which starts with "."
            if (program.get(i).charAt(0) == '.') {
                String Comment = program.get(i);
                CodeLine line = new CodeLine(instruction, operand, label, pc, true);
                line.setComment(Comment);
                lines.add(line);
                continue;
            }
            if (x == 1) {
                tokens = program.get(i).split("\\s+");//split by any number of spaces
                if(tokens.length==4){
                     if(tokens[0].matches("^\\s*$")){
                    System.out.println("fourrrrr");
                    label = tokens[1];
                    instruction = tokens[2];
                    operand = tokens[3];}
                }
                else if (tokens.length == 3 ) {
                    System.out.println("threeeeeeeeeeeeeeeeeeeeeee");
                    label = tokens[0];
                    instruction = tokens[1];
                    operand = tokens[2];
                } else if (tokens.length == 2) {
                    if(tokens[0].equals("\\S+")){
                        label = "";
                        instruction = tokens[1];
                        operand ="";
                    }
                    else{
                    System.out.println("twooooooooooooooooooooooooooo");
                    label = "";
                    instruction = tokens[1];
                    operand ="";}

                }
                else if (tokens.length==1){
                    label="";
                    instruction=tokens[0];
                    operand="";
                }
                else {

                    System.out.println("ay haga");
                    label = tokens[0];
                    instruction = tokens[1];
                    String operand1 = tokens[2];
                    String operand2 = tokens[3];
                    operand2 = operand2.trim();
                    operand = operand1 + operand2;
                }

                label = label.trim();
                System.out.println("label: " + label);
                if (operations.operations.containsKey(label)) {
                    CodeLine line = new CodeLine();
                    //Error 1
                    error = "Error,Using the mnemonics as labels is not allowed.";// not allowed 
                    line.setErrorMessage(error);
                }
                instruction = instruction.trim();
                System.out.println("instruction: " + instruction);
                operand = operand.trim();
                System.out.println("operand: " + operand);
            } else if (x == 0) //for fixed format
            {
                //to extract label from  bit 1 to 8
                if (program.get(i).length() > 8) {
                    label = program.get(i).substring(0, 7); // from 0 to 7
                } else {
                    label = program.get(i).substring(0, program.get(i).length());
                }
                //bit 9 blank
                //to extract instruction from bit 10 to 15
                if (program.get(i).length() > 8) {
                    if (program.get(i).length() > 15) {
                        instruction = program.get(i).substring(9, 14); // from 10 to 15
                    } else {
                        instruction = program.get(i).substring(9, program.get(i).length());
                    }
                }
                // bits 16 and 17 blank
                // to extract operand from bit 18 to 35
                if (program.get(i).length() > 15) {
                    if (program.get(i).length() > 35) {
                        operand = program.get(i).substring(14, 34); // from 18 to 35
                        System.out.println("runaua " + operand);
                    } else {
                        operand = program.get(i).substring(17, program.get(i).length());
                        System.out.println("runnn " + operand);
                    }
                }
                label = label.trim();
                instruction = instruction.trim();
                operand = operand.trim();
                System.out.println(operand);
                if (operand.contains(" ")) {
                    String[] Operand = operand.split("\\s+");
                    String Operand1 = Operand[0];
                    String Operand2 = Operand[1];
                    Operand1 = Operand1.trim();
                    Operand2 = Operand2.trim();
                    System.out.println("Operan111" + Operand1 + "s");
                    System.out.println("Operan22" + Operand2 + "s");
                    operand = Operand1 + Operand2;
                }

            }
            //start directive
            if (instruction.compareToIgnoreCase("start") == 0)// program can be written using either upper case or lower case letters.
            {
                try {
                    pc = Integer.parseInt(operand, 16);
                } catch (Exception e) {
                    error = "Error, not a hexadecimal string!";
                    CodeLine line = new CodeLine();
                    line.setErrorMessage(error);
                    lines.add(line);
                }
                System.out.println("LOCCTR= 0x" + operand);
                programName = label;
                symbolTable.put(programName, pc);
                System.out.println("Program name: " + label);
                CodeLine line = new CodeLine(instruction, operand, label, pc, false);
                lines.add(line);

                //handling error if no program name is given (1)
//                if (programName.isEmpty()) {
//                    System.out.println("Error, Program name is required.");
//                    error = "Program name is required.";
//                    line.setErrorMessage(error);
//                }
                continue;
            } //end directive
            else if (instruction.compareToIgnoreCase("end") == 0)// program can be written using either upper case or lower case letters.
            {
                if (!label.isEmpty()) {
                    error = "Error, END directive must have blank label";
                    CodeLine line = new CodeLine(instruction, operand, label, pc, false);
                    line.setErrorMessage(error);
                }
                String hex = Integer.toHexString(pc).toUpperCase();
                System.out.println("LOCCTR= 0x" + hex);
                System.out.println("ENDDDDD");
                System.out.println("Operand " + operand);
                lines.add(new CodeLine(instruction, operand, label, pc, false));
                continue;
            }
            //equ directive
            else if (instruction.compareToIgnoreCase("equ") == 0) {
                if (label.isEmpty()) {
                    error = "Error, EQU directive must have label";
                    CodeLine line = new CodeLine(instruction, operand, label, pc, false);
                    line.setErrorMessage(error);
                }
                try{
                    int address=  Integer.parseInt(operand);
                    symbolTable.put(label,address);

                }catch (NumberFormatException e){
                   Labels.put(operand,label);

                }

            lines.add(new CodeLine(instruction, operand, label, pc, false));
        }
            //org directive
            else if (instruction.compareToIgnoreCase("org") == 0) {
                if(!label.isEmpty()){
                    error="Error, ORG directive must have blank label";
                    CodeLine line = new CodeLine(instruction, operand, label, pc, false);
                    line.setErrorMessage(error);
                }

                pc = Integer.parseInt(operand);
                String hex = Integer.toHexString(pc).toUpperCase();
                pc= Integer.parseInt(hex);
                System.out.println("PC"+pc);
                lines.add(new CodeLine(instruction, operand, label, pc, false));
                continue;
            } else if (instruction.compareToIgnoreCase("base") == 0)// program can be written using either upper case or lower case letters.
            {
                if(!label.isEmpty()){
                    error="Error, BASE directive must have blank label";
                    CodeLine line = new CodeLine(instruction, operand, label, pc, false);
                    line.setErrorMessage(error);
                }
                String hex = Integer.toHexString(pc).toUpperCase();
                System.out.println("Base LOCCTR= 0x" + hex);
                lines.add(new CodeLine(instruction, operand, label, pc, false));
                continue;
            } else if (instruction.compareToIgnoreCase("resb") == 0)// program can be written using either upper case or lower case letters.
            {
                lines.add(new CodeLine(instruction, operand, label, pc, false));

                if (Validate(label)) {
                    symbolTable.put(label, pc);
                }
                pc = pc + Integer.parseInt(operand);
                String hex = Integer.toHexString(pc).toUpperCase();
                System.out.println("LOCCTR= 0x" + hex);

                continue;
            } else if (instruction.compareToIgnoreCase("resw") == 0)// program can be written using either upper case or lower case letters.
            {
                lines.add(new CodeLine(instruction, operand, label, pc, false));
                if (Validate(label)) {
                    symbolTable.put(label, pc);
                }
                pc = pc + 3 * Integer.parseInt(operand);
                String hex = Integer.toHexString(pc).toUpperCase();
                System.out.println("LOCCTR= 0x" + hex);

                continue;
            } else if (instruction.compareToIgnoreCase("byte") == 0)// program can be written using either upper case or lower case letters.
            {
                lines.add(new CodeLine(instruction, operand, label, pc, true));

                if (Validate(label)) {
                    symbolTable.put(label, pc);
                }
                if (operand.contains("c'") || operand.contains("C'")) //operand contains characters
                {
                    pc = pc + operand.length() - 3;
                    String hex = Integer.toHexString(pc).toUpperCase();
                    System.out.println("LOCCTR= 0x" + hex);
                } else {
                    pc = pc + (operand.length() - 3) / 2;
                    String hex = Integer.toHexString(pc).toUpperCase();
                    System.out.println("LOCCTR= 0x" + hex);

                }
                continue;
            } else if (instruction.compareToIgnoreCase("word") == 0)// program can be written using either upper case or lower case letters.
            {
                lines.add(new CodeLine(instruction, operand, label, pc, true));

                pc = pc + 3;
                String hex = Integer.toHexString(pc).toUpperCase();
                System.out.println("LOCCTR= 0x" + hex);
                System.out.println(label);
                if (Validate(label)) {
                    symbolTable.put(label, pc);
                }

                continue;
            } else {
                if (!label.equalsIgnoreCase("")) {
                    symbolTable.put(label, pc);
                    String hex = Integer.toHexString(pc);
                }
                lines.add(new CodeLine(instruction, operand, label, pc, true));
                if (RegisterInstructions(instruction, operand)) {
//                    CodeLine line = new CodeLine();
//                    //Error 2
//                    error = "Error, undefined symbol in operand!";
//                    line.setErrorMessage(error);
//                    lines.add(line);
                }
                if (instruction.startsWith("+")) {
                    String inst = instruction.substring(1);

                    if (operations.operations.containsKey(inst)) {
                        if (operations.operations.get(inst).getFormat() > 2) {
                            pc = pc + 4;
                            String hex = Integer.toHexString(pc).toUpperCase();
                            System.out.println("LOCCTR= 0x" + hex);
                        } else {

                            CodeLine line = new CodeLine();
                            //Error 5
                            error = "Error, can’t be format 4 instruction!";
                            line.setErrorMessage(error);
                            lines.add(line);

                        }
                    }
                    continue;
                }
                if (operations.operations.containsKey(instruction)) {

                    pc = pc + operations.operations.get(instruction).getFormat();
                    String hex = Integer.toHexString(pc).toUpperCase();
                    System.out.println("LOCCTR= 0x" + hex);
                    continue;

                } else {
                    CodeLine line = new CodeLine();
                    //Error 3
                    error = "Error,unrecognized operation code!";
                    line.setErrorMessage(error);
                    lines.add(line);
                }
                if (ValidateLabel(label, instruction, operand)) {
                }
            }

        }
        Map<String, Integer> map = symbolTable;
        Map<String, String> equate= Labels;
        System.out.println("               Symbol Table");
        for (String key : map.keySet()) {
            for(String value: equate.keySet()){
                if(value.equalsIgnoreCase(key)){
                    System.out.println(true);
                    symbolTable.put(equate.get(value), map.get(key));
                    System.out.println("keyYYYYYYYYYYYYYYYY : " + equate.get(value));
                    String hex = "0x" + Integer.toHexString(map.get(key)).toUpperCase();
                    System.out.println("value : " + hex);}
                }


        }
//        if (!program.get(program.size() - 1).toLowerCase().contains("end")) //missing end statement at the end of program
//        {
//            CodeLine line = new CodeLine();
//            //Error 3
//            error = "Error! missing END statement";
//            line.setErrorMessage(error);
//            lines.add(line);
//        }
        System.out.println(symbolTable.size());
        symbolTableFile(symbolTable);
        IntermediateFile(lines);
    }

    public boolean Validate(String label) {
        if (symbolTable.containsKey(label)) //checks if label is already defined
        {
            System.out.println(label + " Already exits");
            CodeLine line = new CodeLine();
            //Error 4
            error = "Error, " + label + " already exists!";
            line.setErrorMessage(error);
            lines.add(line);

            return false;
        } else {
            System.out.println(label + " added successfuly");
            return true;
        }

    }

    public boolean ValidateLabel(String label, String instruction, String operand) {

        if (label.matches("^\\s*$") && !label.equals("")) {
            error = "Error, misplaced label!";
            System.out.println(label + "here");

            System.out.println("Error, misplaced label");
            CodeLine line = new CodeLine();
            line.setErrorMessage(error);
            lines.add(line);
            return false;
        }
        if (instruction.matches("^\\s*$")) {
            error = "Error, missing or misplaced operation mnemonic!";
            System.out.println("Error, misplaced Instruction");
            CodeLine line = new CodeLine();
            line.setErrorMessage(error);
            lines.add(line);
            return false;
        }
        if (operand.matches("^\\s*$")) {
            error = "Error, missing or misplaced operand field!";
            System.out.println("Error, misplaced Operand");
            CodeLine line = new CodeLine();
            line.setErrorMessage(error);
            lines.add(line);
            return false;
        }

        return true;
    }

    public void symbolTableFile(HashMap<String, Integer> symbolTable) throws IOException {

        FileWriter fw = new FileWriter("src/output/SymbolTable.txt");
        Map<String, Integer> map = symbolTable;
        System.out.println("               Symbol Table");
        for (String key : map.keySet()) {
            System.out.println("key : " + key);
            fw.write(key);
            fw.write(" ");
            String hex = "0x" + Integer.toHexString(map.get(key)).toUpperCase();
            System.out.println("value : " + hex);
            fw.write(hex);
            fw.write("\n");
        }
        fw.close();

    }

    public void IntermediateFile(ArrayList<CodeLine> lines) throws IOException {
        //String.format to adjust spaces
        InterOutput.clear();
        FileWriter fw = new FileWriter("src/output/IntermediateOutputFile.txt");
        String LineNumber = null;
        LineNumber = String.format("%1$-15s", "LineNumber");
        String Address = String.format("%1$10s", "Address");
        String Label = String.format("%1$11s", "Label");
        String Instruction = String.format("%1$16s", "Mnemonic");
        String Operand = String.format("%1$15s", "Operands");
        InterOutput.add(LineNumber);
        InterOutput.add(Address);
        InterOutput.add(Label);
        InterOutput.add(Instruction);
        InterOutput.add(Operand);
        InterOutput.add("\n\n");
        fw.write(LineNumber);
        fw.write(Address);
        fw.write(Label);
        fw.write(Instruction);
        fw.write(Operand);
        fw.write("\n\n");
        for (CodeLine line : lines) {
            if (line.getComment() != null && !line.getComment().isEmpty()) {
                LineNumber = String.format("%1$-15s", counter);
                String hex = "0x" + Integer.toHexString(line.getPC()).toUpperCase();
                Address = String.format("%1$10s", hex);
                String Comment = String.format("%1$35s", line.getComment());
                System.out.println(Comment);
                InterOutput.add(LineNumber);
                InterOutput.add(Address);
                InterOutput.add(Comment);
                InterOutput.add("\n");
                fw.write(LineNumber);
                fw.write(Address);
                fw.write(Comment);
                fw.write("\n");
                counter++;
            } else if (line.getErrorMessage() != null && !line.getErrorMessage().isEmpty()) {
                String Error = String.format("%1$50s", line.getErrorMessage());
                System.out.println(Error);
                InterOutput.add(Error);
                InterOutput.add("\n");
                fw.write(Error);
                fw.write("\n");
            } else {
                LineNumber = String.format("%1$-15s", counter);
                String hex = "0x" + Integer.toHexString(line.getPC()).toUpperCase();
                Address = String.format("%1$10s", hex);
                Label = String.format("%1$11s", line.getLabel());
                Instruction = String.format("%1$16s", line.getInstruction());
                Operand = String.format("%1$15s", line.getOperand());
                System.out.println(line.getOperand());
                InterOutput.add(LineNumber);
                InterOutput.add(Address);
                InterOutput.add(Label);
                InterOutput.add(Instruction);
                InterOutput.add(Operand);
                InterOutput.add("\n");
                fw.write(LineNumber);
                fw.write(Address);
                fw.write(Label);
                fw.write(Instruction);
                fw.write(Operand);

                fw.write("\n");
                counter++;
                System.out.println(counter);
            }

        }
        fw.close();
    }

    public boolean RegisterInstructions(String instruction, String operand) {
        registers = new Registers();
        if (instruction.equalsIgnoreCase("ADDR") || instruction.equalsIgnoreCase("COMPR") || instruction.equalsIgnoreCase("RMO") || instruction.equalsIgnoreCase("SUBR")) {
            String[] Operands = operand.split(",");
            System.out.println("Commaaaa: " + Operands[0]);
            Operands[0] = Operands[0].trim();
            Operands[1] = Operands[1].trim();
            System.out.println(Operands[1]);
            if (!registers.Registers.containsKey(Operands[0].toUpperCase()) || !registers.Registers.containsKey(Operands[1].toUpperCase())) {
                String error = "Error, Illegal address for a register";
                CodeLine line = new CodeLine();
                line.setErrorMessage(error);
                lines.add(line);
                return false;
            }

        }

        if (instruction.equalsIgnoreCase("TIXR")) {
            if (!registers.Registers.containsKey(operand)) {
                String error = "Error, Illegal address for a register!";
                if (operand.equals("\\s+")) {
                    error = "Syntax Error!";
                }
                CodeLine line = new CodeLine();
                line.setErrorMessage(error);
                lines.add(line);
                return false;
            }
        }
        return true;
    }

}
