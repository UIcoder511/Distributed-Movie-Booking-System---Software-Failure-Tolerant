package frontend;

//import FrontEnd.ServerObjectInterfaceApp.ServerObjectInterfacePOA;

//import Response;

//import DMS_CORBA.CommonInterfacePOAServerObjectInterface;

import org.omg.CORBA.ORB;
import frontend.DMS_CORBA.ServerObjectInterfacePOA;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class FrontEndImplementation extends ServerObjectInterfacePOA {

  private static long DYNAMIC_TIMEOUT = 10000;
  private static int rm1Errors = 0;
  private static int rm2Errors = 0;
  private static int rm3Errors = 0;
//  private static int rm1 = 0;
//  private static int Rm2NoResponseCount = 0;
//  private static int Rm3NoResponseCount = 0;
  private long responseTime = DYNAMIC_TIMEOUT;
  private long startTime;
  private CountDownLatch latch;
  private final FEInterface inter;
  private final List<Response> responses = new ArrayList<>();
  private ORB orb;

  public FrontEndImplementation(FEInterface inter) {
    super();
    this.inter = inter;
  }

  public void setORB(ORB orb_val) {
    orb = orb_val;
  }


  public void shutdown() {
    orb.shutdown(false);
  }

  public void waitForResponse() {
    try {
      System.out.println(
        "FE Implementation:waitForResponse>>>ResponsesRemain" + latch.getCount()
      );
      boolean timeoutReached = latch.await(
        DYNAMIC_TIMEOUT,
        TimeUnit.MILLISECONDS
      );
      if (timeoutReached) {
        setDynamicTimout();
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
      //            inter.sendRequestToSequencer(myRequest);
    }
    //         check result and react correspondingly
  }

  private String validateResponses(Request request) {
    String resp;
    switch ((int) latch.getCount()) {
      case 0:
      case 1:
      case 2:
        resp = findMajorityResponse(request);
        break;
//      case 3:
//        resp = "Fail: No response from any server";
//        System.out.println(resp);
//        if (request.haveRetries()) {
//          request.countRetry();
//          resp = retryRequest(request);
//        }
//        rmFailed(1);
//        rmFailed(2);
//        rmFailed(3);
//        break;
      default:
        resp = "Fail: " + request.noRequestSendError();
        break;
    }
    System.out.println(
      "Responses remain : " +
      latch.getCount() +
      " Response to be sent to client " +
      resp
    );
    return resp;
  }

  private String getResFromMajority(Response res1,Response res2,Response res3){
    if (res1.equals(res2)) {
      return res1.getResponse();
    }else if(res1.equals(res3)){
      return res1.getResponse();
    }else if(res2.equals(res3)){
      return res2.getResponse();
    }else {
      return res1.getResponse();
    }
  }

  private String findMajorityResponse(Request request) {
    Response res1 = null;
    Response res2 = null;
    Response res3 = null;
    for (Response response : responses) {
      if (response.getSequenceID() == request.getSequenceNumber()) {
        switch (response.getRmNumber()) {
          case 1:
            res1 = response;
            break;
          case 2:
            res2 = response;
            break;
          case 3:
            res3 = response;
            break;
        }
      }
    }
    System.out.println(
      "RM1 Res" +
      ((res1 != null) ? res1.getResponse() : "null")
    );
    System.out.println(
      "RM2 Res" +
      ((res2 != null) ? res2.getResponse() : "null")
    );
    System.out.println(
      "RM3 Res" +
      ((res3 != null) ? res3.getResponse() : "null")
    );
    if (res1 == null) {
      rmFailed(1);
      return res2.getResponse();
    }
    else if(res2==null){
      rmFailed(2);

      return res1.getResponse();
    }
    else if(res3==null){
      rmFailed(3);
      return res3.getResponse();
    } else{
      //got res from all
      return getResFromMajority(res1,res2,res3);
    }


//
//    else {
//
//      if (res1.equals(res2)) {
//        if (!res1.equals(res3) && res3 != null) {
//          rmSoftwareFailureIn(3);
//        }
//        return res2.getResponse();
//      } else if (res1.equals(res3)) {
//        if (!res1.equals(res2) && res2 != null) {
//          rmSoftwareFailureIn(2);
//        }
//        return res1.getResponse();
//      } else {
//        //                if (res2 != null && res2.equals(res3)) {
//        if (res2 == null && res3 == null) {
//          return res1.getResponse();
//        } else {
//          //                    rmBugFound(1);
//        }
//        //                    return res2.getResponse();
//        //                }
//      }
//    }
//    if (res2 == null) {
//      rmFailed(2);
//    } else {
//
//      if (res2.equals(res3)) {
//        if (!res2.equals(res1) && res1 != null) {
//          rmSoftwareFailureIn(1);
//        }
//        return res2.getResponse();
//      } else if (res2.equals(res1)) {
//        if (!res2.equals(res3) && res3 != null) {
//          rmSoftwareFailureIn(3);
//        }
//        return res2.getResponse();
//      } else {
//        //                if (!res1.equals("null") && res1.equals(res3)) {
//        if (res1 == null && res3 == null) {
//          return res2.getResponse();
//        } else {
//          //                    rmBugFound(2);
//        }
//        //                }
//        //                return res1;
//      }
//    }
//    if (res3 == null) {
//      rmFailed(3);
//    } else {
//
//      if (res3.equals(res2)) {
//        if (!res3.equals(res1) && res1 != null) {
//          rmSoftwareFailureIn(1);
//        }
//        return res2.getResponse();
//      } else if (res3.equals(res1) && res2 != null) {
//        if (!res3.equals(res2)) {
//          rmSoftwareFailureIn(2);
//        }
//        return res3.getResponse();
//      } else {
//        //                if (!res2.equals("null") && res2.equals(res1)) {
//        if (res1 == null && res2 == null) {
//          return res3.getResponse();
//        } else {
//          //                    rmBugFound(3);
//        }
//        //                }
//        //                return res1;
//      }
//    }
//    return "Fail: majority response not found";
  }

  private void rmSoftwareFailureIn(int rmNumber) {
    switch (rmNumber) {
      case 1:
        rm1Errors++;
        if (rm1Errors == 3) {
          rm1Errors = 0;
          inter.informSoftwareFailureIn(rmNumber);
        }
        break;
      case 2:
        rm2Errors++;
        if (rm2Errors == 3) {
          rm2Errors = 0;
          inter.informSoftwareFailureIn(rmNumber);
        }
        break;
      case 3:
        rm3Errors++;
        if (rm3Errors == 3) {
          rm3Errors = 0;
          inter.informSoftwareFailureIn(rmNumber);
        }
        break;
    }
    System.out.println(
      "RM1 - errors:" + rm1Errors
    );
    System.out.println(
      "RM2 - errors:" + rm2Errors
    );
    System.out.println(
      "RM3 - errors:" + rm3Errors
    );
  }

  private void rmFailed(int rmNumber) {
    DYNAMIC_TIMEOUT = 10000;
    inter.InformReplicaDown(rmNumber);
    System.out.println(
      "FE Implementation, rmDown, : " + rmNumber
    );

  }

  private void setDynamicTimout() {
    if (responseTime < 4000) {
      DYNAMIC_TIMEOUT = (DYNAMIC_TIMEOUT + (responseTime * 3)) / 2;
      //            System.out.println("FE Implementation:setDynamicTimout>>>" + responseTime * 2);
    } else {
      DYNAMIC_TIMEOUT = 10000;
    }
    System.out.println(
      "FE Implementation:setDynamicTimout>>>" + DYNAMIC_TIMEOUT
    );
  }

  private void notifyOKCommandReceived() {
    latch.countDown();
    System.out.println(
      "FE Implementation:notifyOKCommandReceived>>>Response Received: Remaining responses" +
      latch.getCount()
    );
  }

  public void addReceivedResponse(Response res) {
    long endTime = System.nanoTime();
    responseTime = (endTime - startTime) / 1000000;
    System.out.println("Current Response time is: " + responseTime);
    responses.add(res);
    notifyOKCommandReceived();
  }

  private int sendUdpUnicastToSequencer(Request request) {
    startTime = System.nanoTime();
    int sequenceNumber = inter.sendRequestToSequencer(request);
    request.setSequenceNumber(sequenceNumber);
    latch = new CountDownLatch(3);
    waitForResponse();
    return sequenceNumber;
  }

  private String retryRequest(Request request) {
    System.out.println(
      "retrying" + request.toString()
    );
    startTime = System.nanoTime();
    inter.retryRequest(request);
    latch = new CountDownLatch(3);
    waitForResponse();
    return validateResponses(request);
  }

  @Override
  public String addMovieSlots(String clientID,String movieID, String movieName, int bookingCapacity) {
    System.out.println("addMovieSlots");
    Request request = new Request("addMovieSlots", null);
    request.setNewMovieID(movieID).setClientID(clientID).setNewMovieName(movieName).setBookingCapacity(bookingCapacity).setSequenceNumber(sendUdpUnicastToSequencer(request));

    System.out.println("FE addMovieSlots: " + request.toString());
    return validateResponses(request);
//    return null;
  }

  @Override
  public String removeMovieSlots(String clientID,String movieID, String movieName) {
    System.out.println("removeMovieSlots");
    Request request = new Request("removeMovieSlots", clientID);
    request.setClientID(clientID).setNewMovieID(movieID).setNewMovieName(movieName).setSequenceNumber(sendUdpUnicastToSequencer(request));

    System.out.println("FE removeMovieSlots: " + request.toString());
    return validateResponses(request);
  }

  @Override
  public String listMovieShowsAvailability(String clientID,String movieName) {
    System.out.println("listMovieShowsAvailability");
    Request request = new Request("listMovieShowsAvailability", clientID);
    request.setClientID(clientID).setNewMovieName(movieName).setSequenceNumber(sendUdpUnicastToSequencer(request));

    System.out.println("FE listMovieShowsAvailability: " + request.toString());
    return validateResponses(request);
//    return null;
  }

  @Override
  public String bookMovieTickets(String customerID, String movieID, String movieName, int numberOfTickets) {
    System.out.println("bookMovieTickets");
    Request request = new Request("bookMovieTickets",customerID);
    request.setNewMovieID(movieID).setClientID(customerID).setNewMovieName(movieName).setNumberOfTickets(numberOfTickets).setSequenceNumber(sendUdpUnicastToSequencer(request));

    System.out.println("FE bookMovieTickets: " + request.toString());
    return validateResponses(request);
//    return null;
  }

  @Override
  public String getBookingSchedule(String customerID) {
    System.out.println("getBookingSchedule");
        Request request = new Request("getBookingSchedule", customerID);

   request.setSequenceNumber( sendUdpUnicastToSequencer(request));
    System.out.println("FE getBookingSchedule>>>" + request);
    return validateResponses(request);
  }

  @Override
  public String cancelMovieTickets(String customerID, String movieID, String movieName, int numberOfTickets) {
    Request request = new Request("cancelMovieTickets",customerID);
    request.setNewMovieID(movieID).setClientID(customerID).setNewMovieName(movieName).setNumberOfTickets(numberOfTickets).setSequenceNumber(sendUdpUnicastToSequencer(request));

    System.out.println("FE cancelMovieTickets: " + request.toString());
    return validateResponses(request);
  }

  @Override
  public String exchangeTickets(String customerID, String oldMovieID, String newMovieID, String oldMovieName, String newMovieName, int numberOfTickets) {
    Request request = new Request("exchangeTickets",customerID);
    request.setNewMovieID(newMovieID).setClientID(customerID).setNewMovieName(newMovieName).setOldMovieName(oldMovieName).setOldMovieID(oldMovieID).setNumberOfTickets(numberOfTickets).setSequenceNumber(sendUdpUnicastToSequencer(request));

    System.out.println("FE exchangeTickets: " + request.toString());
    return validateResponses(request);
  }
}
