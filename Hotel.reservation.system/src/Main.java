import java.sql.*;
import java.util.Scanner;

public class Main {
    private static final String url="jdbc:mysql://127.0.0.1:3306/hotel_db";
    private static final String username="root";
    private static final String password="sahoo@2005";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        try{
            Connection con= DriverManager.getConnection(url,username,password);
            Statement smt=con.createStatement();
            while(true){
                System.out.println();
                System.out.println("Hotel Management System");
                Scanner sc=new Scanner(System.in);
                System.out.println("1. reserve a room");
                System.out.println("2. view reservation");
                System.out.println("3. Get room number");
                System.out.println("4. update reservation");
                System.out.println("5. Delete reservation");
                System.out.println("0. Exit");
                System.out.print("choose an option:");
                int ch=sc.nextInt();
                switch(ch){
                    case 1:
                        reserveRoom(con,sc,smt);
                        break;
                    case 2:
                        viewReservation(con,smt);
                        break;
                    case 3:
                        getRoomNumber(con,sc,smt);
                        break;
                    case 4:
                        updateReservation(con,sc,smt);
                        break;
                    case 5:
                        deleteReservation(con,sc,smt);
                        break;
                    case 0:
                        exit();
                        sc.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        catch(InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    private static void reserveRoom(Connection c,Scanner sc,Statement s){
        System.out.print("Enter guest name:");
        String gname=sc.next();
        sc.nextLine();
        System.out.print("enter room no:");
        int room_no=sc.nextInt();
        System.out.print("enter contact no:");
        String c_no=sc.next();
        String sql="insert into reservation (guest_name,room_number,contact_number) values('"+gname+"',"+room_no+",'"+c_no+"')";
        try{
           int affected_rows=s.executeUpdate(sql);
           if(affected_rows>0){
               System.out.println("Reservation Successful!");
           }else{
               System.out.println("Reservation failed.");
           }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static void viewReservation(Connection c,Statement s){
        String sql="select reservation_id,guest_name,room_number,contact_number,reservation_date from reservation";
        try{
            ResultSet rs=s.executeQuery(sql);
            while(rs.next()){
                int rid=rs.getInt("reservation_id");
                String gname=rs.getString("guest_name");
                int r_no=rs.getInt("room_number");
                String c_no=rs.getString("contact_number");
                String r_date=rs.getTimestamp("reservation_date").toString();

                System.out.println("reservation id:"+rid);
                System.out.println("name:"+gname);
                System.out.println("room no:"+r_no);
                System.out.println("contact number:"+c_no);
                System.out.println("reservation date:"+r_date);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    private static void getRoomNumber(Connection c,Scanner sc,Statement s){
        try{
            System.out.print("enter reservation id:");
            int rid=sc.nextInt();
            System.out.print("enter guest name:");
            String gname=sc.next();
            sc.nextLine();

            String sql="select room_number from reservation where reservation_id="+rid+" and guest_name='"+gname+"'";

            ResultSet rs=s.executeQuery(sql);
            if(rs.next()){
                int room_no=rs.getInt("room_number");
                System.out.println("Room no for reservation id "+rid+" and guest "+gname+" is: "+room_no);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    private static void updateReservation(Connection c,Scanner sc,Statement s){
        try{
            System.out.print("enter reservation id to update:");
            int rid=sc.nextInt();
            sc.nextLine();

            if(!reservationExists(c,rid,s)){
                System.out.println("Reservation not found for the given id.");
                return;
            }
            System.out.print("Enter new guests name:");
            String ngname=sc.nextLine();
            System.out.print("enter new room no:");
            int nr_no=sc.nextInt();
            System.out.print("enter new contact number:");
            String nc_no=sc.next();

            String sql="update reservation set guest_name='"+ngname+"', room_number="+nr_no+",contact_number='"+nc_no+"' where reservation_id="+rid;
            int affected_rows=s.executeUpdate(sql);
            if(affected_rows>0)
                System.out.println("reservation update successfully!");
            else System.out.println("Reservation update failed.");

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static void deleteReservation(Connection c,Scanner sc,Statement s){
        try{
            System.out.println("enter reservation id to delete:");
            int rid=sc.nextInt();

            if(!reservationExists(c,rid,s)){
                System.out.println("reservation not found for the given id");
                return;
            }

            String sql="delete from reservation where reservation_id="+rid;

            int affected_rows=s.executeUpdate(sql);
            if(affected_rows>0)
                System.out.println("reservation deleted successfully");
            else System.out.println("reservation deletion failed");
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static boolean reservationExists(Connection c,int rid,Statement s){
        try{
            String sql="select reservation_id from reservation where reservation_id="+rid;
            ResultSet rs=s.executeQuery(sql);
            return rs.next();
        }
        catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    private static void exit() throws InterruptedException{
        System.out.print("Existing system");
        int i=5;
        while(i!=0){
            System.out.print(".");
            Thread.sleep(500);
            i--;
        }
        System.out.println();
        System.out.println("thanks for using our hotel reservation system");

    }
}