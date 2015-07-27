package test.gui.hand;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import net.sf.latexdraw.glib.models.interfaces.prop.IPlotProp;
import net.sf.latexdraw.glib.models.interfaces.shape.IPlot;
import net.sf.latexdraw.instruments.Hand;
import net.sf.latexdraw.instruments.Pencil;
import net.sf.latexdraw.instruments.ShapePlotCustomiser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import test.gui.CompositeGUIVoidCommand;
import test.gui.ShapePropModule;
import test.gui.TestPlotStyleGUI;

import com.google.inject.AbstractModule;

@RunWith(MockitoJUnitRunner.class)
public class TestHandPlotStyle extends TestPlotStyleGUI {
	@Override
	protected AbstractModule createModule() {
		return new ShapePropModule() {
			@Override
			protected void configure() {
				super.configure();
				pencil = mock(Pencil.class);
				bind(ShapePlotCustomiser.class).asEagerSingleton();
				bind(Hand.class).asEagerSingleton();
				bind(Pencil.class).toInstance(pencil);
			}
		};
	}

	@Test
	public void testControllerNotActivatedWhenSelectionEmpty() {
		new CompositeGUIVoidCommand(activateHand, updateIns, checkInsDeactivated).execute();
	}

	@Test
	public void testControllerActivatedWhenSelectionGrid() {
		new CompositeGUIVoidCommand(selectionAddPlot, activateHand, updateIns).execute();
		assertTrue(ins.isActivated());
		assertTrue(mainPane.isVisible());
	}

	@Test
	public void testControllerDeactivatedWhenSelectionNotGrid() {
		new CompositeGUIVoidCommand(selectionAddRec, activateHand, updateIns).execute();
		assertFalse(ins.isActivated());
		assertFalse(mainPane.isVisible());
	}

	@Test
	public void testControllerDeactivatedWhenSelectionEmpty() {
		new CompositeGUIVoidCommand(activateHand, updateIns).execute();
		assertFalse(ins.isActivated());
		assertFalse(mainPane.isVisible());
	}

	@Test
	public void testSelectDOTSStyleHand() {
		new CompositeGUIVoidCommand(activateHand, selectionAddDot, selectionAddPlot, selectionAddPlot, updateIns).execute();
		IPlotProp.PlotStyle style = plotStyleCB.getSelectionModel().getSelectedItem();
		selectplotStyleCB.execute(IPlotProp.PlotStyle.DOTS);
		IPlotProp.PlotStyle newStyle = plotStyleCB.getSelectionModel().getSelectedItem();
		assertEquals(plotStyleCB.getSelectionModel().getSelectedItem(), ((IPlot)hand.getCanvas().getDrawing().getSelection().getShapeAt(1)).getPlotStyle());
		assertEquals(plotStyleCB.getSelectionModel().getSelectedItem(), ((IPlot)hand.getCanvas().getDrawing().getSelection().getShapeAt(2)).getPlotStyle());
		assertNotEquals(style, newStyle);
	}

	@Test
	public void testSelectCCURVEStyleHand() {
		new CompositeGUIVoidCommand(activateHand, selectionAddDot, selectionAddPlot, selectionAddPlot, updateIns).execute();
		IPlotProp.PlotStyle style = plotStyleCB.getSelectionModel().getSelectedItem();
		selectplotStyleCB.execute(IPlotProp.PlotStyle.CCURVE);
		IPlotProp.PlotStyle newStyle = plotStyleCB.getSelectionModel().getSelectedItem();
		assertEquals(plotStyleCB.getSelectionModel().getSelectedItem(), ((IPlot)hand.getCanvas().getDrawing().getSelection().getShapeAt(1)).getPlotStyle());
		assertEquals(plotStyleCB.getSelectionModel().getSelectedItem(), ((IPlot)hand.getCanvas().getDrawing().getSelection().getShapeAt(2)).getPlotStyle());
		assertNotEquals(style, newStyle);
	}

	@Test
	public void testSelectPOLYGONStyleHand() {
		new CompositeGUIVoidCommand(activateHand, selectionAddDot, selectionAddPlot, selectionAddPlot, updateIns).execute();
		IPlotProp.PlotStyle style = plotStyleCB.getSelectionModel().getSelectedItem();
		selectplotStyleCB.execute(IPlotProp.PlotStyle.POLYGON);
		IPlotProp.PlotStyle newStyle = plotStyleCB.getSelectionModel().getSelectedItem();
		assertEquals(plotStyleCB.getSelectionModel().getSelectedItem(), ((IPlot)hand.getCanvas().getDrawing().getSelection().getShapeAt(1)).getPlotStyle());
		assertEquals(plotStyleCB.getSelectionModel().getSelectedItem(), ((IPlot)hand.getCanvas().getDrawing().getSelection().getShapeAt(2)).getPlotStyle());
		assertNotEquals(style, newStyle);
	}

	@Test
	public void testSelectECURVEStyleHand() {
		new CompositeGUIVoidCommand(activateHand, selectionAddDot, selectionAddPlot, selectionAddPlot, updateIns).execute();
		IPlotProp.PlotStyle style = plotStyleCB.getSelectionModel().getSelectedItem();
		selectplotStyleCB.execute(IPlotProp.PlotStyle.ECURVE);
		IPlotProp.PlotStyle newStyle = plotStyleCB.getSelectionModel().getSelectedItem();
		assertEquals(plotStyleCB.getSelectionModel().getSelectedItem(), ((IPlot)hand.getCanvas().getDrawing().getSelection().getShapeAt(1)).getPlotStyle());
		assertEquals(plotStyleCB.getSelectionModel().getSelectedItem(), ((IPlot)hand.getCanvas().getDrawing().getSelection().getShapeAt(2)).getPlotStyle());
		assertNotEquals(style, newStyle);
	}

	@Test
	public void testSelectCURVEStyleHand() {
		new CompositeGUIVoidCommand(activateHand, selectionAddDot, selectionAddPlot, selectionAddPlot, updateIns).execute();
		selectplotStyleCB.execute(IPlotProp.PlotStyle.ECURVE);
		IPlotProp.PlotStyle style = plotStyleCB.getSelectionModel().getSelectedItem();
		selectplotStyleCB.execute(IPlotProp.PlotStyle.CURVE);
		IPlotProp.PlotStyle newStyle = plotStyleCB.getSelectionModel().getSelectedItem();
		assertEquals(plotStyleCB.getSelectionModel().getSelectedItem(), ((IPlot)hand.getCanvas().getDrawing().getSelection().getShapeAt(1)).getPlotStyle());
		assertEquals(plotStyleCB.getSelectionModel().getSelectedItem(), ((IPlot)hand.getCanvas().getDrawing().getSelection().getShapeAt(2)).getPlotStyle());
		assertNotEquals(style, newStyle);
	}

	@Test
	public void testSelectLINEStyleHand() {
		new CompositeGUIVoidCommand(activateHand, selectionAddDot, selectionAddPlot, selectionAddPlot, updateIns).execute();
		IPlotProp.PlotStyle style = plotStyleCB.getSelectionModel().getSelectedItem();
		selectplotStyleCB.execute(IPlotProp.PlotStyle.LINE);
		IPlotProp.PlotStyle newStyle = plotStyleCB.getSelectionModel().getSelectedItem();
		assertEquals(plotStyleCB.getSelectionModel().getSelectedItem(), ((IPlot)hand.getCanvas().getDrawing().getSelection().getShapeAt(1)).getPlotStyle());
		assertEquals(plotStyleCB.getSelectionModel().getSelectedItem(), ((IPlot)hand.getCanvas().getDrawing().getSelection().getShapeAt(2)).getPlotStyle());
		assertNotEquals(style, newStyle);
	}

	@Test
	public void testincrementnbPtsSpinnerHand() {
		new CompositeGUIVoidCommand(activateHand, selectionAddDot, selectionAddPlot, selectionAddPlot, updateIns).execute();
		int val = nbPtsSpinner.getValue();
		incrementnbPtsSpinner.execute();
		assertEquals((int)nbPtsSpinner.getValue(), ((IPlot)hand.getCanvas().getDrawing().getSelection().getShapeAt(1)).getNbPlottedPoints());
		assertEquals((int)nbPtsSpinner.getValue(), ((IPlot)hand.getCanvas().getDrawing().getSelection().getShapeAt(2)).getNbPlottedPoints());
		assertNotEquals(val, (int)nbPtsSpinner.getValue());
	}

	@Test
	public void testincrementminXSpinnerHand() {
		new CompositeGUIVoidCommand(activateHand, selectionAddDot, selectionAddPlot, selectionAddPlot, updateIns).execute();
		double val = minXSpinner.getValue();
		incrementminXSpinner.execute();
		assertEquals(minXSpinner.getValue(), ((IPlot)hand.getCanvas().getDrawing().getSelection().getShapeAt(1)).getPlotMinX(), 0.0001);
		assertEquals(minXSpinner.getValue(), ((IPlot)hand.getCanvas().getDrawing().getSelection().getShapeAt(2)).getPlotMinX(), 0.0001);
		assertNotEquals(val, minXSpinner.getValue(), 0.0001);
	}

	@Test
	public void testincrementmaxXSpinnerHand() {
		new CompositeGUIVoidCommand(activateHand, selectionAddDot, selectionAddPlot, selectionAddPlot, updateIns).execute();
		double val = maxXSpinner.getValue();
		incrementmaxXSpinner.execute();
		assertEquals(maxXSpinner.getValue(), ((IPlot)hand.getCanvas().getDrawing().getSelection().getShapeAt(1)).getPlotMaxX(), 0.0001);
		assertEquals(maxXSpinner.getValue(), ((IPlot)hand.getCanvas().getDrawing().getSelection().getShapeAt(2)).getPlotMaxX(), 0.0001);
		assertNotEquals(val, maxXSpinner.getValue(), 0.0001);
	}

	@Test
	public void testincrementxScaleSpinnerHand() {
		new CompositeGUIVoidCommand(activateHand, selectionAddDot, selectionAddPlot, selectionAddPlot, updateIns).execute();
		double val = xScaleSpinner.getValue();
		incrementxScaleSpinner.execute();
		assertEquals(xScaleSpinner.getValue(), ((IPlot)hand.getCanvas().getDrawing().getSelection().getShapeAt(1)).getXScale(), 0.0001);
		assertEquals(xScaleSpinner.getValue(), ((IPlot)hand.getCanvas().getDrawing().getSelection().getShapeAt(2)).getXScale(), 0.0001);
		assertNotEquals(val, xScaleSpinner.getValue(), 0.0001);
	}

	@Test
	public void testincrementyScaleSpinnerHand() {
		new CompositeGUIVoidCommand(activateHand, selectionAddDot, selectionAddPlot, selectionAddPlot, updateIns).execute();
		double val = yScaleSpinner.getValue();
		incrementyScaleSpinner.execute();
		assertEquals(yScaleSpinner.getValue(), ((IPlot)hand.getCanvas().getDrawing().getSelection().getShapeAt(1)).getYScale(), 0.0001);
		assertEquals(yScaleSpinner.getValue(), ((IPlot)hand.getCanvas().getDrawing().getSelection().getShapeAt(2)).getYScale(), 0.0001);
		assertNotEquals(val, yScaleSpinner.getValue(), 0.0001);
	}

	@Test
	public void testSelectpolarCBHand() {
		new CompositeGUIVoidCommand(activateHand, selectionAddDot, selectionAddPlot, selectionAddPlot, updateIns).execute();
		boolean sel = polarCB.isSelected();
		clickpolarCB.execute();
		assertEquals(polarCB.isSelected(), ((IPlot)hand.getCanvas().getDrawing().getSelection().getShapeAt(1)).isPolar());
		assertEquals(polarCB.isSelected(), ((IPlot)hand.getCanvas().getDrawing().getSelection().getShapeAt(2)).isPolar());
		assertNotEquals(sel, polarCB.isSelected());
	}
}
