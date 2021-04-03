package com.controledecomandas.textFieldsValidators;

import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.TextField;

public class NumberField extends JFXTextField {


    public NumberField() {
        this.setPromptText("Entre somente com números");
    }

    @Override
    public void replaceText(int i, int i1, String string) {
        if(string.matches("[0-9]") || string.isEmpty()) {
            super.replaceText(i, i1, string);
        }
    }

    public int getValue() {
        if(!super.getText().equals("")) {
            return Integer.parseInt(super.getText());
        }
        return 0;
    }

    public void setValue(int value) {
        super.setText(String.valueOf(value));
    }

    @Override
    public void replaceSelection(String s) {
        super.replaceSelection(s);
    }
}
