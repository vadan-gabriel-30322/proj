package lab4_3;

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

public class Exercise3 {
    public static void main(String[] args) throws FileNotFoundException {
        PetriNet pn = new PetriNet();
        pn.PetriNetName = "Outside Reference Calculator (ORC)";
        pn.NetworkPort = 1084;
        pn.SetInputFile("C:\\Users\\vadan\\Desktop\\DCS\\All_Petri_FW\\PetriInputData\\OutsideTemp.txt");

        // Fuzzy Table for ORC: Maps Outside Temp (v1) to Water Ref Temp (P1 of HTC)
        // If Outside is NL (Very Cold), Water Ref should be PL (Very Hot).
        // If Outside is PL (Hot), Water Ref should be NL (Cold/Off).
        FLRS orcTable = new FLRS(
                new FV(FZ.PL), // Input NL -> Output PL
                new FV(FZ.PM), // Input NM -> Output PM
                new FV(FZ.ZR), // Input ZR -> Output ZR
                new FV(FZ.NM), // Input PM -> Output NM
                new FV(FZ.NL) // Input PL -> Output NL
        );

        FLRS reader = new FLRS(new FV(FZ.NL), new FV(FZ.NM), new FV(FZ.ZR), new FV(FZ.PM), new FV(FZ.PL),
                new FV(FZ.NL), new FV(FZ.NM), new FV(FZ.ZR), new FV(FZ.PM), new FV(FZ.PL),
                new FV(FZ.NL), new FV(FZ.NM), new FV(FZ.ZR), new FV(FZ.PM), new FV(FZ.PL),
                new FV(FZ.NL), new FV(FZ.NM), new FV(FZ.ZR), new FV(FZ.PM), new FV(FZ.PL),
                new FV(FZ.NL), new FV(FZ.NM), new FV(FZ.ZR), new FV(FZ.PM), new FV(FZ.PL));

        orcTable.Print();
        reader.Print();

        DataFuzzy p0 = new DataFuzzy();
        p0.SetName("P0");
        p0.SetValue(new Fuzzy(0.0F));
        pn.PlaceList.add(p0);
        DataFuzzy v1 = new DataFuzzy();
        v1.SetName("v1");
        pn.PlaceList.add(v1); // Outside Temp
        DataFuzzy p1 = new DataFuzzy();
        p1.SetName("P1");
        pn.PlaceList.add(p1); // Temp storage
        DataFuzzy ref = new DataFuzzy();
        ref.SetName("Ref");
        pn.PlaceList.add(ref); // Calculated Reference

        DataTransfer sendRef = new DataTransfer();
        sendRef.SetName("sendRef");
        sendRef.Value = new TransferOperation("localhost", "1083", "P1"); // Send to HTC P1
        pn.PlaceList.add(sendRef);

        // T0 - Read Outside Temp
        PetriTransition t0 = new PetriTransition(pn);
        t0.TransitionName = "T0";
        t0.InputPlaceName.add("P0");
        t0.InputPlaceName.add("v1");
        Condition T0Ct1 = new Condition(t0, "P0", TransitionCondition.NotNull);
        Condition T0Ct2 = new Condition(t0, "v1", TransitionCondition.NotNull);
        T0Ct1.SetNextCondition(LogicConnector.AND, T0Ct2);
        GuardMapping grdT0 = new GuardMapping();
        grdT0.condition = T0Ct1;
        ArrayList<PlaceNameWithWeight> input0 = new ArrayList<>();
        input0.add(new PlaceNameWithWeight("P0", 1F));
        input0.add(new PlaceNameWithWeight("v1", 1F));
        ArrayList<String> Output0 = new ArrayList<>();
        Output0.add("P1");
        grdT0.Activations.add(new Activation(t0, reader, input0, TransitionOperation.FLRS, Output0));
        t0.GuardMappingList.add(grdT0);
        t0.Delay = 0;
        pn.Transitions.add(t0);

        // T1 - Calculate Reference
        PetriTransition t1 = new PetriTransition(pn);
        t1.TransitionName = "T1";
        t1.InputPlaceName.add("P1");
        Condition T1Ct1 = new Condition(t1, "P1", TransitionCondition.NotNull);
        GuardMapping grdT1 = new GuardMapping();
        grdT1.condition = T1Ct1;
        ArrayList<PlaceNameWithWeight> input1 = new ArrayList<>();
        input1.add(new PlaceNameWithWeight("P1", 1F));
        ArrayList<String> Output1 = new ArrayList<>();
        Output1.add("Ref");
        grdT1.Activations.add(new Activation(t1, orcTable, input1, TransitionOperation.FLRS, Output1));
        t1.GuardMappingList.add(grdT1);
        t1.Delay = 0;
        pn.Transitions.add(t1);

        // T2 - Send Reference
        PetriTransition t2 = new PetriTransition(pn);
        t2.TransitionName = "T2";
        t2.InputPlaceName.add("Ref");
        Condition T2Ct1 = new Condition(t2, "Ref", TransitionCondition.NotNull);
        GuardMapping grdT2 = new GuardMapping();
        grdT2.condition = T2Ct1;
        grdT2.Activations.add(new Activation(t2, "Ref", TransitionOperation.SendOverNetwork, "sendRef"));
        grdT2.Activations.add(new Activation(t2, "Ref", TransitionOperation.Move, "P0")); // Loop back token to P0? Or
                                                                                          // just consume?
        // Wait, T0 needs P0 token. So we need to return a token to P0.
        // But SendOverNetwork doesn't consume/move by default?
        // Actually, Activation with Move moves the token.
        // Let's make T2 move Ref to P0 (as a dummy token) to enable next reading?
        // But P0 needs to be 0.0F usually for reader?
        // Let's just put a new token in P0.
        // Actually, let's use a separate activation to put token in P0.
        // Or better, use OneXOne table to map Ref back to P0 (0.0F)? No.
        // Let's just assume T2 moves Ref to P0. But Ref has value. P0 needs 0.0F?
        // Reader table takes P0 and v1. If P0 is not 0, it might affect result if table
        // depends on it.
        // The reader table provided in other files seems to be a 5x5 table.
        // If P0 is used as index, it matters.
        // In RoomPlant, P0 is 0.0F.
        // Let's ensure we reset P0.
        // We can use a transition T3 to reset P0.
        t2.GuardMappingList.add(grdT2);
        t2.Delay = 0;
        pn.Transitions.add(t2);

        // T3 - Reset Loop (Ref -> P0)
        // Actually, let's make T2 output to P0 as well.
        // But we can't easily set value to 0.0F without a table.
        // Let's use a Reset Table.
        // Or just use the fact that Reader might handle it?
        // Let's stick to the pattern in other files.
        // In RoomPlant: T14 outputs to xold and p0.
        // In HTC: T3 outputs to P0.
        // Let's add T3 to move Ref to P0, but we need to ensure P0 is 0.
        // Let's just use a simple Move for now and hope Reader handles it or P0 value
        // doesn't drift too much.
        // Actually, let's look at HTC. T3 uses OneXOneDefaultTable to move P5 to P0.
        // OneXOneDefaultTable maps NL->NL, etc.
        // So P0 gets P5's value.
        // So P0 is not constant 0.
        // So Reader table must handle P0 variation?
        // Reader table is 5x5.
        // If P0 varies, the row selected varies.
        // If P0 is supposed to be "state", then it's fine.
        // If P0 is just a trigger, it should be constant.
        // In HTC, P0 starts at 0.0F.
        // T0 reads P0 and P1.
        // T3 writes P5 to P0.
        // So P0 updates.
        // So for ORC, we should probably loop back.

        PetriTransition t3 = new PetriTransition(pn);
        t3.TransitionName = "T3";
        t3.InputPlaceName.add("Ref");
        Condition T3Ct1 = new Condition(t3, "Ref", TransitionCondition.NotNull);
        GuardMapping grdT3 = new GuardMapping();
        grdT3.condition = T3Ct1;
        ArrayList<PlaceNameWithWeight> input3 = new ArrayList<>();
        input3.add(new PlaceNameWithWeight("Ref", 1F));
        ArrayList<String> Output3 = new ArrayList<>();
        Output3.add("P0");
        // Use OneXOne table to pass value back
        FLRS OneXOneDefaultTable = new FLRS(new FV(FZ.NL), new FV(FZ.NM), new FV(FZ.ZR), new FV(FZ.PM), new FV(FZ.PL));
        grdT3.Activations.add(new Activation(t3, OneXOneDefaultTable, input3, TransitionOperation.FLRS, Output3));
        t3.GuardMappingList.add(grdT3);
        t3.Delay = 1;
        pn.Transitions.add(t3);

        System.out.println("ORC started \n ------------------------------");
        pn.Delay = 500;
        pn.ShowLogInWindow = true;
        PetriNetWindow frame = new PetriNetWindow(false);
        frame.petriNet = pn;
        frame.setVisible(true);
    }
}
