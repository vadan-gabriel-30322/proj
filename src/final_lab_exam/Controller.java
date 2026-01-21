package final_lab_exam;

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

public class Controller {
    public static void main(String[] args) {
        PetriNet pn = new PetriNet();
        pn.PetriNetName = "Controller";
        pn.NetworkPort = 1086; // Port 1086

        // ------------------------------------------------------------------------
        // FLRS Tables
        // ------------------------------------------------------------------------

        // Subtractor (Ref - Level)
        // Table structure: 5x5 Matrix
        // Rows: Input 1 (Ref: NL, NM, ZR, PM, PL)
        // Cols: Input 2 (Level: NL, NM, ZR, PM, PL)
        FLRS subtractor = new FLRS(
                // NL NM ZR PM PL
                new FV(FZ.ZR, FZ.ZR), new FV(FZ.NM, FZ.NM), new FV(FZ.NL, FZ.NL), new FV(FZ.NL, FZ.NL),
                new FV(FZ.NL, FZ.NL), // Row 1 (Ref=NL)
                new FV(FZ.PM, FZ.PM), new FV(FZ.ZR, FZ.ZR), new FV(FZ.NM, FZ.NM), new FV(FZ.NL, FZ.NL),
                new FV(FZ.NL, FZ.NL), // Row 2 (Ref=NM)
                new FV(FZ.PL, FZ.PL), new FV(FZ.PM, FZ.PM), new FV(FZ.ZR, FZ.ZR), new FV(FZ.NM, FZ.NM),
                new FV(FZ.NL, FZ.NL), // Row 3 (Ref=ZR)
                new FV(FZ.PL, FZ.PL), new FV(FZ.PL, FZ.PL), new FV(FZ.PM, FZ.PM), new FV(FZ.ZR, FZ.ZR),
                new FV(FZ.NM, FZ.NM), // Row 4 (Ref=PM)
                new FV(FZ.PL, FZ.PL), new FV(FZ.PL, FZ.PL), new FV(FZ.PL, FZ.PL), new FV(FZ.PM, FZ.PM),
                new FV(FZ.ZR, FZ.ZR) // Row 5 (Ref=PL)
        );
        subtractor.Print();

        // Identity (Pass through)
        // Table structure: 5x5 Matrix (Output depends only on Row Input 1)
        // Rows: Input 1 (Source: NL, NM, ZR, PM, PL)
        // Cols: Input 2 (Ignored/Don't Care)
        FLRS identity = new FLRS(
                // NL NM ZR PM PL
                new FV(FZ.NL, FZ.NL), new FV(FZ.NL, FZ.NL), new FV(FZ.NL, FZ.NL), new FV(FZ.NL, FZ.NL),
                new FV(FZ.NL, FZ.NL), // Row 1 (In=NL)
                new FV(FZ.NM, FZ.NM), new FV(FZ.NM, FZ.NM), new FV(FZ.NM, FZ.NM), new FV(FZ.NM, FZ.NM),
                new FV(FZ.NM, FZ.NM), // Row 2 (In=NM)
                new FV(FZ.ZR, FZ.ZR), new FV(FZ.ZR, FZ.ZR), new FV(FZ.ZR, FZ.ZR), new FV(FZ.ZR, FZ.ZR),
                new FV(FZ.ZR, FZ.ZR), // Row 3 (In=ZR)
                new FV(FZ.PM, FZ.PM), new FV(FZ.PM, FZ.PM), new FV(FZ.PM, FZ.PM), new FV(FZ.PM, FZ.PM),
                new FV(FZ.PM, FZ.PM), // Row 4 (In=PM)
                new FV(FZ.PL, FZ.PL), new FV(FZ.PL, FZ.PL), new FV(FZ.PL, FZ.PL), new FV(FZ.PL, FZ.PL),
                new FV(FZ.PL, FZ.PL) // Row 5 (In=PL)
        );
        identity.Print();

        // ------------------------------------------------------------------------
        // Places
        // ------------------------------------------------------------------------

        DataFuzzy P0 = new DataFuzzy();
        P0.SetName("P0"); // Ref Input
        pn.PlaceList.add(P0);

        DataFuzzy P2 = new DataFuzzy();
        P2.SetName("P2"); // Ref Storage
        pn.PlaceList.add(P2);

        DataFuzzy P3 = new DataFuzzy();
        P3.SetName("P3"); // Level Storage (from Plant)
        pn.PlaceList.add(P3);

        DataFuzzy P6 = new DataFuzzy();
        P6.SetName("P6"); // Error
        pn.PlaceList.add(P6);

        DataFuzzy P1 = new DataFuzzy();
        P1.SetName("P1"); // State
        pn.PlaceList.add(P1);

        // Output Place to Plant
        DataTransfer OP_Cmd = new DataTransfer();
        OP_Cmd.SetName("OP_Cmd");
        OP_Cmd.Value = new TransferOperation("localhost", "1085", "u"); // To Plant
        pn.PlaceList.add(OP_Cmd);

        // Output Values (Fuzzy)
        DataFuzzy valStart = new DataFuzzy();
        valStart.SetName("ValStart");
        valStart.SetValue(new Fuzzy(0.1F)); // Positive value for "Start"
        pn.ConstantPlaceList.add(valStart);

        DataFuzzy valStop = new DataFuzzy();
        valStop.SetName("ValStop");
        valStop.SetValue(new Fuzzy(-0.1F)); // Negative/Zero value for "Stop"
        pn.ConstantPlaceList.add(valStop);

        DataFuzzy pZero = new DataFuzzy();
        pZero.SetName("P_Zero");
        pZero.SetValue(new Fuzzy(0.0F));
        pn.ConstantPlaceList.add(pZero);

        // ------------------------------------------------------------------------
        // Transitions
        // ------------------------------------------------------------------------

        // T1: Read Ref (P0 -> P2)
        PetriTransition t1 = new PetriTransition(pn);
        t1.TransitionName = "T1";
        t1.InputPlaceName.add("P0");

        Condition t1c1 = new Condition(t1, "P0", TransitionCondition.NotNull);

        GuardMapping gt1 = new GuardMapping();
        gt1.condition = t1c1;

        ArrayList<PlaceNameWithWeight> i1 = new ArrayList<>();
        i1.add(new PlaceNameWithWeight("P0", 1.0F));

        ArrayList<String> o1 = new ArrayList<>();
        o1.add("P2");

        gt1.Activations.add(new Activation(t1, identity, i1, TransitionOperation.FLRS, o1));
        t1.GuardMappingList.add(gt1);
        t1.Delay = 0;
        pn.Transitions.add(t1);

        // T2: Calc Error (P2, P3 -> P6)
        PetriTransition t2 = new PetriTransition(pn);
        t2.TransitionName = "T2";
        t2.InputPlaceName.add("P2");
        t2.InputPlaceName.add("P3");

        Condition t2c1 = new Condition(t2, "P2", TransitionCondition.NotNull);
        Condition t2c2 = new Condition(t2, "P3", TransitionCondition.NotNull);
        t2c1.SetNextCondition(LogicConnector.AND, t2c2);

        GuardMapping gt2 = new GuardMapping();
        gt2.condition = t2c1;

        ArrayList<PlaceNameWithWeight> i2 = new ArrayList<>();
        i2.add(new PlaceNameWithWeight("P2", 1.0F)); // Ref
        i2.add(new PlaceNameWithWeight("P3", 1.0F)); // Level

        ArrayList<String> o2 = new ArrayList<>();
        o2.add("P6"); // Error

        gt2.Activations.add(new Activation(t2, subtractor, i2, TransitionOperation.FLRS, o2));
        t2.GuardMappingList.add(gt2);
        t2.Delay = 0;
        pn.Transitions.add(t2);

        // T3: Feedback (P3 -> P1)
        PetriTransition t3 = new PetriTransition(pn);
        t3.TransitionName = "T3";
        t3.InputPlaceName.add("P3");

        Condition t3c1 = new Condition(t3, "P3", TransitionCondition.NotNull);

        GuardMapping gt3 = new GuardMapping();
        gt3.condition = t3c1;

        ArrayList<PlaceNameWithWeight> i3 = new ArrayList<>();
        i3.add(new PlaceNameWithWeight("P3", 1.0F));

        ArrayList<String> o3 = new ArrayList<>();
        o3.add("P1");

        gt3.Activations.add(new Activation(t3, identity, i3, TransitionOperation.FLRS, o3));
        t3.GuardMappingList.add(gt3);
        t3.Delay = 1;
        pn.Transitions.add(t3);

        // T4: Command Output (P6 -> Network)
        // Guard 1: Positive Error
        PetriTransition t4 = new PetriTransition(pn);
        t4.TransitionName = "T4";
        t4.InputPlaceName.add("P6");

        Condition t4c1 = new Condition(t4, "P6", TransitionCondition.MoreThan, "P_Zero");

        GuardMapping gt4a = new GuardMapping();
        gt4a.condition = t4c1;
        gt4a.Activations.add(new Activation(t4, "ValStart", TransitionOperation.SendOverNetwork, "OP_Cmd"));

        // Guard 2: Negative Error
        Condition t4c2 = new Condition(t4, "P6", TransitionCondition.LessThan, "P_Zero");

        GuardMapping gt4b = new GuardMapping();
        gt4b.condition = t4c2;
        gt4b.Activations.add(new Activation(t4, "ValStop", TransitionOperation.SendOverNetwork, "OP_Cmd"));

        t4.GuardMappingList.add(gt4a);
        t4.GuardMappingList.add(gt4b);
        t4.Delay = 0;
        pn.Transitions.add(t4);

        System.out.println("Controller started \n ------------------------------");
        pn.Delay = 2000;

        PetriNetWindow frame = new PetriNetWindow(true);
        frame.petriNet = pn;
        frame.setVisible(true);
    }
}
