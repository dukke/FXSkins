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

public class FXScrollPaneSkin extends ScrollPaneSkin {
    private static final Duration SCROLL_BAR_FADE_DURATION = Duration.millis(400);
    private static final Duration MOUSE_EXITED_FROM_SCROLLBAR_TIMER_DURATION = Duration.seconds(2);

    protected ScrollBar minimalHBar = new ScrollBar();
    protected ScrollBar minimalVBar = new ScrollBar();
    protected ScrollBar normalHBar = new ScrollBar();
    protected ScrollBar normalVBar = new ScrollBar();

    private ScrollBar oldHBar, oldVBar;

    private EventHandler<MouseEvent> mouseEnteredOnNormalVBar = this::mouseEnteredOnNormalVBar;
    private EventHandler<MouseEvent> mouseExitedFromNormalVBar = this::mouseExitedFromNormalVBar;

    private Timeline mouseExitedFromScrollBarTimer = new Timeline();

    /**
     * Creates a new FXScrollPaneSkin instance.
     *
     * @param control The control that this skin should be installed onto.
     */
    public FXScrollPaneSkin(ScrollPane control) {
        super(control);

        minimalHBar.setOrientation(Orientation.HORIZONTAL);
        minimalVBar.setOrientation(Orientation.VERTICAL);
        normalHBar.setOrientation(Orientation.HORIZONTAL);
        normalVBar.setOrientation(Orientation.VERTICAL);

        normalVBar.setOpacity(0);
        normalHBar.setOpacity(0);

        oldHBar = getHorizontalScrollBar();
        oldVBar = getVerticalScrollBar();

        initScrollBars();

        getChildren().addAll(minimalHBar, minimalVBar, normalHBar, normalVBar);
    }

    private void initScrollBars() {
        oldVBar.setManaged(false);
        oldHBar.setManaged(false);

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
    }

    private void mouseEnteredOnNormalVBar(MouseEvent mouseEvent) {
        mouseExitedFromScrollBarTimer.stop();

        if (normalVBar.getOpacity() < 1) {
            FadeTransition fadeOutTransition = new FadeTransition(SCROLL_BAR_FADE_DURATION, minimalVBar);
            fadeOutTransition.setFromValue(1);
            fadeOutTransition.setToValue(0);

            FadeTransition fadeInTransition = new FadeTransition(SCROLL_BAR_FADE_DURATION, normalVBar);
            fadeInTransition.setFromValue(0);
            fadeInTransition.setToValue(1);

            ParallelTransition scrollBarsAnimation = new ParallelTransition(fadeInTransition, fadeOutTransition);
            scrollBarsAnimation.play();
        }
    }

    private void mouseExitedFromNormalVBar(MouseEvent mouseEvent) {
        FadeTransition fadeInTransition = new FadeTransition(SCROLL_BAR_FADE_DURATION, minimalVBar);
        fadeInTransition.setFromValue(0);
        fadeInTransition.setToValue(1);

        FadeTransition fadeOutTransition = new FadeTransition(SCROLL_BAR_FADE_DURATION, normalVBar);
        fadeOutTransition.setFromValue(1);
        fadeOutTransition.setToValue(0);

        ParallelTransition scrollBarAnimation = new ParallelTransition(fadeInTransition, fadeOutTransition);

        KeyFrame keyFrame = new KeyFrame(MOUSE_EXITED_FROM_SCROLLBAR_TIMER_DURATION, event -> scrollBarAnimation.play());
        mouseExitedFromScrollBarTimer.getKeyFrames().setAll(keyFrame);
        mouseExitedFromScrollBarTimer.playFromStart();
    }

    private void bindScrollBar(ScrollBar scrollBarToBind, ScrollBar oldScrollBar) {
        scrollBarToBind.valueProperty().bindBidirectional(oldScrollBar.valueProperty());
        scrollBarToBind.minProperty().bindBidirectional(oldScrollBar.minProperty());
        scrollBarToBind.maxProperty().bindBidirectional(oldScrollBar.maxProperty());
        scrollBarToBind.visibleAmountProperty().bindBidirectional(oldScrollBar.visibleAmountProperty());
        scrollBarToBind.unitIncrementProperty().bindBidirectional(oldScrollBar.unitIncrementProperty());
        scrollBarToBind.blockIncrementProperty().bindBidirectional(oldScrollBar.blockIncrementProperty());
    }

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        final ScrollPane control = getSkinnable();

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
            final double prefHeight = minimalHBar.prefHeight(-1);
            minimalHBar.resizeRelocate(leftPadding, h - prefHeight - bottomPadding, w - leftPadding - rightPadding, prefHeight);
        }
    }

}
