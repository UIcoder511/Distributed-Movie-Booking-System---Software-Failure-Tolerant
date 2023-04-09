package Sequencer;

import configs.Configs;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import static configs.Configs.sequencerIP;
import static configs.Configs.sequencerPort;

public class Sequencer {

  private static int sqcID = 0;

  public static void sendMessage(String msg, int sqcId1, boolean isRequest) {
    int port = 1234;

    if (sqcId1 == 0 && isRequest) {
      sqcId1 = ++sqcID;
    }
    String finalMsg = sqcId1 + ";" + msg;

    DatagramSocket ds = null;
    try {
      ds = new DatagramSocket();
      byte[] msgs = finalMsg.getBytes();
      InetAddress aHost = InetAddress.getByName(
        Configs.RM_Multicast_group_address
      );

      DatagramPacket request = new DatagramPacket(
        msgs,
        msgs.length,
        aHost,
        port
      );
      ds.send(request);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] a) {
    DatagramSocket datagramSocket = null;
    try {
      datagramSocket =
        new DatagramSocket(sequencerPort, InetAddress.getByName(sequencerIP));
      byte[] b = new byte[1000];
      System.out.println("--Sequencer UDP server started--");
      while (true) {
        DatagramPacket datagramPacket = new DatagramPacket(b, b.length);
        datagramSocket.receive(datagramPacket);
        String response = new String(
          datagramPacket.getData(),
          0,
          datagramPacket.getLength()
        );
        String[] responseParts = response.split(";");
        int sqcID1 = Integer.parseInt(responseParts[0]);
        String ip = datagramPacket.getAddress().getHostAddress();
        //	return 0 getSequenceNumber() + ";" +
//1	getFeIpAddress().toUpperCase() + ";" +
//2	getMessageType().toUpperCase() + ";" +
//3	getFunction().toUpperCase() + ";" +
//4	getClientID().toUpperCase() + ";" +
//5	getMovieID().toUpperCase() + ";" +
//6	getMovieName().toUpperCase() + ";" +
//7	getBookingCapacity() + ";" +
//8	getOldMovieID().toUpperCase() + ";" +
//9	getOldMovieName().toUpperCase() + ";" +
//10	getNumberOfTickets() ;
        String response1 =
          ip +
          ";" +
          responseParts[2] +
          ";" +
          responseParts[3] +
          ";" +
          responseParts[4] +
          ";" +
          responseParts[5] +
          ";" +
          responseParts[6] +
          ";" +
          responseParts[7] +
          ";" +
          responseParts[8] +
          ";" +
          responseParts[9] +
          ";" +
          responseParts[10] +
          ";";
        System.out.println(response1);
        sendMessage(response1, sqcID1, responseParts[2].equalsIgnoreCase("00"));
        byte[] seqID = (Integer.toString(sqcID)).getBytes();
        InetAddress aHost = datagramPacket.getAddress();
        int port1 = datagramPacket.getPort();
        System.out.println(aHost + ":" + port1);
        DatagramPacket dp = new DatagramPacket(
          seqID,
          seqID.length,
          aHost,
          port1
        );
        datagramSocket.send(dp);
      }
    } catch (SocketException e) {
      System.out.println("Socket: " + e.getMessage());
    } catch (IOException e) {
      System.out.println("IO: " + e.getMessage());
    } finally {
      if (datagramSocket != null) datagramSocket.close();
    }
  }
}
