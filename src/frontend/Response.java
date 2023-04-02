package frontend;

public class Response {

  private int sequenceID = 0;
  private String response = "null";
  private int rmNumber = 0;
  private String function = "null";
  private String clientID = "null";
  private String newBookingID = "null";
  private String newBookingType = "null";
  private String oldBookingID = "null";
  private String oldBookingType = "null";
  private int bookingCapacity = 0;
  private String udpMessage = "null";
  private boolean isSuccess = false;

  public Response(String udpMessage) {
    setUdpMessage(udpMessage.trim());
    String[] messageParts = getUdpMessage().split(";");
    setSequenceID(Integer.parseInt(messageParts[0]));
    setResponse(messageParts[1].trim());
    setRmNumber(messageParts[2]);
    setFunction(messageParts[3]);
    setCustomerID(messageParts[4]);
    setNewBookingID(messageParts[5]);
    setNewBookingType(messageParts[6]);
    setOldBookingID(messageParts[7]);
    setOldBookingType(messageParts[8]);
    setBookingCapacity(Integer.parseInt(messageParts[9]));
  }

  public int getSequenceID() {
    return sequenceID;
  }

  public void setSequenceID(int sequenceID) {
    this.sequenceID = sequenceID;
  }

  public String getResponse() {
    return response;
  }

  public void setResponse(String response) {
    isSuccess = response.contains("Success:");
    this.response = response;
  }

  public int getRmNumber() {
    return rmNumber;
  }

  public void setRmNumber(String rmNumber) {
    if (rmNumber.equalsIgnoreCase("RM1")) {
      this.rmNumber = 1;
    } else if (rmNumber.equalsIgnoreCase("RM2")) {
      this.rmNumber = 2;
    } else if (rmNumber.equalsIgnoreCase("RM3")) {
      this.rmNumber = 3;
    } else {
      this.rmNumber = 0;
    }
  }

  public String getFunction() {
    return function;
  }

  public void setFunction(String function) {
    this.function = function;
  }

  public String getNewBookingID() {
    return newBookingID;
  }

  public void setNewBookingID(String newBookingID) {
    this.newBookingID = newBookingID;
  }

  public String getNewBookingType() {
    return newBookingType;
  }

  public void setNewBookingType(String newBookingType) {
    this.newBookingType = newBookingType;
  }

  public String getOldBookingID() {
    return oldBookingID;
  }

  public void setOldBookingID(String oldBookingID) {
    this.oldBookingID = oldBookingID;
  }

  public String getOldBookingType() {
    return oldBookingType;
  }

  public void setOldBookingType(String oldBookingType) {
    this.oldBookingType = oldBookingType;
  }

  public int getBookingCapacity() {
    return bookingCapacity;
  }

  public void setBookingCapacity(int bookingCapacity) {
    this.bookingCapacity = bookingCapacity;
  }

  public String getUdpMessage() {
    return udpMessage;
  }

  public void setUdpMessage(String udpMessage) {
    this.udpMessage = udpMessage;
  }

  public boolean isSuccess() {
    return isSuccess;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj != null) {
      if (obj instanceof Response) {
        Response obj1 = (Response) obj;
        return (
          obj1.getFunction().equalsIgnoreCase(this.getFunction()) &&
          obj1.getSequenceID() == this.getSequenceID() &&
          obj1.getUserID().equalsIgnoreCase(this.getUserID()) &&
          obj1.isSuccess() == this.isSuccess()
        );
        //                        && obj1.getResponse().equalsIgnoreCase(this.getResponse());
      }
    }
    return false;
  }
}
