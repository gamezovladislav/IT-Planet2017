package org.worlditplanet.java;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BillingEngineStub implements BillingEngine {

    @Override
    public void billing(Path tariffsFile, Path subscribersFile, Path actionsFile, Path invoicesFile) {
        try {
            BufferedWriter writer = Files.newBufferedWriter(invoicesFile);
            writer.write("{\"invoices\": [{ \"msisdn\": \"79011234567\", \"value\": \"101.00\" }]}");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
