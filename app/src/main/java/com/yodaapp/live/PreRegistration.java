package com.yodaapp.live;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

public class PreRegistration extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.preregistration);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
      /*  getActionBar().setHomeButtonEnabled(true);
        getActionBar().setIcon(R.drawable.pf);
*/
		Button b = (Button) findViewById(R.id.prereg_newuser);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(PreRegistration.this, School_creation.class);
				PreRegistration.this.finish();
				startActivity(intent);
                overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
			}
		});

		Button b2 = (Button) findViewById(R.id.prereg_existinguser);
		b2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PreRegistration.this, Existinguser.class);
				PreRegistration.this.finish();
				startActivity(intent);
                overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);

			}
		});
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                super.onBackPressed();
                System.exit(0);
                android.os.Process.killProcess(android.os.Process.myPid());
                return true;

            default:
                return true;
        }
    }

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		System.exit(0);
		android.os.Process.killProcess(android.os.Process.myPid());
		super.onBackPressed();
	}
}
