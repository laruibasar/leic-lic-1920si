/*-
 * SPDX-License-Identifier: BSD-3-Clause
 * Copyright (c) 2019 Luis Bandarra <luis.bandarra@homestudio.pt>
 */

package isel.leic.lic.g2.User;

import isel.leic.lic.g2.FileAccess;

import java.util.LinkedList;
import java.util.Scanner;

public class Users {
    private static final String USERS_FILE = "users.txt";
    private static final int MAX_USERS = 1000;
    private static LinkedList<User> list;
    private static FileAccess users;
    private static Scanner sc;

    public static void init() {
        users = new FileAccess(USERS_FILE);
        sc = users.read();
        list = new LinkedList<>();
        loadUsers();
    }

    private static void loadUsers() {
        while (sc.hasNextLine()) {
            String str = sc.nextLine();
            User u = new User(str);
            list.add(u);
        }
    }

    public static void addUser(User u) {
        list.add(u);
    }

    public static void removeUser(User u) {
        list.
    }

    public static void main(String[] args) {
        init();

        System.out.println("Teste Users");
        for (User u : list) {
            System.out.println(u.toString());
        }
    }
}
