package lab4_3;

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
import java.util.ArrayList;

public class Exercise1 {
    public static void main(String[] args) {
        PetriNet pn = new PetriNet();
        pn.PetriNetName = "Lab 3 Exercise 1";
        pn.NetworkPort = 1085;

        // Define a Differentiator Fuzzy Table
        FLRS doubleChannelDifferentiator = new FLRS(
                new FV(FZ.ZR, FZ.ZR), new FV(FZ.PM, FZ.PM), new FV(FZ.PL, FZ.PL), new FV(FZ.PL, FZ.PL),
                new FV(FZ.PL, FZ.PL),
                new FV(FZ.NM, FZ.NM), new FV(FZ.ZR, FZ.ZR), new FV(FZ.PM, FZ.PM), new FV(FZ.PL, FZ.PL),
                new FV(FZ.PL, FZ.PL),
                new FV(FZ.NL, FZ.NL), new FV(FZ.NM, FZ.NM), new FV(FZ.ZR, FZ.ZR), new FV(FZ.PM, FZ.PM),
                new FV(FZ.PL, FZ.PL),
                new FV(FZ.NL, FZ.NL), new FV(FZ.NL, FZ.NL), new FV(FZ.NM, FZ.NM), new FV(FZ.ZR, FZ.ZR),
                new FV(FZ.PM, FZ.PM),
                new FV(FZ.NL, FZ.NL), new FV(FZ.NL, FZ.NL), new FV(FZ.NL, FZ.NL), new FV(FZ.NM, FZ.NM),
                new FV(FZ.ZR, FZ.ZR));
        doubleChannelDifferentiator.Print();

        // Places
        DataFuzzy p1 = new DataFuzzy();
        p1.SetName("p1");
        p1.SetValue(new Fuzzy(0.5F));
        pn.PlaceList.add(p1);

        DataFuzzy p2 = new DataFuzzy();
        p2.SetName("p2");
        p2.SetValue(new Fuzzy(0.1F));
        pn.PlaceList.add(p2);

        DataFuzzy p3 = new DataFuzzy();
        p3.SetName("p3");
        pn.PlaceList.add(p3);

        DataFuzzy p4 = new DataFuzzy();
        p4.SetName("p4");
        pn.PlaceList.add(p4);

        // Transition T1
        PetriTransition t1 = new PetriTransition(pn);
        t1.TransitionName = "t1";
        t1.InputPlaceName.add("p1");
        t1.InputPlaceName.add("p2");

        Condition T1Ct1 = new Condition(t1, "p1", TransitionCondition.NotNull);
        Condition T1Ct2 = new Condition(t1, "p2", TransitionCondition.NotNull);
        T1Ct1.SetNextCondition(LogicConnector.AND, T1Ct2);

        GuardMapping grdT1 = new GuardMapping();
        grdT1.condition = T1Ct1;

        ArrayList<PlaceNameWithWeight> input1 = new ArrayList<>();
        input1.add(new PlaceNameWithWeight("p1", 1F));
        input1.add(new PlaceNameWithWeight("p2", 1F));

        ArrayList<String> output1 = new ArrayList<>();
        output1.add("p3");
        output1.add("p4");

        grdT1.Activations
                .add(new Activation(t1, doubleChannelDifferentiator, input1, TransitionOperation.FLRS, output1));

        t1.GuardMappingList.add(grdT1);
        t1.Delay = 0;
        pn.Transitions.add(t1);

        System.out.println("Lab 3 Exercise 1 started");
        pn.Delay = 3000;

        PetriNetWindow frame = new PetriNetWindow(false);
        frame.petriNet = pn;
        frame.setVisible(true);
    }
}
