package final_lab_exam;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import Components.Activation;
import Components.Condition;
import Components.GuardMapping;
import Components.PetriNet;
import Components.PetriNetWindow;
import Components.PetriTransition;
import DataObjects.DataFuzzy;
import DataObjects.DataTransfer;
import DataOnly.FLRS;
import DataOnly.FV;
import DataOnly.Fuzzy;
import DataOnly.PlaceNameWithWeight;
import DataOnly.TransferOperation;
import Enumerations.FZ;
import Enumerations.LogicConnector;
import Enumerations.TransitionCondition;
import Enumerations.TransitionOperation;

public class Plant {
    public static void main(String[] args) {
        PetriNet pn = new PetriNet();
        pn.PetriNetName = "Plant";
        pn.NetworkPort = 1085; // Port 1085 for Plant

        // Define FLRS Table (Double Channel Adder from OETPN_C)
        // Table structure: 5x5 Matrix
        // Rows: Input 1 (u) [NL, NM, ZR, PM, PL]
        // Cols: Input 2 (x) [NL, NM, ZR, PM, PL]
        FLRS doubleChannelAdder = new FLRS(
                // NL NM ZR PM PL
                new FV(FZ.NL, FZ.NL), new FV(FZ.NL, FZ.NL), new FV(FZ.NL, FZ.NL), new FV(FZ.NM, FZ.NM),
                new FV(FZ.ZR, FZ.ZR), // Row 1 (u=NL)
                new FV(FZ.NL, FZ.NL), new FV(FZ.NL, FZ.NL), new FV(FZ.NM, FZ.NM), new FV(FZ.ZR, FZ.ZR),
                new FV(FZ.PM, FZ.PM), // Row 2 (u=NM)
                new FV(FZ.NL, FZ.NL), new FV(FZ.NM, FZ.NM), new FV(FZ.ZR, FZ.ZR), new FV(FZ.PM, FZ.PM),
                new FV(FZ.PL, FZ.PL), // Row 3 (u=ZR)
                new FV(FZ.NM, FZ.NM), new FV(FZ.ZR, FZ.ZR), new FV(FZ.PM, FZ.PM), new FV(FZ.PL, FZ.PL),
                new FV(FZ.PL, FZ.PL), // Row 4 (u=PM)
                new FV(FZ.ZR, FZ.ZR), new FV(FZ.PM, FZ.PM), new FV(FZ.PL, FZ.PL), new FV(FZ.PL, FZ.PL),
                new FV(FZ.PL, FZ.PL) // Row 5 (u=PL)
        );
        doubleChannelAdder.Print();

        // ------------------------------------------------------------------------
        // Places
        // ------------------------------------------------------------------------

        // u: Input Command (from Controller)
        DataFuzzy u = new DataFuzzy();
        u.SetName("u");
        pn.PlaceList.add(u);

        // x: Current Level (Started at 0)
        DataFuzzy x = new DataFuzzy();
        x.SetName("x");
        x.SetValue(new Fuzzy(0.0F)); // Start empty
        pn.PlaceList.add(x);

        // y: Next Level / Output
        DataFuzzy y = new DataFuzzy();
        y.SetName("y");
        pn.PlaceList.add(y);

        // ------------------------------------------------------------------------
        // Transitions
        // ------------------------------------------------------------------------

        // T_21: Calculate Next State (y) from Current (x) and Input (u)
        PetriTransition t_21 = new PetriTransition(pn);
        t_21.TransitionName = "t_21";
        t_21.InputPlaceName.add("u");
        t_21.InputPlaceName.add("x");

        Condition T_21Ct1 = new Condition(t_21, "u", TransitionCondition.NotNull);
        Condition T_21Ct2 = new Condition(t_21, "x", TransitionCondition.NotNull);
        T_21Ct1.SetNextCondition(LogicConnector.AND, T_21Ct2);

        GuardMapping grdt_21 = new GuardMapping();
        grdt_21.condition = T_21Ct1;

        ArrayList<PlaceNameWithWeight> input1 = new ArrayList<>();
        input1.add(new PlaceNameWithWeight("x", 1.0F));
        input1.add(new PlaceNameWithWeight("u", 1.0F));

        ArrayList<String> output1 = new ArrayList<>();
        output1.add("x");
        output1.add("y");

        grdt_21.Activations.add(new Activation(t_21, doubleChannelAdder, input1, TransitionOperation.FLRS, output1));

        t_21.GuardMappingList.add(grdt_21);
        t_21.Delay = 0;
        pn.Transitions.add(t_21);

        // T22: Send Feedback
        DataTransfer p_o2 = new DataTransfer();
        p_o2.SetName("Cc_2_y");
        p_o2.Value = new TransferOperation("localhost", "1086", "P3"); // Send to Controller Port 1086, Place P3
        pn.PlaceList.add(p_o2);

        PetriTransition t_22 = new PetriTransition(pn);
        t_22.TransitionName = "t_22";
        t_22.InputPlaceName.add("y");

        Condition T_22Ct1 = new Condition(t_22, "y", TransitionCondition.NotNull);

        GuardMapping grdt_22 = new GuardMapping();
        grdt_22.condition = T_22Ct1;
        grdt_22.Activations.add(new Activation(t_22, "y", TransitionOperation.SendOverNetwork, "Cc_2_y"));

        t_22.GuardMappingList.add(grdt_22);
        t_22.Delay = 0;
        pn.Transitions.add(t_22);

        // -------------------------------------------
        System.out.println("Plant started \n ------------------------------");
        pn.Delay = 2000;
        pn.PrintingSpeed = 50;

        PetriNetWindow frame = new PetriNetWindow(true);
        frame.petriNet = pn;
        frame.setVisible(true);
    }
}
