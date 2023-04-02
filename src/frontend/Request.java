package frontend;
// import Configs;

public class Request {

  private String function = "null";
  private String customerID = "null";
  private String oldMovieID = "null";
  private String oldMovieName = "null";
  private String numberOfTickets = "null";
  private String newMovieID = "null";
  private String newMovieName = "null";
  private String movieID = "null";
  private String movieName = "null";
  private String FeIpAddress = ;
  private int bookingCapacity = 0;
  private int sequenceNumber = 0;
  private String MessageType = "00";
  private int retryCount = 1;

  public Request(String function, String customerID) {
    setFunction(function);
    setCustomerID(customerID);
  }

  public Request(int rmNumber, String bugType) {
    setMessageType(bugType + rmNumber);
  }

  public String getFunction() {
    return function;
  }

  public void setFunction(String function) {
    this.function = function;
  }

  public String getCustomerID() {
    return customerID;
  }

  public void setCustomerID(String customerID) {
    this.customerID = customerID;
  }

  public int getBookingCapacity() {
    return bookingCapacity;
  }

  public void setBookingCapacity(int bookingCapacity) {
    this.bookingCapacity = bookingCapacity;
  }

  public String noRequestSendError() {
    return (
      "request: " + getFunction() + " from " + getCustomerID() + " not sent"
    );
  }

  public int getSequenceNumber() {
    return sequenceNumber;
  }

  public void setSequenceNumber(int sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
  }

  public String getFeIpAddress() {
    return FeIpAddress;
  }

  public void setFeIpAddress(String feIpAddress) {
    FeIpAddress = feIpAddress;
  }

  public String getMessageType() {
    return MessageType;
  }

  public void setMessageType(String messageType) {
    MessageType = messageType;
  }

  public boolean haveRetries() {
    return retryCount > 0;
  }

  public void countRetry() {
    retryCount--;
  }

  public String getMovieID() {
    return movieID;
  }

  public String getMovieName() {
    return movieName;
  }

  public String getNewMovieID() {
    return newMovieID;
  }

  public String getNewMovieName() {
    return newMovieName;
  }

  public String getNumberOfTickets() {
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

  public void setMovieID(String movieID) {
    this.movieID = movieID;
  }

  public void setMovieName(String movieName) {
    this.movieName = movieName;
  }

  public void setNewMovieID(String newMovieID) {
    this.newMovieID = newMovieID;
  }

  public void setNewMovieName(String newMovieName) {
    this.newMovieName = newMovieName;
  }

  public void setNumberOfTickets(String numberOfTickets) {
    this.numberOfTickets = numberOfTickets;
  }

  public void setOldMovieID(String oldMovieID) {
    this.oldMovieID = oldMovieID;
  }

  public void setOldMovieName(String oldMovieName) {
    this.oldMovieName = oldMovieName;
  }
}
