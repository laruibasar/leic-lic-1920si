/*-
 * SPDX-License-Identifier: BSD-3-Clause
 * Copyright (c) 2019 Luis Bandarra <luis.bandarra@homestudio.pt>
 */

/*
 * Classe que permite gerir a informacao de todos os utilizadores do sistema
 */
package isel.leic.lic.g2.User;

import java.util.LinkedList;
import java.util.Scanner;
import isel.leic.lic.g2.FileAccess;

public class Users {
    private static final String USERS_FILE = "users.txt";
    private static final int MAX_USERS = 1000;
    private static LinkedList<User> list;
    private static FileAccess users;
    private static Scanner sc;

    // inicializa a classe com um handler para o ficheiro
    public static void init() {
        users = new FileAccess(USERS_FILE);
        sc = users.read();
        list = new LinkedList<>();
        loadUsers();
    }

    // carrega em memória os utilizadores a partir de ficheiro
    private static void loadUsers() {
        while (sc.hasNextLine()) {
            String str = sc.nextLine();
            User u = new User(str);
            list.add(u);
        }
    }

    // guarda os utilizadores para ficheiro, reescrevendo todos os utilizadores
    public static void saveUsers() {
        users.clear();

        for (User u : list)
            users.writeln(u.save());
    }

    // adiciona um utilizador atraves de uma instancia de User
    public static boolean addUser(User u) {
        if (count() < MAX_USERS) {
            int idx = getAvailableUIN();
            list.add(idx, u);
            return true;
        }
        return false;
    }

    // remove um utilizador, tendo uma instancia de User
    public static boolean removeUser(User u) {
        return list.remove(u);
    }

    // remove um utilizador, conhecendo um UIN
    public static boolean removeUser(int uin) {
        return removeUser(searchUser(uin));
    }

    // retorna o numero atual de utilizadores
    public static int count() {
        return list.size();
    }

    // pesquisa um utilizador na lista
    public static User searchUser(int uin) {
        for (User u : list)
            if (u.getUID() == uin)
                return u;

        return null;
    }

    // obtem o UIN mas baixo que esteja livre
    public static int getAvailableUIN() {
        int available = 0;
        for (User u : list) {
            if (available != u.getUID())
                break;

            available++;
        }
        return available;
    }

    public static void main(String[] args) {
        init();

        System.out.println("Teste Users");
        System.out.println("Loaded users");
        for (User u : list) {
            System.out.println(u.toString());
        }

        System.out.println("\n1 - UIN disponivel");
        int uinAvailable = getAvailableUIN();
        System.out.println(uinAvailable);

        System.out.println("\n2 - Inserir utilizador");
        addUser(new User("Luis Bandarra", uinAvailable, 111, "Bem vindo"));
        for (User u : list) {
            System.out.println(u.toString());
        }

        System.out.println("\n3 - Remover utilizador (por uin) (remover ultimo utilizador introduzido");
        removeUser(uinAvailable);
        for (User u : list) {
            System.out.println(u.toString());
        }

        System.out.println("\n4 - Inserir utilizador e pesquisar utilizador");
        addUser(new User("Luis Bandarra", getAvailableUIN(), 111, "Bem vindo"));
        for (User u : list) {
            System.out.println(u.toString());
        }
        User t = searchUser(4);
        System.out.println("pesquisa: " + t.toString());

        System.out.println("\n5 - Remover utilizador (com obj User)");
        removeUser(t);
        for (User u : list) {
            System.out.println(u.toString());
        }

        System.out.println("\n6 - Pesquisar utilizador não existente");
        System.out.println ("Utilizador: " + searchUser(7));

        System.out.println("\n7 - Pesquisar utilizador existente");
        System.out.println ("Utilizador: " + searchUser(2).toString());

        System.out.println("\n8 - Guardar para ficheiro");
        addUser(new User("Luis Bandarra", getAvailableUIN(), 111, "Bem vindo"));
        for (User u : list) {
            System.out.println(u.toString());
        }
        saveUsers();
        System.out.println("\n\nShow users");
        loadUsers();
        for (User u : list) {
            System.out.println(u.toString());
        }
    }
}
