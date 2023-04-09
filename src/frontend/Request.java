package frontend;

import configs.Configs;

//import src.Configs;

public class Request {

  private String function = "null";
  private String clientID = "null";
  private String oldMovieID = "null";
  private String oldMovieName = "null";
  private int numberOfTickets = 0;


  private String newMovieID = "null";
  private String newMovieName = "null";
  private String movieID = "null";
  private String movieName = "null";
  private String FeIpAddress = Configs.FE_IP_Address;
  private int bookingCapacity = 0;
  private int sequenceNumber = 0;
  private String MessageType = "00";
  private int retryCount = 1;

  private boolean isRequestFromAdmin = false;

  public Request(String function, String clientID) {
    setFunction(function);
    setClientID(clientID);
    if (clientID==null) isRequestFromAdmin = true;
  }

  public Request(int rmNumber, String bugType) {
    setMessageType(bugType + rmNumber);
  }

  public String getFunction() {
    return function;
  }

  public Request setFunction(String function) {
    this.function = function;
    return this;
  }

  public String getClientID() {
    return clientID;
  }

  public Request setClientID(String clientID) {
    this.clientID = clientID;
    return this;
  }

  public int getBookingCapacity() {
    return bookingCapacity;
  }

  public Request setBookingCapacity(int bookingCapacity) {
    this.bookingCapacity = bookingCapacity;
    return this;
  }

  public String noRequestSendError() {
    return (
      "request: " + getFunction() + " from " + getClientID() + " not sent"
    );
  }

  public int getSequenceNumber() {
    return sequenceNumber;
  }

  public Request setSequenceNumber(int sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
    return this;
  }

  public String getFeIpAddress() {
    return FeIpAddress;
  }

  public Request setFeIpAddress(String feIpAddress) {
    FeIpAddress = feIpAddress;return this;
  }

  public String getMessageType() {
    return MessageType;
  }

  public Request setMessageType(String messageType) {
    MessageType = messageType;return this;
  }

  public boolean haveRetries() {
    return retryCount > 0;
  }

  public void countRetry() {
    retryCount--;
  }
//
//  public String getMovieID() {
//    return movieID;
//  }
//
//  public String getMovieName() {
//    return movieName;
//  }

  public String getNewMovieID() {
    return newMovieID;
  }

  public String getNewMovieName() {
    return newMovieName;
  }

  public int getNumberOfTickets() {
    return numberOfTickets;
  }

  public String getOldMovieID() {
    return oldMovieID;
  }

  public String getOldMovieName() {
    return oldMovieName;
  }

  //   public int getRetryCount() {
  //     return retryCount;
  //   }

//  public Request setMovieID(String movieID) {
//    this.movieID = movieID;
//    return this;
//  }
//
//  public Request setMovieName(String movieName) {
//    this.movieName = movieName;
//    return this;
//  }

  public Request setNewMovieID(String newMovieID) {
    this.newMovieID = newMovieID;
    return this;
  }

  public Request setNewMovieName(String newMovieName) {
    this.newMovieName = newMovieName;
    return this;
  }

  public Request setNumberOfTickets(int numberOfTickets) {
    this.numberOfTickets = numberOfTickets;
    return this;
  }

  public Request setOldMovieID(String oldMovieID) {
    this.oldMovieID = oldMovieID;
    return this;
  }

  public Request setOldMovieName(String oldMovieName) {
    this.oldMovieName = oldMovieName;
    return this;
  }

  @Override
  public String toString() {
    return getSequenceNumber() + ";" +
            getFeIpAddress().toUpperCase() + ";" +
            getMessageType().toUpperCase() + ";" +
            getFunction().toUpperCase() + ";" +
            (getClientID()==null?"":getClientID()).toUpperCase() + ";" +
            getNewMovieID().toUpperCase() + ";" +
            getNewMovieName().toUpperCase() + ";" +
            getBookingCapacity() + ";" +
            getOldMovieID().toUpperCase() + ";" +
            getOldMovieName().toUpperCase() + ";" +
            getNumberOfTickets() ;
  }
}
