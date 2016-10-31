package com.example.numan947.androidend;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import coreJava.ServerPackage.Server;

public class ServerActivity extends AppCompatActivity {
    //server utils
    private Server server=null;
    private boolean isReceiving;
    private ArrayList<File>fileList=null;
    private MyAdapter adapter=null;
    private String defaultDir=null;

    //layout
    private TextView fileNameView=null;
    private TextView totalView=null;
    private TextView addressView=null;
    private TextView doneView=null;
    private Button startButton=null;
    private Button stopButton=null;
    private Button clearButton=null;
    private ProgressBar pbar=null;
    ListView listView=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        //load from xml
        fileNameView=(TextView)findViewById(R.id.current_file_name);
        totalView=(TextView)findViewById(R.id.total_name);
        addressView=(TextView)findViewById(R.id.address_server);
        doneView=(TextView)findViewById(R.id.done_name);
        totalView=(TextView)findViewById(R.id.total_name);
        startButton=(Button)findViewById(R.id.start_server_btn);
        stopButton=(Button)findViewById(R.id.stop_server_btn);
        clearButton=(Button)findViewById(R.id.clear_button);
        pbar=(ProgressBar)findViewById(R.id.pbar);
        listView=(ListView)findViewById(R.id.listView_server);

        //setup start states
        isReceiving=false;

        fileList=new ArrayList<>();
        adapter=new MyAdapter(getBaseContext(),fileList);
        listView.setAdapter(adapter); 

        stopButton.setEnabled(false);

        File m=getBaseContext().getDir("NUMAN947", Context.MODE_PRIVATE);
        if(m.exists())m.delete();
        //this.defaultDir=m.getAbsolutePath();
        //Log.d("MYTAG","HERE "+m.exists()+" " +defaultDir);
        //Log.d("MYTAG","HERE "+m.exists()+" " + Environment.getExternalStorageDirectory().getAbsolutePath());


    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void clearList(View view) {
        fileList.clear();
        adapter.notifyDataSetChanged();
    }

    public void stopServer(View view) {
    }

    public void startServer(View view) {
       // server=new Server(this,)
    }




    public void changeStates()
    {
        if(startButton.isEnabled())startButton.setEnabled(false);
        else startButton.setEnabled(true);
        if(stopButton.isEnabled())stopButton.setEnabled(false);
        else stopButton.setEnabled(true);
    }

    public void setPrimaryVisualEffect( long done,long total)
    {

        double dd=(double)done/total;
        double ddd=(double) done/(1024*1024);
        String dddd=new DecimalFormat("#0.0").format(ddd)+" ";
        this.doneView.setText(dddd);
        this.pbar.setProgress((int)(dd*100));
    }


    public void setSecondaryVisualEffect( String fileName,long length)
    {
        double dd=(double)length/(1024*1024);
        this.doneView.setText("0 ");
        this.totalView.setText("/ "+new DecimalFormat("#0.0").format(dd));
        this.fileNameView.setText(fileName);
        this.pbar.setProgress(0);
    }

    public void clearVisualEffect()
    {
        doneView.setText("- ");
        fileNameView.setText(" - ");
        totalView.setText("/ -");
        pbar.setProgress(0);
    }


    public void updateList() {
        adapter.notifyDataSetChanged();
    }


    public void showMessage(String mini, String deta)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle(mini);
        builder1.setMessage(deta);

        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder1.setCancelable(false);
        //builder1.setNegativeButton()

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
