package p5;

import Components.Activation;
import Components.Condition;
import Components.GuardMapping;
import Components.PetriNet;
import Components.PetriNetWindow;
import Components.PetriTransition;
import DataObjects.DataString;
import DataObjects.DataTransfer;
import Enumerations.LogicConnector;
import Enumerations.TransitionCondition;
import Enumerations.TransitionOperation;

import DataOnly.TransferOperation;

public class TrafficLight {
    public static void main(String[] args) {
        PetriNet pn = new PetriNet();
        pn.PetriNetName = "Traffic Light Controller";
        pn.NetworkPort = 1081;

        // -------------------------------------------------------------------
        // -------------------------------Places--------------------------------
        // --------------------------------------------------------------------

        DataString p1 = new DataString();
        p1.SetName("Uvreq");
        pn.PlaceList.add(p1);

        DataString p2 = new DataString();
        p2.SetName("P_TL");
        p2.SetValue("green");
        pn.PlaceList.add(p2);

        DataTransfer p3 = new DataTransfer();
        p3.SetName("OReq");
        p3.Value = new TransferOperation("localhost", "1080", "P_TL");
        pn.PlaceList.add(p3);

        // -------------------------------------------------------------------
        // ---------------------------Transitions------------------------------
        // --------------------------------------------------------------------

        // T1 ------------------------------------------------
        PetriTransition t1 = new PetriTransition(pn);
        t1.TransitionName = "T1";
        t1.InputPlaceName.add("Uvreq");
        t1.InputPlaceName.add("P_TL");

        Condition T1Ct1 = new Condition(t1, "Uvreq", TransitionCondition.NotNull);
        Condition T1Ct2 = new Condition(t1, "P_TL", TransitionCondition.Equal, "green");
        T1Ct1.SetNextCondition(LogicConnector.AND, T1Ct2);

        GuardMapping grdT1 = new GuardMapping();
        grdT1.condition = T1Ct1;
        grdT1.Activations.add(new Activation(t1, "P_TL", TransitionOperation.Move, "P_TL"));
        grdT1.Activations.add(new Activation(t1, "P_TL", TransitionOperation.SendOverNetwork, "OReq"));
        grdT1.Activations.add(new Activation(t1, "P_TL", TransitionOperation.MakeNull, "Uvreq"));

        // Note: The image says "P_TL: SendOverNetwork (yellow)" but P_TL is a
        // DataString.
        // We probably need to set P_TL to yellow first.
        // Let's follow the logic:
        // t1: (Uvreq != null) AND (wait != null) -> wait = wait,
        // P_TL.SendOverNetwork(yellow)
        // Wait, the image has "wait" place but also "P_TL".
        // Let's look at the "Controller" part of the image.
        // It seems there is a "wait" place and "P_TL" place.
        // But in the "Added Place types" section it lists: Uvreq, P_PTL, OReq.
        // And "The Controller" diagram shows: Uvreq, P_TL, OReq, and some other places
        // like "yr", "rg", "gr".

        // Let's re-read the image carefully.
        // "The Controller":
        // Places: Uvreq, P_TL, OReq
        // Transitions:
        // t1: (Uvreq != null) AND (wait != null) ... wait? maybe "green"?
        // The diagram shows a cycle: green -> yellow -> red -> green.
        // And Uvreq triggers green -> yellow.

        // Let's implement the cycle as shown in the state machine part of the image.
        // Places:
        // P_TL (holds the current light state for cars?)
        // P_PTL (Pedestrian Traffic Light?)
        // OReq (Output Request?)

        // Actually, the image lists:
        // Place types:
        // Uvreq, P_PTL: DataString
        // OReq: DataTransfer

        // And "The Controller" has:
        // Places: green, yellow, red (likely one place P_TL with values, or separate
        // places?)
        // The diagram shows places "g", "y", "r" and transitions between them.
        // And "Uvreq" is an input to the transition from "g" to "y".

        // Let's assume P_TL is the main place holding the state ("green", "yellow",
        // "red").
        // And we have Uvreq as input.

        // t1: Green -> Yellow
        // Condition: Uvreq != null AND P_TL == "green"
        // Action: P_TL = "yellow"
        // Send "yellow" to OReq?

        // Let's look at the text on the right of the controller diagram:
        // t1: (Uvreq != null) AND (wait != null)
        // yr = wait
        // P_TL.SendOverNetwork(yellow)

        // It seems "wait" is a variable or place?
        // Ah, "yr", "rg", "gr" might be transition names or intermediate places?
        // "yr" -> yellow to red?

        // Let's try to interpret the "grid map":
        // t1: (Uvreq != null) AND (wait != null)
        // yr = wait <-- This looks like moving a token from 'wait' to 'yr'
        // P_TL.SendOverNetwork(yellow)

        // t2: (yr != null)
        // rg = yr
        // P_TL.SendOverNetwork(red)
        // P_PTL.SendOverNetwork(green)

        // t3: (rg != null)
        // rg = rg
        // P_PTL.SendOverNetwork(yellow) -- wait, usually ped light goes green -> red
        // directly or green -> flashing -> red.

        // t4: (rg != null)
        // gr = rg
        // P_PTL.SendOverNetwork(red)
        // P_TL.SendOverNetwork(green)

        // t5: (gr != null)
        // wait = gr

        // So we have places:
        // "wait" (initial state, green for cars)
        // "yr" (yellow for cars, red for peds)
        // "rg" (red for cars, green for peds)
        // "gr" (green for cars, red for peds - transitioning back)

        // Let's define these places.

        // Re-defining places based on "The Controller" text:

        // Place: wait (DataString, init "green"?)
        // Place: yr (DataString)
        // Place: rg (DataString)
        // Place: gr (DataString)
        // Place: Uvreq (DataString)
        // Place: P_TL (DataTransfer? No, "P_TL: DataString" in top section, but
        // "P_TL.SendOverNetwork" implies it might be used to send data)
        // Place: P_PTL (DataString? "P_PTL: Data Transfer" in top section)

        // Wait, "P_TL: DataString" and "P_PTL: DataString" in top list.
        // But "OReq: DataTransfer".
        // And "P_TL.SendOverNetwork" usually requires P_TL to be connected to a
        // DataTransfer place or be one?
        // In this framework, `SendOverNetwork` usually takes the value from the place
        // and sends it to a `DataTransfer` place connected to it?
        // Or `SendOverNetwork` is an operation on a transition that sends value OF a
        // place TO a target place (which must be DataTransfer).

        // Let's look at `TransitionOperation.SendOverNetwork`.
        // It sends the value of the first argument to the second argument (which is a
        // place name).

        // So:
        // t1:
        // Input: Uvreq, wait
        // Output: yr, OReq (for P_TL), OReq (for P_PTL?)

        // The text says: "P_TL.SendOverNetwork(yellow)"
        // This probably means: Send string "yellow" to the network via P_TL (if P_TL is
        // DataTransfer) or via OReq?
        // The diagram shows arrows from transitions to "OReq".

        // Let's assume:
        // P_TL is the network interface for Car Traffic Light?
        // P_PTL is the network interface for Pedestrian Traffic Light?

        // But the text says "OReq: DataTransfer".
        // Maybe we send "P_TL:yellow" to OReq?

        // Let's stick to the "grid map" logic as closely as possible.

        // Places:
        // wait (DataString, value="start")
        // yr (DataString)
        // rg (DataString)
        // gr (DataString)
        // Uvreq (DataString)

        // Outputs:
        // P_TL (DataString - representing state?)
        // P_PTL (DataString - representing state?)
        // OReq (DataTransfer - for network communication)

        // Let's implement the places.

        // Clear previous list
        pn.PlaceList.clear();

        DataString wait = new DataString();
        wait.SetName("wait");
        wait.SetValue("start");
        pn.PlaceList.add(wait);

        DataString yr = new DataString();
        yr.SetName("yr");
        pn.PlaceList.add(yr);

        DataString rg = new DataString();
        rg.SetName("rg");
        pn.PlaceList.add(rg);

        DataString gr = new DataString();
        gr.SetName("gr");
        pn.PlaceList.add(gr);

        DataString uvreq = new DataString();
        uvreq.SetName("Uvreq");
        pn.PlaceList.add(uvreq);

        DataString p_tl = new DataString();
        p_tl.SetName("P_TL");
        pn.PlaceList.add(p_tl);

        DataString p_ptl = new DataString();
        p_ptl.SetName("P_PTL");
        pn.PlaceList.add(p_ptl);

        DataTransfer oreq = new DataTransfer();
        oreq.SetName("OReq");
        oreq.Value = new TransferOperation("localhost", "1080", "P_TL");
        pn.PlaceList.add(oreq);

        // Transitions

        // t1: (Uvreq != null) AND (wait != null)
        // Actions:
        // yr = wait (Move token)
        // P_TL = "yellow"
        // Send P_TL to OReq

        t1 = new PetriTransition(pn);
        t1.TransitionName = "t1";
        t1.InputPlaceName.add("Uvreq");
        t1.InputPlaceName.add("wait");

        T1Ct1 = new Condition(t1, "Uvreq", TransitionCondition.NotNull);
        T1Ct2 = new Condition(t1, "wait", TransitionCondition.NotNull);
        T1Ct1.SetNextCondition(LogicConnector.AND, T1Ct2);

        grdT1 = new GuardMapping();
        grdT1.condition = T1Ct1;

        // yr = wait
        grdT1.Activations.add(new Activation(t1, "wait", TransitionOperation.Move, "yr"));
        // P_TL = "yellow" (We need to set the value. Can we do that directly? Or move
        // from a constant?)
        // The framework usually uses "Move" or "AddElement".
        // Let's add constant places for colors.

        // Consume Uvreq
        grdT1.Activations.add(new Activation(t1, "Uvreq", TransitionOperation.MakeNull, ""));

        t1.GuardMappingList.add(grdT1);
        t1.Delay = 0;
        pn.Transitions.add(t1);

        // Wait, I need to send "yellow" to OReq.
        // And update P_TL.
        // I'll add constant places for "green", "yellow", "red".

        DataString green = new DataString();
        green.SetName("green");
        green.SetValue("green");
        green.Printable = false;
        pn.ConstantPlaceList.add(green);

        DataString yellow = new DataString();
        yellow.SetName("yellow");
        yellow.SetValue("yellow");
        yellow.Printable = false;
        pn.ConstantPlaceList.add(yellow);

        DataString red = new DataString();
        red.SetName("red");
        red.SetValue("red");
        red.Printable = false;
        pn.ConstantPlaceList.add(red);

        // Update t1 activations
        grdT1.Activations.add(new Activation(t1, "yellow", TransitionOperation.Copy, "P_TL"));
        grdT1.Activations.add(new Activation(t1, "yellow", TransitionOperation.SendOverNetwork, "OReq"));

        // t2: (yr != null)
        // Actions:
        // rg = yr
        // P_TL = red
        // Send P_TL to OReq
        // P_PTL = green
        // Send P_PTL to OReq (Wait, OReq is one place. Can we distinguish? Maybe
        // different topics/targets?)
        // The image says "P_TL: SendOverNetwork(red)", "P_PTL: SendOverNetwork(green)".
        // Maybe we need two output places? Or OReq handles both?
        // Let's assume OReq broadcasts everything.

        PetriTransition t2 = new PetriTransition(pn);
        t2.TransitionName = "t2";
        t2.InputPlaceName.add("yr");

        Condition T2Ct1 = new Condition(t2, "yr", TransitionCondition.NotNull);

        GuardMapping grdT2 = new GuardMapping();
        grdT2.condition = T2Ct1;
        grdT2.Activations.add(new Activation(t2, "yr", TransitionOperation.Move, "rg"));
        grdT2.Activations.add(new Activation(t2, "red", TransitionOperation.Copy, "P_TL"));
        grdT2.Activations.add(new Activation(t2, "red", TransitionOperation.SendOverNetwork, "OReq"));
        grdT2.Activations.add(new Activation(t2, "green", TransitionOperation.Copy, "P_PTL"));
        grdT2.Activations.add(new Activation(t2, "green", TransitionOperation.SendOverNetwork, "OReq"));

        t2.GuardMappingList.add(grdT2);
        t2.Delay = 5; // Delay for yellow light
        pn.Transitions.add(t2);

        // t3: (rg != null)
        // Actions:
        // rg = rg (Loop? Or just wait?)
        // P_PTL = yellow (Pedestrians get yellow? Or maybe flashing green?)
        // The image says "P_PTL.SendOverNetwork(yellow)".
        // Wait, t3 input is rg, output is rg?
        // "t3: (rg != null) ... rg = rg"
        // This implies a loop or a delay state.
        // And then t4 transitions out of rg?
        // "t4: (rg != null) ... gr = rg"

        // This is ambiguous. If t3 and t4 both have input rg and condition NotNull,
        // they are in conflict.
        // Maybe t3 has a delay and t4 has a longer delay? Or different conditions?
        // Or t3 is an intermediate step?
        // Let's look at the diagram.
        // rg has a self-loop labeled t3.
        // And an outgoing arrow t4 to gr.

        // Usually this means t3 executes, then we are back in rg, then t4 executes?
        // But if t3 keeps executing, we never leave rg.
        // Unless t3 changes something that t4 checks?
        // Or t3 has a delay, and t4 has a delay?
        // If t3 is "Pedestrian Green -> Yellow", and t4 is "Pedestrian Yellow -> Red"?
        // But the state is "rg" (Red for Cars, Green for Peds).

        // Maybe:
        // t2 enters rg. P_TL=Red, P_PTL=Green.
        // t3 fires after some time. P_PTL=Yellow (or flashing).
        // t4 fires after some more time. P_TL=Green, P_PTL=Red.

        // But if t3 loops rg->rg, we need a way to stop it and fire t4.
        // Maybe we need another place?
        // Or maybe t3 moves to a temp place, and t4 moves from temp place?
        // But the text says "rg = rg".

        // Let's assume the diagram implies a sequence:
        // rg (Green Peds) -> t3 -> rg' (Yellow Peds) -> t4 -> gr.
        // But the variable name is "rg" in both cases.

        // Let's implement t3 as moving from rg to a new intermediate place "rg_yellow"?
        // But I should stick to the text if possible.
        // "t3: (rg != null) ... rg = rg"
        // This might be a mistake in my reading or the image.
        // Let's assume t3 moves from rg to rg (self loop) but we need to ensure it only
        // happens once.
        // Petri nets don't have "memory" of previous firing unless we change state.

        // Alternative: t3 moves rg to "rg_y".
        // t4 moves "rg_y" to "gr".
        // Let's add "rg_y" place.

        DataString rg_y = new DataString();
        rg_y.SetName("rg_y");
        pn.PlaceList.add(rg_y);

        PetriTransition t3 = new PetriTransition(pn);
        t3.TransitionName = "t3";
        t3.InputPlaceName.add("rg");

        Condition T3Ct1 = new Condition(t3, "rg", TransitionCondition.NotNull);

        GuardMapping grdT3 = new GuardMapping();
        grdT3.condition = T3Ct1;
        grdT3.Activations.add(new Activation(t3, "rg", TransitionOperation.Move, "rg_y"));
        grdT3.Activations.add(new Activation(t3, "yellow", TransitionOperation.Copy, "P_PTL"));
        grdT3.Activations.add(new Activation(t3, "yellow", TransitionOperation.SendOverNetwork, "OReq"));

        t3.GuardMappingList.add(grdT3);
        t3.Delay = 10; // Green for peds duration
        pn.Transitions.add(t3);

        // t4: (rg_y != null) -> gr
        PetriTransition t4 = new PetriTransition(pn);
        t4.TransitionName = "t4";
        t4.InputPlaceName.add("rg_y");

        Condition T4Ct1 = new Condition(t4, "rg_y", TransitionCondition.NotNull);

        GuardMapping grdT4 = new GuardMapping();
        grdT4.condition = T4Ct1;
        grdT4.Activations.add(new Activation(t4, "rg_y", TransitionOperation.Move, "gr"));
        grdT4.Activations.add(new Activation(t4, "red", TransitionOperation.Copy, "P_PTL"));
        grdT4.Activations.add(new Activation(t4, "red", TransitionOperation.SendOverNetwork, "OReq"));
        grdT4.Activations.add(new Activation(t4, "green", TransitionOperation.Copy, "P_TL"));
        grdT4.Activations.add(new Activation(t4, "green", TransitionOperation.SendOverNetwork, "OReq"));

        t4.GuardMappingList.add(grdT4);
        t4.Delay = 3; // Yellow for peds duration
        pn.Transitions.add(t4);

        // t5: (gr != null) -> wait
        PetriTransition t5 = new PetriTransition(pn);
        t5.TransitionName = "t5";
        t5.InputPlaceName.add("gr");

        Condition T5Ct1 = new Condition(t5, "gr", TransitionCondition.NotNull);

        GuardMapping grdT5 = new GuardMapping();
        grdT5.condition = T5Ct1;
        grdT5.Activations.add(new Activation(t5, "gr", TransitionOperation.Move, "wait"));

        t5.GuardMappingList.add(grdT5);
        t5.Delay = 5; // Safety delay
        pn.Transitions.add(t5);

        // -------------------------------------------------------------------------------------
        // ----------------------------PNStart-------------------------------------------------
        // -------------------------------------------------------------------------------------

        System.out.println("Traffic Light Controller started \n ------------------------------");
        pn.Delay = 2000;

        PetriNetWindow frame = new PetriNetWindow(false);
        frame.petriNet = pn;
        frame.setVisible(true);
    }
}
