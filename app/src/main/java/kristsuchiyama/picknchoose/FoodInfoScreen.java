package kristsuchiyama.picknchoose;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FoodInfoScreen extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String choice;
    Vector<Restaurant> listRestaurants = new Vector<Restaurant>();
    private FusedLocationProviderClient fusedLocationClient;
    double userLat;
    double userLon;
    final MyAdapter myAdapter = new MyAdapter(FoodInfoScreen.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_info_screen);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //final MyAdapter myAdapter = new MyAdapter(FoodInfoScreen.this);
        recyclerView.setAdapter(myAdapter);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            userLat = location.getLatitude();
                            userLon = location.getLongitude();

                            setChoice();
                            final OkHttpClient client = new OkHttpClient();
                            String urlString = "https://api.yelp.com/v3/businesses/search?term=" + choice + "&latitude=" + userLat + "&longitude=" + userLon;
                            final Request request = new Request.Builder()
                                    .url(urlString)
                                    .get()
                                    .addHeader("Authorization", "Bearer Nt8wOqPHj5ub_iv7drgGZvFNPSURfqWJw5lW9Bxxt_99reshEi6kqPTbrN77UYBe_9KakY8zP6qmmc6gJuJ58C8joR3nRIt4lICAejSNpDF6tjvPwn9K016t94kFXXYx")
                                    .addHeader("User-Agent", "PostmanRuntime/7.15.0")
                                    .addHeader("Accept", "*/*")
                                    .addHeader("Cache-Control", "no-cache")
                                    .addHeader("Postman-Token", "03864e55-be5e-4b01-988e-137ce097b2fe,c0dc105d-a720-4c79-89ab-81ef0a0bef7a")
                                    .addHeader("Host", "api.yelp.com")
                                    .addHeader("Connection", "keep-alive")
                                    .addHeader("cache-control", "no-cache")
                                    .build();

                            @SuppressLint("StaticFieldLeak") AsyncTask<Void,Void,String> asyncTask = new AsyncTask<Void, Void, String>() {
                                @SuppressLint("WrongThread")
                                @Override
                                protected String doInBackground(Void... params) {
                                    try{
                                        Response response = client.newCall(request).execute();
                                        if(!response.isSuccessful()){
                                            return null;
                                        }
                                        return response.body().string();
                                    }catch(Exception e){
                                        e.printStackTrace();
                                        return null;
                                    }
                                }
                                @Override
                                protected void onPostExecute(String s) {
                                    super.onPostExecute(s);
                                    if (s != null) {
                                        parseYelp(s);
                                        //myAdapter.setItems(listRestaurants);
                                        sortByRating(myAdapter);
                                    }
                                }
                            };
                            asyncTask.execute();
                        }
                    }
                });

        Spinner sortOptions = (Spinner) findViewById(R.id.Sort_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Sort_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortOptions.setAdapter(adapter);
        sortOptions.setOnItemSelectedListener(this);

        final Button backBtn = (Button) findViewById(R.id.btoSelection);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setChoice();
        final TextView displayChoice = (TextView) findViewById(R.id.displayFoodChoiceFNM);
        displayChoice.setText(choice);

    }
    public void setChoice(){
        choice = getIntent().getStringExtra("foodChoice");
    }
    public void parseYelp(String s){
        //String finalOutput = "";
        for(int i = 0; i < s.length() - 8; i++){
            //Find name
            if(s.substring(i,i+8).equals("\"name\": ")){
                i+=8;
                i++;
                int b = i;
                while(!s.substring(i,i+1).equals("\"")){
                    if(s.substring(i,i+1).equals("\\")){
                        s = s.substring(0,i) + s.substring(i+6,s.length());
                        --i;
                    }
                    ++i;
                }
                Restaurant newRest = new Restaurant();
                listRestaurants.addElement(newRest);
                newRest.setName(s.substring(b,i));


                //Find image url
                for(int a = i; a<s.length()-13; ++a){
                    if(s.substring(a,a+13).equals("\"image_url\": ")){
                        a+=13;
                        a++;
                        int c = a;
                        while(!s.substring(a,a+1).equals("\"")){
                            a++;
                        }
                        newRest.setPictureURL(s.substring(c,a));
                        break;
                    }
                }

                //Find number of reviews
                for(int a = i; a<s.length()-16; a++){
                    if(s.substring(a,a+16).equals("\"review_count\": ")){
                        a+=16;
                        int c = a;
                        while(!s.substring(a,a+1).equals(",")){
                            a++;
                        }
                        double num = Double.parseDouble(s.substring(c,a));
                        newRest.setReviewNum(num);
                        //finalOutput += "Number of Reviews: " + s.substring(c,a);
                        //finalOutput += "\n";
                        break;
                    }
                }
                //Find rating
                for(int a = i; a<s.length()-10; ++a){
                    if(s.substring(a,a+10).equals("\"rating\": ")){
                        a+=10;
                        int c = a;
                        while(!s.substring(a,a+1).equals(",")){
                            a++;
                        }
                        double rat = Double.parseDouble(s.substring(c,a));
                        newRest.setRating(rat);
                        //finalOutput += "Rating: " + s.substring(c,a);
                        //finalOutput += "\n";
                        break;
                    }
                }
                //Find pricing
                for(int a = i; a<s.length()-9; ++a){
                    if(s.substring(a,a+9).equals("\"price\": ")){
                        a+=9;
                        a++;
                        int c = a;
                        while(!s.substring(a,a+1).equals("\"")){
                            a++;
                        }
                        newRest.setPrice(s.substring(c,a));
                        //finalOutput += "Price: " + s.substring(c,a);
                        //finalOutput += "\n";
                        break;
                    }
                }
                //Find address
                for(int a = i; a<s.length()-12; ++a){
                    if(s.substring(a,a+12).equals("\"address1\": ")){
                        a+=12;
                        a++;
                        int c = a;
                        while(!s.substring(a,a+1).equals("\"")){
                            a++;
                        }
                        newRest.setAddress(s.substring(c,a));
                        //finalOutput += "Address: " + s.substring(c,a);
                        //finalOutput += "\n";
                        break;
                    }
                }
                //Find distance
                for(int a = i; a<s.length()-12; ++a){
                    if(s.substring(a,a+12).equals("\"distance\": ")){
                        a+=12;
                        int c = a;
                        while(!s.substring(a,a+1).equals("}")){
                            a++;
                        }
                        double distNum = Double.parseDouble(s.substring(c,a));
                        distNum = distNum/1609.344;
                        BigDecimal dist = new BigDecimal(distNum).setScale(2, RoundingMode.HALF_UP);
                        double distMiles = dist.doubleValue();
                        newRest.setDistance(distMiles);
                        //finalOutput += "Distance: " + distMiles + " miles away";
                        //finalOutput += "\n\n";
                        break;
                    }
                }
                //Find Yelp url
                for(int a = i; a<s.length()-7; ++a){
                    if(s.substring(a,a+7).equals("\"url\": ")){
                        a+=7;
                        a++;
                        int c = a;
                        while(!s.substring(a,a+1).equals("\"")){
                            a++;
                        }
                        newRest.setUrl(s.substring(c,a));
                        break;
                    }
                }

            }
        }
        //return finalOutput;
    }
    public void sortByDistance(MyAdapter ad){
        for(int i = 0; i < listRestaurants.size()-1; ++i){
            for(int a = i+1; a < listRestaurants.size(); ++a){
                if(listRestaurants.elementAt(a).getDistance() < listRestaurants.elementAt(i).getDistance()){
                    Restaurant temp = new Restaurant();
                    temp = listRestaurants.elementAt(i);
                    listRestaurants.set(i,listRestaurants.elementAt(a));
                    listRestaurants.set(a,temp);
                }
            }
        }
        ad.setItems(listRestaurants);
    }
    public void sortByRating(MyAdapter ad){
        for(int i = 0; i < listRestaurants.size()-1; ++i){
            for(int a = i+1; a < listRestaurants.size(); ++a){
                if(listRestaurants.elementAt(a).getRating() > listRestaurants.elementAt(i).getRating()){
                    Restaurant temp = new Restaurant();
                    temp = listRestaurants.elementAt(i);
                    listRestaurants.set(i,listRestaurants.elementAt(a));
                    listRestaurants.set(a,temp);
                }
            }
        }
        ad.setItems(listRestaurants);
    }
    public void sortByReviewNum(MyAdapter ad){
        for(int i = 0; i < listRestaurants.size()-1; ++i){
            for(int a = i+1; a < listRestaurants.size(); ++a){
                if(listRestaurants.elementAt(a).getReviewNumber() > listRestaurants.elementAt(i).getReviewNumber()){
                    Restaurant temp = new Restaurant();
                    temp = listRestaurants.elementAt(i);
                    listRestaurants.set(i,listRestaurants.elementAt(a));
                    listRestaurants.set(a,temp);
                }
            }
        }
        ad.setItems(listRestaurants);
    }
    public void sortByPrice(TextView t){
        for(int i = 0; i < listRestaurants.size()-1; ++i){
            for(int a = i+1; a < listRestaurants.size(); ++a){
                if(listRestaurants.elementAt(a).getPrice().length() < listRestaurants.elementAt(i).getPrice().length()){
                    Restaurant temp = new Restaurant();
                    temp = listRestaurants.elementAt(i);
                    listRestaurants.set(i,listRestaurants.elementAt(a));
                    listRestaurants.set(a,temp);
                }
            }
        }
        displayList(t);
    }
    public void displayList(TextView t){
        t.setText("");
        for(int i = 0; i < listRestaurants.size(); ++i){
            String s = "";
            s += listRestaurants.elementAt(i).getName() + "\n";
            s += (int) listRestaurants.elementAt(i).getReviewNumber() + " Reviews\n";
            s += "Rating: " + listRestaurants.elementAt(i).getRating() + "\n";
            s += "Price: " + listRestaurants.elementAt(i).getPrice() + "\n";
            s += listRestaurants.elementAt(i).getAddress() + "\n";
            s += listRestaurants.elementAt(i).getDistance() + " Miles Away\n";

            t.append(s);
            String infoString = "More Info\n\n";
            SpannableString ss = new SpannableString(infoString);
            final int finalI = i;
            ClickableSpan moreInfoLink = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    //Toast.makeText(FoodInfoScreen.this, "Works", Toast.LENGTH_SHORT).show();
                    Intent yelpBrowser = new Intent(Intent.ACTION_VIEW);
                    yelpBrowser.setData(Uri.parse(listRestaurants.elementAt(finalI).getUrl()));
                    startActivity(yelpBrowser);
                }
            };
            ss.setSpan(moreInfoLink,0,9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            t.append(ss);
        }
        t.setMovementMethod(LinkMovementMethod.getInstance());
    }
    //Selectors for Spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selection = parent.getItemAtPosition(position).toString();
        /*final RecyclerView recyclerView2 = (RecyclerView) findViewById(R.id.recView);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        final MyAdapter myAd = new MyAdapter(FoodInfoScreen.this);
        recyclerView2.setAdapter(myAd); */
        switch (selection){
            case "Highest Rated":
                sortByRating(myAdapter);
                break;
            case "Closest to You":
                sortByDistance(myAdapter);
                break;
            case "Most Reviewed":
                sortByReviewNum(myAdapter);
                break;
            default:
                sortByRating(myAdapter);
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
