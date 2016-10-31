package com.example.numan947.androidend;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import coreJava.ClientPackage.Client;
import paul.arian.fileselector.FileSelectionActivity;

public class ClientActivity extends AppCompatActivity {
    private ArrayList <File>fileList=null;
    private ListView listView=null;
    private MyAdapter adapter=null;
    private EditText addressText=null;
    private TextView fileNameView=null;
    private TextView totalView=null;
    private TextView doneView=null;
    private ProgressBar pbar=null;
    private Button selectButton=null;
    private Button sendButton=null;
    private Button stopButton=null;

    private Client client=null;
    private boolean isSending;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        fileList=new ArrayList<>();
        listView=(ListView)findViewById(R.id.listView_client);
        addressText=(EditText)findViewById(R.id.address);
        fileNameView=(TextView)findViewById(R.id.current_file_name);
        totalView=(TextView)findViewById(R.id.total_name);
        doneView=(TextView)findViewById(R.id.done_name);
        pbar=(ProgressBar)findViewById(R.id.pbar);
        selectButton=(Button)findViewById(R.id.select_files_button);
        sendButton=(Button)findViewById(R.id.stop_server_btn);
        stopButton=(Button)findViewById(R.id.stop_button);

        //inital states
        stopButton.setEnabled(false);
        clearVisualEffect();
        pbar.setMax(100);
        isSending=false;

        adapter=new MyAdapter(getBaseContext(),fileList);
        initiateListView();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if(v.getId()==R.id.listView_client){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle("Options");
            String[] menuItems = getResources().getStringArray(R.array.context_menu);
            for (int i = 0; i<menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String[] menuItems = getResources().getStringArray(R.array.context_menu);
        String menuItemName = menuItems[menuItemIndex];

        if(menuItemName.equals("Delete")){
            fileList.remove(info.position);
            adapter.notifyDataSetChanged();
        }
        else if(menuItemName.equals("Show Path")){
            Dialog d=new Dialog(this);
            TextView tv=new TextView(this);
            tv.setText(fileList.get(info.position).getAbsolutePath());
            d.setContentView(tv);
            d.show();
        }
        return true;
    }

    private void initiateListView() {
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
    }

    //TODO do this
    @Override
    protected void onPause() {
        super.onPause();
        if(isSending&&client!=null){
            client.setStop(true);
            client=null;
            changeStates();
        }
    }
    //TODO do this
    @Override
    protected void onResume() {
        super.onResume();
        isSending=false;
    }
    //TODO do this
    @Override
    public void onBackPressed() {
        addressText.clearFocus();
        super.onBackPressed();

    }
    //TODO do this
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==0&&resultCode==RESULT_OK){
            ArrayList<File>aa=(ArrayList<File>)data.getSerializableExtra("upload");
            for(File a:aa){
                Log.d("MYTAG",a.getName());
                fileList.add(a);
                Log.d("MYTAG",String.valueOf(fileList.size()));
            }
            adapter.notifyDataSetChanged();


        }


    }

    public void selectFile(View view) {
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        addressText.clearFocus();
        Intent intent=new Intent(getBaseContext(), FileSelectionActivity.class);
        startActivityForResult(intent,0);
    }

    public void sendFile(View view) {
        String address=addressText.getText().toString();
        addressText.clearFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        //validation check
        boolean valid=address.matches("10\\.([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])\\.([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])\\.([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])");
        valid|=address.matches("192.168\\.([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])\\.([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])");
        valid|=address.matches("172\\.([1][6-9]|[2][0-9]|[3][0-1])\\.([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])\\.([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])");
        if(!valid||fileList.size()==0){
            showMessage("ERROR!!","Either the address is invalid or the list is empty!");
            return;
        }

        if(client==null){
            client=new Client(this,address,fileList);
            changeStates();
        }
        isSending=true;

    }

    public void stopSending(View view) {
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        if(client!=null){
            client.setStop(true);
            client=null;
            changeStates();
            isSending=false;
        }
        addressText.clearFocus();

    }

    public void changeStates()
    {
        if(sendButton.isEnabled())sendButton.setEnabled(false);
        else sendButton.setEnabled(true);
        if(stopButton.isEnabled())stopButton.setEnabled(false);
        else stopButton.setEnabled(true);
        if(selectButton.isEnabled())selectButton.setEnabled(false);
        else selectButton.setEnabled(true);
        if(addressText.isEnabled())addressText.setEnabled(false);
        else addressText.setEnabled(true);
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
