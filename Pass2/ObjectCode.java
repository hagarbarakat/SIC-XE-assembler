
package Pass2;

import javafx.scene.control.Alert;
import sample.Operations;
import sample.Registers;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

public class ObjectCode {
    
    private static ArrayList<String> objectFile;
    private static ArrayList<String> listFile;
    //
    private Operations operations;
    private Registers registers;
    private SymbolTable table;
    //
    private String operand;     //for the rest of formats
    private String operands[];  //for format 2 and indexed addressing mode
    //
    private boolean baseAvailable = false;
    
    public ObjectCode(ArrayList<IntermediateFileLine> lines, SymbolTable table) {
        this.table = table;
        operations = new Operations();
        registers = new Registers();     
        objectFile = new ArrayList<>();
        listFile = new ArrayList<>();
        
        String startingAddress = null;
        String programName = null;
        
        for(IntermediateFileLine intermediateFileLine : lines) { 
            
            String operationMnemonic = intermediateFileLine.getMnemonic();
            String address = intermediateFileLine.getAddress().substring(2);
            String label = intermediateFileLine.getLabel();
            operand = intermediateFileLine.getOperand();
            
            if(lines.indexOf(intermediateFileLine) == 0) {             
                if(operationMnemonic.equalsIgnoreCase("START")) {
                    //Header Record
                    programName = intermediateFileLine.getLabel();
                    startingAddress = address;
                    
                    if(!isHex(operand)) { 
                        //error
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Invalid operand for START, starting from address 0 instead.");
                        alert.show();
                        System.out.println("Invalid operand for START, starting from address 0 instead.");
                        operand = "0";
                    }
                    objectFile.add("H" + programName + formatZeros(startingAddress, 6)); 
                    listFile.add(address + "\t\t" + ((label==null) ? "" : label) + "\t\t" + operationMnemonic + "\t\t" + startingAddress);       
                    
                    continue;
                }
            }
            
            if(operand != null) {
                if(operand.contains("+")) {
                    String parts[] = operand.split("\\+");
                    if(parts.length != 2 || operand.contains("#") || operand.contains("@")) {
                        //error
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Invalid operand format, instruction was ignored.");
                        alert.show();
                        System.out.println("Invalid operand format, instruction was ignored.");
                        continue;
                    }
                    else {
                        operand = Integer.toString(Integer.parseInt(parts[0], 16) + Integer.parseInt(parts[1], 16), 16);
                    }
                }
                else if(operand.contains("-")) {
                    String parts[] = operand.split("\\-");
                    if(parts.length != 2 || operand.contains("#") || operand.contains("@")) {
                        //error
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Invalid operand format, instruction was ignored.");
                        alert.show();
                        System.out.println("Invalid operand format, instruction was ignored.");
                        continue;
                    }
                    else {
                        operand = Integer.toString(Integer.parseInt(parts[0], 16) - Integer.parseInt(parts[1], 16), 16);
                    }
                }
                else if(operand.contains("*")) {
                    String parts[] = operand.split("\\*");
                    if(parts.length != 2 || operand.contains("#") || operand.contains("@")) {
                        //error
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Invalid operand format, instruction was ignored.");
                        alert.show();
                        System.out.println("Invalid operand format, instruction was ignored.");
                        continue;
                    }
                    else {
                        operand = Integer.toString(Integer.parseInt(parts[0], 16) * Integer.parseInt(parts[1], 16), 16);
                    }
                }
                else if(operand.contains("/")) {
                    String parts[] = operand.split("/");
                    
                    if(parts.length != 2 || operand.contains("#") || operand.contains("@")) {
                        //error
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Invalid operand format, instruction was ignored.");
                        alert.show();
                        System.out.println("Invalid operand format, instruction was ignored.");
                        continue;
                    }
                    else {
                        operand = Integer.toString(Integer.parseInt(parts[0], 16) / Integer.parseInt(parts[1], 16), 16);
                    }
                }
            }
            
            int format = 0;
                
            if(operationMnemonic.startsWith("+")){
                format = 4;
                operationMnemonic = operationMnemonic.substring(1);
                
                if(operations.getOperations().get(operationMnemonic) == null){
                    //error
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Invalid use of format 4, instruction was ignored.");
                    alert.show();
                    System.out.println("Invalid use of format 4, instruction was ignored");
                    continue;
                }
                
                if(operations.getOperations().get(operationMnemonic).getFormat() != 3) {
                    //error
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Invalid use of format 4, instruction was ignored");
                    alert.show();
                    System.out.println("Invalid use of format 4, instruction was ignored");
                    continue;
                }
            }
            
            if(operationMnemonic.equalsIgnoreCase("BYTE")) {
                    if(operand.startsWith("C'") && operand.endsWith("'")) {     //character string
                        operand = operand.substring(2, operand.length() - 1);
                        
                        operand = stringToHex(operand);
                        
                        String length = Integer.toHexString((int)(Math.ceil(operand.length() / 2.0)));
                        objectFile.add("T" + formatZeros(address,6) + formatZeros(length, 2) + formatZeros(operand, 60)); 
                    }
                    else if(operand.startsWith("X'") && operand.endsWith("'")) {    //hexadecimal constant
                        operand = operand.substring(2, operand.length() - 1);
                        //
                        if(isHex(operand)) {
                            String length = Integer.toHexString((int)(Math.ceil(operand.length() / 2.0)));
                            objectFile.add("T" + formatZeros(address,6) + formatZeros(length, 2) + formatZeros(operand, 60)); 
                        }
                        else {
                            //error
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("Operand is not a hexadecimal constant, instruction was ignored.");
                            alert.show();
                            System.out.println("Operand is not a hexadecimal constant, instruction was ignored.");
                        }
                    }
                    else {
                        //error
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Operand for END does not match the starting address of the program");
                        alert.show();
                        System.out.println("Unrecognised operand format, instruction was ignored.");
                    }
                continue;
            }
            else if(operationMnemonic.equalsIgnoreCase("WORD")) {   //Integer Constant
                if(operand.contains(",")) {
                    operands = operand.split(",");
                    
                    for(String op : operands) {
                        if(isInt(op)) {
                            op = Integer.toString(Integer.parseInt(op), 16);
                            String length = Integer.toHexString((int)(Math.ceil(op.length() / 2.0)));
                            objectFile.add("T" + formatZeros(address,6) + formatZeros(length, 2) + formatZeros(op, 60)); 
                        
                            listFile.add(address + "\t\t" + ((label==null) ? "" : label) + "\t\t" + operationMnemonic + "\t\t" + intermediateFileLine.getOperand() + "\t\t" + op);
                        }
                        else
                        {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("Operand is not an integer, instruction was ignored.");
                            alert.show();
                            System.out.println("Operand is not an integer, instruction was ignored");}
                    }
                        
                }
                else {
                    if(isInt(operand)) {
                        operand = Integer.toString(Integer.parseInt(operand), 16);
                        String length = Integer.toHexString((int)(Math.ceil(operand.length() / 2.0)));
                        objectFile.add("T" + formatZeros(address,6) + formatZeros(length, 2) + formatZeros(operand, 60)); 
                        
                        listFile.add(address + "\t\t" + ((label==null) ? "" : label) + "\t\t" + operationMnemonic + "\t\t" + intermediateFileLine.getOperand() + "\t\t" + operand);
                    }
                    else{
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Operand is not an integer, instruction was ignored.");
                        alert.show();
                    }
                }
                continue;
            }
            
            boolean indirect = false, immediate = false, simpleIndexed = false;
            
            if(operand != null) {
                
                if((operand.endsWith(",X") || operand.endsWith(",x")) && (format == 3 || format == 4)){     //simple indexed
                        operand = operand.substring(0, operand.length() - 2);
                        
                        if(!isHex(operand)) {
                            if(isValidLabel(operand)) 
                                operand = getLabelAddress(operand);
                            else{
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setContentText("Refrence to unkown label: '" + operand + "', instruction was ignored.");
                                alert.show();
                                System.out.println("Refrence to unkown label: '" + operand + "', instruction was ignored.");
                                continue;
                            }                            
                        }                        
                        simpleIndexed = true;
                }
                else if(operand.contains(",")) {
                    operands = operand.split(",");
                    if(operands.length > 2) {
                        //error
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("More than 2 operands were found, instruction was ignored.");
                        alert.show();
                        System.out.println("More than 2 operands were found, instruction was ignored.");
                        continue;
                    }
                    else {          
                        boolean skip = false;
                        
                        for(int i = 0; i < 2; i++) {
                            if(operands[i].startsWith("@")) {   //indirect
                                operand = operands[i].substring(1);
                                indirect = true;
                            }
                            else if(operands[i].startsWith("#")) {   //immediate
                                operands[i] = operands[i].substring(1);
                                immediate = true;
                            }
                            
                            if(!isHex(operands[i]) && !registers.isRegister(operands[i]) && !isValidLabel(operands[i])) {
                                System.out.println("Refrence to unkown label: '" + operands[i] + "', instruction was ignored.");
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setContentText("Refrence to unkown label: '" + operands[i] + "', instruction was ignored.");
                                alert.show();
                                skip = true;
                            }
                            else {
                                if(isValidLabel(operands[i])) operands[i] = getLabelAddress(operands[i]).substring(i);
                            }
                        }
                        
                        operand = operands[0];
                        
                        if(skip) continue;
                    }
                }
                else {
                    
                    if(operand.startsWith("@")) {   //indirect
                        operand = operand.substring(1);
                        indirect = true;
                    }
                    else if(operand.startsWith("#")) {   //immediate
                        operand = operand.substring(1);
                        immediate = true;
                    }
                    
                    if(!isHex(operand) && !registers.isRegister(operand) && !isValidLabel(operand)) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setContentText("Refrence to unkown label: '" + operand + "', instruction was ignored.");
                        alert.show();
                        System.out.println("Refrence to unkown label: '" + operand + "', instruction was ignored.");
                        continue;
                    }
                    else {
                        if(isValidLabel(operand)) operand = getLabelAddress(operand);
                    }
                    
                }
            }
            
            if(operations.getOperations().containsKey(operationMnemonic)) {     //processing of instruction
                
                if(format != 4) {
                    format = operations.getOperations().get(operationMnemonic).getFormat();
                }
                                
                int pc = Integer.parseInt(lines.get(lines.indexOf(intermediateFileLine) + 1).getAddress().substring(2), 16);
                
                String opCode = operations.getOperations().get(operationMnemonic).getOpCode();
                
                switch(format) {

                    case 1: {
                        String length = Integer.toHexString((int)(Math.ceil(opCode.length() / 2.0)));
                        objectFile.add("T" + formatZeros(address,6) + formatZeros(length, 2) + formatZeros(opCode, 60)); 
                        
                        listFile.add(address + "\t\t" + ((label==null) ? "" : label) + "\t\t" + operationMnemonic + "\t\t" + intermediateFileLine.getOperand() + "\t\t" + opCode);
                        break;
                    }

                    case 2: {
                                                
                        String r1 = registers.getRegisters().get(operands[0]).getNumber();
                        String r2 = registers.getRegisters().get(operands[1]).getNumber();
                        
                        if(r1 == null || r2 == null) {
                            //error
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("Refrence to unknown register, instruction was ignored.");
                            alert.show();
                            System.out.println("Refrence to unknown register, instruction was ignored.");
                        }
                        else {
                            String instructionObjectCode = opCode + Integer.toHexString(Integer.parseInt(r1)) + Integer.toHexString(Integer.parseInt(r2));
                            String length = Integer.toHexString((int)(Math.ceil(instructionObjectCode.length() / 2.0)));
                            objectFile.add("T" + formatZeros(address, 6) + formatZeros(length, 2) + formatZeros(instructionObjectCode, 60));                          
                            
                            listFile.add(address + "\t\t" + ((label==null) ? "" : label) + "\t\t" + operationMnemonic + "\t\t" + intermediateFileLine.getOperand() + "\t\t" + instructionObjectCode);
                        }
                        break;
                    }

                    case 3: {

                        String nix = null;
                        String bpe = null;
                        String flags = null;
                        
                        int disp = Integer.parseInt(operand, 16);
                        
                        int B = registers.getRegisters().get("B").getValue();
                        
                        if( (disp - pc >= -2048) && (disp - pc) <= 2047) {
                            disp = disp - pc;
                            bpe = "010";
                        } 
                        else if(baseAvailable) {
                            if( (disp - B >= 0) && (disp - B <= 4095) ) {
                                disp = disp - B;
                                bpe = "100";
                            }else {
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setContentText("Displacement out of bounds, instruction was ignored.");
                                alert.show();
                                System.out.println("Displacement out of bounds, instruction was ignored.");
                                continue;
                            }
                        }
                        else {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setContentText("Displacement out of bounds, instruction was ignored.");
                            alert.show();
                            System.out.println("Displacement out of bounds, instruction was ignored.");
                            continue;
                        }

                        if(indirect) {   //indirect
                            nix = "100";
                            flags = nix + bpe;
                            
                        }
                        else if(immediate) {   //immediate
                            nix = "010";
                            flags = nix + bpe;
                        }
                        else if(simpleIndexed){     //simple indexed
                            nix = "111";
                            flags = nix + bpe;
                            
                            disp -= registers.getRegisters().get("X").getValue();
                        }
                        else {  //simple
                            nix = "110";
                            flags = nix + bpe;
                        }
                        
                        if(bpe.equals("010")){
                            operand = Integer.toHexString(disp).toUpperCase();   //2's compliment
                        }
                        else {
                            operand = Integer.toString(disp, 16).toUpperCase();
                        }
                        
                        String instructionObjectCode = opCode + Integer.toString(Integer.parseInt(flags, 2), 16) + operand;
                        String length = Integer.toHexString((int)(Math.ceil(instructionObjectCode.length() / 2.0)));
                        objectFile.add("T" + formatZeros(address, 6) + formatZeros(length, 2) + formatZeros(instructionObjectCode, 60)); 
                        
                        listFile.add(address + "\t\t" + ((label==null) ? "" : label) + "\t\t" + operationMnemonic + "\t\t" + intermediateFileLine.getOperand() + "\t\t" + instructionObjectCode);
                        //System.out.println("Addres: " + address + ", length: " + length + ", opCode: " + opCode + ", flags(binary): " + flags + ", flags(hex): " + Integer.toString(Integer.parseInt(flags, 2), 16) + ", Operand: " + operand);

                        break;
                    }

                    case 4: {
                        
                        String flags = null;

                        if(indirect) {   //indirect
                            flags = "100001";
                        }
                        else if(immediate) {   //immediate
                            flags = "010001";
                        }
                        else if(simpleIndexed){     //simple indexed
                            flags = "111001";
                            operand = Integer.toString(Integer.parseInt(operand, 16) + registers.getRegisters().get("X").getValue(), 16).toUpperCase();
                        }
                        else {  //simple
                            flags = "110001";
                        }
                        
                        String instructionObjectCode = opCode + Integer.toString(Integer.parseInt(flags, 2), 16) + operand;
                        String length = Integer.toHexString((int)(Math.ceil(instructionObjectCode.length() / 2.0)));
                        objectFile.add("T" + formatZeros(address, 6) + formatZeros(length, 2) + formatZeros(instructionObjectCode, 60)); 
                        
                        listFile.add(address + "\t\t" + ((label==null) ? "" : label) + "\t\t" + operationMnemonic + "\t\t" + intermediateFileLine.getOperand() + "\t\t" + instructionObjectCode);
                        break;
                    }

                }
            }
            else { //processing of directives
                    
                if(operationMnemonic.equalsIgnoreCase("RESB")) {
                    listFile.add(address + "\t\t" + ((label==null) ? "" : label) + "\t\t" + operationMnemonic + "\t\t" + intermediateFileLine.getOperand() );
                }
                else if(operationMnemonic.equalsIgnoreCase("RESW")) {
                    listFile.add(address + "\t\t" + ((label==null) ? "" : label) + "\t\t" + operationMnemonic + "\t\t" + intermediateFileLine.getOperand() );
                }
                else if(operationMnemonic.equalsIgnoreCase("ORG")) {
                    listFile.add(address + "\t\t" + ((label==null) ? "" : label) + "\t\t" + operationMnemonic + "\t\t" + intermediateFileLine.getOperand() );
                }
                else if(operationMnemonic.equalsIgnoreCase("EQU")) {
                    listFile.add(address + "\t\t" + ((label==null) ? "" : label) + "\t\t" + operationMnemonic + "\t\t" + intermediateFileLine.getOperand() );
                }
                else if(operationMnemonic.equalsIgnoreCase("LTORG")) {
                    listFile.add(address + "\t\t" + ((label==null) ? "" : label) + "\t\t" + operationMnemonic + "\t\t" + intermediateFileLine.getOperand() );
                }
                else if(operationMnemonic.equalsIgnoreCase("BASE")) {
                    baseAvailable = true;
                    int B = Integer.parseInt(operand, 16);
                    registers.getRegisters().get("B").setValue(B);
                    
                    listFile.add(address + "\t\t" + ((label==null) ? "" : label) + "\t\t" + operationMnemonic + "\t\t" + intermediateFileLine.getOperand() );
                }
                else if(operationMnemonic.equalsIgnoreCase("NOBASE")) {
                    baseAvailable = false;
                    
                    listFile.add(address + "\t\t" + ((label==null) ? "" : label) + "\t\t" + operationMnemonic + "\t\t" + ((intermediateFileLine.getOperand()==null) ? "" : intermediateFileLine.getOperand()) );
                }
                else if(operationMnemonic.equalsIgnoreCase("END")) {
                    //End Record
                    
                    if(operand!=null)
                        if(!operand.equals(startingAddress) && !operand.equals(programName)) {
                        //error

                        System.out.println("Operand for END does not match the starting address of the program.");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Operand for END does not match the starting address of the program.");
                        alert.show();
                        continue;
                    }
                    
                    objectFile.add("E" + formatZeros(startingAddress, 6));  
                    listFile.add(address + "\t\t" + ((label==null) ? "" : label) + "\t\t" + operationMnemonic + "\t\t" + intermediateFileLine.getOperand() );
                    
                    String programLength = Integer.toString(Integer.parseInt(address, 16) - Integer.parseInt(startingAddress, 16), 16).toUpperCase();
                    String headerRecord = objectFile.get(0).concat(formatZeros(programLength, 6));
                    objectFile.remove(0);
                    objectFile.add(0, headerRecord);
                    break;
                }
            }
        }
        
        for(String line : objectFile) System.out.println(line);
        System.out.println("----------------------");
        for(String line : listFile) System.out.println(line);
        System.out.println("----------------------");
        
        try {
            FileWriter fileWriter = new FileWriter("src/output/ObjectFile.txt");
            
            for(String line : objectFile){
                fileWriter.write(line);
                fileWriter.write("\n");
            }
            
            fileWriter.close();
        } catch (IOException ex) {
            System.out.println("Error opening object file.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error opening object file.");
            alert.show();
        }
        
        try {
            FileWriter fileWriter = new FileWriter("src/output/ListFile.txt");
            
            for(String line : listFile){
                fileWriter.write(line);
                fileWriter.write("\n");
            }
            
            fileWriter.close();
        } catch (IOException ex) {
            System.out.println("Error opening list file.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error opening object file.");
            alert.show();
        }
        
    }
    
    private boolean isValidLabel(String label) {
            
        for(SymbolTableLine symbol : table.getTable()) {
            if(label.equalsIgnoreCase(symbol.getLabel())) {
                return true;
            }
        }

        return false;
    }
    
    private String getLabelAddress(String label) {
        for(SymbolTableLine symbol : table.getTable()) {
            if(label.equalsIgnoreCase(symbol.getLabel())) {
                return symbol.getAddress().substring(2);
            }
        }
        return null;
    }
    
    private boolean isInt(String str) {
        try {  
            Integer.parseInt(str);  
            return true;
        } catch(NumberFormatException e){  
            return false;  
        }  
    }
    
    private boolean isHex(String str) { 
        try {  
            Integer.parseInt(str, 16);  
            return true;
        } catch(NumberFormatException e){  
            return false;  
        }  
    }
    
    public String stringToHex(String arg) {
        return String.format("%x", new BigInteger(1, arg.getBytes(/*YOUR_CHARSET?*/)));
    }
    
    private String formatZeros(String str, int requiredLength) {
        while(str.length() < requiredLength) str = "0" + str;
        
        return str.toUpperCase();
    }

    public static ArrayList<String> getObjectFile() {
        return objectFile;
    }

    public static ArrayList<String> getListFile() {
        return listFile;
    }
}
