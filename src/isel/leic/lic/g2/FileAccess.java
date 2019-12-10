/*-
 * SPDX-License-Identifier: BSD-3-Clause
 * Copyright (c) 2019 Luis Bandarra <luis.bandarra@homestudio.pt>
 */

package isel.leic.lic.g2;

import java.io.*;
import java.util.Scanner;

public class FileAccess {
    private final boolean APPEND = true;
    private File file;

    public FileAccess(String file) {
        this.file = new File(file);
    }

    public Scanner read() {
        try {
            InputStream in = new FileInputStream(file);
            return new Scanner(in);
           } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void writeln(String str) {
        try {
            Writer out = new FileWriter(file, APPEND);
            out.write(str + "\n");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        try {
            Writer out = new FileWriter(file, false);
            out.write("");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
