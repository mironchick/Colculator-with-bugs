import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Colculator extends JFrame {
    private JTextField display = new JTextField("0");
    private JLabel historyLabel = new JLabel("");
    private double num1 = 0;
    private String op = "";
    private boolean readyForNew = true;
    private int operationCount = 0;

    public Colculator() {
        setTitle("Колкулатор");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel displayPanel = new JPanel(new BorderLayout());
        
        display.setFont(new Font("Arial", Font.BOLD, 24));
        display.setEditable(false);
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setPreferredSize(new Dimension(0, 80));
        display.setBackground(Color.WHITE);
        display.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 15));
        displayPanel.add(display, BorderLayout.CENTER);

        historyLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        historyLabel.setForeground(Color.GRAY);
        historyLabel.setHorizontalAlignment(JLabel.RIGHT);
        historyLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 5, 15));
        displayPanel.add(historyLabel, BorderLayout.SOUTH);

        add(displayPanel, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(5, 4, 12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 15, 10));

        panel.add(createButton("7")); panel.add(createButton("8")); panel.add(createButton("9")); panel.add(createButton("/"));
        panel.add(createButton("4")); panel.add(createButton("5")); panel.add(createButton("6")); panel.add(createButton("*"));
        panel.add(createButton("1")); panel.add(createButton("2")); panel.add(createButton("3")); panel.add(createButton("-"));
        panel.add(createButton("C")); panel.add(createButton("0")); panel.add(createButton(".")); panel.add(createButton("+"));
        panel.add(new JLabel());
        panel.add(new JLabel());
        panel.add(new JLabel());
        panel.add(createButton("="));

        add(panel, BorderLayout.CENTER);
        setSize(340, 540);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(240, 240, 240));
        btn.setBorder(BorderFactory.createRaisedBevelBorder());
        btn.addActionListener(e -> onButtonClick(text));
        return btn;
    }

    private void onButtonClick(String btn) {
        if (btn.equals("C")) {
            display.setText("0");
            historyLabel.setText("");
            num1 = 0;
            op = "";
            readyForNew = true;
            return;
        }

        if (btn.matches("[0-9]")) {
            if (readyForNew) {
                display.setText(btn);
                readyForNew = false;
            } else {
                String current = display.getText();
                display.setText(current.equals("0") ? btn : current + btn);
            }
            if (!op.isEmpty()) {
                historyLabel.setText(num1 + " " + op + " " + display.getText());
            }
        } else if (btn.equals(".")) {
            if (readyForNew) {
                display.setText("0.");
                readyForNew = false;
            } else if (!display.getText().contains(".")) {
                display.setText(display.getText() + ".");
            }
            if (!op.isEmpty()) {
                historyLabel.setText(num1 + " " + op + " " + display.getText());
            }
        } else if (btn.equals("=")) {
            double num2;
            try {
                num2 = Double.parseDouble(display.getText());
            } catch (NumberFormatException ex) {
                display.setText("Error");
                historyLabel.setText("");
                readyForNew = true;
                return;
            }

            String currentExpression = num1 + " " + op + " " + num2;

            operationCount++;

            if (operationCount % 5 == 0) {
                display.setText("пора отдыхать");
                historyLabel.setText(currentExpression);
                readyForNew = true;
                return;
            }

            if (op.equals("/") && num2 == 0) {
                display.setText("42");
                historyLabel.setText(currentExpression);
                readyForNew = true;
                return;
            }

            double res = 0;
            switch (op) {
                case "+": res = num1 + num2; break;
                case "-": res = num1 - num2; break;
                case "*": res = num1 * num2; break;
                case "/": res = num1 / num2; break;
                default: res = num2;
            }

            double orig1 = num1, orig2 = num2;
            if (num1 == 13) num1 = 12;
            if (num2 == 13) num2 = 12;
            if (orig1 == 13 || orig2 == 13) {
                switch (op) {
                    case "+": res = num1 + num2; break;
                    case "-": res = num1 - num2; break;
                    case "*": res = num1 * num2; break;
                    case "/": res = num1 / num2; break;
                    default: res = num2;
                }
            }

            if (res == (long) res) {
                display.setText(String.valueOf((long) res));
            } else {
                display.setText(String.valueOf(res));
            }

            historyLabel.setText(currentExpression);
            readyForNew = true;
        } else {
            try {
                num1 = Double.parseDouble(display.getText());
            } catch (Exception ex) {
                num1 = 0;
            }
            op = btn;
            readyForNew = true;
            historyLabel.setText(num1 + " " + op);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Colculator().setVisible(true);
        });
    }
}