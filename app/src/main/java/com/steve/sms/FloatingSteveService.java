package com.steve.sms;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.IBinder;
import android.provider.Settings;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FloatingSteveService extends Service {
    WindowManager wm;
    View bubble, panel;
    WindowManager.LayoutParams bubbleParams, panelParams;
    int gold=Color.rgb(238,199,115), dark=Color.rgb(25,28,35), white=Color.WHITE;
    int dp(float n) { return (int)(n*getResources().getDisplayMetrics().density+0.5f); }
    GradientDrawable bg(int c, int radius) {
        GradientDrawable d=new GradientDrawable(); d.setColor(c); d.setCornerRadius(dp(radius)); return d;
    }
    @Override public void onCreate() {
        super.onCreate();
        if (!Settings.canDrawOverlays(this)) { stopSelf(); return; }
        wm=(WindowManager)getSystemService(WINDOW_SERVICE);
        TextView b=new TextView(this); b.setText("SS"); b.setTextColor(gold); b.setTextSize(18);
        b.setTypeface(Typeface.DEFAULT,Typeface.BOLD); b.setGravity(Gravity.CENTER); b.setBackground(bg(dark,100));
        bubble=b;
        bubbleParams=new WindowManager.LayoutParams(dp(58),dp(58),WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,PixelFormat.TRANSLUCENT);
        bubbleParams.gravity=Gravity.TOP|Gravity.START; bubbleParams.x=dp(12); bubbleParams.y=dp(180);
        b.setOnTouchListener(new View.OnTouchListener() {
            float sx,sy; int ox,oy; boolean moved;
            public boolean onTouch(View v, MotionEvent e) {
                if(e.getAction()==MotionEvent.ACTION_DOWN){sx=e.getRawX();sy=e.getRawY();ox=bubbleParams.x;oy=bubbleParams.y;moved=false;return true;}
                if(e.getAction()==MotionEvent.ACTION_MOVE){int dx=(int)(e.getRawX()-sx),dy=(int)(e.getRawY()-sy); if(Math.abs(dx)>dp(4)||Math.abs(dy)>dp(4))moved=true; bubbleParams.x=ox+dx;bubbleParams.y=oy+dy;wm.updateViewLayout(bubble,bubbleParams);return true;}
                if(e.getAction()==MotionEvent.ACTION_UP){if(!moved)togglePanel();return true;} return true;
            }
        });
        wm.addView(bubble,bubbleParams);
    }
    void togglePanel() {
        if(panel!=null){try{wm.removeView(panel);}catch(Exception ignored){} panel=null;return;}
        LinearLayout box=new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL); box.setPadding(dp(12),dp(12),dp(12),dp(12)); box.setBackground(bg(dark,18));
        TextView head=new TextView(this); head.setText("Steve IA  •  suggestions");head.setTextColor(gold);head.setTextSize(18);head.setTypeface(null,Typeface.BOLD);box.addView(head);
        android.content.SharedPreferences pref=getSharedPreferences("steve_ia",MODE_PRIVATE);
        String sender=pref.getString("latest_sender",""); String msg=pref.getString("latest_message","");
        TextView incoming=new TextView(this); incoming.setText((sender.isEmpty()?"Dernier message":sender)+"\n"+(msg.isEmpty()?"Aucun message détecté. Active l'accès aux notifications.":msg)); incoming.setTextColor(white); incoming.setTextSize(14);incoming.setPadding(0,dp(8),0,dp(10));box.addView(incoming);
        List<String> replies=makeReplies(msg);
        String[] labels={"1. Réponse naturelle","2. Réponse attentionnée","3. Question pour continuer"};
        for(int i=0;i<replies.size();i++){
            final String reply=replies.get(i);
            TextView t=new TextView(this);t.setText(labels[i]+"\n"+reply);t.setTextColor(white);t.setTextSize(14);t.setPadding(dp(8),dp(8),dp(8),dp(8));t.setBackground(bg(Color.rgb(43,47,57),12));
            LinearLayout.LayoutParams tp=new LinearLayout.LayoutParams(-1,-2);tp.topMargin=dp(7);box.addView(t,tp);
            Button copy=new Button(this);copy.setText("Copier");copy.setAllCaps(false);copy.setTextColor(Color.BLACK);copy.setBackgroundTintList(android.content.res.ColorStateList.valueOf(gold));
            copy.setOnClickListener(v->{ClipboardManager cm=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);cm.setPrimaryClip(ClipData.newPlainText("Steve IA",reply));Toast.makeText(this,"Réponse copiée",Toast.LENGTH_SHORT).show();});
            box.addView(copy,new LinearLayout.LayoutParams(-1,dp(42)));
        }
        Button close=new Button(this);close.setText("Fermer");close.setAllCaps(false);close.setOnClickListener(v->togglePanel());box.addView(close,new LinearLayout.LayoutParams(-1,dp(42)));
        ScrollView sc=new ScrollView(this);sc.addView(box);panel=sc;
        panelParams=new WindowManager.LayoutParams((int)(getResources().getDisplayMetrics().widthPixels*0.88),dp(470),WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,PixelFormat.TRANSLUCENT);
        panelParams.gravity=Gravity.CENTER;wm.addView(panel,panelParams);
    }
    List<String> makeReplies(String input) {
        String m=input.toLowerCase(Locale.ROOT); List<String> r=new ArrayList<>();
        if(m.isEmpty()){r.add("Je viens de voir ton message 😊. Tu peux me le préciser ?");r.add("Merci pour ton message. Je prends le temps de te répondre.");r.add("Et toi, comment tu vois les choses ?");}
        else if(m.contains("triste")||m.contains("problème")||m.contains("difficile")||m.contains("malade")){
            r.add("Je suis désolé que tu traverses ça. J'espère que les choses iront mieux.");
            r.add("Merci de me le dire. Si tu veux en parler, je suis là pour t'écouter.");
            r.add("Qu'est-ce qui pourrait te faire du bien en ce moment ? Tu peux m'en parler si tu veux.");
        } else if(m.contains("?")){
            r.add("Merci pour ton message 😊. Et toi, qu'est-ce que tu en penses ?");
            r.add("Je vois ce que tu veux dire. J'aimerais bien comprendre ton point de vue aussi.");
            r.add("C'est intéressant ! Qu'est-ce qui t'a amené à te poser cette question ?");
        } else {
            r.add("Merci pour ton message 😊. Comment se passe ta journée ?");
            r.add("Je vois 😊, merci de partager ça avec moi. Comment tu l'as vécu ?");
            r.add("J'aimerais mieux te connaître. Qu'est-ce qui te passionne en ce moment ?");
        }
        return r;
    }
    @Override public int onStartCommand(Intent i,int f,int id){return START_STICKY;}
    @Override public void onDestroy(){if(wm!=null){try{if(panel!=null)wm.removeView(panel);}catch(Exception ignored){}try{if(bubble!=null)wm.removeView(bubble);}catch(Exception ignored){}}super.onDestroy();}
    @Override public IBinder onBind(Intent i){return null;}
}
