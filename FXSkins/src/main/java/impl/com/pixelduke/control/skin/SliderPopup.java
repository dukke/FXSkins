/*
 * FXSkins,
 * Copyright (C) 2021 PixelDuke (Pedro Duque Vieira - www.pixelduke.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package impl.com.pixelduke.control.skin;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.css.PseudoClass;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PopupControl;
import javafx.scene.control.Skin;
import javafx.scene.control.Slider;

public class SliderPopup extends PopupControl {
    private static final String DEFAULT_STYLE_CLASS = "slider-popup";

    private static final PseudoClass VERTICAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("vertical");
    private static final PseudoClass HORIZONTAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("horizontal");

    private Slider slider;
    private DoubleProperty value = new SimpleDoubleProperty();


    public SliderPopup(Slider slider) {
        this.slider = slider;

        getStyleClass().add(DEFAULT_STYLE_CLASS);

        updateOrientationPseudoClass();
        slider.orientationProperty().addListener(observable -> updateOrientationPseudoClass());
    }

    private void updateOrientationPseudoClass() {
        final boolean vertical = (slider.getOrientation() == Orientation.VERTICAL);
        pseudoClassStateChanged(VERTICAL_PSEUDOCLASS_STATE,    vertical);
        pseudoClassStateChanged(HORIZONTAL_PSEUDOCLASS_STATE, !vertical);
    }

    public double getContentWidth() {
        return ((SliderPopupSkin)getSkin()).valueText.getWidth();
    }

    // --- value
    public DoubleProperty valueProperty() { return value; }
    public double getValue() { return value.get(); }
    public void setValue(double value) {
        this.value.set(value);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new SliderPopupSkin(this);
    }

    private class SliderPopupSkin implements Skin<SliderPopup> {

        SliderPopup skinnable;
        Label valueText;

        /**
         * Constructor for all SkinBase instances.
         *
         * @param control The control for which this Skin should attach to.
         */
        SliderPopupSkin(SliderPopup control) {
            skinnable = control;
            valueText = new Label();

            valueText.textProperty().bind(new StringBinding() {
                {
                    super.bind(skinnable.valueProperty());
                }

                @Override
                protected String computeValue() {
                    return String.valueOf(Math.round(skinnable.getValue()));
                }
            });
        }

        @Override
        public SliderPopup getSkinnable() {
            return skinnable;
        }

        @Override
        public Node getNode() {
            return valueText;
        }

        @Override
        public void dispose() {

        }
    }
}
