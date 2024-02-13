package program.Domain;


public interface Observer { //the observer interface
    void update(Subject subject, Object ob);
    void updateCreation(Subject subject);
}
