package controller;

import static org.jnetpcap.Pcap.LOOP_INFINITE;

import java.util.ArrayList;
import java.util.List;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.protocol.tcpip.Http;

import view.MainView;
import view.SelectView;

public class Sniffer {
    private List<PcapIf> alldevs = new ArrayList<PcapIf>(); // Will be filled
                                                            // with NICs
    private StringBuilder errbuf = new StringBuilder(); // For any error msgs
    private PcapIf selectedDevice;
    private Pcap pcap;
    private PcapPacketHandler<String> jpacketHandler;
    private int numberDevicesFound;
    private int selectedDeviceNumber;
    private boolean snifferIsOn = false;

    private static SelectView selectView = SelectView.getInstance();
    private static MainView mainView = MainView.getInstance();
    private static SnifferConfig snifferConfig = SnifferConfig.getInstance();
    private static Sniffer sniffer = new Sniffer();

    private Sniffer(){

    }

    public static Sniffer getInstance(){
        return sniffer;
    }

    public String getDevicesList(){
        String devicesList;
        StringBuffer sb = new StringBuffer();
        int r = Pcap.findAllDevs(alldevs, errbuf);
        if (r == Pcap.NOT_OK || alldevs.isEmpty()) {
            return new String("Can't read list of devices, error is "
                    + errbuf.toString());
        }

        sb.append("Which device would you like to listen?\n\n");
        sb.append("Network devices found:\n");

        numberDevicesFound = 0;
        for (PcapIf device : alldevs) {
            String description = (device.getDescription() != null)
                    ? device.getDescription()
                    : "No description available";
            sb.append(String.format("#%d: %s [%s]\n", numberDevicesFound++,
                    device.getName(), description));
        }

        devicesList = sb.toString();
        return devicesList;
    }

    public boolean isDeviceChoiceSucceed(int deviceChosen){
        PcapIf selectedDevice = selectDevice(deviceChosen);
        if (selectedDevice != null) {
            return true;
        } else {
            return false;
        }
    }

    public PcapIf selectDevice(int deviceID){
        if (deviceID < 0 || deviceID >= alldevs.size()) {
            String deviceChoiceFailedUserOutput = String
                    .format("Invalid device number.");
            selectView.deviceChoiceFailed(deviceChoiceFailedUserOutput);
            return null;
        } else {
            PcapIf device = alldevs.get(deviceID);
            String deviceChosenSuccessfullyUserOutput = String.format(
                    "\nDevice chosen: '%s'.\n", device.getDescription());
            selectView.deviceChosenSuccessfully(
                    deviceChosenSuccessfullyUserOutput);
            return device;
        }
    }

    private void openDevice(PcapIf device){
        int snaplen = 64 * 1024; // Capture all packets, no trucation
        int flags = Pcap.MODE_PROMISCUOUS; // capture all packets
        int timeout = 10 * 1000; // 10 seconds in millis
        pcap = Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);

        if (pcap == null) {
            System.err.printf("Error while opening device for capture: "
                    + errbuf.toString());
            return;
        }
    }

    private void packetHandler(){
        jpacketHandler = new PcapPacketHandler<String>() {
            Http httpheader = new Http();
            StringBuffer sb = new StringBuffer();

            public void nextPacket(PcapPacket packet, String user){
                if (packet.hasHeader(httpheader)
                        && snifferConfig.isOnHTTPProtocol()) {
                    sb.append(httpheader.toString());
                    if (httpheader.hasPayload()) {
                        sb.append("\nHTTP payload: (string length is "
                                + new String(httpheader.getPayload()).length()
                                + ")\n");
                        sb.append(new String(httpheader.getPayload()));
//                        sb.append("\nHTTP truncated? "
//                                + httpheader.isPayloadTruncated());
                    }
                    String packetStr = sb.toString();
                    mainView.updateOutputText(packetStr);
//                    System.out.println(packet.toString());
                }
            }
        };
    }

    private void capturePackets(){
        pcap.loop(LOOP_INFINITE, jpacketHandler, "Received Packet");
        //        int pcapReturnCode = -1; // initial random value (not zero)
        //        while (snifferIsOn) {
        //            while (pcapReturnCode != 0) { // do until 1 packet successfully catched
        //                pcapReturnCode = pcap.loop(pcap.LOOP_INFINITE, jpacketHandler,
        //                        "Received Packet");
        //            }
        //        }
        pcap.close();
    }

    public void startSniffer(){
        if (!snifferIsOn) {
            snifferIsOn = true;
            selectedDevice = selectDevice(selectedDeviceNumber);
            openDevice(selectedDevice);
            packetHandler();
            capturePackets();
        }
    }

    public void stopSniffer(){
        snifferIsOn = false;
    }

    public int getSelectedDeviceNumber(){
        return selectedDeviceNumber;
    }

    public void setSelectedDeviceNumber(int selectedDeviceNumber){
        this.selectedDeviceNumber = selectedDeviceNumber;
    }

}
