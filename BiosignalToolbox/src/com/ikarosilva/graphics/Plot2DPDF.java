package com.ikarosilva.graphics;
/* ----------------------
 * Modified from: XYBlockChartDemo1.java (C) Copyright 2006, 2007, by Object Refinery Limited.
 * ----------------------
 * 
 *
 */
import java.awt.Color;
import java.awt.Font;
import java.util.Random;

import javax.swing.JPanel;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.GrayPaintScale;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.title.PaintScaleLegend;
import org.jfree.data.DomainOrder;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.xy.XYZDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;

class PDFXYZDataset implements XYZDataset {
	private double[][] data;
	public PDFXYZDataset(double[][] data) {
		this.data=data;
	}
	public int getSeriesCount() {
        return 1;
    }
    public int getItemCount(int series) {
        return data[0].length;
    }
    public Number getX(int series, int item) {
        return new Double(getXValue(series, item));
    }
    public double getXValue(int series, int item) {
        return item;
    }
    public Number getY(int series, int item) {
        return new Double(getYValue(series, item));
    }
    public double getYValue(int series, int item) {
        return item;
    }
    public Number getZ(int series, int item) {
        return new Double(getZValue(series, item));
    }
    public double getZValue(int series, int item) {
        int x = (int) getXValue(series, item);
        int y = (int) getYValue(series, item);
        return data[x][y];
    }
    public void addChangeListener(DatasetChangeListener listener) {
        // ignore - this dataset never changes
    }
    public void removeChangeListener(DatasetChangeListener listener) {
        // ignore
    }
    public DatasetGroup getGroup() {
        return null;
    }
    public void setGroup(DatasetGroup group) {
        // ignore
    }
    public Comparable getSeriesKey(int series) {
        return "2D-PDF";
    }
    public int indexOf(Comparable seriesKey) {
        return 0;
    }
    public DomainOrder getDomainOrder() {
        return DomainOrder.ASCENDING;
    }
}

public class Plot2DPDF extends ApplicationFrame {

	public Plot2DPDF(String title, double[][] data) {
		super(title);
		JPanel chartPanel = createDemoPanel(data);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		setContentPane(chartPanel);
	}

	private static JFreeChart createChart(XYZDataset dataset) {
		NumberAxis xAxis = new NumberAxis("X");
		xAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		xAxis.setLowerMargin(0.0);
		xAxis.setUpperMargin(0.0);
		xAxis.setAxisLinePaint(Color.white);
		xAxis.setTickMarkPaint(Color.white);

		NumberAxis yAxis = new NumberAxis("Y");
		yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		yAxis.setLowerMargin(0.0);
		yAxis.setUpperMargin(0.0);
		yAxis.setAxisLinePaint(Color.white);
		yAxis.setTickMarkPaint(Color.white);
		XYBlockRenderer renderer = new XYBlockRenderer();
		PaintScale scale = new GrayPaintScale(-2.0, 1.0);
		renderer.setPaintScale(scale);
		XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinesVisible(false);
		plot.setRangeGridlinePaint(Color.white);
		plot.setAxisOffset(new RectangleInsets(5, 5, 5, 5));
		plot.setOutlinePaint(Color.blue);
		JFreeChart chart = new JFreeChart("2D PDF", plot);
		chart.removeLegend();
		NumberAxis scaleAxis = new NumberAxis("Scale");
		scaleAxis.setAxisLinePaint(Color.white);
		scaleAxis.setTickMarkPaint(Color.white);
		scaleAxis.setTickLabelFont(new Font("Dialog", Font.PLAIN, 5));
		PaintScaleLegend legend = new PaintScaleLegend(new GrayPaintScale(),
				scaleAxis);
		legend.setStripOutlineVisible(false);
		legend.setSubdivisionCount(20);
		legend.setAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
		legend.setAxisOffset(5.0);
		legend.setMargin(new RectangleInsets(5, 5, 5, 5));
		legend.setFrame(new BlockBorder(Color.red));
		legend.setPadding(new RectangleInsets(10, 10, 10, 10));
		legend.setStripWidth(10);
		legend.setPosition(RectangleEdge.LEFT);
		chart.addSubtitle(legend);
		ChartUtilities.applyCurrentTheme(chart);
		return chart;
	}

	private static XYZDataset createDataset(double[][] data) {
        return new PDFXYZDataset(data);
    }
	 
	public static JPanel createDemoPanel(double[][] data) {
		return new ChartPanel(createChart(createDataset(data)));
	}

	public static void main(String[] args) {

		int N=100;
		int bins=100;
		double[] x= new double[N];
		double[] y= new double[N];
		double[][] z= new double [N][N];
		Random generator = new Random(12345678L);//Random(System.currentTimeMillis());
		for (int i = 0; i < N; i++) {
			x[i]=generator.nextGaussian();
			for (int k = 0; k < N; k++) {
				y[k]=generator.nextGaussian();
				z[i][k]=x[i]*y[k];
			}
		}

		Plot2DPDF demo = new Plot2DPDF(
				"PDF",z);
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
	}

}