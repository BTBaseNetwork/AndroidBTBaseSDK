package mobi.btbase.androidbtbasesdk.testapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import mobi.btbase.btbasesdk.BTBaseSDK;
import mobi.btbase.btbasesdk.GameWallActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BTBaseSDK.start(getApplicationContext());
        startActivity(new Intent(this, GameWallActivity.class));
    }
}
