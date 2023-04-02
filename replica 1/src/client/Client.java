package client;

import DMS_CORBA.CommonInterface;
import DMS_CORBA.CommonInterfaceHelper;
import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Pattern;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

public class Client {

  //    private String clientID;
  //    private boolean isAdmin;
  private static final String LOGS_DIR =
    System.getProperty("user.dir") + "\\src\\client\\logs";
  private static final Scanner sc = new Scanner(System.in);
  private static Logger logger;

  public static void main(String[] args) throws IOException {

     new Client(args);
  }

  public Client(String[] args) throws IOException {
    System.out.println("_________________________________________");
    System.out.println("\n\nWelcome to our movie booking system!!\n\n");
    System.out.println("_________________________________________\n\n");

    String clientID = getClientID();
    String clientFromServer = clientID.substring(0, 3);
    boolean isAdmin = clientID.charAt(3) == 'A';
    //        int clientCode = Integer.parseInt(clientID.substring(4));

    addLogger(clientID);
    setupServerRegistries(args, clientID, clientFromServer, isAdmin);
    System.out.println("_________________________________________");
    System.out.println("\n\nThanks for using our movie booking system!!\n\n");
    System.out.println("_________________________________________\n\n");
  }

  public static void setupServerRegistries(
    String[] args,
    String clientID,
    String clientFromServer,
    boolean isAdmin
  ) {
    // resolve the Object Reference in Naming and narrows the returned
    // reference to a Hello interface.
    String port = "";
    switch (clientFromServer) {
      case "ATW":
        port = "7777";

        break;
      case "VER":
        port = "8888";
        break;
      case "OUT":
        port = "9999";
        break;
    }

    Properties props = new Properties();
    props.put("org.omg.CORBA.ORBInitialPort", port);
    props.put("org.omg.CORBA.ORBInitialHost", "localhost");
    ORB orb = ORB.init(args, props);
    // create and initialize the ORB

    // get the root naming context
    // This line resolves the initial reference to the NameService,
    // which is used to locate the remote object.
    org.omg.CORBA.Object objRef = null;
    try {
      objRef = orb.resolve_initial_references("NameService");
    } catch (InvalidName e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    // This line narrows the initial reference to a NamingContext,
    // which is used to look up the remote object.
    NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

    String name = "TH_IMPL";
    CommonInterface server = null;
    try {
      server = CommonInterfaceHelper.narrow(ncRef.resolve_str(name));
    } catch (NotFound e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (CannotProceed e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (org.omg.CosNaming.NamingContextPackage.InvalidName e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    try {
      if (isAdmin) {
        // AdminInterface server = null;
        // try {
        //   server = (AdminInterface) setupLookup(clientFromServer);
        showAdminMainMenu(server, clientFromServer);
        // } catch (NotBoundException e) {
        //   System.out.println("Server is not found in the registry");
        // } catch (Exception e) {
        //   System.out.println("Remote connection error");
        // }
        //  test1(server);
        //                test2(server);

      } else {
        // CustomerInterface server = (CustomerInterface) setupLookup(
        //   clientFromServer
        // );
        //                System.out.println(
        //                        server.bookMovieTickets("ATWC2345", "ATWM070223", "Avatar", 10));
        //ask customer to select server
        //-----
        showCustomerMainMenu(clientID, server);
      }
      //                while (true) {
      //                    boolean shouldBreak = true;
      //                    System.out.println("\n_____________\nSelect Theatre:\n_____________\n ");
      //                    System.out.println("\n1. Atwater ");
      //                    System.out.println("\n2. Verdun");
      //                    System.out.println("\n3. Outremont");
      //                    System.out.println("\nYour choice > ");
      //                    int choice = sc.nextInt();
      //                    switch (choice) {
      //                        case 1:
      //                            server = (CustomerInterface) setupLookup("ATW");
      //                            break;
      //                        case 2:
      //
      //                            server = (CustomerInterface) setupLookup("VER");
      //                            break;
      //                        case 3:
      //                            server = (CustomerInterface) setupLookup("OUT");
      //                            break;
      //                        default:
      //                            shouldBreak = false;
      //                            System.out.println("\nWrong choice!! please select again\n");
      //                    }
      //                    if (shouldBreak)
      //                        break;
      //                }
      //                testClient(server, clientID);

      //            }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void staticAdminMainMenu() {
    String msg =
      "\n\nWhat action you want to perform?" +
      "\n 1. Add Movie slots" +
      "\n 2. Remove Movie Slot" +
      "\n 3. List Movie Shows Availability" +
      "\n 4. Quit";

    System.out.println(msg);
    System.out.print("\nEnter your choice > ");
  }

  public static void staticCustomerMainMenu() {
    String msg =
      "\n\nWhat action you want to perform?" +
      "\n 1. Book movie tickets" +
      "\n 2. Cancel movie tickets" +
      "\n 3. Display booking schedule" +
      "\n 4. Exchange Tickets" +
      "\n 5. Quit";

    System.out.println(msg);
    System.out.print("\nEnter your choice > ");
  }

  public static String getTheatreInput() {
    String op = "";
    byte choice = -1;
    boolean breakLoop = false;
    while (!breakLoop) {
      System.out.println("\n\nSelect Theatre : ");
      System.out.println("1. Atwater : ");
      System.out.println("2. Verdun : ");
      System.out.println("3. Outremont : ");
      System.out.print("\nEnter your choice > ");

      choice = sc.nextByte();

      switch (choice) {
        case 1:
          op = "ATW";
          breakLoop = true;
          break;
        case 2:
          op = "VER";
          breakLoop = true;
          break;
        case 3:
          op = "OUT";
          breakLoop = true;
          break;
        default:
          System.out.println("\nPlease enter a valid choice!");
      }
    }

    return op;
  }

  public static String getSlotInput() {
    String op = "";
    byte choice = -1;
    boolean breakLoop = false;
    while (!breakLoop) {
      System.out.println("\n\nSelect Movie slot : ");
      System.out.println("1. Morning : ");
      System.out.println("2. Afternoon : ");
      System.out.println("3. Evening : ");
      System.out.print("\nEnter your choice > ");

      choice = sc.nextByte();

      switch (choice) {
        case 1:
          op = "M";
          breakLoop = true;
          break;
        case 2:
          op = "A";
          breakLoop = true;
          break;
        case 3:
          op = "E";
          breakLoop = true;
          break;
        default:
          System.out.println("\nPlease enter a valid choice!");
      }
    }

    return op;
  }

  public static String getMovieInput() {
    String op = "";
    byte choice = -1;
    boolean breakLoop = false;
    while (!breakLoop) {
      System.out.println("\n\nSelect Movie : ");
      System.out.println("1. Avatar : ");
      System.out.println("2. Avengers : ");
      System.out.println("3. Titanic : ");
      System.out.print("\nEnter your choice > ");
      choice = sc.nextByte();

      switch (choice) {
        case 1:
          op = "Avatar";
          breakLoop = true;
          break;
        case 2:
          op = "Avengers";
          breakLoop = true;
          break;
        case 3:
          op = "Titanic";
          breakLoop = true;
          break;
        default:
          System.out.println("\nPlease enter a valid choice!");
      }
    }

    return op;
  }

  public static String getDateInput() {
    String op = "";
    sc.nextLine();
    boolean breakLoop = false;
    while (!breakLoop) {
      System.out.println("\n\nEnter Date in format (DD/MM/YYYY) : ");

      String strDate = sc.nextLine();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
        "dd/MM/yyyy",
        Locale.ENGLISH
      );

      try {
        LocalDate.parse(strDate, formatter);
        String[] date = strDate.split("/");

        op = date[0] + date[1] + date[2].substring(2);
      } catch (DateTimeParseException e) {
        System.out.println("\nPlease enter a valid date in format!");
        continue;
      }

      breakLoop = true;
    }

    return op;
  }

  public static String getMovieIDInput(String clientFromServer) {
    String theatre = clientFromServer != null
      ? clientFromServer
      : getTheatreInput();

    String slot = getSlotInput();

    String date = getDateInput();

    return theatre + slot + date;
  }

  public static int getBookingCapacityInput(boolean isBooking) {
    int capacity = 0;

    while (true) {
      System.out.println(
        isBooking ? "\nEnter number of tickets : " : "\nEnter movie capacity : "
      );
      capacity = sc.nextInt();
      if (capacity > 0) {
        return capacity;
      }
      System.out.println(
        isBooking
          ? "\nCan not have tickets - "
          : "\nCan not add movie with capacity - " + capacity
      );
    }
  }

  public static void showAdminMainMenu(
    CommonInterface server,
    String clientFromServer
  ) {
    byte choice = -1;

    while (choice != 4) {
      staticAdminMainMenu();
      choice = sc.nextByte();
      String res = "";
      switch (choice) {
        case 1:
          String movieName = getMovieInput();
          String movieID = getMovieIDInput(clientFromServer);
          int bookingCapacity = getBookingCapacityInput(false);
          String req =
            "movieName : " +
            movieName +
            ", movieID : " +
            movieID +
            ", bookingCapacity : " +
            bookingCapacity;
          System.out.println("Adding : " + req);

          utils.Logger.CustomMessage msg = new utils.Logger.CustomMessage(
            "addMovieSlots",
            req,
            "Function Call",
            ""
          );
          logger.log(Level.INFO, msg.toString());

          try {
            res = server.addMovieSlots(movieID, movieName, bookingCapacity);
          } catch (Exception e) {
            msg =
              new utils.Logger.CustomMessage(
                "addMovieSlots",
                req,
                "Error",
                "Server Exception : movie did not added!"
              );
            logger.log(Level.SEVERE, msg.toString());
            System.out.println("Server Exception : movie did not added!");
            throw new RuntimeException(e);
          }

          System.out.println(res);
          boolean isSuccess = res.equals("Movie slot(s) added");
          msg =
            new utils.Logger.CustomMessage(
              "addMovieSlots",
              req,
              isSuccess ? "Success" : "Error",
              res
            );

          logger.log(isSuccess ? Level.INFO : Level.SEVERE, msg.toString());

          break;
        case 2:
          movieName = getMovieInput();
          movieID = getMovieIDInput(clientFromServer);
          req = "movieName : " + movieName + ", movieID : " + movieID;

          System.out.println("Removing : " + req);

          msg =
            new utils.Logger.CustomMessage(
              "removeMovieSlots",
              req,
              "Function Call",
              ""
            );
          logger.log(Level.INFO, msg.toString());

          try {
            res = server.removeMovieSlots(movieID, movieName);
          } catch (Exception e) {
            msg =
              new utils.Logger.CustomMessage(
                "removeMovieSlots",
                req,
                "Error",
                "Server Exception : movie did not deleted!"
              );
            logger.log(Level.SEVERE, msg.toString());
            System.out.println("Server Exception : movie did not deleted!");
            throw new RuntimeException(e);
          }

          System.out.println(res);
          isSuccess = res.equals("Movie slot(s) removed");
          msg =
            new utils.Logger.CustomMessage(
              "removeMovieSlots",
              req,
              isSuccess ? "Success" : "Error",
              res
            );

          logger.log(isSuccess ? Level.INFO : Level.SEVERE, msg.toString());

          break;
        case 3:
          movieName = getMovieInput();
          req = "movieName : " + movieName;
          try {
            res = server.listMovieShowsAvailability(movieName);
          } catch (Exception e) {
            msg =
              new utils.Logger.CustomMessage(
                "listMovieShowsAvailability",
                req,
                "Error",
                "Server Exception"
              );
            logger.log(Level.SEVERE, msg.toString());
            System.out.println("Server Exception");
            //                        throw new RuntimeException(e);
          }

          System.out.println(res);
          msg =
            new utils.Logger.CustomMessage(
              "listMovieShowsAvailability",
              req,
              "Success",
              res
            );

          logger.log(Level.INFO, msg.toString());
          break;
        default:
          System.out.println("\nPlease enter a valid choice!");
      }
    }
  }

  public static void showCustomerMainMenu(
    String customerID,
    CommonInterface server
  ) {
    byte choice = -1;

    while (choice != 5) {
      staticCustomerMainMenu();
      choice = sc.nextByte();
      String res = "";
      switch (choice) {
        case 1:
          String movieName = getMovieInput();
          String movieID = getMovieIDInput(null);
          int bookingCapacity = getBookingCapacityInput(true);
          String req =
            "customerID : " +
            customerID +
            ", movieName : " +
            movieName +
            ", movieID : " +
            movieID +
            ", numberOfTickets : " +
            bookingCapacity;
          System.out.println("Booking : " + req);

          utils.Logger.CustomMessage msg = new utils.Logger.CustomMessage(
            "bookMovieTickets",
            req,
            "Function Call",
            ""
          );
          logger.log(Level.INFO, msg.toString());

          try {
            res =
              server.bookMovieTickets(
                customerID,
                movieID,
                movieName,
                bookingCapacity
              );
          } catch (Exception e) {
            msg =
              new utils.Logger.CustomMessage(
                "bookMovieTickets",
                req,
                "Error",
                "Server Exception : movie did not added!"
              );
            logger.log(Level.SEVERE, msg.toString());
            System.out.println("Server Exception : movie did not added!");
            throw new RuntimeException(e);
          }

          System.out.println(res);
          boolean isSuccess = res.equals("Booking successful");
          msg =
            new utils.Logger.CustomMessage(
              "bookMovieTickets",
              req,
              isSuccess ? "Success" : "Error",
              res
            );

          logger.log(isSuccess ? Level.INFO : Level.SEVERE, msg.toString());

          break;
        case 2:
          movieName = getMovieInput();
          movieID = getMovieIDInput(null);
          bookingCapacity = getBookingCapacityInput(true);
          req =
            "customerID : " +
            customerID +
            ", movieName : " +
            movieName +
            ", movieID : " +
            movieID +
            ", numberOfTickets : " +
            bookingCapacity;
          System.out.println("Cancelling : " + req);

          msg =
            new utils.Logger.CustomMessage(
              "cancelMovieTickets",
              req,
              "Function Call",
              ""
            );
          logger.log(Level.INFO, msg.toString());

          try {
            res =
              server.cancelMovieTickets(
                customerID,
                movieID,
                movieName,
                bookingCapacity
              );
          } catch (Exception e) {
            msg =
              new utils.Logger.CustomMessage(
                "cancelMovieTickets",
                req,
                "Error",
                "Server Exception : movie did not added!"
              );
            logger.log(Level.SEVERE, msg.toString());
            System.out.println("Server Exception : movie did not added!");
            throw new RuntimeException(e);
          }

          System.out.println(res);
          isSuccess = res.equals("tickets cancelled successfully");
          msg =
            new utils.Logger.CustomMessage(
              "cancelMovieTickets",
              req,
              isSuccess ? "Success" : "Error",
              res
            );

          logger.log(isSuccess ? Level.INFO : Level.SEVERE, msg.toString());

          break;
        case 3:
          req = "customerID : " + customerID;
          try {
            res = server.getBookingSchedule(customerID);
          } catch (Exception e) {
            msg =
              new utils.Logger.CustomMessage(
                "getBookingSchedule",
                req,
                "Error",
                "Server Exception"
              );
            logger.log(Level.SEVERE, msg.toString());
            System.out.println("Server Exception");
            //                        throw new RuntimeException(e);
          }

          System.out.println(res);
          msg =
            new utils.Logger.CustomMessage(
              "getBookingSchedule",
              req,
              "Success",
              res
            );

          logger.log(Level.INFO, msg.toString());
          break;
        case 4:
          System.out.println("\n____________Enter old movie details____________");
          String oldMovieName = getMovieInput();
//          System.out.println("Enter 1old movie details : ");
          String oldMovieID = getMovieIDInput(null);
          System.out.println("\n____________Enter new movie details____________");
          String newMovieName = getMovieInput();
//          System.out.println("Enter new movie details : ");
          String newMovieID = getMovieIDInput(null);

          bookingCapacity = getBookingCapacityInput(true);

          req =
            "customerID : " +
            customerID +
            ", oldMovieName : " +
            oldMovieName +
            ", oldMovieID : " +
            oldMovieID +
            ", newMovieName : " +
            newMovieName +
            ", newMovieID : " +
            newMovieID +
            ", numberOfTickets : " +
            bookingCapacity;
          System.out.println("Cancelling : " + req);

          msg =
            new utils.Logger.CustomMessage(
              "exchangeTickets",
              req,
              "Function Call",
              ""
            );
          logger.log(Level.INFO, msg.toString());

          try {
            res =
              server.exchangeTickets(
                customerID,
                oldMovieID,
                newMovieID,
                oldMovieName,
                newMovieName,
                bookingCapacity
              );
          } catch (Exception e) {
            msg =
              new utils.Logger.CustomMessage(
                "exchangeTickets",
                req,
                "Error",
                "Server Exception : movie did not added!"
              );
            logger.log(Level.SEVERE, msg.toString());
            System.out.println("Server Exception : movie did not added!");
            throw new RuntimeException(e);
          }

          System.out.println(res);
          isSuccess = res.equals("successfully exchanged tickets");
          msg =
            new utils.Logger.CustomMessage(
              "exchangeTickets",
              req,
              isSuccess ? "Success" : "Error",
              res
            );

          logger.log(isSuccess ? Level.INFO : Level.SEVERE, msg.toString());

          break;
        default:
          System.out.println("\nPlease enter a valid choice!");
      }
    }
  }

  public static String getClientID() {
    final String regex = "^(ATW|VER|OUT)[A|C][0-9]{4}$";
    String clientID;
    while (true) {
      System.out.println("\n\nEnter ClientID: ");
      clientID = sc.nextLine();
      if (Pattern.compile(regex).matcher(clientID).matches()) {
        break;
      }
      System.out.println("---- Wrong ClientID Format!!");
    }

    return clientID;
  }

  public static void addLogger(String clientID) {
    logger = Logger.getLogger(Client.class.getName());
    logger.setUseParentHandlers(false);
    try {
      System.out.println(LOGS_DIR + "\\" + clientID + ".txt");
      File directory = new File(LOGS_DIR);
      if (!directory.exists()) {
        directory.mkdir();
      }
      directory = new File(LOGS_DIR + "\\" + clientID + ".txt");
      directory.createNewFile();

      FileHandler fh = new FileHandler(directory.getAbsolutePath());
      logger.addHandler(fh);
      SimpleFormatter formatter = new SimpleFormatter();
      fh.setFormatter(formatter);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
//ATWA1234
//OUTA2345
//ATWC3456
//OUTC1324
