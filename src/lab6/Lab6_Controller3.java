package lab6;

import java.util.ArrayList;

import Components.Activation;
import Components.Condition;
import Components.GuardMapping;
import Components.PetriNet;
import Components.PetriNetWindow;
import Components.PetriTransition;
import DataObjects.DataFloat;
import DataObjects.DataString;
import DataObjects.DataTransfer;
import DataOnly.TransferOperation;
import Enumerations.LogicConnector;
import Enumerations.TransitionCondition;
import Enumerations.TransitionOperation;

public class Lab6_Controller3 {

    public static void main(String[] args) {
        PetriNet pn = new PetriNet();
        pn.PetriNetName = "Controller 3";
        pn.NetworkPort = 1090;

        // ------------------------------------------------------------------------

        DataFloat hr3 = new DataFloat();
        hr3.SetName("hr3");
        hr3.SetValue(20.0f); // Reference Level (Set to 20 to trigger Increase when h3=30? Wait. If hr3 < h3,
                             // it increases? Let's check logic.)
        // Logic from Ct1: hr < h -> Increase.
        // Logic from Ct2/Ct3:
        // grd03: (hr < h) -> Increase.
        // So if h3=30, we need hr3 < 30 to get Increase. 20 is fine.
        pn.PlaceList.add(hr3);

        DataFloat h3 = new DataFloat();
        h3.SetName("h3");
        h3.SetValue(30.0f); // Current Level
        pn.PlaceList.add(h3);

        DataString c3_k_1 = new DataString();
        c3_k_1.SetName("c3(k-1)");
        pn.PlaceList.add(c3_k_1);

        DataString c3 = new DataString();
        c3.SetName("c3");
        pn.PlaceList.add(c3);

        DataString dc3 = new DataString();
        dc3.SetName("dc3");
        pn.PlaceList.add(dc3);

        DataString m13 = new DataString(); // Input from Ct1
        m13.SetName("m13_in");
        pn.PlaceList.add(m13);

        DataTransfer m32 = new DataTransfer();
        m32.SetName("m32");
        m32.Value = new TransferOperation("localhost", "1100", "m32_in"); // Sending to Controller 2
        pn.PlaceList.add(m32);

        DataString po = new DataString();
        po.SetName("po");
        pn.PlaceList.add(po);

        DataString po_3 = new DataString();
        po_3.SetName("po-3");
        po_3.SetValue("No Action");
        pn.PlaceList.add(po_3);

        // T0 ------------------------------------------------
        PetriTransition t0 = new PetriTransition(pn);
        t0.TransitionName = "t0";
        t0.InputPlaceName.add("hr3");
        t0.InputPlaceName.add("h3");
        t0.InputPlaceName.add("po-3");
        t0.InputPlaceName.add("m13_in");

        // Guard 1: (hr3 > h3) AND (m13 == Decrease) -> Decrease
        Condition T0Ct1 = new Condition(t0, "hr3", TransitionCondition.NotNull);
        Condition T0Ct2 = new Condition(t0, "h3", TransitionCondition.NotNull);
        Condition T0Ct3 = new Condition(t0, "po-3", TransitionCondition.NotNull);
        Condition T0Ct4 = new Condition(t0, "m13_in", TransitionCondition.NotNull);
        Condition T0Ct5 = new Condition(t0, "hr3", TransitionCondition.MoreThan, "h3");
        Condition T0Ct6 = new Condition(t0, "m13_in", TransitionCondition.Equal, "Decrease");

        T0Ct5.SetNextCondition(LogicConnector.AND, T0Ct6);
        T0Ct4.SetNextCondition(LogicConnector.AND, T0Ct5);
        T0Ct3.SetNextCondition(LogicConnector.AND, T0Ct4);
        T0Ct2.SetNextCondition(LogicConnector.AND, T0Ct3);
        T0Ct1.SetNextCondition(LogicConnector.AND, T0Ct2);

        GuardMapping grd01 = new GuardMapping();
        grd01.condition = T0Ct1;
        grd01.Activations.add(new Activation(t0, "hr3", TransitionOperation.Move, "hr3"));
        grd01.Activations.add(new Activation(t0, "h3", TransitionOperation.Move, "h3"));
        grd01.Activations.add(new Activation(t0, "po-3", TransitionOperation.Move, "c3(k-1)"));
        grd01.Activations.add(new Activation(t0, "m13_in", TransitionOperation.Move, "m13_in")); // Keep m13? Or
                                                                                                 // consume? Usually
                                                                                                 // consume. But here we
                                                                                                 // might need it for
                                                                                                 // next tick? No, it
                                                                                                 // comes from network.
        // Actually, standard Petri Net consumes tokens. But if it comes from network,
        // it's a place.
        // Let's assume we consume it.

        ArrayList<String> lstInput = new ArrayList<String>();
        lstInput.add("hr3");
        lstInput.add("h3");
        grd01.Activations.add(new Activation(t0, lstInput, TransitionOperation.MakeString, "Decrease", "c3"));
        grd01.Activations.add(new Activation(t0, lstInput, TransitionOperation.MakeString, "Decrease", "dc3"));

        t0.GuardMappingList.add(grd01);

        // Guard 2: (hr3 > h3) AND (m13 == Increase) -> No Action
        Condition T0Ct7 = new Condition(t0, "hr3", TransitionCondition.NotNull);
        Condition T0Ct8 = new Condition(t0, "h3", TransitionCondition.NotNull);
        Condition T0Ct9 = new Condition(t0, "po-3", TransitionCondition.NotNull);
        Condition T0Ct10 = new Condition(t0, "m13_in", TransitionCondition.NotNull);
        Condition T0Ct11 = new Condition(t0, "hr3", TransitionCondition.MoreThan, "h3");
        Condition T0Ct12 = new Condition(t0, "m13_in", TransitionCondition.Equal, "Increase");

        T0Ct11.SetNextCondition(LogicConnector.AND, T0Ct12);
        T0Ct10.SetNextCondition(LogicConnector.AND, T0Ct11);
        T0Ct9.SetNextCondition(LogicConnector.AND, T0Ct10);
        T0Ct8.SetNextCondition(LogicConnector.AND, T0Ct9);
        T0Ct7.SetNextCondition(LogicConnector.AND, T0Ct8);

        GuardMapping grd02 = new GuardMapping();
        grd02.condition = T0Ct7;
        grd02.Activations.add(new Activation(t0, "hr3", TransitionOperation.Move, "hr3"));
        grd02.Activations.add(new Activation(t0, "h3", TransitionOperation.Move, "h3"));
        grd02.Activations.add(new Activation(t0, "po-3", TransitionOperation.Move, "c3(k-1)"));
        grd02.Activations.add(new Activation(t0, "m13_in", TransitionOperation.Move, "m13_in"));

        grd02.Activations.add(new Activation(t0, lstInput, TransitionOperation.MakeString, "No Action", "c3"));
        grd02.Activations.add(new Activation(t0, lstInput, TransitionOperation.MakeString, "No Action", "dc3"));

        t0.GuardMappingList.add(grd02);

        // Guard 3: (hr3 < h3) -> Increase (Independent of m13? Image says "grd03 (hr2 <
        // h2)". It doesn't mention m12.)
        // But we still need m13 token to fire t0 if it's an input place.
        // If m13 is an input place, we MUST have a token there.
        // So we should check m13 != null.
        Condition T0Ct13 = new Condition(t0, "hr3", TransitionCondition.NotNull);
        Condition T0Ct14 = new Condition(t0, "h3", TransitionCondition.NotNull);
        Condition T0Ct15 = new Condition(t0, "po-3", TransitionCondition.NotNull);
        Condition T0Ct16 = new Condition(t0, "m13_in", TransitionCondition.NotNull);
        Condition T0Ct17 = new Condition(t0, "hr3", TransitionCondition.LessThan, "h3");

        T0Ct16.SetNextCondition(LogicConnector.AND, T0Ct17);
        T0Ct15.SetNextCondition(LogicConnector.AND, T0Ct16);
        T0Ct14.SetNextCondition(LogicConnector.AND, T0Ct15);
        T0Ct13.SetNextCondition(LogicConnector.AND, T0Ct14);

        GuardMapping grd03 = new GuardMapping();
        grd03.condition = T0Ct13;
        grd03.Activations.add(new Activation(t0, "hr3", TransitionOperation.Move, "hr3"));
        grd03.Activations.add(new Activation(t0, "h3", TransitionOperation.Move, "h3"));
        grd03.Activations.add(new Activation(t0, "po-3", TransitionOperation.Move, "c3(k-1)"));
        grd03.Activations.add(new Activation(t0, "m13_in", TransitionOperation.Move, "m13_in"));

        grd03.Activations.add(new Activation(t0, lstInput, TransitionOperation.MakeString, "Increase", "c3"));
        grd03.Activations.add(new Activation(t0, lstInput, TransitionOperation.MakeString, "Increase", "dc3"));

        t0.GuardMappingList.add(grd03);

        // Guard 4: (hr3 == h3) AND (m13 == Decrease) -> No Action
        Condition T0Ct18 = new Condition(t0, "hr3", TransitionCondition.NotNull);
        Condition T0Ct19 = new Condition(t0, "h3", TransitionCondition.NotNull);
        Condition T0Ct20 = new Condition(t0, "po-3", TransitionCondition.NotNull);
        Condition T0Ct21 = new Condition(t0, "m13_in", TransitionCondition.NotNull);
        Condition T0Ct22 = new Condition(t0, "hr3", TransitionCondition.Equal, "h3");
        Condition T0Ct23 = new Condition(t0, "m13_in", TransitionCondition.Equal, "Decrease");

        T0Ct22.SetNextCondition(LogicConnector.AND, T0Ct23);
        T0Ct21.SetNextCondition(LogicConnector.AND, T0Ct22);
        T0Ct20.SetNextCondition(LogicConnector.AND, T0Ct21);
        T0Ct19.SetNextCondition(LogicConnector.AND, T0Ct20);
        T0Ct18.SetNextCondition(LogicConnector.AND, T0Ct19);

        GuardMapping grd04 = new GuardMapping();
        grd04.condition = T0Ct18;
        grd04.Activations.add(new Activation(t0, "hr3", TransitionOperation.Move, "hr3"));
        grd04.Activations.add(new Activation(t0, "h3", TransitionOperation.Move, "h3"));
        grd04.Activations.add(new Activation(t0, "po-3", TransitionOperation.Move, "c3(k-1)"));
        grd04.Activations.add(new Activation(t0, "m13_in", TransitionOperation.Move, "m13_in"));

        grd04.Activations.add(new Activation(t0, lstInput, TransitionOperation.MakeString, "No Action", "c3"));
        grd04.Activations.add(new Activation(t0, lstInput, TransitionOperation.MakeString, "No Action", "dc3"));

        t0.GuardMappingList.add(grd04);

        // Guard 5: (hr3 == h3) AND (m13 == Increase) -> Increase
        Condition T0Ct24 = new Condition(t0, "hr3", TransitionCondition.NotNull);
        Condition T0Ct25 = new Condition(t0, "h3", TransitionCondition.NotNull);
        Condition T0Ct26 = new Condition(t0, "po-3", TransitionCondition.NotNull);
        Condition T0Ct27 = new Condition(t0, "m13_in", TransitionCondition.NotNull);
        Condition T0Ct28 = new Condition(t0, "hr3", TransitionCondition.Equal, "h3");
        Condition T0Ct29 = new Condition(t0, "m13_in", TransitionCondition.Equal, "Increase");

        T0Ct28.SetNextCondition(LogicConnector.AND, T0Ct29);
        T0Ct27.SetNextCondition(LogicConnector.AND, T0Ct28);
        T0Ct26.SetNextCondition(LogicConnector.AND, T0Ct27);
        T0Ct25.SetNextCondition(LogicConnector.AND, T0Ct26);
        T0Ct24.SetNextCondition(LogicConnector.AND, T0Ct25);

        GuardMapping grd05 = new GuardMapping();
        grd05.condition = T0Ct24;
        grd05.Activations.add(new Activation(t0, "hr3", TransitionOperation.Move, "hr3"));
        grd05.Activations.add(new Activation(t0, "h3", TransitionOperation.Move, "h3"));
        grd05.Activations.add(new Activation(t0, "po-3", TransitionOperation.Move, "c3(k-1)"));
        grd05.Activations.add(new Activation(t0, "m13_in", TransitionOperation.Move, "m13_in"));

        grd05.Activations.add(new Activation(t0, lstInput, TransitionOperation.MakeString, "Increase", "c3"));
        grd05.Activations.add(new Activation(t0, lstInput, TransitionOperation.MakeString, "Increase", "dc3"));

        t0.GuardMappingList.add(grd05);

        t0.Delay = 0;
        pn.Transitions.add(t0);

        // T1 ------------------------------------------------
        PetriTransition t1 = new PetriTransition(pn);
        t1.TransitionName = "t1";
        t1.InputPlaceName.add("c3");

        Condition T1Ct1 = new Condition(t1, "c3", TransitionCondition.NotNull);

        GuardMapping grdT1 = new GuardMapping();
        grdT1.condition = T1Ct1;

        grdT1.Activations.add(new Activation(t1, "c3", TransitionOperation.SendOverNetwork, "m32"));
        grdT1.Activations.add(new Activation(t1, "c3", TransitionOperation.Move, "po"));

        t1.GuardMappingList.add(grdT1);
        t1.Delay = 0;
        pn.Transitions.add(t1);

        // T2 ------------------------------------------------
        PetriTransition t2 = new PetriTransition(pn);
        t2.TransitionName = "t2";
        t2.InputPlaceName.add("po");

        Condition T2Ct1 = new Condition(t2, "po", TransitionCondition.NotNull);

        GuardMapping grdT2 = new GuardMapping();
        grdT2.condition = T2Ct1;

        grdT2.Activations.add(new Activation(t2, "po", TransitionOperation.Move, "po-3"));

        t2.GuardMappingList.add(grdT2);
        t2.Delay = 1;
        pn.Transitions.add(t2);

        System.out.println("Controller 3 started \n ------------------------------");
        pn.Delay = 3000;

        PetriNetWindow frame = new PetriNetWindow(false);
        frame.petriNet = pn;
        frame.setVisible(true);
    }
}
