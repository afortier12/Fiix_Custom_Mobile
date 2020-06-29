package ITM.maint.fiix_custom_mobile.data.api.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class APIError {
    private boolean success;
    private ArrayList<String> messages;

    public static class Builder {
        private boolean success;
        private ArrayList<String> messages;

        public Builder() {}

        public Builder success(final boolean success) {
            this.success = success;
            return this;
        }

        public Builder messages(final ArrayList<String> messages) {
            this.messages = messages;
            return this;
        }

        public Builder defaultError() {
            this.messages.add("Something error");
            return this;
        }

        public APIError build() { return new APIError(this); }
    }

    private APIError(final Builder builder) {
        messages = builder.messages;
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

}
