package com.example.myfirstprojecthit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView result;
    String num1 = "";
    String num2 = "";
    String operator = "";
    Boolean calculated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            result = findViewById(R.id.textViewResult);
    }

    public void calcButtonClickHandler(View view) {

        if( view instanceof Button){

            Button b = (Button) view;
            String input = b.getText().toString();

            if(this.shouldClear()) {
                result.setText("");
            }

            if(input.equals("-")) {
                Boolean isUsedForDisplay = (!this.num1.isEmpty() && !this.operator.isEmpty()) ||
                        (this.num1.isEmpty() && result.getText().toString().isEmpty());

                if(isUsedForDisplay){
                    this.handleNumberInput(input);
                    return;
                }

            }

            if(this.isNumber(input) || input.equals(".")){
                this.handleNumberInput(input);
                return;
            }

            if(this.isOperator(input)){
                this.handleOperatorInput(input);
                return;
            }

            if (input.equals("=")){
                this.handleExecutionInput();
                return;
            }

            this.reset("");
        }
    }

    private Boolean shouldClear(){
        String str = result.getText().toString();

        return !(this.isNumber(str) || str.endsWith(".") || (this.num1.equals("-0") || this.num2.equals("-0") && str.equals("-")));
    }

    private void handleNumberInput(String input){

        if(result.getText().toString().contains(".") && input.equals(".")){
            this.reset("error");
            return;
        }

        Boolean shouldStopOperationChaining = this.calculated && this.operator.isEmpty();

        if(shouldStopOperationChaining) {
            result.setText(input);
            this.calculated = false;
        } else {
            result.append(input);
        }

        String str = result.getText().toString();

        String displayedNum = input.equals("-") ? "-0" : str;

        if(str.equals(".")){
            result.setText("0.");
        }



        if(this.operator.isEmpty()){
            this.num1 = displayedNum == "0." ? "0.0" : displayedNum;
        } else {
            this.num2 = displayedNum == "0." ? "0.0" : displayedNum;
        }

    }

    private void handleOperatorInput(String input) {
        this.operator = input;
        this.result.setText(input);
    }

    private void handleExecutionInput(){
        if(this.num1.isEmpty() || this.num2.isEmpty()) return;

        String calcResult = this.calculate();
        if(calcResult.isEmpty()){
            reset("Error");
            return;
        }

        result.setText(calcResult);

        // reset with num1 as result
        this.operator = "";
        this.num2 = "";
        this.num1 = calcResult;
        this.calculated = true;
    }

    private String calculate() {
        double num1 = Double.parseDouble(this.num1);
        double num2 = Double.parseDouble(this.num2);

        switch (this.operator) {
            case "+":
                return (num1 + num2)+"";
            case "-":
                return (num1 - num2)+"";
            case "X":
                return (num1 * num2)+"";
            case "/":
                if (num2 == 0) {
                    return "";
                }
                return (num1 / num2)+"";
            default:
                return "";

        }
    }

    private void reset(String error){
        this.operator = "";
        this.num1 = "";
        this.num2 = "";
        this.result.setText(error);
    }

    private boolean isNumber( String input ) {
        try {
            Double.parseDouble( input );
            return true;
        }
        catch( Exception e ) {
            return false;
        }
    }

    private Boolean isOperator(String input){
        return (input.equals("+")  || input.equals("-") || input.equals("/") || input.equals("X"));
    }
}