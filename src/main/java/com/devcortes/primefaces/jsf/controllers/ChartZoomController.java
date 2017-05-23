package com.devcortes.primefaces.jsf.controllers;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ManagedBean
@Scope("view")
public class ChartZoomController implements Serializable {

	private static final long serialVersionUID = 1L;

	private LineChartModel zoomModel;

	@PostConstruct
	public void init() {
		
		createZoomModel();
	}

	public LineChartModel getZoomModel() {
		
		return zoomModel;
	}

	private void createZoomModel() {
		
		zoomModel = initLinearModel();
		zoomModel.setTitle("Zoom");
		zoomModel.setZoom(true);
		zoomModel.setLegendPosition("e");
		Axis yAxis = zoomModel.getAxis(AxisType.Y);
		yAxis.setMin(0);
		yAxis.setMax(10);
	}

	private LineChartModel initLinearModel() {
		
		LineChartModel model = new LineChartModel();

		LineChartSeries series1 = new LineChartSeries();
		series1.setLabel("Series 1");

		series1.set(1, 2);
		series1.set(2, 1);
		series1.set(3, 3);
		series1.set(4, 6);
		series1.set(5, 8);

		LineChartSeries series2 = new LineChartSeries();
		series2.setLabel("Series 2");

		series2.set(1, 6);
		series2.set(2, 3);
		series2.set(3, 2);
		series2.set(4, 7);
		series2.set(5, 9);

		model.addSeries(series1);
		model.addSeries(series2);

		return model;
	}
}
