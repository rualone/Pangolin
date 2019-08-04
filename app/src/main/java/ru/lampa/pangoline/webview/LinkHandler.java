/* -*- Mode: Java; c-basic-offset: 4; tab-width: 4; indent-tabs-mode: nil; -*-
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package ru.lampa.pangoline.webview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;

import mozilla.components.concept.engine.HitResult;
import ru.lampa.pangoline.web.IWebView;

/* package */ class LinkHandler implements View.OnLongClickListener {
    private final WebView webView;
    private @Nullable
    IWebView.Callback callback = null;

    public LinkHandler(final WebView webView) {
        this.webView = webView;
    }

    public void setCallback(final @Nullable IWebView.Callback callback) {
        this.callback = callback;
    }

    @Override
    public boolean onLongClick(View v) {
        if (callback == null) {
            return false;
        }

        final WebView.HitTestResult hitTestResult = webView.getHitTestResult();

        switch (hitTestResult.getType()) {
            case WebView.HitTestResult.SRC_ANCHOR_TYPE:
                final String linkURL = hitTestResult.getExtra();
                if (linkURL != null) {
                    callback.onLongPress(new HitResult.UNKNOWN(linkURL));
                }
                return true;

            case WebView.HitTestResult.IMAGE_TYPE:
                final String imageURL = hitTestResult.getExtra();
                if (imageURL != null) {
                    callback.onLongPress(new HitResult.IMAGE(imageURL));
                }
                return true;

            case WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE:
                // hitTestResult.getExtra() contains only the image URL, and not the link
                // URL. Internally, WebView's HitTestData contains both, but they only
                // make it available via requestPangolineNodeHref...
                final Message message = new Message();
                message.setTarget(new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        final Bundle data = msg.getData();
                        final String url = data.getString("url");
                        final String src = data.getString("src");

                        if (url == null || src == null) {
                            throw new IllegalStateException("WebView did not supply url or src for image link");
                        }

                        if (callback != null) {
                            callback.onLongPress(new HitResult.IMAGE_SRC(src, url));
                        }
                    }
                });

                webView.requestFocusNodeHref(message);
                return true;

            default:
                return false;
        }
    }
}