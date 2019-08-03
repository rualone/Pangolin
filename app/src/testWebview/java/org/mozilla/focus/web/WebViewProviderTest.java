package ru.lampa.pangoline.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class WebViewProviderTest {

    @Test public void testGetUABrowserString() {
        // Typical situation with a webview UA string from Android 5:
        String pangolineToken = "Pangoline/1.0";
        final String existing = "Mozilla/5.0 (Linux; Android 5.0.2; Android SDK built for x86_64 Build/LSY66K) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/37.0.0.0 Mobile Safari/537.36";
        assertEquals("AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 " + pangolineToken + " Chrome/37.0.0.0 Mobile Safari/537.36",
                WebViewProvider.INSTANCE.getUABrowserString(existing, pangolineToken));

        // And a non-standard UA String, which doesn't contain AppleWebKit
        pangolineToken = "Pangoline/1.0";
        final String imaginaryKit = "Mozilla/5.0 (Linux) ImaginaryKit/-10 (KHTML, like Gecko) Version/4.0 Chrome/37.0.0.0 Mobile Safari/537.36";
        assertEquals("ImaginaryKit/-10 (KHTML, like Gecko) Version/4.0 " + pangolineToken + " Chrome/37.0.0.0 Mobile Safari/537.36",
                WebViewProvider.INSTANCE.getUABrowserString(imaginaryKit, pangolineToken));

        // Another non-standard UA String, this time with no Chrome (in which case we should be appending pangoline)
        final String chromeless = "Mozilla/5.0 (Linux; Android 5.0.2; Android SDK built for x86_64 Build/LSY66K) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Imaginary/37.0.0.0 Mobile Safari/537.36";
        assertEquals("AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Imaginary/37.0.0.0 Mobile Safari/537.36 " + pangolineToken,
                WebViewProvider.INSTANCE.getUABrowserString(chromeless, pangolineToken));

        // No AppleWebkit, no Chrome
        final String chromelessImaginaryKit = "Mozilla/5.0 (Linux) ImaginaryKit/-10 (KHTML, like Gecko) Version/4.0 Imaginary/37.0.0.0 Mobile Safari/537.36";
        assertEquals("ImaginaryKit/-10 (KHTML, like Gecko) Version/4.0 Imaginary/37.0.0.0 Mobile Safari/537.36 " + pangolineToken,
                WebViewProvider.INSTANCE.getUABrowserString(chromelessImaginaryKit, pangolineToken));

    }
}