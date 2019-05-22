
package Pass2;

import java.util.ArrayList;

public class Pass2 {
    
    public Pass2(ArrayList<String> intermediateFile, ArrayList<String> symbolTable) {
        
        ArrayList<IntermediateFileLine> intermediateFileLines = new ArrayList<>();
        ArrayList<SymbolTableLine> symbolTableLines = new ArrayList<>();
        
        for(String line : intermediateFile) {
            
            if(intermediateFile.indexOf(line) < 2) continue;
            
            line = line.trim().replaceAll(" +", " ");
            
            String parts[] = line.split(" ", 0);
            
            if(parts.length == 5) {
                if(!parts[2].startsWith(".") && isNumeric(parts[0]))
                    intermediateFileLines.add(new IntermediateFileLine(parts[0], parts[1], parts[2], parts[3], parts[4]));
            }
            else if(parts.length == 4) {
                if(!parts[2].startsWith(".") && isNumeric(parts[0]))
                    intermediateFileLines.add(new IntermediateFileLine(parts[0], parts[1], null, parts[2], parts[3]));
            }
            else if(parts.length == 3) {
                if(!parts[2].startsWith(".") && isNumeric(parts[0]))
                    intermediateFileLines.add(new IntermediateFileLine(parts[0], parts[1], null, parts[2], null));
            }
        
        }
        
        System.out.println("--------------");
        for(IntermediateFileLine line : intermediateFileLines) System.out.println(line);
        
        for(String line : symbolTable) {
            line = line.trim().replaceAll(" +", " ");
            
            String parts[] = line.split(" ", 0);
            
            if(parts.length == 2)
                symbolTableLines.add(new SymbolTableLine(parts[0], parts[1]));
            
        }
        
        System.out.println("--------------");
        for(SymbolTableLine line : symbolTableLines) System.out.println(line);
        System.out.println("--------------");
        SymbolTable table = new SymbolTable(symbolTableLines);

        ObjectCode objectCode = new ObjectCode(intermediateFileLines, table);
    }
    
    
    private boolean isNumeric(String str) { 
        try {  
            Integer.parseInt(str);  
            return true;
        } catch(NumberFormatException e){  
            return false;  
        }  
    }
    

}
