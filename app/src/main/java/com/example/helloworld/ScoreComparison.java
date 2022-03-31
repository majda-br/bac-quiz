package com.example.helloworld;

import android.os.AsyncTask;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

class ScoreComparison extends AsyncTask<LoginTable, Void, Void> {

    private LoginViewModel loginViewModel;
    int ancienScore;
    int ancienNbQuestion = 10;
    String ancienPseudo;
    float ancienRatio;
    private float ratio;
    private String pseudo;
    public String result;
    private ScoreActivity  theScoreAct;

    public ScoreComparison(ScoreActivity theScoreActivity, float ratio, String pseudo) {

        this.loginViewModel = ViewModelProviders.of(theScoreActivity).get(LoginViewModel.class);
        this.ratio = ratio;
        this.pseudo = pseudo;
        this.theScoreAct = theScoreActivity;

    }

    @Override
    protected Void doInBackground(LoginTable... data) {

        try {
            if (loginViewModel.getSizeTable()!=0){
                loginViewModel.getMaxScore().observe(theScoreAct, new Observer<LoginTable>() {
                    @Override
                    public void onChanged(@Nullable LoginTable data) {
                        ancienScore= data.getScore();
                        ancienPseudo=data.getName();
                    }
                });
                ancienRatio = (float)ancienScore/(float)ancienNbQuestion;

                if(ratio > ancienRatio){
                    result = String.format(theScoreAct.getString(R.string.winnerText), ancienPseudo, ancienScore, ancienNbQuestion, pseudo);
                  //  tv.setText(String.format(getString(R.string.winnerText), ancienPseudo, ancienScore, ancienNbQuestion, pseudo));
                    //ajouterBoutonEtEditText();
                }
                else if (ratio == ancienRatio) {
                    result = String.format(theScoreAct.getString(R.string.equalScoreText), ancienPseudo, ancienScore, ancienNbQuestion, pseudo);
                    // tv.setText(String.format(getString(R.string.equalScoreText), ancienPseudo, ancienScore, ancienNbQuestion, pseudo));
                    //ajouterBoutonEtEditText();
                }
                else {
                  //  tv.setText(String.format(getString(R.string.looserText), ancienPseudo, ancienScore, ancienNbQuestion, pseudo));

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

}