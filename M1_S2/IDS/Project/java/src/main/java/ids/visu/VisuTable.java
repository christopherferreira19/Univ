package ids.visu;

import ids.entity.EntityId;
import ids.listener.Listener;
import ids.msg.Message;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.UUID;

public class VisuTable extends TableView<VisuTable.Entry> implements Listener {

    private final ObservableList<Entry> entries;
    private int order;

    public VisuTable() {
        TableColumn<Entry, ?> numberColumn = createColumn("N°", "number", 0.05);
        createColumn("Sender", "sender", 0.17);
        createColumn("Receiver", "receiver", 0.17);
        createColumn("Summary", "summary", 1-0.05-0.17-0.17);

        this.entries = FXCollections.observableArrayList();
        SortedList<Entry> sortedList = new SortedList<>(entries);
        setItems(sortedList);

        sortedList.comparatorProperty().bind(comparatorProperty());
        numberColumn.setSortType(TableColumn.SortType.DESCENDING);
        getSortOrder().clear();
        getSortOrder().add(numberColumn);
    }

    private TableColumn<Entry, ?> createColumn(String sender, String property, double widthRatio) {
        TableColumn<Entry, String> column = new TableColumn<>(sender);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        getColumns().add(column);

        column.prefWidthProperty().bind(widthProperty().multiply(widthRatio));
        return column;
    }

    public static class Entry {

        private final SimpleIntegerProperty numberProperty;
        private final SimpleStringProperty senderProperty;
        private final SimpleStringProperty receiverProperty;
        private final SimpleStringProperty summaryProperty;

        private Entry(int numberProperty, EntityId sender, EntityId receiverProperty, Message message) {
            this.numberProperty = new SimpleIntegerProperty(numberProperty);
            this.senderProperty = new SimpleStringProperty(sender.toString());
            this.receiverProperty = new SimpleStringProperty(receiverProperty.toString());
            this.summaryProperty = new SimpleStringProperty(message.summary());
        }

        public SimpleIntegerProperty numberProperty() {
            return numberProperty;
        }

        public SimpleStringProperty senderProperty() {
            return senderProperty;
        }

        public SimpleStringProperty receiverProperty() {
            return receiverProperty;
        }

        public SimpleStringProperty summaryProperty() {
            return summaryProperty;
        }
    }

    private boolean addEntry(EntityId fromId, EntityId toId, Message message) {
        int number = order++;
        return entries.add(new Entry(number, fromId, toId, message));
    }

    @Override
    public void onMessageSending(EntityId fromId, EntityId toId, UUID uuid, Message message) {
    }

    @Override
    public void onMessageReceived(EntityId fromId, EntityId toId, UUID uuid, Message message) {
        Platform.runLater(() -> addEntry(fromId, toId, message));
    }

    @Override
    public void onMessageHandled(EntityId fromId, EntityId toId, UUID uuid, Message message) {
    }
}
