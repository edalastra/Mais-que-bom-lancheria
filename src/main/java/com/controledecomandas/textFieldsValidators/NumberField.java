package com.controledecomandas.textFieldsValidators;

import javafx.scene.control.TextField;

public class NumberField extends TextField {

    public NumberField() {
        this.setPromptText("Entre somente com números");
    }

    @Override
    public void replaceText(int i, int i1, String string) {
        if(string.matches("[0-9]") || string.isEmpty()) {
            super.replaceText(i, i1, string);
        }
    }

    @Override
    public void replaceSelection(String s) {
        super.replaceSelection(s);
    }
}
