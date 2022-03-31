package com.example.helloworld;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.helloworld.databinding.ActivityAcceuilBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;
import java.util.Vector;

public class AccueilActivity extends AppCompatActivity implements Listener {

    private Quiz quiz;
    private LoginViewModel loginViewModel;
    private ActivityAcceuilBinding binding;
    private String pseudo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceuil);

        //on cree un quiz vide
        quiz = new Quiz();

        //on recupère des jeux de questions grace à l'API
        String url = "https://opentdb.com/api.php?amount=10";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        Response.Listener<String> responseListener = new QuizResponseListener();
        Response.ErrorListener responseErrorListener = new QuizResponseErrorListener();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener, responseErrorListener);
        requestQueue.add(stringRequest);


        binding = DataBindingUtil.setContentView(AccueilActivity.this, R.layout.activity_acceuil);

        binding.setClickListener((AccueilActivity) this);

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        loginViewModel.getGetAllData().observe(this, new Observer<List<LoginTable>>() {
            @Override
            public void onChanged(@Nullable List<LoginTable> data) {

                try {
                    binding.lblNameAnswer.setText((Objects.requireNonNull(data).get(0).getName()));
                    binding.lblPasswordAnswer.setText((Objects.requireNonNull(data.get(0).getPassword())));
                    binding.lblScoreAnswer.setText("" + (Objects.requireNonNull(data.get(0).getScore())));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        loginViewModel.getMaxScore().observe(this, new Observer<LoginTable>() {
            @Override
            public void onChanged(@Nullable LoginTable data) {

                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private class QuizResponseListener implements Response.Listener<String> {
        @Override
        public void onResponse(String response) {
            JSONObject jsonObject = null;

            //on serialise le JSON récuperé pour obtenir les informations que nous voulons

            try {
                jsonObject = new JSONObject(response);

                for(int j=0;j<10;j++){
                    JSONObject jsonSubObject = jsonObject.getJSONArray("results").getJSONObject(j);

                    String question = String.valueOf(Html.fromHtml(jsonSubObject.getString("question")));

                    String reponseCorrecte = String.valueOf(Html.fromHtml(jsonSubObject.getString("correct_answer")));
                    Vector<String> propositionsIncorrectes = new Vector<String>();
                    JSONArray arr = jsonSubObject.getJSONArray("incorrect_answers");
                    int size = arr.length();
                    for(int i=0;i<size;i++)
                    {
                        String element = String.valueOf(Html.fromHtml(arr.get(i).toString()));
                        propositionsIncorrectes.add(element);
                    }

                    quiz.ajouterCarte(new Carte(question, reponseCorrecte, propositionsIncorrectes));

                }

            } catch (JSONException e) {
                //Toast.makeText(getApplicationContext(), "error 1", Toast.LENGTH_SHORT).show();
                creerQuizDefaut();
            }

        }
    }

    private class QuizResponseErrorListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            //Toast.makeText(getApplicationContext(), "error 2", Toast.LENGTH_SHORT).show();
            creerQuizDefaut();

        }
    }

    public void creerQuizDefaut(){

        Vector<String> propositionsIncorrectes = new Vector<String>();
        propositionsIncorrectes.add("x");
        propositionsIncorrectes.add("-x");
        propositionsIncorrectes.add("x^2");
        quiz.ajouterCarte(new Carte(
                "Que vaut la valeur absolue de x ?",
                "max(-x, x)",
                propositionsIncorrectes));

        propositionsIncorrectes = new Vector<String>();
        propositionsIncorrectes.add("faux");
        quiz.ajouterCarte(new Carte(
                "Si la suite (un) n'est pas minorée alors elle est majorée.",
                "vrai",
                propositionsIncorrectes));

        propositionsIncorrectes = new Vector<String>();
        propositionsIncorrectes.add("vrai");
        quiz.ajouterCarte(new Carte(
                "Si la suite (un) est bornée alors (un) converge.",
                "faux",
                propositionsIncorrectes));

        propositionsIncorrectes = new Vector<String>();
        propositionsIncorrectes.add("ils peuvent être réels");
        propositionsIncorrectes.add("ils peuvent être imaginaires purs");
        propositionsIncorrectes.add("on peut les écrire sous forme exponentielle");
        propositionsIncorrectes.add("on peut les écrire sous forme trigonométrique");
        propositionsIncorrectes.add("|z|^2 = a^2 + b^2 avec z= a + i*b");
        propositionsIncorrectes.add("exp(i*n*x) = cos(n*x) + i*sin(n*x)");
        propositionsIncorrectes.add("exp(i*pi) = -1");
        propositionsIncorrectes.add("exp(i*pi/2) = i");
        quiz.ajouterCarte(new Carte(
                "Laquelle de ces affirmations sur les nombres complexes est fausse ?",
                "leur module est égal à 1",
                propositionsIncorrectes));

        propositionsIncorrectes = new Vector<String>();
        propositionsIncorrectes.add("vrai");
        quiz.ajouterCarte(new Carte(
                "Une courbe peut intercepter son asymptote.",
                "faux",
                propositionsIncorrectes));

        propositionsIncorrectes = new Vector<String>();
        propositionsIncorrectes.add("rien ou un point");
        propositionsIncorrectes.add("un point");
        propositionsIncorrectes.add("un point ou deux points");
        quiz.ajouterCarte(new Carte(
                "L'intersection entre un cerle et une droite peut être...",
                "rien ou un point ou deux points",
                propositionsIncorrectes));

        propositionsIncorrectes = new Vector<String>();
        propositionsIncorrectes.add("2*pi*r");
        propositionsIncorrectes.add("pi*r");
        propositionsIncorrectes.add("2*pi*r^2");
        quiz.ajouterCarte(new Carte(
                "L'aire d'un cercle est...",
                "pi*r^2",
                propositionsIncorrectes));

        propositionsIncorrectes = new Vector<String>();
        propositionsIncorrectes.add("b^2/2*a");
        propositionsIncorrectes.add("4*b^2 - a*c");
        quiz.ajouterCarte(new Carte(
                "Le discriminant d'un polynome du second degré est...",
                "b^2 - 4*a*c",
                propositionsIncorrectes));

        propositionsIncorrectes = new Vector<String>();
        propositionsIncorrectes.add("P(A)*P(B)");
        propositionsIncorrectes.add("P(A et B)/P(A)");
        propositionsIncorrectes.add("P(B)+P(A)");
        quiz.ajouterCarte(new Carte(
                "Soit A et B deux évènements, P(A | B) =",
                "P(A et B)/P(B)",
                propositionsIncorrectes));

        propositionsIncorrectes = new Vector<String>();
        propositionsIncorrectes.add("un vecteur de D et parallele à un vecteur de G");
        propositionsIncorrectes.add("Le vecteur normal de D est perpendiculaire à un vecteur de G");
        propositionsIncorrectes.add("Le vecteur normal de D est perpendiculaire à deux vecteurs de G");
        quiz.ajouterCarte(new Carte(
                "Deux plans D et G sont paralléles si et seulement si",
                "Le vecteur normal de D est parallele au vecteur normal de G",
                propositionsIncorrectes));
    }



    @Override
    public void onClick(View view) {

        String strName = binding.txtName.getText().toString().trim();
        String strPassword = binding.txtPassword.getText().toString().trim();

        LoginTable data = new LoginTable();

        if (TextUtils.isEmpty(strName)) {
            binding.txtName.setError("Please Enter Your Name");
        }
        else if (TextUtils.isEmpty(strPassword)) {
            binding.txtPassword.setError("Please Enter Your Password");
        }
        else {

            data.setName(strName);
            data.setPassword(strPassword);
            data.setScore(0);
            loginViewModel.insert(data);

            EditText editTextPseudo = findViewById(R.id.txtName);
            pseudo = editTextPseudo.getText().toString();
            Intent transitionToScoreActivity = new Intent(AccueilActivity.this, CarteActivity.class);
            transitionToScoreActivity.putExtra("quiz", quiz);     //on passe le quiz à la "CarteActivity"
            transitionToScoreActivity.putExtra("pseudo", pseudo); //on passe le pseudo à la "CarteActivity"
            startActivity(transitionToScoreActivity);
            finish();

        }

    }
}
