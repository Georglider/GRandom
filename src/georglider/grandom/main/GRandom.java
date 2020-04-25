package georglider.grandom.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;


public class GRandom {
    private JButton generateButton;
    private JTextField T_Min;
    private JTextField T_Max;
    private JPanel JP1;
    private JTextField Quantity;

    public static void main(String[]args){
        ResourceBundle res = ResourceBundle.getBundle("georglider.grandom.lang.lang");
        //ResourceBundle res = ResourceBundle.getBundle("georglider.grandom.lang.lang_ru_RU");
        JFrame F = new JFrame(res.getString("GRandom"));
        F.setContentPane(new GRandom().JP1);
        F.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        F.pack();
        F.setVisible(true);
    }

    public GRandom() {
        ResourceBundle res = ResourceBundle.getBundle("georglider.grandom.lang.lang");
        //ResourceBundle res = ResourceBundle.getBundle("georglider.grandom.lang.lang_ru_RU");
        generateButton.setText(res.getString("GenerateTT"));
        generateButton.setToolTipText(res.getString("Generate"));
        T_Min.setText(res.getString("T_Min"));
        T_Min.setToolTipText(res.getString("T_Min"));
        T_Max.setText(res.getString("T_Max"));
        T_Max.setToolTipText(res.getString("T_Max"));
        Quantity.setText(res.getString("Quantity"));
        Quantity.setToolTipText(res.getString("Quantity"));

        UIManager.put("OptionPane.yesButtonText", res.getString("Yes"));
        UIManager.put("OptionPane.noButtonText", res.getString("No"));
        UIManager.put("OptionPane.cancelButtonText", res.getString("Cancel"));

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Random R = new Random();
                int min = Integer.parseInt(T_Min.getText());
                int max = Integer.parseInt(T_Max.getText());
                int int_q = Integer.parseInt(Quantity.getText());
                if (min > max) {
                    System.out.println(res.getString("Error"));
                    JOptionPane.showMessageDialog(null, (res.getString("MinimumGreaterThanMaximum")), (res.getString("Error")), JOptionPane.ERROR_MESSAGE);
                } else {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy MM dd HH mm ss");
                    LocalDateTime now = LocalDateTime.now();
                    System.out.println(dtf.format(now));

                    String pathbad = getClass().getResource("").getPath();
                    String path1 = pathbad.replace("file:/","");
                    String path2 = path1.replace(".jar","");
                    String path = path2.replace("!/georglider/grandom/main/","");
                    //new File(path + "/GRandom").mkdirs();

                    File out = new File(path + "/GRandom/" + dtf.format(now) + ".txt");

                    System.out.println(path);
                    for (int i = 1; i < int_q + 1; i++) {
                        int value = min + R.nextInt(1 + max - min);
                        System.out.println(i + " = " + value);

                        try {
                            FileWriter fw = new FileWriter(out, true);
                            fw.write(i + " = " + value + "\r\n");
                            fw.close();
                        } catch (Exception ex) {

                        }
                    }
                    int openfldr = 0;
                    openfldr = JOptionPane.showConfirmDialog(null, (res.getString("OpenFolder")),(res.getString("NumbersGenerated")), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (openfldr == JOptionPane.NO_OPTION){

                    }
                    else if (openfldr == JOptionPane.YES_OPTION){
                    try {
                    Desktop.getDesktop().open(new File(path+"/GRandom"));
                    } catch (Exception ex) {

                    }}
                }
            }
        });
        T_Max.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                T_Max.setText("");
            }
        });
        Quantity.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                Quantity.setText("");
            }
        });
        T_Min.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                T_Min.setText("");
            }
        });
    }}