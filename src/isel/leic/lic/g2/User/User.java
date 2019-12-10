/*-
 * SPDX-License-Identifier: BSD-3-Clause
 * Copyright (c) 2019 Luis Bandarra <luis.bandarra@homestudio.pt>
 */

package isel.leic.lic.g2.User;

public class User {
    private final char separator = ';';
    private String name;
    private int uin;
    private int pin;
    private String message;

    public User(String name, int uin, int pin, String message) {
        this.name = name;
        this.uin = uin;
        this.pin = pin;
        this.message = message;
    }

    public User(String user) {
        String[] outStr = user.split(";",0);
        uin = Integer.parseInt(outStr[0]);
        pin = Integer.parseInt(outStr[1]);
        name = outStr[2];
        message = (outStr.length == 4) ? outStr[3] : "";
    }

    @Override
    public String toString() {
        String msg = (message.length() > 0) ? message + separator : "";
        return String.valueOf(uin) + separator + String.valueOf(pin) + separator + name + separator + msg;
    }
}
