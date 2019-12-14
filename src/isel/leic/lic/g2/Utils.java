/*-
 * SPDX-License-Identifier: BSD-3-Clause
 * Copyright (c) 2019 Luis Bandarra <luis.bandarra@homestudio.pt>
 */

package isel.leic.lic.g2;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;

public class Utils {
    public static String getProperties(String property) {
        try (InputStream io = new FileInputStream("USB_PORT.properties")) {
            Properties prop = new Properties();

            prop.load(io);
            return prop.getProperty(property);
        } catch (IOException ie) {
            return null;
        }
    }
}
