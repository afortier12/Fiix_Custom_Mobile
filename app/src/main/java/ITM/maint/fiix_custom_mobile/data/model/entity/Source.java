package ITM.maint.fiix_custom_mobile.data.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.NO_ACTION;

@Entity(tableName = "rca_source_table",
        indices = {@Index(value = {"id"}, unique = true),
                @Index(value = {"problem_id"}),
                @Index(value = {"cause_id"})},
        foreignKeys = {@ForeignKey(entity = Problem.class,
                parentColumns = "id",
                childColumns = "problem_id",
                onDelete = NO_ACTION),
                @ForeignKey(entity = Cause.class,
                        parentColumns = "id",
                        childColumns = "cause_id",
                        onDelete = NO_ACTION)})
public class Source {

    //{
    //  { "name":"Battery-Lead Acid", "problem_id":"22593", "cause_id":"21563"},
    //  { "name":"Battery-Lead Acid", "problem_id":"22498", "cause_id":"21567"},
    //  ...
    //}


    @PrimaryKey
    @NonNull
    @ColumnInfo(name="id")
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("problem")
    @Expose
    private int problem_id;
    @SerializedName("cause")
    @Expose
    private int cause_id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProblem_id() {
        return problem_id;
    }

    public void setProblem_id(int problem_id) {
        this.problem_id = problem_id;
    }

    public int getCause_id() {
        return cause_id;
    }

    public void setCause_id(int cause_id) {
        this.cause_id = cause_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
