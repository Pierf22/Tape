package program.Domain;

import java.util.Objects;

public class Message  { //the message class
    private long id;
    public String sender;
    public String text;
    public Long idConv;
    public void setId(long id) {
        this.id = id;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return id == message.id;
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getSender() {
        return sender;
    }
    public String getText() {
        return text;
    }
    public Long getIdConv() {
        return idConv;
    }
    public void setIdConv(long idConv) {
        this.idConv =idConv;
    }
    public Long getId() {
        return id;
    }
}
