package dragndrop;

public interface DragObserver {

    void started();

    void updated();

    void cancelled();

    void stopped();
}
