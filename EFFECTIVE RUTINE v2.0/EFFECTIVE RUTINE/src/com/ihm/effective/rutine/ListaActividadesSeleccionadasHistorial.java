package com.ihm.effective.rutine;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.ihm.bd.DatabaseHelper;
import com.ihm.graphics.Hmensual;
import com.ihm.graphics.Hsemanal;
import com.ihm.providers.ActividadProvider;
import com.ihm.providers.DetalleRutinaActividadProvider;
import com.ihm.providers.RutinaActividadProvider;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ListaActividadesSeleccionadasHistorial extends ListActivity {

	private ListView miLista;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividades_seleccionadas2);
        
        //String hoy=getDatePhone();
        
        String hoy=null;
        
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            hoy = extras.getString("fecha");
        }
        cambiarTitulo(hoy);
        updateRutina(hoy);
       
        // La URI content://contacts/people consulta todos los contactos
 		Uri uriContacts = DetalleRutinaActividadProvider.CONTENT_URI;
 		
		//Cursor c = managedQuery(uriContacts, null, null, null, null);
 		      
 		DatabaseHelper usdbh =
 	            new DatabaseHelper(this, "effective_rutine.db", null, 1);
 	 
 	        SQLiteDatabase db = usdbh.getWritableDatabase();
 		
		Cursor c =  db.rawQuery( "select id_detalle as _id,* from detalle_rutina_actividad d,actividad a,rutina_actividad r where d.id_actividad=a.id_actividad and d.id_rutina_actividad=r.id_rutina_actividad and r.tiempo_inicio='"+hoy+"'", null);

 		String[] columns = new String[] {
 				
 				ActividadProvider.NOMBRE,
 				
 				DetalleRutinaActividadProvider.CALORIAS_KCAL,
 				DetalleRutinaActividadProvider.DURACION_S,
 				ActividadProvider.KCAL,
 				
 				DetalleRutinaActividadProvider._ID
 				
 				 };
 		int[] views = new int[] {R.id.nombre, R.id.tcalorias, R.id.nminutos };

 		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,R.layout.row_a_seleccionadas, c, columns, views);
 		miLista=this.getListView();
 		miLista.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
 		//this.getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
 		miLista.setLongClickable(true);
 		miLista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
 		    public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {
 		        return onLongListItemClick(v,pos,id);
 		    }
 		});
 		miLista.setAdapter(adapter);
    }
	
	/*protected void onListItemClick(ListView l, View v, int position, long id) {
		   // TODO Auto-generated method stub
		   super.onListItemClick(l, v, position, id);

		   Toast.makeText(this,"Item Clicked", Toast.LENGTH_SHORT).show();
		  
		  }*/
	
	
	
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
    
    public void verCAlimentos(View view){
        Intent i = new Intent(this, CatalogoComidas.class);
     startActivity(i);
    }
    
    public void verCActividades(View view){
    	Bundle extras = getIntent().getExtras();
    	String codigo=null;
    	String fecha=null;
        if(extras!=null){
            codigo = extras.getString("codigo");
            fecha =extras.getString("fecha");
            Log.d("my tag2" ,"yo tengo el codigo "+codigo);
        }
        Intent i = new Intent(this, CatalogoActividadesHistorial.class);
        i.putExtra("codigo", codigo);
        i.putExtra("fecha", fecha);
     startActivity(i);
    }
    
    private String getDatePhone() 
    { 
        Calendar cal = new GregorianCalendar(); 
        Date date = cal.getTime(); 
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
        String formatteDate = df.format(date); 
        return formatteDate; 
    } 
    
    public void deleteSelected(View view) {
        //Obtengo los elementos seleccionados de mi lista
        SparseBooleanArray seleccionados = miLista.getCheckedItemPositions();
 
        if(seleccionados==null || seleccionados.size()==0){
            //Si no hab�a elementos seleccionados...
            Toast.makeText(this,"No hay elementos seleccionados",Toast.LENGTH_SHORT).show();
        }else{
            //si los hab�a, miro sus valores
 
        	Bundle extras = getIntent().getExtras();
        	String codigo=null;
        	String fecha=null;
            if(extras!=null){
                codigo = extras.getString("codigo"); 
                fecha=extras.getString("fecha");
            }           
 
            //Recorro my "array" de elementos seleccionados
            final int size=seleccionados.size();
            for (int i=0; i<size; i++) {
                //Si valueAt(i) es true, es que estaba seleccionado
                if (seleccionados.valueAt(i)) {
                    //en keyAt(i) obtengo su posici�n
                	Cursor c=(Cursor)miLista.getItemAtPosition(seleccionados.keyAt(i));
                	
                    //resultado.append("El elemento "+c.getString(1)+" estaba seleccionado\n");
                    getContentResolver().delete(Uri.parse("content://effectiveRutine.proveedor.DetalleRutinaActividad/detalle_rutina_actividad/"+c.getString(1)),  null, null);
                    //miLista.refreshDrawableState();
                    
                    Intent in = new Intent(this, ListaActividadesSeleccionadasHistorial.class);
                    in.putExtra("codigo", codigo);
                    in.putExtra("fecha", fecha);
                    
                 startActivity(in);
                    
                }
            }
            
        }
    }
    
    public void updateRutina(String hoy){
    	String sum="0";
        //select sum (d.calorias_kcal) as sum from detalle_rutina_alimento d,alimento a,rutina_alimento r where d.id_alimento=a.id_alimento and d.id_rutina_alimento=r.id_rutina_alimento and r.id_regimen=2
    	Bundle extras = getIntent().getExtras();
    	String codigo=null;
        if(extras!=null){
            codigo = extras.getString("codigo");           
        } 
        
    	DatabaseHelper usdbh =
 	            new DatabaseHelper(this, "effective_rutine.db", null, 1);
 	 
 	    SQLiteDatabase db = usdbh.getWritableDatabase();
 		
		Cursor c =  db.rawQuery("select sum (d.calorias_kcal) as sum from detalle_rutina_actividad d,actividad a,rutina_actividad r where d.id_actividad=a.id_actividad and d.id_rutina_actividad=r.id_rutina_actividad and r.tiempo_inicio='"+hoy+"' ",null);
		       
		if ( c.moveToFirst() ) {
			Log.d("my tag2" ,"yo sumo "+sum);
			sum=c.getString(c.getColumnIndex("sum"));
		}
		if(sum==null){
			sum="0";
			Log.d("my tag2" ,"no sumo nada por eso soy un "+sum);
		}
		ContentValues values = new ContentValues();
		
		values.put(
				RutinaActividadProvider.TOTAL_CALORIAS_KCAL,sum);
		
		Log.d("my tag2" ,"yo sumo ahora "+sum);
		TextView tcal = (TextView) findViewById(R.id.tcaloriasp);
		tcal.setText(sum);
		
		String uriString="content://"+RutinaActividadProvider.PROVIDER_NAME+"/"+RutinaActividadProvider.ENTIDAD+"/"+codigo;
		Uri CONTENT_URI = Uri.parse(uriString);
		getContentResolver().update(CONTENT_URI, values,null,null);
		
		usdbh.close();
		
    } 
    
    protected boolean onLongListItemClick(View v, int pos, long id) {
        
        Cursor c=(Cursor)miLista.getItemAtPosition(pos);
        Log.d("TAG", "onLongListItemClick");
        Bundle extras = getIntent().getExtras();
    	String codigo=null;
    	String fecha=null;
        if(extras!=null){
            codigo = extras.getString("codigo");  
            fecha=extras.getString("fecha");
        } 
        
        Intent i = new Intent(this, MultiplicadorActividadesHistorial.class);
        i.putExtra("codigo", codigo);
        i.putExtra("nombre", c.getString(c.getColumnIndex("nombre")));
        i.putExtra("km", c.getString(c.getColumnIndex("kcal")));
        i.putExtra("tk", c.getString(c.getColumnIndex("calorias_kcal")));
        Log.d("TAG", "enviando tk: "+c.getString(c.getColumnIndex("calorias_kcal")));
        //i.putExtra("minutos", c.getString(c.getColumnIndex("duracion_s")));
        i.putExtra("id", c.getString(c.getColumnIndex("id_detalle")));
        i.putExtra("fecha",fecha );
       
        
        startActivity(i);
        return true;
    }
    
    public void verAyuda(View view){
        Intent i = new Intent(this, Ayuda.class);
     startActivity(i);
    }
    
    public void cambiarTitulo(String fecha){
    	Log.d("TAG", "yo cambio los titulos");
    	TextView title=(TextView)findViewById(R.id.title);
    	title.setText("Actividades del "+fecha);
    }
}

