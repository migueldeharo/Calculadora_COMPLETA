package paqCalculadora.pCalculadora;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

public class Calculadora extends WindowAdapter implements ActionListener, KeyListener {

    private final JFrame f = new JFrame("Calculadora");
    private final JTextField t = new JTextField();
    private final JButton[] botones = {new JButton("1"), new JButton("2"), new JButton("3"), new JButton("4"),
            new JButton("5"), new JButton("6"), new JButton("7"), new JButton("8"),
            new JButton("9"), new JButton("0"), new JButton("."), new JButton("+"),
            new JButton("-"), new JButton("*"), new JButton("/"), new JButton("=")
    };
    private final JButton bdel = new JButton("Delete");
    private final JButton bclr = new JButton("Clear");
    private double a;
    private double b;
    private double resultado;
    private int operador;

    private JCheckBox cientifica = new JCheckBox("Cientifica");
    private JButton bPI = new JButton("PI");
    private JButton bCos = new JButton("Coseno");
    private JButton bAli = new JButton("Alicuota");

    public Calculadora() {

        a = b = resultado = operador = 0;

        t.setBounds(30, 40, 280, 30);
        f.add(t);
        int indice = 0;
        for (int x = 40; x <= 250; x = x + 70) {
            for (int y = 100; y <= 310; y = y + 70) {
                botones[indice].setBounds(x, y, 50, 40);
                f.add(botones[indice]);
                indice++;
            }
        }

        bdel.setBounds(60, 380, 100, 40);
        bclr.setBounds(180, 380, 100, 40);
        f.add(bdel);
        f.add(bclr);

        f.setLayout(null);
        f.setVisible(true);
        f.setSize(350, 500);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Por defecto se usa HIDE_ON_CLOSE y el programa sigue activo en memoria
        f.setResizable(false);
        Image icon = Toolkit.getDefaultToolkit().getImage("icon.png");
        f.setIconImage(icon);
        f.setLocationRelativeTo(null); //se posiciona en el centro de la pantalla

        cientifica.setBounds(30, 0, 100, 40);
        f.add(cientifica);

        bPI.setBounds(60, 440, 100, 40);
        f.add(bPI);
        bPI.setVisible(false);

        bCos.setBounds(180, 440, 100, 40);
        f.add(bCos);
        bCos.setVisible(false);

        bAli.setBounds(60, 500, 100, 40);
        f.add(bAli);
        bAli.setVisible(false);

        // AÑADE LOS LISTENERS
        for (JButton b : botones) {
            b.addActionListener(this);
        }
        bdel.addActionListener(this);
        bclr.addActionListener(this);
        bAli.addActionListener(this);
        bPI.addActionListener(this);
        bCos.addActionListener(this);

        cientifica.addChangeListener(new Listener());

        t.addKeyListener(this);

        t.requestFocus();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // BOTONES CALCULADORA
        try {
            for (int i = 0; i < 11; i++) {
                if (e.getSource() == botones[i]) {
                    t.setText(t.getText().concat(botones[i].getText()));
                }
            }

            if (e.getSource() == botones[11]) {                      // suma
                operador = 1;
                a = Double.parseDouble(t.getText());
                t.setText("");
            }
            if (e.getSource() == botones[12]) {                      // resta
                operador = 2;
                a = Double.parseDouble(t.getText());
                t.setText("");
            }
            if (e.getSource() == botones[13]) {                      // multiplicacion
                operador = 3;
                a = Double.parseDouble(t.getText());
                t.setText("");
            }
            if (e.getSource() == botones[14]) {                      // division
                operador = 4;
                a = Double.parseDouble(t.getText());
                t.setText("");
            }
            if (e.getSource() == botones[15]) {                      // calcular
                try {
                    operar();
                } catch (NANException ex) {
                    t.setText("Resultado indefinido");
                }

            }

        } catch (NumberFormatException ex) {

        }

        // RESTO COMPONENTES
        if (e.getSource() == bdel) {
            try {
                StringBuilder s = new StringBuilder(t.getText());
                int index = s.length() - 1;
                s.deleteCharAt(index);
                t.setText(s.toString());
            } catch (StringIndexOutOfBoundsException ex) {

            }
        }

        if (e.getSource() == bclr) {
            t.setText("");
        }

        if (e.getSource() == bPI) {
            t.setText(Double.toString(Math.PI));
        }

        if (e.getSource() == bCos) {
            try {
                double x = Double.parseDouble(t.getText());
                double res = 1.0;
                double ter = 1.0;

                for (int i = 1; i <= 100; i++) {
                    ter *= -x * x / ((2 * i) * (2 * i - 1));
                    res += ter;
                }

                t.setText(Double.toString(res));

            } catch (NumberFormatException ex) {

            }
        }

        if (e.getSource() == bAli) {
            try {
                t.setText(alicuota(Integer.parseInt(t.getText())));
            } catch (NumberFormatException ex) {

            }
        }

        t.requestFocus();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                operar();
            } catch (NANException ex) {
                t.setText("Resultado indefinido");
            } catch (NumberFormatException ex) {

            }
        }
        if (e.getKeyCode() == KeyEvent.VK_F1) {
            if (cientifica.isSelected()) {
                try {
                    t.setText(alicuota(Integer.parseInt(t.getText())));
                } catch (NumberFormatException ex) {

                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        String v[] = {"Si", "No"};
        int PromptResult = JOptionPane.showOptionDialog(null, "Estas seguro de cerrar", "Confirmar", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, v, v[1]);
        switch (PromptResult) {
            case JOptionPane.YES_OPTION:
                System.exit(0);
            case JOptionPane.NO_OPTION:
                f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        }
    }

    private class Listener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            if (cientifica.isSelected()) {
                bPI.setVisible(true);
                bCos.setVisible(true);
                bAli.setVisible(true);
                f.setSize(350, 600);
            } else {
                bPI.setVisible(false);
                bCos.setVisible(false);
                bAli.setVisible(false);
                f.setSize(350, 500);
            }
        }
    }

    // METODOS PRIVADOS
    private void operar() throws NANException {
        try {
            b = Double.parseDouble(t.getText());

            switch (operador) {
                case 1:
                    resultado = a + b;
                    break;
                case 2:
                    resultado = a - b;
                    break;
                case 3:
                    resultado = a * b;
                    break;
                case 4:
                    resultado = a / b;
                    if (Double.isNaN(resultado)) {
                        throw new NANException();
                    }
                    break;
            }

            t.setText(Double.toString(resultado));
        } catch (ArithmeticException ex) {
            t.setText("Division entre 0");
        } catch (NumberFormatException ex) {

        }
    }

    private String alicuota(int n) {
        if (n < 10) {
            return "1";
        } else {
            String s = "" + n;

            for (int c = 1; c < 5; c++) {
                int sum = 0;
                for (int i = 1; i <= n / 2; i++) {
                    if (n % i == 0) {
                        sum += i;
                    }
                }
                s += ", " + sum;
                n = sum;
            }
            return s;
        }

    }
}

