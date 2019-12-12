package isel.leic.lic.g2;

import isel.leic.lic.g2.Door.DoorMechanism;
import isel.leic.lic.g2.Log.Log;
import isel.leic.lic.g2.TUI.TUI;
import isel.leic.lic.g2.User.Users;

public class App {
    public static void init() {
        Log.init();
        Users.init();
        TUI.init();
        DoorMechanism.init();
        M.init();
    }

    public static void main(String[] args) {
        init();

    }
}
