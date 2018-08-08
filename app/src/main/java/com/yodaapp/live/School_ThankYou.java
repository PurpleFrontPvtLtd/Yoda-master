package com.yodaapp.live;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class School_ThankYou extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.school_thankyou);

        Button b = (Button) findViewById(R.id.school_thankyou_exit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
    }
}
