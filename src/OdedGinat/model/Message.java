package OdedGinat.model;

import java.util.Objects;

public class Message {
    private String text;
    private Boolean error;

    public Message(){
        text = "";
        error = false;
    }

    public Message(String text){
        this.text = text;
        this.error = false;
    }

    public Message(String text, Boolean error) {
        this.text = text;
        this.error = error;
    }

    public Boolean setText(String text) {
        this.text = text;
        return true;
    }

    public Boolean setError(Boolean error) {
        this.error = error;
        return true;
    }

    public String getText() {
        return text;
    }

    public Boolean getError() {
        return error;
    }

    @Override
    public String toString() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(text, message.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, error);
    }
}
