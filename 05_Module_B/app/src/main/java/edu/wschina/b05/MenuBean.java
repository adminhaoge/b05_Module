package edu.wschina.b05;

public class MenuBean {
    private int id;
    private String image_url;
    private String name;
    private int price;
    private int countx;


    public MenuBean(){}

    public MenuBean(int id, String image_url, String name, int price) {
        this.id = id;
        this.image_url = image_url;
        this.name = name;
        this.price = price;
    }

    public int getCountx() {
        return countx;
    }

    public void setCountx(int count) {
        this.countx = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}
