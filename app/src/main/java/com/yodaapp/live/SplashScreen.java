package com.yodaapp.live;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;

import com.yodaapp.live.Parent.MainActivity_Parent;

public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                String id = "", email = "",school_id = "",status = "", roles = "", school_name = "";
                try {
                    SQLiteDatabase db2=openOrCreateDatabase("school", MODE_PRIVATE, null);
                    Cursor c = db2.rawQuery("SELECT * FROM user_master", null);
                    c.moveToFirst();
                    id=c.getString(c.getColumnIndex("id"));
                    school_id=c.getString(c.getColumnIndex("school_id"));
                    email=c.getString(c.getColumnIndex("email"));
                    status=c.getString(c.getColumnIndex("status"));
                    roles = c.getString(c.getColumnIndex("user_type"));
                    school_name = c.getString(c.getColumnIndex("schoolname"));
                    c.close();
                    db2.close();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    Intent intent = new Intent(SplashScreen.this, PreRegistration.class);
                    SplashScreen.this.finish();
                    finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                }


                if(status.equals("1")) {
                    if (!id.equals("") && !email.equals("") && !school_id.equals("")) {

                        StaticVariable.school_id=school_id;
                        StaticVariable.user_id=id;
                        StaticVariable.email=email;

                        if(roles.equals("1")) {
                            Intent intent = new Intent(SplashScreen.this, MainActivity.class);

                            intent.putExtra("school_id", school_id);
                            intent.putExtra("user_id", id);
                            intent.putExtra("email", email);
                            intent.putExtra("school_name",school_name);
                            SplashScreen.this.finish();
                            finish();
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                        }
                        else if(roles.equals("3")) {
                            Intent intent = new Intent(SplashScreen.this, MainActivity_Parent.class);
                            intent.putExtra("school_id", school_id);
                            intent.putExtra("user_id", id);
                            intent.putExtra("email", email);
                            intent.putExtra("school_name",school_name);
                            SplashScreen.this.finish();
                            finish();
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                        }
                    }
                }

                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}
