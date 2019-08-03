package ru.lampa.pangoline.webview.matcher;

import org.junit.Test;
import org.junit.runner.RunWith;
import ru.lampa.pangoline.webview.matcher.Trie.WhiteListTrie;
import ru.lampa.pangoline.webview.matcher.util.PangolineString;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
public class TrieTest {

    @Test
    public void findNode() throws Exception {
        final Trie trie = Trie.createRootNode();

        assertNull(trie.findNode(PangolineString.create("hello")));

        final Trie putNode = trie.put(PangolineString.create("hello"));
        final Trie foundNode = trie.findNode(PangolineString.create("hello"));

        assertNotNull(putNode);
        assertNotNull(foundNode);
        assertEquals(putNode, foundNode);

        // Substring matching: doesn't happen (except for subdomains, we test those later)
        assertNull(trie.findNode(PangolineString.create("hell")));
        assertNull(trie.findNode(PangolineString.create("hellop")));

        trie.put(PangolineString.create("hellohello"));

        // Ensure both old and new overlapping strings can still be found
        assertNotNull(trie.findNode(PangolineString.create("hello")));
        assertNotNull(trie.findNode(PangolineString.create("hellohello")));

        // These still don't match:
        assertNull(trie.findNode(PangolineString.create("hell")));
        assertNull(trie.findNode(PangolineString.create("hellop")));

        // Domain specific / partial domain tests:
        trie.put(PangolineString.create("foo.com").reverse());

        // Domain and subdomain can be found
        assertNotNull(trie.findNode(PangolineString.create("foo.com").reverse()));
        assertNotNull(trie.findNode(PangolineString.create("bar.foo.com").reverse()));
        // But other domains with some overlap don't match
        assertNull(trie.findNode(PangolineString.create("bar-foo.com").reverse()));
        assertNull(trie.findNode(PangolineString.create("oo.com").reverse()));
    }

    @Test
    public void testWhiteListTrie() {
        final WhiteListTrie trie;

        {
            final Trie whitelist = Trie.createRootNode();

            whitelist.put(PangolineString.create("abc"));

            trie = WhiteListTrie.createRootNode();
            trie.putWhiteList(PangolineString.create("def"), whitelist);
        }

        assertNull(trie.findNode(PangolineString.create("abc")));

        // In practice EntityList uses it's own search in order to cover all possible matching notes
        // (e.g. in case we have separate whitelists for mozilla.org and foo.mozilla.org), however
        // we don't need to test that here yet.
        final WhiteListTrie foundWhitelist = (WhiteListTrie) trie.findNode(PangolineString.create("def"));
        assertNotNull(foundWhitelist);

        assertNotNull(foundWhitelist.whitelist.findNode(PangolineString.create("abc")));
    }
}