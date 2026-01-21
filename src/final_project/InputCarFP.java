package final_project;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Components.PetriNet;
import DataObjects.DataCar;
import DataObjects.DataCarQueue;
import DataOnly.Car;

public class InputCarFP extends JFrame {
    private static final long serialVersionUID = 1L;
    public PetriNet pn; // Made public or suppress warning, but field is used in inner class

    public InputCarFP(PetriNet pn) {
        this.pn = pn;
        setTitle("Input Car Panel - " + pn.PetriNetName);
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, 350, 250);
        add(panel);

        JLabel lblPlace = new JLabel("Place Name:");
        lblPlace.setBounds(10, 10, 100, 20);
        panel.add(lblPlace);

        JTextField txtPlace = new JTextField();
        txtPlace.setBounds(120, 10, 150, 20);
        txtPlace.setText("P_a1");
        panel.add(txtPlace);

        JLabel lblType = new JLabel("Car Type:");
        lblType.setBounds(10, 40, 100, 20);
        panel.add(lblType);

        String[] carTypes = { "Car", "Bus", "Taxi", "Priority" };
        JComboBox<String> cmbType = new JComboBox<>(carTypes);
        cmbType.setBounds(120, 40, 150, 20);
        panel.add(cmbType);

        JLabel lblRoute = new JLabel("Target Route:");
        lblRoute.setBounds(10, 70, 100, 20);
        panel.add(lblRoute);

        JTextField txtRoute = new JTextField();
        txtRoute.setBounds(120, 70, 150, 20);
        panel.add(txtRoute);

        JButton btnAdd = new JButton("Add Car");
        btnAdd.setBounds(120, 110, 100, 30);
        panel.add(btnAdd);

        JTextArea txtLog = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(txtLog);
        scrollPane.setBounds(10, 150, 310, 50);
        panel.add(scrollPane);

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String placeName = txtPlace.getText();
                String type = (String) cmbType.getSelectedItem();
                String route = txtRoute.getText();

                DataCar car = new DataCar();
                car.SetName("Car_" + System.currentTimeMillis());

                String[] targets = {};
                if (route != null && !route.isEmpty()) {
                    targets = route.split(",");
                }

                // Use Model string to define type (Bus/Taxi/Priority)
                Car carValue = new Car(type, "1", targets);
                car.SetValue(carValue);

                // Find place and add
                boolean added = false;
                if (InputCarFP.this.pn != null && InputCarFP.this.pn.PlaceList != null) {
                    for (int i = 0; i < InputCarFP.this.pn.PlaceList.size(); i++) {
                        if (InputCarFP.this.pn.PlaceList.get(i).GetName().equals(placeName)) {
                            if (InputCarFP.this.pn.PlaceList.get(i) instanceof DataCar) {
                                ((DataCar) InputCarFP.this.pn.PlaceList.get(i)).SetValue(carValue);
                                added = true;
                            } else if (InputCarFP.this.pn.PlaceList.get(i) instanceof DataCarQueue) {
                                ((DataCarQueue) InputCarFP.this.pn.PlaceList.get(i)).Value.AddCar(car);
                                added = true;
                            }
                            break;
                        }
                    }
                }

                if (added) {
                    txtLog.append("Added " + type + " to " + placeName + "\n");
                } else {
                    txtLog.append("Place " + placeName + " not found or invalid type.\n");
                }
            }
        });
    }
}
