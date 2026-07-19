package models;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Journal {

    public static final String TYPE_EMPRUNT    = "emprunt";
    public static final String TYPE_RETOUR     = "retour";
    public static final String TYPE_LIVRE      = "livre";
    public static final String TYPE_LECTEUR    = "lecteur";

    public static class Action {
        public String type;
        public String message;
        public String heure;

        public Action(String type, String message) {
            this.type    = type;
            this.message = message;
            this.heure   = LocalTime.now().format(
                DateTimeFormatter.ofPattern("HH:mm"));
        }
    }

    private static final int MAX = 5;
    private static final ArrayList<Action> actions =
        new ArrayList<>();

    public static void ajouter(String type, String message) {
        actions.add(0, new Action(type, message));
        if (actions.size() > MAX)
            actions.remove(actions.size() - 1);
    }

    public static ArrayList<Action> getActions() {
        return actions;
    }

    public static void vider() {
        actions.clear();
    }
}