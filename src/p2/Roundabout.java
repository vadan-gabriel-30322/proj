package p2;

import Components.Activation;
import Components.Condition;
import Components.GuardMapping;
import Components.PetriNet;
import Components.PetriNetWindow;
import Components.PetriTransition;
import DataObjects.DataCar;
import DataObjects.DataCarQueue;
import DataOnly.CarQueue;
import Enumerations.LogicConnector;
import Enumerations.TransitionCondition;
import Enumerations.TransitionOperation;

public class Roundabout extends PetriNet {
    public PetriNet pn = new PetriNet();

    public Roundabout() {

        pn.PetriNetName = "Roundabout Petri Net";
        pn.NetworkPort = 1080;

        // ------------------------------------------------------------------------
        // Define Places
        // ------------------------------------------------------------------------

        // Lanes (DataCarQueue)
        DataCarQueue P1 = new DataCarQueue();
        P1.SetName("P1");
        P1.Value.Size = 3;
        pn.PlaceList.add(P1);

        DataCarQueue P2 = new DataCarQueue();
        P2.SetName("P2");
        P2.Value.Size = 3;
        pn.PlaceList.add(P2);

        DataCarQueue P3 = new DataCarQueue();
        P3.SetName("P3");
        P3.Value.Size = 3;
        pn.PlaceList.add(P3);

        // Input Buffers (DataCar)
        DataCar P5 = new DataCar();
        P5.SetName("P5");
        pn.PlaceList.add(P5);

        DataCar P7 = new DataCar();
        P7.SetName("P7");
        pn.PlaceList.add(P7);

        DataCar P8 = new DataCar();
        P8.SetName("P8");
        pn.PlaceList.add(P8);

        // Output Buffers (DataCar)
        DataCar P4 = new DataCar();
        P4.SetName("P4");
        pn.PlaceList.add(P4);

        DataCar P6 = new DataCar();
        P6.SetName("P6");
        pn. PlaceList.add(P6);

        DataCar P9 = new DataCar();
        P9.SetName("P9");
        pn.PlaceList.add(P9);

        // ------------------------------------------------------------------------
        // Define Transitions
        // ------------------------------------------------------------------------

        // t5: Input P5 -> P1
        PetriTransition t5 = new PetriTransition(pn);
        t5.TransitionName = "t5";
        t5.InputPlaceName.add("P5");


        Condition T5Ct1 = new Condition(t5, "P5", TransitionCondition.NotNull);
        Condition T5Ct2 = new Condition(t5, "P1", TransitionCondition.CanAddCars);
        T5Ct1.SetNextCondition(LogicConnector.AND, T5Ct2);

        GuardMapping grdT5 = new GuardMapping();
        grdT5.condition = T5Ct1;
        grdT5.Activations.add(new Activation(t5, "P5", TransitionOperation.AddElement, "P1"));
        t5.GuardMappingList.add(grdT5);
        t5.Delay = 1;
        pn.Transitions.add(t5);

        // t1: Move P1 -> P2
        PetriTransition t1 = new PetriTransition(pn);
        t1.TransitionName = "t1";
        t1.InputPlaceName.add("P1");


        Condition T1Ct1 = new Condition(t1, "P1", TransitionCondition.HaveCarForMe);
        Condition T1Ct2 = new Condition(t1, "P2", TransitionCondition.CanAddCars);
        T1Ct1.SetNextCondition(LogicConnector.AND, T1Ct2);

        GuardMapping grdT1 = new GuardMapping();
        grdT1.condition = T1Ct1;
        grdT1.Activations.add(new Activation(t1, "P1", TransitionOperation.PopElementWithTargetToQueue, "P2"));
        t1.GuardMappingList.add(grdT1);
        t1.Delay = 1;
        pn.Transitions.add(t1);

        // t4: Exit P1 -> P4
        PetriTransition t4 = new PetriTransition(pn);
        t4.TransitionName = "t4";
        t4.InputPlaceName.add("P1");

        Condition T4Ct1 = new Condition(t4, "P1", TransitionCondition.HaveCarForMe);

        GuardMapping grdT4 = new GuardMapping();
        grdT4.condition = T4Ct1;
        grdT4.Activations.add(new Activation(t4, "P1", TransitionOperation.PopElementWithTarget, "P4"));
        t4.GuardMappingList.add(grdT4);
        t4.Delay = 1;
        pn.Transitions.add(t4);

        // t8: Input P8 -> P3
        PetriTransition t8 = new PetriTransition(pn);
        t8.TransitionName = "t8";
        t8.InputPlaceName.add("P8");


        Condition T8Ct1 = new Condition(t8, "P8", TransitionCondition.NotNull);
        Condition T8Ct2 = new Condition(t8, "P3", TransitionCondition.CanAddCars);
        T8Ct1.SetNextCondition(LogicConnector.AND, T8Ct2);

        GuardMapping grdT8 = new GuardMapping();
        grdT8.condition = T8Ct1;
        grdT8.Activations.add(new Activation(t8, "P8", TransitionOperation.AddElement, "P3"));
        t8.GuardMappingList.add(grdT8);
        t8.Delay = 1;
        pn.Transitions.add(t8);

        // t3: Move P3 -> P1
        PetriTransition t3 = new PetriTransition(pn);
        t3.TransitionName = "t3";
        t3.InputPlaceName.add("P3");


        Condition T3Ct1 = new Condition(t3, "P3", TransitionCondition.HaveCarForMe);
        Condition T3Ct2 = new Condition(t3, "P1", TransitionCondition.CanAddCars);
        T3Ct1.SetNextCondition(LogicConnector.AND, T3Ct2);

        GuardMapping grdT3 = new GuardMapping();
        grdT3.condition = T3Ct1;
        grdT3.Activations.add(new Activation(t3, "P3", TransitionOperation.PopElementWithTargetToQueue, "P1"));
        t3.GuardMappingList.add(grdT3);
        t3.Delay = 1;
        pn.Transitions.add(t3);

        // t9: Exit P3 -> P9
        PetriTransition t9 = new PetriTransition(pn);
        t9.TransitionName = "t9";
        t9.InputPlaceName.add("P3");

        Condition T9Ct1 = new Condition(t9, "P3", TransitionCondition.HaveCarForMe);

        GuardMapping grdT9 = new GuardMapping();
        grdT9.condition = T9Ct1;
        grdT9.Activations.add(new Activation(t9, "P3", TransitionOperation.PopElementWithTarget, "P9"));
        t9.GuardMappingList.add(grdT9);
        t9.Delay = 1;
        pn.Transitions.add(t9);

        // t7: Input P7 -> P2
        PetriTransition t7 = new PetriTransition(pn);
        t7.TransitionName = "t7";

        t7.InputPlaceName.add("P2");


        Condition T7Ct1 = new Condition(t7, "P2", TransitionCondition.HaveCarForMe);


        GuardMapping grdT7 = new GuardMapping();
        grdT7.condition = T7Ct1;
        grdT7.Activations.add(new Activation(t7, "P2", TransitionOperation.PopElementWithTarget, "P7"));
        t7.GuardMappingList.add(grdT7);
        t7.Delay = 1;
        pn.Transitions.add(t7);

        // t2: Move P2 -> P3
        PetriTransition t2 = new PetriTransition(pn);
        t2.TransitionName = "t2";
        t2.InputPlaceName.add("P2");
        t2.InputPlaceName.add("P3");

        Condition T2Ct1 = new Condition(t2, "P2", TransitionCondition.HaveCarForMe);
        Condition T2Ct2 = new Condition(t3, "P3", TransitionCondition.CanAddCars);
        T2Ct1.SetNextCondition(LogicConnector.AND, T2Ct2);

        GuardMapping grdT2 = new GuardMapping();
        grdT2.condition = T2Ct1;
        grdT2.Activations.add(new Activation(t2, "P2", TransitionOperation.PopElementWithTargetToQueue, "P3"));
        t2.GuardMappingList.add(grdT2);
        t2.Delay = 1;
        pn.Transitions.add(t2);

        // t6: Exit P2 -> P6
        PetriTransition t6 = new PetriTransition(pn);
        t6.TransitionName = "t6";
        t6.InputPlaceName.add("P2");

        Condition T6Ct1 = new Condition(t6, "P2", TransitionCondition.HaveCarForMe);

        GuardMapping grdT6 = new GuardMapping();
        grdT6.condition = T6Ct1;
        grdT6.Activations.add(new Activation(t6, "P2", TransitionOperation.PopElementWithTarget, "P6"));
        t6.GuardMappingList.add(grdT6);
        t6.Delay = 1;
        pn.Transitions.add(t6);
    }

    public static void main(String[] args) {
        Roundabout roundabout = new Roundabout();
        roundabout.pn.Delay = 2000;
        PetriNetWindow frame = new PetriNetWindow(false);
        frame.petriNet = roundabout.pn;
        frame.setVisible(true);

//        InputCar inputCar = new InputCar(roundabout);
//        inputCar.setVisible(true);
    }
}
