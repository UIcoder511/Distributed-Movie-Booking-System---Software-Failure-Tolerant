package servers.Verdun;

import static utils.UDPSendRecieve.recieveUDPMessages;
import static utils.UDPSendRecieve.sendUDPMessages;

import DMS_CORBA.CommonInterface;
import DMS_CORBA.CommonInterfaceHelper;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import servers.Atwater.AtwaterTheatre;
import servers.TheatreImplementation;

public class VerdunTheatre extends TheatreImplementation {

  private static final String LOGS_DIR =
    System.getProperty("user.dir") + "\\src\\servers\\verdun";
  private static Logger logger;

  public VerdunTheatre() {
    super(logger);
  }

  public static void main(String[] args) {
    VerdunTheatre verdunTheatre;
    try {
      addLogger();

      Properties props = new Properties();
      props.put("org.omg.CORBA.ORBInitialPort", "8888");
      props.put("org.omg.CORBA.ORBInitialHost", "localhost");
      //   props.put("org.omg.CORBA.ORBTransport", "UDP");
      // create and initialize the ORB
      ORB orb = ORB.init(args, props);

      // get reference to rootpoa & activate the POAManager
      POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
      rootpoa.the_POAManager().activate();

      // create servant and register it with the ORB
      verdunTheatre = new VerdunTheatre();
      verdunTheatre.addTestData();
      verdunTheatre.setORB(orb);
      org.omg.CORBA.Object ref = rootpoa.servant_to_reference(verdunTheatre);
      CommonInterface href = CommonInterfaceHelper.narrow(ref);

      // get the root naming context
      // NameService invokes the name service
      // This line resolves the initial reference to the NameService,
      // which is used to locate the remote object.
      org.omg.CORBA.Object objRef = orb.resolve_initial_references(
        "NameService"
      );
      // This line narrows the initial reference to a NamingContext,
      // which is used to look up the remote object.
      NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

      // bind the Object Reference in Naming
      String name = "TH_IMPL";
      ncRef.rebind(ncRef.to_name(name), href);

      System.out.println("verdunTheatre ready and waiting ...");

      // wait for invocations from clients


      (
        new Thread(
          new Runnable() {
            @Override
            public void run() {
              System.out.println("verdunTheatre UDP ruuning 8888 ");
              recieveUDPMessages(verdunTheatre, 8888, logger);
            }
          }
        )
      ).start();

      orb.run();
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

  public static void addLogger() {
    logger = Logger.getLogger(VerdunTheatre.class.getName());
    try {
      System.out.println(LOGS_DIR + "\\logs.txt");
      File directory = new File(LOGS_DIR);
      if (!directory.exists()) {
        directory.mkdir();
      }
      directory = new File(LOGS_DIR + "\\logs.txt");
      directory.createNewFile();

      FileHandler fh = new FileHandler(directory.getAbsolutePath());
      logger.addHandler(fh);
      SimpleFormatter formatter = new SimpleFormatter();
      fh.setFormatter(formatter);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void addTestData() {
    addMovieSlotInHashMap("Titanic", "VERE100323", 10);
  }

  @Override
  public String listMovieShowsAvailability(String movieName) {
    String result = "";
    String resultFromOUT = sendUDPMessages(9999, "GET_SHOWS-" + movieName);
    String resultFromATW = sendUDPMessages(7777, "GET_SHOWS-" + movieName);
    String resultLocal = super.listMovieShowsAvailability(movieName);
    String joined = Stream
      .of(resultLocal, resultFromOUT, resultFromATW)
      .filter(s -> s != null && !s.isEmpty())
      .collect(Collectors.joining(","));
    if (joined.isEmpty()) result = ""; else result = movieName + ":" + joined;

    utils.Logger.CustomMessage msg = new utils.Logger.CustomMessage(
      "listMovieShowsAvailability",
      "movieName : " + movieName,
      "Operation Sucessful",
      result
    );

    logger.log(Level.INFO, msg.toString());

    return result;
  }

  @Override
  public String exchangeTickets(
          String customerID,
          String movieID,
          String newMovieID,
          String oldMovieName,
          String movieName,
          int numberOfTickets
  ) {
    String res = super.exchangeTickets(
            customerID,
            movieID,
            newMovieID,
            oldMovieName,
            movieName,
            numberOfTickets
    );
    if (res.equals("")) {
      String bookingFnRes = bookMovieTickets(
              customerID,
              newMovieID,
              movieName,
              numberOfTickets
      );
      if (bookingFnRes.equals("Booking successful")) {
        String cancelTicketsRes = cancelMovieTickets(
                customerID,
                movieID,
                oldMovieName,
                numberOfTickets
        );
        if (cancelTicketsRes.equals("tickets cancelled successfully")) {
          res = "successfully exchanged tickets";
          utils.Logger.CustomMessage msg = new utils.Logger.CustomMessage(
                  "exchangeTickets",
                  "customerID : " +
                          customerID +
                          ", movieID: " +
                          movieID +
                          ", newMovieID: " +
                          newMovieID +
                          ", oldMovieName: " +
                          oldMovieName +
                          ", movieName: " +
                          movieName +
                          ", numberOfTickets: " +
                          numberOfTickets,
                  "Success",
                  res
          );

          logger.log(Level.INFO, msg.toString());
        } else {
          cancelMovieTickets(
                  customerID,
                  newMovieID,
                  movieName,
                  numberOfTickets
          );

          res = "Failed to exchange tickets";
          utils.Logger.CustomMessage msg = new utils.Logger.CustomMessage(
                  "exchangeTickets",
                  "customerID : " +
                          customerID +
                          ", movieID: " +
                          movieID +
                          ", newMovieID: " +
                          newMovieID +
                          ", oldMovieName: " +
                          oldMovieName +
                          ", movieName: " +
                          movieName +
                          ", numberOfTickets: " +
                          numberOfTickets,
                  "Error",
                  res
          );

          logger.log(Level.SEVERE, msg.toString());
        }
      } else {
        res = bookingFnRes;
        utils.Logger.CustomMessage msg = new utils.Logger.CustomMessage(
                "exchangeTickets",
                "customerID : " +
                        customerID +
                        ", movieID: " +
                        movieID +
                        ", newMovieID: " +
                        newMovieID +
                        ", oldMovieName: " +
                        oldMovieName +
                        ", movieName: " +
                        movieName +
                        ", numberOfTickets: " +
                        numberOfTickets,
                "Error",
                res
        );

        logger.log(Level.SEVERE, msg.toString());
      }
    }

    return res;
  }

  @Override
  public synchronized String bookMovieTickets(
    String customerID,
    String movieID,
    String movieName,
    int numberOfTickets
  ) {
    String res = super.bookMovieTickets(
      customerID,
      movieID,
      movieName,
      numberOfTickets
    );
    String movieInServer = movieID.substring(0, 3);
    if (res.equals("SEND_TO_SERVERS")) {
      String propToSend =
        "BOOK_TICKETS-" +
        customerID +
        "," +
        movieID +
        "," +
        movieName +
        "," +
        numberOfTickets;
      switch (movieInServer) {
        case "OUT":
          res = sendUDPMessages(9999, propToSend);
          break;
        case "ATW":
          res = sendUDPMessages(7777, propToSend);
          break;
      }

      if (res.equals("Booking successful")) {
        //                utils.Logger.CustomMessage msg = new utils.Logger.CustomMessage("bookMovieTickets", "customerID : " + customerID + ", movieID: " + movieID + ", movieName: " + movieName + ", numberOfTickets: " + numberOfTickets, "Operation Sucessful", res);
        //                logger.log(Level.INFO, msg.toString());
        addCustomerMovieCount(customerID, movieName, movieID, numberOfTickets);
      }
    }
    return res;
    //        String res = super.bookMovieTickets(customerID, movieID, movieName, numberOfTickets);
    //        if (res == null) {
    //            //book in other servers
    //
    //            //
    //            String customerTicketsInOtherTheatres = getCustomerTicketsInOtherTheatres(customerID);
    //            System.out.println(customerTicketsInOtherTheatres);
    //            //same server -> check customer other server tickets
    //            if (clientFromServer.equals(movieInServer)) {
    //                //write logic
    //
    //                //if sucess
    //                updateMovieCount(movieName, movieID, numberOfTickets, false);
    //                addCustomerMovieCount(customerID, movieName, movieID, numberOfTickets);
    //                res = "Booking successful";
    //
    //                utils.Logger.CustomMessage msg = new utils.Logger.CustomMessage("bookMovieTickets", "customerID : " + customerID + ", movieID: " + movieID + ", movieName: " + movieName + ", numberOfTickets: " + numberOfTickets, "Operation Sucessful", res);
    //
    //                logger.log(Level.INFO, msg.toString());
    //
    //                return res;
    //            }
    //            return "";
    //
    //        } else {
    //            return res;
    //        }
  }
  //    public String getCustomerTicketsInOtherTheatres(String customerID) {
  //        String result = "";
  //        String resultFromOUT = sendUDPMessages(9999, "GET_TICKETS-" + customerID);
  //        String resultFromATW = sendUDPMessages(7777, "GET_TICKETS-" + customerID);
  //        String joined =
  //                Stream.of(resultFromOUT, resultFromATW)
  //                        .filter(s -> s != null && !s.isEmpty())
  //                        .collect(Collectors.joining(","));
  //        if (joined.isEmpty()) result = "";
  //        else result = joined;
  //
  //        utils.Logger.CustomMessage msg = new utils.Logger.CustomMessage("getCustomerTicketsInOtherTheatres", "customerID : " + customerID, "Operation Sucessful", result);
  //
  //        logger.log(Level.INFO, msg.toString());
  //
  //        return result;
  //    }

  //    @Override
  //    public synchronized String getBookingSchedule(String customerID) {
  ////        String result = Stream.of(getCustomerTicketsInCurrentTheatre(customerID), getCustomerTicketsInOtherTheatres(customerID))
  ////                .filter(s -> s != null && !s.isEmpty())
  ////                .collect(Collectors.joining(","));
  ////        utils.Logger.CustomMessage msg = new utils.Logger.CustomMessage("getBookingSchedule", "customerID : " + customerID, "Operation Sucessful", result);
  ////
  ////        logger.log(Level.INFO, msg.toString());
  ////        return result;
  //    }
}
