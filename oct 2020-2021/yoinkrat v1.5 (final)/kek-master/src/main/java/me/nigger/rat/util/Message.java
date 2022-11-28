package me.nigger.rat.util;

import java.util.ArrayList;
import java.util.List;

public final class Message {
    private final String name;
    private final String description;
    private final List<Field> fields;

    private Message(String name, String description, List<Field> fields) {
        this.name = name;
        this.description = description;
        this.fields = fields;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Field> getFields() {
        return fields;
    }

    public static class Builder {
        private final String name;
        private String description;
        private final List<Field> fields = new ArrayList<>();

        public Builder(String name) {
            this.name = name;
        }

        public Builder addField(String name, String value, boolean inline) {
            fields.add(new Field(name, value, inline));
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Message build() {
            return new Message(name, description, fields);
        }
    }

    public static class Field {
        private final String name;
        private final String value;
        private final boolean inline;

        public Field(String name, String value, boolean inline) {
            this.name = name;
            this.value = value;
            this.inline = inline;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public boolean isInline() {
            return inline;
        }
    }
}
