package com.devcortes.primefaces.jsf.views.charts;

import java.io.File;
import java.io.FileInputStream;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;


@RestController
@ManagedBean
@Scope("view")
public class ChartGraphicImageController {
	
	private static final Logger LOGGER = Logger.getLogger(ChartGraphicImageController.class);

	private StreamedContent graphicText;
    
    private StreamedContent chart;
 
    @PostConstruct
    public void init() {
    	
        try {    
 
            //Chart
            JFreeChart jfreechart = ChartFactory.createPieChart("Cities", createDataset(), true, true, false);
            File chartFile = new File("dynamichart");
            ChartUtilities.saveChartAsPNG(chartFile, jfreechart, 375, 300);
            chart = new DefaultStreamedContent(new FileInputStream(chartFile), "image/png");
        } 
        catch (Exception e) {
        	
        	LOGGER.error(e.getMessage());
        }
    }
 
    public StreamedContent getGraphicText() {
    	
        return graphicText;
    }
         
    public StreamedContent getChart() {
    	
        return chart;
    }
     
    private PieDataset createDataset() {
    	
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("New York", 45.0);
        dataset.setValue("London", 15.0);
        dataset.setValue("Paris", 25.2);
        dataset.setValue("Berlin", 14.8);
 
        return dataset;
    }
}
