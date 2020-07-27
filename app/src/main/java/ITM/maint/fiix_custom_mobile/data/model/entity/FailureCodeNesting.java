package ITM.maint.fiix_custom_mobile.data.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;

import ITM.maint.fiix_custom_mobile.data.model.Converters.Converters;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "nesting_table")

public class FailureCodeNesting {

    @PrimaryKey(autoGenerate = true)
    @Expose
    @NonNull
    @ColumnInfo(name="id")
    private int id;

    @SerializedName("category")
    @Expose
    @Embedded(prefix="cat_")
    @TypeConverters(Converters.class)
    private ArrayList<Category> category = null;

    public ArrayList<Category> getCategory() {
        return category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCategory(ArrayList<Category> category) {
        this.category = category;
    }

        @Entity(tableName = "rca_categories_table")
        public class Category {

            @PrimaryKey(autoGenerate = true)
            @NonNull
            @ColumnInfo(name="id")
            private int id;
            @SerializedName("name")
            @Expose
            @ColumnInfo(name="name")
            private String name;
            @SerializedName("source")
            @Expose
            @Embedded(prefix="source_")
            @TypeConverters(Converters.class)
            private ArrayList<Source> source = null;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public ArrayList<Source> getSource() {
                return source;
            }

            public void setSource(ArrayList<Source> source) {
                this.source = source;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }

        @Entity(tableName = "rca_source_table")
        public class Source {

            @PrimaryKey
            @NonNull
            @SerializedName("name")
            @Expose
            @ColumnInfo(name="name")
            private String name;
            @SerializedName("problem")
            @Expose
            @Embedded(prefix="problem_")
            @TypeConverters(Converters.class)
            private ArrayList<Problem> problem = null;
            @SerializedName("cause")
            @Expose
            @Embedded(prefix="cause_")
            @TypeConverters(Converters.class)
            private ArrayList<Cause> cause = null;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public ArrayList<Problem> getProblem() {
                return problem;
            }

            public void setProblem(ArrayList<Problem> problem) {
                this.problem = problem;
            }

            public ArrayList<Cause> getCause() {
                return cause;
            }

            public void setCause(ArrayList<Cause> cause) {
                this.cause = cause;
            }
        }

}
