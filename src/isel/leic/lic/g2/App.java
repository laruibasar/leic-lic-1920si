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
    private static Scanner sc;

    public static void init() {
        Log.init();
        Users.init();
        TUI.init();
        DoorMechanism.init();
        M.init();

        run = true;
        maintenance_mode = M.isMaintenanceMode();
        sc = new Scanner(System.in);
    }

    public static void enterMaintenance() {
        boolean turnOff = false;

        showMaintenanceMenu();
        while (M.isMaintenanceMode() && !turnOff) {
            System.out.print("Maintenance> ");
            String cmd = sc.nextLine();
            cmd = cmd.toUpperCase();

            switch (cmd) {
                case "NEW":
                    maintenanceNewUser();
                    break;
                case "DEL":
                    maintenanceDelUser();
                    break;
                case "MSG":
                    maintenanceInsertMessage();
                    break;
                case "OFF":
                    run = false;
                    turnOff = true;
                    break;
                default:
                    System.out.println("Invalid command: " + cmd);
                    break;
            }
        }
    }

    private static void maintenanceNewUser() {
        System.out.println("User name?");
        String name = sc.nextLine();
        System.out.println("PIN?");

        int pin = Integer.parseInt(sc.nextLine());
        int uin = Users.getAvailableUIN();

        User u = new User(name, uin, pin, "");
        Users.addUser(u);
        System.out.println("Adding user " + u.toString());
    }

    private static void maintenanceDelUser() {
        User u = getUser();
        if (u == null) {
            System.out.println("Invalid UIN");
            return;
        }

        System.out.println("Remove user " + u.toString());
        System.out.println("Y/N");
        String cmd = sc.nextLine();

        if (confirm(cmd)) {
            System.out.println("User " + u.toString() + " removed");
            Users.removeUser(u);
        } else {
            System.out.println("Command aborted");
        }
    }

    private static void maintenanceInsertMessage() {
        User u = getUser();
        if (u == null) {
            System.out.println("Invalid UIN");
            return;
        }

        if (!u.getMessage().equals("")) {
            System.out.println("User has this message: " + u.getMessage());
            System.out.println("Remove this message Y/N? ");
            String question = sc.nextLine();

            if (!confirm(question)) {
                System.out.println("Command aborted");
                return;
            }
        }

        System.out.print("Message? ");
        String message = sc.nextLine();

        if (message.length() > 32) {
            System.out.println("Message to long for display");
            System.out.println("Command aborted");
            return;
        }

        System.out.println("The message " + message + " as been associated to " + u.toString());
        u.changeMessage(message);
    }

    private static boolean confirm(String answer) {
        return answer.equalsIgnoreCase("Y");
    }

    private static User getUser() {
        System.out.println("UIN?");
        int uin = Integer.parseInt(sc.nextLine());

        return Users.searchUser(uin);
    }

    private static void showMaintenanceMenu() {
        System.out.println("Turn M key off, to terminate the maintenance mode.");
        System.out.println("Commands: NEW, DEL, MSG, or OFF");
    }

    public static void main(String[] args) {
        init();

        while (run) {
            if (maintenance_mode)
                enterMaintenance();
            maintenance_mode = M.isMaintenanceMode();
        }

        TUI.clearScreen();
        TUI.showMessage("Fim do programa", 0, 0);
        TUI.showMessage("Adeus", 1, 5);
        Time.sleep(1000);
        TUI.clearScreen();
        System.exit(0);
    }
}
