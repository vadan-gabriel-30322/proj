package lab4_3;

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
import Enumerations.TransitionCondition;
import Enumerations.TransitionOperation;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ORC {
    public static void main(String[] args) throws FileNotFoundException {
        PetriNet pn = new PetriNet();
        pn.PetriNetName = "ORC - Outside Reference Calculator";
        pn.NetworkPort = 1084;

        // Fuzzy Table for T2 (Calculates Reference Temp based on Outside Temp)
        // Assuming inverse relationship: Colder outside -> Hotter water needed
        FLRS table = new FLRS(
                new FV(FZ.PL), // NL -> PL
                new FV(FZ.PM), // NM -> PM
                new FV(FZ.ZR), // ZR -> ZR
                new FV(FZ.NM), // PM -> NM
                new FV(FZ.NL) // PL -> NL
        );

        // Places
        DataFuzzy p0 = new DataFuzzy();
        p0.SetName("P0");
        p0.SetValue(new Fuzzy(0.0F));
        pn.PlaceList.add(p0);
        DataFuzzy p1 = new DataFuzzy();
        p1.SetName("P1");
        p1.SetValue(new Fuzzy(-0.5F));
        pn.PlaceList.add(p1); // Outside Temp (Input)
        DataFuzzy p2 = new DataFuzzy();
        p2.SetName("P2");
        pn.PlaceList.add(p2);
        DataFuzzy p3 = new DataFuzzy();
        p3.SetName("P3");
        pn.PlaceList.add(p3);
        DataFuzzy p4 = new DataFuzzy();
        p4.SetName("P4");
        pn.PlaceList.add(p4);
        DataFuzzy p5 = new DataFuzzy();
        p5.SetName("P5");
        pn.PlaceList.add(p5);

        DataTransfer waterTempRef = new DataTransfer();
        waterTempRef.SetName("waterTempRef");
        waterTempRef.Value = new TransferOperation("localhost", "1083", "P1"); // To HTC PI (P1)
        pn.PlaceList.add(waterTempRef);

        // T0 - Read Input
        PetriTransition t0 = new PetriTransition(pn);
        t0.TransitionName = "T0";
        t0.InputPlaceName.add("P0");
        t0.InputPlaceName.add("P1");
        Condition T0Ct1 = new Condition(t0, "P0", TransitionCondition.NotNull);
        Condition T0Ct2 = new Condition(t0, "P1", TransitionCondition.NotNull);
        T0Ct1.SetNextCondition(Enumerations.LogicConnector.AND, T0Ct2);
        GuardMapping grdT0 = new GuardMapping();
        grdT0.condition = T0Ct1;
        ArrayList<PlaceNameWithWeight> input0 = new ArrayList<>();
        input0.add(new PlaceNameWithWeight("P0", 1F));
        input0.add(new PlaceNameWithWeight("P1", 1F));
        ArrayList<String> Output0 = new ArrayList<>();
        Output0.add("P2");
        Output0.add("P3");
        grdT0.Activations.add(new Activation(t0, table, input0, TransitionOperation.FLRS, Output0)); // Using table just
                                                                                                     // to pass through
                                                                                                     // or simple copy?
        // Figure 4.2 doesn't specify T0 logic, assuming simple copy.
        // But Activation needs a table or operation. Let's use OneXOne for simple copy
        // if possible, or just use the table if it maps P1 to P3 correctly.
        // Let's assume T0 just moves tokens.
        // Actually, T2 is the one with FLRS. T0 might just be a splitter.
        // Let's use a simple OneXOne table for T0 to pass P1 to P3.
        FLRS OneXOne = new FLRS(new FV(FZ.NL), new FV(FZ.NM), new FV(FZ.ZR), new FV(FZ.PM), new FV(FZ.PL));
        grdT0.Activations.clear();
        grdT0.Activations.add(new Activation(t0, OneXOne, input0, TransitionOperation.FLRS, Output0));
        t0.GuardMappingList.add(grdT0);
        t0.Delay = 0;
        pn.Transitions.add(t0);

        // T1 - Loop back P2 -> P0
        PetriTransition t1 = new PetriTransition(pn);
        t1.TransitionName = "T1";
        t1.InputPlaceName.add("P2");
        Condition T1Ct1 = new Condition(t1, "P2", TransitionCondition.NotNull);
        GuardMapping grdT1 = new GuardMapping();
        grdT1.condition = T1Ct1;
        ArrayList<PlaceNameWithWeight> input1 = new ArrayList<>();
        input1.add(new PlaceNameWithWeight("P2", 1F));
        ArrayList<String> Output1 = new ArrayList<>();
        Output1.add("P0");
        grdT1.Activations.add(new Activation(t1, OneXOne, input1, TransitionOperation.FLRS, Output1));
        t1.GuardMappingList.add(grdT1);
        t1.Delay = 1;
        pn.Transitions.add(t1);

        // T2 - Calculate Reference (FLRS)
        PetriTransition t2 = new PetriTransition(pn);
        t2.TransitionName = "T2";
        t2.InputPlaceName.add("P3");
        Condition T2Ct1 = new Condition(t2, "P3", TransitionCondition.NotNull);
        GuardMapping grdT2 = new GuardMapping();
        grdT2.condition = T2Ct1;
        ArrayList<PlaceNameWithWeight> input2 = new ArrayList<>();
        input2.add(new PlaceNameWithWeight("P3", 1F));
        ArrayList<String> Output2 = new ArrayList<>();
        Output2.add("P4");
        grdT2.Activations.add(new Activation(t2, table, input2, TransitionOperation.FLRS, Output2));
        t2.GuardMappingList.add(grdT2);
        t2.Delay = 0;
        pn.Transitions.add(t2);

        // T3 - Send Output
        PetriTransition t3 = new PetriTransition(pn);
        t3.TransitionName = "T3";
        t3.InputPlaceName.add("P4");
        Condition T3Ct1 = new Condition(t3, "P4", TransitionCondition.NotNull);
        GuardMapping grdT3 = new GuardMapping();
        grdT3.condition = T3Ct1;
        grdT3.Activations.add(new Activation(t3, "P4", TransitionOperation.SendOverNetwork, "waterTempRef"));
        ArrayList<String> Output3 = new ArrayList<>();
        Output3.add("P5");
        // Also move token to P5
        ArrayList<PlaceNameWithWeight> input3 = new ArrayList<>();
        input3.add(new PlaceNameWithWeight("P4", 1F));
        grdT3.Activations.add(new Activation(t3, OneXOne, input3, TransitionOperation.FLRS, Output3));
        t3.GuardMappingList.add(grdT3);
        t3.Delay = 0;
        pn.Transitions.add(t3);

        System.out.println("ORC started");
        pn.Delay = 1000;
        pn.ShowLogInWindow = true;

        PetriNetWindow frame = new PetriNetWindow(false);
        frame.petriNet = pn;
        frame.setVisible(true);
    }
}
