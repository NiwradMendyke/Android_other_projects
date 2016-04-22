package com.example.CalculatorApp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.text.Editable;
import android.view.View;

public class Calculate extends Activity implements View.OnClickListener {

    Button one, two, three, four, five, six, seven, eight, nine, zero, add, sub, mult, div, equal, decimal;
    EditText inp, outp;
    double op1 = 0;
    double op2 = 0;
    String optr = "";

    String input = "";

    Boolean equalWasLast = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        one = (Button) findViewById(R.id.button1);
        two = (Button) findViewById(R.id.button2);
        three = (Button) findViewById(R.id.button3);
        four = (Button) findViewById(R.id.button4);
        five = (Button) findViewById(R.id.button5);
        six = (Button) findViewById(R.id.button6);
        seven = (Button) findViewById(R.id.button7);
        eight = (Button) findViewById(R.id.button8);
        nine = (Button) findViewById(R.id.button9);
        zero = (Button) findViewById(R.id.button0);

        add = (Button) findViewById(R.id.button_addition);
        sub = (Button) findViewById(R.id.button_subtraction);
        mult = (Button) findViewById(R.id.button_multiplication);
        div = (Button) findViewById(R.id.button_division);

        decimal = (Button) findViewById(R.id.button_decimal);
        equal = (Button) findViewById(R.id.button_calculate);

        inp = (EditText) findViewById(R.id.text_input);
        outp = (EditText) findViewById(R.id.text_output);

        try {
            one.setOnClickListener(this);
            two.setOnClickListener(this);
            three.setOnClickListener(this);
            four.setOnClickListener(this);
            five.setOnClickListener(this);
            six.setOnClickListener(this);
            seven.setOnClickListener(this);
            eight.setOnClickListener(this);
            nine.setOnClickListener(this);
            zero.setOnClickListener(this);
            add.setOnClickListener(this);
            sub.setOnClickListener(this);
            mult.setOnClickListener(this);
            div.setOnClickListener(this);
            decimal.setOnClickListener(this);
            equal.setOnClickListener(this);
        } catch (Exception e) { }
    }

    public void operation(){
        if(optr.equals("+")){
            op2 = Double.parseDouble(inp.getText().toString());
            inp.setText("");
            op1 = op1 + op2;
            outp.setText(Double.toString(op1));
        }
        else if(optr.equals("-")){
            op2 = Double.parseDouble(inp.getText().toString());
            inp.setText("");
            op1 = op1 - op2;
            outp.setText(Double.toString(op1));
        }
        else if(optr.equals("*")){
            op2 = Double.parseDouble(inp.getText().toString());
            inp.setText("");
            op1 = op1 * op2;
            outp.setText(Double.toString(op1));
        }
        else if(optr.equals("/")){
            op2 = Double.parseDouble(inp.getText().toString());
            inp.setText("");
            if (op2 == 0) {
                outp.setText("Error");
                return;
            }
            op1 = op1 / op2;
            outp.setText(Double.toString(op1));
        }
        else;
    }

    @Override
    public void onClick(View v) {

        Editable str = inp.getText();

        switch(v.getId()) {
            // if press 1
            case R.id.button1:
                if (equalWasLast) {
                    op1 = 0;
                    op2 = 0;
                    equalWasLast = false;
                }
                str = str.append(one.getText());
                inp.setText(input + str);
                break;

            // if press 2
            case R.id.button2:
                if (equalWasLast) {
                    op1 = 0;
                    op2 = 0;
                    equalWasLast = false;
                }
                //Log.d("a", "at 2 case, op1 = " + op1 + " and op2 = " + op2);
                str = str.append(two.getText());
                inp.setText(input + str);
                break;

            // if press 3
            case R.id.button3:
                if (equalWasLast) {
                    op1 = 0;
                    op2 = 0;
                    equalWasLast = false;
                }
                //Log.d("a", "at 3 case, op1 = " + op1 + " and op2 = " + op2);
                str = str.append(three.getText());
                inp.setText(input + str);
                break;

            // if press 4
            case R.id.button4:
                if (equalWasLast) {
                    op1 = 0;
                    op2 = 0;
                    equalWasLast = false;
                }
                str = str.append(four.getText());
                inp.setText(input + str);
                break;

            // if press 5
            case R.id.button5:
                if (equalWasLast) {
                    op1 = 0;
                    op2 = 0;
                    equalWasLast = false;
                }
                str = str.append(five.getText());
                inp.setText(input + str);
                break;

            // if press 6
            case R.id.button6:
                if (equalWasLast) {
                    op1 = 0;
                    op2 = 0;
                    equalWasLast = false;
                }
                str = str.append(six.getText());
                inp.setText(input + str);
                break;

            // if press 7
            case R.id.button7:
                if (equalWasLast) {
                    op1 = 0;
                    op2 = 0;
                    equalWasLast = false;
                }
                str = str.append(seven.getText());
                inp.setText(input + str);
                break;

            // if press 8
            case R.id.button8:
                if (equalWasLast) {
                    op1 = 0;
                    op2 = 0;
                    equalWasLast = false;
                }
                str = str.append(eight.getText());
                inp.setText(input + str);
                break;

            // if press 9
            case R.id.button9:
                if (equalWasLast) {
                    op1 = 0;
                    op2 = 0;
                    equalWasLast = false;
                }
                str = str.append(nine.getText());
                inp.setText(input + str);
                break;

            // if press 0
            case R.id.button0:
                if (equalWasLast) {
                    op1 = 0;
                    op2 = 0;
                    equalWasLast = false;
                }
                str = str.append(zero.getText());
                inp.setText(input + str);
                break;

            // if press .
            case R.id.button_decimal:
                String theInput = inp.getText().toString();
                if (theInput.length() >= 1)
                    if ((input.substring(theInput.length()-1).equals(".")))
                        break;
                if (equalWasLast) {
                    op1 = 0;
                    op2 = 0;
                    equalWasLast = false;
                }
                str = str.append(decimal.getText());
                inp.setText(input + str);

                break;

            // if press +
            case R.id.button_addition:
                optr = "+";
                //Log.d("a", "at beginning of addition case, op1 = " + op1 + " and op2 = " + op2);
                if (inp.getText().toString().equals(""))
                    break;
                else if(op1 == 0){
                    op1 = Double.parseDouble(inp.getText().toString());
                    inp.setText("");
                }
                else{
                    op2 = Double.parseDouble(inp.getText().toString());
                    inp.setText("");
                    op1 = op1 + op2;
                    outp.setText(Double.toString(op1));
                }
                break;

            // if press -
            case R.id.button_subtraction:
                optr = "-";
                if (inp.getText().toString().equals(""))
                    break;
                else if(op1 == 0){
                    op1 = Double.parseDouble(inp.getText().toString());
                    inp.setText("");
                }
                else{
                    op2 = Double.parseDouble(inp.getText().toString());
                    inp.setText("");
                    op1 = op1 - op2;
                    outp.setText(Double.toString(op1));
                }
                break;

            // if press *
            case R.id.button_multiplication:
                optr = "*";
                //Log.d("a", "at beginning of mult case, op1 = " + op1 + " and op2 = " + op2);
                if (inp.getText().toString().equals(""))
                    break;
                else if(op1 == 0){
                    op1 = Double.parseDouble(inp.getText().toString());
                    inp.setText("");
                    break;
                }
                else{
                    op2 = Double.parseDouble(inp.getText().toString());
                    inp.setText("");
                    op1 = op1 * op2;
                    outp.setText(Double.toString(op1));
                    break;
                }

            // if press /
            case R.id.button_division:
                optr = "/";
                if (inp.getText().toString().equals(""))
                    break;
                else if(op1 == 0){
                    op1 = Double.parseDouble(inp.getText().toString());
                    inp.setText("");
                    break;
                }
                else{
                    op2 = Double.parseDouble(inp.getText().toString());
                    inp.setText("");
                    op1 = op1 / op2;
                    outp.setText(Double.toString(op1));
                    break;
                }

            // if press =
            case R.id.button_calculate:
                equalWasLast = true;
                if(!optr.equals(null)) {
                    if (op2 != 0) {
                        if (optr.equals("+")) {
                            inp.setText("");
                            op1 = op1 + op2;
                            outp.setText(Double.toString(op1));
                        }
                        else if (optr.equals("-")) {
                            inp.setText("");
							op1 = op1 - op2;
                            outp.setText(Double.toString(op1));
                        }
                        else if (optr.equals("*")) {
                            inp.setText("");
                            op1 = op1 * op2;
                            outp.setText(Double.toString(op1));
                        }
                        else if (optr.equals("/")) {
                            inp.setText("");
                            op1 = op1 / op2;
                            outp.setText(Double.toString(op1));
                        }
                    }
                    else if (optr.equals(""))
                        break;
                    else if (inp.getText().toString().equals(""))
                        break;
                    else
                        operation();
                }

            //op1 = 0;
            //op2 = 0;
            break;
        }
    }
}
