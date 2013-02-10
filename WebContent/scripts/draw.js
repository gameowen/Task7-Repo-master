$(function() {
    // Create the chart
    window.chart = new Highcharts.StockChart({
        chart : {
            renderTo : 'content'
        },

        rangeSelector : {
            selected : 1
        },

        title : {
            text : fundName
        },
        
        xAxis : {
        	title : {
        		text: "Time"
        	},
        },
        
        yAxis: {
        	title : {
        		text:"Price ($)"
        	},
        },
        
        series : [{
            name : fundName,
            data : financeData,
            marker : {
                enabled : true,
                radius : 3
            },
            shadow : true,
            tooltip : {
                valueDecimals : 2
            }
        }]
    });
});