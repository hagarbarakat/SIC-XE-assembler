
package Pass2;

import java.util.ArrayList;

public class SymbolTable {
    
    private ArrayList<SymbolTableLine> table;

    public SymbolTable(ArrayList<SymbolTableLine> table) {
        this.table = table;
    }

    public ArrayList<SymbolTableLine> getTable() {
        return table;
    }
    

}
