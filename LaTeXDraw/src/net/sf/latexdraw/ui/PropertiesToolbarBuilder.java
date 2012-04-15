package net.sf.latexdraw.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.IdentityHashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import net.sf.latexdraw.glib.ui.LCanvas;
import net.sf.latexdraw.instruments.MetaShapeCustomiser;
import net.sf.latexdraw.instruments.ShapeArcCustomiser;
import net.sf.latexdraw.instruments.ShapeArrowCustomiser;
import net.sf.latexdraw.instruments.ShapeAxesCustomiser;
import net.sf.latexdraw.instruments.ShapeBorderCustomiser;
import net.sf.latexdraw.instruments.ShapeCoordDimCustomiser;
import net.sf.latexdraw.instruments.ShapeDotCustomiser;
import net.sf.latexdraw.instruments.ShapeDoubleBorderCustomiser;
import net.sf.latexdraw.instruments.ShapeFillingCustomiser;
import net.sf.latexdraw.instruments.ShapeGridCustomiser;
import net.sf.latexdraw.instruments.ShapeStandardGridCustomiser;
import net.sf.latexdraw.instruments.ShapeGrouper;
import net.sf.latexdraw.instruments.ShapeRotationCustomiser;
import net.sf.latexdraw.instruments.ShapeShadowCustomiser;
import net.sf.latexdraw.instruments.TextCustomiser;
import net.sf.latexdraw.lang.LangTool;
import net.sf.latexdraw.util.LResources;

import org.malai.ui.UIComposer;
import org.malai.widget.MComboBox;
import org.malai.widget.MPanel;
import org.malai.widget.MProgressBar;
import org.malai.widget.MSpinner;

/**
 * The composer that creates the properties tool bar of the application.<br>
 * <br>
 * This file is part of LaTeXDraw.<br>
 * Copyright (c) 2005-2012 Arnaud BLOUIN<br>
 * <br>
 * LaTeXDraw is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later version.
 * <br>
 * LaTeXDraw is distributed without any warranty; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.<br>
 * <br>
 * 12/08/11<br>
 * @author Arnaud BLOUIN
 * @since 3.0
 */
public class PropertiesToolbarBuilder extends UIComposer<MPanel> {
	/** The max height of the textfield widget. */
	protected static final int HEIGHT_TEXTFIELD = 30;

	/** The space added between widgets. */
	protected static final int SEPARATION_WIDTH = 5;

	/** The main frame of the application. */
	protected LFrame frame;

	/** The hash map used to map a widget to its container. */
	protected Map<Component, WidgetMiniToolbar> mapContainers;


	/**
	 * Creates the bottom panel that contains a set of widgets to modify shapes.
	 * @param frame The frame that will contains the tool bar.
	 * @throws IllegalArgumentException If the given instrument is null.
	 * @since 3.0
	 */
	public PropertiesToolbarBuilder(final LFrame frame) {
		super();
		this.frame 		= frame;
		mapContainers	= new IdentityHashMap<Component, WidgetMiniToolbar>();
	}


	@Override
	public void compose(final MProgressBar progressBar) {
		final LCanvas canvas = frame.getCanvas();
		final MetaShapeCustomiser metaShapeCustomiser = frame.getMetaShapeCustomiser();

		widget = new MPanel(false, true);
		widget.setLayout(new FlowLayout(FlowLayout.LEFT));

		// Creation of the widgets layout of the shape properties instruments.
		composeJoinShapesWidgets(metaShapeCustomiser.getShapeGrouper());
		widget.add(composeRotationToolbar(metaShapeCustomiser.getRotationCustomiser(), canvas));
		widget.add(composeDimPosPropertiesToolbar(metaShapeCustomiser.getDimPosCustomiser(), canvas));
		widget.add(composeBorderPropertiesPanel(metaShapeCustomiser.getBorderCustomiser(), canvas));
		if(progressBar!=null) progressBar.addToProgressBar(5);
		widget.add(composeDoubleBorderPropertiesPanel(metaShapeCustomiser.getDoubleBorderCustomiser(), canvas));
		widget.add(composeShadowPropertiesPanel(metaShapeCustomiser.getShadowCustomiser(), canvas));
		widget.add(composeFillingPanel(metaShapeCustomiser.getFillingCustomiser(), canvas));
		if(progressBar!=null) progressBar.addToProgressBar(5);
		widget.add(composeArrowToolbar(metaShapeCustomiser.getArrowCustomiser(), canvas));
		widget.add(composeDotToolbar(metaShapeCustomiser.getDotCustomiser(), canvas));
		widget.add(composeTextPositionToolbar(metaShapeCustomiser.getTextCustomiser(), canvas));
		widget.add(composeTextPropertiesToolbar(metaShapeCustomiser.getTextCustomiser(), canvas));
		widget.add(composeArcPropertiesWidgets(metaShapeCustomiser.getArcCustomiser(), canvas));
		widget.add(composeGridPropertiesToolbar(metaShapeCustomiser.getStandardGridCustomiser(), canvas));
		widget.add(composeAxesPropertiesToolbar(metaShapeCustomiser.getAxesCustomiser(), canvas));
		widget.add(composeGridLabelsPropertiesToolbar(metaShapeCustomiser.getAxesCustomiser(), metaShapeCustomiser.getGridCustomiser(),
					metaShapeCustomiser.getStandardGridCustomiser(), canvas));
		if(progressBar!=null) progressBar.addToProgressBar(5);
	}


	// Composition of the widgets that joins/separates shapes.
	protected void composeJoinShapesWidgets(final ShapeGrouper grouper) {
		widget.add(grouper.getGroupB());
		widget.add(grouper.getSepB());
		grouper.addEventable(widget);
	}


	/** Creates the toolbar containing the widgets that customises grids' labels. */
	protected WidgetMiniToolbar composeGridLabelsPropertiesToolbar(final ShapeAxesCustomiser axeCust, final ShapeGridCustomiser gridCust,
																	final ShapeStandardGridCustomiser stdGridCust, final LCanvas canvas) {
		final WidgetMiniToolbar list = new WidgetMiniToolbar(frame, LResources.GRID_LABELS, WidgetMiniToolbar.LOCATION_NORTH, canvas);
		list.setToolTipText("Modifies the properties of grids' labels.");

		addSpinner(list, stdGridCust.getLabelsSizeS(), 50);
		addCombobox(list, axeCust.getShowLabels());
		list.addComponent(gridCust.getColourLabels());
		list.addComponent(axeCust.getShowOrigin());
		list.addComponent(stdGridCust.getLabelsXInvertedCB());
		list.addComponent(stdGridCust.getLabelsYInvertedCB());
		addSpinner(list, axeCust.getIncrLabelX(), 50);
		addSpinner(list, axeCust.getIncrLabelY(), 50);
		addSpinner(list, axeCust.getDistLabelsX(), 60);
		addSpinner(list, axeCust.getDistLabelsY(), 60);
		list.addSeparator();

		mapContainers.put(axeCust.getShowLabels(), list);
		mapContainers.put(gridCust.getColourLabels(), list);
		mapContainers.put(axeCust.getShowOrigin(), list);
		mapContainers.put(axeCust.getIncrLabelX(), list);
		mapContainers.put(axeCust.getIncrLabelY(), list);
		mapContainers.put(axeCust.getDistLabelsX(), list);
		mapContainers.put(axeCust.getDistLabelsY(), list);
		mapContainers.put(stdGridCust.getLabelsSizeS(), list);
		mapContainers.put(stdGridCust.getLabelsXInvertedCB(), list);
		mapContainers.put(stdGridCust.getLabelsYInvertedCB(), list);

		axeCust.addEventable(list.getToolbar());
		stdGridCust.addEventable(list.getToolbar());
		gridCust.addEventable(list.getToolbar());

		return list;
	}


	/** Creates the toolbar containing the widgets that customises axes. */
	protected WidgetMiniToolbar composeAxesPropertiesToolbar(final ShapeAxesCustomiser cust, final LCanvas canvas) {
		final WidgetMiniToolbar list = new WidgetMiniToolbar(frame, LResources.AXES_ICON, WidgetMiniToolbar.LOCATION_NORTH, canvas);
		list.setToolTipText("Modifies the properties of axes.");
		final MPanel panel 	  = new MPanel(false, true);
		final MPanel ticksPanel = new MPanel(false, true);

		ticksPanel.setBorder(new CompoundBorder(new TitledBorder(null, LangTool.INSTANCE.getString18("ParametersAxeFrame.17"), //$NON-NLS-1$
				  TitledBorder.LEFT, TitledBorder.TOP), new EmptyBorder(0,0,0,0)));

		panel.add(cust.getShapeAxes());

		addCombobox(ticksPanel, cust.getShapeTicks());
		addSpinner(ticksPanel, cust.getTicksSizeS(), 70);
		addCombobox(ticksPanel, cust.getShowTicks());

		list.addComponent(panel);
		list.addComponent(ticksPanel);
		list.addSeparator();

		mapContainers.put(cust.getShapeAxes(), list);
		mapContainers.put(cust.getShapeTicks(), list);
		mapContainers.put(cust.getTicksSizeS(), list);
		mapContainers.put(cust.getShowTicks(), list);

		cust.addEventable(panel);
		cust.addEventable(ticksPanel);
		return list;
	}



	protected WidgetMiniToolbar composeGridPropertiesToolbar(final ShapeStandardGridCustomiser cust, final LCanvas canvas) {
		final WidgetMiniToolbar list = new WidgetMiniToolbar(frame, LResources.GRID_ICON, WidgetMiniToolbar.LOCATION_NORTH, canvas);
		list.setToolTipText("Modifies the properties of grids and axes.");

		addSpinner(list, cust.getxStartS(), 50);
		addSpinner(list, cust.getyStartS(), 50);
		addSpinner(list, cust.getxEndS(), 50);
		addSpinner(list, cust.getyEndS(), 50);
		addSpinner(list, cust.getxOriginS(), 50);
		addSpinner(list, cust.getyOriginS(), 50);
		list.addSeparator();

		mapContainers.put(cust.getxStartS(), list);
		mapContainers.put(cust.getyStartS(), list);
		mapContainers.put(cust.getxEndS(), list);
		mapContainers.put(cust.getyEndS(), list);
		mapContainers.put(cust.getxOriginS(), list);
		mapContainers.put(cust.getyOriginS(), list);

		cust.addEventable(list.getToolbar());
		return list;
	}


	protected WidgetMiniToolbar composeDimPosPropertiesToolbar(final ShapeCoordDimCustomiser cust, final LCanvas canvas) {
		final WidgetMiniToolbar list = new WidgetMiniToolbar(frame, LResources.DIM_POS_ICON, WidgetMiniToolbar.LOCATION_NORTH, canvas);
		list.setToolTipText("Modifies the dimensions and the position.");

		addSpinner(list, cust.getTlxS(), 90);
		addSpinner(list, cust.getTlyS(), 90);
		list.addSeparator();

		mapContainers.put(cust.getTlxS(), list);
		mapContainers.put(cust.getTlyS(), list);

		cust.addEventable(list.getToolbar());
		return list;
	}


	protected WidgetMiniToolbar composeTextPropertiesToolbar(final TextCustomiser textCustomiser, final LCanvas canvas) {
		final WidgetMiniToolbar list = new WidgetMiniToolbar(frame, LResources.TEXT_ICON, WidgetMiniToolbar.LOCATION_NORTH, canvas);
		list.setToolTipText("Modifies the properties of the text.");

		list.addComponent(textCustomiser.getPackagesLabel());
		list.addComponent(textCustomiser.getPackagesField().getScrollpane());
		list.addSeparator();

		mapContainers.put(textCustomiser.getPackagesLabel(), list);
		mapContainers.put(textCustomiser.getPackagesField().getScrollpane(), list);

		textCustomiser.addEventable(textCustomiser.getPackagesField());
		return list;
	}


	protected WidgetMiniToolbar composeTextPositionToolbar(final TextCustomiser textCustomiser, final LCanvas canvas) {
		final WidgetMiniToolbar list = new WidgetMiniToolbar(frame, LResources.TEXTPOS_BL, WidgetMiniToolbar.LOCATION_NORTH, canvas);
		list.setToolTipText("Modifies the position of the text.");
		list.addComponent(textCustomiser.getBlButton());
		list.addComponent(textCustomiser.getBButton());
		list.addComponent(textCustomiser.getBrButton());
		list.addComponent(textCustomiser.getTlButton());
		list.addComponent(textCustomiser.getTButton());
		list.addComponent(textCustomiser.getTrButton());

		mapContainers.put(textCustomiser.getBlButton(), list);
		mapContainers.put(textCustomiser.getBButton(), list);
		mapContainers.put(textCustomiser.getBrButton(), list);
		mapContainers.put(textCustomiser.getTlButton(), list);
		mapContainers.put(textCustomiser.getTButton(), list);
		mapContainers.put(textCustomiser.getTrButton(), list);

		list.addSeparator();
		textCustomiser.addEventable(list.getToolbar());
		return list;
	}


	// FIXME SCALA: When a trait will be used, this trait could be used as type of the parameter instead of several operations.
	protected void addSpinner(final Container cont, final MSpinner spinner, final int width) {
		spinner.setPreferredSize(new Dimension(width, HEIGHT_TEXTFIELD));
		if(spinner.getLabel()!=null)
			cont.add(spinner.getLabel());
		cont.add(spinner);
	}


	protected void addCombobox(final Container cont, final MComboBox spinner) {
		if(spinner.getLabel()!=null)
			cont.add(spinner.getLabel());
		cont.add(spinner);
	}


	protected JComponent composeArcPropertiesWidgets(final ShapeArcCustomiser ins, final LCanvas canvas) {
		final WidgetMiniToolbar list = new WidgetMiniToolbar(frame, LResources.ARC_ICON, WidgetMiniToolbar.LOCATION_NORTH, canvas);
		list.setToolTipText("Customises the arcs.");

		list.addComponent(ins.getArcB());
		list.addComponent(ins.getChordB());
		list.addComponent(ins.getWedgeB());
		addSpinner(list, ins.getStartAngleS(), 70);
		addSpinner(list, ins.getEndAngleS(), 70);
		list.addSeparator();

		mapContainers.put(ins.getArcB(), list);
		mapContainers.put(ins.getChordB(), list);
		mapContainers.put(ins.getWedgeB(), list);
		mapContainers.put(ins.getStartAngleS(), list);
		mapContainers.put(ins.getEndAngleS(), list);

        ins.addEventable(list.getToolbar());

		return list;
	}


	protected JComponent composeDotToolbar(final ShapeDotCustomiser ins, final LCanvas canvas) {
		final WidgetMiniToolbar list = new WidgetMiniToolbar(frame, LResources.DOT_ICON, WidgetMiniToolbar.LOCATION_NORTH, canvas);
		list.setToolTipText("Customises the dots.");

		list.addComponent(ins.getDotCB());
		addSpinner(list, ins.getDotSizeField(), 70);
		list.addComponent(ins.getFillingB());
		list.addSeparator();

		mapContainers.put(ins.getDotCB(), list);
		mapContainers.put(ins.getDotSizeField(), list);
		mapContainers.put(ins.getFillingB(), list);

        ins.addEventable(list.getToolbar());

		return list;
	}


	protected JComponent composeArrowToolbar(final ShapeArrowCustomiser ins, final LCanvas canvas) {
		final int size = 70;
		final WidgetMiniToolbar list = new WidgetMiniToolbar(frame, LResources.ARROW_ICON, WidgetMiniToolbar.LOCATION_NORTH, canvas);
		list.setToolTipText("Customises the arrows.");

		list.addComponent(ins.getArrowLeftCB());
		list.addComponent(ins.getArrowRightCB());
		addSpinner(list, ins.getArrowInset(), size);
		addSpinner(list, ins.getArrowLength(), size);
		addSpinner(list, ins.getArrowSizeNum(), size);
		addSpinner(list, ins.getArrowSizeDim(), size);
		addSpinner(list, ins.getBracketNum(), size);
		addSpinner(list, ins.getTbarsizeNum(), size);
		addSpinner(list, ins.getTbarsizeDim(), size);
		addSpinner(list, ins.getDotSizeNum(), size);
		addSpinner(list, ins.getDotSizeDim(), size);
		addSpinner(list, ins.getRbracketNum(), size);

		list.addSeparator();

		mapContainers.put(ins.getArrowLeftCB(), list);
		mapContainers.put(ins.getArrowRightCB(), list);
		mapContainers.put(ins.getArrowInset(), list);
		mapContainers.put(ins.getArrowLength(), list);
		mapContainers.put(ins.getArrowSizeNum(), list);
		mapContainers.put(ins.getArrowSizeDim(), list);
		mapContainers.put(ins.getTbarsizeNum(), list);
		mapContainers.put(ins.getTbarsizeDim(), list);
		mapContainers.put(ins.getBracketNum(), list);
		mapContainers.put(ins.getRbracketNum(), list);
		mapContainers.put(ins.getDotSizeNum(), list);
		mapContainers.put(ins.getDotSizeDim(), list);

        ins.addEventable(list.getToolbar());

		return list;
	}



	protected JComponent composeRotationToolbar(final ShapeRotationCustomiser ins, final LCanvas canvas) {
		final WidgetMiniToolbar list = new WidgetMiniToolbar(frame, LResources.ROTATE_ICON, WidgetMiniToolbar.LOCATION_NORTH, canvas);
        list.setToolTipText(LangTool.INSTANCE.getString18("LaTeXDrawFrame.2")); //$NON-NLS-1$

        addSpinner(list, ins.getRotationField(), 65);
        list.addComponent(ins.getRotate90Button());
        list.addComponent(ins.getRotate180Button());
        list.addComponent(ins.getRotate270Button());
        list.addSeparator();

        mapContainers.put(ins.getRotationField(), list);
        mapContainers.put(ins.getRotate90Button(), list);
        mapContainers.put(ins.getRotate180Button(), list);
        mapContainers.put(ins.getRotate270Button(), list);

        ins.addEventable(list.getToolbar());

		return list;
	}


	/**
	 * Creates the widget that contains the widgets dedicated to the modification of shapes filling properties.
	 * @param fillingCustomiser The instrument that contains the widgets.
	 * @return The created widget. Cannot be null.
	 * @since 3.0
	 */
	protected JComponent composeFillingPanel(final ShapeFillingCustomiser fillingCustomiser, final LCanvas canvas) {
		final WidgetMiniToolbar list = new WidgetMiniToolbar(frame, LResources.FILLING_ICON, WidgetMiniToolbar.LOCATION_NORTH, canvas);
		list.setToolTipText("Modifies the filling properties.");
		list.addComponent(fillingCustomiser.getFillStyleCB());
		list.addComponent(Box.createHorizontalStrut(SEPARATION_WIDTH));
		list.addComponent(fillingCustomiser.getFillColButton());
		list.addComponent(Box.createHorizontalStrut(SEPARATION_WIDTH));
		list.addComponent(fillingCustomiser.getHatchColButton());
		list.addComponent(Box.createHorizontalStrut(SEPARATION_WIDTH));
		addSpinner(list, fillingCustomiser.getHatchAngleField(), 65);
		list.addComponent(Box.createHorizontalStrut(SEPARATION_WIDTH));
		addSpinner(list, fillingCustomiser.getHatchWidthField(), 60);
		list.addComponent(Box.createHorizontalStrut(SEPARATION_WIDTH));
		addSpinner(list, fillingCustomiser.getHatchSepField(), 65);
		list.addComponent(Box.createHorizontalStrut(SEPARATION_WIDTH));
		list.addComponent(fillingCustomiser.getGradStartColButton());
		list.addComponent(Box.createHorizontalStrut(SEPARATION_WIDTH));
		list.addComponent(fillingCustomiser.getGradEndColButton());
		list.addComponent(Box.createHorizontalStrut(SEPARATION_WIDTH));
		addSpinner(list, fillingCustomiser.getGradAngleField(), 60);
		list.addComponent(Box.createHorizontalStrut(SEPARATION_WIDTH));
		addSpinner(list, fillingCustomiser.getGradMidPtField(), 70);
		list.addSeparator();

		mapContainers.put(fillingCustomiser.getFillStyleCB(), list);
		mapContainers.put(fillingCustomiser.getFillColButton(), list);
		mapContainers.put(fillingCustomiser.getHatchColButton(), list);
		mapContainers.put(fillingCustomiser.getHatchAngleField(), list);
		mapContainers.put(fillingCustomiser.getHatchWidthField(), list);
		mapContainers.put(fillingCustomiser.getHatchSepField(), list);
		mapContainers.put(fillingCustomiser.getGradStartColButton(), list);
		mapContainers.put(fillingCustomiser.getGradEndColButton(), list);
		mapContainers.put(fillingCustomiser.getGradAngleField(), list);
		mapContainers.put(fillingCustomiser.getGradMidPtField(), list);

		fillingCustomiser.addEventable(list.getToolbar());

		return list;
	}


	/**
	 * Creates the widget that contains the widgets dedicated to the modification of shapes double border properties.
	 * @param shadowCustomiser The instrument that contains the widgets.
	 * @return The created widget. Cannot be null.
	 * @since 3.0
	 */
	protected JComponent composeShadowPropertiesPanel(final ShapeShadowCustomiser shadowCustomiser, final LCanvas canvas) {
		final WidgetMiniToolbar list = new WidgetMiniToolbar(frame, LResources.SHADOW_ICON, WidgetMiniToolbar.LOCATION_NORTH, canvas);
		list.setToolTipText("Modifies the shadow properties.");
		list.addComponent(shadowCustomiser.getShadowCB());
		list.addComponent(Box.createHorizontalStrut(SEPARATION_WIDTH));
		list.addComponent(shadowCustomiser.getShadowColB());
		list.addComponent(Box.createHorizontalStrut(SEPARATION_WIDTH));
		addSpinner(list, shadowCustomiser.getShadowSizeField(), 75);
		list.addComponent(Box.createHorizontalStrut(SEPARATION_WIDTH));
		addSpinner(list, shadowCustomiser.getShadowAngleField(), 75);
		list.addSeparator();

		mapContainers.put(shadowCustomiser.getShadowCB(), list);
		mapContainers.put(shadowCustomiser.getShadowColB(), list);
		mapContainers.put(shadowCustomiser.getShadowSizeField(), list);
		mapContainers.put(shadowCustomiser.getShadowAngleField(), list);

		shadowCustomiser.addEventable(list.getToolbar());

		return list;
	}


	/**
	 * Creates the widget that contains the widgets dedicated to the modification of shapes double border properties.
	 * @param dbleBorderCustomiser The instrument that contains the widgets.
	 * @return The created widget. Cannot be null.
	 * @since 3.0
	 */
	protected JComponent composeDoubleBorderPropertiesPanel(final ShapeDoubleBorderCustomiser dbleBorderCustomiser, final LCanvas canvas) {
		final WidgetMiniToolbar list = new WidgetMiniToolbar(frame, LResources.DOUBLE_BORDER_ICON, WidgetMiniToolbar.LOCATION_NORTH, canvas);
		list.setToolTipText("Modifies the double border properties.");
		list.addComponent(dbleBorderCustomiser.getDbleBoundCB());
		list.addComponent(Box.createHorizontalStrut(SEPARATION_WIDTH));
		list.addComponent(dbleBorderCustomiser.getDbleBoundColB());
		list.addComponent(Box.createHorizontalStrut(SEPARATION_WIDTH));
		addSpinner(list, dbleBorderCustomiser.getDbleSepField(), 55);
		list.addSeparator();

		mapContainers.put(dbleBorderCustomiser.getDbleBoundCB(), list);
		mapContainers.put(dbleBorderCustomiser.getDbleBoundColB(), list);
		mapContainers.put(dbleBorderCustomiser.getDbleSepField(), list);

		dbleBorderCustomiser.addEventable(list.getToolbar());

		return list;
	}


	/**
	 * Creates the widget that contains the widgets dedicated to the modification of shapes border properties.
	 * @param borderCustomiser The instrument that contains the widgets.
	 * @return The created widget. Cannot be null.
	 * @since 3.0
	 */
	protected JComponent composeBorderPropertiesPanel(final ShapeBorderCustomiser borderCustomiser, final LCanvas canvas) {
		final WidgetMiniToolbar list = new WidgetMiniToolbar(frame, LResources.BORDER_ICON, WidgetMiniToolbar.LOCATION_NORTH, canvas);
		list.setToolTipText("Modifies the border properties.");

		addSpinner(list, borderCustomiser.getThicknessField(), 65);
		list.addComponent(Box.createHorizontalStrut(SEPARATION_WIDTH));
		list.addComponent(borderCustomiser.getLineColButton());
		list.addComponent(Box.createHorizontalStrut(SEPARATION_WIDTH));
		list.addComponent(borderCustomiser.getLineCB());
		list.addComponent(Box.createHorizontalStrut(SEPARATION_WIDTH));
		list.addComponent(borderCustomiser.getBordersPosCB());
		list.addComponent(Box.createHorizontalStrut(SEPARATION_WIDTH));
		addSpinner(list, borderCustomiser.getFrameArcField(), 65);
		list.addSeparator();

		mapContainers.put(borderCustomiser.getThicknessField(), list);
		mapContainers.put(borderCustomiser.getLineColButton(), list);
		mapContainers.put(borderCustomiser.getLineCB(), list);
		mapContainers.put(borderCustomiser.getBordersPosCB(), list);
		mapContainers.put(borderCustomiser.getFrameArcField(), list);

		borderCustomiser.addEventable(list.getToolbar());

		return list;
	}
}
