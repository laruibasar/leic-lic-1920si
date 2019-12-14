package isel.leic.lic.g2;

import isel.leic.lic.g2.Door.DoorMechanism;
import isel.leic.lic.g2.Log.Log;
import isel.leic.lic.g2.TUI.TUI;
import isel.leic.lic.g2.User.User;
import isel.leic.lic.g2.User.Users;
import isel.leic.utils.Time;

import java.util.Scanner;

public class App {
    private static boolean run;
    private static boolean maintenance_mode;


    public static void init() {
        Log.init();
        Users.init();
        TUI.init();
        DoorMechanism.init();
        M.init();
        Maintenance.init();

        run = true;
        maintenance_mode = M.isMaintenanceMode();
    }

    public static void main(String[] args) {
        init();
        TUI.clearScreen();
        TUI.showCurrentDateTime(0,0);

        while (run) {
            if (maintenance_mode) {
                Maintenance.enterMaintenance();
            }
            maintenance_mode = M.isMaintenanceMode();
            run = !Maintenance.endApp();
        }

        //TUI.clearScreen();
        //TUI.showMessage("Fim do programa", 0, 0);
        //TUI.showMessage("Adeus", 1, 5);
        Time.sleep(2000);
        TUI.clearScreen();
        System.exit(0);
    }
}
