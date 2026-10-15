package dao;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Admin
 */
public class Connexion {

    private static Connection conn = null;
    private static Properties config = null;
    private static final String FICHIER_CONFIG = "config.properties";

    // Dossiers où on cherche config.properties, dans l'ordre :
    // 1) à côté du jar (ou du dossier des classes en développement)
    // 2) dans le dossier de travail (racine du projet sous NetBeans)
    private static List<Path> emplacementsPossibles() {
        List<Path> liste = new ArrayList<>();
        try {
            Path code = Paths.get(Connexion.class
                    .getProtectionDomain().getCodeSource().getLocation().toURI());
            Path dossier = Files.isDirectory(code) ? code : code.getParent();
            if (dossier != null) {
                liste.add(dossier.resolve(FICHIER_CONFIG));
            }
        } catch (URISyntaxException | RuntimeException e) {
            // on ignore : on essaiera le dossier de travail
        }
        liste.add(Paths.get(System.getProperty("user.dir"), FICHIER_CONFIG));
        return liste;
    }

    // Lit la configuration une seule fois, puis la garde en mémoire
    private static Properties chargerConfig() {
        if (config != null) {
            return config;
        }

        Properties p = new Properties();
        // Valeurs par défaut = ta configuration de développement actuelle
        p.setProperty("db.host", "localhost");
        p.setProperty("db.port", "3306");
        p.setProperty("db.name", "biblioapp");
        p.setProperty("db.user", "root");
        p.setProperty("db.password", "");

        boolean trouve = false;
        for (Path candidat : emplacementsPossibles()) {
            if (Files.isRegularFile(candidat)) {
                try (Reader r = new InputStreamReader(
                        Files.newInputStream(candidat), StandardCharsets.UTF_8)) {
                    p.load(r);
                    System.out.println("Configuration lue : " + candidat);
                    trouve = true;
                    break;
                } catch (Exception e) {
                    System.out.println("Impossible de lire " + candidat + " : " + e.getMessage());
                }
            }
        }
        if (!trouve) {
            System.out.println("Aucun " + FICHIER_CONFIG + " trouvé : valeurs par défaut (développement)");
        }

        config = p;
        return config;
    }

    public static Connection getConnexion() throws Exception {
        try {
            Properties cfg = chargerConfig();

            // CHANGEMENT : jdbc:mariadb:// (nouveau driver), et plus de ?useSSL=false
            String url = "jdbc:mariadb://" + cfg.getProperty("db.host").trim()
                    + ":" + cfg.getProperty("db.port").trim()
                    + "/" + cfg.getProperty("db.name").trim();
            String user = cfg.getProperty("db.user").trim();
            String password = cfg.getProperty("db.password"); // pas de trim sur le mot de passe

            System.out.println("Connexion à : " + url + " (utilisateur " + user + ")");
            conn = DriverManager.getConnection(url, user, password);

            System.out.println("Bien réussie ");
        } catch (Exception e) {
            System.out.println("Erreur connexion : " + e);
            e.printStackTrace(); // trace complète pour trouver la vraie cause
        }
        return conn;
    }

    public static void main(String[] args) throws Exception {
        Connexion.getConnexion();
    }
}