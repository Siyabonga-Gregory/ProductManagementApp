package za.co.sts3d.productmanagementsystem;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;

public class Scan extends AppCompatActivity {

    private DrawerLayout mDrawer;
    Dialog  itemDetails;

    private ActionBarDrawerToggle mToggle;
    TextView txtAdd;
    TextView txtClose;

    //Variables
    SurfaceView cameraView;
    TextView txtResult;
    CameraSource cameraSource;
    final int RequestCameraPermissionID=100;

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        txtResult.setText("Requesting Permission    :  " + requestCode);
        Toast.makeText(this, "Requesting Permission", Toast.LENGTH_SHORT).show();
        switch (requestCode)
        {
            case  RequestCameraPermissionID:
            {
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED)
                {
                    if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
                    {
                        Toast.makeText(this, "Permission not granted", Toast.LENGTH_LONG).show();
                        return;
                    }
                    try
                    {
                        cameraSource.start(cameraView.getHolder());
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);




        cameraView=(SurfaceView) findViewById(R.id.surfaceView);
        txtResult=(TextView)findViewById(R.id.txtResult);

        TextRecognizer txtRecognizer= new TextRecognizer.Builder(getApplicationContext()).build();
        if(!txtRecognizer.isOperational())
        {
            Log.w("Result","Detector dependencies are not yet loaded     " + txtRecognizer.isOperational());
            Toast.makeText(this, "Detector dependencies are not yet loaded", Toast.LENGTH_LONG).show();
            //txtResult.setText("Detector dependencies are not yet loaded");

            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

            if (hasLowStorage) {
                Toast.makeText(this, "Low Storage   " + hasLowStorage, Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            txtResult.setText("Else part");
            cameraSource=new CameraSource.Builder(getApplicationContext(),txtRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280,1024)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();

            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try
                    {
                        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
                        {
                            ActivityCompat.requestPermissions(Scan.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    RequestCameraPermissionID);
                            return;
                        }
                        cameraSource.start(cameraView.getHolder());
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    cameraSource.stop();
                }
            });

            txtRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items=detections.getDetectedItems();
                    if(items.size()!=0)
                    {
                        txtResult.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder builder=new StringBuilder();
                                for(int i=0;i<items.size();++i)
                                {
                                    TextBlock item=items.valueAt(i);
                                    builder.append(item.getValue());
                                    builder.append("\n");
                                }

                            }
                        });
                    }
                    else
                    {
                        txtResult.setText("Invalid Product Code");
                    }
                }
            });
        }





        itemDetails=new Dialog(this);

        mDrawer=(DrawerLayout) findViewById(R.id.drawer);
        mToggle=new ActionBarDrawerToggle(this,mDrawer,R.string.open,R.string.close);

        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtAdd=(TextView)findViewById(R.id.txtResult);
        txtAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                itemDetails.setContentView(R.layout.item_details);
                txtClose=(TextView)itemDetails.findViewById(R.id.txtClose);
                itemDetails.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                itemDetails.show();
            }
        });

       /* txtClose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                itemDetails.dismiss();
            }
        });*/
    }

    public boolean OnOptionItemSelected(MenuItem item)
    {
        if(mToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

   /* public void ShowItemDetails(View view)
    {
        itemDetails.setContentView(R.layout.item_details);

        txtClose=(TextView)itemDetails.findViewById(R.id.txtClose);

        txtClose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                itemDetails.dismiss();
            }
        });
        itemDetails.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        itemDetails.show();
    }*/
}
