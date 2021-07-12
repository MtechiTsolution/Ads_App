package mtech.pak.tajrabaadds;

public class withdrwadata {

    String name,number,id;

    public withdrwadata(String name, String number, String id) {
        this.name = name;
        this.number = number;
        this.id = id;
    }
    public withdrwadata()
    {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
