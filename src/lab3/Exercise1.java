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

public class Exercise1 {
    public static void main(String[] args) {
        // Define the Adder FLERS table (5x5)
        // Rows: u1 (NL, NM, ZR, PM, PL)
        // Cols: u2 (NL, NM, ZR, PM, PL)
        FLRS adderTable = new FLRS(
                // Row NL
                new FV(FZ.NL), new FV(FZ.NL), new FV(FZ.NM), new FV(FZ.NM), new FV(FZ.ZR),
                // Row NM
                new FV(FZ.NL), new FV(FZ.NM), new FV(FZ.NM), new FV(FZ.ZR), new FV(FZ.PM),
                // Row ZR
                new FV(FZ.NM), new FV(FZ.NM), new FV(FZ.ZR), new FV(FZ.PM), new FV(FZ.PM),
                // Row PM
                new FV(FZ.NM), new FV(FZ.ZR), new FV(FZ.PM), new FV(FZ.PM), new FV(FZ.PL),
                // Row PL
                new FV(FZ.ZR), new FV(FZ.PM), new FV(FZ.PM), new FV(FZ.PL), new FV(FZ.PL));

        PetriNet pn = new PetriNet();
        pn.PetriNetName = "Exercise 1 - Adder";
        pn.NetworkPort = 1082;

        DataFuzzy p1 = new DataFuzzy();
        p1.SetName("P1");
        p1.SetValue(new Fuzzy(-0.5F)); // u1 = -0.5
        pn.PlaceList.add(p1);

        DataFuzzy p2 = new DataFuzzy();
        p2.SetName("P2");
        p2.SetValue(new Fuzzy(0.5F)); // u2 = 0.5
        pn.PlaceList.add(p2);

        DataFuzzy p3 = new DataFuzzy();
        p3.SetName("P3");
        pn.PlaceList.add(p3);

        PetriTransition t1 = new PetriTransition(pn);
        t1.TransitionName = "T1";
        t1.InputPlaceName.add("P1");
        t1.InputPlaceName.add("P2");

        Condition T1Ct1 = new Condition(t1, "P1", TransitionCondition.NotNull);
        Condition T1Ct2 = new Condition(t1, "P2", TransitionCondition.NotNull);
        T1Ct1.SetNextCondition(LogicConnector.AND, T1Ct2);

        GuardMapping grdT1 = new GuardMapping();
        grdT1.condition = T1Ct1;

        ArrayList<PlaceNameWithWeight> input = new ArrayList<>();
        input.add(new PlaceNameWithWeight("P1", 1F));
        input.add(new PlaceNameWithWeight("P2", 1F));

        ArrayList<String> output = new ArrayList<>();
        output.add("P3");

        grdT1.Activations.add(new Activation(t1, adderTable, input, TransitionOperation.FLRS, output));

        t1.GuardMappingList.add(grdT1);
        t1.Delay = 0;
        pn.Transitions.add(t1);

        System.out.println("Exercise 1 started");
        pn.Delay = 3000;

        PetriNetWindow frame = new PetriNetWindow(false);
        frame.petriNet = pn;
        frame.setVisible(true);
    }
}
