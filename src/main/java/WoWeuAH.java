import java.awt.dnd.DragGestureEvent;
import java.lang.reflect.Type;
import java.rmi.server.ExportException;
import java.sql.*;

import api.Blizzardapi;
import api.DatabaseWoW;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.scene.chart.PieChart;
import org.json.*;
import podatci.Auction;
import podatci.BlizzardObj;
import podatci.Item;
import podatci.Pet;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;
import java.util.Date;

@SuppressWarnings("ALL")
public class WoWeuAH {
    static Date zadnjiup = new Date();

    static int totkol=0;
    private static Connection Veza;
    
    /*public class Bazalink extends Thread{
        private Thread A;

        public void run() {
            System.out.println("Spajam se na bazu");
            try {
                Veza = spojinabazu();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                sleep(600000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        public void start(){
            if (A == null)
            {
                A = new Thread (this);
                A.start ();
            }
        }
    }
    */

    public static void bestdeals(Connection Veza, int gornja, int donja,int razlika) throws SQLException {
        String query,query1,query2 = null;
        query = "call Bpet(?,?)";
        PreparedStatement a = Veza.prepareStatement(query);
        a.setInt(1,donja);
        a.setInt(2,gornja);
        ResultSet b = a.executeQuery();
        while(b.next()){
            int id = b.getInt("id");
            query1= "call Bpetins(?)";
            PreparedStatement c =Veza.prepareStatement(query1);
             c.setInt(1,id);
            ResultSet d = c.executeQuery();
            int [] price = new int[2];
            int jid= 0,avgprice= 0,server = 0;
            String name = null, sname = null,lastseen = null;
            d.next();

            for(int i = 0 ; i < 2; i++){
                price[i]= d.getInt("MIN(d.price)");
                if ( i == 0) {
                    jid = d.getInt("id");
                    avgprice = d.getInt("avgprice");
                    lastseen = d.getString("lastseen");
                    name = d.getString("name");
                    sname = d.getString("ime");
                    server = d.getInt("server");
                }
                d.next();
            }
            if(price[0]+ razlika <= price[1]){
                query2 = "insert into newdeals (id,price,avgprice,lastseen,name,server,sname,razlika)"+" values (?,?,?,?,?,?,?,?)";
                PreparedStatement e = Veza.prepareStatement(query2);
                e.setInt(1,jid);
                e.setInt(2,price[0]);
                e.setInt(3,avgprice);
                e.setString(4,lastseen);
                e.setString(5,name);
                e.setInt(6,server);
                e.setString(7,sname);
                e.setInt(8,price[1]-price[0]);
                e.execute();
            }
        }
    }

    public static void SQLgiveitemsnames() throws SQLException, IOException {
        String query,petquery,itemquery;
        int brpromjena= 0;
        query = "select id,name,petid from item  where name is null";
        Statement statem = DatabaseWoW.Veza.createStatement();
        ResultSet a = statem.executeQuery(query);

        petquery = "update item set name = (?) where petid = (?)";
        PreparedStatement pet = DatabaseWoW.Veza.prepareStatement(petquery);

        itemquery = "update item set name = (?) where id = (?)";
        PreparedStatement itemstatement = DatabaseWoW.Veza.prepareStatement(itemquery);

        while(a.next()) {
            if(brpromjena%10==0)System.out.print("|");
            int petid = a.getInt("petid");
            if (petid != 0) {
                String link = String.format("https://eu.api.battle.net/wow/pet/species/%d?locale=en_GB&apikey=wsem5n926pw97ccb2bsundrnwct8bwjn", petid);
                String ime = GETbpetinfo(link, petid);
                pet.setString(1, ime);
                pet.setInt(2, petid);
                pet.execute();
                brpromjena++;
            }
            else{
                int id = a.getInt("id");
                String link = String.format("https://eu.api.battle.net/wow/item/%d?locale=en_GB&apikey=wsem5n926pw97ccb2bsundrnwct8bwjn", id);
                Item item = null;
                try{item = GetItem(link);}
                catch (Exception e ){
                    System.out.println("skippin adding item " + id);
                }
                if(item != null){
                    itemstatement.setString(1, item.getName());
                    itemstatement.setInt(2, id);
                    itemstatement.execute();
                    brpromjena++;
                }
            }
        }
        System.out.println("Dodano " + brpromjena +" imena");
    }

    private static Item GetItem(String link) throws IOException {
        String ime = null;
        URL url = new URL(link);
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
            public X509Certificate[] getAcceptedIssuers(){return null;}
            public void checkClientTrusted(X509Certificate[] certs, String authType){}
            public void checkServerTrusted(X509Certificate[] certs, String authType){}
        }};

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {}

        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        String input;
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        Item item = new Gson().fromJson(reader,Item.class);
        return item;
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

    public static void insertintoaukcije(Connection Veza,int imeproiz,double bid,double buy,String server,String owner,int quant, int cheaper) throws SQLException {
        String query;
        query = "insert into aukcije (item, owner,realm,bid,buyout,quantity,cheaper)"
                + " values (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement one = Veza.prepareStatement(query);
        one.setInt(1, imeproiz);
        one.setString(2, owner);
        one.setString(3, server);
        one.setDouble(4,bid);
        one.setDouble(5,buy);
        one.setInt(6,quant);
        one.setInt(7,cheaper);
        one.execute();
    }

    public static Connection spojinabazu() throws SQLException {
        String url = "jdbc:mysql://moops.ddns.net:3306/Zavrsni";
        String username = "moops";
        String password = "ge54ck32o1";

        System.out.println("Connecting database...");

        Connection con = DriverManager.getConnection(url,username,password);
        System.out.println("Database connected!");

        return con;
    }

    public static String GETbpetinfo(String urlstranice, int id) throws IOException {
        String ime = null;
        URL url = new URL(urlstranice);
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
            public X509Certificate[] getAcceptedIssuers(){return null;}
            public void checkClientTrusted(X509Certificate[] certs, String authType){}
            public void checkServerTrusted(X509Certificate[] certs, String authType){}
        }};

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {}

        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        String input;
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        Pet bpet = new Gson().fromJson(reader,Pet.class);
        return bpet.getName();
    }

    public static void Battlepet(String urlstranice, int server) throws IOException {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("213.221.93.242" , 8080));
        int kolicina = 0;
        URL url = new URL(urlstranice);
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
            public X509Certificate[] getAcceptedIssuers(){return null;}
            public void checkClientTrusted(X509Certificate[] certs, String authType){}
            public void checkServerTrusted(X509Certificate[] certs, String authType){}
        }};

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {}

        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        String input;

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        while ((input = reader.readLine()) != null)
        {
            JSONObject response = new JSONObject(new JSONTokener(input));
            JSONObject stats = (JSONObject) response.get("stats");
            if(response.opt("auctions") instanceof JSONArray)break;
            JSONObject auctions = (JSONObject) response.opt("auctions");
            String[] B = new String[50];
            for (int z = 0 ; z < 50 ; z++){
                B[z] = String.format("%d",z);
            }

            String minname= "";
            int minbuy=999999;
            int realm = 0;

            for(int i = 0; i < B.length; i++){
                String bar = String.format("%S",B[i]);
                JSONArray arr = (JSONArray) auctions.optJSONArray(bar);
                JSONObject carr = (JSONObject) stats.optJSONObject(bar);

                if(carr != null){
                    kolicina = carr.getInt("quantity");
                    totkol += kolicina;
                }

                if(arr != null) {
                    for (Object s : arr) {
                        JSONObject seller = (JSONObject) s;
                        int a = seller.getInt("buy");
                        a = a / 1000;
                        if (a < minbuy) {
                            minbuy = a;
                            minname = seller.getString("sellername");
                            realm = seller.getInt("sellerrealm");
                        }
                    }
                }
            }
            if(minbuy == 99999 ) minbuy = 0;
            switch (server){
                case 467:  System.out.println("MINIMALNI BUY JE = " + minbuy/10 + " SELLER JE " + minname + " server je Stormscale id je " + realm );break;
                case 188: System.out.println("Draenor(" + server + ") -  " + minbuy/10 + " - " + minname + " - " + kolicina);break;
                case 215: System.out.println("Kazzak (" + server + ") -  " + minbuy/10 + " - " + minname + " - " + kolicina);break;
                case 228: System.out.println("Outland (" + server + ") -  " + minbuy/10 + " - " + minname + " - " + kolicina);break;
                case 141: System.out.println("Argent Dawn (" + server + ") -  " + minbuy/10 + " - " + minname + " - " + kolicina);break;
                case 203: System.out.println("Frost Mane(" + server+ ") -  " + minbuy/10 + " - " + minname + " - " + kolicina);break;
                case 231: System.out.println("Ragnaros(" + server + ") -  " + minbuy/10 + " - " + minname + " - " + kolicina );break;
                case 241: System.out.println("Twisting Nether(" + server + ") -  " + minbuy/10 + " - " + minname + " - " + kolicina);break;
                case 237: System.out.println("Sylvanas(" + server + ") -  " + minbuy/10 + " - " + minname + " - " + kolicina);break;
                case 180: System.out.println("Dentarg(" + server + ") -  " + minbuy/10 + " - " + minname + " - " + kolicina);break;
                case 236: System.out.println("Stormscale(" + server + ") -  " + minbuy/10 + " - " + minname + " - " + kolicina);break;
                case 232: System.out.println("Ravencrest(" + server + ") -  " + minbuy/10 + " - " + minname + " - " + kolicina);break;
                default:  System.out.println("MINIMALNI BUY JE = " + minbuy/10 + " SELLER JE " + minname + " server je " + realm );
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException, SQLException {
        DatabaseWoW.connect();
        Calendar cal = Calendar.getInstance();
        cal.setTime(zadnjiup);
        System.out.println("zadnji update- " + " " + cal.getTime());
       /*  WoWeuAH B1 = new WoWeuAH();
        WoWeuAH.Bazalink C1 = B1.new Bazalink();
        C1.start();*/
        int[] serveri = {188,241,203,180,215,231,232,236,237,141,228};
        int a = 0;
        System.out.println("WoW Economy SERVER 1.0");
        do{Scanner scan = new Scanner(System.in);
        if(a!=0)System.out.println("\n");
        System.out.println("MENU\n1)Seacalf\n2)Landshark\n3)InsBpet\n4)Battlepetlist(Sql)\n5)Blizzard auctions\n6)SQL - Auctions Blizzard \n7)SQL - Give null items names\n8)SQL-Bestdeals8 \nOption: " );
        int unos = scan.nextInt();
        switch(unos){
            case 1: TUJgetcalf();break;
            case 2: TUJgetshark();break;
            case 3:  System.out.println("Unesi Pet ID: ");
                      int scaned = scan.nextInt();  Tujgetbpet(scaned);break;
            case 4:
                System.out.println("TUJ bpetovi se učitavaju");
                Statement brisi = Veza.createStatement();
                brisi.execute("TRUNCATE deals;");
                for(int i = 0; i < serveri.length;i++){
                    String link = String.format("https://theunderminejournal.com/api/category.php?house=%d&id=battlepets", serveri[i]);
                    WoWeuAH a1 = new WoWeuAH();
                    WoWeuAH.Tujapi a2 = a1.new Tujapi(link,serveri[i]);
                    a2.start();
                }
                break;
            case 5: /*WoWeuAH A = new WoWeuAH();
                    WoWeuAH.Blizzardapi Draenor = A.new Blizzardapi("https://eu.api.battle.net/wow/auction/data/Draenor?locale=en_GB&apikey=4qww8wggu7g4cfgfzjqqwenev3ymcg6e",zadnjiup,Veza);
                    Draenor.start();*/
                    Blizzardapi Draenor = new Blizzardapi("https://eu.api.battle.net/wow/auction/data/Draenor?locale=en_GB&apikey=6sq5hjtpsrr8erc9h42h32xupqqa3hww",zadnjiup,"Draenor");
                    Draenor.start();

                    break;
            case 6:
                   //Statement del = Veza.createStatement();
                    //del.execute("TRUNCATE aukcije;");
                String [] b = {"Draenor","Dentarg","Kazzak","Ragnaros","Stormscale","Twisting-Nether","Argent-Dawn","Frostmane","Outland","Ravencrest","Sylvanas"};
                    for(int i = 0 ; i < b.length; i++){
                         String input=String.format("https://eu.api.battle.net/wow/auction/data/%s?locale=en_GB&apikey=6sq5hjtpsrr8erc9h42h32xupqqa3hww",b[i]);
                        Blizzardapi Serverx= new Blizzardapi(input,zadnjiup,b[i]);
                        Serverx.start();
                    }
                    break;
            case 7:
                    SQLgiveitemsnames();
                    break;
            case 8:
                Statement del1 = Veza.createStatement();
                del1.execute("TRUNCATE newdeals;");
                System.out.println("Unesite Gornju granicu: ");
                int prvi = scan.nextInt();
                System.out.println("Unesite Donju granicu: ");
                int drugi = scan.nextInt();
                System.out.println("Unesite Razliku: ");
                int treci = scan.nextInt();
                bestdeals(Veza,prvi,drugi,treci);
                break;
            case 9:
                System.out.println("finding cheapest");
                try {
                    DatabaseWoW.findcheapest();
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            default: System.out.println("krivi unos"); a++;
        }}while(true);
    }

    public static void TUJgetcalf() throws IOException {
        totkol = 0;
        int[] b = {188,241,203,180,215,231,232,236,237,141,228};
        String poruka = String.format("SEA CALF ON %d DIFFERENT SERVERS",b.length);
        System.out.println(poruka);
        for(int i = 0 ; i < b.length;i++) {
            String temp = String.format("https://theunderminejournal.com/api/battlepet.php?house=%d&species=1448", b[i]);
            Battlepet(temp,b[i]);
        }
        System.out.println("Ukupna kolicina je - " + totkol );
    }

    public static void TUJgetshark() throws IOException {
        totkol = 0;
        int[] b = {188,241,203,180,215,231,232,236,237,141,228};
        String poruka = String.format("LAND SHARK ON %d DIFFERENT SERVERS",b.length);
        System.out.println(poruka);
        for(int i = 0 ; i < b.length;i++) {
            String temp = String.format("https://theunderminejournal.com/api/battlepet.php?house=%d&species=115", b[i]);
            Battlepet(temp,b[i]);
        }
        System.out.println("Ukupna kolicina je - " + totkol );
    }

    public static void Tujgetbpet(int petid /*pet id from tuj link*/) throws IOException  {
        totkol = 0;
        int[] b = {141,188,215,228,236,241,237,180,232,203};
        String poruka = String.format("%d on %d DIFFERENT SERVERS",petid,b.length);
        System.out.println(poruka);
        for(int i = 0 ; i < b.length;i++) {
            String temp = String.format("https://theunderminejournal.com/api/battlepet.php?house=%d&species=%d", b[i],petid);
            Battlepet(temp,b[i]);
        }
        System.out.println("Ukupna kolicina je - " + totkol );
    }

    public static void TUJallbpets(String unesi, Connection Veza, int Razlika) throws IOException, SQLException {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("213.221.93.242" , 8080));
        int kolicina = 0;

        String brojservera = new String();
        brojservera = unesi;
        String brojservera1 = brojservera.substring(brojservera.indexOf("house=")+6,brojservera.indexOf("house=")+9);
        int idservera = Integer.parseInt(brojservera1);

        URL url = new URL(unesi);
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
            public X509Certificate[] getAcceptedIssuers(){return null;}
            public void checkClientTrusted(X509Certificate[] certs, String authType){}
            public void checkServerTrusted(X509Certificate[] certs, String authType){}
        }};

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {}

        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        String input;

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        while ((input = reader.readLine()) != null)
        {
            JSONObject response = new JSONObject(new JSONTokener(input));
            JSONArray reponse2 = (JSONArray) response.get("results");
            JSONObject response3 = reponse2.getJSONObject(0);
            JSONObject podatci = response3.getJSONObject("data");

            for (int i = 0; i < 10; i++){
                String a=String.format("%d",i);
                JSONObject podatci1 = podatci.getJSONObject(a);
                Iterator iterator  = podatci1.keys();
                String key = null;
                while(iterator.hasNext()){
                    key = (String)iterator.next();
                    if((JSONObject)podatci1.opt(key) == null)break;
                    JSONObject podatci2 = (JSONObject) podatci1.opt(key);
                    String species = podatci2.optString("species");
                    if(species == ""){
                        Iterator iterator1 = podatci2.keys();
                            String key1;
                            while(iterator1.hasNext()){
                                key1 = (String)iterator1.next();
                                if(podatci2.opt(key1) instanceof JSONArray)break;
                                JSONObject podatci3 = (JSONObject) podatci2.get(key1);
                                String icon = (String) podatci3.opt("icon");
                                if(icon != "" && icon != null){
                                    int price = podatci3.optInt("price")/10000;
                                    int avgprice = podatci3.optInt("avgprice")/10000;
                                    int minlocal;
                                    int imeproiz = podatci3.optInt("species");
                                    String Lastseen1 = podatci3.optString("lastseen");
                                    String ime = podatci3.optString("name_enus");
                                    insertintodeals(Veza,imeproiz,price,avgprice,ime,Lastseen1,idservera);
                                }

                                if(icon == null){
                                    Iterator iterator2 = podatci3.keys();
                                    String key2;
                                    while(iterator2.hasNext()){
                                        key2 = (String)iterator2.next();
                                        if(podatci3.opt(key2) instanceof JSONArray)break;
                                        JSONObject podatci4 = (JSONObject) podatci3.get(key2);
                                        String icon2 = (String) podatci4.opt("icon");
                                        if(icon2 != "" && icon2 != null){
                                            int price = podatci4.optInt("price")/10000;
                                            int avgprice = podatci4.optInt("avgprice")/10000;
                                            int minlocal;
                                            int imeproiz = podatci4.optInt("species");
                                            String ime = podatci4.optString("name_enus");
                                            String Lastseen = podatci4.optString("lastseen");
                                            insertintodeals(Veza,imeproiz,price,avgprice,ime,Lastseen,idservera);
                                        }
                                    }
                                }
                        }
                    }
                }
            }
        }
    }

   /* public class Blizzardapi extends Thread{
        private Thread T;
        JSONArray aukcije;
        Date update;
        String unos;
        Date Zadnjiup1;
        Connection Veza1;
        JSONArray aukcije1;
        String JsonString;

        Blizzardapi(String unesi, Date zadnjiup, Connection Veza) throws IOException {
            Veza1 = Veza;
            unos = unesi;
            Zadnjiup1 = zadnjiup;
        }

        public int getnumofauc(int item,double price){
            int count = 0;
            for(Object s : aukcije1){
                JSONObject ono = (JSONObject) s;
                int item1 = ono.getInt("item");
                if(item1 == 82800){
                    int btype = ono.getInt("petSpeciesId");
                    if(btype==item && ono.getInt("buyout") < (int)price )count++;
                }
            }
            return count;
        }

        public Date getallauctions() throws SQLException, IOException {
            if(aukcije == null) return update;
            System.out.println("novi update- " + update+ "-" + unos);
            String ime = new String();
            ime = "Directive";
            String jsonS = aukcije.toString();
            Type tip = new TypeToken<List<Auction>>(){}.getType();
            List<Auction> povratna=new Gson().fromJson(jsonS,tip);

            for (Object a: aukcije) {
                JSONObject aukcija =(JSONObject) a;
                String naziv = aukcija.getString("owner");
                String realm = aukcija.getString("ownerRealm");
                String usporedba;
                usporedba = null;
                if(realm.equals("Draenor"))usporedba = "Directive";
                if(realm.equals("Dentarg"))usporedba = "Gudgai";
                if(realm.equals("Kazzak"))usporedba = "Ilubyu";
                if(realm.equals("Ragnaros"))usporedba = "Crocopz";
                if(realm.equals("Stormscale"))usporedba = "Ripednsxy";
                if(realm.equals("TwistingNether"))usporedba = "Gitgudington";
                if(realm.equals("ArgentDawn"))usporedba = "Sealaly";
                if(realm.equals("Frostmane"))usporedba = "Dzabaskill";
                if(realm.equals("Outland"))usporedba = "Clcuko";
                if(realm.equals("Ravencrest"))usporedba = "Memlord";
                if(realm.equals("Sylvanas"))usporedba = "Suchprice";

                if(naziv.equals(usporedba)){
                    if(aukcija.getInt("item") == 82800){
                        int b = aukcija.getInt("petSpeciesId");
                        double price = aukcija.getDouble("buyout");
                        int c = getnumofauc(b,price);
                        insertintoauctions(Veza1, aukcija.getInt("petSpeciesId"), aukcija.getDouble("bid")/10000,
                                aukcija.getDouble("buyout")/10000, aukcija.getString("ownerRealm"), aukcija.getString("owner"), aukcija.getInt("quantity"),c);
                    }
                   else {
                        insertintoauctions(Veza1, aukcija.getInt("item"), aukcija.getDouble("bid")/10000, aukcija.getDouble("buyout")/10000, aukcija.getString("ownerRealm"),
                                aukcija.getString("owner"), aukcija.getInt("quantity"),0);
                    }
                }
            }
            return update;
        }
        public HttpURLConnection spoji(String url) throws IOException {
            URL veza = new URL(url);
            String ime = veza.toString();

            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
                public X509Certificate[] getAcceptedIssuers(){return null;}
                public void checkClientTrusted(X509Certificate[] certs, String authType){}
                public void checkServerTrusted(X509Certificate[] certs, String authType){}
            }};

            try {
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, trustAllCerts, new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            } catch (Exception e) {}
            HttpsURLConnection veza1= (HttpsURLConnection) veza.openConnection();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(veza1.getInputStream()));
                JSONObject prvi = new JSONObject(new JSONTokener(reader));
                JSONArray drugi = prvi.getJSONArray("files");
                JSONObject treci = drugi.getJSONObject(0);
                URL veza2 = new URL((String) treci.get("url"));
                update = new Date(treci.getLong("lastModified"));
                if (!update.equals(Zadnjiup1)) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(update);
                }
                return (HttpURLConnection) veza2.openConnection();
            } catch(IOException f) {System.out.println("Error: " + f.getMessage() + veza);}
            return null;
        }
        public JSONArray parsejson(HttpURLConnection unos, Date zadnjiup) throws IOException {
            if (update.equals(zadnjiup)) {
                return null;
                }
            BufferedReader reader = new BufferedReader(new InputStreamReader(unos.getInputStream()));
            JSONObject one = new JSONObject(new JSONTokener(reader));
            JSONArray two = one.getJSONArray("auctions");
            return  two;
        }
        public void run(){
            System.out.println("Running " +  unos );
            while(true) {
                try {
                    HttpURLConnection jedan = this.spoji(unos);
                    aukcije = this.parsejson(jedan,Zadnjiup1);
                    aukcije1 = aukcije;
                    if(aukcije != null ) {
                        this.getallauctions();
                        Zadnjiup1 = update;
                        sleep(600000);
                    }
                    sleep(60000);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        public void start(){
            System.out.println("Starting " + unos  );
            if (T == null)
            {
                T = new Thread(this,unos);
                T.start ();
            }
        }
    }
    */

    public class Tujapi extends Thread{

        private Thread T;
        String unos;
        int server1;

        Tujapi(String unesi,int server) throws IOException {
            unos = unesi;
            server1= server;
        }

        public void GetAllBpets(String unesi, Connection Veza) throws IOException, SQLException {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("213.221.93.242" , 8080));
            int kolicina = 0;

            String brojservera = new String();
            brojservera = unesi;
            String brojservera1 = brojservera.substring(brojservera.indexOf("house=")+6,brojservera.indexOf("house=")+9);
            int idservera = Integer.parseInt(brojservera1);

            URL url = new URL(unesi);
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
                public X509Certificate[] getAcceptedIssuers(){return null;}
                public void checkClientTrusted(X509Certificate[] certs, String authType){}
                public void checkServerTrusted(X509Certificate[] certs, String authType){}
            }};

            try {
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, trustAllCerts, new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            } catch (Exception e) {}

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            String input;

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            while ((input = reader.readLine()) != null)
            {
                JSONObject response = new JSONObject(new JSONTokener(input));
                JSONArray reponse2 = (JSONArray) response.get("results");
                JSONObject response3 = reponse2.getJSONObject(0);
                JSONObject podatci = response3.getJSONObject("data");

                for (int i = 0; i < 10; i++){
                    String a=String.format("%d",i);
                    JSONObject podatci1 = podatci.getJSONObject(a);
                    Iterator iterator  = podatci1.keys();
                    String key = null;
                    while(iterator.hasNext()){
                        key = (String)iterator.next();
                        if((JSONObject)podatci1.opt(key) == null)break;
                        JSONObject podatci2 = (JSONObject) podatci1.opt(key);
                        String species = podatci2.optString("species");
                        if(species == ""){
                            Iterator iterator1 = podatci2.keys();
                            String key1;
                            while(iterator1.hasNext()){
                                key1 = (String)iterator1.next();
                                if(podatci2.opt(key1) instanceof JSONArray)break;
                                JSONObject podatci3 = (JSONObject) podatci2.get(key1);
                                String icon = (String) podatci3.opt("icon");
                                if(icon != "" && icon != null){
                                    int price = podatci3.optInt("price")/10000;
                                    int avgprice = podatci3.optInt("avgprice")/10000;
                                    int minlocal;
                                    int imeproiz = podatci3.optInt("species");
                                    String Lastseen1 = podatci3.optString("lastseen");
                                    String ime = podatci3.optString("name_enus");
                                    insertintodeals(Veza,imeproiz,price,avgprice,ime,Lastseen1,idservera);
                                }

                                if(icon == null){
                                    Iterator iterator2 = podatci3.keys();
                                    String key2;
                                    while(iterator2.hasNext()){
                                        key2 = (String)iterator2.next();
                                        if(podatci3.opt(key2) instanceof JSONArray)break;
                                        JSONObject podatci4 = (JSONObject) podatci3.get(key2);
                                        String icon2 = (String) podatci4.opt("icon");
                                        if(icon2 != "" && icon2 != null){
                                            int price = podatci4.optInt("price")/10000;
                                            int avgprice = podatci4.optInt("avgprice")/10000;
                                            int minlocal;
                                            int imeproiz = podatci4.optInt("species");
                                            String ime = podatci4.optString("name_enus");
                                            String Lastseen = podatci4.optString("lastseen");
                                            insertintodeals(Veza,imeproiz,price,avgprice,ime,Lastseen,idservera);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        public void start(){
            System.out.println("Starting " + unos  );
            if (T == null)
            {
                T = new Thread(this,unos);
                T.start ();
            }
        }
        public void run(){
            while(true) {
                try {
                    GetAllBpets(unos, Veza);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                System.out.println("¤" + server1 + " je obrađen");

                try {
                    sleep(600000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }




    }


}






