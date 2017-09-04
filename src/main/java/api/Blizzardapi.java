package api;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import podatci.BlizzardObj;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import static api.DatabaseWoW.GetLastUpdate;
import static api.DatabaseWoW.insertintoauctions;


/**
 * Created by stjep on 7/19/2017.
 */
public class Blizzardapi extends Thread{
    private Thread T;
    JSONArray aukcije;
    java.sql.Timestamp update,now,newupdate;
    String unos;
    Date Zadnjiup1;
    JSONArray aukcije1;
    BlizzardObj Objekt;
    String ServerName;
    int ntimes = 0;


    public Blizzardapi(String unesi, Date zadnjiup,String serverName) throws IOException {
        unos = unesi;
        Zadnjiup1 = zadnjiup;
        ServerName =serverName;

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

            /*if(naziv.equals(usporedba)){
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
            }*/
        }
        return update;
    }
    public HttpURLConnection spoji(String url) throws IOException {
       // System.out.println(new Timestamp(System.currentTimeMillis())+": "+this.ServerName+" - Connecting " + url);
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
            update = new Timestamp(treci.getLong("lastModified"));
            if (!update.equals(Zadnjiup1)) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(update);
            }
            return (HttpURLConnection) veza2.openConnection();
        } catch(IOException f) {System.out.println("Error: " + f.getMessage() + veza);}
        return null;
    }
    public BlizzardObj parsejson(HttpURLConnection unos, Date zadnjiup) throws IOException, InterruptedException {
        System.out.println(new Timestamp(System.currentTimeMillis())+": "+this.ServerName+" - Parsing ");
        if (update.equals(zadnjiup)) {
            ntimes++;
            if(ntimes>1){
                System.out.println(new Timestamp(System.currentTimeMillis())+": "+this.ServerName+" - Blizzard server is not updating at regular times checking every 5 mins ");
                sleep(300000);
            }
            return null;
        }
        ntimes= 0;
        System.out.println(new Timestamp(System.currentTimeMillis())+": "+this.ServerName+" - New update " + update + " / "+ "Last update " + zadnjiup);
        BufferedReader reader = new BufferedReader(new InputStreamReader(unos.getInputStream()));
        /* Type tip = new TypeToken<List<Auction>>(){}.getType();*/
        BlizzardObj povratna=new Gson().fromJson(reader, BlizzardObj.class);
        if(!povratna.auctions.isEmpty()) {
            povratna.realm = povratna.auctions.get(0).ownerRealm;
            povratna.timestamp = new Timestamp(update.getTime());
        }
        System.out.println(new Timestamp(System.currentTimeMillis())+": "+this.ServerName+" - Finished Parsing");
        return  povratna;
    }


    public void run(){
        System.out.println(new Timestamp(System.currentTimeMillis())+": "+this.ServerName+" - Running " +  unos );
        try {
            Zadnjiup1 = GetLastUpdate(ServerName,DatabaseWoW.Veza);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while(true) {
            int minsleft;
            try {
                HttpURLConnection jedan = this.spoji(unos);
                if(Zadnjiup1 == null){
                    now = new Timestamp(System.currentTimeMillis());
                    newupdate = new Timestamp(now.getTime()-20000);
                }
                else{
                newupdate = new Timestamp(Zadnjiup1.getTime()+ 40*60*1000);
                now = new Timestamp(System.currentTimeMillis());
                }
                if(now.getTime()>newupdate.getTime()) {
                    Objekt = parsejson(jedan, Zadnjiup1);
                    if (Objekt != null) {
                        Zadnjiup1 = insertintoauctions(DatabaseWoW.Veza, Objekt);
                    }
                }
                else{
                    long timeToSleep = newupdate.getTime() - now.getTime();

                    long timeonepercent = (long)(timeToSleep*0.1);
                    long timelastpercent = (long)((timeToSleep*0.1)%1*10);
                    System.out.println(new Timestamp(System.currentTimeMillis())+": "+this.ServerName + " Waiting for update at "+ newupdate);
                    for( double progress = 0.0; progress< 1.0; progress+=0.1){
                        minsleft = (int)((1-progress)*10*timeonepercent)/(1000*60);
                        if(minsleft == 0){
                            System.out.println(new Timestamp(System.currentTimeMillis())+ ": "+this.ServerName + " - "+ "Update in "+ (int)(((1-progress)*10*timeonepercent)/(1000))+"seconds");
                        }
                        else {
                            System.out.println(new Timestamp(System.currentTimeMillis()) + ": " + this.ServerName + " - " + "Update in " + (int) (((1 - progress) * 10 * timeonepercent) / (1000 * 60)) + " minutes");
                        }
                        Thread.sleep(timeonepercent);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                try {
                    DatabaseWoW.connect();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
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