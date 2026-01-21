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

public class Lab6_Controller1 {

    public static void main(String[] args) {
        PetriNet pn = new PetriNet();
        pn.PetriNetName = "Controller 1";
        pn.NetworkPort = 1080;

        // ------------------------------------------------------------------------

        DataFloat hr1 = new DataFloat();
        hr1.SetName("hr1");
        hr1.SetValue(30.0f); // Reference Level
        pn.PlaceList.add(hr1);

        DataFloat h1 = new DataFloat();
        h1.SetName("h1");
        h1.SetValue(40.0f); // Current Level
        pn.PlaceList.add(h1);

        DataString c1_k_1 = new DataString();
        c1_k_1.SetName("c1(k-1)");
        pn.PlaceList.add(c1_k_1);

        DataString c1 = new DataString();
        c1.SetName("c1");
        pn.PlaceList.add(c1);

        DataString dc1 = new DataString();
        dc1.SetName("dc1");
        pn.PlaceList.add(dc1);

        DataTransfer m13 = new DataTransfer();
        m13.SetName("m13");
        m13.Value = new TransferOperation("localhost", "1090", "m13_in"); // Sending to Controller 3
        pn.PlaceList.add(m13);

        DataString po = new DataString();
        po.SetName("po");
        pn.PlaceList.add(po);

        DataString po_1 = new DataString();
        po_1.SetName("po-1");
        po_1.SetValue("No Action"); // Initial state
        pn.PlaceList.add(po_1);

        // T0 ------------------------------------------------
        PetriTransition t0 = new PetriTransition(pn);
        t0.TransitionName = "t0";
        t0.InputPlaceName.add("hr1");
        t0.InputPlaceName.add("h1");
        t0.InputPlaceName.add("po-1");

        // Guard 1: hr1 > h1 -> Decrease
        Condition T0Ct1 = new Condition(t0, "hr1", TransitionCondition.NotNull);
        Condition T0Ct2 = new Condition(t0, "h1", TransitionCondition.NotNull);
        Condition T0Ct3 = new Condition(t0, "po-1", TransitionCondition.NotNull);
        Condition T0Ct4 = new Condition(t0, "hr1", TransitionCondition.MoreThan, "h1");

        T0Ct3.SetNextCondition(LogicConnector.AND, T0Ct4);
        T0Ct2.SetNextCondition(LogicConnector.AND, T0Ct3);
        T0Ct1.SetNextCondition(LogicConnector.AND, T0Ct2);

        GuardMapping grd01 = new GuardMapping();
        grd01.condition = T0Ct1;
        grd01.Activations.add(new Activation(t0, "hr1", TransitionOperation.Move, "hr1"));
        grd01.Activations.add(new Activation(t0, "h1", TransitionOperation.Move, "h1"));
        grd01.Activations.add(new Activation(t0, "po-1", TransitionOperation.Move, "c1(k-1)"));

        ArrayList<String> lstInput = new ArrayList<String>();
        lstInput.add("hr1");
        lstInput.add("h1");
        grd01.Activations.add(new Activation(t0, lstInput, TransitionOperation.MakeString, "Decrease", "c1"));
        grd01.Activations.add(new Activation(t0, lstInput, TransitionOperation.MakeString, "Decrease", "dc1"));

        t0.GuardMappingList.add(grd01);

        // Guard 2: hr1 < h1 -> Increase
        Condition T0Ct5 = new Condition(t0, "hr1", TransitionCondition.NotNull);
        Condition T0Ct6 = new Condition(t0, "h1", TransitionCondition.NotNull);
        Condition T0Ct7 = new Condition(t0, "po-1", TransitionCondition.NotNull);
        Condition T0Ct8 = new Condition(t0, "hr1", TransitionCondition.LessThan, "h1");

        T0Ct7.SetNextCondition(LogicConnector.AND, T0Ct8);
        T0Ct6.SetNextCondition(LogicConnector.AND, T0Ct7);
        T0Ct5.SetNextCondition(LogicConnector.AND, T0Ct6);

        GuardMapping grd02 = new GuardMapping();
        grd02.condition = T0Ct5;
        grd02.Activations.add(new Activation(t0, "hr1", TransitionOperation.Move, "hr1"));
        grd02.Activations.add(new Activation(t0, "h1", TransitionOperation.Move, "h1"));
        grd02.Activations.add(new Activation(t0, "po-1", TransitionOperation.Move, "c1(k-1)"));

        grd02.Activations.add(new Activation(t0, lstInput, TransitionOperation.MakeString, "Increase", "c1"));
        grd02.Activations.add(new Activation(t0, lstInput, TransitionOperation.MakeString, "Increase", "dc1"));

        t0.GuardMappingList.add(grd02);

        // Guard 3: hr1 == h1 -> No Action
        Condition T0Ct9 = new Condition(t0, "hr1", TransitionCondition.NotNull);
        Condition T0Ct10 = new Condition(t0, "h1", TransitionCondition.NotNull);
        Condition T0Ct11 = new Condition(t0, "po-1", TransitionCondition.NotNull);
        Condition T0Ct12 = new Condition(t0, "hr1", TransitionCondition.Equal, "h1");

        T0Ct11.SetNextCondition(LogicConnector.AND, T0Ct12);
        T0Ct10.SetNextCondition(LogicConnector.AND, T0Ct11);
        T0Ct9.SetNextCondition(LogicConnector.AND, T0Ct10);

        GuardMapping grd03 = new GuardMapping();
        grd03.condition = T0Ct9;
        grd03.Activations.add(new Activation(t0, "hr1", TransitionOperation.Move, "hr1"));
        grd03.Activations.add(new Activation(t0, "h1", TransitionOperation.Move, "h1"));
        grd03.Activations.add(new Activation(t0, "po-1", TransitionOperation.Move, "c1(k-1)"));

        grd03.Activations.add(new Activation(t0, lstInput, TransitionOperation.MakeString, "No Action", "c1"));
        grd03.Activations.add(new Activation(t0, lstInput, TransitionOperation.MakeString, "No Action", "dc1"));

        t0.GuardMappingList.add(grd03);

        t0.Delay = 0;
        pn.Transitions.add(t0);

        // T1 ------------------------------------------------
        PetriTransition t1 = new PetriTransition(pn);
        t1.TransitionName = "t1";
        t1.InputPlaceName.add("c1");

        Condition T1Ct1 = new Condition(t1, "c1", TransitionCondition.NotNull);

        GuardMapping grdT1 = new GuardMapping();
        grdT1.condition = T1Ct1;

        grdT1.Activations.add(new Activation(t1, "c1", TransitionOperation.SendOverNetwork, "m13"));
        grdT1.Activations.add(new Activation(t1, "c1", TransitionOperation.Move, "po"));

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

        grdT2.Activations.add(new Activation(t2, "po", TransitionOperation.Move, "po-1"));

        t2.GuardMappingList.add(grdT2);
        t2.Delay = 1;
        pn.Transitions.add(t2);

        System.out.println("Controller 1 started \n ------------------------------");
        pn.Delay = 3000;

        PetriNetWindow frame = new PetriNetWindow(false);
        frame.petriNet = pn;
        frame.setVisible(true);
    }
}
