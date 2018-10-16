package org.worlditplanet.java;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by gamezovladislav on 02.06.2017.
 */
public class MyRunner {

    public static void main(String[] args) {
        BillingEngineImpl runner = new BillingEngineImpl();
        Path tariffs = Paths.get("tariffs.xml");
        Path subscribers = Paths.get("subscribers.xml");
        Path actions = Paths.get("actions.xml");
        Path invoices = Paths.get("invoices.json");
        runner.billing(tariffs, subscribers, actions, invoices);
    }
}
