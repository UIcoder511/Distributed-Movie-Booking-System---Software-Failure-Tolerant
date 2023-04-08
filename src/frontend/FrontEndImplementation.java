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
//
//  @Override
//  public synchronized String addEvent(
//    String managerID,
//    String eventID,
//    String eventType,
//    int bookingCapacity
//  ) {
//    Request request = new Request("addEvent", managerID);
//    myRequest.setEventID(eventID);
//    myRequest.setEventType(eventType);
//    myRequest.setBookingCapacity(bookingCapacity);
//    myRequest.setSequenceNumber(sendUdpUnicastToSequencer(myRequest));
//    System.out.println("FE Implementation:addEvent>>>" + myRequest.toString());
//    return validateResponses(myRequest);
//  }
//
//  @Override
//  public synchronized String removeEvent(
//    String managerID,
//    String eventID,
//    String eventType
//  ) {
//    MyRequest myRequest = new MyRequest("removeEvent", managerID);
//    myRequest.setEventID(eventID);
//    myRequest.setEventType(eventType);
//    myRequest.setSequenceNumber(sendUdpUnicastToSequencer(myRequest));
//    System.out.println(
//      "FE Implementation:removeEvent>>>" + myRequest.toString()
//    );
//    return validateResponses(myRequest);
//  }
//
//  @Override
//  public synchronized String listEventAvailability(
//    String managerID,
//    String eventType
//  ) {
//    MyRequest myRequest = new MyRequest("listEventAvailability", managerID);
//    myRequest.setEventType(eventType);
//    myRequest.setSequenceNumber(sendUdpUnicastToSequencer(myRequest));
//    System.out.println(
//      "FE Implementation:listEventAvailability>>>" + myRequest.toString()
//    );
//    return validateResponses(myRequest);
//  }
//
//  @Override
//  public synchronized String bookEvent(
//    String customerID,
//    String eventID,
//    String eventType
//  ) {
//    MyRequest myRequest = new MyRequest("bookEvent", customerID);
//    myRequest.setEventID(eventID);
//    myRequest.setEventType(eventType);
//    myRequest.setSequenceNumber(sendUdpUnicastToSequencer(myRequest));
//    System.out.println("FE Implementation:bookEvent>>>" + myRequest.toString());
//    return validateResponses(myRequest);
//  }
//
//  @Override
//  public synchronized String getBookingSchedule(String customerID) {
//    MyRequest myRequest = new MyRequest("getBookingSchedule", customerID);
//    myRequest.setSequenceNumber(sendUdpUnicastToSequencer(myRequest));
//    System.out.println(
//      "FE Implementation:getBookingSchedule>>>" + myRequest.toString()
//    );
//    return validateResponses(myRequest);
//  }
//
//  @Override
//  public synchronized String cancelEvent(
//    String customerID,
//    String eventID,
//    String eventType
//  ) {
//    MyRequest myRequest = new MyRequest("cancelEvent", customerID);
//    myRequest.setEventID(eventID);
//    myRequest.setEventType(eventType);
//    myRequest.setSequenceNumber(sendUdpUnicastToSequencer(myRequest));
//    System.out.println(
//      "FE Implementation:cancelEvent>>>" + myRequest.toString()
//    );
//    return validateResponses(myRequest);
//  }
//
//  @Override
//  public synchronized String swapEvent(
//    String customerID,
//    String newEventID,
//    String newEventType,
//    String oldEventID,
//    String oldEventType
//  ) {
//    MyRequest myRequest = new MyRequest("swapEvent", customerID);
//    myRequest.setEventID(newEventID);
//    myRequest.setEventType(newEventType);
//    myRequest.setOldEventID(oldEventID);
//    myRequest.setOldEventType(oldEventType);
//    myRequest.setSequenceNumber(sendUdpUnicastToSequencer(myRequest));
//    System.out.println("FE Implementation:swapEvent>>>" + myRequest.toString());
//    return validateResponses(myRequest);
//  }

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
      case 3:
        resp = "Fail: No response from any server";
        System.out.println(resp);
        if (request.haveRetries()) {
          request.countRetry();
          resp = retryRequest(request);
        }
        rmFailed(1);
        rmFailed(2);
        rmFailed(3);
        break;
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
    } else {

      if (res1.equals(res2)) {
        if (!res1.equals(res3) && res3 != null) {
          rmSoftwareFailureIn(3);
        }
        return res2.getResponse();
      } else if (res1.equals(res3)) {
        if (!res1.equals(res2) && res2 != null) {
          rmSoftwareFailureIn(2);
        }
        return res1.getResponse();
      } else {
        //                if (res2 != null && res2.equals(res3)) {
        if (res2 == null && res3 == null) {
          return res1.getResponse();
        } else {
          //                    rmBugFound(1);
        }
        //                    return res2.getResponse();
        //                }
      }
    }
    if (res2 == null) {
      rmFailed(2);
    } else {

      if (res2.equals(res3)) {
        if (!res2.equals(res1) && res1 != null) {
          rmSoftwareFailureIn(1);
        }
        return res2.getResponse();
      } else if (res2.equals(res1)) {
        if (!res2.equals(res3) && res3 != null) {
          rmSoftwareFailureIn(3);
        }
        return res2.getResponse();
      } else {
        //                if (!res1.equals("null") && res1.equals(res3)) {
        if (res1 == null && res3 == null) {
          return res2.getResponse();
        } else {
          //                    rmBugFound(2);
        }
        //                }
        //                return res1;
      }
    }
    if (res3 == null) {
      rmFailed(3);
    } else {

      if (res3.equals(res2)) {
        if (!res3.equals(res1) && res1 != null) {
          rmSoftwareFailureIn(1);
        }
        return res2.getResponse();
      } else if (res3.equals(res1) && res2 != null) {
        if (!res3.equals(res2)) {
          rmSoftwareFailureIn(2);
        }
        return res3.getResponse();
      } else {
        //                if (!res2.equals("null") && res2.equals(res1)) {
        if (res1 == null && res2 == null) {
          return res3.getResponse();
        } else {
          //                    rmBugFound(3);
        }
        //                }
        //                return res1;
      }
    }
    return "Fail: majority response not found";
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
  public String addMovieSlots(String movieID, String movieName, int bookingCapacity) {
    System.out.println("addMovieSlots");
    return null;
  }

  @Override
  public String removeMovieSlots(String movieID, String movieName) {
    System.out.println("removeMovieSlots");
    return null;
  }

  @Override
  public String listMovieShowsAvailability(String movieName) {
    System.out.println("listMovieShowsAvailability");
    return null;
  }

  @Override
  public String bookMovieTickets(String customerID, String movieID, String movieName, int numberOfTickets) {
    System.out.println("bookMovieTickets");
    return null;
  }

  @Override
  public String getBookingSchedule(String customerID) {
    System.out.println("getBookingSchedule");
        Request request = new Request("getBookingSchedule", customerID);

    sendUdpUnicastToSequencer(request);
    System.out.println("FE getBookingSchedule>>>" + request);
    return validateResponses(request);
  }

  @Override
  public String cancelMovieTickets(String customerID, String movieID, String movieName, int numberOfTickets) {
    return null;
  }

  @Override
  public String exchangeTickets(String customerID, String movieID, String newMovieID, String oldMovieName, String movieName, int numberOfTickets) {
    return null;
  }
}
