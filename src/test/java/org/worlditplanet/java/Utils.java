package org.worlditplanet.java;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static io.restassured.path.json.JsonPath.from;

import static java.lang.String.format;

final class Utils {

    private Utils() {
    }

    static Path getResourcePath(String path) {
        try {
            return Paths.get(Utils.class.getResource(path).toURI());
        } catch (URISyntaxException e) {
            // do nothing
        }
        return null;
    }

    static String findInvoiceValue(Path path, String msisdn) {
        return from(path.toFile()).getString(format("invoices.find{ invoice -> invoice.msisdn == \"%s\" }.value", msisdn));
    }
}
