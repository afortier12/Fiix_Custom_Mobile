package ITM.maint.fiix_custom_mobile.data.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "nesting_table")
public class FailureCodeNesting {

    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name="id")
    private Integer id;
    @SerializedName("category")
    @Expose
    private String category;
    /*@SerializedName("source")
    @Expose
    private List<Source> source = null;

    private class Source {

        @SerializedName("asset")
        @Expose
        private String asset;
        @SerializedName("problem")
        @Expose
        private List<Problem> problem = null;
        @SerializedName("cause")
        @Expose
        private List<Cause> cause = null;
        @SerializedName("action")
        @Expose
        private List<Action> action = null;


        public String getAsset() {
            return asset;
        }

        public void setAsset(String asset) {
            this.asset = asset;
        }

        public List<Problem> getProblem() {
            return problem;
        }

        public void setProblem(List<Problem> problem) {
            this.problem = problem;
        }

        public List<Cause> getCause() {
            return cause;
        }

        public void setCause(List<Cause> cause) {
            this.cause = cause;
        }

        public List<Action> getAction() {
            return action;
        }

        public void setAction(List<Action> action) {
            this.action = action;
        }
    }

    private class Problem {

        @SerializedName("code")
        @Expose
        private String code;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

    }

    private class Action {

        @SerializedName("code")
        @Expose
        private String code;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

    }

    private class Cause {

        @SerializedName("code")
        @Expose
        private String code;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

    }*/

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    /*public List<Source> getSource() {
        return source;
    }

    public void setSource(List<Source> source) {
        this.source = source;
    }*/

}
