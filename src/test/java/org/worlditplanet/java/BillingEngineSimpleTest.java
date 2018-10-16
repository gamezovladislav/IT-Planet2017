package org.worlditplanet.java;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.worlditplanet.java.Utils.findInvoiceValue;
import static org.worlditplanet.java.Utils.getResourcePath;

@RunWith(MockitoJUnitRunner.class)
public class BillingEngineSimpleTest {

    @Test
    public void checkCorrectInvoice() throws IOException {
        Path tariffs = getResourcePath("/test3/tariffs.xml");
        Path subscribers = getResourcePath("/test3/subscribers.xml");
        Path actions = getResourcePath("/test3/actions.zip");
        Path invoices = Paths.get("/tmp/invoices.json");

        // TODO: Delete stub, use real object
        BillingEngine engine = new BillingEngineStub();
        engine.billing(tariffs, subscribers, actions, invoices);

        String invoice = findInvoiceValue(invoices, "79011234567");
        Files.delete(invoices);
        assertThat(invoice, is("101.00"));
    }

}
