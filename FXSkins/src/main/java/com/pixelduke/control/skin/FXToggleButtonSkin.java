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

package com.pixelduke.control.skin;

import impl.com.pixelduke.control.skin.ButtonAnimationHelper;
import javafx.beans.property.BooleanProperty;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableBooleanProperty;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.converter.BooleanConverter;
import javafx.scene.control.SkinBase;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.skin.ToggleButtonSkin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FXToggleButtonSkin extends ToggleButtonSkin {
    private ButtonAnimationHelper buttonAnimationHelper;

    public FXToggleButtonSkin(ToggleButton button) {
        super(button);

        buttonAnimationHelper = ButtonAnimationHelper.setupButton(button, shrinkAnimateOnPressProperty());
    }

    /********** CSS Properties ****************/

    private static final CssMetaData<ToggleButton, Boolean> SHRINK_ANIMATE_ON_PRESS_META_DATA =
            new CssMetaData<>(ButtonAnimationHelper.SHRINK_ANIMATE_ON_PRESS_PROPERTY_NAME,
                    BooleanConverter.getInstance(), true) {

                @Override
                public boolean isSettable(ToggleButton button) {
                    final FXToggleButtonSkin skin = (FXToggleButtonSkin) button.getSkin();
                    return !skin.shrinkAnimateOnPress.isBound();
                }

                @Override
                public StyleableProperty<Boolean> getStyleableProperty(ToggleButton button) {
                    final FXToggleButtonSkin skin = (FXToggleButtonSkin) button.getSkin();
                    return (StyleableProperty<Boolean>) skin.shrinkAnimateOnPressProperty();
                }
            };

    private BooleanProperty shrinkAnimateOnPress = new SimpleStyleableBooleanProperty(SHRINK_ANIMATE_ON_PRESS_META_DATA, true);

    private BooleanProperty shrinkAnimateOnPressProperty() { return shrinkAnimateOnPress; }

    private boolean isShrinkAnimateOnPress() { return shrinkAnimateOnPress.get(); }


    /* Setup styleables for this Skin */
    private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

    static {
        final List<CssMetaData<? extends Styleable, ?>> styleables =
                new ArrayList<>(SkinBase.getClassCssMetaData());
        styleables.add(SHRINK_ANIMATE_ON_PRESS_META_DATA);
        STYLEABLES = Collections.unmodifiableList(styleables);
    }

    /**
     * @return The CssMetaData associated with this class, which may include the
     * CssMetaData of its super classes.
     */
    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return STYLEABLES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }

    @Override
    public void dispose() {
        buttonAnimationHelper.dispose();

        super.dispose();
    }
}
