package org.worlditplanet.java;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemErrRule;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Path;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BillingAppTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Rule
    public final SystemErrRule systemErrRule = new SystemErrRule().enableLog().muteForSuccessfulTests();

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog().muteForSuccessfulTests();

    @Mock
    private BillingEngine engine;

    @Test
    public void shouldBeSuccessCreated() {
        BillingApp app = new BillingApp(engine);
        assertThat(app, notNullValue());
    }

    @Test
    public void shouldBeThrowNPEWithNullEngine() {
        exception.expect(NullPointerException.class);
        new BillingApp(null);
    }

    @Test
    public void shouldBePrintHelpIfMissingRequiredArgs() {
        BillingApp app = new BillingApp(engine);
        String[] args = {""};
        app.process(args);
        String errorLog = systemErrRule.getLog();
        assertThat(errorLog, containsString("Missing required options"));
        String outLog = systemOutRule.getLog();
        assertThat(outLog, containsString("usage: billing"));
    }

    @Test
    public void shouldBeRunBillingEngine() {
        BillingApp app = new BillingApp(engine);
        String[] args = {"-t", "/tmp/tariffs", "-a", "/tmp/actions", "-s", "/tmp/subscribers", "-i", "/tmp/invoices"};
        app.process(args);
        verify(engine).billing(any(Path.class), any(Path.class), any(Path.class), any(Path.class));
    }
}
