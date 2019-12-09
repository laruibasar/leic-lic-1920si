/*-
 * SPDX-License-Identifier: BSD-3-Clause
 * Copyright (c) 2019 Luis Bandarra <luis.bandarra@homestudio.pt>
 */

package isel.leic.lic.g2.User;

import isel.leic.lic.g2.FileAccess;
import isel.leic.lic.g2.Log.Log;

import java.util.Scanner;

public class Users {
    private static final String USERS_FILE = "users.txt";
    private static FileAccess users;
    private static Scanner sc;

    public static void init() {
        users = new FileAccess(USERS_FILE);
        sc = users.read().useDelimiter(";");
        loadUsers();
    }

    private static void loadUsers() {
        while (sc.hasNextLine()) {
            int uin = sc.nextInt();
            int pin = sc.nextInt();
            String name = sc.next();
            String message = sc.next();
            sc.next(); // clear newline
            System.out.println(uin + " " + pin + " " + name + " " + message);
        }
    }

    public static void main(String[] args) {
        init();

        System.out.println("Teste Users");
    }
}
