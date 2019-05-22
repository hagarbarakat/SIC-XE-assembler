/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample;

/**
 *
 * @author lap store
 */
public class CodeLine {
    private String instruction; 
    private String operand; 
    private String objectCode; 
    private String label; 
    private int PC; 
    private String errorMessage; 
    private String Comment;
    protected boolean TrnsltedFlg ; //start

    public CodeLine() {
    }

    public CodeLine(String instruction, String operand, String label, int PC, boolean TrnsltedFlg) {
        this.instruction = instruction;
        this.operand = operand;
        this.label = label;
        this.PC = PC;
        this.TrnsltedFlg = TrnsltedFlg;
    }

    public CodeLine(String instruction, String operand, String label) {
        this.instruction = instruction;
        this.operand = operand;
        this.label = label;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String Comment) {
        this.Comment = Comment;
    }
    
    

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getOperand() {
        return operand;
    }

    public void setOperand(String operand) {
        this.operand = operand;
    }

    public String getObjectCode() {
        return objectCode;
    }

    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getPC() {
        return PC;
    }

    public void setPC(int PC) {
        this.PC = PC;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isTrnsltedFlg() {
        return TrnsltedFlg;
    }

    public void setTrnsltedFlg(boolean TrnsltedFlg) {
        this.TrnsltedFlg = TrnsltedFlg;
    }
 

    
}
