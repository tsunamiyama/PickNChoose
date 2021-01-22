package kristsuchiyama.picknchoose;

public class Restaurant {
    private String name;
    private double reviewNum;
    private double rating;
    private String price;
    private String address;
    private double distance;
    private String url;
    private String pictureURL;

    public void Restaurant(){
        name = "";
        reviewNum = 0;
        rating = 0;
        price = "";
        address = "";
        distance = 0;
        url = "";
        pictureURL = "";
    }
    public void setName(String s){name = s;}
    public void setRating(double s){rating = s;}
    public void setAddress(String s){address = s;}
    public void setReviewNum(double s){reviewNum = s;}
    public void setPrice(String s){price = s;}
    public void setDistance(double s){distance = s;}
    public void setUrl(String s){url = s;}
    public void setPictureURL(String s) { pictureURL = s; }
    public String getName(){ return name; }
    public double getReviewNumber(){return reviewNum;}
    public double getRating(){return rating;}
    public String getPrice(){
        if(price != null){
            return price;
        }
        else{
            return "unavailable";
        }
    }
    public String getAddress(){
        if(address.length() >= 6) {
            return address;
        }
        else{
            return "Address Unavailable";
        }
    }
    public double getDistance(){return distance;}
    public String getUrl(){return url;}
    public String getPictureURL(){return pictureURL;}
    public String displayRestaurant(){
        String s = "";
        //s += getName() + "\n";
        //s += "Rating: " + getRating() + "\n";
        s += (int)getReviewNumber() + " Reviews\n";
        s += "Price: " + getPrice() + "\n";
        s += getAddress() + "\n";
        s += getDistance() + " Miles Away";
        return s;
    }
}
