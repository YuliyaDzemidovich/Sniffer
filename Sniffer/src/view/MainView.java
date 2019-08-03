package view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import controller.Sniffer;
import controller.SnifferConfig;

public class MainView extends JFrame {
    private static MainView mainView = new MainView();
    private static SnifferConfig snifferConfig = SnifferConfig.getInstance();
    private static Sniffer sniffer = Sniffer.getInstance();
    private JButton btnStart, btnStop;
    private JCheckBox chboxHttpProtocol, chbox2Protocol, chbox3Protocol;
    JTextArea outputTextArea;

    private MainView(){

    }

    public static MainView getInstance(){
        return mainView;
    }

    public void draw(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        chboxHttpProtocol = new JCheckBox("HTTP");
        chboxHttpProtocol.setSelected(true);
        add(chboxHttpProtocol);
        chboxHttpProtocol.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent event){
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    snifferConfig.setOnHTTPProtocol(true);
                } else {
                    snifferConfig.setOnHTTPProtocol(false);
                }
            }
        });

        chbox2Protocol = new JCheckBox("2ndProt");
        add(chbox2Protocol);
        chbox2Protocol.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent event){
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    snifferConfig.setOnThe2ndProtocol(true);
                } else {
                    snifferConfig.setOnThe2ndProtocol(false);
                }
            }
        });

        chbox3Protocol = new JCheckBox("3dProt");
        add(chbox3Protocol);
        chbox3Protocol.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent event){
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    snifferConfig.setOnThe3dProtocol(true);
                } else {
                    snifferConfig.setOnThe3dProtocol(false);
                }
            }
        });

        btnStart = new JButton("Start");
        add(btnStart);
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt){
                cleanOutputText();
                new Thread(new Runnable() {
                    @Override
                    public void run(){
                        sniffer.startSniffer();
                    }
                }).start();
            }

        });

        btnStop = new JButton("Stop");
        add(btnStop);
        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt){
                sniffer.stopSniffer();
            }
        });

        JPanel panel = new JPanel();
        outputTextArea = new JTextArea(10, 40); // 10 rows, 40 columns
        outputTextArea.setText("Press 'Start' button to start sniffer");
        outputTextArea.setLineWrap(true);
        outputTextArea.setEditable(false);
        JScrollPane areaScrollPane = new JScrollPane(outputTextArea);
        panel.add(areaScrollPane);
        this.add(panel);

        setTitle("Sniffer");
        setSize(500, 300);
        setVisible(true);
    }

    private void cleanOutputText(){
        outputTextArea.setText("");
    }

    public void updateOutputText(String packetStr){
        outputTextArea.append("\n\nNEXT PACKET:\n");
        outputTextArea.append(packetStr);
    }

}
