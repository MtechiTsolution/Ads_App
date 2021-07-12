package mtech.pak.tajrabaadds;

public class Showuserdata {
    String name,countery,images,earn;

    public Showuserdata(String name, String countery, String images, String earn) {
        this.name = name;
        this.countery = countery;
        this.images = images;
        this.earn = earn;
    }
    public Showuserdata()
    {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountery() {
        return countery;
    }

    public void setCountery(String countery) {
        this.countery = countery;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getEarn() {
        return earn;
    }

    public void setEarn(String earn) {
        this.earn = earn;
    }
}
