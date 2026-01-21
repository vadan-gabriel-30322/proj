package lab4_2;

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

public class Exercise2 {
    public static void main(String[] args) throws FileNotFoundException {
        PetriNet pn = new PetriNet();
        pn.PetriNetName = "Heater Tank Plant Optimized (Exercise 2)";
        pn.NetworkPort = 1081;

        // ... (Same FLRS definitions as HeaterTankPlant.java) ...
        FLRS doubleChannelAdder = new FLRS(new FV(FZ.NL, FZ.NL), new FV(FZ.NL, FZ.NL), new FV(FZ.NL, FZ.NL),
                new FV(FZ.NM, FZ.NM), new FV(FZ.ZR, FZ.ZR),
                new FV(FZ.NL, FZ.NL), new FV(FZ.NL, FZ.NL), new FV(FZ.NM, FZ.NM), new FV(FZ.ZR, FZ.ZR),
                new FV(FZ.PM, FZ.PM),
                new FV(FZ.NL, FZ.NL), new FV(FZ.NM, FZ.NM), new FV(FZ.ZR, FZ.ZR), new FV(FZ.PM, FZ.PM),
                new FV(FZ.PL, FZ.PL),
                new FV(FZ.NM, FZ.NM), new FV(FZ.ZR, FZ.ZR), new FV(FZ.PM, FZ.PM), new FV(FZ.PL, FZ.PL),
                new FV(FZ.PL, FZ.PL),
                new FV(FZ.ZR, FZ.ZR), new FV(FZ.PM, FZ.PM), new FV(FZ.PL, FZ.PL), new FV(FZ.PL, FZ.PL),
                new FV(FZ.PL, FZ.PL));

        FLRS OneXOneDefaultTable = new FLRS(new FV(FZ.NL), new FV(FZ.NM), new FV(FZ.ZR), new FV(FZ.PM), new FV(FZ.PL));

        DataFuzzy x2old = new DataFuzzy();
        x2old.SetName("x2old");
        x2old.SetValue(new Fuzzy(0.3F));
        pn.PlaceList.add(x2old);
        DataFuzzy x2 = new DataFuzzy();
        x2.SetName("x2");
        pn.PlaceList.add(x2);
        DataFuzzy p0 = new DataFuzzy();
        p0.SetName("p0");
        pn.PlaceList.add(p0);
        DataFuzzy u = new DataFuzzy();
        u.SetName("u");
        pn.PlaceList.add(u);
        DataFuzzy runningState = new DataFuzzy();
        runningState.SetName("Running State");
        pn.PlaceList.add(runningState);

        DataTransfer sendHeat = new DataTransfer();
        sendHeat.SetName("sendHeat");
        sendHeat.Value = new TransferOperation("localhost", "1080", "c");
        pn.PlaceList.add(sendHeat);
        DataTransfer sendWaterTemp = new DataTransfer();
        sendWaterTemp.SetName("sendTemp");
        sendWaterTemp.Value = new TransferOperation("localhost", "1083", "P3");
        pn.PlaceList.add(sendWaterTemp);

        // t_21 - Optimized Constants
        PetriTransition t_21 = new PetriTransition(pn);
        t_21.TransitionName = "t_21";
        t_21.InputPlaceName.add("x2old");
        t_21.InputPlaceName.add("u");
        Condition T_12Ct1 = new Condition(t_21, "x2old", TransitionCondition.NotNull);
        Condition T_12Ct2 = new Condition(t_21, "u", TransitionCondition.NotNull);
        T_12Ct1.SetNextCondition(LogicConnector.AND, T_12Ct2);
        GuardMapping grdt_12 = new GuardMapping();
        grdt_12.condition = T_12Ct1;
        ArrayList<PlaceNameWithWeight> input1 = new ArrayList<>();
        // OPTIMIZATION: Changed weight of x2old from 0.1F to 0.2F
        input1.add(new PlaceNameWithWeight("x2old", 0.2F));
        input1.add(new PlaceNameWithWeight("u", 0.4F));
        ArrayList<String> output1 = new ArrayList<>();
        output1.add("p0");
        output1.add("x2");
        grdt_12.Activations.add(new Activation(t_21, doubleChannelAdder, input1, TransitionOperation.FLRS, output1));
        t_21.GuardMappingList.add(grdt_12);
        t_21.Delay = 0;
        pn.Transitions.add(t_21);

        // t_22
        PetriTransition t_22 = new PetriTransition(pn);
        t_22.TransitionName = "t_22";
        t_22.InputPlaceName.add("x2");
        Condition T_22Ct1 = new Condition(t_22, "x2", TransitionCondition.NotNull);
        GuardMapping grdt_22 = new GuardMapping();
        grdt_22.condition = T_22Ct1;
        grdt_22.Activations.add(new Activation(t_22, "x2", TransitionOperation.SendOverNetwork, "sendHeat"));
        grdt_22.Activations.add(new Activation(t_22, "x2", TransitionOperation.SendOverNetwork, "sendTemp"));
        t_22.GuardMappingList.add(grdt_22);
        t_22.Delay = 1;
        pn.Transitions.add(t_22);

        // t_23
        PetriTransition t_23 = new PetriTransition(pn);
        t_23.TransitionName = "t_23";
        t_23.InputPlaceName.add("p0");
        Condition T_23Ct1 = new Condition(t_23, "p0", TransitionCondition.NotNull);
        GuardMapping grdt_23 = new GuardMapping();
        grdt_23.condition = T_23Ct1;
        ArrayList<PlaceNameWithWeight> input2 = new ArrayList<>();
        input2.add(new PlaceNameWithWeight("p0", 0.1F));
        ArrayList<String> output2 = new ArrayList<>();
        output2.add("x2old");
        grdt_23.Activations.add(new Activation(t_23, OneXOneDefaultTable, input2, TransitionOperation.FLRS, output2));
        t_23.GuardMappingList.add(grdt_23);
        t_23.Delay = 1;
        pn.Transitions.add(t_23);

        System.out.println("Heater Tank Plant Optimized (Exercise 2) started");
        pn.Delay = 500;
        pn.ShowLogInWindow = true;

        PetriNetWindow frame = new PetriNetWindow(false);
        frame.petriNet = pn;
        frame.setVisible(true);
    }
}
