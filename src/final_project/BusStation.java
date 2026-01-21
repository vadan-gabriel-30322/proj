package final_project;

import Components.Activation;
import Components.Condition;
import Components.GuardMapping;
import Components.PetriNet;
import Components.PetriNetWindow;
import Components.PetriTransition;
import DataObjects.DataCar;
import DataObjects.DataCarQueue;
import Enumerations.LogicConnector;
import Enumerations.TransitionCondition;
import Enumerations.TransitionOperation;
import java.util.ArrayList;

public class BusStation {
    public static void main(String[] args) {

        PetriNet pn = new PetriNet();
        pn.PetriNetName = "Bus Station";
        pn.NetworkPort = java.lang.Integer.valueOf(1084);

        DataCar p1 = new DataCar();
        p1.SetName("P_a");
        pn.PlaceList.add(p1);

        DataCarQueue p2 = new DataCarQueue();
        p2.Value.Size = java.lang.Integer.valueOf(3);
        p2.SetName("P_x1");
        pn.PlaceList.add(p2);

        DataCarQueue p3 = new DataCarQueue();
        p3.Value.Size = java.lang.Integer.valueOf(3);
        p3.SetName("P_Station");
        pn.PlaceList.add(p3);

        DataCarQueue p4 = new DataCarQueue();
        p4.Value.Size = java.lang.Integer.valueOf(3);
        p4.SetName("P_Rs");
        pn.PlaceList.add(p4);

        DataCarQueue p5 = new DataCarQueue();
        p5.Value.Size = java.lang.Integer.valueOf(3);
        p5.SetName("P_Tr");
        pn.PlaceList.add(p5);

        DataCar p6 = new DataCar();
        p6.SetName("P_o");
        pn.PlaceList.add(p6);

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
        t_in.Delay = java.lang.Integer.valueOf(0);
        pn.Transitions.add(t_in);

        PetriTransition t_s = new PetriTransition(pn);
        t_s.TransitionName = "T_S";
        t_s.InputPlaceName.add("P_x1");
        t_s.InputPlaceName.add("P_Station");

        Condition TsCt1 = new Condition(t_s, "P_x1", TransitionCondition.HaveBus);
        Condition TsCt2 = new Condition(t_s, "P_Station", TransitionCondition.CanAddCars);
        Condition TsCt3 = new Condition(t_s, "P_x1", TransitionCondition.HavePriorityCar);

        TsCt1.SetNextCondition(LogicConnector.AND, TsCt2);
        TsCt2.SetNextCondition(LogicConnector.AND, TsCt3);

        GuardMapping grdTs = new GuardMapping();
        grdTs.condition = TsCt1;
        grdTs.Activations
                .add(new Activation(t_s, "P_x1", TransitionOperation.PopElementWithTargetToQueue, "P_Station"));
        t_s.GuardMappingList.add(grdTs);
        t_s.Delay = java.lang.Integer.valueOf(0);
        pn.Transitions.add(t_s);

        PetriTransition t_o = new PetriTransition(pn);
        t_o.TransitionName = "T_o";
        t_o.InputPlaceName.add("P_x1");

        Condition ToCt1 = new Condition(t_o, "P_x1", TransitionCondition.HaveCar);

        GuardMapping grdTo = new GuardMapping();
        grdTo.condition = ToCt1;
        grdTo.Activations.add(new Activation(t_o, "P_x1", TransitionOperation.PopElementWithTarget, "P_o"));

        t_o.GuardMappingList.add(grdTo);
        t_o.Delay = java.lang.Integer.valueOf(0);
        pn.Transitions.add(t_o);

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
        t_es.Delay = java.lang.Integer.valueOf(10);
        pn.Transitions.add(t_es);

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
        t_1.Delay = java.lang.Integer.valueOf(0);
        pn.Transitions.add(t_1);

        System.out.println("Bus Station started \n ------------------------------");
        pn.Delay = java.lang.Integer.valueOf(2000);

        PetriNetWindow frame = new PetriNetWindow(false);
        frame.petriNet = pn;
        frame.setVisible(true);

        InputCarFP inputCar = new InputCarFP(pn);
        inputCar.setVisible(true);
    }
}
