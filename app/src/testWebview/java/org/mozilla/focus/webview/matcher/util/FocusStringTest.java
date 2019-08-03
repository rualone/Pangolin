package ru.lampa.pangoline.webview.matcher.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PangolineStringTest {

    @Test (expected = StringIndexOutOfBoundsException.class)
    public void outOfBounds() throws StringIndexOutOfBoundsException {
        final String fullStringRaw = "a";
        final PangolineString fullString = PangolineString.create(fullStringRaw);

        // Is beyond the raw input string
        fullString.charAt(1);
    }

    @Test (expected = StringIndexOutOfBoundsException.class)
    public void outofBoundsAfterSubstring() throws StringIndexOutOfBoundsException {
        final String fullStringRaw = "abcd";
        final PangolineString fullString = PangolineString.create(fullStringRaw);

        final PangolineString substring = fullString.substring(3);
        // substring == "d"
        substring.charAt(1);
    }

    @Test (expected = StringIndexOutOfBoundsException.class)
    public void outofBoundsSubstringLarge() throws StringIndexOutOfBoundsException {
        final String fullStringRaw = "abcd";
        final PangolineString fullString = PangolineString.create(fullStringRaw);

        final PangolineString substring = fullString.substring(5);
    }

    @Test (expected = StringIndexOutOfBoundsException.class)
    public void outofBoundsSubstringNegative() throws StringIndexOutOfBoundsException {
        final String fullStringRaw = "abcd";
        final PangolineString fullString = PangolineString.create(fullStringRaw);

        final PangolineString substring = fullString.substring(-1);
    }


    @Test
    public void testSubstringLength() {
        final String fullStringRaw = "a";
        final PangolineString fullString = PangolineString.create(fullStringRaw);

        assertEquals("PangolineString length must match input string length",
                fullStringRaw.length(), fullString.length());

        final PangolineString sameString = fullString.substring(0);
        assertEquals("substring(0) should equal input String",
                fullStringRaw.length(), sameString.length());
        assertEquals("substring(0) should equal input String",
                fullStringRaw.charAt(0), sameString.charAt(0));


        final PangolineString emptyString = fullString.substring(1);
        assertEquals("empty substring should be empty",
                0, emptyString.length());
    }

    @Test (expected = StringIndexOutOfBoundsException.class)
    public void outofBoundsAfterSubstringEmpty() throws StringIndexOutOfBoundsException {
        final String fullStringRaw = "abcd";
        final PangolineString fullString = PangolineString.create(fullStringRaw);

        final PangolineString substring = fullString.substring(4);
        // substring == ""
        substring.charAt(0);
    }

    @Test
    public void testForwardString() {
        final String fullStringRaw = "abcd";
        final PangolineString fullString = PangolineString.create(fullStringRaw);

        assertEquals("PangolineString length must match input string length",
                fullStringRaw.length(), fullString.length());

        for (int i = 0; i < fullStringRaw.length(); i++) {
            assertEquals("PangolineString character doesn't match input string character",
                    fullStringRaw.charAt(i), fullString.charAt(i));
        }

        final String substringRaw = fullStringRaw.substring(2);
        final PangolineString substring = fullString.substring(2);

        for (int i = 0; i < substringRaw.length(); i++) {
            assertEquals("PangolineString character doesn't match input string character",
                    substringRaw.charAt(i), substring.charAt(i));
        }
    }

    @Test
    public void testReverseString() {
        final String fullUnreversedStringRaw = "abcd";

        final String fullStringRaw = new StringBuffer(fullUnreversedStringRaw).reverse().toString();
        final PangolineString fullString = PangolineString.create(fullUnreversedStringRaw).reverse();

        assertEquals("PangolineString length must match input string length",
                fullStringRaw.length(), fullString.length());

        for (int i = 0; i < fullStringRaw.length(); i++) {
            assertEquals("PangolineString character doesn't match input string character",
                    fullStringRaw.charAt(i), fullString.charAt(i));
        }

        final String substringRaw = fullStringRaw.substring(2);
        final PangolineString substring = fullString.substring(2);

        for (int i = 0; i < substringRaw.length(); i++) {
            assertEquals("PangolineString character doesn't match input string character",
                    substringRaw.charAt(i), substring.charAt(i));
        }
    }
}