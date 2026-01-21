package lab3;

import java.util.ArrayList;
import Components.Activation;
import Components.Condition;
import Components.GuardMapping;
import Components.PetriNet;
import Components.PetriNetWindow;
import Components.PetriTransition;
import DataObjects.DataFuzzy;
import DataOnly.FLRS;
import DataOnly.FV;
import DataOnly.Fuzzy;
import DataOnly.PlaceNameWithWeight;
import Enumerations.FZ;
import Enumerations.LogicConnector;
import Enumerations.TransitionCondition;
import Enumerations.TransitionOperation;

public class Exercise2 {
    public static void main(String[] args) {
        // Adder Table (Same as Exercise 1)
        FLRS adderTable = new FLRS(
                new FV(FZ.NL), new FV(FZ.NL), new FV(FZ.NM), new FV(FZ.NM), new FV(FZ.ZR),
                new FV(FZ.NL), new FV(FZ.NM), new FV(FZ.NM), new FV(FZ.ZR), new FV(FZ.PM),
                new FV(FZ.NM), new FV(FZ.NM), new FV(FZ.ZR), new FV(FZ.PM), new FV(FZ.PM),
                new FV(FZ.NM), new FV(FZ.ZR), new FV(FZ.PM), new FV(FZ.PM), new FV(FZ.PL),
                new FV(FZ.ZR), new FV(FZ.PM), new FV(FZ.PM), new FV(FZ.PL), new FV(FZ.PL));

        // Subtractor Table (u2 - u1)
        // Rows: u1, Cols: u2
        FLRS subtractorTable = new FLRS(
                // Row NL (u1)
                new FV(FZ.ZR), new FV(FZ.PM), new FV(FZ.PL), new FV(FZ.PL), new FV(FZ.PL),
                // Row NM
                new FV(FZ.NM), new FV(FZ.ZR), new FV(FZ.PM), new FV(FZ.PL), new FV(FZ.PL),
                // Row ZR
                new FV(FZ.NL), new FV(FZ.NM), new FV(FZ.ZR), new FV(FZ.PM), new FV(FZ.PL),
                // Row PM
                new FV(FZ.NL), new FV(FZ.NL), new FV(FZ.NM), new FV(FZ.ZR), new FV(FZ.PM),
                // Row PL
                new FV(FZ.NL), new FV(FZ.NL), new FV(FZ.NL), new FV(FZ.NM), new FV(FZ.ZR));

        PetriNet pn = new PetriNet();
        pn.PetriNetName = "Exercise 2 - Adder/Subtractor";
        pn.NetworkPort = 1083;

        // Inputs for Adder
        DataFuzzy p1_add = new DataFuzzy();
        p1_add.SetName("P1_Add");
        p1_add.SetValue(new Fuzzy(0.5F)); // u1 = 0.5 (PM)
        pn.PlaceList.add(p1_add);

        DataFuzzy p2_add = new DataFuzzy();
        p2_add.SetName("P2_Add");
        p2_add.SetValue(new Fuzzy(1.0F)); // u2 = 1.0 (PL)
        pn.PlaceList.add(p2_add);

        // Inputs for Subtractor
        DataFuzzy p1_sub = new DataFuzzy();
        p1_sub.SetName("P1_Sub");
        p1_sub.SetValue(new Fuzzy(0.5F)); // u1 = 0.5 (PM)
        pn.PlaceList.add(p1_sub);

        DataFuzzy p2_sub = new DataFuzzy();
        p2_sub.SetName("P2_Sub");
        p2_sub.SetValue(new Fuzzy(1.0F)); // u2 = 1.0 (PL)
        pn.PlaceList.add(p2_sub);

        // Outputs
        DataFuzzy p3_add = new DataFuzzy();
        p3_add.SetName("P3_Add");
        pn.PlaceList.add(p3_add);

        DataFuzzy p4_sub = new DataFuzzy();
        p4_sub.SetName("P4_Sub");
        pn.PlaceList.add(p4_sub);

        // Transition for Adder
        PetriTransition t1 = new PetriTransition(pn);
        t1.TransitionName = "T1_Add";
        t1.InputPlaceName.add("P1_Add");
        t1.InputPlaceName.add("P2_Add");

        Condition T1Ct1 = new Condition(t1, "P1_Add", TransitionCondition.NotNull);
        Condition T1Ct2 = new Condition(t1, "P2_Add", TransitionCondition.NotNull);
        T1Ct1.SetNextCondition(LogicConnector.AND, T1Ct2);

        GuardMapping grdT1 = new GuardMapping();
        grdT1.condition = T1Ct1;

        ArrayList<PlaceNameWithWeight> inputAdd = new ArrayList<>();
        inputAdd.add(new PlaceNameWithWeight("P1_Add", 1F));
        inputAdd.add(new PlaceNameWithWeight("P2_Add", 1F));

        ArrayList<String> outputAdd = new ArrayList<>();
        outputAdd.add("P3_Add");

        grdT1.Activations.add(new Activation(t1, adderTable, inputAdd, TransitionOperation.FLRS, outputAdd));
        t1.GuardMappingList.add(grdT1);
        t1.Delay = 0;
        pn.Transitions.add(t1);

        // Transition for Subtractor
        PetriTransition t2 = new PetriTransition(pn);
        t2.TransitionName = "T2_Sub";
        t2.InputPlaceName.add("P1_Sub");
        t2.InputPlaceName.add("P2_Sub");

        Condition T2Ct1 = new Condition(t2, "P1_Sub", TransitionCondition.NotNull);
        Condition T2Ct2 = new Condition(t2, "P2_Sub", TransitionCondition.NotNull);
        T2Ct1.SetNextCondition(LogicConnector.AND, T2Ct2);

        GuardMapping grdT2 = new GuardMapping();
        grdT2.condition = T2Ct1;

        ArrayList<PlaceNameWithWeight> inputSub = new ArrayList<>();
        inputSub.add(new PlaceNameWithWeight("P1_Sub", 1F));
        inputSub.add(new PlaceNameWithWeight("P2_Sub", 1F));

        ArrayList<String> outputSub = new ArrayList<>();
        outputSub.add("P4_Sub");

        grdT2.Activations.add(new Activation(t2, subtractorTable, inputSub, TransitionOperation.FLRS, outputSub));
        t2.GuardMappingList.add(grdT2);
        t2.Delay = 0;
        pn.Transitions.add(t2);

        System.out.println("Exercise 2 started");
        pn.Delay = 3000;

        PetriNetWindow frame = new PetriNetWindow(false);
        frame.petriNet = pn;
        frame.setVisible(true);
    }
}
