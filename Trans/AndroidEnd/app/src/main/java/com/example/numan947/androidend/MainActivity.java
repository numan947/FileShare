package com.example.numan947.androidend;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        File logDir=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"Trans"+File.separator+".LOG");
        if(!logDir.exists())logDir.mkdirs();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        if (id == R.id.save_path) {

            builder1.setTitle("Save Path");
            builder1.setMessage("Your received files are saved at "+Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"Trans");

            builder1.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            builder1.setCancelable(false);
            AlertDialog alert11 = builder1.create();
            alert11.show();
        }else if(id==R.id.about){
            builder1.setTitle("About This App");
            builder1.setMessage("This application is made by S.Mahmudul Hasan, Roll: 1305043," +
                    " current student of Bangladesh University Of Engineering and Technology," +
                    " as a side project. Few open source libraries (especially a filechooser library) were used to build this application." +
                    "Now while we're at it, the naming credit goes to none other than Akib Ahmed (1305074) aka Akibbai" +
                    "Thanks to him, this application has been given a nice name.");

            builder1.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            builder1.setCancelable(false);
            AlertDialog alert11 = builder1.create();
            alert11.show();        }


        return super.onOptionsItemSelected(item);
    }


    public void selectReceive(View view)
    {
        Intent intent=new Intent(this,ServerActivity.class);
        startActivity(intent);
    }

    public void selectSend(View view)
    {
        Intent intent=new Intent(this,ClientActivity.class);
        startActivity(intent);
    }


}
