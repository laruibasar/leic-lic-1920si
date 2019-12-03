/*-
 * SPDX-License-Identifier: BSD-3-Clause
 * Copyright (c) 2019 Luis Bandarra <luis.bandarra@homestudio.pt>
 */

package isel.leic.lic.g2.User;

public class User {
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
}
