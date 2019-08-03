package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import controller.Sniffer;

public class SelectView extends JFrame {
    private static SelectView selectView = new SelectView();
    private static MainView mainView = MainView.getInstance();
    private static Sniffer sniffer = Sniffer.getInstance();
    private JButton btnSubmit;

    private SelectView(){

    }

    public static SelectView getInstance(){
        return selectView;
    }

    public void draw(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel outputPanel = new JPanel();
        JTextArea outputTextArea = new JTextArea(7, 40); // 7 rows, 40 columns
        outputTextArea.setText(sniffer.getDevicesList());
        outputTextArea.setEditable(false);
        outputTextArea.setLineWrap(true);
        outputTextArea.setEditable(false);

        // scroll for text output
        JScrollPane areaScrollPane = new JScrollPane(outputTextArea);
        outputPanel.add(areaScrollPane);

        JPanel inputPanel = new JPanel();
        JTextArea textInputArea = new JTextArea(1, 32);
        textInputArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e){
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    checkAndProceedInput(textInputArea);
                }
            }
        });
        inputPanel.add(textInputArea);

        // confirm input button
        btnSubmit = new JButton("submit");
        inputPanel.add(btnSubmit);
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt){
                checkAndProceedInput(textInputArea);
            }
        });

        this.add(outputPanel);
        this.add(inputPanel);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setTitle("Sniffer");
        setSize(500, 300);
        setVisible(true);
    }

    public void checkAndProceedInput(JTextArea textInput){
        String input = textInput.getText();
        try {
            int deviceChosen = Integer.valueOf(input);
            openMainView(deviceChosen);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please, enter a number");
        }
    }

    public void openMainView(int deviceChosen){
        boolean deviceChoiceSucceed = sniffer
                .isDeviceChoiceSucceed(deviceChosen);
        if (deviceChoiceSucceed) {
            sniffer.setSelectedDeviceNumber(deviceChosen);
            closeThisFrame();
            mainView.draw();
        }
    }

    private void closeThisFrame(){
        dispose();
    }

    public void deviceChosenSuccessfully(
            String deviceChosenSuccessfullyUserOutput){
        JOptionPane.showMessageDialog(null, deviceChosenSuccessfullyUserOutput);
    }

    public void deviceChoiceFailed(String deviceChoiceFailedUserOutput){
        JOptionPane.showMessageDialog(null, deviceChoiceFailedUserOutput);
    }

}
