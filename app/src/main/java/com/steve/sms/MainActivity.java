package com.steve.sms;

import android.app.Activity;
import android.os.Bundle;
import android.content.ClipboardManager;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {
    int bg = Color.rgb(18,20,25);
    int panel = Color.rgb(34,37,45);
    int gold = Color.rgb(238,199,115);
    int white = Color.WHITE;
    EditText message;
    RadioGroup styles;
    LinearLayout results;

    int dp(int n) {
        return (int)(n * getResources().getDisplayMetrics().density + 0.5f);
    }

    GradientDrawable shape(int color) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(dp(13));
        return d;
    }

    TextView label(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return t;
    }

    LinearLayout.LayoutParams full() {
        return new LinearLayout.LayoutParams(-1, -2);
    }

    Button makeButton(String value) {
        Button b = new Button(this);
        b.setText(value);
        b.setAllCaps(false);
        b.setTextColor(Color.rgb(30,30,30));
        b.setBackground(shape(gold));
        return b;
    }

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);

        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(bg);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(18),dp(20),dp(18),dp(28));
        scroll.addView(root);
        setContentView(scroll);

        TextView logo = label("SS",26,gold,true);
        logo.setGravity(Gravity.CENTER);
        logo.setBackground(shape(panel));
        root.addView(logo,new LinearLayout.LayoutParams(dp(64),dp(64)));

        TextView title = label("Steve IA",27,white,true);
        title.setPadding(0,dp(10),0,0);
        root.addView(title);
        root.addView(label("SMS • Assistant de conversation",14,Color.LTGRAY,false));
        root.addView(gap(20));

        root.addView(label("Colle le message reçu",17,white,true));
        message = new EditText(this);
        message.setTextColor(white);
        message.setHintTextColor(Color.GRAY);
        message.setHint("Ex. : Salut, comment tu vas ?");
        message.setGravity(Gravity.TOP);
        message.setMinLines(3);
        message.setPadding(dp(12),dp(12),dp(12),dp(12));
        message.setBackground(shape(panel));
        LinearLayout.LayoutParams mp = full();
        mp.topMargin = dp(8);
        root.addView(message,mp);

        root.addView(gap(12));
        root.addView(label("Style des réponses",17,white,true));
        styles = new RadioGroup(this);
        styles.setOrientation(RadioGroup.VERTICAL);
        String[] names = {"Doux (par défaut)","Amical","Drôle","Drague légère"};
        for (int i=0;i<names.length;i++) {
            RadioButton r = new RadioButton(this);
            r.setId(200+i);
            r.setText(names[i]);
            r.setTextColor(white);
            r.setButtonTintList(android.content.res.ColorStateList.valueOf(gold));
            styles.addView(r);
            if(i==0) r.setChecked(true);
        }
        root.addView(styles);

        Button generate = makeButton("Générer 3 suggestions");
        generate.setOnClickListener(v -> generateReplies());
        root.addView(generate,full());

        root.addView(gap(20));
        root.addView(label("Tes suggestions",20,white,true));
        results = new LinearLayout(this);
        results.setOrientation(LinearLayout.VERTICAL);
        root.addView(results);

        root.addView(gap(12));
        root.addView(label(
            "Prototype local : les propositions sont des exemples. Aucun message n'est envoyé automatiquement.",
            12,Color.LTGRAY,false
        ));
    }

    View gap(int height) {
        View v = new View(this);
        v.setLayoutParams(new LinearLayout.LayoutParams(1,dp(height)));
        return v;
    }

    List<String> makeReplies(String input, String style) {
        String m = input.toLowerCase(Locale.ROOT);
        List<String> r = new ArrayList<>();

        if (m.contains("triste") || m.contains("problème") ||
            m.contains("difficile") || m.contains("malade")) {
            r.add("Je suis désolé que tu traverses ça. J'espère que les choses iront mieux.");
            r.add("Merci de me le dire. Si tu veux en parler, je suis là pour t'écouter.");
            r.add("Qu'est-ce qui pourrait te faire du bien en ce moment ? Tu peux m'en parler si tu veux.");
        } else if (m.contains("comment tu vas") || m.contains("ça va") ||
                   m.startsWith("salut") || m.startsWith("coucou") ||
                   m.startsWith("bonjour") || m.startsWith("hello")) {
            r.add("Salut 😊 ! Je vais bien, merci. Et toi, comment se passe ta journée ?");
            r.add("Coucou 😊, ça me fait plaisir d'avoir de tes nouvelles. Comment tu te sens aujourd'hui ?");
            r.add("Je vais bien, merci ! Qu'est-ce que tu aimes faire pendant ton temps libre ?");
        } else if (m.contains("?")) {
            r.add("Merci pour ta question 😊. Et toi, qu'est-ce que tu en penses ?");
            r.add("Je vois ce que tu veux dire. J'aimerais bien comprendre ton point de vue aussi.");
            r.add("C'est intéressant ! Qu'est-ce qui t'a amené à te poser cette question ?");
        } else {
            r.add("Je vois 😊, merci de partager ça avec moi. Comment tu l'as vécu ?");
            r.add("C'est intéressant, j'aime bien découvrir ta façon de voir les choses.");
            r.add("J'aimerais mieux te connaître. Qu'est-ce qui te passionne en ce moment ?");
        }

        if (style.equals("Drôle")) {
            r.set(0,"Ah d'accord 😄 ! Raconte-moi la suite, je suis curieux.");
            r.set(1,"Tu piques ma curiosité là 😂. Comment ça s'est passé ?");
            r.set(2,"Maintenant, il faut que tu m'en dises plus 😄 !");
        } else if (style.equals("Drague légère")) {
            r.set(0,"J'aime bien discuter avec toi 😊. Raconte-moi un peu plus.");
            r.set(1,"Tu as une façon intéressante de voir les choses, et j'aime bien ça.");
            r.set(2,"J'aimerais en apprendre davantage sur toi. Qu'est-ce qui te fait sourire ?");
        } else if (style.equals("Amical")) {
            r.set(0,"Merci de partager ça avec moi 😊. Et toi, comment tu vois les choses ?");
            r.set(1,"Je comprends. C'est sympa de pouvoir en parler avec toi !");
            r.set(2,"Au fait, qu'est-ce que tu aimes faire pendant ton temps libre ?");
        }
        return r;
    }

    void generateReplies() {
        String input = message.getText().toString().trim();
        if(input.isEmpty()) {
            message.setError("Colle d'abord un message reçu");
            return;
        }

        RadioButton selected = findViewById(styles.getCheckedRadioButtonId());
        String style = selected == null ? "Doux (par défaut)" : selected.getText().toString();
        if(style.startsWith("Doux")) style = "Doux";

        results.removeAllViews();
        String[] titles = {"1 · Naturelle","2 · Attentionnée","3 · Pour poursuivre"};

        List<String> replies = makeReplies(input,style);
        for(int i=0;i<replies.size();i++) {
            String reply = replies.get(i);
            LinearLayout card = new LinearLayout(this);
            card.setOrientation(LinearLayout.VERTICAL);
            card.setPadding(dp(13),dp(12),dp(13),dp(12));
            card.setBackground(shape(panel));

            LinearLayout.LayoutParams cp = full();
            cp.topMargin = dp(10);
            results.addView(card,cp);
            card.addView(label(titles[i],13,gold,true));

            TextView body = label(reply,16,white,false);
            body.setPadding(0,dp(8),0,dp(10));
            card.addView(body);

            Button copy = makeButton("Copier cette réponse");
            copy.setOnClickListener(v -> {
                ClipboardManager cm = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setPrimaryClip(ClipData.newPlainText("Steve IA",reply));
                Toast.makeText(this,"Réponse copiée",Toast.LENGTH_SHORT).show();
            });
            card.addView(copy,full());
        }
    }
}
