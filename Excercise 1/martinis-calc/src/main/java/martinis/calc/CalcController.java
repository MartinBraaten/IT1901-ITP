package martinis.calc;

import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;

public class CalcController {

    private Calc calc;

    public CalcController() {
        calc = new Calc(0.0, 0.0, 0.0);
    }

    public Calc getCalc() {
        return calc;
    }

    public void setCalc(Calc calc) {
        this.calc = calc;
        updateOperandsView();
    }

    @FXML
    private ListView<Double> operandsView;

    @FXML
    private Label operandView;

    @FXML
    void initialize() {
        updateOperandsView();
    }

    private void updateOperandsView() {
        List<Double> operands = operandsView.getItems();
        operands.clear();
        int elementCount = Math.min(calc.getOperandCount(), 3);
        for (int i = 0; i < elementCount; i++) {
            operands.add(calc.peekOperand(elementCount - i - 1));
        }
    }

    private String getOperandString() {
        return operandView.getText();
    }

    private boolean hasOperand() {
        return ! getOperandString().isBlank();
    }

    private double getOperand() {
        return Double.valueOf(operandView.getText());
    }
    
    private void setOperand(String operandString) {
        operandView.setText(operandString);
    }

    @FXML
    void handleEnter() {
        if (hasOperand()) {
            calc.pushOperand(getOperand());
        } else {
            calc.dup();
        }
        setOperand("");
        updateOperandsView();
    }

    private void appendToOperand(String s) {
        setOperand(getOperandString() + s);
    }

    @FXML
    void handleDigit(ActionEvent ae) {
        if (ae.getSource() instanceof Labeled l) {
            appendToOperand(l.getText());
        }
    }

    @FXML
    void handlePoint() {
        var operandString = getOperandString();
        if (operandString.contains(".")) {
            String[] parts = operandString.split("\\.");
            setOperand(parts[0] + ".");
        } else {
            appendToOperand(".");
        }
    }

    @FXML
    void handleClear() {
        setOperand("");
    }

    @FXML
    void handleSwap() {
        calc.swap();
        updateOperandsView();
    }

    private void performOperation(UnaryOperator<Double> op) {
        calc.performOperation(op);
        setOperand("");
        updateOperandsView();
        

    }

    private void performOperation(boolean swap, BinaryOperator<Double> op) {
        if (hasOperand()) {
            calc.pushOperand(getOperand());
        }
        if (swap) {
            calc.swap();
        } 
        calc.performOperation(op);
        setOperand("");
        updateOperandsView();
    }

    @FXML
    void handleOpAdd() {
        performOperation(false, (a, b) -> a + b);
    }

    @FXML
    void handleOpSub() {
        performOperation(false, (a, b) -> a - b);
    }

    @FXML
    void handleOpMult() {
        performOperation(false, (a, b) -> a * b);
    }
    @FXML
    void handlePi() {
        setOperand(Double.toString(Math.PI));
        handleEnter();
    }

    @FXML
    void handleOpDiv() {
        performOperation(false, (a, b) -> a / b);
    }
    @FXML
    void handleOpSqrt() {
        performOperation(a -> Math.sqrt(a));
        
        
    }
}
