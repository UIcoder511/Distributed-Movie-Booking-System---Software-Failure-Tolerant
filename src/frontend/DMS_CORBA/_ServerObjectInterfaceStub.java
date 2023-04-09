package frontend.DMS_CORBA;


/**
* DMS_CORBA/_ServerObjectInterfaceStub.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ./ServerObjectInterface.idl
* Saturday, April 8, 2023 3:06:56 o'clock PM EDT
*/

public class _ServerObjectInterfaceStub extends org.omg.CORBA.portable.ObjectImpl implements ServerObjectInterface
{

  public String addMovieSlots (String customerID, String movieID, String movieName, int bookingCapacity)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("addMovieSlots", true);
                $out.write_string (customerID);
                $out.write_string (movieID);
                $out.write_string (movieName);
                $out.write_long (bookingCapacity);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return addMovieSlots (customerID, movieID, movieName, bookingCapacity        );
            } finally {
                _releaseReply ($in);
            }
  } // addMovieSlots

  public String removeMovieSlots (String customerID, String movieID, String movieName)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("removeMovieSlots", true);
                $out.write_string (customerID);
                $out.write_string (movieID);
                $out.write_string (movieName);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return removeMovieSlots (customerID, movieID, movieName        );
            } finally {
                _releaseReply ($in);
            }
  } // removeMovieSlots

  public String listMovieShowsAvailability (String customerID, String movieName)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("listMovieShowsAvailability", true);
                $out.write_string (customerID);
                $out.write_string (movieName);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return listMovieShowsAvailability (customerID, movieName        );
            } finally {
                _releaseReply ($in);
            }
  } // listMovieShowsAvailability

  public String bookMovieTickets (String customerID, String movieID, String movieName, int numberOfTickets)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("bookMovieTickets", true);
                $out.write_string (customerID);
                $out.write_string (movieID);
                $out.write_string (movieName);
                $out.write_long (numberOfTickets);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return bookMovieTickets (customerID, movieID, movieName, numberOfTickets        );
            } finally {
                _releaseReply ($in);
            }
  } // bookMovieTickets

  public String getBookingSchedule (String customerID)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("getBookingSchedule", true);
                $out.write_string (customerID);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return getBookingSchedule (customerID        );
            } finally {
                _releaseReply ($in);
            }
  } // getBookingSchedule

  public String cancelMovieTickets (String customerID, String movieID, String movieName, int numberOfTickets)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("cancelMovieTickets", true);
                $out.write_string (customerID);
                $out.write_string (movieID);
                $out.write_string (movieName);
                $out.write_long (numberOfTickets);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return cancelMovieTickets (customerID, movieID, movieName, numberOfTickets        );
            } finally {
                _releaseReply ($in);
            }
  } // cancelMovieTickets

  public String exchangeTickets (String customerID, String movieID, String newMovieID, String oldMovieName, String movieName, int numberOfTickets)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("exchangeTickets", true);
                $out.write_string (customerID);
                $out.write_string (movieID);
                $out.write_string (newMovieID);
                $out.write_string (oldMovieName);
                $out.write_string (movieName);
                $out.write_long (numberOfTickets);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return exchangeTickets (customerID, movieID, newMovieID, oldMovieName, movieName, numberOfTickets        );
            } finally {
                _releaseReply ($in);
            }
  } // exchangeTickets

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:DMS_CORBA/ServerObjectInterface:1.0"};

  public String[] _ids ()
  {
    return (String[])__ids.clone ();
  }

  private void readObject (java.io.ObjectInputStream s) throws java.io.IOException
  {
     String str = s.readUTF ();
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     org.omg.CORBA.Object obj = orb.string_to_object (str);
     org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate ();
     _set_delegate (delegate);
   } finally {
     orb.destroy() ;
   }
  }

  private void writeObject (java.io.ObjectOutputStream s) throws java.io.IOException
  {
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     String str = orb.object_to_string (this);
     s.writeUTF (str);
   } finally {
     orb.destroy() ;
   }
  }
} // class _ServerObjectInterfaceStub
