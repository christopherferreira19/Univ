package ids.visu;

import com.google.common.collect.*;
import ids.entity.EntityId;
import ids.listener.Listener;
import ids.msg.*;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.omg.CORBA.IMP_LIMIT;

import java.util.*;

class VisuCanvas extends Canvas implements Listener {

    class LinkId {
        private final int minX;
        private final int minY;
        private final int maxX;
        private final int maxY;

        LinkId(EntityId.Region from, EntityId.Region to) {
            this(from.getX(), to.getX(), from.getY(), to.getY());
        }

        LinkId(int x1, int x2, int y1, int y2) {
            this.minX = Math.min(x1, x2);
            this.minY = Math.min(y1, y2);
            this.maxX = Math.max(x1, x2);
            this.maxY = Math.max(y1, y2);
        }

        @Override
        public int hashCode() {
            return Objects.hash(minX, minY, maxX, maxY);
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof LinkId)) {
                return false;
            }

            LinkId other = (LinkId) obj;
            return minX == other.minX
                    && minY == other.minY
                    && maxX == other.maxX
                    && maxY == other.maxY;
        }
    }

    class Rect {
        private final double x;
        private final double y;
        private final double w;
        private final double h;

        Rect(double x, double y, double w, double h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }
    }

    private static final Color[] REGISTRIES_COLORS = { Base16.B_08, Base16.B_09, Base16.B_0A,
            Base16.B_00, Base16.B_01, Base16.B_02, Base16.B_03, Base16.B_04};
    private static final Color REGIONS_COLOR = Base16.B_0C;
    private static final Color LINKS_COLOR = Base16.B_0D;
    private static final Color USERS_COLOR = Base16.B_0B;

    private static final int GAP = 10;
    private static final int MARGIN = 4;
    private static final double LINK_SIZE_RATIO = 0.3;

    private final int size;
    private Map<EntityId.Registry, Rect> registries;
    private Map<EntityId.Region, Rect> regions;
    private ListMultimap<EntityId, UUID> registryRegionMessages;
    private Map<LinkId, Rect> links;
    private ListMultimap<LinkId, UUID> linksMessages;
    private ListMultimap<EntityId.Region, EntityId.User> usersPerRegion;

    VisuCanvas(int size) {
        super(0, 0);

        this.size = size;
        this.registries = ImmutableMap.of();
        this.regions = ImmutableMap.of();
        this.registryRegionMessages = ArrayListMultimap.create();
        this.links = ImmutableMap.of();
        this.linksMessages = ArrayListMultimap.create();
        this.usersPerRegion = ArrayListMultimap.create();

        widthProperty().addListener(this::resize);
        heightProperty().addListener(this::resize);
    }

    private void resize(Observable event) {
        computeEntities();
        computeLinks();
        redraw();
    }

    private void computeEntities() {
        ImmutableMap.Builder<EntityId.Registry, Rect> registriesBuilder = ImmutableMap.builder();
        ImmutableMap.Builder<EntityId.Region, Rect> regionsBuilder = ImmutableMap.builder();

        doComputeRegions(MARGIN, MARGIN, getWidth() - 2 * MARGIN, getHeight() - 2 * MARGIN,
                0, 0, 0, registriesBuilder, regionsBuilder);
        this.registries = registriesBuilder.build();
        this.regions = regionsBuilder.build();
    }

    private void doComputeRegions(double x, double y, double w, double h, int level,
            int i, int j,
            ImmutableMap.Builder<EntityId.Registry, Rect> registriesBuilder,
            ImmutableMap.Builder<EntityId.Region, Rect> regionsBuilder) {
        double subw = w / 2;
        double subh = h / 2;

        if ((1 << level) == size) {
            regionsBuilder.put(EntityId.region(i, j), new Rect(x, y, w, h));
        }
        else {
            registriesBuilder.put(EntityId.registry(i, j, level), new Rect(x, y, w, h));
            doComputeRegions(
                    x + GAP,
                    y + GAP,
                    subw - 3 * GAP / 2,
                    subh - 3 * GAP / 2,
                    level + 1,
                    i * 2, j * 2, registriesBuilder, regionsBuilder);
            doComputeRegions(
                    x + subw + GAP / 2,
                    y + GAP,
                    subw - 3 * GAP / 2,
                    subh - 3 * GAP / 2,
                    level + 1,
                    i * 2 + 1, j * 2, registriesBuilder, regionsBuilder);
            doComputeRegions(
                    x + GAP,
                    y + subh + GAP / 2,
                    subw - 3 * GAP / 2,
                    subh - 3 * GAP / 2,
                    level + 1,
                    i * 2, j * 2 + 1, registriesBuilder, regionsBuilder);
            doComputeRegions(
                    x + subw + GAP / 2,
                    y + subh + GAP / 2,
                    subw - 3 * GAP / 2,
                    subh - 3 * GAP / 2,
                    level + 1,
                    i * 2 + 1, j * 2 + 1, registriesBuilder, regionsBuilder);
        }
    }

    private void computeLinks() {
        ImmutableMap.Builder<LinkId, Rect> linksBuilder = ImmutableMap.builder();

        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size; j++) {
                Rect rectFrom = regions.get(EntityId.region(i, j));
                Rect rectTo = regions.get(EntityId.region(i + 1, j));
                LinkId linkId = new LinkId(i, i + 1, j, j);
                double linkSize = rectFrom.h * LINK_SIZE_RATIO;
                Rect linkRect = new Rect(
                        rectFrom.x + rectFrom.w,
                        rectFrom.y + (rectFrom.h - linkSize) / 2,
                        rectTo.x - rectFrom.x - rectFrom.w,
                        linkSize);
                linksBuilder.put(linkId, linkRect);
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size - 1; j++) {
                Rect rectFrom = regions.get(EntityId.region(i, j));
                Rect rectTo = regions.get(EntityId.region(i, j + 1));
                LinkId linkId = new LinkId(i, i, j, j + 1);
                double linkSize = rectFrom.w * LINK_SIZE_RATIO;
                Rect linkRect = new Rect(
                        rectFrom.x + (rectFrom.w - linkSize) / 2,
                        rectFrom.y + rectFrom.h,
                        linkSize,
                        rectTo.y - rectFrom.y - rectFrom.h);
                linksBuilder.put(linkId, linkRect);
            }
        }

        links = linksBuilder.build();
    }

    void redraw() {
        GraphicsContext gc = getGraphicsContext2D();
        if (gc.getFill().isOpaque()) {
            gc.clearRect(0, 0, getWidth(), getHeight());
        }

        gc.setLineWidth(3);
        int levelsCount = 31 - Integer.numberOfLeadingZeros(size);
        for (int level = 0, subsize = 1; level < levelsCount; level++, subsize <<= 1) {
            for (int i = 0; i < subsize; i++) {
                for (int j = 0; j < subsize; j++) {
                    EntityId.Registry registry = EntityId.registry(i, j, level);
                    setColor(gc, REGISTRIES_COLORS[level], !registryRegionMessages.get(registry).isEmpty());
                    Rect rect = registries.get(registry);
                    drawEntity(gc, rect);
                }
            }
        }

        for (LinkId link : links.keySet()) {
            setColor(gc, LINKS_COLOR, !linksMessages.get(link).isEmpty());
            drawEntity(gc, links.get(link));
        }

        for (EntityId.Region region : regions.keySet()) {
            setColor(gc, REGIONS_COLOR, !registryRegionMessages.get(region).isEmpty());
            drawEntity(gc, regions.get(region));
        }

        setColor(gc, USERS_COLOR, false);
        for (EntityId.Region region : usersPerRegion.keySet()) {
            Rect regionRect = regions.get(region);
            List<EntityId.User> users = Multimaps.asMap(usersPerRegion).get(region);
            for (EntityId.User user : users) {
                drawEntity(gc, new Rect(
                        regionRect.x + regionRect.w * 0.25,
                        regionRect.y + regionRect.h * 0.25,
                        regionRect.w - regionRect.w * 0.5,
                        regionRect.h - regionRect.h * 0.5));
            }
        }
    }

    private void drawEntity(GraphicsContext gc, Rect rect) {
        gc.fillRect(rect.x, rect.y, rect.w, rect.h);
        gc.strokeRect(rect.x, rect.y, rect.w, rect.h);
    }

    private void setColor(GraphicsContext gc, Color color, boolean busy) {
        gc.setFill(busy
                ? color.darker()
                : color.brighter().brighter());
        gc.setStroke(color.darker());
    }

    @Override
    public void onMessageSending(EntityId fromId, EntityId toId, UUID uuid, Message message) {
        Platform.runLater(() -> doOnMessageSending(fromId, toId, uuid, message));
    }

    private void doOnMessageSending(EntityId fromId, EntityId toId, UUID uuid, Message message) {
        if (fromId instanceof EntityId.Region && toId instanceof EntityId.Region) {
            linksMessages.put(new LinkId((EntityId.Region) fromId, (EntityId.Region) toId), uuid);
            redraw();
        }
    }

    @Override
    public void onMessageReceived(EntityId fromId, EntityId toId, UUID uuid, Message message) {
        try {
            Thread.sleep(200);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Platform.runLater(() -> doOnMessageReceived(fromId, toId, uuid, message));

        try {
            Thread.sleep(200);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void doOnMessageReceived(EntityId fromId, EntityId toId, UUID uuid, Message message) {
        boolean shouldRedraw = false;

        if (fromId instanceof EntityId.Region && toId instanceof EntityId.Region) {
            linksMessages.remove(new LinkId((EntityId.Region) fromId, (EntityId.Region) toId), uuid);
            shouldRedraw = true;
        }

        if (!(toId instanceof EntityId.User)) {
            registryRegionMessages.put(toId, uuid);
            shouldRedraw = true;
        }

        if (shouldRedraw) {
            redraw();
        }
    }

    @Override
    public void onMessageHandled(EntityId fromId, EntityId toId, UUID uuid, Message message) {
        Platform.runLater(() -> doOnMessageHandled(fromId, toId, uuid, message));
    }

    private void doOnMessageHandled(EntityId fromId, EntityId toId, UUID uuid, Message message) {
        boolean shouldRedraw = false;
        if (message instanceof UserConnect) {
            usersPerRegion.put((EntityId.Region) toId, (EntityId.User) fromId);
            shouldRedraw = true;
        }
        else if (message instanceof UserDisconnect) {
            usersPerRegion.remove((EntityId.Region) toId, (EntityId.User) fromId);
            shouldRedraw = true;
        }
        else if (message instanceof MoveOk) {
            Set<EntityId.Region> keys = ImmutableSet.copyOf(usersPerRegion.keys());
            for (EntityId.Region region : keys) {
                usersPerRegion.remove(region, (EntityId.User) toId);
            }
            usersPerRegion.put((EntityId.Region) fromId, (EntityId.User) toId);

            shouldRedraw = true;
        }

        if (!(toId instanceof EntityId.User)) {
            registryRegionMessages.remove(toId, uuid);
            shouldRedraw = true;
        }

        if (shouldRedraw) {
            redraw();
        }
    }
}
