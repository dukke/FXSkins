package com.pixelduke.control.skin;

import impl.com.pixelduke.utils.ReflectionUtils;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.skin.ScrollPaneSkin;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class ConsciousScrollPaneSkin extends ScrollPaneSkin {
    private static final Duration SCROLL_BAR_FADE_DURATION = Duration.millis(400);
    private static final Duration MOUSE_EXITED_FROM_SCROLLBAR_TIMER_DURATION = Duration.seconds(2);

    protected ScrollBar minimalHBar = new ScrollBar();
    protected ScrollBar minimalVBar = new ScrollBar();
    protected ScrollBar normalHBar = new ScrollBar();
    protected ScrollBar normalVBar = new ScrollBar();

    private ScrollBar oldHBar, oldVBar;

    private EventHandler<MouseEvent> mouseEnteredOnNormalVBar = this::mouseEnteredOnNormalVBar;
    private EventHandler<MouseEvent> mouseExitedFromNormalVBar = this::mouseExitedFromNormalVBar;

    private EventHandler<MouseEvent> mouseEnteredOnNormalHBar = this::mouseEnteredOnNormalHBar;
    private EventHandler<MouseEvent> mouseExitedFromNormalHBar = this::mouseExitedFromNormalHBar;

    private Timeline mouseExitedFromVScrollBarTimer = new Timeline();
    private Timeline mouseExitedFromHScrollBarTimer = new Timeline();

    /**
     * Creates a new FXScrollPaneSkin instance.
     *
     * @param control The control that this skin should be installed onto.
     */
    public ConsciousScrollPaneSkin(ScrollPane control) {
        super(control);

        oldHBar = getHorizontalScrollBar();
        oldVBar = getVerticalScrollBar();

        oldVBar.setManaged(false);
        oldHBar.setManaged(false);

        initScrollBars();

        getChildren().addAll(minimalHBar, minimalVBar, normalHBar, normalVBar);
    }

    private void initScrollBars() {
        minimalHBar.setOrientation(Orientation.HORIZONTAL);
        minimalVBar.setOrientation(Orientation.VERTICAL);
        normalHBar.setOrientation(Orientation.HORIZONTAL);
        normalVBar.setOrientation(Orientation.VERTICAL);

        normalVBar.setOpacity(0);
        normalHBar.setOpacity(0);

        bindScrollBar(minimalHBar, oldHBar);
        bindScrollBar(minimalVBar, oldVBar);
        bindScrollBar(normalHBar, oldHBar);
        bindScrollBar(normalVBar, oldVBar);

        minimalHBar.getStyleClass().add("minimal-scroll-bar");
        minimalVBar.getStyleClass().add("minimal-scroll-bar");
        normalHBar.getStyleClass().add("normal-scroll-bar");
        normalVBar.getStyleClass().add("normal-scroll-bar");

        normalVBar.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEnteredOnNormalVBar);
        normalVBar.addEventHandler(MouseEvent.MOUSE_EXITED, mouseExitedFromNormalVBar);

        normalHBar.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEnteredOnNormalHBar);
        normalHBar.addEventHandler(MouseEvent.MOUSE_EXITED, mouseExitedFromNormalHBar);
    }

    private void bindScrollBar(ScrollBar scrollBarToBind, ScrollBar oldScrollBar) {
        scrollBarToBind.valueProperty().bindBidirectional(oldScrollBar.valueProperty());
        scrollBarToBind.minProperty().bindBidirectional(oldScrollBar.minProperty());
        scrollBarToBind.maxProperty().bindBidirectional(oldScrollBar.maxProperty());
        scrollBarToBind.visibleAmountProperty().bindBidirectional(oldScrollBar.visibleAmountProperty());
        scrollBarToBind.unitIncrementProperty().bindBidirectional(oldScrollBar.unitIncrementProperty());
        scrollBarToBind.blockIncrementProperty().bindBidirectional(oldScrollBar.blockIncrementProperty());
    }

    private void mouseEnteredOnNormalVBar(MouseEvent mouseEvent) {
        mouseExitedFromVScrollBarTimer.stop();

        doMouseEnteredOnNormalBar(normalVBar, minimalVBar);
    }

    private void mouseEnteredOnNormalHBar(MouseEvent mouseEvent) {
        mouseExitedFromHScrollBarTimer.stop();

        doMouseEnteredOnNormalBar(normalHBar, minimalHBar);
    }

    private void doMouseEnteredOnNormalBar(ScrollBar normalScrollBar, ScrollBar minimalScrollBar) {
        normalScrollBar.toFront();

        if (normalScrollBar.getOpacity() < 1) {
            FadeTransition fadeInTransition = new FadeTransition(SCROLL_BAR_FADE_DURATION, normalScrollBar);
            fadeInTransition.setToValue(1);

            FadeTransition fadeOutTransition = new FadeTransition(SCROLL_BAR_FADE_DURATION, minimalScrollBar);
            fadeOutTransition.setToValue(0);

            ParallelTransition scrollBarsAnimation = new ParallelTransition(fadeInTransition, fadeOutTransition);
            scrollBarsAnimation.play();
        }
    }

    private void mouseExitedFromNormalVBar(MouseEvent mouseEvent) {
        doMouseExitedFromNormalBar(normalVBar, minimalVBar, mouseExitedFromVScrollBarTimer);
    }

    private void mouseExitedFromNormalHBar(MouseEvent mouseEvent) {
        doMouseExitedFromNormalBar(normalHBar, minimalHBar, mouseExitedFromHScrollBarTimer);
    }

    private void doMouseExitedFromNormalBar(ScrollBar normalBar, ScrollBar minimalBar, Timeline mouseExitedTimer) {
        FadeTransition fadeInTransition = new FadeTransition(SCROLL_BAR_FADE_DURATION, minimalBar);
        fadeInTransition.setToValue(1);

        FadeTransition fadeOutTransition = new FadeTransition(SCROLL_BAR_FADE_DURATION, normalBar);
        fadeOutTransition.setToValue(0);

        ParallelTransition scrollBarAnimation = new ParallelTransition(fadeInTransition, fadeOutTransition);

        KeyFrame keyFrame = new KeyFrame(MOUSE_EXITED_FROM_SCROLLBAR_TIMER_DURATION, event -> scrollBarAnimation.play());
        mouseExitedTimer.getKeyFrames().setAll(keyFrame);
        mouseExitedTimer.playFromStart();
    }

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        final ScrollPane control = getSkinnable();

        // We call the super.layoutChildren and temporarily set things up so that the old ScrollBars don't show up
        ScrollPane.ScrollBarPolicy hBarPolicy = control.getHbarPolicy();
        ScrollPane.ScrollBarPolicy vBarPolicy = control.getVbarPolicy();
        control.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        control.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        super.layoutChildren(x, y, w, h);
        control.setHbarPolicy(hBarPolicy);
        control.setVbarPolicy(vBarPolicy);

        final Insets padding = control.getPadding();
        final double rightPadding = snapSizeX(padding.getRight());
        final double leftPadding = snapSizeX(padding.getLeft());
        final double topPadding = snapSizeY(padding.getTop());
        final double bottomPadding = snapSizeY(padding.getBottom());

        // Layout "new" ScrollBars
        Boolean verticalSBVisible = (Boolean) ReflectionUtils.forceInvokeMethod(this, "determineVerticalSBVisible");
        minimalVBar.setVisible(verticalSBVisible);
        normalVBar.setVisible(verticalSBVisible);
        if (verticalSBVisible) {
            if (minimalVBar.getOpacity() > 0) {
                final double prefWidth = minimalVBar.prefWidth(-1);
                minimalVBar.resizeRelocate(w - prefWidth - rightPadding, topPadding, prefWidth, h - topPadding - bottomPadding);
            }

            final double prefWidth = normalVBar.prefWidth(-1);
            normalVBar.resizeRelocate(w - prefWidth - rightPadding, topPadding, prefWidth, h - topPadding - bottomPadding);
        }

        Boolean horizontalSBVisible = (Boolean) ReflectionUtils.forceInvokeMethod(this, "determineHorizontalSBVisible");
        minimalHBar.setVisible(horizontalSBVisible);
        normalHBar.setVisible(horizontalSBVisible);
        if (horizontalSBVisible) {
            if (minimalHBar.getOpacity() > 0) {
                final double prefHeight = minimalHBar.prefHeight(-1);
                minimalHBar.resizeRelocate(leftPadding, h - prefHeight - bottomPadding, w - leftPadding - rightPadding, prefHeight);
            }

            final double prefHeight = normalHBar.prefHeight(-1);
            normalHBar.resizeRelocate(leftPadding, h - prefHeight - bottomPadding, w - leftPadding - rightPadding, prefHeight);
        }
    }

}
