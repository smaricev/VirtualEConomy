import podatci.Auction;
import podatci.AukcijeEntity;
import podatci.ProfitEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.List;

import static spark.Spark.*;

public class ResTAPI {
    public static void main(String[] args) {
        EntityManagerFactory sessionFactory = Persistence.createEntityManagerFactory("hibernate.cfg.xml");
        port(80);
        get("/hello", (req, res) -> {
            return "hello world";

        });
    }
}
