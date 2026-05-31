package ute.buiquanghuy.maytinh;


import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView tvExpression, tvResult;
    String expression = "";
    boolean isResultShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvExpression = findViewById(R.id.tvExpression);
        tvResult = findViewById(R.id.tvResult);
        GridLayout grid = findViewById(R.id.mainGrid);

        if (grid != null) {
            for (int i = 0; i < grid.getChildCount(); i++) {
                View v = grid.getChildAt(i);
                if (v instanceof Button) {
                    v.setOnClickListener(this::onClick);
                }
            }
        }
    }

    private void onClick(View v) {
        Button btn = (Button) v;
        String text = btn.getText().toString();

        switch (text) {
            case "AC":
                expression = "";
                tvExpression.setText("");
                tvResult.setText("0");
                isResultShown = false;
                break;
            case "⌫":
                if (isResultShown) {
                    isResultShown = false;
                } else if (!expression.isEmpty()) {
                    expression = expression.substring(0, expression.length() - 1);
                }
                tvExpression.setText(expression);
                liveCalculate();
                break;
            case "( )":
                handleParentheses();
                break;
            case "=":
                if (!expression.isEmpty()) {
                    calculateFinal();
                    isResultShown = true;
                }
                break;
            case "+": case "-": case "×": case "÷": case "%":
                isResultShown = false;
                handleOperator(text);
                break;
            default:
                if (isResultShown) {
                    expression = text;
                    isResultShown = false;
                } else {
                    expression += text;
                }
                tvExpression.setText(expression);
                liveCalculate();
                break;
        }
    }

    private void handleParentheses() {
        if (isResultShown) {
            expression = "";
            isResultShown = false;
        }
        int open = 0, close = 0;
        for (char c : expression.toCharArray()) {
            if (c == '(') open++;
            if (c == ')') close++;
        }
        if (open == close || expression.endsWith("(")) {
            expression += "(";
        } else {
            expression += ")";
        }
        tvExpression.setText(expression);
        liveCalculate();
    }

    private void handleOperator(String op) {
        if (!expression.isEmpty()) {
            char last = expression.charAt(expression.length() - 1);
            if ("+-(×÷%".indexOf(last) != -1) {
                expression = expression.substring(0, expression.length() - 1);
            }
            expression += op;
            tvExpression.setText(expression);
        } else if (op.equals("-")) {
            expression += op;
            tvExpression.setText(expression);
        }
    }

    private String formatResult(BigDecimal bd) {
        if (bd.compareTo(BigDecimal.ZERO) == 0) return "0";

        bd = bd.stripTrailingZeros();
        String plain = bd.toPlainString();

        if (plain.length() > 15) {
            DecimalFormat df = new DecimalFormat("0.########E0", new DecimalFormatSymbols(Locale.US));
            return df.format(bd);
        }
        return plain;
    }

    private void liveCalculate() {
        if (expression.isEmpty()) {
            tvResult.setText("0");
            return;
        }
        try {
            BigDecimal result = eval(formatExpression(expression));
            tvResult.setText(formatResult(result));
        } catch (Exception e) {
            tvResult.setText("");
        }
    }

    private void calculateFinal() {
        try {
            BigDecimal result = eval(formatExpression(expression));
            expression = formatResult(result);
            tvExpression.setText(expression);
            tvResult.setText("");
        } catch (Exception e) {
            tvResult.setText("Error");
        }
    }

    private String formatExpression(String exp) {
        return exp.replaceAll("([0-9.E+-]+)%", "($1/100.0)")
                .replace("×", "*")
                .replace("÷", "/");
    }

    public static BigDecimal eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            BigDecimal parse() {
                nextChar();
                BigDecimal x = parseExpression();
                if (x == null) x = BigDecimal.ZERO;
                return x;
            }

            BigDecimal parseExpression() {
                BigDecimal x = parseTerm();
                for (;;) {
                    if (eat('+')) {
                        BigDecimal y = parseTerm();
                        if (x == null) x = y;
                        else if (y != null) x = x.add(y, MathContext.DECIMAL128);
                    } else if (eat('-')) {
                        BigDecimal y = parseTerm();
                        if (x == null) x = (y != null) ? y.negate() : null;
                        else if (y != null) x = x.subtract(y, MathContext.DECIMAL128);
                    } else return x;
                }
            }

            BigDecimal parseTerm() {
                BigDecimal x = parseFactor();
                for (;;) {
                    if (eat('*')) {
                        BigDecimal y = parseFactor();
                        if (x == null) x = y;
                        else if (y != null) x = x.multiply(y, MathContext.DECIMAL128);
                    } else if (eat('/')) {
                        BigDecimal y = parseFactor();
                        if (x == null) x = y;
                        else if (y != null) {
                            if (y.compareTo(BigDecimal.ZERO) == 0) {
                                throw new ArithmeticException("Divide by zero");
                            }
                            x = x.divide(y, MathContext.DECIMAL128);
                        }
                    } else return x;
                }
            }

            BigDecimal parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) {
                    BigDecimal val = parseFactor();
                    return val != null ? val.negate() : null;
                }

                BigDecimal x;
                int startPos = this.pos;
                if (eat('(')) {
                    x = parseExpression();
                    eat(')');
                } else {
                    while ((ch >= '0' && ch <= '9') || ch == '.' || ch == 'E' || ch == 'e') {
                        nextChar();
                        if (ch == '-' || ch == '+') {
                            int prevPos = pos - 1;
                            if (prevPos >= 0 && (str.charAt(prevPos) == 'E' || str.charAt(prevPos) == 'e')) {
                                nextChar();
                            }
                        }
                    }
                    if (startPos == pos) {
                        return null;
                    }
                    x = new BigDecimal(str.substring(startPos, this.pos));
                }
                return x;
            }
        }.parse();
    }
}