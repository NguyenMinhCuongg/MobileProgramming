package ute.buiquanghuy.maytinh;

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

    private TextView tvExpression;
    private TextView tvResult;

    private String currentExpression = "";
    private boolean resultIsShowing = false;

    private static final MathContext MC = MathContext.DECIMAL128;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupCalculatorButtons();
    }

    private void initViews() {
        tvExpression = findViewById(R.id.tvExpression);
        tvResult = findViewById(R.id.tvResult);
    }

    private void setupCalculatorButtons() {
        GridLayout mainGrid = findViewById(R.id.mainGrid);

        if (mainGrid == null) {
            return;
        }

        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            View child = mainGrid.getChildAt(i);

            if (child instanceof Button) {
                child.setOnClickListener(view -> {
                    Button button = (Button) view;
                    String value = button.getText().toString();
                    processButton(value);
                });
            }
        }
    }

    private void processButton(String value) {
        if (value.equals("AC")) {
            clearCalculator();
            return;
        }

        if (value.equals("⌫")) {
            deleteLastCharacter();
            return;
        }

        if (value.equals("( )")) {
            addParenthesis();
            return;
        }

        if (value.equals("=")) {
            showFinalResult();
            return;
        }

        if (isOperator(value)) {
            addOperator(value);
            return;
        }

        addNumberOrDot(value);
    }

    private void clearCalculator() {
        currentExpression = "";
        resultIsShowing = false;

        tvExpression.setText("");
        tvResult.setText("0");
    }

    private void deleteLastCharacter() {
        if (resultIsShowing) {
            resultIsShowing = false;
        } else if (!currentExpression.isEmpty()) {
            currentExpression = currentExpression.substring(0, currentExpression.length() - 1);
        }

        refreshExpression();
        calculatePreview();
    }

    private void addParenthesis() {
        if (resultIsShowing) {
            currentExpression = "";
            resultIsShowing = false;
        }

        int openCount = countCharacter(currentExpression, '(');
        int closeCount = countCharacter(currentExpression, ')');

        if (openCount == closeCount || currentExpression.endsWith("(")) {
            currentExpression += "(";
        } else {
            currentExpression += ")";
        }

        refreshExpression();
        calculatePreview();
    }

    private int countCharacter(String text, char target) {
        int count = 0;

        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == target) {
                count++;
            }
        }

        return count;
    }

    private void showFinalResult() {
        if (currentExpression.isEmpty()) {
            return;
        }

        try {
            String convertedExpression = convertExpression(currentExpression);
            BigDecimal result = ExpressionParser.calculate(convertedExpression);

            currentExpression = formatNumber(result);
            resultIsShowing = true;

            tvExpression.setText(currentExpression);
            tvResult.setText("");
        } catch (Exception e) {
            tvResult.setText("Error");
        }
    }

    private void addOperator(String operator) {
        resultIsShowing = false;

        if (currentExpression.isEmpty()) {
            if (operator.equals("-")) {
                currentExpression = "-";
                refreshExpression();
            }
            return;
        }

        char lastChar = currentExpression.charAt(currentExpression.length() - 1);

        /*
         * Trường hợp đặc biệt:
         * Nếu phía trước là dấu "(" thì không được xóa dấu "(".
         * Chỉ cho phép thêm dấu + hoặc - sau "(".
         *
         * Ví dụ hợp lệ:
         * 5×(-3)
         * 5×(+3)
         */
        if (lastChar == '(') {
            if (operator.equals("+") || operator.equals("-")) {
                currentExpression += operator;
                refreshExpression();
            }
            return;
        }

        /*
         * Nếu ký tự cuối là toán tử thật sự thì thay toán tử cũ.
         * Lưu ý: không tính dấu "(" là toán tử để tránh lỗi mất ngoặc.
         */
        if (isLastCharOperator(lastChar)) {
            currentExpression = currentExpression.substring(0, currentExpression.length() - 1);
        }

        currentExpression += operator;
        refreshExpression();
    }

    private void addNumberOrDot(String value) {
        if (resultIsShowing) {
            currentExpression = value;
            resultIsShowing = false;
        } else {
            currentExpression += value;
        }

        refreshExpression();
        calculatePreview();
    }

    private boolean isOperator(String value) {
        return value.equals("+")
                || value.equals("-")
                || value.equals("×")
                || value.equals("÷")
                || value.equals("%");
    }

    private boolean isLastCharOperator(char c) {
        return c == '+'
                || c == '-'
                || c == '×'
                || c == '÷'
                || c == '%';
    }

    private void refreshExpression() {
        tvExpression.setText(currentExpression);
    }

    private void calculatePreview() {
        if (currentExpression.isEmpty()) {
            tvResult.setText("0");
            return;
        }

        try {
            String convertedExpression = convertExpression(currentExpression);
            BigDecimal result = ExpressionParser.calculate(convertedExpression);

            tvResult.setText(formatNumber(result));
        } catch (Exception e) {
            tvResult.setText("");
        }
    }

    private String convertExpression(String expression) {
        String newExpression = expression;

        newExpression = newExpression.replaceAll("([0-9.E+-]+)%", "($1/100.0)");
        newExpression = newExpression.replace("×", "*");
        newExpression = newExpression.replace("÷", "/");

        return newExpression;
    }

    private String formatNumber(BigDecimal number) {
        if (number.compareTo(BigDecimal.ZERO) == 0) {
            return "0";
        }

        BigDecimal cleanNumber = number.stripTrailingZeros();
        String plainText = cleanNumber.toPlainString();

        if (plainText.length() > 15) {
            DecimalFormat formatter = new DecimalFormat(
                    "0.########E0",
                    new DecimalFormatSymbols(Locale.US)
            );
            return formatter.format(cleanNumber);
        }

        return plainText;
    }

    private static class ExpressionParser {

        private final String input;
        private int index;
        private char currentChar;

        private ExpressionParser(String input) {
            this.input = input;
            this.index = -1;
            moveNext();
        }

        public static BigDecimal calculate(String input) {
            ExpressionParser parser = new ExpressionParser(input);
            BigDecimal value = parser.parseExpression();

            if (value == null) {
                return BigDecimal.ZERO;
            }

            return value;
        }

        private void moveNext() {
            index++;

            if (index < input.length()) {
                currentChar = input.charAt(index);
            } else {
                currentChar = '\0';
            }
        }

        private void skipSpaces() {
            while (currentChar == ' ') {
                moveNext();
            }
        }

        private boolean accept(char expected) {
            skipSpaces();

            if (currentChar == expected) {
                moveNext();
                return true;
            }

            return false;
        }

        private BigDecimal parseExpression() {
            BigDecimal result = parseTerm();

            while (true) {
                if (accept('+')) {
                    BigDecimal nextValue = parseTerm();

                    if (result == null) {
                        result = nextValue;
                    } else if (nextValue != null) {
                        result = result.add(nextValue, MC);
                    }

                } else if (accept('-')) {
                    BigDecimal nextValue = parseTerm();

                    if (result == null) {
                        result = nextValue == null ? null : nextValue.negate();
                    } else if (nextValue != null) {
                        result = result.subtract(nextValue, MC);
                    }

                } else {
                    return result;
                }
            }
        }

        private BigDecimal parseTerm() {
            BigDecimal result = parseFactor();

            while (true) {
                if (accept('*')) {
                    BigDecimal nextValue = parseFactor();

                    if (result == null) {
                        result = nextValue;
                    } else if (nextValue != null) {
                        result = result.multiply(nextValue, MC);
                    }

                } else if (accept('/')) {
                    BigDecimal nextValue = parseFactor();

                    if (result == null) {
                        result = nextValue;
                    } else if (nextValue != null) {
                        if (nextValue.compareTo(BigDecimal.ZERO) == 0) {
                            throw new ArithmeticException("Divide by zero");
                        }

                        result = result.divide(nextValue, MC);
                    }

                } else {
                    return result;
                }
            }
        }

        private BigDecimal parseFactor() {
            if (accept('+')) {
                return parseFactor();
            }

            if (accept('-')) {
                BigDecimal value = parseFactor();
                return value == null ? null : value.negate();
            }

            if (accept('(')) {
                BigDecimal insideValue = parseExpression();
                accept(')');
                return insideValue;
            }

            return parseNumber();
        }

        private BigDecimal parseNumber() {
            int startIndex = index;

            while (isNumberCharacter(currentChar)) {
                moveNext();

                if (currentChar == '+' || currentChar == '-') {
                    int previousIndex = index - 1;

                    if (previousIndex >= 0) {
                        char previousChar = input.charAt(previousIndex);

                        if (previousChar == 'E' || previousChar == 'e') {
                            moveNext();
                        }
                    }
                }
            }

            if (startIndex == index) {
                return null;
            }

            String numberText = input.substring(startIndex, index);
            return new BigDecimal(numberText);
        }

        private boolean isNumberCharacter(char c) {
            return (c >= '0' && c <= '9')
                    || c == '.'
                    || c == 'E'
                    || c == 'e';
        }
    }
}