package ute.buiquanghuy.maytinh;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends Activity {

    TextView tvExpression, tvResult;
    ImageView btnHistory;
    String expression = "";
    boolean isResultShown = false;
    ArrayList<String> historyList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvExpression = findViewById(R.id.tvExpression);
        tvResult = findViewById(R.id.tvResult);
        btnHistory = findViewById(R.id.btnHistory);
        GridLayout grid = findViewById(R.id.mainGrid);

        btnHistory.setOnClickListener(v -> showHistoryDialog());

        for (int i = 0; i < grid.getChildCount(); i++) {
            View v = grid.getChildAt(i);
            if (v instanceof Button) {
                v.setOnClickListener(this::onClick);
            }
        }
    }

    private void showHistoryDialog() {
        if (historyList.isEmpty()) {
            Toast.makeText(this, "Chưa có lịch sử tính toán", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] historyArray = historyList.toArray(new String[0]);
        new AlertDialog.Builder(this)
                .setTitle("Lịch sử tính toán")
                .setItems(historyArray, (dialog, which) -> {
                    String selected = historyArray[which];
                    String resultOnly = selected.substring(selected.lastIndexOf("=") + 1).trim();
                    expression = resultOnly;
                    tvExpression.setText(expression);
                    tvResult.setText("");
                    isResultShown = true;
                })
                .setPositiveButton("Đóng", null)
                .setNeutralButton("Xóa lịch sử", (dialog, which) -> {
                    historyList.clear();
                    Toast.makeText(this, "Đã xóa lịch sử", Toast.LENGTH_SHORT).show();
                })
                .show();
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
                    if (!expression.isEmpty()) {
                        char last = expression.charAt(expression.length() - 1);
                        if (last == ')' && !text.equals(".")) {
                            expression += "×";
                        }
                    }
                    expression += text;
                }
                tvExpression.setText(expression);
                liveCalculate();
                break;
        }
    }

    private void handleParentheses() {
        if (isResultShown) {
            expression = "(";
            isResultShown = false;
            tvExpression.setText(expression);
            liveCalculate();
            return;
        }

        int open = 0, close = 0;
        for (char c : expression.toCharArray()) {
            if (c == '(') open++;
            if (c == ')') close++;
        }

        if (expression.isEmpty()) {
            expression += "(";
        } else {
            char last = expression.charAt(expression.length() - 1);

            if (open > close && (Character.isDigit(last) || last == ')' || last == '.')) {
                expression += ")";
            } else {
                if (Character.isDigit(last) || last == ')' || last == '.') {
                    expression += "×(";
                } else {
                    expression += "(";
                }
            }
        }
        tvExpression.setText(expression);
        liveCalculate();
    }

    private void handleOperator(String op) {
        if (!expression.isEmpty()) {
            char last = expression.charAt(expression.length() - 1);

            if (last == '(') {
                if (op.equals("-")) {
                    expression += op;
                    tvExpression.setText(expression);
                }
                return;
            }

            if ("+-×÷%".indexOf(last) != -1) {
                expression = expression.substring(0, expression.length() - 1);
            }
            expression += op;
            tvExpression.setText(expression);
        } else if (op.equals("-")) {
            expression += op;
            tvExpression.setText(expression);
        }
    }

    private void liveCalculate() {
        if (expression.isEmpty()) {
            tvResult.setText("0");
            return;
        }
        try {
            double result = eval(formatExpression(expression));
            tvResult.setText(result == (long) result ? String.valueOf((long) result) : String.valueOf(result));
        } catch (Exception e) {
            tvResult.setText("");
        }
    }

    private void calculateFinal() {
        try {
            double result = eval(formatExpression(expression));
            String finalRes = result == (long) result ? String.valueOf((long) result) : String.valueOf(result);
            historyList.add(0, expression + " = " + finalRes);
            expression = finalRes;
            tvExpression.setText(expression);
            tvResult.setText("");
        } catch (Exception e) {
            tvResult.setText("Error");
        }
    }

    private String formatExpression(String exp) {
        return exp.replaceAll("(\\d+)%", "($1/100.0)").replace("×", "*").replace("÷", "/");
    }

    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;
            void nextChar() { ch = (++pos < str.length()) ? str.charAt(pos) : -1; }
            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) { nextChar(); return true; }
                return false;
            }
            double parse() { nextChar(); return parseExpression(); }
            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }
            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if (eat('*')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else return x;
                }
            }
            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();
                double x;
                int startPos = this.pos;
                if (eat('(')) { x = parseExpression(); eat(')'); }
                else {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                }
                return x;
            }
        }.parse();
    }
}