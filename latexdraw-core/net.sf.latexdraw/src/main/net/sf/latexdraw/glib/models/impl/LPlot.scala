package net.sf.latexdraw.glib.models.impl

import java.awt.geom.Rectangle2D

import net.sf.latexdraw.glib.models.GLibUtilities
import net.sf.latexdraw.glib.models.ShapeFactory
import net.sf.latexdraw.glib.models.interfaces.prop.IDotProp
import net.sf.latexdraw.glib.models.interfaces.prop.IPlotProp
import net.sf.latexdraw.glib.models.interfaces.shape.Color
import net.sf.latexdraw.glib.models.interfaces.shape.DotStyle
import net.sf.latexdraw.glib.models.interfaces.shape.IPlot
import net.sf.latexdraw.glib.models.interfaces.shape.IPoint
import net.sf.latexdraw.glib.models.interfaces.shape.IShape
import net.sf.latexdraw.glib.models.interfaces.shape.PlotStyle
import net.sf.latexdraw.glib.models.interfaces.shape.Position
import net.sf.latexdraw.glib.views.pst.PSTricksConstants
import net.sf.latexdraw.parsers.ps.PSFunctionParser

/**
 * Implementation of the plotted function.
 * @since 3.2
 * @author Arnaud Blouin
 */
private[impl] class LPlot(pt:IPoint, var minX:Double, var maxX:Double, var equation:String, var polar:Boolean) extends LPositionShape(pt) with IPlot with LScalable {
	private var nbPoints:Int = 50
	private var style:PlotStyle = PlotStyle.CURVE
	private var parser:PSFunctionParser = new PSFunctionParser(equation)
	
	private var dotStyle = DotStyle.DOT
	private var dotDiametre = PSTricksConstants.DEFAULT_ARROW_DOTSIZE_DIM*IShape.PPC+PSTricksConstants.DEFAULT_ARROW_DOTSIZE_NUM

	require(GLibUtilities.isValidPoint(pt) && minX<maxX && GLibUtilities.isValidPoint(minX, maxX), "Parameter not valid: " + minX + " " + maxX + " " +GLibUtilities.isValidPoint(pt))

	
	override def copy(sh:IShape) {
		super.copy(sh)

		sh match {
			case plot:IPlotProp =>
				style = plot.getPlotStyle()
				nbPoints = plot.getNbPlottedPoints
				polar = plot.isPolar
				dotStyle = plot.getDotStyle()
				dotDiametre = plot.getDiametre
				minX = plot.getPlotMinX
				maxX = plot.getPlotMaxX
				xscale = plot.getXScale
				yscale = plot.getYScale
				setPlotEquation(plot.getPlotEquation)
      case dot:IDotProp =>
        dotStyle = dot.getDotStyle()
        dotDiametre = dot.getDiametre
			case _ =>
		}
	}
  
  
  override def mirrorVertical(origin:IPoint) {
    val gc = getGravityCentre
    if(GLibUtilities.isValidPoint(origin) && !origin.equals(gc, 0.0001))
      translate(0, gc.verticalSymmetry(origin).getY-gc.getY)
  }
  
  override def mirrorHorizontal(origin:IPoint) {
    val gc = getGravityCentre
    if(GLibUtilities.isValidPoint(origin) && !origin.equals(gc, 0.0001))
      translate(gc.horizontalSymmetry(origin).getX-gc.getX, 0)
  }
  

	override def setPlotStyle(style:PlotStyle) {
		if(style!=null) this.style = style
	}

	override def getPlotStyle = style

	override def setNbPlottedPoints(nbPts:Int) {
		if(nbPts>1)
			nbPoints = nbPts
	}

	override def isShowPtsable = false
	override def isThicknessable = style!=PlotStyle.DOTS
	override def isShadowable = style!=PlotStyle.DOTS
	override def isLineStylable = style!=PlotStyle.DOTS
	override def isInteriorStylable = style!=PlotStyle.DOTS
	override def isFillable = style!=PlotStyle.DOTS || dotStyle.isFillable
	override def isDbleBorderable = style!=PlotStyle.DOTS

	override def getPlottingStep = (maxX-minX)/(nbPoints-1)

	override def getTopLeftPoint = {
		val step = getPlottingStep
		val pos = getPosition
		ShapeFactory.createPoint(pos.getX+minX*IShape.PPC*xscale, pos.getY-(0 until nbPoints).map{x=>getY(minX+x*step)}.max*IShape.PPC*yscale)
	}

	override def getBottomRightPoint = {
		val step = getPlottingStep
		val pos = getPosition
		ShapeFactory.createPoint(pos.getX+maxX*IShape.PPC*xscale, pos.getY-(0 until nbPoints).map{x=>getY(minX+x*step)}.min*IShape.PPC*yscale)
	}

	override def getTopRightPoint = {
		val step = getPlottingStep
		val pos = getPosition
		ShapeFactory.createPoint(pos.getX+maxX*IShape.PPC*xscale, pos.getY-(0 until nbPoints).map{x=>getY(minX+x*step)}.max*IShape.PPC*yscale)
	}

	override def getBottomLeftPoint = {
		val step = getPlottingStep
		val pos = getPosition
		ShapeFactory.createPoint(pos.getX+minX*IShape.PPC*xscale, pos.getY-(0 until nbPoints).map{x=>getY(minX+x*step)}.min*IShape.PPC*yscale)
	}
	
	protected override def scaleSetPointsWithRatio(pts:java.util.List[IPoint], prevWidth:Double, prevHeight:Double, pos:Position, bound:Rectangle2D) {
	  scaleSetPoints(pts, prevWidth, prevHeight, pos, bound)
	}
	
	protected override def scaleSetPoints(pts:java.util.List[IPoint], prevWidth:Double, prevHeight:Double, pos:Position, bound:Rectangle2D) {
	  pos match {
		  case Position.EAST => getPtAt(0).translate(bound.getWidth-prevWidth, 0.0) 
		  case Position.WEST => getPtAt(0).translate(prevWidth-bound.getWidth, 0.0)
		  case Position.SOUTH => getPtAt(0).translate(0.0, bound.getHeight-prevHeight)
		  case Position.NORTH => getPtAt(0).translate(0.0, prevHeight-bound.getHeight)
		  case Position.NE => getPtAt(0).translate(bound.getWidth-prevWidth, prevHeight-bound.getHeight)
		  case Position.NW => getPtAt(0).translate(prevWidth-bound.getWidth, prevHeight-bound.getHeight)
		  case Position.SE => getPtAt(0).translate(bound.getWidth-prevWidth, bound.getHeight-prevHeight)
		  case Position.SW => getPtAt(0).translate(prevWidth-bound.getWidth, bound.getHeight-prevHeight) 
		}
	}

	override def getPosition = getPtAt(0)

	override def getNbPlottedPoints = nbPoints

	override def getY(x:Double) = parser.getY(x)

	override def getPlotEquation = equation

	override def setPlotEquation(eq:String) {
		if(eq!=null && !eq.isEmpty) {
			equation = eq
      parser = new PSFunctionParser(equation)
		}
	}

	override def getPlotMinX = minX

	override def getPlotMaxX = maxX

	override def setPolar(pol:Boolean) {
		polar = pol
	}

	override def isPolar = polar

	override def setPlotMaxX(x:Double) {
		if(GLibUtilities.isValidCoordinate(x) && x>minX)
			maxX = x
	}

	override def setPlotMinX(x:Double) {
		if(GLibUtilities.isValidCoordinate(x) && x<maxX)
			minX = x
	}
	
	
	def getDiametre(): Double = dotDiametre

  def getDotFillingCol(): Color = super.getFillingCol

  def getDotStyle(): DotStyle = dotStyle

  def setDiametre(diam: Double) {
  	if(diam>0.0 && GLibUtilities.isValidCoordinate(diam)) dotDiametre = diam
  }

  def setDotFillingCol(col: Color) {
  	setFillingCol(col)
  }

  def setDotStyle(dotst: DotStyle) {
  	if(dotst!=null) dotStyle = dotst
  }
}
