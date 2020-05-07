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
import java.util.*;
import java.util.Timer;

public class GRandom {
    private JButton generateButton;
    private JTextField T_Min;
    private JTextField T_Max;
    private JPanel JP1;
    private JTextField Quantity;
    private JProgressBar progress;
    private JPanel JP_Progress;
    private JPanel J_Main;
    private JPanel JP2;


    public static void main(String[] args){
        ResourceBundle res = ResourceBundle.getBundle("georglider.grandom.lang.lang");
        //ResourceBundle res = ResourceBundle.getBundle("georglider.grandom.lang.lang_ru_RU");
        JFrame F = new JFrame(res.getString("GRandom"));
        F.setContentPane(new GRandom().JP1);
        F.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        F.pack();
        F.setVisible(true);
        F.setSize(300,158);
        F.setResizable(false);

        JMenuBar gmenu = new JMenuBar();

        JMenu Mode = new JMenu("Режим");
        JMenu Display = new JMenu("После генерации");
        JMenu GenerateOptions = new JMenu("Опции для генерации");

        gmenu.add(Mode);
        gmenu.add(Display);
        gmenu.add(GenerateOptions);

        Icon dicon = new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {

            }

            @Override
            public int getIconWidth() {
                return 0;
            }

            @Override
            public int getIconHeight() {
                return 0;
            }
        };

        //M = Menu | D = Display | GO = GenerateOptions
        JRadioButtonMenuItem Mnumbers = new JRadioButtonMenuItem("Генерировать числа",dicon,true);
        JRadioButtonMenuItem Mstring = new JRadioButtonMenuItem("Генерировать заданные строки");

        JRadioButtonMenuItem Ddefault = new JRadioButtonMenuItem("По умолчанию",dicon,true);
        JRadioButtonMenuItem Dopen = new JRadioButtonMenuItem("Открыть файл");
        JRadioButtonMenuItem Dshowhere = new JRadioButtonMenuItem("Показать здесь");

        JRadioButtonMenuItem GOninclude = new JRadioButtonMenuItem("Не включать числа");

        Mode.add(Mnumbers);
        Mode.add(Mstring);
        Display.add(Ddefault);
        Display.add(Dopen);
        Display.add(Dshowhere);
        GenerateOptions.add(GOninclude);

        F.setJMenuBar(gmenu);
    }
    public boolean generating;
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
                } else if (int_q>2147483646) {
                    System.out.println(res.getString("Error"));
                    JOptionPane.showMessageDialog(null, (res.getString("JavaLimitationsQ")), (res.getString("Error")), JOptionPane.ERROR_MESSAGE);
                } else if (int_q<1) {
                    JOptionPane.showMessageDialog(null, (res.getString("MinimalNumber")), (res.getString("Error")), JOptionPane.ERROR_MESSAGE);
                } else if (min<-2147483641) {
                    JOptionPane.showMessageDialog(null, (res.getString("JavaLimitationsMin")), (res.getString("Error")), JOptionPane.ERROR_MESSAGE);
                } else if (min>2147483646) {
                    JOptionPane.showMessageDialog(null, (res.getString("JavaLimitationsMax")), (res.getString("Error")), JOptionPane.ERROR_MESSAGE);
                } else if (max<-2147483641) {
                    JOptionPane.showMessageDialog(null, (res.getString("JavaLimitationsMin")), (res.getString("Error")), JOptionPane.ERROR_MESSAGE);
                } else if (max>2147483646) {
                    JOptionPane.showMessageDialog(null, (res.getString("JavaLimitationsMax")), (res.getString("Error")), JOptionPane.ERROR_MESSAGE);
                }
                else {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy MM dd HH mm ss");
                    LocalDateTime now = LocalDateTime.now();
                    System.out.println(dtf.format(now));

                    String path = "";
                    new File(path + "\\GRandom").mkdirs();
                    File out = new File(path + "\\GRandom\\" + dtf.format(now) + ".txt");

                    System.out.println(path);

                    Timer timer = new Timer();
                    int update = int_q/1250;
                    if (int_q>1250) {
                        update = int_q/1250;
                    }
                    else {
                        update = 50;
                    }
                    timer.schedule(new UpdatePercent(),0,update);
                    boolean generating = false;
                    for (int i = 1; i < int_q + 1; i++) {
                        int value = min + R.nextInt(1 + max - min);
                        System.out.println(i + " = " + value);
                        try {
                            FileWriter fw = new FileWriter(out, true);
                            fw.write(i + " = " + value + "\r\n");
                            fw.close();
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, (res.getString("UnableToTypeInFile")), (res.getString("Error")), JOptionPane.ERROR_MESSAGE);
                        }
                            generating=true;
                            progress.setMaximum(int_q);
                            progress.setValue(i);
                        if (i==int_q) {
                            generating=false;
                            timer.cancel();
                        }
                        else {}
                    }
                    int openfldr = 0;
                    openfldr = JOptionPane.showConfirmDialog(null, (res.getString("OpenFolder")),(res.getString("NumbersGenerated")), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (openfldr == JOptionPane.NO_OPTION){

                    }
                    else if (openfldr == JOptionPane.YES_OPTION){
                    try {
                    Desktop.getDesktop().open(new File(path+"\\GRandom\\"));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, (res.getString("UnableToOpenFolder")), (res.getString("Error")), JOptionPane.ERROR_MESSAGE);
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
        class UpdatePercent extends TimerTask {
            public void run() {
                progress.update(progress.getGraphics());
            }
    }}

    public class UpdatePercent extends TimerTask {
        public void run() {
            if (generating=true) {
                progress.update(progress.getGraphics());
            }
            else {}
            }
        }
    }