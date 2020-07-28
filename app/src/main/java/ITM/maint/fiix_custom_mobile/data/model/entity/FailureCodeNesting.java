package ITM.maint.fiix_custom_mobile.data.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.Relation;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.checkerframework.checker.units.qual.C;
import org.checkerframework.common.aliasing.qual.Unique;

import java.util.ArrayList;
import java.util.List;

import ITM.maint.fiix_custom_mobile.data.model.Converters.Converters;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.NO_ACTION;

@Entity(tableName = "nesting_table",
        indices = {@Index(value = {"id"}, unique = true),
                @Index(value = {"source_name"})},
        foreignKeys = @ForeignKey(entity = FailureCodeNesting.Source.class,
                parentColumns = "name",
                childColumns = "source_name",
                onDelete = CASCADE))
public class FailureCodeNesting {

    //{
    //  { "name":"Battery", "source_name":"Battery-Lithium Ion"},
    //  { "name":"Battery", "source_name":"Battery-Lead Acid"},
    //  ...
    //}

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
    @Unique
    @ColumnInfo(name="source_name")
    private String source_name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource_name() {
        return source_name;
    }

    public void setSource_name(String source_name) {
        this.source_name = source_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Entity(tableName = "rca_source_table",
            indices = {@Index(value = {"id"}, unique = true),
                    @Index(value = {"name"}, unique = true),
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


        @PrimaryKey(autoGenerate = true)
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

    public static class FailureCodeNestingJoinSource {
        @Embedded
        private FailureCodeNesting failureCodeNesting;
        @Relation(
                parentColumn = "source_name",
                entityColumn = "source_name",
                entity = Source.class
        )
        private List<Source> sourceList;

        public FailureCodeNesting getFailureCodeNesting() {
            return failureCodeNesting;
        }

        public void setFailureCodeNesting(FailureCodeNesting failureCodeNesting) {
            this.failureCodeNesting = failureCodeNesting;
        }

        public List<Source> getSourceList() {
            return sourceList;
        }

        public void setSourceList(List<Source> sourceList) {
            this.sourceList = sourceList;
        }
    }

    public static class SourceJoinProblemCause {
        @Embedded
        private Source source;
        @Relation(
                parentColumn = "problem",
                entityColumn = "id",
                entity = Problem.class
        )
        private List<Problem> problemList;
        @Relation(
                parentColumn = "cause",
                entityColumn = "id",
                entity = Cause.class
        )
        private List<Cause> causeList;

        public Source getSource() {
            return source;
        }

        public void setSource(Source source) {
            this.source = source;
        }

        public List<Problem> getProblemList() {
            return problemList;
        }

        public void setProblemList(List<Problem> problemList) {
            this.problemList = problemList;
        }

        public List<Cause> getCauseList() {
            return causeList;
        }

        public void setCauseList(List<Cause> causeList) {
            this.causeList = causeList;
        }
    }


}
