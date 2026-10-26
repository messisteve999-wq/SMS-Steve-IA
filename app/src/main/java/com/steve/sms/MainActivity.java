package com.steve.sms;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    int dp(int n) { return (int)(n * getResources().getDisplayMetrics().density + 0.5f); }

    @Override public void onCreate(Bundle saved) {
        super.onCreate(saved);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(20),dp(28),dp(20),dp(20));
        root.setBackgroundColor(Color.rgb(18,20,25));
        setContentView(root);

        TextView logo = new TextView(this);
        logo.setText("SS");
        logo.setTextSize(32);
        logo.setTextColor(Color.rgb(238,199,115));
        logo.setGravity(17);
        root.addView(logo,new LinearLayout.LayoutParams(-1,dp(72)));

        TextView title = new TextView(this);
        title.setText("Steve IA — version 2");
        title.setTextSize(24);
        title.setTextColor(Color.WHITE);
        root.addView(title);

        TextView info = new TextView(this);
        info.setText("Une bulle flottante reste disponible au-dessus de tes applications. Steve IA lit le texte des notifications que tu autorises et prépare des suggestions à copier. Il n'envoie aucun message.");
        info.setTextColor(Color.LTGRAY);
        info.setTextSize(15);
        info.setPadding(0,dp(12),0,dp(18));
        root.addView(info);

        Button notif = button("1. Autoriser les notifications");
        notif.setOnClickListener(v -> startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")));
        root.addView(notif,params());

        Button overlay = button("2. Autoriser la superposition");
        overlay.setOnClickListener(v -> {
            if (!Settings.canDrawOverlays(this)) {
                Intent i = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:"+getPackageName()));
                startActivity(i);
            } else Toast.makeText(this,"Autorisation déjà accordée",Toast.LENGTH_SHORT).show();
        });
        root.addView(overlay,params());

        Button start = button("3. Afficher la bulle Steve IA");
        start.setOnClickListener(v -> {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this,"Autorise d'abord la superposition",Toast.LENGTH_LONG).show();
                startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:"+getPackageName())));
                return;
            }
            startService(new Intent(this,FloatingSteveService.class));
            Toast.makeText(this,"Bulle Steve IA activée",Toast.LENGTH_SHORT).show();
            finish();
        });
        root.addView(start,params());

        Button stop = button("Arrêter la bulle");
        stop.setOnClickListener(v -> { stopService(new Intent(this,FloatingSteveService.class)); Toast.makeText(this,"Bulle arrêtée",Toast.LENGTH_SHORT).show(); });
        root.addView(stop,params());
    }

    Button button(String s) {
        Button b = new Button(this);
        b.setText(s); b.setAllCaps(false); b.setTextColor(Color.BLACK);
        b.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.rgb(238,199,115)));
        return b;
    }
    LinearLayout.LayoutParams params() {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1,-2);
        p.bottomMargin=dp(12); return p;
    }
}
