package final_project;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import DataObjects.DataCar;
import DataObjects.DataString;
import DataOnly.Car;
import Utilities.DataOverNetwork;

public class UniversalInput extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtPort;
    private JTextField txtPlaceName;
    private JTextField txtCarId;
    private JTextField txtRoute;
    private JTextArea txtLog;
    private JComboBox<String> cmbTokenType;
    private JComboBox<String> cmbCarType;
    private JTextField txtStringValue;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UniversalInput frame = new UniversalInput();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public UniversalInput() {
        setTitle("Universal Network Input");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblPort = new JLabel("Target Port:");
        lblPort.setBounds(20, 20, 80, 20);
        contentPane.add(lblPort);

        txtPort = new JTextField();
        txtPort.setText("1081");
        txtPort.setBounds(110, 20, 100, 20);
        contentPane.add(txtPort);
        txtPort.setColumns(10);

        JLabel lblPlace = new JLabel("Place Name:");
        lblPlace.setBounds(20, 50, 80, 20);
        contentPane.add(lblPlace);

        txtPlaceName = new JTextField();
        txtPlaceName.setText("P_a1");
        txtPlaceName.setBounds(110, 50, 150, 20);
        contentPane.add(txtPlaceName);
        txtPlaceName.setColumns(10);

        JLabel lblType = new JLabel("Token Type:");
        lblType.setBounds(20, 80, 80, 20);
        contentPane.add(lblType);

        String[] tokenTypes = { "DataCar", "DataString" };
        cmbTokenType = new JComboBox<>(tokenTypes);
        cmbTokenType.setBounds(110, 80, 150, 20);
        contentPane.add(cmbTokenType);

        // Car Fields Panel
        JPanel carPanel = new JPanel();
        carPanel.setBounds(10, 110, 400, 100);
        carPanel.setLayout(null);
        contentPane.add(carPanel);

        JLabel lblCarType = new JLabel("Car Type:");
        lblCarType.setBounds(10, 0, 80, 20);
        carPanel.add(lblCarType);

        String[] carTypes = { "Car", "Bus", "Taxi", "Priority" };
        cmbCarType = new JComboBox<>(carTypes);
        cmbCarType.setBounds(100, 0, 150, 20);
        carPanel.add(cmbCarType);

        JLabel lblCarId = new JLabel("Car ID:");
        lblCarId.setBounds(10, 30, 80, 20);
        carPanel.add(lblCarId);

        txtCarId = new JTextField();
        txtCarId.setText("1");
        txtCarId.setBounds(100, 30, 150, 20);
        carPanel.add(txtCarId);

        JLabel lblRoute = new JLabel("Route (,):");
        lblRoute.setBounds(10, 60, 80, 20);
        carPanel.add(lblRoute);

        txtRoute = new JTextField();
        txtRoute.setBounds(100, 60, 250, 20);
        carPanel.add(txtRoute);

        // String Fields Panel
        JPanel stringPanel = new JPanel();
        stringPanel.setBounds(10, 110, 400, 100);
        stringPanel.setLayout(null);
        stringPanel.setVisible(false);
        contentPane.add(stringPanel);

        JLabel lblStrVal = new JLabel("String Value:");
        lblStrVal.setBounds(10, 0, 80, 20);
        stringPanel.add(lblStrVal);

        txtStringValue = new JTextField();
        txtStringValue.setText("Hello");
        txtStringValue.setBounds(100, 0, 250, 20);
        stringPanel.add(txtStringValue);

        // Toggle panels based on selection
        cmbTokenType.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cmbTokenType.getSelectedItem().equals("DataCar")) {
                    carPanel.setVisible(true);
                    stringPanel.setVisible(false);
                } else {
                    carPanel.setVisible(false);
                    stringPanel.setVisible(true);
                }
            }
        });

        JButton btnSend = new JButton("Send to Port");
        btnSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendData();
            }
        });
        btnSend.setBounds(110, 220, 150, 30);
        contentPane.add(btnSend);

        txtLog = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(txtLog);
        scrollPane.setBounds(20, 260, 400, 90);
        contentPane.add(scrollPane);
    }

    private void sendData() {
        try {
            int port = Integer.parseInt(txtPort.getText());
            String place = txtPlaceName.getText();
            String type = (String) cmbTokenType.getSelectedItem();

            DataOverNetwork packet = new DataOverNetwork();
            packet.NetWorkPort = port;

            if (type.equals("DataCar")) {
                DataCar dc = new DataCar();
                dc.SetName(place);

                String cType = (String) cmbCarType.getSelectedItem();
                String cId = txtCarId.getText();
                String routeStr = txtRoute.getText();
                ArrayList<String> targets = new ArrayList<>();
                if (!routeStr.isEmpty()) {
                    String[] parts = routeStr.split(",");
                    for (String p : parts)
                        targets.add(p.trim());
                }

                dc.SetValue(new Car(cType, cId, targets));
                packet.petriObject = dc;

                txtLog.append("Sending Car " + cType + " to Port " + port + "\n");

            } else if (type.equals("DataString")) {
                DataString ds = new DataString();
                ds.SetName(place);
                ds.SetValue(txtStringValue.getText());
                packet.petriObject = ds;

                txtLog.append("Sending String to Port " + port + "\n");
            }

            Socket s = new Socket(InetAddress.getByName("localhost"), port);
            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
            oos.writeObject(packet);
            s.close();
            txtLog.append("Success!\n");

        } catch (Exception ex) {
            txtLog.append("Error: " + ex.getMessage() + "\n");
            ex.printStackTrace();
        }
    }
}
