package api;

import podatci.Auction;
import podatci.BlizzardObj;

import java.sql.*;


public final class DatabaseWoW extends Thread{
    public static Connection Veza = null;
    private Thread A;
    private int a;


    public static Connection connect() throws SQLException {
        String myConnectionString =
                "jdbc:mysql://marichely.me:3306/WoW_Auction?" +
                        "useUnicode=true&characterEncoding=UTF-8" +
                        "&rewriteBatchedStatements=true"
                        +"&autoreconnect=true";
        String username = "moops";
        String password = "ge54ck32o1";

        if(Veza!=null){
            System.out.println("Returning existing connection");
            return Veza;
        };
        System.out.println("Connecting database...");
        Veza = DriverManager.getConnection(myConnectionString,username,password);
        System.out.println("Database connected!");
        return Veza;
    }

    private DatabaseWoW() throws SQLException {
        Veza = connect();
    }


    public static void insertintodeals(Connection Veza, int imeproiz, int price, int avgprice, String ime, String lastseen, int server) throws SQLException {

        String query;
        if (ime!= null && ime != "") {
            // the mysql insert statement
            query = " insert into deals (id, price, avgprice,lastseen,server,name)"
                    + " values (?, ?, ?, ?, ?, ?)";

        }
        else {
            query = " insert into deals (id , price, avgprice, lastseen,server)"
                    + " values (?, ?, ?, ?, ?)";
        }
        // create the mysql insert preparedstatement
        PreparedStatement preparedStmt = Veza.prepareStatement(query);
        if (ime!= null && ime != "") preparedStmt.setString(6,ime);
        preparedStmt.setInt(1, imeproiz);
        preparedStmt.setInt(2, price);
        preparedStmt.setInt(3, avgprice);
        preparedStmt.setString(4, lastseen);
        preparedStmt.setInt(5, server);
        // execute the preparedstatement
        preparedStmt.execute();
    }


    public static Timestamp GetLastUpdate(String server, Connection Conn) throws SQLException {
        Timestamp lastUpdate = null;
        String query = "Select updatetime from lastupdate where server = ? ORDER BY updatetime DESC ";
        PreparedStatement prep = Conn.prepareStatement(query);
        prep.setString(1,server);
        ResultSet result = prep.executeQuery();
        if(!result.next())return null;
        lastUpdate = result.getTimestamp("updatetime");
        return lastUpdate;
    }

    // Creates deals table from available auction data


    public static void findcheapest() throws SQLException, InterruptedException {
            synchronized (DatabaseWoW.class) {
                System.out.println(new Timestamp(System.currentTimeMillis())+"- Finding cheapest");
                String query = "CALL allpetids";
                Statement one = Veza.createStatement();
                one.execute("delete from auctions where date < NOW() - INTERVAL 2 hour");
                ResultSet rs = one.executeQuery(query);
                String query3 = "INSERT INTO profit (auction,minprofit,midprofit,maxprofit,date,realm) VALUES (?,?,?,?,now(),?)";
                PreparedStatement profitprepared = Veza.prepareStatement(query3);

                while (rs.next()) {
                    int petid = rs.getInt(1);
                    String query2 = "CALL cheapest_pet_per_realm(?,null)";
                    PreparedStatement two = Veza.prepareStatement(query2);

                    int numrows = 0;
                    double minprofit = 0, midprofit = 0, maxprofit = 0, buyout = 0,auction = 0;
                    String realm="";
                    two.setInt(1, petid);
                    ResultSet three = two.executeQuery(); // three - cheapest pet per server resultset

                    if (three.last()) {
                        numrows = three.getRow();
                        three.beforeFirst();
                    }
                    for (int i = 1; three.next(); i++) {
                        if (i == 1) {
                            auction = three.getDouble("auc");
                            buyout = three.getDouble("buyout");
                            realm = three.getString("realm");
                        }
                        if (i == 2) {
                            minprofit = three.getDouble("buyout") - buyout;
                        }
                        if (i == numrows / 2) {
                            midprofit = three.getDouble("buyout") - buyout;
                        }
                        if (i == numrows) {
                            maxprofit = three.getDouble("buyout") - buyout;
                        }
                    }

                    profitprepared.setDouble(1, auction);
                    profitprepared.setDouble(2, minprofit);
                    profitprepared.setDouble(3, midprofit);
                    profitprepared.setDouble(4, maxprofit);
                    profitprepared.setString(5,realm);
                    profitprepared.addBatch();

                }
                profitprepared.executeBatch();
                Statement a = Veza.createStatement();
                a.execute("Call bestdeals");
                System.out.println(new Timestamp(System.currentTimeMillis())+"- Done finding cheapest");


            }
    }

    public static Timestamp insertintoauctions(Connection Veza, BlizzardObj serverObjekt) throws SQLException, InterruptedException {
        String query1 = "Delete from auctions where date < ? and  realm = ?";
        PreparedStatement two = Veza.prepareStatement(query1);
        if(serverObjekt.realm.equals("Tarren Mill"))serverObjekt.realm = "Dentarg";
        two.setString(2,serverObjekt.realm);
        two.setTimestamp(1,serverObjekt.timestamp);
        two.execute();
        System.out.println(new Timestamp(System.currentTimeMillis())+": " + serverObjekt.realm + " - DELETED: " + two.getUpdateCount()+" old records");


        System.out.println(new Timestamp(System.currentTimeMillis())+": "+serverObjekt.realm+" - Inserting into DB");
        String query;
        query = "insert into auctions (item, owner,realm,bid,buyout,quantity,auc,date,petBreedId,petSpeciesId,petQualityId,petLevel)"
                + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement one = Veza.prepareStatement(query);
        for ( Auction aukcija: serverObjekt.auctions ) {
            one.setInt(1, aukcija.item.intValue());
            one.setString(2, aukcija.owner);
            one.setString(3, aukcija.ownerRealm);
            one.setDouble(4,aukcija.bid.doubleValue());
            one.setDouble(5,aukcija.buyout.doubleValue());
            one.setInt(6,aukcija.quantity.intValue());
            one.setDouble(7,aukcija.auc.doubleValue());
            one.setTimestamp(8,serverObjekt.timestamp);
            if(aukcija.item.intValue()==82800) {
                one.setInt(9, aukcija.petBreedId.intValue());
                one.setInt(10, aukcija.petSpeciesId.intValue());
                one.setInt(11, aukcija.petQualityId.intValue());
                one.setInt(12, aukcija.petLevel.intValue());
            }
            else{
                one.setNull(9, Types.INTEGER);
                one.setNull(10, Types.INTEGER);
                one.setNull(11, Types.INTEGER);
                one.setNull(12, Types.INTEGER);
            }
            one.addBatch();
        }
        one.executeBatch();
        System.out.println(new Timestamp(System.currentTimeMillis())+": "+ serverObjekt.realm +" - Done inserting");
        query = "insert into lastupdate ( server, updatetime) VALUES (?,?)";
        one = Veza.prepareStatement(query);
        one.setString(1,serverObjekt.realm);
        one.setTimestamp(2,serverObjekt.timestamp);
        one.execute();
        Timestamp vrati = serverObjekt.timestamp;
        serverObjekt = null;
        Runnable zadaca  = () -> {
            try {
                findcheapest();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } ;
        Thread a = new Thread(zadaca);
        a.start();

        return vrati;
    }

}