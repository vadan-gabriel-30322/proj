package p5;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Components.PetriNet;
import DataObjects.DataCar;
import DataOnly.Car;

public class InputCarP5 extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtModel;
    private JTextField txtNumber;

    public InputCarP5(PetriNet pn) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 350);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblModel = new JLabel("Car Model:");
        lblModel.setBounds(10, 11, 80, 14);
        contentPane.add(lblModel);

        txtModel = new JTextField();
        txtModel.setText("Bus"); // Default to Bus for testing
        txtModel.setBounds(100, 8, 150, 20);
        contentPane.add(txtModel);
        txtModel.setColumns(10);

        JLabel lblHint = new JLabel("(Bus, Taxi, Priority, Car)");
        lblHint.setBounds(260, 11, 150, 14);
        contentPane.add(lblHint);

        JLabel lblNumber = new JLabel("Car Number:");
        lblNumber.setBounds(10, 42, 80, 14);
        contentPane.add(lblNumber);

        txtNumber = new JTextField();
        txtNumber.setText("1");
        txtNumber.setBounds(100, 39, 150, 20);
        contentPane.add(txtNumber);
        txtNumber.setColumns(10);

        JLabel lblInputLane = new JLabel("Input Lane:");
        lblInputLane.setBounds(10, 73, 80, 14);
        contentPane.add(lblInputLane);

        String[] lanes = { "P_a1", "P_a2", "P_a3", "P_a4", "P_a" };
        JComboBox<String> cmbLane = new JComboBox<>(lanes);
        cmbLane.setBounds(100, 70, 150, 22);
        contentPane.add(cmbLane);

        JLabel lblTargets = new JLabel("Target Exit (T_g1..T_g4, T_S, T_o, T_2, T_3):");
        lblTargets.setBounds(10, 104, 300, 14);
        contentPane.add(lblTargets);

        JTextArea txtTargets = new JTextArea();
        txtTargets.setText("T_S");
        txtTargets.setBounds(10, 129, 414, 80);
        contentPane.add(txtTargets);

        JButton btnSend = new JButton("Send Car");
        btnSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String model = txtModel.getText();
                String number = txtNumber.getText();
                String lane = (String) cmbLane.getSelectedItem();
                String[] targets = txtTargets.getText().split(",");
                ArrayList<String> targetList = new ArrayList<>();
                for (String t : targets) {
                    targetList.add(t.trim());
                }

                Car car = new Car(model, number, targetList);
                DataCar dataCar = new DataCar();
                dataCar.SetName(lane);
                dataCar.SetValue(car);

                int index = -1;
                for (int i = 0; i < pn.PlaceList.size(); i++) {
                    if (pn.PlaceList.get(i).GetName().equals(lane)) {
                        index = i;
                        break;
                    }
                }

                if (index != -1) {
                    pn.PlaceList.get(index).SetValue(car);
                    System.out.println("Car sent to " + lane);
                } else {
                    System.err.println("Lane " + lane + " not found!");
                }
            }
        });
        btnSend.setBounds(10, 220, 414, 30);
        contentPane.add(btnSend);
    }
}
