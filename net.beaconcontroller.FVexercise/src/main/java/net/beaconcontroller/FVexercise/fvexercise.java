package net.beaconcontroller.FVexercise;

import java.io.IOException;
import java.util.ArrayList;

import org.openflow.protocol.OFFlowMod;
import org.openflow.protocol.OFMatch;
import org.openflow.protocol.OFMessage;
import org.openflow.protocol.OFType;
import org.openflow.protocol.action.OFAction;
import org.openflow.protocol.action.OFActionOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.beaconcontroller.core.IBeaconProvider;
import net.beaconcontroller.core.IOFMessageListener;
import net.beaconcontroller.core.IOFSwitch;
import net.beaconcontroller.core.IOFSwitchListener;
import net.beaconcontroller.core.IOFMessageListener.Command;
import net.beaconcontroller.packet.Ethernet;


public class fvexercise implements IOFMessageListener, IOFSwitchListener {
    
    protected static Logger log = LoggerFactory.getLogger(fvexercise.class);
    protected IBeaconProvider beaconProvider;
    
    
    @Override
    public Command receive(IOFSwitch sw, OFMessage msg) throws IOException {
        // TODO Auto-generated method stub
        return Command.CONTINUE;
    }

    /**
     * @param beaconProvider the beaconProvider to set
     */
    public void setBeaconProvider(IBeaconProvider beaconProvider) {
        this.beaconProvider = beaconProvider;
    }

    public void startUp() {
        log.trace("Starting");
        beaconProvider.addOFMessageListener(OFType.PACKET_IN, this);
        beaconProvider.addOFSwitchListener(this);
    }

    public void shutDown() {
        log.trace("Stopping");
        beaconProvider.removeOFMessageListener(OFType.PACKET_IN, this);
        beaconProvider.removeOFSwitchListener(this);
    }

    public String getName() {
        return "FlowVisor Exercise";
    }

    private void instllEntry(IOFSwitch sw, String dmac, short inport, short[] outports) {
        OFMatch match = new OFMatch();
        int wildcard = OFMatch.OFPFW_ALL;
        OFFlowMod fm = new OFFlowMod();
        OFAction action;
        ArrayList<OFAction> actions = new ArrayList<OFAction>();
        for (int i=0; i < outports.length; i++) {
            action = new OFActionOutput().setPort(outports[i]);
            actions.add(action);
        }
        if (dmac != "") {
            match.setDataLayerDestination(Ethernet.toMACAddress(dmac));
            wildcard &= ~(OFMatch.OFPFW_DL_DST);
        }
        if (inport != 0) {
            match.setInputPort(inport);
            wildcard &= ~(OFMatch.OFPFW_IN_PORT);
        }
        match.setWildcards(wildcard);
        fm.setBufferId(-1).setCommand(OFFlowMod.OFPFC_ADD).setIdleTimeout((short) 0)
        .setMatch(match).setActions(actions);
        log.info("Flow Mod: {}",fm.toString());
        try {
            sw.getOutputStream().write(fm);
        } catch (IOException e) {
            log.error("Failure writing FlowMod", e);
        }
    }
    
    @Override
    public void addedSwitch(IOFSwitch sw) {
        
        long dpid = sw.getId();
        log.info("switch joined with dpid {}", dpid);        
        if (dpid == 1) {
            instllEntry(sw,"00:00:00:00:00:01",(short)0,new short[] {3});
            instllEntry(sw,"00:00:00:00:00:02",(short)0,new short[] {4});
            instllEntry(sw,"00:00:00:00:00:03",(short)0,new short[] {1});
            instllEntry(sw,"00:00:00:00:00:03",(short)0,new short[] {2});
            instllEntry(sw,"00:00:00:00:00:04",(short)0,new short[] {1});
            instllEntry(sw,"00:00:00:00:00:04",(short)0,new short[] {2});
        }
        if (dpid == 4) {
            instllEntry(sw,"00:00:00:00:00:01",(short)0,new short[] {1});
            instllEntry(sw,"00:00:00:00:00:01",(short)0,new short[] {2});
            instllEntry(sw,"00:00:00:00:00:02",(short)0,new short[] {1});
            instllEntry(sw,"00:00:00:00:00:02",(short)0,new short[] {2});
            instllEntry(sw,"00:00:00:00:00:03",(short)0,new short[] {3});
            instllEntry(sw,"00:00:00:00:00:04",(short)0,new short[] {4});
        }
        if (dpid == 2 || dpid == 3) {
            instllEntry(sw,"00:00:00:00:00:01",(short)0,new short[] {1});
            instllEntry(sw,"00:00:00:00:00:02",(short)0,new short[] {1});
            instllEntry(sw,"00:00:00:00:00:03",(short)0,new short[] {2});
            instllEntry(sw,"00:00:00:00:00:04",(short)0,new short[] {2});
        }
    }

    @Override
    public void removedSwitch(IOFSwitch sw) {
        // TODO Auto-generated method stub
        
    }
}
