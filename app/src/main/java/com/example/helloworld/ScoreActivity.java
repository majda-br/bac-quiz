package com.example.helloworld;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helloworld.databinding.ActivityAcceuilBinding;

import java.util.List;

public class ScoreActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textScore;
    private int score;
    private int nbQuestion;
    public String pseudo;
    public float ratio;

    private LinearLayout ll;
    private Button boutonEnregister;
    public TextView tv;

    @SuppressLint("StringFormatInvalid")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        //affectation du LinearLayout
        ll = findViewById(R.id.monLayoutScore);

        //on reçoit l'intent de CarteActivity (score et nombre de question
        Intent intent = getIntent();
        pseudo = intent.getStringExtra("pseudo");
        score = intent.getIntExtra("score", 0);
        nbQuestion = intent.getIntExtra("nbQuestion", 0);
        ratio = (float)score/(float)nbQuestion;


        //on affiche le score de l'utilisateur à l'issu du quiz
        score();

        //on crée un document pour enregistrer des informations pour une exécution future
        //SharedPreferences sp = getSharedPreferences("memScore", Context.MODE_PRIVATE);

        //textView qui va servir à faire le bilan de la performance du joueur par rapport aux autres joueurs
        tv = findViewById(R.id.textViewComparaison);
        ScoreComparison theScoreComparison = new ScoreComparison(this, ratio, pseudo);
        theScoreComparison.execute();
        ajouterBoutonEtEditText();
        tv.setText(theScoreComparison.result);


        /*if (LoginDetails.getSizeTable() != 0) {     //on verifie si le document "memScore.xml" contient quelquechose (cad s'il y a au moins un joueur qui a déjà joué au jeu)
            int ancienScore = 0;
            int ancienNbQuestion = 10;
            String ancienPseudo;
            float ancienRatio;
            ancienScore = LoginDetails.getMaxScore();
            ancienNbQuestion = sp.getInt("nbQuestionPerson", 0);
            ancienPseudo =LoginDetails.getNameMaxScore;
            ancienRatio = (float)ancienScore/(float)ancienNbQuestion;

            if(ratio > ancienRatio){
                tv.setText(String.format(getString(R.string.winnerText), ancienPseudo, ancienScore, ancienNbQuestion, pseudo));
                ajouterBoutonEtEditText();
            }
            else if (ratio == ancienRatio) {
                tv.setText(String.format(getString(R.string.equalScoreText), ancienPseudo, ancienScore, ancienNbQuestion, pseudo));
                ajouterBoutonEtEditText();
            }
            else {
                tv.setText(String.format(getString(R.string.looserText), ancienPseudo, ancienScore, ancienNbQuestion, pseudo));
            }
        }

        else{
            tv.setText(R.string.FirstTry);
            ajouterBoutonEtEditText();
        }*/
    }

    @SuppressLint("StringFormatInvalid")
    public void score(){
        textScore = findViewById(R.id.textViewScore);
        textScore.setText(String.format(getString(R.string.scoreDisplay), score, nbQuestion));
    }

    public void ajouterBoutonEtEditText(){
        /*ajout dynamique de l'EditText
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(930, 150);
        editTextPseudo = new EditText(this);
        editTextPseudo.setLayoutParams(lparams);
        lparams.gravity = Gravity.CENTER;
        editTextPseudo.setHint(R.string.Nickname);
        editTextPseudo.setInputType(InputType.TYPE_CLASS_TEXT);
        editTextPseudo.setTextColor(Color.parseColor("#000000"));
        editTextPseudo.setEms(10);
        ll.addView(editTextPseudo);*/

        //ajout dynamique du bouton
        boutonEnregister = new Button(this);
        boutonEnregister.setOnClickListener(this);
        boutonEnregister.setText(R.string.ButtonSaveScore);
        boutonEnregister.setBackgroundResource(R.drawable.custom_button);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0,30,0,20);
        params.gravity = Gravity.CENTER;
        boutonEnregister.setTextColor(Color.parseColor("#2e86de"));
        boutonEnregister.setLayoutParams(params);
        boutonEnregister.setWidth(1000);
        boutonEnregister.setTypeface(boutonEnregister.getTypeface(), Typeface.BOLD);
        ll.addView(boutonEnregister);
    }

    @Override
    public void onClick(View v) {
        /*on récupère l'input du pseudo
        pseudo = editTextPseudo.getText().toString();

        //on inscrit les infos du nouveau joueur (score, peudo, nombre de questions du quiz auquel il a repondu, ratio) dans le document "memScore.xml"
        SharedPreferences sp = getSharedPreferences("memScore", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("scorePerson", score);
        editor.putInt("nbQuestionPerson", nbQuestion);
        editor.putString("pseudoPerson", pseudo);
        editor.putFloat("ratioPerson", ratio);
        editor.apply();*/

        //On passe à l'activite finale en lui communicant le score et le nombre de questions auquel le joueur a répondu
        Intent transitionToFinActivity = new Intent(this, FinActivity.class);    //on passe à FinActivity
        transitionToFinActivity.putExtra("score", score);     //on passe le score à l'activité finale
        transitionToFinActivity.putExtra("nbQuestion", nbQuestion);
        startActivity(transitionToFinActivity);
        finish();
    }
}

