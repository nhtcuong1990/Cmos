package newnote.cmos.POCO;

/**
 * Created by JIN on 1/19/2016.
 */
public class Setting {
    public Setting() {
    }

    public Setting(int id, byte[] image, String name) {
        Id = id;
        Image = image;
        Name = name;
    }

    public int Id;
    public String Name;
    public byte[] Image;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public byte[] getImage() {
        return Image;
    }

    public void setImage(byte[] image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
