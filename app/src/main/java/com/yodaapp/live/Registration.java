package com.yodaapp.live;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class Registration extends Activity {

	Button submit;
	EditText et,et2,et3,et4;
	CheckBox ch;
	String name = "" , email = "" , password = "" , confirmpassword = "", passwordtrim = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.registration);

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setIcon(R.drawable.pf);

		et = (EditText) findViewById(R.id.registration_name);
		et2 = (EditText) findViewById(R.id.registration_email);
		et3 = (EditText) findViewById(R.id.registration_password);
		et4 = (EditText) findViewById(R.id.registration_confirmpassword);
		submit = (Button) findViewById(R.id.registation_submit);
		ch = (CheckBox) findViewById(R.id.registration_showpassword);
		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				name = et.getText().toString();
				email = et2.getText().toString();
				password = et3.getText().toString();
				passwordtrim = password.trim();
				confirmpassword = et4.getText().toString();
				validationfunction();

			}
		});
		ch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if(isChecked)
				{
					et3.setTransformationMethod(null);
					et4.setTransformationMethod(null);
					ch.setText(R.string.hide_password);
				}
				else
				{
					et3.setTransformationMethod(new PasswordTransformationMethod());
					et4.setTransformationMethod(new PasswordTransformationMethod());
					ch.setText(R.string.show_password);
				}
			}
		});

	}

	public void validationfunction()
	{
		boolean invalid = false;
		if(name.equals("") || name.isEmpty() || name.trim().isEmpty())
		{
            et.setError(getResources().getString(R.string.correct_name));
			invalid = true;
			Toast.makeText(Registration.this, R.string.correct_name, Toast.LENGTH_LONG).show();
		}
		else if(email.equals("") || email.isEmpty() || email.trim().isEmpty())
		{
            et2.setError(getResources().getString(R.string.correct_email));
			invalid = true;
			Toast.makeText(Registration.this, R.string.correct_email, Toast.LENGTH_LONG).show();
		}
		else if(password.equals("") || password.isEmpty() || password.trim().isEmpty())
		{
            et3.setError(getResources().getString(R.string.correct_password));
			invalid = true;
			Toast.makeText(Registration.this, R.string.correct_password, Toast.LENGTH_LONG).show();
		}
		else if(confirmpassword.equals("") || confirmpassword.isEmpty() || confirmpassword.trim().isEmpty())
		{
            et4.setError(getResources().getString(R.string.correct_confirm_password));
			invalid = true;
			Toast.makeText(Registration.this, R.string.correct_confirm_password, Toast.LENGTH_LONG).show();
		}
		else if(password.length() < 6)
		{
            et3.setError(getResources().getString(R.string.password_limit_correct));
			invalid = true;
			Toast.makeText(Registration.this, R.string.password_limit_correct, Toast.LENGTH_LONG).show();
		}
		else if(password.trim().length() < 6)
		{
            et3.setError(getResources().getString(R.string.password_limit_correct));
			invalid = true;
			Toast.makeText(Registration.this, R.string.password_limit_correct, Toast.LENGTH_LONG).show();
		}
		else if(confirmpassword.length() < 6)
		{
            et4.setError(getResources().getString(R.string.confirm_password_limit_correct));
			invalid = true;
			Toast.makeText(Registration.this, R.string.confirm_password_limit_correct, Toast.LENGTH_LONG).show();
		}
		else
		{
			if(invalid == false)
			{
				if(passwordtrim.equals(password))
				{
					if(password.equals(confirmpassword))
					{
						if(isEmailValid(email))
						{

						}
						else
						{
                            et2.setError(getResources().getString(R.string.correct_email));
							Toast.makeText(Registration.this, R.string.correct_email, Toast.LENGTH_LONG).show();
						}
					}
					else
					{
						Toast.makeText(Registration.this, R.string.password_confirm_password_not_match, Toast.LENGTH_LONG).show();
					}
				}
				else
				{
					Toast.makeText(Registration.this, R.string.password_space, Toast.LENGTH_LONG).show();
				}

			}
			else
			{
				Toast.makeText(Registration.this, R.string.password_error, Toast.LENGTH_LONG).show();
			}
		}
	}

	boolean Is_Valid_Webaddress(String edt) {
		return android.util.Patterns.WEB_URL.matcher(edt).matches();
	}

	boolean isEmailValid(CharSequence cemail) {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(cemail).matches();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		View v = getCurrentFocus();

		if (v != null && 
				(ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && 
				v instanceof EditText && 
				!v.getClass().getName().startsWith("android.webkit."))
		{
			int scrcoords[] = new int[2];
			v.getLocationOnScreen(scrcoords);
			float x = ev.getRawX() + v.getLeft() - scrcoords[0];
			float y = ev.getRawY() + v.getTop() - scrcoords[1];

			if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
				hideKeyboard(this);
		}
		return super.dispatchTouchEvent(ev);
	}

	public static void hideKeyboard(Activity activity)
	{
		if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null)
		{
			InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
		}
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                Intent intent = new Intent(Registration.this,PreRegistration.class);
	            Registration.this.finish();
                startActivity(intent);
                overridePendingTransition(R.anim.left_right,R.anim.right_left);
                return true;

            default:
                return true;
        }
    }

	@Override
	public void onBackPressed() {

		super.onBackPressed();
		Intent intent = new Intent(Registration.this,PreRegistration.class);
		Registration.this.finish();
		startActivity(intent);
        overridePendingTransition(R.anim.left_right,R.anim.right_left);
	}

}