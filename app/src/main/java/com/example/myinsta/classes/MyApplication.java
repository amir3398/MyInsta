package com.example.myinsta.classes;

import android.app.Application;

import androidx.core.provider.FontRequest;
import androidx.emoji.text.EmojiCompat;
import androidx.emoji.text.FontRequestEmojiCompatConfig;

import com.example.myinsta.R;
import com.facebook.drawee.backends.pipeline.Fresco;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);

        emoji();
    }

    public void emoji() {
        EmojiCompat.Config config;
        FontRequest fontRequest = new FontRequest(
                "com.google.android.gms.fonts",
                "com.google.android.gms",
                "Noto Color Emoji Compat",
                R.array.com_google_android_gms_fonts_certs);
        config = new FontRequestEmojiCompatConfig(getApplicationContext(), fontRequest).setReplaceAll(true);
        EmojiCompat.init(config);
    }
}
