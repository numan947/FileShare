package com.example.numan947.androidend;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import coreJava.ServerPackage.Server;

public class ServerActivity extends AppCompatActivity {
    //server utils
    private Server server=null;
    private boolean isReceiving;
    private ArrayList<File>fileList=null;
    private MyAdapter adapter=null;
    private String defaultDir=null;
    private String ipAddress=null;

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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
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

        //todo change this
        this.defaultDir= Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"THIS IS SPARTA";
        File file=new File(defaultDir);
        if(!file.exists())file.mkdirs();

        clearVisualEffect();



    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isReceiving &&server!=null){
            addressView.setText("YOUR ADDRESS");
            server.shutdownServer();
            changeStates();
            server=null;
        }
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
        if(server!=null) {
            addressView.setText("YOUR ADDRESS");
            server.shutdownServer();
            changeStates();
            server=null;
        }
    }

    public void startServer(View view) {

        findIP();
        addressView.setText(ipAddress);
        if(ipAddress.equals("NO_NETWORK"))return;

        isReceiving=true;
        server=new Server(this,defaultDir);
        changeStates();
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


    public void updateList(File f) {
        if(f!=null)this.fileList.add(f);
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

    private void findIP()
    {
        Vector<String> addresses=new Vector<>();
        try {
            Enumeration<NetworkInterface> n=NetworkInterface.getNetworkInterfaces();
            while(n.hasMoreElements()){
                NetworkInterface e=n.nextElement();

                Enumeration<InetAddress>a=e.getInetAddresses();

                while (a.hasMoreElements()){
                    addresses.add(a.nextElement().getHostAddress());
                }
            }
        } catch (SocketException e) {
            //todo logger.log(Level.WARNING,"Problem in NetworkInterfaces "+new Date().toString());
        }
        this.ipAddress="NO_NETWORK";

        for(String s:addresses){

            if(s.matches("10\\.([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])\\.([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])\\.([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])")){
                this.ipAddress=s;
                break;
            }
            else if(s.matches("192.168\\.([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])\\.([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])")){
                this.ipAddress=s;
                break;
            }
            else if(s.matches("172\\.([1][6-9]|[2][0-9]|[3][0-1])\\.([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])\\.([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])")){
                this.ipAddress=s;
                break;
            }
        }
    }

    public void regenerateServer()
    {
        server=new Server(this,defaultDir);
    }

}
