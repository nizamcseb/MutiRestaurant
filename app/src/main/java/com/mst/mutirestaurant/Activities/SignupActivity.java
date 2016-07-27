package com.mst.mutirestaurant.Activities;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.mst.mutirestaurant.R;
import com.mst.mutirestaurant.support.CommonFunction;
import com.mst.mutirestaurant.support.Custom_Textwatcher;
import com.mst.mutirestaurant.support.DataBase;
import com.mst.mutirestaurant.support.ListViewAdapter;
import com.mst.mutirestaurant.support.VerificationSms;
import com.mst.mutirestaurant.support.WorldPopulation;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.READ_CONTACTS;

public class SignupActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private UserSignupTask mAuthTask = null;

    private EditText mnameView;
    //private EditText mmobileView;
    private EditText memailView;
    private EditText mPasswordView;
    private EditText mMobilenumber;
    public static EditText mCountrycode;
    CommonFunction cf;
    DataBase db;

    String response = null;

    static String str_result = "", str_userid = "", str_message = "";

    public static Button Verify, btn_countrycode, btn_close;
    private static final int REQUEST_READ_CONTACTS = 0;
    public static View popupView = null;
    public static PopupWindow popupWindow = null;

    VerificationSms vsms;
    public static String vmobile = "";
    // ConfirmBooking cb;
    public static int verifyid = 0;
    Button mSignUpButton;
    public static LinearLayout reglay, counlay;
    String[] code;
    String[] country;
    ListView list;
    ListViewAdapter adapter;
    public static EditText editsearch;
    ArrayList<WorldPopulation> arraylist = new ArrayList<WorldPopulation>();
    public static String name = "", mobile = "", email = "", password = "", code_mobnum="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        cf = new CommonFunction(this);
        db = new DataBase(this);
        vsms = new VerificationSms(this);
        //cb = new ConfirmBooking(this);
        mnameView = (EditText) findViewById(R.id.name);
        mMobilenumber = (EditText) findViewById(R.id.mobilenum);
        memailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mCountrycode = (EditText) findViewById(R.id.countrycode);
        btn_countrycode = (Button) findViewById(R.id.btn_countrycode);
        reglay = (LinearLayout) findViewById(R.id.regLayId);
        counlay = (LinearLayout) findViewById(R.id.counLayId);
        btn_close = (Button) findViewById(R.id.btn_close);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reglay.setVisibility(View.VISIBLE);
                counlay.setVisibility(View.GONE);
            }
        });


        country = new String[]{"Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda",
                "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin",
                "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegovina", "Botswana", "Brazil", "British Virgin Islands", "Brunei", "Bulgaria", "Burkina Faso", "Burma (Myanmar)",
                "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island",
                "Cocos (Keeling) Islands", "Colombia", "Comoros", "Cook Islands", "Costa Rica", "Croatia", "Cuba", "Cyprus", "Czech Republic", "Democratic Republic of the Congo",
                "Denmark", "Djibouti", "Dominica", "Dominican Republic", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Falkland Islands",
                "Faroe Islands", "Fiji", "Finland", "France", "French Polynesia", "Gabon", "Gambia", "Gaza Strip", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland",
                "Grenada", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Holy See (Vatican City)", "Honduras", "Hong Kong", "Hungary", "Iceland", "India",
                "Indonesia", "Iran", "Iraq", "Ireland", "Isle of Man", "Israel", "Italy", "Ivory Coast", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Kosovo",
                "Kuwait", "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia", "Madagascar",
                "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "MarshallIslands", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia",
                "Montenegro", "Montserrat", "Morocco", "Mozambique", "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia", "New Zealand", "Nicaragua",
                "Niger", "Nigeria", "Niue", "Norfolk Island", "North Korea", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay",
                "Peru", "Philippines", "Pitcairn Islands", "Poland", "Portugal", "Puerto Rico", "Qatar", "Republic of the Congo", "Romania", "Russia", "Rwanda", "Saint Barthelemy",
                "Saint Helena", "Saint Kitts and Nevis", "Saint Lucia", "Saint Martin", "Saint Pierre and Miquelon", "Saint Vincent and the Grenadines", "Samoa", "San Marino",
                "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia", "Solomon Islands", "Somalia",
                "South Africa", "South Korea", "Spain", "Sri Lanka", "Sudan", "Suriname", "Swaziland", "Sweden", "Switzerland", "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand",
                "Timor-Leste", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine",
                "United Arab Emirates", "United Kingdom", "United States", "Uruguay", "US Virgin Islands", "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam", "Wallis and Futuna",
                "West Bank", "Yemen", "Zambia", "Zimbabwe"
        };

        code = new String[]{"+93", "+355", "+213", "+1684", "+376", "+244", "+1264", "+672", "+1268", "+54", "+374", "+297", "+61", "+43", "+994", "+1242", "+973",
                "+880", "+1246", "+375", "+32", "+501", "+229", "+1441", "+975", "+591", "+387", "+267", "+55", "+1284", "+673", "+359", "+226", "+95", "+257", "+855", "+237", "+1", "+238",
                "+1345", "+236", "+235", "+56", "+86", "+61", "+61", "+57", "+269", "+682", "+506", "+385", "+53", "+357", "+420", "+243", "+45", "+253", "+1767", "+1809", "+593", "+20", "+503",
                "+240", "+291", "+372", "+251", "+500", "+298", "+679", "+358", "+33", "+689", "+241", "+220", "+970", "+995", "+49", "+233", "+350", "+30", "+299", "+1473", "+1671", "+502",
                "+224", "+245", "+592", "+509", "+39", "+504", "+852", "+36", "+354", "+91", "+62", "+98", "+964", "+353", "+44", "+972", "+39", "+225", "+1876", "+81", "+962", "+7", "+254",
                "+686", "+381", "+965", "+996", "+856", "+371", "+961", "+266", "+231", "+218", "+423", "+370", "+352", "+853", "+389", "+261", "+265", "+60", "+960", "+223", "+356", "+692",
                "+222", "+230", "+262", "+52", "+691", "+373", "+377", "+976", "+382", "+1664", "+212", "+258", "+264", "+674", "+977", "+31", "+599", "+687", "+64", "+505", "+227", "+234",
                "+683", "+672", "+850", "+1670", "+47", "+968", "+92", "+680", "+507", "+675", "+595", "+51", "+63", "+870", "+48", "+351", "+1", "+974", "+242", "+40", "+7", "+250", "+590",
                "+290", "+1869", "+1758", "+1599", "+508", "+1784", "+685", "+378", "+239", "+966", "+221", "+381", "+248", "+232", "+65", "+421", "+386", "+677", "+252", "+27", "+82", "+34",
                "+94", "+249", "+597", "+268", "+46", "+41", "+963", "+886", "+992", "+255", "+66", "+670", "+228", "+690", "+676", "+1868", "+216", "+90", "+993", "+1649", "+688", "+256",
                "+380", "+971", "+44", "+1", "+598", "+1340", "+998", "+678", "+58", "+84", "+681", "970", "+967", "+260", "+263"

        };

        list = (ListView) findViewById(R.id.countrylist);
        for (int i = 0; i < country.length; i++) {
            WorldPopulation wp = new WorldPopulation(country[i], code[i]);
            arraylist.add(wp);
        }

        adapter = new ListViewAdapter(SignupActivity.this, arraylist);

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);

        // Locate the EditText in listview_main.xml
        editsearch = (EditText) findViewById(R.id.search_country);

        // Capture Text in EditText
        editsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }
        });

        btn_countrycode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reglay.setVisibility(View.GONE);
                counlay.setVisibility(View.VISIBLE);

            }
        });

        final Drawable d = this.getResources().getDrawable(R.mipmap.ic_tick);

        mMobilenumber.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent arg1) {

                if (mMobilenumber.getText().length() == 10) {


                }

                return false;
            }
        });

        mnameView.addTextChangedListener(new Custom_Textwatcher(this, cf.TAG_TXT_NAME, mnameView));
        memailView.addTextChangedListener(new Custom_Textwatcher(this, cf.TAG_TXT_EMAIL, memailView));
        mPasswordView.addTextChangedListener(new Custom_Textwatcher(this, cf.TAG_TXT_PASSWORD, mPasswordView));
        mMobilenumber.addTextChangedListener(new Custom_Textwatcher(this, cf.TAG_TXT_MOBILE, mMobilenumber));


         mSignUpButton = (Button) findViewById(R.id.signup_btn);
        mSignUpButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                attemptSignup();
            }

            private void attemptSignup() {
                if (mAuthTask != null) {
                    return;
                }

                // Reset errors.
                mnameView.setError(null);
                mMobilenumber.setError(null);
                memailView.setError(null);
                mPasswordView.setError(null);

                // Store values at the time of the login attempt.
                 name = mnameView.getText().toString();
                 mobile = mMobilenumber.getText().toString();
                 email = memailView.getText().toString();
                 password = mPasswordView.getText().toString();
                if(mobile.startsWith("0")) {
                    mobile = mobile.substring(1); System.out.println("signup mobnum"+mobile);
                }

                if (name.equals("") || name.length() < 4) {
                    Toast.makeText(SignupActivity.this, "Please enter name with atleast 4 characters", Toast.LENGTH_SHORT).show();
                } else {
                    boolean chkmail = cf.eMailValidation(email);
                    if (email.equals("") || !chkmail) {
                        Toast.makeText(SignupActivity.this, "Please enter valid email Id", Toast.LENGTH_SHORT).show();
                    } else {
                        if (password.equals("") || password.length() < 6) {
                            Toast.makeText(SignupActivity.this, "Please enter password with atleast 6 digits", Toast.LENGTH_SHORT).show();
                        } else {
                            if (mobile.trim().equals("") || mobile.trim().length() > 13) {
                                Toast.makeText(SignupActivity.this, "please enter valid phone number", Toast.LENGTH_SHORT).show();
                            }  else {
                                final String mobile_verify = mMobilenumber.getText().toString();
                                code_mobnum = mCountrycode.getText().toString() + mobile; System.out.println("countrycodemobilenumber"+code_mobnum);
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                                builder.setTitle("Restaurant")
                                        .setMessage("Are you sure that " + code_mobnum + " is correct number?")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                                try {
                                                    if (cf.isInternetOn() == true) {
                                                        code_mobnum = code_mobnum.substring(1); System.out.println("ccmobilenumber"+code_mobnum);
                                                        verify_mobile vm = new verify_mobile(code_mobnum);
                                                        vm.execute();
                                                    } else {
                                                        Toast.makeText(SignupActivity.this, "Please enable internet connection", Toast.LENGTH_SHORT).show();
                                                    }

                                                } catch (Exception e) {
                                                    System.out.println("mobileverify error alert" + e.getMessage());
                                                }
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                                   /* mAuthTask = new UserSignupTask(name, mobile, email, password);
                                    mAuthTask.execute();*/

                            }
                        }
                    }
                }

                boolean cancel = false;
                View focusView = null;

            }
        });


    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    public void onBackPressed() {
        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        finish();
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mMobilenumber, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 6;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(SignupActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        //mMobilenumber.setAdapter(adapter);
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserSignupTask extends AsyncTask<Void, Void, String> {

        private final String mName;
        private final String mMobile;
        private final String mEmail;
        private final String mPassword;

        UserSignupTask(String name, String mobile, String email, String password) {
            mName = name;
            mMobile = mobile;
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            if (cf.isInternetOn() == true) {

                try {

                    String API = cf.url + cf.PHP_FILE_SignUp;

                    HttpClient httpclient = new DefaultHttpClient();

                    HttpPost httppost = new HttpPost(API);

                    // Add your data
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("name", mName));
                    nameValuePairs.add(new BasicNameValuePair("mobilenum", mMobile));
                    nameValuePairs.add(new BasicNameValuePair("email", mEmail));
                    nameValuePairs.add(new BasicNameValuePair("password", mPassword));

                    System.out.println("register input value" + mName + " " + mEmail + " " + mMobile + " " + mPassword);

                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    // Execute HTTP Post Request

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    response = httpclient.execute(httppost, responseHandler);
                    System.out.println("register response" + response.toString());

                    JSONObject json = new JSONObject(response);
                    str_result = json.getString("success");

                    if (Integer.parseInt(str_result) == 0) {
                        System.out.println("register failure");
                        str_message = json.getString("message");

                    } else if (Integer.parseInt(str_result) == 1) {
                        System.out.println("register success");

                        JSONArray data = json.getJSONArray("product");


                        for (int i = 0; i < data.length(); i++) {
                            JSONObject json1 = data.getJSONObject(i);

                            str_userid = json1.getString("userid");

                            System.out.println("register output value" + str_userid);
                        }
                    }

                    Thread.sleep(4000);
                } catch (Exception e) {
                    System.out.println("catch register error" + e.getMessage());
                    return null;
                }
            }
            /*for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }*/
            else {
                Toast.makeText(SignupActivity.this, "Please enable internet connection", Toast.LENGTH_SHORT).show();
            }

            // TODO: register the new account here.
            return str_result;
        }

        @Override
        protected void onPostExecute(final String result) {
            mAuthTask = null;
            //showProgress(false);
            cf.dismissTProgress();
            try {
                if (str_result != null) {
                    if (result.equals("1")) {
                        //Toast.makeText(LoginActivity.this, "Login Succes", Toast.LENGTH_SHORT).show();
                        //finish();
                        try {
                            db.CreateTable(1);
                            db.restaurant.execSQL("INSERT INTO "
                                    + db.Login
                                    + "(name, mobilenum, email, userid, password, status)"
                                    + "VALUES ('" + mName + "', '" + mMobile + "', '" + mEmail + "', '" + str_userid + "', '" + mPassword + "', '1')");
                            Toast.makeText(SignupActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                            //startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                   /*if (cf.chk_signup == 2) {

                        cb.booking(cf.USERID, "member");

                    } el se {
                        startActivity(new Intent(SignupActivity.this, MainActivity.class));
                        finish();
                    }*/

                        } catch (Exception e) {
                            System.out.println("insert value into reg db" + e.getMessage());
                        }
                        cf.dismissTProgress();
                    } else if (result.equals("null")) {
                        cf.dismissTProgress();
                        //Toast.makeText(LoginActivity.this, "null value", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignupActivity.this, str_message, Toast.LENGTH_SHORT).show();
                        cf.dismissTProgress();
                        //mPasswordView.setError(getString(R.string.error_incorrect_password));
                        //mPasswordView.requestFocus();
                    }

                }
                cf.dismissTProgress();
            }
            catch(Exception e){
                System.out.println("err=="+e.getMessage());
            }
           // cf.dismissTProgress();

        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            //showProgress(false);
        }
    }

    private class verify_mob_code extends AsyncTask<Void, Void, String> {
        String ver_mobile;
        String ver_code;
        String response = null, str_result = "";

        public verify_mob_code(String vmobile, String str_vcode) {
            ver_mobile = vmobile;
            ver_code = str_vcode;
        }

        @Override
        protected String doInBackground(Void... params) {

                try {

                    String API= cf.url+cf.PHP_FILE_VERIFY_CODE;
                    //String API = "http://blyncsolutions.com/BLYNC_APPS/android/vroom/test/verify_code.php";

                    HttpClient httpclient = new DefaultHttpClient();

                    HttpPost httppost = new HttpPost(API);

                    // Add your data
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("mobilenum", ver_mobile));
                    nameValuePairs.add(new BasicNameValuePair("code", ver_code));

                    System.out.println("verifycode input value" + ver_mobile + " " + ver_code);

                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    // Execute HTTP Post Request

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    response = httpclient.execute(httppost, responseHandler);
                    System.out.println("verifycode response" + response.toString());

                    JSONObject json = new JSONObject(response);
                    str_result = json.getString("success");

                    if (Integer.parseInt(str_result) == 0) {
                        System.out.println("verifycode failure");
                        String str_message = json.getString("message");

                    } else if (Integer.parseInt(str_result) == 1) {
                        System.out.println("verifycode success");
                    }

                    Thread.sleep(4000);
                } catch (Exception e) {
                    System.out.println("catch verifycode error" + e.getMessage());
                    return null;
                }

            // TODO: register the new account here.
            return str_result;
        }

        @Override
        protected void onPreExecute() {

            cf.showTProgress(SignupActivity.this, mSignUpButton);
        }

        @Override
        protected void onPostExecute(final String result) {


            if (result.equals("1")) {
                Toast.makeText(SignupActivity.this, "Successfully verified", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                if (cf.isInternetOn() == true) {
                    mAuthTask = new UserSignupTask(name, code_mobnum, email, password);
                    mAuthTask.execute();
                } else {
                    Toast.makeText(SignupActivity.this, "Please enable internet connection", Toast.LENGTH_SHORT).show();
                }

            } else if (result.equals("null")) {
                Toast.makeText(SignupActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                cf.dismissTProgress();
            } else {
                Toast.makeText(SignupActivity.this, str_message, Toast.LENGTH_SHORT).show();
                cf.dismissTProgress();
            }
        }
    }

    private class verify_mobile extends AsyncTask<Void, Void, String> {
        String mobile_veri;
        String response = null, str_result = "", str_failmessage = "", mobres="";

        public verify_mobile(String mobile_verify) {
            mobile_veri = mobile_verify;
        }

        @Override
        protected String doInBackground(Void... params) {

            try {

                String API = cf.url + cf.PHP_FILE_CREATE_SMS_CODE;
                //String API = "http://blyncsolutions.com/BLYNC_APPS/android/rentataxi/test/create_sms_code.php";

                HttpClient httpclient = new DefaultHttpClient();

                HttpPost httppost = new HttpPost(API);

                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("mobilenum", mobile_veri));

                System.out.println("verifynumber input value" + mobile_veri);

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                response = httpclient.execute(httppost, responseHandler);
                System.out.println("verifynumber response" + response.toString());

                JSONObject json = new JSONObject(response);
                str_result = json.getString("success"); System.out.println("verifynumber strresult"+str_result);

                if (Integer.parseInt(str_result) == 0) {
                    System.out.println("verifynumber failure");
                    str_failmessage = json.getString("message");

                } else if (Integer.parseInt(str_result) == 1) {
                    System.out.println("verifynumber success");

                    JSONArray data = json.getJSONArray("product");
                    for(int i =0; i < data.length(); i++) {
                        JSONObject json1 = data.getJSONObject(i);
                        mobres = json1.getString("result"); System.out.println("unicode"+mobres);
                    }
                }

                Thread.sleep(4000);
            } catch (Exception e) {
                System.out.println("catch verifynumber error" + e.getMessage());
                return null;
            }


            // TODO: register the new account here.
            return str_result;
        }

        @Override
        protected void onPreExecute() {
            cf.showTProgress(SignupActivity.this,mSignUpButton);
        }

        @Override
        protected void onPostExecute(final String result) {


            if (result.equals("1")) {

                //verifyid = 1;
                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                popupView = layoutInflater.inflate(R.layout.alert_number_verify, null);
                final EditText et_verifycode = (EditText) popupView.findViewById(R.id.tv_verifycode);
                final Button fpok = (Button) popupView.findViewById(R.id.ok_btn);
                final Button btn_cancel = (Button) popupView.findViewById(R.id.cancel_btn);

                popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);

                //popupWindow.showAtLocation(Verify, Gravity.CENTER, 0, 0);

                //vmobile = mMobilenumber.getText().toString();
                code_mobnum = mCountrycode.getText().toString() + mobile;
                code_mobnum = code_mobnum.substring(1);
                if (cf.isInternetOn() == true) {
                    vsms.sendVerificationSms(code_mobnum, popupWindow, mMobilenumber, mobres);

                } else {
                    Toast.makeText(SignupActivity.this, "Please enable internet connection", Toast.LENGTH_SHORT).show();
                }

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                fpok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String str_vcode = et_verifycode.getText().toString();
                        System.out.println("getting input" + str_vcode);

                        if (cf.isInternetOn() == true) {

                            if (str_vcode.equals("")) {
                                Toast.makeText(SignupActivity.this, "Please enter your verification code", Toast.LENGTH_SHORT).show();
                            } else {

                                verify_mob_code vmc = new verify_mob_code(code_mobnum, str_vcode);
                                vmc.execute();
                            }
                        } else {
                            Toast.makeText(SignupActivity.this, "Please enable internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } else if (result.equals("null")) {
                Toast.makeText(SignupActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                cf.dismissTProgress();
            } else {
                Toast.makeText(SignupActivity.this, str_failmessage, Toast.LENGTH_SHORT).show();
                cf.dismissTProgress();
            }
        }

    }
}
