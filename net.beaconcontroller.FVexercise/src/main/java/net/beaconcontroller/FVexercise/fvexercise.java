package net.beaconcontroller.FVexercise;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.openflow.protocol.OFFlowMod;
import org.openflow.protocol.OFMatch;
import org.openflow.protocol.OFMessage;
import org.openflow.protocol.OFPacketIn;
import org.openflow.protocol.OFPacketOut;
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
        OFPacketIn pi = (OFPacketIn) msg;
        routePacket(sw,pi);
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

    private void installEntry(IOFSwitch sw, OFPacketIn pi, OFMatch match, short[] outports) {
        OFFlowMod fm = new OFFlowMod();
        OFAction action;
        ArrayList<OFAction> actions = new ArrayList<OFAction>();
        for (int i=0; i < outports.length; i++) {
            action = new OFActionOutput().setPort(outports[i]);
            actions.add(action);
        }
        fm.setBufferId(pi.getBufferId()).setCommand(OFFlowMod.OFPFC_ADD).setIdleTimeout((short) 5)
        .setMatch(match).setActions(actions);
        log.info("Flow Mod: {}",fm.toString());
        try {
            sw.getOutputStream().write(fm);
        } catch (IOException e) {
            log.error("Failure writing FlowMod", e);
        }
        if (pi.getBufferId() == OFPacketOut.BUFFER_ID_NONE) {
            log.info("Packet out event triggered");
            OFPacketOut po = new OFPacketOut();
            po.setActions(actions);
            po.setBufferId(OFPacketOut.BUFFER_ID_NONE);
            po.setInPort(pi.getInPort());
            po.setPacketData(pi.getPacketData());
            try {
                sw.getOutputStream().write(po);
            } catch (IOException e) {
                log.error("Sending a packet out", e);
            }
        }
    }
    
    @Override
    public void addedSwitch(IOFSwitch sw) {
    }
        
    private void routePacket(IOFSwitch sw, OFPacketIn pi){
        OFMatch match = OFMatch.load(pi.getPacketData(), pi.getInPort());
        long dpid = sw.getId();
        long dmac = Ethernet.toLong(match.getDataLayerDestination());
       
        if (dpid == 1) {
            if (dmac == 1) installEntry(sw,pi,match,new short[] {3});
            else if (dmac == 2) installEntry(sw,pi,match,new short[] {4});
            else if (dmac == 3) {
                installEntry(sw,pi,match,new short[] {1});
                installEntry(sw,pi,match,new short[] {2});
            }
            else if (dmac == 4) {
                installEntry(sw,pi,match,new short[] {1});
                installEntry(sw,pi,match,new short[] {2});
            }
        }
        if (dpid == 4) {
            if (dmac == 3) installEntry(sw,pi,match,new short[] {3});
            else if (dmac == 4) installEntry(sw,pi,match,new short[] {4});
            else if (dmac == 1) {
                installEntry(sw,pi,match,new short[] {1});
                installEntry(sw,pi,match,new short[] {2});
            }
            else if (dmac == 2) {
                installEntry(sw,pi,match,new short[] {1});
                installEntry(sw,pi,match,new short[] {2});
            }
        }
        if (dpid == 2 || dpid == 3) {
            if (dmac == 1) installEntry(sw,pi,match,new short[] {1});
            else if (dmac == 2) installEntry(sw,pi,match,new short[] {1});
            else if (dmac == 3) installEntry(sw,pi,match,new short[] {2});
            else if (dmac == 4) installEntry(sw,pi,match,new short[] {2});
        }
    }

    @Override
    public void removedSwitch(IOFSwitch sw) {
        // TODO Auto-generated method stub
        
    }
}
