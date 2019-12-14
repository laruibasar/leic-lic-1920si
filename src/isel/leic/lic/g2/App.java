package isel.leic.lic.g2;

import isel.leic.lic.g2.Door.DoorMechanism;
import isel.leic.lic.g2.Log.Log;
import isel.leic.lic.g2.TUI.TUI;
import isel.leic.lic.g2.User.User;
import isel.leic.lic.g2.User.Users;
import isel.leic.utils.Time;

public class App {
    private static boolean maintenance_mode;
    private static boolean run;

    private static final String USER = "UIN:";
    private static final int USER_DIGITS = 3;
    private static final char USER_OBS = '?';
    private static final String PIN = "PIN:";
    private static final int PIN_DIGITS = 4;
    private static final char PIN_OBS = '*';
    private static final long TIMEOUT = 5000;

    public static void init() {
        Log.init();
        Users.init();
        TUI.init();
        DoorMechanism.init();
        M.init();
        Maintenance.init();

        maintenance_mode = M.isMaintenanceMode();
        run = true;
    }

    public static void main(String[] args) {
        init();
        TUI.clearScreen();

        while (run) {
            TUI.showCurrentDateTime(0);
            getAccessUser();

            if (maintenance_mode) {
                Maintenance.enterMaintenance();
                TUI.clearLine(1);
            }
            maintenance_mode = M.isMaintenanceMode();
            run = !Maintenance.endApp();
        }

        saveAppState();
        System.exit(0);
    }

    private static void saveAppState() {
        TUI.showCenterMessage("Shutdown...", 1);
        Users.saveUsers();
        Time.sleep(TIMEOUT);
        TUI.clearScreen();
    }

    private static void getAccessUser() {
        User u;

        int uin = TUI.readInput(USER, USER_DIGITS, true, USER_OBS, TIMEOUT, 1);
        if (uin < 0)
            return;

        int pin = TUI.readInput(PIN, PIN_DIGITS, true, PIN_OBS, TIMEOUT, 1);
        u = Users.searchUser(uin);

        if (u == null)
            return;

        if (u.checkPin(pin))
            authenticatedMode(u);
        else {
            TUI.clearLine(1);
            TUI.showCenterMessage("Login Failed", 1);
            Time.sleep(TIMEOUT);
        }
        TUI.clearLine(1);
    }

    private static void authenticatedMode(User user) {
        long time = Time.getTimeInMillis() + TIMEOUT;
        char key = 0;
        Log.logger(user.toString());

        TUI.clearScreen();
        TUI.showCenterMessage(user.getMessage(), 0);
        TUI.showCenterMessage(user.getName(), 1);

        while (Time.getTimeInMillis() < time && key != '#' && key != '*')
            key = TUI.readInputKeyboard();

        if (key == '#')
            if (askChangePin())
                changePin(user);

        TUI.clearScreen();
        TUI.showCenterMessage(user.getName(), 0);
        DoorMechanism.open(1);
        TUI.showCenterMessage("Door opened", 1);
        Time.sleep(TIMEOUT);
        TUI.showCenterMessage("Closing door...", 1);
        DoorMechanism.close(1);
    }

    private static boolean askChangePin() {
        long time = Time.getTimeInMillis() + TIMEOUT;
        char key = 0;

        TUI.clearScreen();
        TUI.showCenterMessage("Change PIN?", 0);
        TUI.showCenterMessage("(Yes=*)", 1);

        while (Time.getTimeInMillis() < time && key == 0) {
            key = TUI.readInputKeyboard();
        }

        if (key == '*')
            return true;
        else
            return false;
    }

    private static void changePin(User user) {
        TUI.clearScreen();
        TUI.showCenterMessage("Insert New", 0);
        int newPin = TUI.readInput(PIN, PIN_DIGITS, true, PIN_OBS, TIMEOUT, 1);
        TUI.showCenterMessage("Re-insert New", 0);
        int confirmPin = TUI.readInput(PIN, PIN_DIGITS, true, PIN_OBS, TIMEOUT, 1);

        TUI.clearScreen();
        TUI.showCenterMessage("PIN has been", 0);
        if (newPin < 0 || newPin != confirmPin) {
            TUI.showCenterMessage("held", 1);
        } else {
            user.changePin(newPin);
            TUI.showCenterMessage("changed", 1);
        }
        Time.sleep(TIMEOUT);
    }
}
