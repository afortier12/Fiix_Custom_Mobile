package ITM.maint.fiix_custom_mobile.data.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
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
    private List<Category> category = null;

        public class Category {

            @SerializedName("name")
            @Expose
            private String name;
            @Embedded
            private List<Source> source = null;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<Source> getSource() {
                return source;
            }

            public void setSource(List<Source> source) {
                this.source = source;
            }

        }

        public class Source {

            @SerializedName("name")
            @Expose
            private String name;
            @Embedded
            private List<Problem> problem = null;
            @Embedded
            private List<Cause> cause = null;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
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

        }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }


}
