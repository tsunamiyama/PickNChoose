package kristsuchiyama.picknchoose;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.Random;


public class HenryAnimation extends AppCompatActivity {
    VideoView vid;
    String menuChoice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_henry_animation);
        /*View someView = findViewById(R.id.henryAnimation);
        View root = someView.getRootView();
        root.setBackgroundColor(Color.WHITE); */
        menuChoice = getIntent().getStringExtra("menuChoice");
        vid = (VideoView) findViewById(R.id.henryAnimation);
        playVideo(vid);
    }
    public void playVideo(View v) {
        final TextView theFoodChoice = (TextView) findViewById(R.id.FoodChoice);
        switch(menuChoice) {
            case "cuisine":
                theFoodChoice.setText(getCuisine());
                break;
            case "food":
                theFoodChoice.setText(getFood());
                break;
            case "drink":
                theFoodChoice.setText(getDrink());
                break;
            default:
                theFoodChoice.setText(menuChoice);
        }
        theFoodChoice.setVisibility(View.GONE);

        String path = "android.resource://" + getPackageName() + "/" + R.raw.henryfancyrestraunt;
        Uri u = Uri.parse(path);
        vid.setVideoURI(u);
        vid.start();

        final Button returnBtn = (Button) findViewById(R.id.ReturnHomeFromCuisine);
        //returnBtn.setBackgroundColor(Color.TRANSPARENT);
        //returnBtn.setTextColor(Color.TRANSPARENT);
        final Button infoBtn = (Button) findViewById(R.id.FindNearMeButton);
        //infoBtn.setBackgroundColor(Color.TRANSPARENT);
        //infoBtn.setTextColor(Color.TRANSPARENT);
        final ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.answerLayout);
        returnBtn.setVisibility(View.INVISIBLE);
        infoBtn.setVisibility(View.INVISIBLE);
        layout.setVisibility(View.INVISIBLE);

        vid.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer) {
                vid.stopPlayback();
                layout.setVisibility(View.VISIBLE);
                theFoodChoice.setVisibility(View.VISIBLE);
                returnBtn.setVisibility(View.VISIBLE);
                infoBtn.setVisibility(View.VISIBLE);
                //returnBtn.setTextColor(Color.parseColor("#223500"));
                //infoBtn.setTextColor(Color.parseColor("#223500"));
                //Create return to home "button"
                returnBtn.setOnClickListener(new View.OnClickListener()
                {
                    public void onClick(View v){
                        finish();
                    }
                });
                //Create InfoButton
                infoBtn.setOnClickListener(new View.OnClickListener()
                {
                    public void onClick(View v){
                        startActivity(new Intent(HenryAnimation.this , FoodInfoScreen.class).putExtra("foodChoice",theFoodChoice.getText()));
                        vid.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
    }
    public String getCuisine(){
        String cuisineOptions[] = {"Argentine","Cajun","Chinese","French","Filipino","Greek","Indian","Indonesian","Italian","Japanese",
                                "Korean","Mediterranean","Mexican","Middle Eastern","Taiwanese","Thai","Barbecue","Spanish", "Vietnamese", "\0"};
        int listSize = getSize(cuisineOptions);
        Random rand = new Random();
        int foodChoice = rand.nextInt(listSize);
        return cuisineOptions[foodChoice];
    }
    public String getFood(){
        String foodOptions[] = {"Tacos","Burritos","Ramen","Spaghetti","Fried Chicken", "Salad", "Soup", "Lasagna", "Pizza", "Tamales", "Kimchi", "Tofu", "Bulgogi", "Fajitas",
                                "Nachos", "Quesadillas", "Bibimbap", "Tonkatsu", "Wontons", "Curry", "Enchiladas", "Pad Thai", "Onigiri", "Udon", "Spaghetti", "Fish and Chips",
                                "Quiche", "Pho", "Hamburgers", "Mac and Cheese", "Risotto", "Sashimi", "Sushi", "Dim Sum", "Paella", "\0"};
        int listSize = getSize(foodOptions);
        Random rand = new Random();
        int foodChoice = rand.nextInt(listSize);
        return foodOptions[foodChoice];
    }
    public String getDrink(){
        String drinkOptions[] = {"Boba","Water", "Tea", "Coffee", "Soda", "Juice", "Milk", "Alcohol", "Energy Drink", "Milkshake", "Smoothie", "Lemonade", "Hot Chocolate", "\0"};
        int listSize = getSize(drinkOptions);
        Random rand = new Random();
        int drinkChoice = rand.nextInt(listSize);
        return drinkOptions[drinkChoice];
    }
    public int getSize(String arr[]){
        int i = 0;
        while(!arr[i].equals("\0")) {
            ++i;
        }
        //i--; //so the array will never return the last value "\0"
        return i;
    }
}

