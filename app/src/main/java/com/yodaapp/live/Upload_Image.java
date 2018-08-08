package com.yodaapp.live;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yodaapp.live.controller.AppController;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Upload_Image extends Activity {

    ProgressBar progressBar;
    String tag_string_req_recieve2 = "string_req_recieve2";
    String school_id = "", user_id = "", user_email = "", role_id = "";
    String school_name = "";
    String direction = "";
    private static int RESULT_LOAD_IMAGE = 0;
    String imgPath = "", fileName = "";
    long totalSize = 0;
    TextView txtPercentage;
    EditText et;
    String message = "";
    String branch_id = "", branch_name = "";
    String grade_id = "", grade_name = "";
    String section_id = "", section_name = "";
    long file_upload_limit = 0;
    String ddefault = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_file);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if(Build.VERSION.SDK_INT > 19) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            //    getActionBar().setHomeAsUpIndicator(R.drawable.ic_home_white_24dp);
        } else {
            getActionBar().setHomeButtonEnabled(true);
            // getActionBar().setIcon(R.drawable.ic_home_white_24dp);
        }

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        try {
            school_id = getIntent().getExtras().getString("school_id");
            user_id = getIntent().getExtras().getString("user_id");
            user_email = getIntent().getExtras().getString("email");
            school_name = getIntent().getExtras().getString("school_name");
            role_id = getIntent().getExtras().getString("role_id");
            direction = getIntent().getExtras().getString("direction");
            if(direction.equals("branch")) {
                branch_id = getIntent().getExtras().getString("branch_id");
                branch_name = getIntent().getExtras().getString("branch_name");
            }
            else if(direction.equals("grade")) {
                grade_id = getIntent().getExtras().getString("grade_id");
                grade_name = getIntent().getExtras().getString("grade_name");
                branch_id = getIntent().getExtras().getString("branch_id");
                branch_name = getIntent().getExtras().getString("branch_name");
            }
            else if(direction.equals("section")) {
                section_id = getIntent().getExtras().getString("section_id");
                section_name = getIntent().getExtras().getString("section_name");
                branch_id = getIntent().getExtras().getString("branch_id");
                branch_name = getIntent().getExtras().getString("branch_name");
                grade_id = getIntent().getExtras().getString("grade_id");
                grade_name = getIntent().getExtras().getString("grade_name");
            }
            ddefault = getIntent().getExtras().getString("ddefault");
        } catch (Exception e) {
            e.printStackTrace();
        }

        upload_limit(getResources().getString(R.string.url_reference) + "home/settings_file_upload_limit.php");


        TextView tv = (TextView) findViewById(R.id.file_upload_image);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RESULT_LOAD_IMAGE = 1;
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        TextView tv2 = (TextView) findViewById(R.id.file_upload_document);
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RESULT_LOAD_IMAGE = 2;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });

        txtPercentage = (TextView) findViewById(R.id.txtPercentage);
        txtPercentage.setTextColor(Color.BLUE);
        Button b = (Button) findViewById(R.id.file_upload_submit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et = (EditText) findViewById(R.id.upload_file_message_edit);
                message = et.getText().toString();
                if(file_upload_limit != 0 ) {
                    if (!imgPath.equals("")) {
                        File Img = new File(imgPath);
                        long length = Img.length();
//                        Log.d("file size", "" + length / 1024);
//                        Log.d("file path", imgPath);
                        if (file_upload_limit <= length) {
                                txtPercentage.setText("");
                                new UploadFileToServer().execute();
                        } else {
                            Toast.makeText(Upload_Image.this, getResources().getString(R.string.file_limit) + file_upload_limit / 1024 + "MB", Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(Upload_Image.this, getResources().getString(R.string.file_selection_error), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(Upload_Image.this, getResources().getString(R.string.unknownerror9), Toast.LENGTH_LONG).show();
                }
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

            try {
                if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgPath = cursor.getString(columnIndex);
                cursor.close();
                // Get the Image's file name
                String fileNameSegments[] = imgPath.split("/");
                fileName = fileNameSegments[fileNameSegments.length - 1];
                // Put file name in Async Http Post Param which will used in Php web app
                Toast.makeText(Upload_Image.this,getResources().getString(R.string.file_success),Toast.LENGTH_LONG).show();
                }
                else if (requestCode == 2 && resultCode == RESULT_OK && null != data) {
                    Uri selectedMediaUri = data.getData();
                    String filemanagerstring = selectedMediaUri.getPath();
                    String selectedMediaPath = getPath(selectedMediaUri);
                    if (!selectedMediaPath.equals("")) {
                        imgPath = selectedMediaPath;
                    } else if (!filemanagerstring.equals("")) {
                        imgPath = filemanagerstring;
                    }
                    int lastIndex = imgPath.lastIndexOf("/");
                    fileName = imgPath.substring(lastIndex + 1);
                }
            }
            catch (Exception e) {

                e.printStackTrace();
                Toast.makeText(Upload_Image.this, R.string.correct_icone_size, Toast.LENGTH_LONG).show();
            }

    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return "";
    }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    Intent intent = new Intent(Upload_Image.this, MainActivity.class);
                    intent.putExtra("school_id",school_id);
                    intent.putExtra("user_id",user_id);
                    intent.putExtra("email",user_email);
                    intent.putExtra("redirection","Communications");
                    Upload_Image.this.finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_right, R.anim.right_left);
                    return true;
                default:
                    return true;
            }
        }

        @Override
        public void onBackPressed() {
            if (direction.equals("") || direction.equals("school")) {
                super.onBackPressed();
                Intent intent = new Intent(Upload_Image.this, Communication_Staff_Send_School.class);
                intent.putExtra("school_id", school_id);
                intent.putExtra("user_id", user_id);
                intent.putExtra("email", user_email);
                intent.putExtra("role_id",role_id);
                intent.putExtra("school_name",school_name);
                intent.putExtra("ddefault",ddefault);
                Upload_Image.this.finish();
                startActivity(intent);
                Upload_Image.this.overridePendingTransition(R.anim.left_right, R.anim.right_left);
            }
            else if (direction.equals("branch")) {
                super.onBackPressed();
                Intent intent = new Intent(Upload_Image.this, Communication_Staff_Branches_send.class);
                intent.putExtra("school_id", school_id);
                intent.putExtra("user_id", user_id);
                intent.putExtra("email", user_email);
                intent.putExtra("school_name",school_name);
                intent.putExtra("role_id",role_id);
                intent.putExtra("id",branch_id);
                intent.putExtra("branch_name",branch_name);
                intent.putExtra("ddefault",ddefault);
                Upload_Image.this.finish();
                startActivity(intent);
                Upload_Image.this.overridePendingTransition(R.anim.left_right, R.anim.right_left);
            }
            else if (direction.equals("grade")) {
                super.onBackPressed();
                Intent intent = new Intent(Upload_Image.this, Communication_Staff_Grades_Send.class);
                intent.putExtra("school_id", school_id);
                intent.putExtra("user_id", user_id);
                intent.putExtra("email", user_email);
                intent.putExtra("school_name",school_name);
                intent.putExtra("role_id",role_id);
                intent.putExtra("branch_id",branch_id);
                intent.putExtra("branch_name",branch_name);
                intent.putExtra("grade_id",grade_id);
                intent.putExtra("grade_name",grade_name);
                intent.putExtra("ddefault",ddefault);
                Upload_Image.this.finish();
                startActivity(intent);
                Upload_Image.this.overridePendingTransition(R.anim.left_right, R.anim.right_left);
            }
            else if (direction.equals("section")) {
                super.onBackPressed();
                Intent intent = new Intent(Upload_Image.this, Communication_Staff_Section_Send.class);
                intent.putExtra("school_id", school_id);
                intent.putExtra("user_id", user_id);
                intent.putExtra("email", user_email);
                intent.putExtra("school_name", school_name);
                intent.putExtra("role_id",role_id);
                intent.putExtra("branch_id",branch_id);
                intent.putExtra("branch_name",branch_name);
                intent.putExtra("grade_id",grade_id);
                intent.putExtra("grade_name",grade_name);
                intent.putExtra("section_id",section_id);
                intent.putExtra("section_name",section_name);
                intent.putExtra("ddefault",ddefault);
                Upload_Image.this.finish();
                startActivity(intent);
                overridePendingTransition(R.anim.left_right, R.anim.right_left);
            }
        }


        void upload_limit(final String uri) {
            StringRequest request = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        file_upload_limit = Long.valueOf(s);
                    } catch (Exception e) {
                        e.printStackTrace();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Upload_Image.this);
                        builder.setMessage(getResources().getString(R.string.unknownerror2))
                                .setCancelable(false)
                                .setNeutralButton(getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        upload_limit(uri);
                                    }
                                })
                                .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Upload_Image.this, SplashScreen.class);
                                        Upload_Image.this.finish();
                                        startActivity(intent);
                                        Upload_Image.this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(Upload_Image.this, getResources().getString(R.string.nointernetaccess), Toast.LENGTH_LONG).show();
                    Toast.makeText(Upload_Image.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d("here in error", volleyError.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(Upload_Image.this);
                    builder.setMessage(getResources().getString(R.string.error_occured))
                            .setCancelable(false)
                            .setNeutralButton(getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    upload_limit(uri);
                                }
                            })
                            .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Upload_Image.this, SplashScreen.class);
                                    Upload_Image.this.finish();
                                    startActivity(intent);
                                    Upload_Image.this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }){

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("user_id", user_id);
                    params.put("username", user_email);
                    params.put("school_id", school_id);
                    return params;
                }

            };
            AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);

        }


    /**
     * Uploading the file to server
     * */
    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            progressBar.setProgress(progress[0]);

            // updating percentage value
            txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("");
            if(direction.equals("school") || direction.equals("")) {
                httppost = new HttpPost(getResources().getString(R.string.url_reference) + "home/fileUpload.php");
            } else if(direction.equals("branch")) {
                httppost = new HttpPost(getResources().getString(R.string.url_reference) + "home/fileUpload_branch.php");
            } else if(direction.equals("grade")) {
                httppost = new HttpPost(getResources().getString(R.string.url_reference) + "home/fileUpload_grades.php");
            } else if(direction.equals("section")) {
                httppost = new HttpPost(getResources().getString(R.string.url_reference) + "home/fileUpload_section.php");
            }
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(imgPath);

                // Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));

                // Extra parameters if you want to pass to server
                entity.addPart("file_name", new StringBody(fileName));
                entity.addPart("email", new StringBody(user_email));
                entity.addPart("user_id", new StringBody(user_id));
                entity.addPart("school_id", new StringBody(school_id));
                entity.addPart("message", new StringBody(message));
                if(direction.equals("branch")) {
                    entity.addPart("branch_id", new StringBody(branch_id));
                }
                else if(direction.equals("grade")) {
//                    entity.addPart("branch_id", new StringBody(branch_id));
                    entity.addPart("grade_id", new StringBody(grade_id));
                }
                else if(direction.equals("section")) {
//                    entity.addPart("branch_id", new StringBody(branch_id));
//                    entity.addPart("grade_id", new StringBody(grade_id));
                    entity.addPart("section_id", new StringBody(section_id));
                }


                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
//            Log.e("response", "Response from server: " + result);

            // showing the server response in an alert dialog
            showAlert(result);

            super.onPostExecute(result);
        }

    }

    /**
     * Method to show alert dialog
     * */
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle(getResources().getString(R.string.response_server))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (direction.equals("") || direction.equals("school")) {
                            Intent intent = new Intent(Upload_Image.this, Communication_Staff_Send_School.class);
                            intent.putExtra("school_id", school_id);
                            intent.putExtra("user_id", user_id);
                            intent.putExtra("email", user_email);
                            intent.putExtra("school_name",school_name);
                            intent.putExtra("role_id",role_id);
                            intent.putExtra("ddefault",ddefault);
                            Upload_Image.this.finish();
                            startActivity(intent);
                            Upload_Image.this.overridePendingTransition(R.anim.left_right, R.anim.right_left);
                        }
                        else if (direction.equals("branch")) {
                            Intent intent = new Intent(Upload_Image.this, Communication_Staff_Branches_send.class);
                            intent.putExtra("school_id", school_id);
                            intent.putExtra("user_id", user_id);
                            intent.putExtra("email", user_email);
                            intent.putExtra("school_name",school_name);
                            intent.putExtra("role_id",role_id);
                            intent.putExtra("id",branch_id);
                            intent.putExtra("branch_name",branch_name);
                            intent.putExtra("ddefault",ddefault);
                            Upload_Image.this.finish();
                            startActivity(intent);
                            Upload_Image.this.overridePendingTransition(R.anim.left_right, R.anim.right_left);
                        }
                        else if (direction.equals("grade")) {
                            Intent intent = new Intent(Upload_Image.this, Communication_Staff_Grades_Send.class);
                            intent.putExtra("school_id", school_id);
                            intent.putExtra("user_id", user_id);
                            intent.putExtra("email", user_email);
                            intent.putExtra("school_name",school_name);
                            intent.putExtra("role_id",role_id);
                            intent.putExtra("branch_id",branch_id);
                            intent.putExtra("branch_name",branch_name);
                            intent.putExtra("grade_id",grade_id);
                            intent.putExtra("grade_name",grade_name);
                            intent.putExtra("ddefault",ddefault);
                            Upload_Image.this.finish();
                            startActivity(intent);
                            Upload_Image.this.overridePendingTransition(R.anim.left_right, R.anim.right_left);
                        }
                        else if (direction.equals("section")) {
                            Intent intent = new Intent(Upload_Image.this, Communication_Staff_Section_Send.class);
                            intent.putExtra("school_id", school_id);
                            intent.putExtra("user_id", user_id);
                            intent.putExtra("email", user_email);
                            intent.putExtra("school_name", school_name);
                            intent.putExtra("role_id",role_id);
                            intent.putExtra("branch_id",branch_id);
                            intent.putExtra("branch_name",branch_name);
                            intent.putExtra("grade_id",grade_id);
                            intent.putExtra("grade_name",grade_name);
                            intent.putExtra("section_id",section_id);
                            intent.putExtra("section_name",section_name);
                            intent.putExtra("ddefault",ddefault);
                            Upload_Image.this.finish();
                            startActivity(intent);
                            Upload_Image.this.overridePendingTransition(R.anim.left_right, R.anim.right_left);
                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


}