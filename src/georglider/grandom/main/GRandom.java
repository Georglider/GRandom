package georglider.grandom.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.*;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Timer;

public class GRandom extends JFrame {
    private JButton generateButton;
    private JTextField T_Min;
    private JTextField T_Max;
    private JPanel JP1;
    private JTextField Quantity;
    private JProgressBar progress;
    private JPanel JP_Progress;
    private JPanel J_Main;
    private JPanel JP2;
    int seed;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GRandom frame = new GRandom();
                    frame.setVisible(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public boolean T_Min_active;
    public boolean T_Max_active;
    public boolean Quantity_active;
    public boolean generating;
    private Icon createIcon() {
        Icon dicon = new Icon() {
            @Override
            public void paintIcon(Component c,
                                  Graphics g, int x, int y) {

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

        return dicon;
    }
    public GRandom() {
        ResourceBundle res = ResourceBundle.getBundle("georglider.grandom.lang.lang");
        JFrame F = new JFrame(res.getString("GRandom"));
        F.setContentPane(JP1);
        F.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        F.pack();
        F.setVisible(true);
        F.setSize(300,163);
        F.setResizable(false);
        JMenuBar gmenu = new JMenuBar();

        JMenu Mode = new JMenu((res.getString("Mode")));
        JMenu Display = new JMenu((res.getString("Display")));
        JMenu GenerateOptions = new JMenu((res.getString("GenerateOptions")));

        // M = Menu | D = Display | GO = GenerateOptions
        JRadioButtonMenuItem Mnumbers = new JRadioButtonMenuItem((res.getString("Mnumbers")), createIcon(), true);
        JRadioButtonMenuItem Mstring = new JRadioButtonMenuItem((res.getString("Mstring")));

        JRadioButtonMenuItem Ddefault = new JRadioButtonMenuItem((res.getString("Ddefault")), createIcon(), true);
        JRadioButtonMenuItem Dopen = new JRadioButtonMenuItem((res.getString("Dopen")));
        JRadioButtonMenuItem Dshowhere = new JRadioButtonMenuItem((res.getString("Dshowhere")));

        JRadioButtonMenuItem GOninclude = new JRadioButtonMenuItem((res.getString("GOninclude")));

        Mode.add(Mnumbers);
        Mode.add(Mstring);
        Display.add(Ddefault);
        Display.add(Dopen);
        Display.add(Dshowhere);
        GenerateOptions.add(GOninclude);

        gmenu.add(Mode);
        gmenu.add(Display);
        gmenu.add(GenerateOptions);
        F.setJMenuBar(gmenu);

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

        //Seed list https://github.com/Georglider/GRandom/wiki/Seed-list

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Setting up seed
                getSeed();
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
            public void getSeed() {
                //1 option chosen (6)
                if (Mnumbers.isSelected()) { seed=1; }
                if (Mstring.isSelected()) {seed=2;}
                if (Ddefault.isSelected()) {seed=3;}
                if (Dopen.isSelected()) {seed=4;}
                if (Dshowhere.isSelected()) {seed=5;}
                if (GOninclude.isSelected()) {seed=6;}
                //2 options chosen (15)
                if (Mnumbers.isSelected()&&Mstring.isSelected()) {seed=7;}
                if (Mnumbers.isSelected()&&Ddefault.isSelected()) {seed=8;}
                if (Mnumbers.isSelected()&&Dopen.isSelected()) {seed=9;}
                if (Mnumbers.isSelected()&&Dshowhere.isSelected()) {seed=10;}
                if (Mnumbers.isSelected()&&GOninclude.isSelected()) {seed=11;}
                if (Mstring.isSelected()&&Ddefault.isSelected()) {seed=12;}
                if (Mstring.isSelected()&&Ddefault.isSelected()) {seed=13;}
                if (Mstring.isSelected()&&Dopen.isSelected()) {seed=14;}
                if (Mstring.isSelected()&&Dshowhere.isSelected()) {seed=15;}
                if (Mstring.isSelected()&&GOninclude.isSelected()) {seed=16;}
                if (Ddefault.isSelected()&&Dopen.isSelected()) {seed=17;}
                if (Ddefault.isSelected()&&Dshowhere.isSelected()) {seed=18;}
                if (Ddefault.isSelected()&&GOninclude.isSelected()) {seed=19;}
                if (Dopen.isSelected()&&Dshowhere.isSelected()) {seed=20;}
                if (Dopen.isSelected()&&GOninclude.isSelected()) {seed=21;}
                if (Dshowhere.isSelected()&&GOninclude.isSelected()) {seed=22;}
                //3 Options chosen (9)
                if (Mnumbers.isSelected()&&Mstring.isSelected()&&Ddefault.isSelected()) {seed=23;}
                if (Mnumbers.isSelected()&&Mstring.isSelected()&&Dopen.isSelected()) {seed=24;}
                if (Mnumbers.isSelected()&&Mstring.isSelected()&&Dshowhere.isSelected()) {seed=25;}
                if (Mnumbers.isSelected()&&Mstring.isSelected()&&GOninclude.isSelected()) {seed=26;}
                if (Mnumbers.isSelected()&&Dopen.isSelected()&&Ddefault.isSelected()) {seed=27;}
                if (Mnumbers.isSelected()&&Dopen.isSelected()&&Dshowhere.isSelected()) {seed=28;}
                if (Mnumbers.isSelected()&&Dopen.isSelected()&&GOninclude.isSelected()) {seed=29;}
                if (Mnumbers.isSelected()&&Dshowhere.isSelected()&&GOninclude.isSelected()) {seed=30;}
                if (Mnumbers.isSelected()&&Dshowhere.isSelected()&&Ddefault.isSelected()) {seed=31;}
                if (Ddefault.isSelected()&&GOninclude.isSelected()&&Mnumbers.isSelected()) {seed=32;}
                //4 Options chosen (12)
                if (Mnumbers.isSelected()&&Mstring.isSelected()&&Ddefault.isSelected()&&Dopen.isSelected()) {seed=33;}
                if (Mnumbers.isSelected()&&Mstring.isSelected()&&Ddefault.isSelected()&&Dshowhere.isSelected()) {seed=34;}
                if (Mnumbers.isSelected()&&Mstring.isSelected()&&Ddefault.isSelected()&&GOninclude.isSelected()) {seed=35;}
                if (Mnumbers.isSelected()&&Mstring.isSelected()&&Dopen.isSelected()&&Dshowhere.isSelected()) {seed=36;}
                if (Mnumbers.isSelected()&&Mstring.isSelected()&&Dopen.isSelected()&&GOninclude.isSelected()) {seed=37;}
                if (GOninclude.isSelected()&&Ddefault.isSelected()&&Dopen.isSelected()&&Mnumbers.isSelected()) {seed=38;}
                if (GOninclude.isSelected()&&Dshowhere.isSelected()&&Dopen.isSelected()&&Mnumbers.isSelected()) {seed=39;}
                if (GOninclude.isSelected()&&Dshowhere.isSelected()&&Ddefault.isSelected()&&Mnumbers.isSelected()) {seed=40;}
                if (GOninclude.isSelected()&&Ddefault.isSelected()&&Dopen.isSelected()&&Mstring.isSelected()) {seed=41;}
                if (GOninclude.isSelected()&&Dshowhere.isSelected()&&Dopen.isSelected()&&Mstring.isSelected()) {seed=42;}
                if (GOninclude.isSelected()&&Dshowhere.isSelected()&&Ddefault.isSelected()&&Mstring.isSelected()) {seed=43;}
                if (Dopen.isSelected()&&Dshowhere.isSelected()&&Ddefault.isSelected()&&Mstring.isSelected()) {seed=44;}
                if (Dopen.isSelected()&&Dshowhere.isSelected()&&Ddefault.isSelected()&&Mnumbers.isSelected()) {seed=45;}

                System.out.println("Seed: "+seed);
            }
        });

        T_Max.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if (T_Max_active==false) {
                    T_Max.setText("");
                    T_Max_active=true;
                } else{}
            }
        });
        Quantity.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if (Quantity_active==false) {
                    Quantity.setText("");
                    Quantity_active=true;
                } else{}
            }
        });
        T_Min.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if (T_Min_active==false) {
                    T_Min.setText("");
                    T_Min_active=true;
                } else{}
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