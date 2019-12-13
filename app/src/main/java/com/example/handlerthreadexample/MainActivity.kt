package com.example.handlerthreadexample

import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"

        private const val HELLO_WORLD = 0
    }

    lateinit var taskHandlerThread: HandlerThread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        taskHandlerThread = HandlerThread("TaskHandlerThread").also { it.start() }

        post_runnable.setOnClickListener{
            val handler = Handler(taskHandlerThread.looper)
            handler.post{
                //some heavy work off Main thread
                val x = 23 * 4
                runOnUiThread{
                    Toast.makeText(this, "The value is: $x", Toast.LENGTH_SHORT).show()
                }

                //region
//                // We can also post to Main thread
//                val uIHanlder = Handler(Looper.getMainLooper())
//                uIHanlder.post{
//                    Toast.makeText(this, "From uIhandler runnable", Toast.LENGTH_SHORT).show()
//                }
                //endregion
            }
        }

        //region
        val handler = object: Handler(taskHandlerThread.looper){
            override fun handleMessage(msg: Message){
                if(msg.what == HELLO_WORLD){
                    runOnUiThread{
                        Toast.makeText(this@MainActivity, "Hello World! from runOnUiThread",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        post_message.setOnClickListener{
            handler.sendEmptyMessage(HELLO_WORLD)
        }
        //endregion

    }

    override fun onDestroy() {
        super.onDestroy()

        taskHandlerThread.quit()
    }
}
