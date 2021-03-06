package com.ihm.graphics;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.ihm.bd.DatabaseHelper;
import com.ihm.effective.rutine.Ayuda;
import com.ihm.effective.rutine.CambiarPeso;
import com.ihm.effective.rutine.ComidasDisponibles;
import com.ihm.effective.rutine.HistorialConsulta;
import com.ihm.effective.rutine.ListaActividades;
import com.ihm.effective.rutine.ListaActividadesSeleccionadas;
import com.ihm.effective.rutine.ListaComidas;
import com.ihm.effective.rutine.MainActivity;
import com.ihm.effective.rutine.R;
import com.ihm.effective.rutine.Rutina;
import com.ihm.providers.RutinaActividadProvider;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Hsemanal extends Activity{
	
private GraphicalView mChartView;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        setContentView(R.layout.historial_semanal2);
    }
	
	protected void onResume() {
	    super.onResume();
	    if (mChartView == null) {
		    LinearLayout layout = (LinearLayout) findViewById(R.id.chart); 
	        
		    //SELECT sum (a.total_calorias_kcal) as sum, a.tiempo_inicio, p.total_calorias_kcal  FROM rutina_alimento a ,rutina_actividad p where a.tiempo_inicio=p.tiempo_inicio GROUP BY a.tiempo_inicio ORDER BY a.tiempo_inicio   desc limit 3
		    DatabaseHelper usdbh =
	 	            new DatabaseHelper(this, "effective_rutine.db", null, 1);
	 	 
	 	    SQLiteDatabase db = usdbh.getWritableDatabase();
	 		
			Cursor c =  db.rawQuery("SELECT sum (a.total_calorias_kcal) as sum, a.tiempo_inicio, p.total_calorias_kcal  FROM rutina_alimento a ,rutina_actividad p where a.tiempo_inicio=p.tiempo_inicio GROUP BY a.tiempo_inicio ORDER BY a.tiempo_inicio",null);
			
			int tamanio=0;
			if ( c.moveToFirst() ) {
				tamanio=c.getCount();
				if(tamanio>7){
					tamanio=7;
				}
			}
			if(tamanio!=0){
				Cursor c2 =  db.rawQuery("SELECT sum (a.total_calorias_kcal) as sum, a.tiempo_inicio, p.total_calorias_kcal  FROM rutina_alimento a ,rutina_actividad p where a.tiempo_inicio=p.tiempo_inicio GROUP BY a.tiempo_inicio ORDER BY a.tiempo_inicio   asc limit "+tamanio,null);
				int y[];
				String fechas[];
				String dias[];
		        y=new int[tamanio];
		        dias=new String[tamanio];
		        fechas=new String[tamanio];
		       
		        Log.d("algo", "bueno2");
				for(int i=0;i<tamanio;i++){
					c2.moveToPosition(i);
					double ganadas=Double.parseDouble(c2.getString(0));
					double perdidas=Double.parseDouble(c2.getString(2));
					double netas=ganadas-perdidas;
					y[i]=(int) netas;
					fechas[i]=c2.getString(1);
					dias[i]=c2.getString(1).substring(8);
					Log.d("algo", "bueno");
				}
				
				String titulo= "Historial desde " +fechas[0]+" hasta "+fechas[tamanio-1];
			    //cambiarTitulo(titulo);
			
			
			
		    
			
		    
		        //int[] y = { 12, 13, 10, 14, 10, 12, 13};
			    /*int y[];
		        y=new int[7];
		        y[0]=12;
		        y[1]=13;
		        y[2]=10;
		        y[3]=14;
		        y[4]=10;
		        y[5]=12;
		        y[6]=13;*/
		       
				CategorySeries series = new CategorySeries("Dias de la semana");
				for (int i = 0; i < y.length; i++) {
					series.add("Bar " + (i+1), y[i]);
					//renderer.addTextLabel(i+1, dias[i]);
				}
				
				/*// Bar 2
				int[] y2 = { 224, 235, 243, 256, 234, 223, 242, 234, 223, 243, 234, 274 };
				CategorySeries series2 = new CategorySeries("Demo Bar Graph 2");
				for (int i = 0; i < y.length; i++) {
					series2.add("Bar " + (i+1), y2[i]);
				}*/
				
				XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
				dataset.addSeries(series.toXYSeries());
				//dataset.addSeries(series2.toXYSeries());
		
				// This is how the "Graph" itself will look like
				XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
				mRenderer.setChartTitle(titulo);
				mRenderer.setXTitle("Calorias netas");
				mRenderer.setYTitle("Calorias netas");
				mRenderer.setAxesColor(Color.BLACK);
			    mRenderer.setLabelsColor(Color.BLACK);
			    // Customize bar 1
				XYSeriesRenderer renderer = new XYSeriesRenderer();
			    renderer.setDisplayChartValues(true);
			    renderer.setColor(Color.BLUE);
			    renderer.setChartValuesSpacing((float) 0.5);
			    renderer.setChartValuesTextSize(20);
			    
			    
			    //mRenderer.setYAxisMin(50);
			    //mRenderer.setYAxisMax(200);
			    mRenderer.addSeriesRenderer(renderer);
			    /*// Customize bar 2
			    XYSeriesRenderer renderer2 = new XYSeriesRenderer();
			    renderer.setColor(Color.CYAN);
			    renderer.setDisplayChartValues(true);
			    renderer.setChartValuesSpacing((float) 0.5);
			    mRenderer.addSeriesRenderer(renderer2);*/
			    
			    mRenderer.setApplyBackgroundColor(true);
			    mRenderer.setBackgroundColor(Color.WHITE);
			    mRenderer.setMarginsColor(Color.WHITE);
			    
			    for (int i = 0; i < y.length; i++) {
					//series.add("Bar " + (i+1), y[i]);
					mRenderer.addTextLabel(i+1, dias[i]);
				}
			    
			    mRenderer.setXLabelsAlign(Align.RIGHT);
			    mRenderer.setXLabels(0);
			    mRenderer.setLabelsTextSize(20);
			    mRenderer.setXLabelsColor(Color.BLACK);
			    
				//mChartView= ChartFactory.getBarChartView(this, dataset,mRenderer, Type.DEFAULT);
				mChartView= ChartFactory.getLineChartView(this, dataset,mRenderer);
				layout.addView(mChartView);
			}
			usdbh.close();
		}
	    
	    else{
	        mChartView.repaint(); 

	    }
		}
	
	public void cambiarPeso(View view){
        Intent i = new Intent(this, CambiarPeso.class);
     startActivity(i);
    }
    
    public void verComidas(View view){
        Intent i = new Intent(this, ComidasDisponibles.class);
     startActivity(i);
    }
    
    public void verHConsulta(View view){
        Intent i = new Intent(this, HistorialConsulta.class);
     startActivity(i);
    }
    
    public void verRutina(View view){
        Intent i = new Intent(this, Rutina.class);
     startActivity(i);
    }
    
    public void verHMensual(View view){
        Intent i = new Intent(this, Hmensual.class);
     startActivity(i);
    }
    
    public void verHSemanal(View view){
        Intent i = new Intent(this, Hsemanal.class);
     startActivity(i);
    }
    
    public void verLActividades(View view){
        Intent i = new Intent(this, ListaActividades.class);
     startActivity(i);
    }
    
    public void verLComidas(View view){
        Intent i = new Intent(this, ListaComidas.class);
     startActivity(i);
    }
    
    public void verHome(View view){
        Intent i = new Intent(this, MainActivity.class);
     startActivity(i);
    }
    
    public void verAyuda(View view){
        Intent i = new Intent(this, Ayuda.class);
     startActivity(i);
    }
	
    
    public void verLASeleccionadas(View view){
        	
        	String fecha=getDatePhone(); 
        	String existo=existeId(fecha);
        	if(existo.equals("no")){
        		ContentValues values2 = new ContentValues();
          		
        		values2.put(RutinaActividadProvider.TIEMPO_INICIO,fecha);
        		values2.put(RutinaActividadProvider.TIEMPO_TOTAL_S,0);
        		values2.put(RutinaActividadProvider.TOTAL_CALORIAS_KCAL,0);
        		    		
        		Uri uriNueva = getContentResolver().insert(RutinaActividadProvider.CONTENT_URI, values2);
        		
        		//IO implemento
        		managedQuery(uriNueva, null, null, null, null);	
        		
        		existo=existeId(fecha); 
        		Log.d("my tag2" ,"yo soy la rutina_actividad "+existo);
        		Intent i = new Intent(this, ListaActividadesSeleccionadas.class);
        		i.putExtra("codigo", existo);
        		
                startActivity(i);
        	}
        	else{
        		
                Intent i = new Intent(this, ListaActividadesSeleccionadas.class);
                i.putExtra("codigo", existo);
               
                Log.d("my tag2" ,"enviando "+existo);
                startActivity(i);
        	}
            
        }

    	private String getDatePhone() 
    	{ 
    	    Calendar cal = new GregorianCalendar(); 
    	    Date date = cal.getTime(); 
    	    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
    	    String formatteDate = df.format(date); 
    	    return formatteDate; 
    	} 
    	
    	private void cambiarTitulo(String titulo){
    		TextView t=(TextView)findViewById(R.id.titulo);
    		t.setText(titulo);
    	}
    	
    	private String existeId(String fecha) 
    	{ 
    		DatabaseHelper usdbh =  new DatabaseHelper(this, "effective_rutine.db", null, 1);
    		  
    		SQLiteDatabase db = usdbh.getWritableDatabase();
    		Cursor c =  db.rawQuery( "select * from rutina_actividad where tiempo_inicio='"+fecha+"'", null);
    	  	//String g=c.getString(c.getColumnIndex("id_rutina_alimento"));
    		if ( c.moveToFirst() ) {
    			String g=c.getString(c.getColumnIndex("id_rutina_actividad"));
    			Log.d("my tag2" ,"la actividad si existe");
    			usdbh.close();
    			return g;
    		} else {
    			String g="no";
    			Log.d("my tag2" ,"la actividad no existe");
    			usdbh.close();
    			return g;
    		}
    	} 
        
       

}
