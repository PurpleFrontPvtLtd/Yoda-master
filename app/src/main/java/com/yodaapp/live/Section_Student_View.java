package com.yodaapp.live;


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
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.yodaapp.live.Parent_Model.Home_View_Model;
import com.yodaapp.live.adapter.ExpandableListView;
import com.yodaapp.live.adapter.student_parents_adapter;
import com.yodaapp.live.adapter.student_view_adapter;
import com.yodaapp.live.controller.AppController;
import com.yodaapp.live.model.ParentsDetailsModel;
import com.yodaapp.live.model.Section_Student_Model;
import com.yodaapp.live.parsers.Section_Student_JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Section_Student_View extends Activity {


    String branch_id = "", branch_name = "", grade_id = "", grade_name = "";
    private String tag_string_req_recieve2 = "string_req_recieve2";
    ProgressDialog progress;
    String school_id = "",user_id = "",user_email = "";
    String section_id = "",section_name = "";
    String student_id = "",student_name = "";
    List<Section_Student_Model> feedslist;
    List<ParentsDetailsModel> feedslist11=new ArrayList<>();
    List<Home_View_Model> feedslist12=new ArrayList<>();
    int min_role = 0 ;
    String redirection = "";
    ImageView iv;
    private String tag_string_req = "string_req";
    private static int RESULT_LOAD_IMAGE = 0;
    byte[] array;
    String imgString = "",fileName = "";
    String picturePathicon = "",allergies="";
    ExpandableListView lisview;
    TextView student_inactivate;
    LinearLayout lincrtpar,lincrtextpar,lininact;
   Activity thisActivity=this;
    ExpandableListView listView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.section_student_view1);
        lisview=(ExpandableListView)findViewById(R.id.listview);
        listView1=(ExpandableListView)findViewById(R.id.listView);
        student_inactivate=(TextView)findViewById(R.id.student_inactivate);
        lincrtpar = (LinearLayout)findViewById(R.id.crtPar);
        lincrtextpar = (LinearLayout)findViewById(R.id.crtExt);
        lininact = (LinearLayout)findViewById(R.id.crtInact);


        try {
            branch_id = getIntent().getExtras().getString("branch_id");
            branch_name = getIntent().getExtras().getString("branch_name");
            grade_id = getIntent().getExtras().getString("grade_id");
            grade_name = getIntent().getExtras().getString("grade_name");
            user_id = getIntent().getExtras().getString("user_id");
            school_id = getIntent().getExtras().getString("school_id");
            user_email = getIntent().getExtras().getString("email");
            section_id = getIntent().getExtras().getString("section_id");
            section_name = getIntent().getExtras().getString("section_name");
            student_id = getIntent().getExtras().getString("student_id");
            student_name = getIntent().getExtras().getString("student_name");
            min_role = getIntent().getExtras().getInt("min_role");
            redirection = getIntent().getExtras().getString("redirection");

            Log.d("role_id","" + min_role);
        } catch (Exception e) {
//            e.printStackTrace();
        }

        if (min_role == 0 || min_role >=4)
        {
            lincrtpar.setVisibility(View.GONE);
            lincrtextpar.setVisibility(View.GONE);
            lininact.setVisibility(View.GONE);

        }


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

        progress = new ProgressDialog(Section_Student_View.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();


        student_inactivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (min_role<4) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(Section_Student_View.this);
                    builder.setMessage(getResources().getString(R.string.confirm_inactivate_student))
                            .setCancelable(false)
                            .setNegativeButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    studentInactivate();
                                }
                            })
                            .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else {
                    Toast.makeText(Section_Student_View.this,getResources().getString(R.string.notallowed),Toast.LENGTH_LONG).show();

                }

            }
        });

        iv = (ImageView) findViewById(R.id.section_student_image);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(min_role == 7 || min_role <=3 ) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Section_Student_View.this);
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
                    Toast.makeText(Section_Student_View.this,getResources().getString(R.string.notallowed),Toast.LENGTH_LONG).show();
                }
            }
        });

        if(school_id.equals("") || user_id.equals("") || branch_id.equals("")) {
            Toast.makeText(Section_Student_View.this, getResources().getString(R.string.unknownerror3), Toast.LENGTH_LONG).show();
        } else {
            if (isonline()) {
                progress.show();
                updatedata(getResources().getString(R.string.url_reference) + "home/student_view.php");
            } else {
                Toast.makeText(Section_Student_View.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
            }
        }

        TextView tv = (TextView) findViewById(R.id.student_parient_gaurdian_add);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                if(min_role == 0) {
                    Toast.makeText(Section_Student_View.this,getResources().getString(R.string.notallowed),Toast.LENGTH_LONG).show();
                }
                else if(min_role >= 4) {
                    Toast.makeText(Section_Student_View.this,getResources().getString(R.string.notallowed),Toast.LENGTH_LONG).show();
                }
                else if(min_role < 4 && min_role > 0) {
                    Intent intent = new Intent(Section_Student_View.this, Student_Parent_Existing_Add.class);
                    intent.putExtra("branch_id", branch_id);
                    intent.putExtra("branch_name", branch_name);
                    intent.putExtra("grade_id", grade_id);
                    intent.putExtra("grade_name", grade_name);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("school_id", school_id);
                    intent.putExtra("school_id", school_id);
                    intent.putExtra("email", user_email);
                    intent.putExtra("section_id", section_id);
                    intent.putExtra("section_name", section_name);
                    intent.putExtra("student_id", student_id);
                    intent.putExtra("student_name", student_name);
                    intent.putExtra("min_role", min_role);
                    intent.putExtra("redirection",redirection);
                    Section_Student_View.this.finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                } else {
                    Toast.makeText(Section_Student_View.this,getResources().getString(R.string.roleerror),Toast.LENGTH_LONG).show();
                }
            }
        });

        TextView tv2 = (TextView) findViewById(R.id.student_parient_gaurdian_create);
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                if(min_role == 0) {
                    Toast.makeText(Section_Student_View.this,getResources().getString(R.string.notallowed),Toast.LENGTH_LONG).show();
                }
                else if(min_role >= 4) {
                    Toast.makeText(Section_Student_View.this,getResources().getString(R.string.notallowed),Toast.LENGTH_LONG).show();
                }
                else if(min_role < 4 && min_role > 0) {
                    Intent intent = new Intent(Section_Student_View.this, Student_Parent_Creation.class);
                    intent.putExtra("branch_id", branch_id);
                    intent.putExtra("branch_name", branch_name);
                    intent.putExtra("grade_id", grade_id);
                    intent.putExtra("grade_name", grade_name);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("school_id", school_id);
                    intent.putExtra("school_id", school_id);
                    intent.putExtra("email", user_email);
                    intent.putExtra("section_id", section_id);
                    intent.putExtra("section_name", section_name);
                    intent.putExtra("student_id", student_id);
                    intent.putExtra("student_name", student_name);
                    intent.putExtra("min_role", min_role);
                    intent.putExtra("redirection",redirection);
                    Section_Student_View.this.finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                } else {
                Toast.makeText(Section_Student_View.this,getResources().getString(R.string.roleerror),Toast.LENGTH_LONG).show();}
            }
        });
    }

    void studentInactivate() {
        progress.show();

        StringRequest request = new StringRequest(Request.Method.POST,  getResources().getString(R.string.url_reference) + "home/inactivate_student_parent.php",

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        progress.hide();
                        Log.d("response", arg0);
//                        Log.d("here in sucess", "sucess");

                        try {
                            JSONObject obj=new JSONObject(arg0);

                            String status=obj.getString("sucess");

                            if (status.equals("Profile updated")){
                                AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                                builder.setMessage(getResources().getString(R.string.student_inactivate_update))
                                        .setCancelable(false)
                                        .setNeutralButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        })
                                        .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(thisActivity, Student_View_All.class);
                                                intent.putExtra("school_id",school_id);
                                                intent.putExtra("user_id",user_id);
                                                intent.putExtra("min_role",min_role);
                                                intent.putExtra("email",user_email);
                                                finish();
                                                startActivity(intent);
                                                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },


                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
                        Toast.makeText(thisActivity, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Student_View_All.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
//                        Log.d("here in error", arg0.getMessage());
                        AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                        builder.setMessage(getResources().getString(R.string.error_config))
                                .setCancelable(false)
                                .setNeutralButton(getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        updatedata(getResources().getString(R.string.url_reference) + "home/home_view.php");
                                    }
                                })
                                .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(thisActivity, Existinguser.class);
                                         finish();
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("school_id", school_id);
                params.put("user_id", user_id);
                params.put("student_id", student_id);
                params.put("status", "1");

                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }


    void updatedata(final String uri)
    {
        StringRequest request = new StringRequest(Request.Method.POST,uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        progress.hide();
                        Log.d("response", arg0);
                        feedslist = Section_Student_JSONParser.parserFeed(arg0);
                        updateddisplay();

//                        Log.d("here in sucess", "sucess");
                    }
                },


                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
//                        Log.d("Error",arg0.getMessage());
                        Toast.makeText(Section_Student_View.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Section_Student_View.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Section_Student_View.this);
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
                                        Intent intent = new Intent(Section_Student_View.this, Branch_Detail_View.class);
                                        intent.putExtra("user_id", user_id);
                                        intent.putExtra("school_id", school_id);
                                        intent.putExtra("email", user_email);
                                        intent.putExtra("id", branch_id);
                                        intent.putExtra("branch_name", branch_name);
                                        intent.putExtra("min_role", min_role);
                                        Section_Student_View.this.finish();
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
                params.put("user_email",user_email);
                params.put("branch_id",branch_id);
                params.put("branch_name",branch_name);
                params.put("grade_id",grade_id);
                params.put("grade_name",grade_name);
                params.put("section_id",section_id);
                params.put("section_name",section_name);
                params.put("student_id",student_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);

    }


    void updateddisplay()
    {
        progress.show();
        if (feedslist != null) {
            for (final Section_Student_Model flower : feedslist) {

                TextView tv = (TextView) findViewById(R.id.section_student_name);
                tv.setText(student_name);

                TextView tv2 = (TextView) findViewById(R.id.section_student_branch);
                tv2.setText(flower.getBranch_name());

                TextView tv3 = (TextView) findViewById(R.id.section_student_grade);
                tv3.setText(flower.getGrade_name());

                TextView tv4 = (TextView) findViewById(R.id.section_student_year);
                if(!flower.getYear().equals("null")) {
                    tv4.setText(flower.getYear());
                }

                TextView tv5 = (TextView) findViewById(R.id.section_student_section);
                tv5.setText(flower.getSection_name());

                TextView tv6 = (TextView) findViewById(R.id.section_student_allergies);
                if(!flower.getAllergies().equals("null")) {
                    allergies=flower.getAllergies();
                    tv6.setText(flower.getAllergies());
                }

                Log.d("image url",flower.getImage());
                if(!flower.getImage().equals("")) {
                    get_image(flower.getImage());
                }
                String parents = flower.getParents();
                String students = flower.getStudents();

                try {
                    JSONArray ar = new JSONArray(parents);
                    for (int i = 0; i < ar.length(); i++) {
                        JSONObject obj = ar.getJSONObject(i);
                        String name = obj.getString("name");
                        String contact = obj.getString("contact");
                        String email = obj.getString("email");
                        String role = obj.getString("role");
                        String profession = obj.getString("profession");
                        ParentsDetailsModel pm=new ParentsDetailsModel();
                        pm.setName(name);
                        pm.setContact(contact);
                        pm.setEmail(email);
                        pm.setRole(role);
                        pm.setProfession(profession);
                        feedslist11.add(pm);

                      }
                    JSONArray ar1 = new JSONArray(students);

                    for (int i = 0; i < ar1.length(); i++) {
                        JSONObject obj = ar1.getJSONObject(i);
                        String section = obj.getString("section_name");
                        String grade = obj.getString("grade_name");
                        String branch = obj.getString("branch_name");
                        Home_View_Model pm=new Home_View_Model();
                        pm.setSection_name(section);
                        pm.setBranch_name(branch_name);
                        pm.setGrade_name(grade_name);
                        feedslist12.add(pm);

                    }





                    lisview.setAdapter(new student_parents_adapter(Section_Student_View.this,feedslist11));
                    listView1.setAdapter(new student_view_adapter(Section_Student_View.this,feedslist12));


                } catch (JSONException e) {
//                    e.printStackTrace();
                }
            }
        }
        else {
            Toast.makeText(Section_Student_View.this,getResources().getString(R.string.unknownerror7),Toast.LENGTH_LONG).show();
        }
        progress.hide();
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

                AlertDialog.Builder builder = new AlertDialog.Builder(Section_Student_View.this);
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
                Toast.makeText(Section_Student_View.this,getResources().getString(R.string.nointernetaccess),Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(Section_Student_View.this);
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
        AppController.getInstance().addToRequestQueue(imageRequest, tag_string_req);
    }

    protected boolean isonline()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.viewedit, menu);
//        MenuItem edit = menu.findItem(R.id.edit);
//
//        if(min_role == 0 || min_role >=4 || min_role == 7)
//        {
//            edit.setVisible(true);
//        }
//        else
//        {
//            edit.setVisible(false);
//        }

        return true;
    }



    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if(min_role == 0 || min_role >=4)
        {
            menu.findItem(R.id.edit).setVisible(false);
        }

        else if (min_role <=3 || min_role ==7)
        {
            menu.findItem(R.id.edit).setVisible(true);
        }


        return true;
    }








    public void onBack(){

        super.onBackPressed();
        if(redirection.equals("allstudents")) {
            Intent intent = new Intent(Section_Student_View.this, Student_View_All.class);
            intent.putExtra("school_id", school_id);
            intent.putExtra("user_id", user_id);
            intent.putExtra("email", user_email);
            intent.putExtra("min_role", min_role);
            Section_Student_View.this.finish();
            startActivity(intent);
            overridePendingTransition(R.anim.left_right, R.anim.right_left);
        }
        else if(redirection.equals("home")) {
            Intent intent = new Intent(thisActivity, MainActivity.class);
            intent.putExtra("user_id",user_id);
            intent.putExtra("school_id", school_id);
            intent.putExtra("email", user_email);
            thisActivity.finish();
            startActivity(intent);
            overridePendingTransition(R.anim.left_right, R.anim.right_left);
        }
        else
        {
            Intent intent = new Intent(Section_Student_View.this, Grade_Section_view.class);
            intent.putExtra("branch_id", branch_id);
            intent.putExtra("branch_name", branch_name);
            intent.putExtra("school_id", school_id);
            intent.putExtra("user_id", user_id);
            intent.putExtra("email", user_email);
            intent.putExtra("grade_id", grade_id);
            intent.putExtra("grade_name", grade_name);
            intent.putExtra("section_id", section_id);
            intent.putExtra("section_name", section_name);
            intent.putExtra("min_role", min_role);
            Section_Student_View.this.finish();
            startActivity(intent);
            overridePendingTransition(R.anim.left_right, R.anim.right_left);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBack();
                return true;

             case R.id.edit:
                if(isonline())
                {

                    if(min_role == 0) {
                        Toast.makeText(Section_Student_View.this,getResources().getString(R.string.notallowed),Toast.LENGTH_LONG).show();
                    }
                    else if(min_role >= 4) {
                        Toast.makeText(Section_Student_View.this,getResources().getString(R.string.notallowed),Toast.LENGTH_LONG).show();
                    }
                    else if(min_role < 4 && min_role > 0) {

                        Intent intent = new Intent(Section_Student_View.this, Edit_Student_Details.class);
                        intent.putExtra("branch_id", branch_id);
                        intent.putExtra("branch_name", branch_name);
                        intent.putExtra("grade_id", grade_id);
                        intent.putExtra("grade_name", grade_name);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("school_id", school_id);
                        intent.putExtra("email", user_email);
                        intent.putExtra("section_id", section_id);
                        intent.putExtra("section_name", section_name);
                        intent.putExtra("student_id", student_id);
                        intent.putExtra("student_name", student_name);
                        intent.putExtra("min_role", min_role);
                        intent.putExtra("redirection", redirection);
                        intent.putExtra("allergies", allergies);
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    }
                }

            default:
                return true;
        }
    }

    @Override
    public void onBackPressed() {
        onBack();

    }
}
