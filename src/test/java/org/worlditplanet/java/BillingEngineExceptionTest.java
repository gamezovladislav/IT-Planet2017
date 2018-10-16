package org.worlditplanet.java;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.doThrow;
import static org.worlditplanet.java.Utils.getResourcePath;

@RunWith(MockitoJUnitRunner.class)
public class BillingEngineExceptionTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Mock
    private BillingEngine engine;

    @Test
    public void shouldBeThrowExceptionIfFilesNotExists() throws URISyntaxException {
        Path notExistFile = Paths.get("/tmp");

        // TODO: Delete mock, use real object
        doThrow(new RuntimeException("File not found")).when(engine).billing(same(notExistFile), same(notExistFile), same(notExistFile), any());

        exception.expect(RuntimeException.class);
        engine.billing(notExistFile, notExistFile, notExistFile, notExistFile);
    }

    @Test
    public void shouldBeThrowExceptionIfTariffIncorrect() throws URISyntaxException {
        Path tariffs = getResourcePath("/test1/tariffs.xml");

        // TODO: Delete mock, use real object
        doThrow(new RuntimeException("Tariff with id 1 incorrect")).when(engine).billing(same(tariffs), any(), any(), any());

        exception.expect(RuntimeException.class);
        exception.expectMessage(is("Tariff with id 1 incorrect"));
        engine.billing(tariffs, null, null, null);
    }

    @Test
    public void shouldBeThrowExceptionIfSubscriberMsisdnIncorrect() throws URISyntaxException {
        Path tariffs = getResourcePath("/test2/tariffs.xml");
        Path subscribers = getResourcePath("/test2/subscribers_msisdn.xml");

        // TODO: Delete mock, use real object
        doThrow(new RuntimeException("Subscriber with msisdn 9011234567 incorrect"))
                .when(engine).billing(same(tariffs), same(subscribers), any(), any());

        exception.expect(RuntimeException.class);
        exception.expectMessage(is("Subscriber with msisdn 9011234567 incorrect"));
        engine.billing(tariffs, subscribers, null, null);
    }

    @Test
    public void shouldBeThrowExceptionIfSubscriberTariffIncorrect() throws URISyntaxException {
        Path tariffs = getResourcePath("/test2/tariffs.xml");
        Path subscribers = getResourcePath("/test2/subscribers_tariff.xml");

        // TODO: Delete mock, use real object
        doThrow(new RuntimeException("Subscriber with msisdn 79011234567 incorrect"))
                .when(engine).billing(same(tariffs), same(subscribers), any(), any());

        exception.expect(RuntimeException.class);
        exception.expectMessage(is("Subscriber with msisdn 79011234567 incorrect"));
        engine.billing(tariffs, subscribers, null, null);
    }

}
