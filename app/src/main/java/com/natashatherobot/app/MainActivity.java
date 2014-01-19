package com.natashatherobot.app;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

import java.text.NumberFormat;

public class MainActivity extends Activity {
    EditText etInitialAmount;
    TextView tvTipAmountValue;
    TextView tvTotalPaymentValue;
    EditText etTipPercentageOther;
    RadioGroup rgTipPercentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etInitialAmount = (EditText) findViewById(R.id.etInitialAmount);
        tvTipAmountValue = (TextView) findViewById(R.id.tvTipAmountValue);
        tvTotalPaymentValue = (TextView) findViewById(R.id.tvTotalPaymentValue);
        etTipPercentageOther = (EditText) findViewById(R.id.etTipPercentageOther);
        rgTipPercentage = (RadioGroup) findViewById(R.id.rgTipPercentage);

        setupEditTextInitialAmountListener();
        setupEditTextOtherAmountListener();
        setupRadioGroupListener();
    }

    public void setupEditTextInitialAmountListener() {
       etInitialAmount.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {}

           @Override
           public void afterTextChanged(Editable s) {
               tvTipAmountValue.setText(tipDollarAmount());
               tvTotalPaymentValue.setText(totalDollarAmount());
           }
       });
    }

    public void setupEditTextOtherAmountListener() {
        etTipPercentageOther.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                tvTipAmountValue.setText(tipDollarAmount());
                tvTotalPaymentValue.setText(totalDollarAmount());
            }
        });
    }

    public void setupRadioGroupListener() {
        rgTipPercentage.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton checkedRadioButton = (RadioButton) rgTipPercentage.findViewById(checkedId);
                if (checkedRadioButton.getTag().toString().equals("100")) {
                    etTipPercentageOther.setEnabled(true);
                    etTipPercentageOther.requestFocus();
                } else {
                    etTipPercentageOther.setEnabled(false);
                }

                tvTipAmountValue.setText(tipDollarAmount());
                tvTotalPaymentValue.setText(totalDollarAmount());
            }
        });
    }

    // Private Calculations

    private String tipDollarAmount() {
        return NumberFormat.getCurrencyInstance().format(tipAmount());
    }

    private String totalDollarAmount() {
        Double totalPay = 0.0;

        String initialAmountString = etInitialAmount.getText().toString();
        if (initialAmountString.length() > 0) {
            Double initialAmount = Double.parseDouble(etInitialAmount.getText().toString());
            totalPay = initialAmount + tipAmount();
        }

        return NumberFormat.getCurrencyInstance().format(totalPay);
    }

    private Double tipAmount() {
        Double tipAmount = 0.0;

        String initialAmountString = etInitialAmount.getText().toString();
        if (initialAmountString.length() > 0) {
            Double initialAmount = Double.parseDouble(initialAmountString);

            int checkedRadioButtonId = rgTipPercentage.getCheckedRadioButtonId();
            RadioButton checkedRadioButton = (RadioButton) rgTipPercentage.findViewById(checkedRadioButtonId);
            int checkedRadioButtonPercentage = Integer.parseInt(checkedRadioButton.getTag().toString());

            if (checkedRadioButtonPercentage == 100) {
                Double otherTipPercentage = 0.0;
                String otherTipPercentageString = etTipPercentageOther.getText().toString();
                if (otherTipPercentageString.length() > 0) {
                    otherTipPercentage = Double.parseDouble(otherTipPercentageString);
                }
                tipAmount = initialAmount * otherTipPercentage / 100;
            } else {
                tipAmount = initialAmount * checkedRadioButtonPercentage / 100;
            }
        }

        return tipAmount;
    }

}
