package gauffre.moteur;

import gauffre.Coords;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

class History {

    private List<Coords> history;
    private ListIterator<Coords> current;

    History() {
        this.history = new LinkedList<>();
        this.current = history.listIterator();
    }

    List<Coords> annuler() {
        List<Coords> undoList = new LinkedList<>();
        if (current.hasPrevious()) {
            undoList.add(current.previous());
            int next = 0;
            while (current.hasPrevious()) {
                undoList.add(current.previous());
                next++;
            }
            for (int j = 0; j < next; j++) {
                current.next();
            }
        }

        return undoList;
    }

    List<Coords> refaire() {
        List<Coords> redoList = new LinkedList<>();
        if (current.hasNext()) {
            redoList.add(current.next());
        }
        return redoList;
    }

    void add(Coords coords) {
        while (current.hasNext()) {
            current.next();
            current.remove();
        }

        current.add(coords);
    }
}
