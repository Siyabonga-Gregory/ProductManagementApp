package za.co.sts3d.productmanagementsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }


    public void ScanItem(View view)
    {
        Intent intent= new Intent(this,Scan.class);
        Toast.makeText(this  , "Success", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }
}
