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
import java.util.ArrayList;

public class PedestrianController {
    public static void main(String[] args) {
        // --------------------------------------------------------------------
        // --------------------- Pedestrian Light PN --------------------------
        // --------------------------------------------------------------------
        PetriNet pnLight = new PetriNet();
        pnLight.PetriNetName = "Pedestrian Light";
        pnLight.NetworkPort = 1086;

        DataString pRed = new DataString();
        pRed.SetName("P_Red");
        pRed.SetValue("red");
        pnLight.PlaceList.add(pRed);

        DataString pGreen = new DataString();
        pGreen.SetName("P_Green");
        pnLight.PlaceList.add(pGreen);

        DataString pRequest = new DataString();
        pRequest.SetName("P_Request");
        pnLight.PlaceList.add(pRequest);

        // Controller Interface
        DataTransfer opCmd = new DataTransfer();
        opCmd.SetName("OP_Cmd");
        opCmd.Value = new TransferOperation("localhost", "1096", "inCmd");
        pnLight.PlaceList.add(opCmd);

        // T_Request: User requests crossing
        PetriTransition tReq = new PetriTransition(pnLight);
        tReq.TransitionName = "T_Req";
        tReq.InputPlaceName.add("P_Request");

        Condition cReq = new Condition(tReq, "P_Request", TransitionCondition.NotNull);
        GuardMapping gReq = new GuardMapping();
        gReq.condition = cReq;
        gReq.Activations.add(new Activation(tReq, "P_Request", TransitionOperation.SendOverNetwork, "OP_Cmd"));
        gReq.Activations.add(new Activation(tReq, "", TransitionOperation.MakeNull, "P_Request")); // Consume request

        tReq.GuardMappingList.add(gReq);
        tReq.Delay = 0;
        pnLight.Transitions.add(tReq);

        // T_toGreen
        PetriTransition tG = new PetriTransition(pnLight);
        tG.TransitionName = "T_toGreen";
        tG.InputPlaceName.add("P_Red");
        // Needs command from Controller? Or just simple state change?
        // Let's assume the controller sends "Green" to P_Red (if it was a DataString
        // but we want to swap places)
        // Simplification: We listen to Controller on a specific place?
        // For distinct Petri Nets in same class communication, we used logic connector
        // usually.
        // But here I'll just rely on Network Transfer as requested "different pn
        // objects".
        // I will use `P_Red` and `P_Green` as status.
        // Actually, let's make it simple: The Controller PN sends tokens to
        // P_Red/P_Green directly via Network if possible
        // OR Controller sends a command place.

        // Let's add a place `P_Cmd` received from Controller
        DataString pCmdRcv = new DataString();
        pCmdRcv.SetName("P_CmdRcv");
        pnLight.PlaceList.add(pCmdRcv);

        PetriTransition tSwitchG = new PetriTransition(pnLight);
        tSwitchG.TransitionName = "T_SwitchG";
        tSwitchG.InputPlaceName.add("P_Red");
        tSwitchG.InputPlaceName.add("P_CmdRcv");

        Condition cSG = new Condition(tSwitchG, "P_CmdRcv", TransitionCondition.Equal, "goGreen");
        GuardMapping gSG = new GuardMapping();
        gSG.condition = cSG;
        gSG.Activations.add(new Activation(tSwitchG, "P_Red", TransitionOperation.Move, "P_Green")); // Move Red token
                                                                                                     // to Green? No,
                                                                                                     // value is "red",
                                                                                                     // we want "green".
        // Better: MakeNull Red, and Add Green? Or just Move and set value.
        // Let's just Move and ignore value for now, or distinct places imply state.
        gSG.Activations.add(new Activation(tSwitchG, "", TransitionOperation.MakeNull, "P_CmdRcv"));

        tSwitchG.GuardMappingList.add(gSG);
        tSwitchG.Delay = 0;
        pnLight.Transitions.add(tSwitchG);

        PetriTransition tSwitchR = new PetriTransition(pnLight);
        tSwitchR.TransitionName = "T_SwitchR";
        tSwitchR.InputPlaceName.add("P_Green");
        tSwitchR.InputPlaceName.add("P_CmdRcv");

        Condition cSR = new Condition(tSwitchR, "P_CmdRcv", TransitionCondition.Equal, "goRed");
        GuardMapping gSR = new GuardMapping();
        gSR.condition = cSR;
        gSR.Activations.add(new Activation(tSwitchR, "P_Green", TransitionOperation.Move, "P_Red"));
        gSR.Activations.add(new Activation(tSwitchR, "", TransitionOperation.MakeNull, "P_CmdRcv"));

        tSwitchR.GuardMappingList.add(gSR);
        tSwitchR.Delay = 0;
        pnLight.Transitions.add(tSwitchR);

        // --------------------------------------------------------------------
        // --------------------- Pedestrian Controller PN ---------------------
        // --------------------------------------------------------------------
        PetriNet pnCtrl = new PetriNet();
        pnCtrl.PetriNetName = "Pedestrian Controller";
        pnCtrl.NetworkPort = 1096;

        DataString inCmd = new DataString();
        inCmd.SetName("inCmd");
        pnCtrl.PlaceList.add(inCmd);

        DataString stIdle = new DataString();
        stIdle.SetName("Idle");
        stIdle.SetValue("idle");
        pnCtrl.PlaceList.add(stIdle);

        DataString stWalking = new DataString();
        stWalking.SetName("Walking");
        pnCtrl.PlaceList.add(stWalking);

        DataTransfer opCmdSend = new DataTransfer();
        opCmdSend.SetName("OP_CmdSend");
        opCmdSend.Value = new TransferOperation("localhost", "1086", "P_CmdRcv");
        pnCtrl.PlaceList.add(opCmdSend);

        DataString sGreen = new DataString();
        sGreen.SetName("sGreen");
        sGreen.SetValue("goGreen");
        pnCtrl.ConstantPlaceList.add(sGreen);

        DataString sRed = new DataString();
        sRed.SetName("sRed");
        sRed.SetValue("goRed");
        pnCtrl.ConstantPlaceList.add(sRed);

        // T_StartWalk
        PetriTransition tStart = new PetriTransition(pnCtrl);
        tStart.TransitionName = "T_Start";
        tStart.InputPlaceName.add("Idle");
        tStart.InputPlaceName.add("inCmd");

        Condition cStart = new Condition(tStart, "inCmd", TransitionCondition.NotNull);
        GuardMapping gStart = new GuardMapping();
        gStart.condition = cStart;
        gStart.Activations.add(new Activation(tStart, "Idle", TransitionOperation.Move, "Walking"));
        gStart.Activations.add(new Activation(tStart, "sGreen", TransitionOperation.SendOverNetwork, "OP_CmdSend"));
        gStart.Activations.add(new Activation(tStart, "", TransitionOperation.MakeNull, "inCmd"));

        tStart.GuardMappingList.add(gStart);
        tStart.Delay = 0;
        pnCtrl.Transitions.add(tStart);

        // T_EndWalk
        PetriTransition tEnd = new PetriTransition(pnCtrl);
        tEnd.TransitionName = "T_End";
        tEnd.InputPlaceName.add("Walking");

        Condition cEnd = new Condition(tEnd, "Walking", TransitionCondition.NotNull);
        GuardMapping gEnd = new GuardMapping();
        gEnd.condition = cEnd;
        gEnd.Activations.add(new Activation(tEnd, "Walking", TransitionOperation.Move, "Idle"));
        gEnd.Activations.add(new Activation(tEnd, "sRed", TransitionOperation.SendOverNetwork, "OP_CmdSend"));

        tEnd.GuardMappingList.add(gEnd);
        tEnd.Delay = 50; // Walk duration
        pnCtrl.Transitions.add(tEnd);

        // --------------------------------------------------------------------
        // ------------------------- Execution --------------------------------
        // --------------------------------------------------------------------
        System.out.println("Pedestrian System Started");
        pnLight.Delay = 1000;
        pnCtrl.Delay = 1000;

        PetriNetWindow frame1 = new PetriNetWindow(false);
        frame1.petriNet = pnLight;
        frame1.setVisible(true);

        PetriNetWindow frame2 = new PetriNetWindow(false);
        frame2.petriNet = pnCtrl;
        frame2.setVisible(true);

        // Input panel for Request
        InputCarFP input = new InputCarFP(pnLight); // Attach to Light PN to inject Request
        input.setVisible(true);
    }
}
