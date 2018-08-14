package tina.com.common.http.model;

/**
 * @author Ragnar
 * @date 2018/5/18 上午11:27
 */
public class TagModel extends BaseModel {

    private int id;
    private String name;

    public TagModel() {
    }

    public TagModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}