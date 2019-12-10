/*-
 * SPDX-License-Identifier: BSD-3-Clause
 * Copyright (c) 2019 Luis Bandarra <luis.bandarra@homestudio.pt>
 */

package isel.leic.lic.g2.User;

public class User {
    private final char separator = ';';
    private String name;
    private int uin;
    private String pin;
    private String message;

    public User(String name, int uin, int pin, String message) {
        this.name = name;
        this.uin = uin;
        this.pin = generatePassword(pin);
        this.message = message;
    }

    public User(String user) {
        String[] outStr = user.split(";",0);
        uin = Integer.parseInt(outStr[0]);
        pin = outStr[1];
        name = outStr[2];
        message = (outStr.length == 4) ? outStr[3] : "";
    }

    // to do
    private String generatePassword(int value) {
        return Integer.toString(value);
    }

    public boolean checkPassword(int pinCheck) {
        String codedPin = generatePassword(pinCheck);
        return pin.equals(codedPin);
    }

    public void changePassword(int newPin) {
        pin = generatePassword(newPin);
    }

    public void removeMessage() {
        message = "";
    }

    public int getUID() {
        return uin;
    }

    @Override
    public String toString() {
        String msg = (message.length() > 0) ? message + separator : "";
        return String.valueOf(uin) + separator + String.valueOf(pin) + separator + name + separator + msg;
    }
}
