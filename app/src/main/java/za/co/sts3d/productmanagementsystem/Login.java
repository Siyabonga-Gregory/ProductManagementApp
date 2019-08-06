package za.co.sts3d.productmanagementsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.View;
import android.widget.Toast;
import android.content.Intent;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
    }

    public void Login(View view)
    {
        Intent intent= new Intent(this,Scan.class);
        Toast.makeText(this  , "Success", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }
}
