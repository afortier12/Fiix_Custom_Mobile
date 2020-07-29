package ITM.maint.fiix_custom_mobile.data.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.checkerframework.common.aliasing.qual.Unique;

import java.util.List;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.NO_ACTION;

@Entity(tableName = "nesting_table",
        indices = {@Index(value = {"id"}, unique = true),
                @Index(value = {"source_id"})},
        foreignKeys = @ForeignKey(entity = Source.class,
                parentColumns = "id",
                childColumns = "source_id",
                onDelete = CASCADE))
public class FailureCodeNesting {

    //{
    //  { "name":"Battery", "source_name":"Battery-Lithium Ion"},
    //  { "name":"Battery", "source_name":"Battery-Lead Acid"},
    //  ...
    //}

    @PrimaryKey
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
    @ColumnInfo(name="source_id")
    private int source_id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSource_id() {
        return source_id;
    }

    public void setSource_id(int source_id) {
        this.source_id = source_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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


    public static class FailureCodeNestingJoinSource {
        @Embedded
        private FailureCodeNesting failureCodeNesting;
        @Relation(
                parentColumn = "source_id",
                entityColumn = "id",
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
