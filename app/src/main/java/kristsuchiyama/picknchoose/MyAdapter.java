package kristsuchiyama.picknchoose;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

class MyAdapter extends RecyclerView.Adapter{
    private Vector<Restaurant> mItems = new Vector<>();
    private Context context;

    public MyAdapter(Context context) {
        this.context = context;
    }
    public void setItems(Vector<Restaurant> items){
        mItems = items;
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if(viewType == 0){
            return TextViewHolder.inflate(viewGroup);
        }
        else{
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof TextViewHolder){
            ((TextViewHolder) viewHolder).bind(mItems.elementAt(position).getName(),mItems.elementAt(position).getRating(),mItems.elementAt(position).displayRestaurant(),mItems.elementAt(position).getPictureURL(), mItems.elementAt(position).getUrl(), context);
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        return 0;
    }

    static class TextViewHolder extends RecyclerView.ViewHolder{
        private TextView RestaurantNameTV;
        private TextView RestaurantInfoTV;
        private ImageView RestaurantPictureIV;
        private ImageView RestaurantRatingIM;
        private Button yelpButtonV;

        public static TextViewHolder inflate(ViewGroup parent){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_card_item,parent,false);
            return new TextViewHolder(view);
        }
        public TextViewHolder(View itemView){
            super(itemView);
           RestaurantNameTV = (TextView) itemView.findViewById(R.id.RestaurantName);
           RestaurantInfoTV = (TextView) itemView.findViewById(R.id.RestaurantInfo);
           RestaurantPictureIV = (ImageView) itemView.findViewById(R.id.RestaurantPicture);
           RestaurantRatingIM = (ImageView) itemView.findViewById(R.id.starRating);
           yelpButtonV = (Button) itemView.findViewById(R.id.YelpButton);
        }
        public void bind(String name, double rating, String info, String pictureURL, final String url, final Context con){
            RestaurantNameTV.setText(name);
            RestaurantInfoTV.setText(info);
            getStars(rating,RestaurantRatingIM);
            if(!pictureURL.equals("")) {
                Picasso.get().load(pictureURL).resize(375, 500).centerCrop().into(RestaurantPictureIV);
            }
            View.OnClickListener Listener = new View.OnClickListener(){
                public void onClick(View v){
                    Intent yelpBrowser = new Intent(Intent.ACTION_VIEW);
                    yelpBrowser.setData(Uri.parse(url));
                    con.startActivity(yelpBrowser);
                }
            };
            yelpButtonV.setOnClickListener(Listener);
        }
    }
    public static void getStars(double rating, ImageView imgV){
        if(rating == 1.0){  imgV.setImageResource(R.drawable.stars_small_1); }
        else {
            if(rating == 1.5){ imgV.setImageResource(R.drawable.stars_small_1_half); }
            else {
                if(rating == 2.0){ imgV.setImageResource(R.drawable.stars_small_2); }
                else {
                    if(rating == 2.5){ imgV.setImageResource(R.drawable.stars_small_2_half); }
                    else {
                        if(rating == 3.0){ imgV.setImageResource(R.drawable.stars_small_3); }
                        else {
                            if(rating == 3.5){ imgV.setImageResource(R.drawable.stars_small_3_half); }
                            else {
                                if(rating == 4.0){ imgV.setImageResource(R.drawable.stars_small_4); }
                                else {
                                    if(rating == 4.5){ imgV.setImageResource(R.drawable.stars_small_4_half); }
                                    else {
                                        if(rating == 5.0){ imgV.setImageResource(R.drawable.stars_small_5); }
                                        else {
                                            imgV.setImageResource(R.drawable.stars_small_0);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
