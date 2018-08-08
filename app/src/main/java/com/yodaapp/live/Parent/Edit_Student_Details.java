package com.yodaapp.live.Parent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonObject;
import com.yodaapp.live.*;
import com.yodaapp.live.Parent.MainActivity_Parent;
import com.yodaapp.live.Parent_Model.Home_View_Model;
import com.yodaapp.live.adapter.student_parents_adapter;
import com.yodaapp.live.adapter.student_view_adapter;
import com.yodaapp.live.controller.AppController;
import com.yodaapp.live.model.Createadmin;
import com.yodaapp.live.model.ParentsDetailsModel;
import com.yodaapp.live.model.Section_Student_Model;
import com.yodaapp.live.parsers.CreateadminJSONParsers;
import com.yodaapp.live.parsers.Section_Student_JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Edit_Student_Details extends Activity {

    String tag_string_req_recieve2 = "string_req_recieve2";
    ProgressDialog progress;
    String student_id = "", name = "",school_id,user_id="";
    List<Createadmin> feedslist;
    String student_name = "", student_user_id = "", student_email = "", student_phone = "", student_password = "", student_confirm_password = "";
    List<Section_Student_Model> feedslist11;

    String allergies = "";
    int min_role = 0;
    boolean entriesValid = true;
    EditText et,et7;
    String picturePathicon = "";

    String imgString = "",fileName = "";
    private static int RESULT_LOAD_IMAGE = 0;
    byte[] array;
    Activity thisActivity=this;
    ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_student_details1);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if(Build.VERSION.SDK_INT > 19) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            //    getActionBar().setHomeAsUpIndicator(R.drawable.ic_home_white_24dp);
        } else {


            Drawable drawable = getResources().getDrawable(R.drawable.ic_action_back);
/*

            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, ContextCompat.getColor(this,R.color.md_white_1000));
       */     getActionBar().setIcon(drawable);
            getActionBar().setHomeButtonEnabled(true);
        }

        et = (EditText) findViewById(R.id.student_add_name);
        et7 = (EditText) findViewById(R.id.student_add_allergies);

        progress = new ProgressDialog(thisActivity);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();


        iv = (ImageView) findViewById(R.id.section_student_image);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(min_role < 7) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                    builder.setMessage(getResources().getString(R.string.student_image_change))
                            .setCancelable(false)
                            .setNeutralButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    RESULT_LOAD_IMAGE = 1;
                                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                                }
                            })
                            .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    Toast.makeText(thisActivity,getResources().getString(R.string.roleerror),Toast.LENGTH_LONG).show();
                }
            }
        });


        try {
            student_id = getIntent().getExtras().getString("student_id");
            name = getIntent().getExtras().getString("name");
            allergies = getIntent().getExtras().getString("allergies");
            school_id = getIntent().getExtras().getString("school_id");
            et.setText(name);
            et7.setText(allergies);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Button b = (Button) findViewById(R.id.add_new_student_submit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(thisActivity);


                student_name = et.getText().toString();

                allergies = et7.getText().toString();

                if(student_name.trim().equals("")) {
                    et.setError(getResources().getString(R.string.correct__name));
                    Toast.makeText(thisActivity,getResources().getString(R.string.correct__name),Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(isonline()) {

                        progress.show();
                        updatedata(getResources().getString(R.string.url_reference) + "parent_home/edit_student_details.php");
                    }
                }
            }
        });

        if (isonline()) {
            progress.show();
            updatedata1(getResources().getString(R.string.url_reference) + "home/student_view.php");
        } else {
            Toast.makeText(Edit_Student_Details.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
        }

    }
    void updatedata1(final String uri)
    {
        StringRequest request = new StringRequest(Request.Method.POST,uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        progress.hide();
                        Log.d("response1", arg0);
                        feedslist11 = Section_Student_JSONParser.parserFeed(arg0);
                        updateddisplay();
                        Log.d("feedslist11", feedslist11.size()+"");
                    }
                },


                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
//                        Log.d("Error",arg0.getMessage());
                        Toast.makeText(Edit_Student_Details.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Section_Student_View.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Edit_Student_Details.this);
                        builder.setMessage(getResources().getString(R.string.error_occured))
                                .setCancelable(false)
                                .setNeutralButton(getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        updatedata(uri);
                                    }
                                })
                                .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Edit_Student_Details.this, Branch_Detail_View.class);
                                        intent.putExtra("user_id", user_id);
                                        intent.putExtra("school_id", school_id);
                                        intent.putExtra("email", student_email);

                                        intent.putExtra("min_role", min_role);
                                        Edit_Student_Details.this.finish();
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
//                        Log.d("here in error", arg0.getMessage());
                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id", school_id);
                params.put("user_id", user_id);
                params.put("student_id",student_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);

    }

    void updateddisplay()
    {
        progress.show();
        if (feedslist11 != null) {
            for (final Section_Student_Model flower : feedslist11) {

                       if(!flower.getImage().equals("")) {
                    get_image(flower.getImage());
                           Log.v("image",flower.getImage());
                }

            }
        }
        else {
            Toast.makeText(Edit_Student_Details.this,getResources().getString(R.string.unknownerror7),Toast.LENGTH_LONG).show();
        }
        progress.hide();
    }
    private void upload_image(final String url, final Bitmap bm) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progress.hide();
                iv.setImageBitmap(bm);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progress.hide();
                Toast.makeText(thisActivity,getResources().getString(R.string.nointernetaccess),Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                builder.setMessage(getResources().getString(R.string.error_occured))
                        .setCancelable(false)
                        .setNeutralButton(getResources().getString(R.string.upload), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                upload_image(url,bm);
                            }
                        })
                        .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id", StaticVariable.school_id);
                params.put("user_id", StaticVariable.user_id);
                params.put("user_email", StaticVariable.email);
                params.put("student_id",student_id);
                params.put("image",imgString);
                params.put("file_name",fileName);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }

    void updatedata(String uri) {

        StringRequest request = new StringRequest(Request.Method.POST, uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        progress.hide();
                        Log.d("response", arg0);

                        try {
                            JSONObject json=new JSONObject(arg0);
                            String response=json.getString("sucess");

                            if (response.equals("Profile updated")){


                                AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                                builder.setMessage(getResources().getString(R.string.student_update))
                                        .setCancelable(false)
                                        .setNeutralButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent=new Intent(thisActivity,MainActivity_Parent.class);
                                                startActivity(intent);
                                                finish();
                                                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();


                            }
                            else {
                                Toast.makeText(thisActivity, R.string.error_occured, Toast.LENGTH_LONG).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

//                        Log.d("here in sucess", "sucess");
                        progress.hide();
                    }
                },


                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
                        Toast.makeText(thisActivity, R.string.nointernetaccess, Toast.LENGTH_LONG).show();


//                        Toast.makeText(thisActivity, arg0.getMessage(), Toast.LENGTH_LONG).show();
//                        Log.d("here in error", arg0.getMessage());
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id", school_id);
                params.put("user_id", user_id);
                params.put("user_email", "");
                params.put("id",student_id);
                params.put("name",student_name);
                params.put("allergies",allergies);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }


    protected boolean isonline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
    }

    boolean isEmailValid(CharSequence cemail) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(cemail).matches();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goBack();
                return true;
            default:
                return true;
        }
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

    private void get_image(String url) {
        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                iv.setImageBitmap(bitmap);
            }
        }, 0, 0, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        AppController.getInstance().addToRequestQueue(imageRequest, "tra");
    }

    public static void hideKeyboard(Activity activity)
    {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null)
        {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    public void goBack(){
        Intent intent=new Intent(thisActivity,MainActivity_Parent.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }
    @Override
    public void onBackPressed() {
        goBack();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            try {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePathicon = cursor.getString(columnIndex);
                cursor.close();
                String fileNameSegments[] = picturePathicon.split("/");
                fileName = fileNameSegments[fileNameSegments.length - 1];
                Log.d("file_name",fileName);
//				Log.d("lee", "icon");
                Bitmap bm = BitmapFactory.decodeFile(picturePathicon);
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                //bm = Bitmap.createScaledBitmap(bm, 96, 96, true);
                float maxImageSize = (float) 300;
                float ratio = Math.min(
                        (float) maxImageSize / bm.getWidth(),
                        (float) maxImageSize / bm.getHeight());
                int width = Math.round((float) ratio * bm.getWidth());
                int height = Math.round((float) ratio * bm.getHeight());
                Log.d("width", "" + width);
                Log.d("height",""+height);
                maxImageSize = (float) 200;
                ratio = Math.min(
                        (float) maxImageSize / bm.getWidth(),
                        (float) maxImageSize / bm.getHeight());
                width = Math.round((float) ratio * bm.getWidth());
                height = Math.round((float) ratio * bm.getHeight());
                bm = Bitmap.createScaledBitmap(bm, width, height, true);
                bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
                array=stream.toByteArray();
                imgString = Base64.encodeToString(array, Base64.NO_WRAP);

                AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                final Bitmap finalBm = bm;
                builder.setMessage(getResources().getString(R.string.confirm_upload))
                        .setCancelable(false)
                        .setNeutralButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progress.show();
                                upload_image(getResources().getString(R.string.url_reference) + "home/student_edit_photo.php", finalBm);
                            }
                        })
                        .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                //			Toast.makeText(Adminconfig.this, "Image icon is set", Toast.LENGTH_LONG).show();
            }
            catch (Exception e) {

                e.printStackTrace();
                //		Toast.makeText(Adminconfig.this, "Please try uploading image of less in size", Toast.LENGTH_LONG).show();
            }
        }
    }
}
