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

public class Lab6_Controller2 {

    public static void main(String[] args) {
        PetriNet pn = new PetriNet();
        pn.PetriNetName = "Controller 2";
        pn.NetworkPort = 1100;

        // ------------------------------------------------------------------------

        DataFloat hr2 = new DataFloat();
        hr2.SetName("hr2");
        hr2.SetValue(25.0f); // Reference Level (Set to 25 to trigger No Action when h2=15 and m32=Increase)
        pn.PlaceList.add(hr2);

        DataFloat h2 = new DataFloat();
        h2.SetName("h2");
        h2.SetValue(15.0f); // Current Level
        pn.PlaceList.add(h2);

        DataString c2_k_1 = new DataString();
        c2_k_1.SetName("c2(k-1)");
        pn.PlaceList.add(c2_k_1);

        DataString c2 = new DataString();
        c2.SetName("c2");
        pn.PlaceList.add(c2);

        DataString dc2 = new DataString();
        dc2.SetName("dc2");
        pn.PlaceList.add(dc2);

        DataString m32 = new DataString(); // Input from Ct3
        m32.SetName("m32_in");
        pn.PlaceList.add(m32);

        DataString po = new DataString();
        po.SetName("po");
        pn.PlaceList.add(po);

        DataString po_2 = new DataString();
        po_2.SetName("po-2");
        po_2.SetValue("No Action");
        pn.PlaceList.add(po_2);

        // T0 ------------------------------------------------
        PetriTransition t0 = new PetriTransition(pn);
        t0.TransitionName = "t0";
        t0.InputPlaceName.add("hr2");
        t0.InputPlaceName.add("h2");
        t0.InputPlaceName.add("po-2");
        t0.InputPlaceName.add("m32_in");

        // Guard 1: (hr2 > h2) AND (m32 == Decrease) -> Decrease
        Condition T0Ct1 = new Condition(t0, "hr2", TransitionCondition.NotNull);
        Condition T0Ct2 = new Condition(t0, "h2", TransitionCondition.NotNull);
        Condition T0Ct3 = new Condition(t0, "po-2", TransitionCondition.NotNull);
        Condition T0Ct4 = new Condition(t0, "m32_in", TransitionCondition.NotNull);
        Condition T0Ct5 = new Condition(t0, "hr2", TransitionCondition.MoreThan, "h2");
        Condition T0Ct6 = new Condition(t0, "m32_in", TransitionCondition.Equal, "Decrease");

        T0Ct5.SetNextCondition(LogicConnector.AND, T0Ct6);
        T0Ct4.SetNextCondition(LogicConnector.AND, T0Ct5);
        T0Ct3.SetNextCondition(LogicConnector.AND, T0Ct4);
        T0Ct2.SetNextCondition(LogicConnector.AND, T0Ct3);
        T0Ct1.SetNextCondition(LogicConnector.AND, T0Ct2);

        GuardMapping grd01 = new GuardMapping();
        grd01.condition = T0Ct1;
        grd01.Activations.add(new Activation(t0, "hr2", TransitionOperation.Move, "hr2"));
        grd01.Activations.add(new Activation(t0, "h2", TransitionOperation.Move, "h2"));
        grd01.Activations.add(new Activation(t0, "po-2", TransitionOperation.Move, "c2(k-1)"));
        grd01.Activations.add(new Activation(t0, "m32_in", TransitionOperation.Move, "m32_in"));

        ArrayList<String> lstInput = new ArrayList<String>();
        lstInput.add("hr2");
        lstInput.add("h2");
        grd01.Activations.add(new Activation(t0, lstInput, TransitionOperation.MakeString, "Decrease", "c2"));
        grd01.Activations.add(new Activation(t0, lstInput, TransitionOperation.MakeString, "Decrease", "dc2"));

        t0.GuardMappingList.add(grd01);

        // Guard 2: (hr2 > h2) AND (m32 == Increase) -> No Action
        Condition T0Ct7 = new Condition(t0, "hr2", TransitionCondition.NotNull);
        Condition T0Ct8 = new Condition(t0, "h2", TransitionCondition.NotNull);
        Condition T0Ct9 = new Condition(t0, "po-2", TransitionCondition.NotNull);
        Condition T0Ct10 = new Condition(t0, "m32_in", TransitionCondition.NotNull);
        Condition T0Ct11 = new Condition(t0, "hr2", TransitionCondition.MoreThan, "h2");
        Condition T0Ct12 = new Condition(t0, "m32_in", TransitionCondition.Equal, "Increase");

        T0Ct11.SetNextCondition(LogicConnector.AND, T0Ct12);
        T0Ct10.SetNextCondition(LogicConnector.AND, T0Ct11);
        T0Ct9.SetNextCondition(LogicConnector.AND, T0Ct10);
        T0Ct8.SetNextCondition(LogicConnector.AND, T0Ct9);
        T0Ct7.SetNextCondition(LogicConnector.AND, T0Ct8);

        GuardMapping grd02 = new GuardMapping();
        grd02.condition = T0Ct7;
        grd02.Activations.add(new Activation(t0, "hr2", TransitionOperation.Move, "hr2"));
        grd02.Activations.add(new Activation(t0, "h2", TransitionOperation.Move, "h2"));
        grd02.Activations.add(new Activation(t0, "po-2", TransitionOperation.Move, "c2(k-1)"));
        grd02.Activations.add(new Activation(t0, "m32_in", TransitionOperation.Move, "m32_in"));

        grd02.Activations.add(new Activation(t0, lstInput, TransitionOperation.MakeString, "No Action", "c2"));
        grd02.Activations.add(new Activation(t0, lstInput, TransitionOperation.MakeString, "No Action", "dc2"));

        t0.GuardMappingList.add(grd02);

        // Guard 3: (hr2 < h2) -> Increase
        Condition T0Ct13 = new Condition(t0, "hr2", TransitionCondition.NotNull);
        Condition T0Ct14 = new Condition(t0, "h2", TransitionCondition.NotNull);
        Condition T0Ct15 = new Condition(t0, "po-2", TransitionCondition.NotNull);
        Condition T0Ct16 = new Condition(t0, "m32_in", TransitionCondition.NotNull);
        Condition T0Ct17 = new Condition(t0, "hr2", TransitionCondition.LessThan, "h2");

        T0Ct16.SetNextCondition(LogicConnector.AND, T0Ct17);
        T0Ct15.SetNextCondition(LogicConnector.AND, T0Ct16);
        T0Ct14.SetNextCondition(LogicConnector.AND, T0Ct15);
        T0Ct13.SetNextCondition(LogicConnector.AND, T0Ct14);

        GuardMapping grd03 = new GuardMapping();
        grd03.condition = T0Ct13;
        grd03.Activations.add(new Activation(t0, "hr2", TransitionOperation.Move, "hr2"));
        grd03.Activations.add(new Activation(t0, "h2", TransitionOperation.Move, "h2"));
        grd03.Activations.add(new Activation(t0, "po-2", TransitionOperation.Move, "c2(k-1)"));
        grd03.Activations.add(new Activation(t0, "m32_in", TransitionOperation.Move, "m32_in"));

        grd03.Activations.add(new Activation(t0, lstInput, TransitionOperation.MakeString, "Increase", "c2"));
        grd03.Activations.add(new Activation(t0, lstInput, TransitionOperation.MakeString, "Increase", "dc2"));

        t0.GuardMappingList.add(grd03);

        // Guard 4: (hr2 == h2) AND (m32 == Decrease) -> No Action
        Condition T0Ct18 = new Condition(t0, "hr2", TransitionCondition.NotNull);
        Condition T0Ct19 = new Condition(t0, "h2", TransitionCondition.NotNull);
        Condition T0Ct20 = new Condition(t0, "po-2", TransitionCondition.NotNull);
        Condition T0Ct21 = new Condition(t0, "m32_in", TransitionCondition.NotNull);
        Condition T0Ct22 = new Condition(t0, "hr2", TransitionCondition.Equal, "h2");
        Condition T0Ct23 = new Condition(t0, "m32_in", TransitionCondition.Equal, "Decrease");

        T0Ct22.SetNextCondition(LogicConnector.AND, T0Ct23);
        T0Ct21.SetNextCondition(LogicConnector.AND, T0Ct22);
        T0Ct20.SetNextCondition(LogicConnector.AND, T0Ct21);
        T0Ct19.SetNextCondition(LogicConnector.AND, T0Ct20);
        T0Ct18.SetNextCondition(LogicConnector.AND, T0Ct19);

        GuardMapping grd04 = new GuardMapping();
        grd04.condition = T0Ct18;
        grd04.Activations.add(new Activation(t0, "hr2", TransitionOperation.Move, "hr2"));
        grd04.Activations.add(new Activation(t0, "h2", TransitionOperation.Move, "h2"));
        grd04.Activations.add(new Activation(t0, "po-2", TransitionOperation.Move, "c2(k-1)"));
        grd04.Activations.add(new Activation(t0, "m32_in", TransitionOperation.Move, "m32_in"));

        grd04.Activations.add(new Activation(t0, lstInput, TransitionOperation.MakeString, "No Action", "c2"));
        grd04.Activations.add(new Activation(t0, lstInput, TransitionOperation.MakeString, "No Action", "dc2"));

        t0.GuardMappingList.add(grd04);

        // Guard 5: (hr2 == h2) AND (m32 == Increase) -> Increase
        Condition T0Ct24 = new Condition(t0, "hr2", TransitionCondition.NotNull);
        Condition T0Ct25 = new Condition(t0, "h2", TransitionCondition.NotNull);
        Condition T0Ct26 = new Condition(t0, "po-2", TransitionCondition.NotNull);
        Condition T0Ct27 = new Condition(t0, "m32_in", TransitionCondition.NotNull);
        Condition T0Ct28 = new Condition(t0, "hr2", TransitionCondition.Equal, "h2");
        Condition T0Ct29 = new Condition(t0, "m32_in", TransitionCondition.Equal, "Increase");

        T0Ct28.SetNextCondition(LogicConnector.AND, T0Ct29);
        T0Ct27.SetNextCondition(LogicConnector.AND, T0Ct28);
        T0Ct26.SetNextCondition(LogicConnector.AND, T0Ct27);
        T0Ct25.SetNextCondition(LogicConnector.AND, T0Ct26);
        T0Ct24.SetNextCondition(LogicConnector.AND, T0Ct25);

        GuardMapping grd05 = new GuardMapping();
        grd05.condition = T0Ct24;
        grd05.Activations.add(new Activation(t0, "hr2", TransitionOperation.Move, "hr2"));
        grd05.Activations.add(new Activation(t0, "h2", TransitionOperation.Move, "h2"));
        grd05.Activations.add(new Activation(t0, "po-2", TransitionOperation.Move, "c2(k-1)"));
        grd05.Activations.add(new Activation(t0, "m32_in", TransitionOperation.Move, "m32_in"));

        grd05.Activations.add(new Activation(t0, lstInput, TransitionOperation.MakeString, "Increase", "c2"));
        grd05.Activations.add(new Activation(t0, lstInput, TransitionOperation.MakeString, "Increase", "dc2"));

        t0.GuardMappingList.add(grd05);

        t0.Delay = 0;
        pn.Transitions.add(t0);

        // T1 ------------------------------------------------
        PetriTransition t1 = new PetriTransition(pn);
        t1.TransitionName = "t1";
        t1.InputPlaceName.add("c2");

        Condition T1Ct1 = new Condition(t1, "c2", TransitionCondition.NotNull);

        GuardMapping grdT1 = new GuardMapping();
        grdT1.condition = T1Ct1;

        grdT1.Activations.add(new Activation(t1, "c2", TransitionOperation.Move, "po"));

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

        grdT2.Activations.add(new Activation(t2, "po", TransitionOperation.Move, "po-2"));

        t2.GuardMappingList.add(grdT2);
        t2.Delay = 1;
        pn.Transitions.add(t2);

        System.out.println("Controller 2 started \n ------------------------------");
        pn.Delay = 3000;

        PetriNetWindow frame = new PetriNetWindow(false);
        frame.petriNet = pn;
        frame.setVisible(true);
    }
}
