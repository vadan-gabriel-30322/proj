package final_project;

import Components.Activation;
import Components.Condition;
import Components.GuardMapping;
import Components.PetriNet;
import Components.PetriNetWindow;
import Components.PetriTransition;
import DataObjects.DataInteger;
import DataObjects.DataString;
import DataObjects.DataTransfer;
import DataOnly.TransferOperation;
import Enumerations.LogicConnector;
import Enumerations.TransitionCondition;
import Enumerations.TransitionOperation;

public class Controller2 {

    public static void main(String[] args) {
        PetriNet pn = new PetriNet();
        pn.PetriNetName = "Controller 2 (Dynamic, 3 Lanes)";
        pn.SetName("Controller 2");
        pn.NetworkPort = 1092; // Port for Controller 2

        DataString ini = new DataString();
        ini.SetName("ini");
        ini.SetValue("red");
        pn.ConstantPlaceList.add(ini);

        DataString red = new DataString();
        red.SetName("red");
        red.SetValue("red");
        pn.ConstantPlaceList.add(red);

        DataString green = new DataString();
        green.SetName("green");
        green.SetValue("green");
        pn.ConstantPlaceList.add(green);

        DataString yellow = new DataString();
        yellow.SetName("yellow");
        yellow.SetValue("yellow");
        pn.ConstantPlaceList.add(yellow);

        // Delay Constants
        DataInteger Two = new DataInteger();
        Two.SetName("Two");
        Two.SetValue(2);
        pn.ConstantPlaceList.add(Two);

        DataInteger Three = new DataInteger();
        Three.SetName("Three");
        Three.SetValue(3);
        pn.ConstantPlaceList.add(Three);

        DataInteger Five = new DataInteger();
        Five.SetName("Five");
        Five.SetValue(5);
        pn.ConstantPlaceList.add(Five);

        DataInteger Six = new DataInteger();
        Six.SetName("Six");
        Six.SetValue(6);
        pn.ConstantPlaceList.add(Six);

        DataInteger Eight = new DataInteger();
        Eight.SetName("Eight");
        Eight.SetValue(8);
        pn.ConstantPlaceList.add(Eight);

        DataInteger Eleven = new DataInteger();
        Eleven.SetName("Eleven");
        Eleven.SetValue(11);
        pn.ConstantPlaceList.add(Eleven);

        // Places for state machine
        DataString p1 = new DataString();
        p1.SetName("r1r2r3");
        p1.SetValue("signal");
        pn.PlaceList.add(p1);

        DataString p2 = new DataString();
        p2.SetName("g1r2r3");
        pn.PlaceList.add(p2);

        DataString p3 = new DataString();
        p3.SetName("y1r2r3");
        pn.PlaceList.add(p3);

        DataString p4 = new DataString();
        p4.SetName("r1g2r3");
        pn.PlaceList.add(p4);

        DataString p5 = new DataString();
        p5.SetName("r1y2r3");
        pn.PlaceList.add(p5);

        DataString p6 = new DataString();
        p6.SetName("r1r2g3");
        pn.PlaceList.add(p6);

        DataString p7 = new DataString();
        p7.SetName("r1r2y3");
        pn.PlaceList.add(p7);

        // Control Place
        DataString pf = new DataString();
        pf.SetName("pf");
        pn.PlaceList.add(pf);

        // Input places
        DataString in1 = new DataString();
        in1.SetName("in1");
        pn.PlaceList.add(in1);

        DataString in2 = new DataString();
        in2.SetName("in2");
        pn.PlaceList.add(in2);

        DataString in3 = new DataString();
        in3.SetName("in3");
        pn.PlaceList.add(in3);

        // Output places - Targeting 1082 (Intersection 2)
        DataTransfer op1 = new DataTransfer();
        op1.SetName("OP1");
        op1.Value = new TransferOperation("localhost", "1082", "P_TL1");
        pn.PlaceList.add(op1);

        DataTransfer op2 = new DataTransfer();
        op2.SetName("OP2");
        op2.Value = new TransferOperation("localhost", "1082", "P_TL2");
        pn.PlaceList.add(op2);

        DataTransfer op3 = new DataTransfer();
        op3.SetName("OP3");
        op3.Value = new TransferOperation("localhost", "1082", "P_TL3");
        pn.PlaceList.add(op3);

        // ----------------------------iniT------------------------------------
        PetriTransition iniT = new PetriTransition(pn);
        iniT.TransitionName = "iniT";

        Condition iniTCt1 = new Condition(iniT, "ini", TransitionCondition.NotNull);

        GuardMapping grdiniT = new GuardMapping();
        grdiniT.condition = iniTCt1;

        grdiniT.Activations.add(new Activation(iniT, "ini", TransitionOperation.SendOverNetwork, "OP1"));
        grdiniT.Activations.add(new Activation(iniT, "ini", TransitionOperation.SendOverNetwork, "OP2"));
        grdiniT.Activations.add(new Activation(iniT, "ini", TransitionOperation.SendOverNetwork, "OP3"));
        grdiniT.Activations.add(new Activation(iniT, "", TransitionOperation.MakeNull, "ini"));

        iniT.GuardMappingList.add(grdiniT);

        iniT.Delay = 0;
        pn.Transitions.add(iniT);

        // ----------------------------T1------------------------------------
        // r1r2r3 -> g1r2r3 (OP1 Green) AND pf
        PetriTransition t1 = new PetriTransition(pn);
        t1.TransitionName = "T1";
        t1.InputPlaceName.add("r1r2r3");

        Condition T1Ct1 = new Condition(t1, "r1r2r3", TransitionCondition.NotNull);

        GuardMapping grdT1 = new GuardMapping();
        grdT1.condition = T1Ct1;
        grdT1.Activations.add(new Activation(t1, "r1r2r3", TransitionOperation.Move, "g1r2r3"));
        grdT1.Activations.add(new Activation(t1, "green", TransitionOperation.SendOverNetwork, "OP1"));
        grdT1.Activations.add(new Activation(t1, "r1r2r3", TransitionOperation.Move, "pf")); // Move to pf
        t1.GuardMappingList.add(grdT1);

        t1.Delay = 5;
        pn.Transitions.add(t1);

        // ----------------------------T2------------------------------------
        // g1r2r3 -> y1r2r3 (OP1 Yellow)
        PetriTransition t2 = new PetriTransition(pn);
        t2.TransitionName = "T2";
        t2.InputPlaceName.add("g1r2r3");

        Condition T2Ct1 = new Condition(t2, "g1r2r3", TransitionCondition.NotNull);

        GuardMapping grdT2 = new GuardMapping();
        grdT2.condition = T2Ct1;
        grdT2.Activations.add(new Activation(t2, "g1r2r3", TransitionOperation.Move, "y1r2r3"));
        grdT2.Activations.add(new Activation(t2, "yellow", TransitionOperation.SendOverNetwork, "OP1"));

        t2.GuardMappingList.add(grdT2);

        t2.Delay = 5; // Default delay, modified by tf
        pn.Transitions.add(t2);

        // ----------------------------T3------------------------------------
        // y1r2r3 -> r1g2r3 (OP1 Red, OP2 Green)
        PetriTransition t3 = new PetriTransition(pn);
        t3.TransitionName = "T3";
        t3.InputPlaceName.add("y1r2r3");

        Condition T3Ct1 = new Condition(t3, "y1r2r3", TransitionCondition.NotNull);

        GuardMapping grdT3 = new GuardMapping();
        grdT3.condition = T3Ct1;
        grdT3.Activations.add(new Activation(t3, "y1r2r3", TransitionOperation.Move, "r1g2r3"));
        grdT3.Activations.add(new Activation(t3, "red", TransitionOperation.SendOverNetwork, "OP1"));
        grdT3.Activations.add(new Activation(t3, "green", TransitionOperation.SendOverNetwork, "OP2"));

        t3.GuardMappingList.add(grdT3);

        t3.Delay = 5;
        pn.Transitions.add(t3);

        // ----------------------------T4------------------------------------
        // r1g2r3 -> r1y2r3 (OP2 Yellow)
        PetriTransition t4 = new PetriTransition(pn);
        t4.TransitionName = "T4";
        t4.InputPlaceName.add("r1g2r3");

        Condition T4Ct1 = new Condition(t4, "r1g2r3", TransitionCondition.NotNull);

        GuardMapping grdT4 = new GuardMapping();
        grdT4.condition = T4Ct1;
        grdT4.Activations.add(new Activation(t4, "r1g2r3", TransitionOperation.Move, "r1y2r3"));
        grdT4.Activations.add(new Activation(t4, "yellow", TransitionOperation.SendOverNetwork, "OP2"));

        t4.GuardMappingList.add(grdT4);

        t4.Delay = 5; // Default delay, modified by tf
        pn.Transitions.add(t4);

        // ----------------------------T5------------------------------------
        // r1y2r3 -> r1r2g3 (OP2 Red, OP3 Green)
        PetriTransition t5 = new PetriTransition(pn);
        t5.TransitionName = "T5";
        t5.InputPlaceName.add("r1y2r3");

        Condition T5Ct1 = new Condition(t5, "r1y2r3", TransitionCondition.NotNull);

        GuardMapping grdT5 = new GuardMapping();
        grdT5.condition = T5Ct1;
        grdT5.Activations.add(new Activation(t5, "r1y2r3", TransitionOperation.Move, "r1r2g3"));
        grdT5.Activations.add(new Activation(t5, "red", TransitionOperation.SendOverNetwork, "OP2"));
        grdT5.Activations.add(new Activation(t5, "green", TransitionOperation.SendOverNetwork, "OP3"));

        t5.GuardMappingList.add(grdT5);

        t5.Delay = 5;
        pn.Transitions.add(t5);

        // ----------------------------T6------------------------------------
        // r1r2g3 -> r1r2y3 (OP3 Yellow)
        PetriTransition t6 = new PetriTransition(pn);
        t6.TransitionName = "T6";
        t6.InputPlaceName.add("r1r2g3");

        Condition T6Ct1 = new Condition(t6, "r1r2g3", TransitionCondition.NotNull);

        GuardMapping grdT6 = new GuardMapping();
        grdT6.condition = T6Ct1;
        grdT6.Activations.add(new Activation(t6, "r1r2g3", TransitionOperation.Move, "r1r2y3"));
        grdT6.Activations.add(new Activation(t6, "yellow", TransitionOperation.SendOverNetwork, "OP3"));

        t6.GuardMappingList.add(grdT6);

        t6.Delay = 5; // Default delay, modified by tf
        pn.Transitions.add(t6);

        // ----------------------------T7------------------------------------
        // r1r2y3 -> r1r2r3 (OP3 Red)
        PetriTransition t7 = new PetriTransition(pn);
        t7.TransitionName = "T7";
        t7.InputPlaceName.add("r1r2y3");

        Condition T7Ct1 = new Condition(t7, "r1r2y3", TransitionCondition.NotNull);

        GuardMapping grdT7 = new GuardMapping();
        grdT7.condition = T7Ct1;
        grdT7.Activations.add(new Activation(t7, "r1r2y3", TransitionOperation.Move, "r1r2r3"));
        grdT7.Activations.add(new Activation(t7, "red", TransitionOperation.SendOverNetwork, "OP3"));

        t7.GuardMappingList.add(grdT7);

        t7.Delay = 5;
        pn.Transitions.add(t7);

        // ----------------------------tf (Control
        // Transition)------------------------------------
        PetriTransition tf = new PetriTransition(pn);
        tf.TransitionName = "tf";
        tf.InputPlaceName.add("pf");
        tf.InputPlaceName.add("in1");
        tf.InputPlaceName.add("in2");
        tf.InputPlaceName.add("in3");

        // Add guards for all cases
        // Case 1: 0 0 0 -> 5 5 5
        addGuard(tf, t2, t4, t6, 0, 0, 0, "Five", "Five", "Five");

        // Case 2: 1 0 0 -> 11 3 3
        addGuard(tf, t2, t4, t6, 1, 0, 0, "Eleven", "Three", "Three");

        // Case 3: 0 1 0 -> 3 11 3
        addGuard(tf, t2, t4, t6, 0, 1, 0, "Three", "Eleven", "Three");

        // Case 4: 0 0 1 -> 3 3 11
        addGuard(tf, t2, t4, t6, 0, 0, 1, "Three", "Three", "Eleven");

        // Case 5: 1 1 0 -> 8 8 2
        addGuard(tf, t2, t4, t6, 1, 1, 0, "Eight", "Eight", "Two");

        // Case 6: 1 0 1 -> 8 2 8
        addGuard(tf, t2, t4, t6, 1, 0, 1, "Eight", "Two", "Eight");

        // Case 7: 0 1 1 -> 2 8 8
        addGuard(tf, t2, t4, t6, 0, 1, 1, "Two", "Eight", "Eight");

        // Case 8: 1 1 1 -> 5 5 5
        addGuard(tf, t2, t4, t6, 1, 1, 1, "Five", "Five", "Five");

        tf.Delay = 0;
        pn.Transitions.add(tf);

        // -------------------------------------------------------------------------------------
        // ----------------------------PN
        // Start-------------------------------------------------
        // -------------------------------------------------------------------------------------

        System.out.println("Controller 2 started \n ------------------------------");
        pn.Delay = 2000;
        // pn.Start();

        PetriNetWindow frame = new PetriNetWindow(false);
        frame.petriNet = pn;
        frame.setVisible(true);
    }

    private static void addGuard(PetriTransition tf, PetriTransition t2, PetriTransition t4, PetriTransition t6,
            int i1, int i2, int i3,
            String d2, String d4, String d6) {

        Condition cPf = new Condition(tf, "pf", TransitionCondition.NotNull);
        Condition c1 = new Condition(tf, "in1", i1 == 1 ? TransitionCondition.NotNull : TransitionCondition.IsNull);
        Condition c2 = new Condition(tf, "in2", i2 == 1 ? TransitionCondition.NotNull : TransitionCondition.IsNull);
        Condition c3 = new Condition(tf, "in3", i3 == 1 ? TransitionCondition.NotNull : TransitionCondition.IsNull);

        cPf.SetNextCondition(LogicConnector.AND, c1);
        c1.SetNextCondition(LogicConnector.AND, c2);
        c2.SetNextCondition(LogicConnector.AND, c3);

        GuardMapping g = new GuardMapping();
        g.condition = cPf;

        g.Activations.add(new Activation(t2, d2, TransitionOperation.DynamicDelay, ""));
        g.Activations.add(new Activation(t4, d4, TransitionOperation.DynamicDelay, ""));
        g.Activations.add(new Activation(t6, d6, TransitionOperation.DynamicDelay, ""));

        tf.GuardMappingList.add(g);
    }
}
