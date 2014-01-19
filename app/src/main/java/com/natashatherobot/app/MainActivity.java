package com.natashatherobot.app;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class MainActivity extends Activity {
    EditText etInitialAmount;
    EditText etTipPercentageOther;
    RadioGroup rgTipPercentage;

    public static final Integer tagForRadioButtonOther = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etInitialAmount = (EditText) findViewById(R.id.etInitialAmount);
        etTipPercentageOther = (EditText) findViewById(R.id.etTipPercentageOther);
        rgTipPercentage = (RadioGroup) findViewById(R.id.rgTipPercentage);

        setupEditTextAmountListener(etInitialAmount);
        setupEditTextAmountListener(etTipPercentageOther);
        setupRadioGroupListener();
    }

    public void setupEditTextAmountListener(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                displayFinalAmounts();
            }
        });
    }

    public void setupRadioGroupListener() {
        rgTipPercentage.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton checkedRadioButton = (RadioButton) rgTipPercentage.findViewById(checkedId);
                if (tagForView(checkedRadioButton) == tagForRadioButtonOther) {
                    etTipPercentageOther.setEnabled(true);
                    etTipPercentageOther.requestFocus();
                } else {
                    etInitialAmount.requestFocus();
                    etTipPercentageOther.setEnabled(false);
                }

                displayFinalAmounts();
            }
        });
    }

    // Private Calculations

    private String formattedStringFromAmount(BigDecimal amount) {
        return NumberFormat.getCurrencyInstance().format(amount);
    }

    private BigDecimal tipAmount() {
        BigDecimal initialAmount = decimalFromEditText(etInitialAmount);

        Integer tagForCheckedRadioButton = tagForCheckedRadioButton();
        BigDecimal tipPercentage = tagForCheckedRadioButton == tagForRadioButtonOther ? decimalFromEditText(etTipPercentageOther) : (new BigDecimal(tagForCheckedRadioButton));

        return initialAmount.multiply(tipPercentage).divide(new BigDecimal(100));
    }

    private Integer tagForView(View view) {
        return Integer.parseInt(view.getTag().toString());
    }

    private Integer tagForCheckedRadioButton() {
        Integer checkedRadioButtonId = rgTipPercentage.getCheckedRadioButtonId();
        RadioButton checkedRadioButton = (RadioButton) rgTipPercentage.findViewById(checkedRadioButtonId);
        return tagForView(checkedRadioButton);
    }

    private BigDecimal decimalFromEditText(EditText editText) {
        BigDecimal amount = new BigDecimal(0.0);
        String amountString = editText.getText().toString();
        if (amountString.length() > 0) {
            amount = new BigDecimal(amountString);
        }
        return amount;
    }

    private void displayFinalAmounts() {
        TextView tvTipAmountValue = (TextView) findViewById(R.id.tvTipAmountValue);
        TextView tvTotalPaymentValue = (TextView) findViewById(R.id.tvTotalPaymentValue);

        BigDecimal tipAmount = tipAmount();
        tvTipAmountValue.setText(formattedStringFromAmount(tipAmount));

        BigDecimal totalPayment = decimalFromEditText(etInitialAmount).add(tipAmount);
        tvTotalPaymentValue.setText(formattedStringFromAmount(totalPayment));
    }

}
