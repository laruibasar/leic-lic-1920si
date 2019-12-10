/*-
 * SPDX-License-Identifier: BSD-3-Clause
 * Copyright (c) 2019 Luis Bandarra <luis.bandarra@homestudio.pt>
 */

package isel.leic.lic.g2.Log;

import isel.leic.lic.g2.FileAccess;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {
    private final String LOG_FILE = "log.txt";
    private FileAccess log;

    public Log() {
        log = new FileAccess(LOG_FILE);
    }

    public void logger(String msg) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        log.writeLine(time + " -> " + msg);
    }

    public static void main(String[] args) {
        Log log = new Log();

        System.out.println("Teste Log");
        System.out.println("1\t Logger");
        log.logger("Hello");

        System.out.println("2\t 1:Luis Bandarra");
        log.logger("1:Luis Bandarra");

        System.out.println("3\t X: José Manuel");
        log.logger("X: José Manuel");

        System.out.println("4:\t Libertem Matorras");
        log.logger("Libertem Matorras");
    }
}