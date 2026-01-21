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

public class Exercise3 {
        public static void main(String[] args) throws FileNotFoundException {
                PetriNet pn = new PetriNet();
                pn.PetriNetName = "HTC PID Controller (Exercise 3)";
                pn.NetworkPort = 1083;
                pn.SetInputFile("C:\\Users\\vadan\\Desktop\\DCS\\All_Petri_FW\\PetriInputData\\HTC.txt");

                // ... (Same FLRS definitions) ...
                FLRS reader = new FLRS(new FV(FZ.NL), new FV(FZ.NM), new FV(FZ.ZR), new FV(FZ.PM), new FV(FZ.PL),
                                new FV(FZ.NL), new FV(FZ.NM), new FV(FZ.ZR), new FV(FZ.PM), new FV(FZ.PL),
                                new FV(FZ.NL), new FV(FZ.NM), new FV(FZ.ZR), new FV(FZ.PM), new FV(FZ.PL),
                                new FV(FZ.NL), new FV(FZ.NM), new FV(FZ.ZR), new FV(FZ.PM), new FV(FZ.PL),
                                new FV(FZ.NL), new FV(FZ.NM), new FV(FZ.ZR), new FV(FZ.PM), new FV(FZ.PL));

                FLRS doubleChannelDifferentiator = new FLRS(new FV(FZ.ZR, FZ.ZR), new FV(FZ.NM, FZ.NM),
                                new FV(FZ.NL, FZ.NL), new FV(FZ.NL, FZ.NL), new FV(FZ.NL, FZ.NL),
                                new FV(FZ.PM, FZ.PM), new FV(FZ.ZR, FZ.ZR), new FV(FZ.NM, FZ.NM), new FV(FZ.NL, FZ.NL),
                                new FV(FZ.NL, FZ.NL),
                                new FV(FZ.PL, FZ.PL), new FV(FZ.PM, FZ.PM), new FV(FZ.ZR, FZ.ZR), new FV(FZ.NM, FZ.NM),
                                new FV(FZ.NL, FZ.NL),
                                new FV(FZ.PL, FZ.PL), new FV(FZ.PL, FZ.PL), new FV(FZ.PM, FZ.PM), new FV(FZ.ZR, FZ.ZR),
                                new FV(FZ.NM, FZ.NM),
                                new FV(FZ.PL, FZ.PL), new FV(FZ.PL, FZ.PL), new FV(FZ.PL, FZ.PL), new FV(FZ.PM, FZ.PM),
                                new FV(FZ.ZR, FZ.ZR));

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

                FLRS OneXOneDefaultTable = new FLRS(new FV(FZ.NL), new FV(FZ.NM), new FV(FZ.ZR), new FV(FZ.PM),
                                new FV(FZ.PL));
                FLRS OneXTwoDefaultTable = new FLRS(new FV(FZ.NL, FZ.NL), new FV(FZ.NM, FZ.NM), new FV(FZ.ZR, FZ.ZR),
                                new FV(FZ.PM, FZ.PM), new FV(FZ.PL, FZ.PL));

                // Places
                DataFuzzy p0 = new DataFuzzy();
                p0.SetName("P0");
                p0.SetValue(new Fuzzy(0.0F));
                pn.PlaceList.add(p0);
                DataFuzzy p1 = new DataFuzzy();
                p1.SetName("P1");
                pn.PlaceList.add(p1);
                DataFuzzy p2 = new DataFuzzy();
                p2.SetName("P2");
                pn.PlaceList.add(p2);
                DataFuzzy p3 = new DataFuzzy();
                p3.SetName("P3");
                p3.SetValue(new Fuzzy(0.3F));
                pn.PlaceList.add(p3);
                DataFuzzy p4 = new DataFuzzy();
                p4.SetName("P4");
                pn.PlaceList.add(p4);
                DataFuzzy p5 = new DataFuzzy();
                p5.SetName("P5");
                pn.PlaceList.add(p5);
                DataFuzzy p6 = new DataFuzzy();
                p6.SetName("P6");
                p6.SetValue(new Fuzzy(0.0F));
                pn.PlaceList.add(p6);
                DataFuzzy p7 = new DataFuzzy();
                p7.SetName("P7");
                pn.PlaceList.add(p7);
                DataFuzzy p8 = new DataFuzzy();
                p8.SetName("P8");
                pn.PlaceList.add(p8);

                // Extra places for PID
                DataFuzzy p9 = new DataFuzzy();
                p9.SetName("P9");
                pn.PlaceList.add(p9); // Derivative term
                DataFuzzy p10 = new DataFuzzy();
                p10.SetName("P10");
                p10.SetValue(new Fuzzy(0.0F));
                pn.PlaceList.add(p10); // Previous Error for D
                DataFuzzy p11 = new DataFuzzy();
                p11.SetName("P11");
                pn.PlaceList.add(p11); // Error copy for D
                DataFuzzy p12 = new DataFuzzy();
                p12.SetName("P12");
                pn.PlaceList.add(p12); // D term result

                DataTransfer gascmd = new DataTransfer();
                gascmd.SetName("gascmd");
                gascmd.Value = new TransferOperation("localhost", "1081", "u");
                pn.PlaceList.add(gascmd);

                // T0
                PetriTransition t0 = new PetriTransition(pn);
                t0.TransitionName = "T0";
                t0.InputPlaceName.add("P0");
                t0.InputPlaceName.add("P1");
                Condition T0Ct1 = new Condition(t0, "P0", TransitionCondition.NotNull);
                Condition T0Ct2 = new Condition(t0, "P1", TransitionCondition.NotNull);
                T0Ct1.SetNextCondition(LogicConnector.AND, T0Ct2);
                GuardMapping grdT0 = new GuardMapping();
                grdT0.condition = T0Ct1;
                ArrayList<PlaceNameWithWeight> input0 = new ArrayList<>();
                input0.add(new PlaceNameWithWeight("P0", 1F));
                input0.add(new PlaceNameWithWeight("P1", 1F));
                ArrayList<String> Output0 = new ArrayList<>();
                Output0.add("P2");
                grdT0.Activations.add(new Activation(t0, reader, input0, TransitionOperation.FLRS, Output0));
                t0.GuardMappingList.add(grdT0);
                t0.Delay = 0;
                pn.Transitions.add(t0);

                // T1
                PetriTransition t1 = new PetriTransition(pn);
                t1.TransitionName = "T1";
                t1.InputPlaceName.add("P2");
                t1.InputPlaceName.add("P3");
                Condition T1Ct1 = new Condition(t1, "P2", TransitionCondition.NotNull);
                Condition T1Ct2 = new Condition(t1, "P3", TransitionCondition.NotNull);
                T1Ct1.SetNextCondition(LogicConnector.AND, T1Ct2);
                GuardMapping grdT1 = new GuardMapping();
                grdT1.condition = T1Ct1;
                ArrayList<PlaceNameWithWeight> input1 = new ArrayList<>();
                input1.add(new PlaceNameWithWeight("P2", 1F));
                input1.add(new PlaceNameWithWeight("P3", 1F));
                ArrayList<String> Output1 = new ArrayList<>();
                Output1.add("P4");
                Output1.add("P5");
                grdT1.Activations.add(new Activation(t1, doubleChannelDifferentiator, input1, TransitionOperation.FLRS,
                                Output1));
                t1.GuardMappingList.add(grdT1);
                t1.Delay = 0;
                pn.Transitions.add(t1);

                // T_Split_Error - Split P4 to P4 (for P/I) and P11 (for D)
                // Wait, P4 is used in T4. Let's make T1 output to P4 and P11 directly?
                // Or add a splitter transition.
                // Let's modify T1 to output to P4, P5, and P11? FLRS only supports 2 outputs
                // usually in this framework's examples (OneXTwo).
                // Let's use P4 for both.

                // T_D - Calculate Derivative: D = Error(k) - Error(k-1)
                // P4 is Error(k). P10 is Error(k-1).
                // We need to update P10 with P4 value after calculation.

                PetriTransition t_d = new PetriTransition(pn);
                t_d.TransitionName = "T_D";
                t_d.InputPlaceName.add("P4");
                t_d.InputPlaceName.add("P10");
                Condition TDCt1 = new Condition(t_d, "P4", TransitionCondition.NotNull);
                Condition TDCt2 = new Condition(t_d, "P10", TransitionCondition.NotNull);
                TDCt1.SetNextCondition(LogicConnector.AND, TDCt2);
                GuardMapping grdTD = new GuardMapping();
                grdTD.condition = TDCt1;
                ArrayList<PlaceNameWithWeight> inputD = new ArrayList<>();
                inputD.add(new PlaceNameWithWeight("P4", 1F));
                inputD.add(new PlaceNameWithWeight("P10", 1F));
                ArrayList<String> OutputD = new ArrayList<>();
                OutputD.add("P12");
                OutputD.add("P10"); // P12 is D term, P10 updated
                // Use differentiator table
                grdTD.Activations.add(new Activation(t_d, doubleChannelDifferentiator, inputD, TransitionOperation.FLRS,
                                OutputD));
                t_d.GuardMappingList.add(grdTD);
                t_d.Delay = 0;
                pn.Transitions.add(t_d);

                // T4 - Sum P (P4), I (implicit in P6 accumulation?), D (P12)
                // Original T4: P4 + P6 -> P7, P8.
                // New T4: P4 + P12 + P6 -> P7, P8.
                // Need to sum 3 things.
                // Sum P4 and P12 first -> P9 (PD term).

                PetriTransition t_sum_pd = new PetriTransition(pn);
                t_sum_pd.TransitionName = "T_Sum_PD";
                t_sum_pd.InputPlaceName.add("P4");
                t_sum_pd.InputPlaceName.add("P12");
                Condition TSumPDCt1 = new Condition(t_sum_pd, "P4", TransitionCondition.NotNull);
                Condition TSumPDCt2 = new Condition(t_sum_pd, "P12", TransitionCondition.NotNull);
                TSumPDCt1.SetNextCondition(LogicConnector.AND, TSumPDCt2);
                GuardMapping grdTSumPD = new GuardMapping();
                grdTSumPD.condition = TSumPDCt1;
                ArrayList<PlaceNameWithWeight> inputSumPD = new ArrayList<>();
                inputSumPD.add(new PlaceNameWithWeight("P4", 1F));
                inputSumPD.add(new PlaceNameWithWeight("P12", 0.5F)); // D gain
                ArrayList<String> OutputSumPD = new ArrayList<>();
                OutputSumPD.add("P9");
                grdTSumPD.Activations.add(new Activation(t_sum_pd, doubleChannelAdder, inputSumPD,
                                TransitionOperation.FLRS, OutputSumPD));
                t_sum_pd.GuardMappingList.add(grdTSumPD);
                t_sum_pd.Delay = 0;
                pn.Transitions.add(t_sum_pd);

                // T4 (Modified) - P9 (PD) + P6 (Prev) -> P7, P8
                PetriTransition t4 = new PetriTransition(pn);
                t4.TransitionName = "T4";
                t4.InputPlaceName.add("P9");
                t4.InputPlaceName.add("P6");
                Condition T4Ct1 = new Condition(t4, "P9", TransitionCondition.NotNull);
                Condition T4Ct2 = new Condition(t4, "P6", TransitionCondition.NotNull);
                T4Ct1.SetNextCondition(LogicConnector.AND, T4Ct2);
                GuardMapping grdT4 = new GuardMapping();
                grdT4.condition = T4Ct1;
                ArrayList<PlaceNameWithWeight> input4 = new ArrayList<>();
                input4.add(new PlaceNameWithWeight("P9", 1.2F));
                input4.add(new PlaceNameWithWeight("P6", 1F));
                ArrayList<String> Output4 = new ArrayList<>();
                Output4.add("P7");
                Output4.add("P8");
                grdT4.Activations
                                .add(new Activation(t4, doubleChannelAdder, input4, TransitionOperation.FLRS, Output4));
                t4.GuardMappingList.add(grdT4);
                t4.Delay = 1;
                pn.Transitions.add(t4);

                // T2
                PetriTransition t2 = new PetriTransition(pn);
                t2.TransitionName = "T2";
                t2.InputPlaceName.add("P7");
                Condition T2Ct1 = new Condition(t2, "P7", TransitionCondition.NotNull);
                GuardMapping grdT2 = new GuardMapping();
                grdT2.condition = T2Ct1;
                grdT2.Activations.add(new Activation(t2, "P7", TransitionOperation.SendOverNetwork, "gascmd"));
                t2.GuardMappingList.add(grdT2);
                t2.Delay = 0;
                pn.Transitions.add(t2);

                // T3
                PetriTransition t3 = new PetriTransition(pn);
                t3.TransitionName = "T3";
                t3.InputPlaceName.add("P5");
                Condition T3Ct1 = new Condition(t3, "P5", TransitionCondition.NotNull);
                GuardMapping grdT3 = new GuardMapping();
                grdT3.condition = T3Ct1;
                ArrayList<PlaceNameWithWeight> input3 = new ArrayList<>();
                input3.add(new PlaceNameWithWeight("P5", 1F));
                ArrayList<String> Output3 = new ArrayList<>();
                Output3.add("P0");
                grdT3.Activations.add(
                                new Activation(t3, OneXOneDefaultTable, input3, TransitionOperation.FLRS, Output3));
                t3.GuardMappingList.add(grdT3);
                t3.Delay = 1;
                pn.Transitions.add(t3);

                // T5
                PetriTransition t5 = new PetriTransition(pn);
                t5.TransitionName = "T5";
                t5.InputPlaceName.add("P8");
                t5.InputPlaceName.add("P6");
                Condition T5Ct1 = new Condition(t5, "P8", TransitionCondition.NotNull);
                GuardMapping grdT5 = new GuardMapping();
                grdT5.condition = T5Ct1;
                ArrayList<PlaceNameWithWeight> input5 = new ArrayList<>();
                input5.add(new PlaceNameWithWeight("P8", 1F));
                ArrayList<String> Output5 = new ArrayList<>();
                Output5.add("P6");
                grdT5.Activations.add(
                                new Activation(t5, OneXOneDefaultTable, input5, TransitionOperation.FLRS, Output5));
                t5.GuardMappingList.add(grdT5);
                t5.Delay = 0;
                pn.Transitions.add(t5);

                System.out.println("HTC PID Controller (Exercise 3) started");
                pn.Delay = 500;
                pn.ShowLogInWindow = true;

                PetriNetWindow frame = new PetriNetWindow(false);
                frame.petriNet = pn;
                frame.setVisible(true);
        }
}
