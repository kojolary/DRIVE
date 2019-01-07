package net.vokacom.drive.mData;

public class ProductMen {



    private int id;
    private String title;
    private String short_description;
    private String rating;
    private String price;
    private String image;

    public ProductMen(int id, String title, String short_description, String rating, String price, String image) {
        this.id = id;
        this.title = title;
        this.short_description = short_description;
        this.rating = rating;
        this.price = price;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getshort_description() {
        return short_description;
    }

    public String getRating() {
        return rating;
    }

    public String getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }
}
