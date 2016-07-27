package com.mst.mutirestaurant.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mst.mutirestaurant.R;
import com.mst.mutirestaurant.support.Booking;
import com.mst.mutirestaurant.support.CommonFunction;
import com.mst.mutirestaurant.support.Confirmation;
import com.mst.mutirestaurant.support.DataBase;
import com.mst.mutirestaurant.support.ForgotPassword;
import com.mst.mutirestaurant.support.LogAdapter;
import com.mst.mutirestaurant.support.WorldPopulation;
import com.mst.mutirestaurant.support.getDb;

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

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mMobilenumber;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private TextView tv_Signup, tv_forgotpwd;

    CommonFunction cf;
    String response = null;

    static String str_result = "", str_name = "", str_userid = "", str_mobile = "", str_email = "", str_password = "";

    DataBase db;
    ForgotPassword fp_sms;

    public static View popupView = null;
    public static PopupWindow popupWindow = null;
    Booking bk;
    // Confirmation ct;
    getDb gdb;

    public static LinearLayout loglay, counlay;
    String[] code;
    String[] country;
    ListView list;
    LogAdapter adapter;
    public static EditText editsearch;
    ArrayList<WorldPopulation> arraylist = new ArrayList<WorldPopulation>();
    public static EditText mCountrycode;
    public static Button btn_countrycode, btn_close;
    public static String mobile = "", password = "", code_mobnum = "";

    ProgressBar pb;
    LinearLayout log_lay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        tv_forgotpwd = (TextView) findViewById(R.id.tv_forgotpwd);
        cf = new CommonFunction(this);
        db = new DataBase(this);
        fp_sms = new ForgotPassword(this, tv_forgotpwd);
        bk = new Booking(this);
        // ct = new Confirmation(this);
        gdb = new getDb(this);
        log_lay = (LinearLayout) findViewById(R.id.login_lay);
        pb = (ProgressBar) findViewById(R.id.login_progress);
        tv_Signup = (TextView) findViewById(R.id.tv_signup);
        tv_Signup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                finish();
            }
        });


        tv_forgotpwd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                popupView = layoutInflater.inflate(R.layout.alert_forgotpwd, null);
                final EditText fpMobile = (EditText) popupView.findViewById(R.id.mobilenum);
                final Button fpok = (Button) popupView.findViewById(R.id.ok_btn);
                final Button btn_cancel = (Button) popupView.findViewById(R.id.cancel_btn);

                popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);

                popupWindow.showAtLocation(tv_forgotpwd, Gravity.CENTER, 0, 0);
                btn_cancel.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                fpok.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String str_fpMobile = fpMobile.getText().toString();
                        System.out.println("getting input" + str_fpMobile);

                        if (cf.isInternetOn() == true) {

                            if (str_fpMobile.equals("") || str_fpMobile.trim().length() > 13) {
                                Toast.makeText(LoginActivity.this, "Please enter your mobile number", Toast.LENGTH_SHORT).show();
                            } else {
                                fp_sms.sendsms(str_fpMobile);
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Please enable internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


        mMobilenumber = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);


        mCountrycode = (EditText) findViewById(R.id.countrycode);
        btn_countrycode = (Button) findViewById(R.id.btn_countrycode);
        loglay = (LinearLayout) findViewById(R.id.logLayId);
        counlay = (LinearLayout) findViewById(R.id.counLayId);
        btn_close = (Button) findViewById(R.id.btn_close);

        btn_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loglay.setVisibility(View.VISIBLE);
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

        adapter = new LogAdapter(LoginActivity.this, arraylist);

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

        btn_countrycode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loglay.setVisibility(View.GONE);
                counlay.setVisibility(View.VISIBLE);

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
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
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
                    .setAction(android.R.string.ok, new OnClickListener() {
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


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mMobilenumber.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        mobile = mMobilenumber.getText().toString();
        if (mobile.startsWith("0")) {
            mobile = mobile.substring(1);
            System.out.println("login mobnum" + mobile);
        }
        password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(mobile)) {
            mMobilenumber.setError(getString(R.string.error_field_required));
            focusView = mMobilenumber;
            cancel = true;
        } else if (!isEmailValid(mobile)) {
            mMobilenumber.setError(getString(R.string.error_invalid_email));
            focusView = mMobilenumber;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            // showProgress(true);

            if (mobile.trim().equals("") || mobile.trim().length() > 13) {
                Toast.makeText(LoginActivity.this, "please enter valid mobile number", Toast.LENGTH_SHORT).show();
            } else {
                if (password.equals("") || password.length() < 6) {
                    Toast.makeText(LoginActivity.this, "Please enter password with atleast 6 digits", Toast.LENGTH_SHORT).show();
                } else {
                    if (cf.isInternetOn() == true) {
                        //showProgress(true);
                        code_mobnum = mCountrycode.getText().toString() + mobile;
                        System.out.println("Login-countrycodemobilenumber" + code_mobnum);
                        code_mobnum = code_mobnum.substring(1);
                        System.out.println("Login-ccmobilenumber" + code_mobnum);
                        // cf.showTProgress(LoginActivity.this,tv_Signup);
                        mAuthTask = new UserLoginTask(code_mobnum, password);
                        mAuthTask.execute();
                    } else {
                        Toast.makeText(LoginActivity.this, "Please enable internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }
    }

    private boolean isEmailValid(String mobile) {
        //TODO: Replace this with your own logic
        return mobile.length() >= 10;
    }

    private boolean isPasswordValid(String password) {
        // TODO: Replace this with your own logic
        return password.length() >= 6;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
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
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mMobilenumber.setAdapter(adapter);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, String> {

        private final String mMobile;
        private final String mPassword;

        UserLoginTask(String mobile, String password) {
            mMobile = mobile;
            mPassword = password;
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            if (cf.isInternetOn() == true) {

                try {

                    String API = cf.url + cf.PHP_FILE_Login;

                    HttpClient httpclient = new DefaultHttpClient();

                    HttpPost httppost = new HttpPost(API);

                    // Add your data
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("mobilenum", mMobile));
                    nameValuePairs.add(new BasicNameValuePair("password", mPassword));

                    System.out.println("login input value" + mMobile + " " + mPassword);

                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    // Execute HTTP Post Request

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    response = httpclient.execute(httppost, responseHandler);
                    System.out.println("login response" + response.toString());

                    JSONObject json = new JSONObject(response);
                    str_result = json.getString("success");

                    if (Integer.parseInt(str_result) == 0) {
                        System.out.println("login failure");
                        String str_message = json.getString("message");

                    } else if (Integer.parseInt(str_result) == 1) {
                        System.out.println("login success");

                        JSONArray data = json.getJSONArray("product");


                        for (int i = 0; i < data.length(); i++) {
                            JSONObject json1 = data.getJSONObject(i);
                            str_name = json1.getString("name");
                            str_mobile = json1.getString("mobilenum");
                            str_email = json1.getString("email");
                            str_password = json1.getString("password");
                            str_userid = json1.getString("userid");
                            System.out.println("login output value" + str_name + " " + str_mobile + " " + str_email + " " + str_password + " " + str_userid);
                        }
                    }

                    Thread.sleep(4000);
                } catch (Exception e) {
                    System.out.println("catch login error" + e.getMessage());
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
                Toast.makeText(LoginActivity.this, "Please enable internet connection", Toast.LENGTH_SHORT).show();
            }

            // TODO: register the new account here.
            return str_result;
        }

        @Override
        protected void onPreExecute() {

            // cf.showTProgress(LoginActivity.this, tv_Signup);
            log_lay.setVisibility(View.GONE);
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(final String result) {
            log_lay.setVisibility(View.VISIBLE);
            pb.setVisibility(View.GONE);
            mAuthTask = null;
            // showProgress(false);
            // cf.dismissTProgress();
            if (result != null) {
                // cf.dismissTProgress();
                if (result.equals("1")) {
                    // cf.dismissTProgress();
                    //Toast.makeText(LoginActivity.this, "Login Succes", Toast.LENGTH_SHORT).show();
                    //finish();
                    try {
                        db.CreateTable(2);
                        db.restaurant.execSQL("INSERT INTO "
                                + db.Login
                                + "(userid, email, password, status, name, mobilenum)"
                                + "VALUES ('" + str_userid + "', '" + str_email + "', '" + str_password + "', '1', '" + str_name + "', '" + str_mobile + "')");
                        Toast.makeText(LoginActivity.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                        // startActivity(new Intent(LoginActivity.this, PaymentActivity.class));

                        if (cf.chk_login == 2) {
                            if (cf.isInternetOn() == true) {
                                //cf.dismissTProgress();
                                // bk.booking(str_userid, MainActivity.str_orderId);
                                //PaymentActivity pamt= new PaymentActivity(con);
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                // pamt.inPuts(con);
                            } else {
                                Toast.makeText(LoginActivity.this, "Please enable internet connection", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                    } catch (Exception e) {
                        System.out.println("insert value into login db" + e.getMessage());
                    }
                    // cf.dismissTProgress();
                } else if (result.equals("null")) {
                    //cf.dismissTProgress();
                    // Toast.makeText(LoginActivity.this, "Please", Toast.LENGTH_SHORT).show();
                } else {
                    //  cf.dismissTProgress();
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                }
                //cf.dismissTProgress();
            }
            //cf.dismissTProgress();
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);

        }
    }
}

