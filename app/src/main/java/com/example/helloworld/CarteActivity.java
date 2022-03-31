package com.example.helloworld;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;


public class CarteActivity extends AppCompatActivity implements View.OnClickListener {
    private Quiz quiz;    //le quiz (c'est-à-dire l'ensemble des cartes à jouer)
    private Iterator<Carte> carteIterator;     //l'itérateur permettant de savoir où on en est dans le jeu
    private Carte carte;    //la carte à traiter (c'est-à-dire à afficher, jouer et vérifier)
    private Vector<String> toutesLesReponses;     //la liste avec toutes les reponses
    private int score;
    private int nbQuestion;     //nombre de questions du quiz
    private String pseudo;
    private LinearLayout ll;

    //références vers le TextView les boutons
    private TextView question;
    private Button bouton;
    private int nbReponses;

    private MediaPlayer musique;
    private LoginViewModel loginViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carte);

        //affectation du LinearLayout
        ll = findViewById(R.id.monLayoutCarte);

        //musique de fond
        musique = MediaPlayer.create(this, R.raw.entertainer);
        musique.setLooping(true);
        musique.start();

        //on récupère le quiz et on cree l'itérateur
        Intent intent = getIntent();
        quiz = (Quiz) intent.getSerializableExtra("quiz");
        carteIterator = quiz.getCartesIterator();
        pseudo = intent.getStringExtra("pseudo");

        //récupération des références vers le TextView les 4 boutons
        question = findViewById(R.id.Question);

        //on recupere la première carte
        afficherCarte();

        //on calcul le nombre de questions/cartes du quiz pour l'afficher au moment du score
        nbQuestion = quiz.getQuizSize();

        loginViewModel = ViewModelProviders.of(CarteActivity.this).get(LoginViewModel.class);
    }

    @Override
    protected void onPause(){
        super.onPause();
        musique.pause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        musique.setLooping(true);
        musique.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        musique.release();

    }
    public void afficherCarte() {
        //récupération de la prochaine carte
        carte = carteIterator.next();

        //on assigne la question au textView et les réponses aux boutons

        //affichage de la question de la carte
        question.setText(carte.getQuestion());

        //création d'une liste mélangée des propositions de réponses (correcte et incorrectes)
        toutesLesReponses = new Vector<String>();
        for (int i=0; i<carte.getPropositionsIncorrectes().size(); i++){
            toutesLesReponses.add(carte.getPropositionsIncorrectes().get(i));
        }
        toutesLesReponses.add(carte.getReponseCorrecte());    //on concatene les reponses correctes et fausses
        Collections.shuffle(toutesLesReponses);    //on melange la liste de questions

        //affichage d'une proposition par bouton
        nbReponses = toutesLesReponses.size();
        for (int i = 0; i < nbReponses; i++) {
            bouton = new Button(this);
            bouton.setOnClickListener(this);
            bouton.setText(toutesLesReponses.get(i));
            bouton.setBackgroundResource(R.drawable.custom_button);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0,20,0,20);
            params.gravity = Gravity.CENTER;
            bouton.setTextColor(Color.parseColor("#2e86de"));
            bouton.setLayoutParams(params);
            bouton.setWidth(1000);
            bouton.setTypeface(bouton.getTypeface(), Typeface.BOLD);
            ll.addView(bouton);
        }
    }

    public void supprimerBoutons() {
        final int count = ll.getChildCount();

        for (int i = count-1; i >=0 ; i--) {        //on fait attention a ne pas supprimer le TextView de la question dont le texte est juste remplacée à chaque fois
            final View child = ll.getChildAt(i);
            if (child.getId() != question.getId()) {
                ll.removeView(child);
            }
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View v) {
        //on récupère le bouton cliqué
        Button buttonClique = (Button) v;

        //on verifie la réponse puis on affiche la pop-up en fonction de la réponse choisie (si la réponse choisie est correcte ou non)
        String message = buttonClique.getText().toString();
        if (message.equals(carte.getReponseCorrecte())) {
            Toast toast = Toast.makeText(this, R.string.cheering, Toast.LENGTH_SHORT);
            TextView view = (TextView) toast.getView().findViewById(android.R.id.message);
            view.setTextColor(Color.parseColor("#27ae60"));
            view.setTextSize(19);
            toast.getView().setBackgroundTintList((ColorStateList.valueOf(Color.parseColor("#7bed9f"))));
            toast.show();
            score+=1;
            loginViewModel.updateScore(score, pseudo);
        }

        else {
            Toast toast = Toast.makeText(this, R.string.incorrectAnswer, Toast.LENGTH_SHORT);
            TextView view = (TextView) toast.getView().findViewById(android.R.id.message);
            view.setTextColor(Color.parseColor("#c0392b"));
            view.setTextSize(19);
            toast.getView().setBackgroundTintList((ColorStateList.valueOf(Color.parseColor("#ea8685"))));
            toast.show();
        }

        //on supprime les réponses de la question précedente
        supprimerBoutons();

        //s'il reste des cartes, on affiche la suivante
        if (carteIterator.hasNext()) {
            afficherCarte();
        }

        //sinon on arrête la musique, on update le score et on passe à l'activité suivante
        else {
            musique.stop();
            Intent transitionToScoreActivity = new Intent(this, ScoreActivity.class);
            transitionToScoreActivity.putExtra("score", score);     //on passe le score à la deuxième activité
            transitionToScoreActivity.putExtra("nbQuestion", nbQuestion);     //on passe le nombre de questions à la deuxième activité
            transitionToScoreActivity.putExtra("pseudo", pseudo);
            startActivity(transitionToScoreActivity);
            finish();   //une fois le score affiché, il n'est plus possible de revenir en arrière sur la première activité
        }
    }
}