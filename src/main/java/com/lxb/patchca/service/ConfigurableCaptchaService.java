/*
 * Copyright (c) 2009 Piotr Piastucki
 * 
 * This file is part of Patchca CAPTCHA library.
 * 
 *  Patchca is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  Patchca is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *  
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Patchca. If not, see <http://www.gnu.org/licenses/>.
 */
package com.lxb.patchca.service;

import com.lxb.patchca.background.SingleColorBackgroundFactory;
import com.lxb.patchca.color.SingleColorFactory;
import com.lxb.patchca.font.RandomFontFactory;
import com.lxb.patchca.text.renderer.BestFitTextRenderer;
import com.lxb.patchca.word.AdaptiveRandomWordFactory;

public class ConfigurableCaptchaService extends AbstractCaptchaService {

	public ConfigurableCaptchaService() {
		backgroundFactory = new SingleColorBackgroundFactory();
		wordFactory = new AdaptiveRandomWordFactory();
		fontFactory = new RandomFontFactory();
		textRenderer = new BestFitTextRenderer();
		colorFactory = new SingleColorFactory();
		// filterFactory = new CurvesRippleFilterFactory(colorFactory);
		filterFactory = null;
		textRenderer.setLeftMargin(0);
		textRenderer.setRightMargin(0);
		textRenderer.setBottomMargin(0);
		textRenderer.setTopMargin(0);
		width = 120;
		height = 45;
	}

}
