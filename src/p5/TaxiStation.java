package p5;

import Components.Activation;
import Components.Condition;
import Components.GuardMapping;
import Components.PetriNet;
import Components.PetriNetWindow;
import Components.PetriTransition;
import DataObjects.DataCar;
import DataObjects.DataCarQueue;
import DataObjects.DataString;
import DataObjects.DataTransfer;
import DataOnly.TransferOperation;
import Enumerations.LogicConnector;
import Enumerations.TransitionCondition;
import Enumerations.TransitionOperation;

public class TaxiStation {
    public static void main(String[] args) {

        PetriNet pn = new PetriNet();
        pn.PetriNetName = "Taxi Station";
        pn.NetworkPort = 1084;

        // -------------------------------------------------------------------
        // -------------------------------Places--------------------------------
        // --------------------------------------------------------------------

        DataCar p1 = new DataCar();
        p1.SetName("P_a");
        pn.PlaceList.add(p1);

        DataCarQueue p2 = new DataCarQueue();
        p2.Value.Size = 3;
        p2.SetName("P_x1");
        pn.PlaceList.add(p2);

        DataCarQueue p3 = new DataCarQueue();
        p3.Value.Size = 3;
        p3.SetName("P_Station");
        pn.PlaceList.add(p3);

        DataCarQueue p4 = new DataCarQueue();
        p4.Value.Size = 3;
        p4.SetName("P_Rs"); // Road segment after station?
        pn.PlaceList.add(p4);

        DataCarQueue p5 = new DataCarQueue();
        p5.Value.Size = 3;
        p5.SetName("P_Tr"); // Transfer/Transition area?
        pn.PlaceList.add(p5);

        DataCar p6 = new DataCar();
        p6.SetName("P_o"); // Bypass exit
        pn.PlaceList.add(p6);

        DataCar p7 = new DataCar();
        p7.SetName("PT_2"); // Target 2
        pn.PlaceList.add(p7);

        DataCar p8 = new DataCar();
        p8.SetName("PT_3"); // Target 3
        pn.PlaceList.add(p8);

        // -------------------------------------------------------------------
        // ---------------------------Transitions------------------------------
        // --------------------------------------------------------------------

        // T_in: Entry to P_x1
        PetriTransition t_in = new PetriTransition(pn);
        t_in.TransitionName = "T_in";
        t_in.InputPlaceName.add("P_a");
        t_in.InputPlaceName.add("P_x1");

        Condition TinCt1 = new Condition(t_in, "P_a", TransitionCondition.NotNull);
        Condition TinCt2 = new Condition(t_in, "P_x1", TransitionCondition.CanAddCars);
        TinCt1.SetNextCondition(LogicConnector.AND, TinCt2);

        GuardMapping grdTin = new GuardMapping();
        grdTin.condition = TinCt1;
        grdTin.Activations.add(new Activation(t_in, "P_a", TransitionOperation.AddElement, "P_x1"));
        t_in.GuardMappingList.add(grdTin);
        t_in.Delay = 0;
        pn.Transitions.add(t_in);

        // T_S: P_x1 -> P_Station (Only Taxis that want to enter)
        PetriTransition t_s = new PetriTransition(pn);
        t_s.TransitionName = "T_S";
        t_s.InputPlaceName.add("P_x1");
        t_s.InputPlaceName.add("P_Station");

        Condition TsCt1 = new Condition(t_s, "P_x1", TransitionCondition.HaveTaxi); // Check for Taxi
        Condition TsCt2 = new Condition(t_s, "P_Station", TransitionCondition.CanAddCars);
        Condition TsCt3 = new Condition(t_s, "P_x1", TransitionCondition.HaveCarForMe); // Ensure target is T_S

        TsCt1.SetNextCondition(LogicConnector.AND, TsCt2);
        TsCt2.SetNextCondition(LogicConnector.AND, TsCt3);

        GuardMapping grdTs = new GuardMapping();
        grdTs.condition = TsCt1;
        grdTs.Activations
                .add(new Activation(t_s, "P_x1", TransitionOperation.PopElementWithTargetToQueue, "P_Station"));
        t_s.GuardMappingList.add(grdTs);
        t_s.Delay = 0;
        pn.Transitions.add(t_s);

        // T_o: P_x1 -> P_o (Bypass)
        PetriTransition t_o = new PetriTransition(pn);
        t_o.TransitionName = "T_o";
        t_o.InputPlaceName.add("P_x1");

        Condition ToCt1 = new Condition(t_o, "P_x1", TransitionCondition.HaveCar);
        Condition ToCt2 = new Condition(t_o, "P_x1", TransitionCondition.HaveCarForMe);
        ToCt1.SetNextCondition(LogicConnector.AND, ToCt2);

        GuardMapping grdTo = new GuardMapping();
        grdTo.condition = ToCt1;
        grdTo.Activations.add(new Activation(t_o, "P_x1", TransitionOperation.PopElementWithTarget, "P_o"));

        t_o.GuardMappingList.add(grdTo);
        t_o.Delay = 0;
        pn.Transitions.add(t_o);

        // T_es: P_Station -> P_Rs
        PetriTransition t_es = new PetriTransition(pn);
        t_es.TransitionName = "T_es";
        t_es.InputPlaceName.add("P_Station");
        t_es.InputPlaceName.add("P_Rs");

        Condition TesCt1 = new Condition(t_es, "P_Station", TransitionCondition.HaveCar);
        Condition TesCt2 = new Condition(t_es, "P_Rs", TransitionCondition.CanAddCars);
        TesCt1.SetNextCondition(LogicConnector.AND, TesCt2);

        GuardMapping grdTes = new GuardMapping();
        grdTes.condition = TesCt1;
        grdTes.Activations
                .add(new Activation(t_es, "P_Station", TransitionOperation.PopElementWithTargetToQueue, "P_Rs"));

        t_es.GuardMappingList.add(grdTes);
        t_es.Delay = 1;
        pn.Transitions.add(t_es);

        // t_1: P_Rs -> P_Tr
        PetriTransition t_1 = new PetriTransition(pn);
        t_1.TransitionName = "t_1";
        t_1.InputPlaceName.add("P_Rs");
        t_1.InputPlaceName.add("P_Tr");

        Condition T1Ct1 = new Condition(t_1, "P_Rs", TransitionCondition.HaveCar);
        Condition T1Ct2 = new Condition(t_1, "P_Tr", TransitionCondition.CanAddCars);
        T1Ct1.SetNextCondition(LogicConnector.AND, T1Ct2);

        GuardMapping grdT1 = new GuardMapping();
        grdT1.condition = T1Ct1;
        grdT1.Activations.add(new Activation(t_1, "P_Rs", TransitionOperation.PopElementWithTargetToQueue, "P_Tr"));

        t_1.GuardMappingList.add(grdT1);
        t_1.Delay = 0;
        pn.Transitions.add(t_1);

        // T_2: P_Tr -> PT_2
        PetriTransition t_2 = new PetriTransition(pn);
        t_2.TransitionName = "T_2";
        t_2.InputPlaceName.add("P_Tr");

        Condition T2Ct1 = new Condition(t_2, "P_Tr", TransitionCondition.HaveCarForMe); // Target T_2

        GuardMapping grdT2 = new GuardMapping();
        grdT2.condition = T2Ct1;
        grdT2.Activations.add(new Activation(t_2, "P_Tr", TransitionOperation.PopElementWithTarget, "PT_2"));

        t_2.GuardMappingList.add(grdT2);
        t_2.Delay = 0;
        pn.Transitions.add(t_2);

        // T_3: P_Tr -> PT_3
        PetriTransition t_3 = new PetriTransition(pn);
        t_3.TransitionName = "T_3";
        t_3.InputPlaceName.add("P_Tr");

        Condition T3Ct1 = new Condition(t_3, "P_Tr", TransitionCondition.HaveCarForMe); // Target T_3

        GuardMapping grdT3 = new GuardMapping();
        grdT3.condition = T3Ct1;
        grdT3.Activations.add(new Activation(t_3, "P_Tr", TransitionOperation.PopElementWithTarget, "PT_3"));

        t_3.GuardMappingList.add(grdT3);
        t_3.Delay = 0;
        pn.Transitions.add(t_3);

        // -------------------------------------------------------------------------------------
        // ----------------------------PNStart-------------------------------------------------
        // -------------------------------------------------------------------------------------

        System.out.println("Taxi Station started \n ------------------------------");
        pn.Delay = 2000;

        PetriNetWindow frame = new PetriNetWindow(false);
        frame.petriNet = pn;
        frame.setVisible(true);

        InputCarP5 inputCar = new InputCarP5(pn);
        inputCar.setVisible(true);
    }
}
