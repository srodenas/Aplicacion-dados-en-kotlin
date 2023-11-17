package com.example.tiradados

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.example.tiradados.databinding.ActivityMainBinding
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.random.Random

/*
Ejemplo de santiag Rodenas Herráiz
 srodher115@g.educaand.es
 PMDM 23/24
 */
class MainActivity : AppCompatActivity() {
    /*
    1.- ActivityMainBinding. Esta clase, es generada automáticamente por el sistema de vinculación.
    Contiene referencias de todos los elementos de la interfaz.
    2.- inflate(layoutInflater). Se utiliza para crear una instancia de la clase
    ActivityMainBinding. Toma un objeto layoutInflater, que es el encargado de convertir
    un fichero xml a objetos representados en memoria.
    3.- Con setContentView(binding.root), estamos cargando la interfaz en el ACtivity.
    HAcemos referencia al root, porque vista en forma de árbol, debemos indicarle donde
    comienza el arbol de vistas.
     */
    private lateinit var bindingMain : ActivityMainBinding  //Para referenciar a las vistas.
    private var sum : Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingMain.root)
        initEvent()
       // setContentView(R.layout.activity_main)
    }



    private fun initEvent() {
        bindingMain.txtResultado.visibility = View.INVISIBLE
        bindingMain.imageButton.setOnClickListener{
            bindingMain.txtResultado.visibility = View.VISIBLE
            game()  //comienza el juego

        }
    }

    //Comienza el juego
    private fun game(){
        sheduleRun() //planificamos las tiradas.
    }

    /*
    Lanzamos 5 veces los dados y lo programamos con 1sg de diferencia.
    A los 7 seguindos, sacamos la suma de la última tirada.
    Utilizamos un grupo de hilos.
    PARA LAS TIRADAS DE LOS DADOS, UTILIZAMOS UN SÓLO HILO DE EJECUCIÓN. Ese
    hilo lo creamos mediante un Executor. Dentro del for, lanzamos tantas tareas
    como veces queramos. Tener en cuenta, que en este caso, sería conveniente que la
    actualización de la UI estuviera perfectamente sincronizada. Si fueran diferentes hilos
    de ejecución, seguramente de esta forma tendría problemas, porque Android decide que la
    UI se lleve a cabo dentro del hilo principal. Para no ser más pesado, quiero que vosotros
    actualicés el método shdeuleRun, utilizando un Handler de la forma:

    private hander ....
    ...
    handler = Handler(Looper.getMAinLooper())
    ...
    schedulerExecutor = Executors.newSingleThereadSchedulerExecutor()
    schedulerExector.shedule(
        {
            handler.post({
                //tareas que hacer de la UI
            })
        },
        Tiempo_lanzamiento, Unidad_tiempo
    )

    Otra alternativa, es crear tantos executor como hilos quieras. Podéis utilizar la
    solución que prefiráis.
     */
    private fun sheduleRun() {

        val schedulerExecutor = Executors.newSingleThreadScheduledExecutor()
        val msc = 1000
        for (i in 1..5){//lanzamos 5 veces el dado
            schedulerExecutor.schedule(
                {
                throwDadoInTime()  //Lanzo los tres dados.
                },
                msc * i.toLong(), TimeUnit.MILLISECONDS)
        }

        schedulerExecutor.schedule({//El último hilo, es mostrar el resultado.
            viewResult()
        },
            msc  * 7.toLong(), TimeUnit.MILLISECONDS)

        schedulerExecutor.shutdown()  //Ya no aceptamos más hilos.

    }


/*
Método que lanza los tres dados a partir de 3 aleatorios.
 */
    private fun throwDadoInTime() {
        val numDados = Array(3){Random.nextInt(1, 6)}
        val imagViews : Array<ImageView> = arrayOf<ImageView>(
            bindingMain.imagviewDado1,
            bindingMain.imagviewDado2,
            bindingMain.imagviewDado3)

        sum = numDados.sum() //me quedo con la suma actual
        for (i in 0..3) //cambio las imagenes, a razón de los aleatorios.
            selectView(imagViews[i], numDados[i])

    }


    /*
    Método que dependiendo de la vista, carga una imagen de dado u otro.
     */
    private fun selectView(imgV: ImageView, v: Int) {
        when (v){
            1 -> imgV.setImageResource(R.drawable.dado1);
            2 -> imgV.setImageResource(R.drawable.dado2);
            3 -> imgV.setImageResource(R.drawable.dado3);
            4 -> imgV.setImageResource(R.drawable.dado4);
            5 -> imgV.setImageResource(R.drawable.dado5);
            6 -> imgV.setImageResource(R.drawable.dado6);
        }

    }


    /*
    Muestra los resultados, que es la suma de la última tirada.
     */
    private fun viewResult() {
        bindingMain.txtResultado.text = sum.toString()
        println(sum)
    }

}