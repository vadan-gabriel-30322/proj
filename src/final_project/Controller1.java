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

public class Controller1 {

    public static void main(String[] args) {
        PetriNet pn = new PetriNet();
        pn.PetriNetName = "Controller 1 (Dynamic, 4 Lanes)";
        pn.SetName("Controller 1");
        pn.NetworkPort = 1091; // Port for Controller 1

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
        p1.SetName("r1r2r3r4");
        p1.SetValue("signal");
        pn.PlaceList.add(p1);

        DataString p2 = new DataString();
        p2.SetName("g1r2r3r4");
        pn.PlaceList.add(p2);

        DataString p3 = new DataString();
        p3.SetName("y1r2r3r4");
        pn.PlaceList.add(p3);

        DataString p4 = new DataString();
        p4.SetName("r1g2r3r4");
        pn.PlaceList.add(p4);

        DataString p5 = new DataString();
        p5.SetName("r1y2r3r4");
        pn.PlaceList.add(p5);

        DataString p6 = new DataString();
        p6.SetName("r1r2g3r4");
        pn.PlaceList.add(p6);

        DataString p7 = new DataString();
        p7.SetName("r1r2y3r4");
        pn.PlaceList.add(p7);

        DataString p8 = new DataString();
        p8.SetName("r1r2r3g4");
        pn.PlaceList.add(p8);

        DataString p9 = new DataString();
        p9.SetName("r1r2r3y4");
        pn.PlaceList.add(p9);

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

        DataString in4 = new DataString();
        in4.SetName("in4");
        pn.PlaceList.add(in4);

        // Output places - Targeting 1081 (Intersection 1)
        DataTransfer op1 = new DataTransfer();
        op1.SetName("OP1");
        op1.Value = new TransferOperation("localhost", "1081", "P_TL1");
        pn.PlaceList.add(op1);

        DataTransfer op2 = new DataTransfer();
        op2.SetName("OP2");
        op2.Value = new TransferOperation("localhost", "1081", "P_TL2");
        pn.PlaceList.add(op2);

        DataTransfer op3 = new DataTransfer();
        op3.SetName("OP3");
        op3.Value = new TransferOperation("localhost", "1081", "P_TL3");
        pn.PlaceList.add(op3);

        DataTransfer op4 = new DataTransfer();
        op4.SetName("OP4");
        op4.Value = new TransferOperation("localhost", "1081", "P_TL4");
        pn.PlaceList.add(op4);

        // ----------------------------iniT------------------------------------
        PetriTransition iniT = new PetriTransition(pn);
        iniT.TransitionName = "iniT";

        Condition iniTCt1 = new Condition(iniT, "ini", TransitionCondition.NotNull);

        GuardMapping grdiniT = new GuardMapping();
        grdiniT.condition = iniTCt1;

        grdiniT.Activations.add(new Activation(iniT, "ini", TransitionOperation.SendOverNetwork, "OP1"));
        grdiniT.Activations.add(new Activation(iniT, "ini", TransitionOperation.SendOverNetwork, "OP2"));
        grdiniT.Activations.add(new Activation(iniT, "ini", TransitionOperation.SendOverNetwork, "OP3"));
        grdiniT.Activations.add(new Activation(iniT, "ini", TransitionOperation.SendOverNetwork, "OP4"));
        grdiniT.Activations.add(new Activation(iniT, "", TransitionOperation.MakeNull, "ini"));

        iniT.GuardMappingList.add(grdiniT);

        iniT.Delay = 0;
        pn.Transitions.add(iniT);

        // ----------------------------T1------------------------------------
        // r1r2r3r4 -> g1r2r3r4 (OP1 Green) AND pf
        PetriTransition t1 = new PetriTransition(pn);
        t1.TransitionName = "T1";
        t1.InputPlaceName.add("r1r2r3r4");

        Condition T1Ct1 = new Condition(t1, "r1r2r3r4", TransitionCondition.NotNull);

        GuardMapping grdT1 = new GuardMapping();
        grdT1.condition = T1Ct1;
        grdT1.Activations.add(new Activation(t1, "r1r2r3r4", TransitionOperation.Move, "g1r2r3r4"));
        grdT1.Activations.add(new Activation(t1, "green", TransitionOperation.SendOverNetwork, "OP1"));
        grdT1.Activations.add(new Activation(t1, "r1r2r3r4", TransitionOperation.Move, "pf")); // Move to pf
        t1.GuardMappingList.add(grdT1);

        t1.Delay = 5;
        pn.Transitions.add(t1);

        // ----------------------------T2------------------------------------
        // g1r2r3r4 -> y1r2r3r4 (OP1 Yellow)
        PetriTransition t2 = new PetriTransition(pn);
        t2.TransitionName = "T2";
        t2.InputPlaceName.add("g1r2r3r4");

        Condition T2Ct1 = new Condition(t2, "g1r2r3r4", TransitionCondition.NotNull);

        GuardMapping grdT2 = new GuardMapping();
        grdT2.condition = T2Ct1;
        grdT2.Activations.add(new Activation(t2, "g1r2r3r4", TransitionOperation.Move, "y1r2r3r4"));
        grdT2.Activations.add(new Activation(t2, "yellow", TransitionOperation.SendOverNetwork, "OP1"));

        t2.GuardMappingList.add(grdT2);

        t2.Delay = 5; // Default delay, modified by tf
        pn.Transitions.add(t2);

        // ----------------------------T3------------------------------------
        // y1r2r3r4 -> r1g2r3r4 (OP1 Red, OP2 Green)
        PetriTransition t3 = new PetriTransition(pn);
        t3.TransitionName = "T3";
        t3.InputPlaceName.add("y1r2r3r4");

        Condition T3Ct1 = new Condition(t3, "y1r2r3r4", TransitionCondition.NotNull);

        GuardMapping grdT3 = new GuardMapping();
        grdT3.condition = T3Ct1;
        grdT3.Activations.add(new Activation(t3, "y1r2r3r4", TransitionOperation.Move, "r1g2r3r4"));
        grdT3.Activations.add(new Activation(t3, "red", TransitionOperation.SendOverNetwork, "OP1"));
        grdT3.Activations.add(new Activation(t3, "green", TransitionOperation.SendOverNetwork, "OP2"));

        t3.GuardMappingList.add(grdT3);

        t3.Delay = 5;
        pn.Transitions.add(t3);

        // ----------------------------T4------------------------------------
        // r1g2r3r4 -> r1y2r3r4 (OP2 Yellow)
        PetriTransition t4 = new PetriTransition(pn);
        t4.TransitionName = "T4";
        t4.InputPlaceName.add("r1g2r3r4");

        Condition T4Ct1 = new Condition(t4, "r1g2r3r4", TransitionCondition.NotNull);

        GuardMapping grdT4 = new GuardMapping();
        grdT4.condition = T4Ct1;
        grdT4.Activations.add(new Activation(t4, "r1g2r3r4", TransitionOperation.Move, "r1y2r3r4"));
        grdT4.Activations.add(new Activation(t4, "yellow", TransitionOperation.SendOverNetwork, "OP2"));

        t4.GuardMappingList.add(grdT4);

        t4.Delay = 5; // Default delay, modified by tf
        pn.Transitions.add(t4);

        // ----------------------------T5------------------------------------
        // r1y2r3r4 -> r1r2g3r4 (OP2 Red, OP3 Green)
        PetriTransition t5 = new PetriTransition(pn);
        t5.TransitionName = "T5";
        t5.InputPlaceName.add("r1y2r3r4");

        Condition T5Ct1 = new Condition(t5, "r1y2r3r4", TransitionCondition.NotNull);

        GuardMapping grdT5 = new GuardMapping();
        grdT5.condition = T5Ct1;
        grdT5.Activations.add(new Activation(t5, "r1y2r3r4", TransitionOperation.Move, "r1r2g3r4"));
        grdT5.Activations.add(new Activation(t5, "red", TransitionOperation.SendOverNetwork, "OP2"));
        grdT5.Activations.add(new Activation(t5, "green", TransitionOperation.SendOverNetwork, "OP3"));

        t5.GuardMappingList.add(grdT5);

        t5.Delay = 5;
        pn.Transitions.add(t5);

        // ----------------------------T6------------------------------------
        // r1r2g3r4 -> r1r2y3r4 (OP3 Yellow)
        PetriTransition t6 = new PetriTransition(pn);
        t6.TransitionName = "T6";
        t6.InputPlaceName.add("r1r2g3r4");

        Condition T6Ct1 = new Condition(t6, "r1r2g3r4", TransitionCondition.NotNull);

        GuardMapping grdT6 = new GuardMapping();
        grdT6.condition = T6Ct1;
        grdT6.Activations.add(new Activation(t6, "r1r2g3r4", TransitionOperation.Move, "r1r2y3r4"));
        grdT6.Activations.add(new Activation(t6, "yellow", TransitionOperation.SendOverNetwork, "OP3"));

        t6.GuardMappingList.add(grdT6);

        t6.Delay = 5; // Default delay, modified by tf
        pn.Transitions.add(t6);

        // ----------------------------T7------------------------------------
        // r1r2y3r4 -> r1r2r3g4 (OP3 Red, OP4 Green)
        PetriTransition t7 = new PetriTransition(pn);
        t7.TransitionName = "T7";
        t7.InputPlaceName.add("r1r2y3r4");

        Condition T7Ct1 = new Condition(t7, "r1r2y3r4", TransitionCondition.NotNull);

        GuardMapping grdT7 = new GuardMapping();
        grdT7.condition = T7Ct1;
        grdT7.Activations.add(new Activation(t7, "r1r2y3r4", TransitionOperation.Move, "r1r2r3g4"));
        grdT7.Activations.add(new Activation(t7, "red", TransitionOperation.SendOverNetwork, "OP3"));
        grdT7.Activations.add(new Activation(t7, "green", TransitionOperation.SendOverNetwork, "OP4"));

        t7.GuardMappingList.add(grdT7);

        t7.Delay = 5;
        pn.Transitions.add(t7);

        // ----------------------------T8------------------------------------
        // r1r2r3g4 -> r1r2r3y4 (OP4 Yellow)
        PetriTransition t8 = new PetriTransition(pn);
        t8.TransitionName = "T8";
        t8.InputPlaceName.add("r1r2r3g4");

        Condition T8Ct1 = new Condition(t8, "r1r2r3g4", TransitionCondition.NotNull);

        GuardMapping grdT8 = new GuardMapping();
        grdT8.condition = T8Ct1;
        grdT8.Activations.add(new Activation(t8, "r1r2r3g4", TransitionOperation.Move, "r1r2r3y4"));
        grdT8.Activations.add(new Activation(t8, "yellow", TransitionOperation.SendOverNetwork, "OP4"));

        t8.GuardMappingList.add(grdT8);

        t8.Delay = 5; // Default delay, modified by tf
        pn.Transitions.add(t8);

        // ----------------------------T9------------------------------------
        // r1r2r3y4 -> r1r2r3r4 (OP4 Red)
        PetriTransition t9 = new PetriTransition(pn);
        t9.TransitionName = "T9";
        t9.InputPlaceName.add("r1r2r3y4");

        Condition T9Ct1 = new Condition(t9, "r1r2r3y4", TransitionCondition.NotNull);

        GuardMapping grdT9 = new GuardMapping();
        grdT9.condition = T9Ct1;
        grdT9.Activations.add(new Activation(t9, "r1r2r3y4", TransitionOperation.Move, "r1r2r3r4"));
        grdT9.Activations.add(new Activation(t9, "red", TransitionOperation.SendOverNetwork, "OP4"));

        t9.GuardMappingList.add(grdT9);

        t9.Delay = 5;
        pn.Transitions.add(t9);

        // ----------------------------tf (Control
        // Transition)------------------------------------
        PetriTransition tf = new PetriTransition(pn);
        tf.TransitionName = "tf";
        tf.InputPlaceName.add("pf");
        tf.InputPlaceName.add("in1");
        tf.InputPlaceName.add("in2");
        tf.InputPlaceName.add("in3");
        tf.InputPlaceName.add("in4");

        // Add guards for all cases
        // Case 1: 0 0 0 0 -> 5 5 5 5
        addGuard(tf, t2, t4, t6, t8, 0, 0, 0, 0, "Five", "Five", "Five", "Five");

        // Case 2: 1 0 0 0 -> 11 3 3 3
        addGuard(tf, t2, t4, t6, t8, 1, 0, 0, 0, "Eleven", "Three", "Three", "Three");

        // Case 3: 0 1 0 0 -> 3 11 3 3
        addGuard(tf, t2, t4, t6, t8, 0, 1, 0, 0, "Three", "Eleven", "Three", "Three");

        // Case 4: 0 0 1 0 -> 3 3 11 3
        addGuard(tf, t2, t4, t6, t8, 0, 0, 1, 0, "Three", "Three", "Eleven", "Three");

        // Case 5: 0 0 0 1 -> 3 3 3 11
        addGuard(tf, t2, t4, t6, t8, 0, 0, 0, 1, "Three", "Three", "Three", "Eleven");

        // Case 6: 1 1 0 0 -> 8 8 2 2
        addGuard(tf, t2, t4, t6, t8, 1, 1, 0, 0, "Eight", "Eight", "Two", "Two");

        // Case 7: 1 0 1 0 -> 8 2 8 2
        addGuard(tf, t2, t4, t6, t8, 1, 0, 1, 0, "Eight", "Two", "Eight", "Two");

        // Case 8: 1 0 0 1 -> 8 2 2 8
        addGuard(tf, t2, t4, t6, t8, 1, 0, 0, 1, "Eight", "Two", "Two", "Eight");

        // Case 9: 0 1 1 0 -> 2 8 8 2
        addGuard(tf, t2, t4, t6, t8, 0, 1, 1, 0, "Two", "Eight", "Eight", "Two");

        // Case 10: 0 1 0 1 -> 2 8 2 8
        addGuard(tf, t2, t4, t6, t8, 0, 1, 0, 1, "Two", "Eight", "Two", "Eight");

        // Case 11: 0 0 1 1 -> 2 2 8 8
        addGuard(tf, t2, t4, t6, t8, 0, 0, 1, 1, "Two", "Two", "Eight", "Eight");

        // Case 12: 0 1 1 1 -> 2 6 6 6
        addGuard(tf, t2, t4, t6, t8, 0, 1, 1, 1, "Two", "Six", "Six", "Six");

        // Case 13: 1 0 1 1 -> 6 2 6 6
        addGuard(tf, t2, t4, t6, t8, 1, 0, 1, 1, "Six", "Two", "Six", "Six");

        // Case 14: 1 1 0 1 -> 6 6 2 6
        addGuard(tf, t2, t4, t6, t8, 1, 1, 0, 1, "Six", "Six", "Two", "Six");

        // Case 15: 1 1 1 0 -> 6 6 6 2
        addGuard(tf, t2, t4, t6, t8, 1, 1, 1, 0, "Six", "Six", "Six", "Two");

        // Case 16: 1 1 1 1 -> 5 5 5 5
        addGuard(tf, t2, t4, t6, t8, 1, 1, 1, 1, "Five", "Five", "Five", "Five");

        tf.Delay = 0;
        pn.Transitions.add(tf);

        // -------------------------------------------------------------------------------------
        // ----------------------------PN
        // Start-------------------------------------------------
        // -------------------------------------------------------------------------------------

        System.out.println("Controller 1 started \n ------------------------------");
        pn.Delay = 2000;
        // pn.Start();

        PetriNetWindow frame = new PetriNetWindow(false);
        frame.petriNet = pn;
        frame.setVisible(true);
    }

    private static void addGuard(PetriTransition tf, PetriTransition t2, PetriTransition t4, PetriTransition t6,
            PetriTransition t8,
            int i1, int i2, int i3, int i4,
            String d2, String d4, String d6, String d8) {

        Condition cPf = new Condition(tf, "pf", TransitionCondition.NotNull);
        Condition c1 = new Condition(tf, "in1", i1 == 1 ? TransitionCondition.NotNull : TransitionCondition.IsNull);
        Condition c2 = new Condition(tf, "in2", i2 == 1 ? TransitionCondition.NotNull : TransitionCondition.IsNull);
        Condition c3 = new Condition(tf, "in3", i3 == 1 ? TransitionCondition.NotNull : TransitionCondition.IsNull);
        Condition c4 = new Condition(tf, "in4", i4 == 1 ? TransitionCondition.NotNull : TransitionCondition.IsNull);

        cPf.SetNextCondition(LogicConnector.AND, c1);
        c1.SetNextCondition(LogicConnector.AND, c2);
        c2.SetNextCondition(LogicConnector.AND, c3);
        c3.SetNextCondition(LogicConnector.AND, c4);

        GuardMapping g = new GuardMapping();
        g.condition = cPf;

        g.Activations.add(new Activation(t2, d2, TransitionOperation.DynamicDelay, ""));
        g.Activations.add(new Activation(t4, d4, TransitionOperation.DynamicDelay, ""));
        g.Activations.add(new Activation(t6, d6, TransitionOperation.DynamicDelay, ""));
        g.Activations.add(new Activation(t8, d8, TransitionOperation.DynamicDelay, ""));

        tf.GuardMappingList.add(g);
    }
}
