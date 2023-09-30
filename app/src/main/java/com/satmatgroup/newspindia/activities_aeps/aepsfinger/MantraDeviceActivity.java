package com.satmatgroup.newspindia.activities_aeps.aepsfinger;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;

import com.google.gson.GsonBuilder;
import com.satmatgroup.newspindia.NewMainActivity;
import com.satmatgroup.newspindia.R;
import com.satmatgroup.newspindia.activities_aeps.BalanceCheckResponseActivity;
import com.satmatgroup.newspindia.activities_aeps.CashWithdrawalSuccessActivity;
import com.satmatgroup.newspindia.activities_aeps.MiniStatementModel;
import com.satmatgroup.newspindia.activities_aeps.MinistatementActivity;
import com.satmatgroup.newspindia.activities_aeps.aepsfinger.global.Verhoeff;
import com.satmatgroup.newspindia.activities_aeps.aepsfinger.maskedittext.MaskedEditText;
import com.satmatgroup.newspindia.activities_aeps.aepsfinger.model.DeviceInfo;
import com.satmatgroup.newspindia.activities_aeps.aepsfinger.model.Opts;
import com.satmatgroup.newspindia.activities_aeps.aepsfinger.model.PidData;
import com.satmatgroup.newspindia.activities_aeps.aepsfinger.model.PidOptions;
import com.satmatgroup.newspindia.activities_aeps.aepsfinger.model.uid.AuthReq;
import com.satmatgroup.newspindia.activities_aeps.aepsfinger.model.uid.AuthRes;
import com.satmatgroup.newspindia.activities_aeps.aepsfinger.model.uid.Meta;
import com.satmatgroup.newspindia.activities_aeps.aepsfinger.model.uid.Uses;
import com.satmatgroup.newspindia.activities_aeps.aepsfinger.signer.XMLSigner;
import com.satmatgroup.newspindia.activities_aeps.twofactorAuthentication.AepsTwoFactorAuthenticationActivity;
import com.satmatgroup.newspindia.activities_aeps.twofactorAuthentication.BaseAUthRegistrationResponse;
import com.satmatgroup.newspindia.network.Preferences;
import com.satmatgroup.newspindia.network_calls.AppApiCalls;
import com.satmatgroup.newspindia.utils.AppCommonMethods;
import com.satmatgroup.newspindia.utils.AppConstants;
import com.satmatgroup.newspindia.utils.MainIAPI;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MantraDeviceActivity extends AppCompatActivity implements AppApiCalls.OnAPICallCompleteListener {
    public static final String AEPS_TRANSACTION = "AEPS_TRANSACTION";
    public static final String EKYC = "E_KYC";

    @BindView(R.id.spinnerTotalFingerCount)
    Spinner spinnerTotalFingerCount;
    @BindView(R.id.linearFingerCount)
    LinearLayout linearFingerCount;
    @BindView(R.id.spinnerTotalFingerType)
    Spinner spinnerTotalFingerType;
    @BindView(R.id.spinnerTotalFingerFormat)
    Spinner spinnerTotalFingerFormat;
    @BindView(R.id.linearFingerFormat)
    LinearLayout linearFingerFormat;
    @BindView(R.id.edtxTimeOut)
    EditText edtxTimeOut;
    @BindView(R.id.edtxPidVer)
    EditText edtxPidVer;
    @BindView(R.id.linearTimeoutPidVer)
    LinearLayout linearTimeoutPidVer;
    @BindView(R.id.txtSelectPosition)
    TextView txtSelectPosition;
    @BindView(R.id.chbxUnknown)
    CheckBox chbxUnknown;
    @BindView(R.id.chbxLeftIndex)
    CheckBox chbxLeftIndex;
    @BindView(R.id.chbxLeftMiddle)
    CheckBox chbxLeftMiddle;
    @BindView(R.id.chbxLeftRing)
    CheckBox chbxLeftRing;
    @BindView(R.id.chbxLeftSmall)
    CheckBox chbxLeftSmall;
    @BindView(R.id.chbxLeftThumb)
    CheckBox chbxLeftThumb;
    @BindView(R.id.chbxRightIndex)
    CheckBox chbxRightIndex;
    @BindView(R.id.chbxRightMiddle)
    CheckBox chbxRightMiddle;
    @BindView(R.id.chbxRightRing)
    CheckBox chbxRightRing;
    @BindView(R.id.chbxRightSmall)
    CheckBox chbxRightSmall;
    @BindView(R.id.chbxRightThumb)
    CheckBox chbxRightThumb;
    @BindView(R.id.linearSelectPosition)
    LinearLayout linearSelectPosition;
    @BindView(R.id.edtxAdharNo)
    MaskedEditText edtxAdharNo;
    @BindView(R.id.linearAdharNo)
    LinearLayout linearAdharNo;
    @BindView(R.id.btnDeviceInfo)
    Button btnDeviceInfo;
    @BindView(R.id.btnCapture)
    Button btnCapture;
    @BindView(R.id.btnAuthRequest)
    Button btnAuthRequest;
    @BindView(R.id.btnReset)
    Button btnReset;
    @BindView(R.id.txtDataLabel)
    TextView txtDataLabel;
    @BindView(R.id.txtOutput)
    TextView txtOutput;
    @BindView(R.id.activity_main)
    LinearLayout activityMain;
    @BindView(R.id.spinnerEnv)
    Spinner spinnerEnv;


    private int fingerCount = 0;
    private PidData pidData = null;
    private Serializer serializer = null;
    private ArrayList<String> positions;

    private String latitude;
    private String longitude;

    private String cus_id;
    private String aadhar_no;
    private String nationalBankIdenticationNumber;
    private String mobile_no;
    private String transactionType;
    private String sendAmount;
    private String bankName;
    private String flag;
    private String requestremarks;
    private String aadharnumberkyc;
    private String pannumberkyc;
    private String kyccusid;
    String pidOptionDummy;
    private RelativeLayout progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mantra_device);
        ButterKnife.bind(this);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        positions = new ArrayList<>();
        serializer = new Persister();
        //Toolbar
        Toolbar toolbar = findViewById(R.id.custToolbar);
        ImageView ivBackBtn = toolbar.findViewById(R.id.ivBackBtn);
        ivBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            flag = bundle.getString("flag");
            if (flag.equals("ekyc")) {
                requestremarks = bundle.getString("requestremarks");
                aadharnumberkyc = bundle.getString("aadharnumberkyc");
                pannumberkyc = bundle.getString("pannumberkyc");
                kyccusid = bundle.getString("kyccusid");
            } else {
                cus_id = bundle.getString("cus_id");
                aadhar_no = bundle.getString("aadhar_no");
                nationalBankIdenticationNumber = bundle.getString("nationalBankIdenticationNumber");
                mobile_no = bundle.getString("mobile_no");
                transactionType = bundle.getString("transactionType");
                sendAmount = bundle.getString("sendAmount");
                bankName = bundle.getString("bankName");
                latitude = bundle.getString("latitude");
                longitude = bundle.getString("longitude");

            }
        }

        edtxAdharNo.setText("");


        progress_bar = findViewById(R.id.progress_bar);
       /* aepsTransaction(cus_id, "<PidData>\n" +
                        "   <Data type=\"X\">MjAyMS0wMi0wMVQxMjoxNToyNi56wzenqHLg8GYnIc5iHQuTa+P5ufU93s+e2HFm5j5XQDIYdvIQ43dTqvb+G2O5tbk9z3TrR5BLt5VCjNLeEqE615n6jmSSee5AhK4xqQVq2oAI2nT7b0fqFBxgnhvD8Tvv4/VRgcrODRF6QOy0ryzwwixzrs7ZfkzPrnR8XJNmCj8J6wVKSb2aWJa8G/EckAAifb9fxnT/kh4d9ZfvxYq0nl40jNNki1eLvt9OjGvRwgeseBgxTvTOD8/1TrR5Z9LeTY4INIlLCNVOl6ty5dDfhyzfUHdRfGh2Sdy+HIjG9kfgn1ipsmE+J+fu4R0xCGj35YPVnkCShu+aY8KYI6D9CXS1bBmmO4ytwIyAy9UhovPqi5jErLN7alCeMootKIwkG60bbqUq0b79YZcCXFbUBDXAaI45UXy2TL6FsnI2l/9CUpbb6P5F14l6ncP0d19zb7e1gAA51HliGNiH6GjP/ez1x3+mHYTvFthfckYJOzeKlpyK9uwh9oV7KaMP5+BcH67wezHGypZWQ0T6tbWutDdq4vOxRm3SPM7SeGGxWlVl1ufGZfuhTibl5D2tqZAjwUcvpXUi9EMJLezKM/rZySI6hc6msLFhClmeUxvYVdI8w9JCM2MQW+pzo40gHKmUXgVwE7JpMhQmoU15/UznqE5JtdhsRgClSEbnPdk7KW3LMCaIm5u8LtPwpHjqy6b8R+THQ+BeFjx9Gv4lJcTYKyHfBatKBZ/LFzZBqzZ0V1wzi/DEziyqMMFDyITLX1tZDLictjwO6jCXTqFTh8H5n4g5CjrAAihq/PRsMlCF1t6Iyul5wGOXpk7zSAambsgA8O3Qu3P3ShYyUWnGQdKFtO3tGIaeS11CVDzeR7xwQxfn8cxgRT1jM0wQnkMV/429jKfu1vePLGNoMSeDk4cErf/RCLIsaRyjhV7V0dJUmYydzI6WZRPqJeCcnTsEWPatWcZ/2zLwjvzXB304K/u/LUiO3pFZ6SnYjgpOCKS67NWpQSdv5PsrM5d3yg==</Data>\n" +
                        "   <DeviceInfo dc=\"c39c09b6-02e1-416c-a308-6f0d87632cb7\" dpId=\"MANTRA.MSIPL\" mc=\"MIIEGjCCAwKgAwIBAgIGAXdH3uNvMA0GCSqGSIb3DQEBCwUAMIHqMSowKAYDVQQDEyFEUyBNYW50cmEgU29mdGVjaCBJbmRpYSBQdnQgTHRkIDcxQzBBBgNVBDMTOkIgMjAzIFNoYXBhdGggSGV4YSBvcHBvc2l0ZSBHdWphcmF0IEhpZ2ggQ291cnQgUyBHIEhpZ2h3YXkxEjAQBgNVBAkTCUFobWVkYWJhZDEQMA4GA1UECBMHR3VqYXJhdDEdMBsGA1UECxMUVGVjaG5pY2FsIERlcGFydG1lbnQxJTAjBgNVBAoTHE1hbnRyYSBTb2Z0ZWNoIEluZGlhIFB2dCBMdGQxCzAJBgNVBAYTAklOMB4XDTIxMDEyODA3MDcxNloXDTIxMDIyNzA3MjIwM1owgbAxJDAiBgkqhkiG9w0BCQEWFXN1cHBvcnRAbWFudHJhdGVjLmNvbTELMAkGA1UEBhMCSU4xEDAOBgNVBAgTB0dVSkFSQVQxEjAQBgNVBAcTCUFITUVEQUJBRDEOMAwGA1UEChMFTVNJUEwxHjAcBgNVBAsTFUJpb21ldHJpYyBNYW51ZmFjdHVyZTElMCMGA1UEAxMcTWFudHJhIFNvZnRlY2ggSW5kaWEgUHZ0IEx0ZDCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBANyiYcJuzMZfo+h9n4rjCkz1lMQyw3hPakiSspcJ8IauKnjEzUu0M/89LwP3Hs2kEIBEompipv8U+dydVXP0djYolTvNe80SVb98Tc/V1kraZqEnqISRtoF7KABqEbAWz419bK513jRXJDHsJe8vbxs9AHL4GNM/zQ04mDqVichNmdtSG6nDJ0VXhbdP4mMxw0fJH/IemiRhx5T0/qV968Y8Q+QVCAX6W6WkkpryZYtEQMqs1yzrAqND5OqZ04poyGMYpOAqgakK9pffHlm2Vjoqt2eZyJ31QU53skXCSWhBHWuAP4CL6Oi0Ds9lvuLKuPBWVYovp6039g+DHLRkOm0CAwEAATANBgkqhkiG9w0BAQsFAAOCAQEAiy1GNY/z+tgZ3UumEDPDTXeomkhffJgMLfeQMeptgA4BYe4NKKu33ddZk7MnTxlq6ruqePXwhHoeg6zmhTbU/eikcHrtVR0HgG1BA6Sp4ftlKhLpZ/XanMA0lRiLPX5Z4FDOcwLlyRISXwqNW7FIAZ8qjpNPGnKbL8qnuE+utNKJNZ1klTfUPcA5hxMQXjlEx6VVssw5FFEm7h3dI5bnC3DR/MN5tDWE6zHWotDjFjEEGC7RZ5kMdqGkt1GsB3ByLOtxbAzow4GkZ9YdFOjBIfhqDEggAg9WOk07/Knitrfo0jsRbVbfgklyH9jzRUfYnAnq3uSp5JV0BRkrEFL5yA==\" mi=\"MFS100\" rdsId=\"MANTRA.AND.001\" rdsVer=\"1.0.4\">\n" +
                        "      <additional_info>\n" +
                        "         <Param name=\"srno\" value=\"3170633\"/>\n" +
                        "         <Param name=\"sysid\" value=\"353573092631109\"/>\n" +
                        "         <Param name=\"ts\" value=\"2021-02-01T12:15:31+05:30\"/>\n" +
                        "      </additional_info>\n" +
                        "   </DeviceInfo>\n" +
                        "   <Hmac>ZkD2Tlt+URRcMDueQTok4z7QDhvie/k4JTixVPYWqBFnOwTaLm7RqbWrHa+fDAkB</Hmac>\n" +
                        "   <Resp errCode=\"0\" errInfo=\"Capture Success\" fCount=\"1\" fType=\"0\" iCount=\"0\" iType=\"0\" nmPoints=\"25\" pCount=\"0\" pType=\"0\" qScore=\"78\"/>\n" +
                        "   <Skey ci=\"20221021\">jI+hQad0FNwO3UnQ9usmHyjCFv8t2HzyoqQVYJW/vXbd18293/XS73jEHmKj6uEboUTy4rukURojNrETFaI0ICg7GMDbC9BioGUbiFXWUmJcDSkmILcdCByrJ1H+QGtkerYcToIzu2eQ5XhlBT3a4O6bvzrzZNRVjWNt2+DUmejY3sxLYsIf0sLR3dn121MOMlDXCSyO1Ad24q2+DQsbwE3vOI8DZB4SeM4UWeMMB/cmYyC98D0ZqFlswwZu93YUSbob9EiN1TBG85uaHj3PEbpiRL/YKjOlRDbS8OUs3AXkMs7Gh/zjaUYtV5CnFbOSXRNQopFEarQV680WPTr+rg==</Skey>\n" +
                        "</PidData>", aadhar_no, nationalBankIdenticationNumber,
                mobile_no, transactionType, sendAmount);*/


    }

    @OnClick({R.id.btnDeviceInfo, R.id.btnCapture, R.id.btnAuthRequest, R.id.btnReset})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDeviceInfo:
                try {
                    Intent intent = new Intent();
                    intent.setAction("in.gov.uidai.rdservice.fp.INFO");
                    startActivityForResult(intent, 1);
                } catch (Exception e) {
                    Log.e("Error", e.toString());
                }
                break;
            case R.id.btnCapture:
                try {

                   /* String resutSend="<?xml version=\"1.0\"?>\n" +
                            "<PidData>\n" +
                            "  <Resp errCode=\"0\" errInfo=\"Success.\" fCount=\"1\" fType=\"2\" nmPoints=\"56\" qScore=\"79\" />\n" +
                            "  <DeviceInfo dpId=\"MANTRA.MSIPL\" rdsId=\"MANTRA.WIN.001\" rdsVer=\"1.0.8\" mi=\"MFS100\" mc=\"MIIEGDCCAwCgAwIBAgIEAp9jADANBgkqhkiG9w0BAQsFADCB6jEqMCgGA1UEAxMhRFMgTUFOVFJBIFNPRlRFQ0ggSU5ESUEgUFZUIExURCAzMVUwUwYDVQQzE0xCLTIwMyBTaGFwYXRoIEhleGEgT3Bwb3NpdGUgR3VqYXJhdCBIaWdoIENvdXJ0IFMuRyBIaWdod2F5IEFobWVkYWJhZCAtMzgwMDYwMRIwEAYDVQQJEwlBSE1FREFCQUQxEDAOBgNVBAgTB0dVSkFSQVQxCzAJBgNVBAsTAklUMSUwIwYDVQQKExxNQU5UUkEgU09GVEVDSCBJTkRJQSBQVlQgTFREMQswCQYDVQQGEwJJTjAeFw0yMzA2MjkxMTUxNDZaFw0yMzA3MjkxMjA2NDRaMIGwMSUwIwYDVQQDExxNYW50cmEgU29mdGVjaCBJbmRpYSBQdnQgTHRkMR4wHAYDVQQLExVCaW9tZXRyaWMgTWFudWZhY3R1cmUxDjAMBgNVBAoTBU1TSVBMMRIwEAYDVQQHEwlBSE1FREFCQUQxEDAOBgNVBAgTB0dVSkFSQVQxCzAJBgNVBAYTAklOMSQwIgYJKoZIhvcNAQkBFhVzdXBwb3J0QG1hbnRyYXRlYy5jb20wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDSLiVpmaVrvJnbSaKmf/aUSI8jcK3WI6+gKhSDF+KU8+MkupPdQ6KLBY3mOARkXca0r4CXyMu2W6e1PQ+5mtktR3Yh5PaJURhCXIjuFFrNVWWcH88IO7I4+pam+yS8NM0Gi4mXJBxXR5OqFkEoiCluPgJgIo7NeDGtwO6bxxd83YnjZGtJb6opxvQ/Xenft8ILp6IBNDnwUD4w1QhCKRUqnYKkwZuUf9dd7wLNMp3gqkdQoVjvQm3KhGC83kqdMiNu8Nb/pSwSdd9ntRd6L01LB9Leg7O/fNo1yfm0C5fyXyWQoeuVs/qTPZ4huUEaQChJERCijeitOSdg1iRzBUqfAgMBAAEwDQYJKoZIhvcNAQELBQADggEBAAhRkqCRMQFG54oGpH7krmpcexg3hHPHCuTX2PkSNNenuzBnVz3yfHBih6uCtbJDQHHB8hqNHJi9r58IqsH16aZWkS+VpNuXWqxtuGds8Mk2XL3NZed9L9It7mvd+RegmU5NKXy0AZIcUjrm9maYGzDZWTAdO5mSkaqSEoCrZTWGc9fGAhuwRqIsEWknpommduf+cCP68x6vHzZuWVX/m8DJdOIUxDLi72EXHrWzqbF0UjHjKNuOCD6hmen/iVd9XCimWvTAxFHkntbyPHwNeJQPgqhTRRwP4kF8cvBU1SAEcr9UxI6qDbMakKHWi1r95nIP665WtepYIXVbZHYb7QM=\" dc=\"eaaaf4f5-8cdb-43e4-b92a-7fb1fa64f434\">\n" +
                            "    <additional_info>\n" +
                            "      <Param name=\"srno\" value=\"3343576\" />\n" +
                            "      <Param name=\"sysid\" value=\"6E9FEBF3ED6125EFBFF0\" />\n" +
                            "      <Param name=\"ts\" value=\"2023-06-29T17:37:21+05:30\" />\n" +
                            "    </additional_info>\n" +
                            "  </DeviceInfo>\n" +
                            "  <Skey ci=\"20250923\">Dfs5m7ueyL2eQZgfqOTNTrJZsQeAlvze0kXN+UsdiqDvYNU3TmJZeahVXodBdzC5sgLw5NJ5+bW0Gz/Mf40qRZg2DJ9cMhVlkROim24zKn3xWbmBSSLhm8AHLildO1hPzuvfn25ntjbGCI0SEcERsCr/JzHE96xlIxQNErpYG11U0CMw1Ff8nM8cThfjUgnEcqJGUTUFFI9H8XP+GbMF67TbqOg0qMpyLgsiwFDO5/7f7R4unbBEHANlHRi9k7mp+TckJML3uu6ophs1PeM2ZsjEhlgiK9qyBDN3GNq/17a5dsgMUm1hJlAPFiy/yzvRfM7d1VjaAGa5ce4PWVnB7w==</Skey>\n" +
                            "  <Hmac>P+zDNUuujMACRaRx5fKTtA0QmGQmFCHA3XRak19jvUjCfoIbtovCkPTKsrhLg9cL</Hmac>\n" +
                            "  <Data type=\"X\">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</Data>\n" +
                            "</PidData>";

                    callServiceSave(cus_id, resutSend, "418869728175", "",
                            "9799754037", transactionType, "", latitude,
                            longitude);*/
                  /*  aepsTransaction(cus_id, resutSend, "418869728175", "",
                            "9799754037", transactionType, "", latitude,
                            longitude);*/





                    try {
                        Intent intent = new Intent();
                        intent.setAction("in.gov.uidai.rdservice.fp.INFO");
                        startActivityForResult(intent, 1);
                    } catch (Exception e) {
                        Log.e("Error", e.toString());
                    }


                    String pidOption = getPIDOptions();
                    pidOptionDummy = getPIDOptions();


                    if (pidOption != null) {
                        Log.e("PidOptions", pidOption);
                        Intent intent2 = new Intent();
                        intent2.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
                        intent2.putExtra("PID_OPTIONS", pidOption);
                        startActivityForResult(intent2, 2);
                    }
                } catch (Exception e) {
                    Log.e("Error", e.toString());
                }
                break;
            case R.id.btnAuthRequest:
                String aadharNo = edtxAdharNo.getText().toString();
                if (aadharNo.contains("-")) {
                    aadharNo = aadharNo.replaceAll("-", "").trim();
                }
                if (aadharNo.length() != 12 || !Verhoeff.validateVerhoeff(aadharNo)) {
                    setText("Please enter valid aadhaar number.");
                } else if (pidData == null) {
                    setText("Please scan your finger.");
                } else if (!pidData._Resp.errCode.equals("0")) {
                    setText("Error: " + pidData._Resp.errInfo);
                } else {
                    new MantraDeviceActivity.AuthRequest(aadharNo, pidData).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
                break;
            case R.id.btnReset:
                txtOutput.setText("");
                onResetClicked();
                break;
        }
    }

    public void onCheckboxClicked(View view) {
        CheckBox cb = (CheckBox) view;
        boolean checked = cb.isChecked();
        if (checked) {
            int pos = spinnerTotalFingerCount.getSelectedItemPosition();
            if ((pos + 1) > fingerCount) {
                fingerCount++;
                positions.add(cb.getText().toString());
            } else {
                ((CheckBox) view).setChecked(false);
                Toast.makeText(this, "Please Select Total Finger Count Proper", Toast.LENGTH_LONG).show();
            }
        } else {
            fingerCount--;
            String val = cb.getText().toString();
            if (positions.contains(val)) {
                positions.remove(val);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void onResetClicked() {
        fingerCount = 0;
        edtxTimeOut.setText("10000");
        edtxAdharNo.setText("");
        edtxPidVer.setText("2.0");
        spinnerTotalFingerCount.setSelection(0);
        spinnerTotalFingerType.setSelection(0);
        spinnerTotalFingerFormat.setSelection(0);
//        spinnerEnv.setSelection(0);
        chbxLeftIndex.setChecked(false);
        chbxLeftMiddle.setChecked(false);
        chbxLeftRing.setChecked(false);
        chbxLeftSmall.setChecked(false);
        chbxLeftThumb.setChecked(false);
        chbxRightIndex.setChecked(false);
        chbxRightMiddle.setChecked(false);
        chbxRightRing.setChecked(false);
        chbxRightSmall.setChecked(false);
        chbxRightThumb.setChecked(false);
        chbxUnknown.setChecked(false);
        pidData = null;
        positions.clear();
        positions = new ArrayList<>();
    }

    private void setText(final String message) {

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (message.contains("<PidData>")) {


                }
                txtOutput.setText(message);


            }
        });
    }

    private String getPIDOptions() {
        try {
            int fingerCount = spinnerTotalFingerCount.getSelectedItemPosition() + 1;
           // int fingerType = spinnerTotalFingerType.getSelectedItemPosition();
            int fingerType = 2;
            int fingerFormat = spinnerTotalFingerFormat.getSelectedItemPosition();
            String pidVer = edtxPidVer.getText().toString();
            String timeOut = edtxTimeOut.getText().toString();
            String posh = "UNKNOWN";
            if (positions.size() > 0) {
                posh = positions.toString().replace("[", "").replace("]", "").replaceAll("[\\s+]", "");
            }

            Opts opts = new Opts();
            opts.fCount = String.valueOf(fingerCount);
            opts.fType = String.valueOf(fingerType);
            opts.iCount = "0";
            opts.iType = "0";
            opts.pCount = "0";
            opts.pType = "0";
            opts.format = String.valueOf(fingerFormat);
            opts.pidVer = pidVer;
            opts.timeout = timeOut;
//            opts.otp = "123456";
//            opts.wadh = "Hello";
            if(flag.equals("ekyc")) {
                opts.wadh = "NA";
            }
            opts.posh = posh;
            opts.env = "P";
            //      Toast.makeText(this, "ENV : " + spinnerEnv.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();

            PidOptions pidOptions = new PidOptions();
            pidOptions.ver = "1.0";
            pidOptions.Opts = opts;

            //Toast.makeText(this, ""+pidOptions.Opts, Toast.LENGTH_SHORT).show();

            Serializer serializer = new Persister();
            StringWriter writer = new StringWriter();
            serializer.write(pidOptions, writer);
            return writer.toString();
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        if (data != null) {
                            String result = data.getStringExtra("DEVICE_INFO");
                            String rdService = data.getStringExtra("RD_SERVICE_INFO");
                            String display = "";
                            if (rdService != null) {
                                display = "RD Service Info :\n" + rdService + "\n\n";
                            }
                            if (result != null) {
                                /*DeviceInfo info = serializer.read(DeviceInfo.class, result);
                                display = display + "Device Code: " + info.dc + "\n\n"
                                        + "Serial No: " + info.srno + "\n\n"
                                        + "dpId: " + info.dpId + "\n\n"
                                        + "MC: " + info.mc + "\n\n"
                                        + "MI: " + info.mi + "\n\n"
                                        + "rdsId: " + info.rdsId + "\n\n"
                                        + "rdsVer: " + info.rdsVer;*/
                                display += "Device Info :\n" + result;
                             //   Toast.makeText(this, "" + display, Toast.LENGTH_SHORT).show();
                                setText(display);
                            }
                        }
                    } catch (Exception e) {
                        Log.e("Error", "Error while deserialze device info", e);
                      //  Toast.makeText(this, "Error while deserialze device info: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case 2:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        if (data != null) {
                            String result = data.getStringExtra("PID_DATA");
                            if (result != null) {
                                pidData = serializer.read(PidData.class, result);
                                setText(result);

                                if (!result.contains("Resp errCode=\"720\"")) {
                                    //   dummPid(result, pidOptionDummy);

                                    //open(result);


                                    if (flag.equals("ekyc")) {
                                        eKyc(requestremarks,
                                                pannumberkyc,
                                                aadharnumberkyc,
                                                result,
                                                pidOptionDummy,
                                                kyccusid);
                                //        Toast.makeText(this, "KYC Flag " , Toast.LENGTH_SHORT).show();

                                    } else {


                                        Preferences.saveValue(Preferences.FINGERDATA,result);
                                        Preferences.saveValue(Preferences.aadhar_no,aadhar_no);
                                        Preferences.saveValue(Preferences.nationalBankIdenticationNumber,nationalBankIdenticationNumber);
                                        Preferences.saveValue(Preferences.mobile_no,mobile_no);
                                        Preferences.saveValue(Preferences.transactionType,transactionType);
                                        Preferences.saveValue(Preferences.sendAmount,sendAmount);
                                        Preferences.saveValue(Preferences.latitude,latitude);
                                        Preferences.saveValue(Preferences.longitude,longitude);


                                        if (transactionType.equals("aepstwofactorregistration"))
                                        {
                                            callServiceSave(cus_id, result, aadhar_no, nationalBankIdenticationNumber,
                                                    mobile_no, transactionType, sendAmount, latitude,
                                                    longitude);
                                         /*   aepsTransaction(cus_id, result, aadhar_no, "",
                                                    mobile_no, transactionType, "", latitude,
                                                    longitude);*/
                                        }else if (transactionType.equals("aepstwofactorauthentication"))
                                        {
                                            callServiceSave(cus_id, result, aadhar_no, nationalBankIdenticationNumber,
                                                    mobile_no, transactionType, sendAmount, latitude,
                                                    longitude);
                                         /*   aepsTransaction(cus_id, result, aadhar_no, "",
                                                    mobile_no, transactionType, "", latitude,
                                                    longitude);*/
                                        }else {
                                            callServiceSave(cus_id, result, aadhar_no, nationalBankIdenticationNumber,
                                                    mobile_no, transactionType, sendAmount, latitude,
                                                    longitude);
                                          /*  aepsTransaction(cus_id, result, aadhar_no, nationalBankIdenticationNumber,
                                                    mobile_no, transactionType, sendAmount, latitude,
                                                    longitude);*/
                                        }
                                    //    Toast.makeText(this, "DATATTTTTAAA: "+result , Toast.LENGTH_SHORT).show();
                                    }

                                   /* Bundle bundle = new Bundle();
                                    bundle.putString("pid", result);
                                    Intent intent = new Intent(MantraDeviceActivity.this, AepsTransactionActivity.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);*/

                                }
                            }
                        }
                    } catch (Exception e) {
                        Log.e("Error", "Error while deserialze pid data", e);
                    }
                }
                break;
        }
    }

    @Override
    public void onAPICallCompleteListner(@Nullable Object item, @Nullable String flag, @NotNull String result) throws JSONException {
        if (flag.equals(AEPS_TRANSACTION)) {
            Log.e("AEPS_TRANSACTION", result);
         //  Toast.makeText(this, "AEPS_TRANSACTION : "+result , Toast.LENGTH_SHORT).show();
            //result(result);
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString(AppConstants.STATUS);
            String aepsmessage = jsonObject.getString(AppConstants.MESSAGE);

            Log.e(AppConstants.STATUS, status);
            //Toast.makeText(this, "Result3 : "+status , Toast.LENGTH_SHORT).show();
            if (status.contains("true")) {
                progress_bar.setVisibility(View.GONE);
             //   Toast.makeText(this, "Result : "+status , Toast.LENGTH_SHORT).show();

                if (transactionType.equals("aepstwofactorregistration"))
                {
                    Intent intent=new Intent(this, AepsTwoFactorAuthenticationActivity.class);
                    intent.putExtra("from","aepstwofactorauthentication");
                    startActivity(intent);
                }else if (transactionType.equals("aepstwofactorauthentication")){
                    Toast.makeText(this,aepsmessage,Toast.LENGTH_LONG).show();

                    Intent intent=new Intent(this, NewMainActivity.class);
                    startActivity(intent);

                }else {
                    if (transactionType.equalsIgnoreCase("ministatement")) {

                        //          Toast.makeText(this, "" + result, Toast.LENGTH_SHORT).show();
                        ArrayList<MiniStatementModel> miniStatementModelArrayList = new ArrayList<>();

                        JSONArray cast = jsonObject.getJSONArray("ministatement");
                        Toast.makeText(this, "cast : "+cast , Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < cast.length(); i++) {
                            JSONObject notifyObjJson = cast.getJSONObject(i);
                            String date = notifyObjJson.getString("date");

                            Log.e("date", date);
                            MiniStatementModel offersModel = new Gson()
                                    .fromJson(notifyObjJson.toString(), MiniStatementModel.class);
                            miniStatementModelArrayList.add(offersModel);

                        }

                        Bundle newBundle = new Bundle();
                        newBundle.putParcelableArrayList("miniStatementModelArrayList",
                                miniStatementModelArrayList);
                        Intent intent = new Intent(this, MinistatementActivity.class);
                        intent.putExtras(newBundle);
                        startActivity(intent);

                    } else {


                        JSONObject aepsResultObj = jsonObject.getJSONObject("result");
                        //  JSONObject aepsData = aepsResultObj.getJSONObject("data");
                        String terminalId = aepsResultObj.getString("terminalId");
                        String requestTransactionTime = aepsResultObj.getString("requestTransactionTime");
                        String transactionAmount = aepsResultObj.getString("transactionAmount");
                        String transactionStatus = aepsResultObj.getString("transactionStatus");
                        String balanceAmount = aepsResultObj.getString("balanceAmount");
                        String bankRRN = aepsResultObj.getString("bankRRN");
                        String transactionType = aepsResultObj.getString("transactionType");
                        String fpTransactionId = aepsResultObj.getString("fpTransactionId");
                        String merchantTransactionId = aepsResultObj.getString("merchantTransactionId");
                        String outletname = jsonObject.getString("outletname");
                        String outletmobile = jsonObject.getString("outletmobile");
                        String url = jsonObject.getString("url");

                        //   Toast.makeText(this, "transactionType : "+transactionType , Toast.LENGTH_SHORT).show();

                        // String retailerid = jsonObject.getString("cus_id");
                        if (transactionType.equalsIgnoreCase("BE")) {

                            Bundle beBunndle = new Bundle();
                            beBunndle.putString("aepsmessage", aepsmessage);
                            beBunndle.putString("terminalId", terminalId);
                            beBunndle.putString("requestTransactionTime", requestTransactionTime);
                            beBunndle.putString("transactionAmount", transactionAmount);
                            beBunndle.putString("transactionStatus", transactionStatus);
                            beBunndle.putString("balanceAmount", balanceAmount);
                            beBunndle.putString("bankRRN", bankRRN);
                            beBunndle.putString("transactionType", transactionType);
                            beBunndle.putString("fpTransactionId", fpTransactionId);
                            beBunndle.putString("merchantTransactionId", merchantTransactionId);
                            beBunndle.putString("outletname", outletname);
                            beBunndle.putString("outletmobile", outletmobile);
                            beBunndle.putString("url", url);
                            Intent intent = new Intent(this, BalanceCheckResponseActivity.class);
                            intent.putExtras(beBunndle);
                            startActivity(intent);
                            finish();

                        } else if (transactionType.equalsIgnoreCase("CW") ||
                                transactionType.equalsIgnoreCase("M")) {
                            Bundle cwBunndle = new Bundle();
                            cwBunndle.putString("aepsmessage", aepsmessage);
                            cwBunndle.putString("terminalId", terminalId);
                            cwBunndle.putString("requestTransactionTime", requestTransactionTime);
                            cwBunndle.putString("transactionAmount", transactionAmount);
                            cwBunndle.putString("transactionStatus", transactionStatus);
                            cwBunndle.putString("balanceAmount", balanceAmount);
                            cwBunndle.putString("bankRRN", bankRRN);
                            cwBunndle.putString("transactionType", transactionType);
                            cwBunndle.putString("fpTransactionId", fpTransactionId);
                            cwBunndle.putString("merchantTransactionId", merchantTransactionId);
                            cwBunndle.putString("outletname", outletname);
                            cwBunndle.putString("outletmobile", outletmobile);
                            cwBunndle.putString("url", url);
                            cwBunndle.putString("aadhar_no", aadhar_no);
                            cwBunndle.putString("bankName", bankName);
                            // cwBunndle.putString("retailerId", retailerid);
                            Intent intent = new Intent(this, CashWithdrawalSuccessActivity.class);
                            intent.putExtras(cwBunndle);
                            startActivity(intent);
                            finish();

                        } else if (transactionType.equalsIgnoreCase("cashdeposit")) {
                            Bundle cwBunndle = new Bundle();
                            cwBunndle.putString("aepsmessage", aepsmessage);
                            cwBunndle.putString("terminalId", terminalId);
                            cwBunndle.putString("requestTransactionTime", requestTransactionTime);
                            cwBunndle.putString("transactionAmount", transactionAmount);
                            cwBunndle.putString("transactionStatus", transactionStatus);
                            cwBunndle.putString("balanceAmount", balanceAmount);
                            cwBunndle.putString("bankRRN", bankRRN);
                            cwBunndle.putString("transactionType", transactionType);
                            cwBunndle.putString("fpTransactionId", fpTransactionId);
                            cwBunndle.putString("merchantTransactionId", merchantTransactionId);
                            cwBunndle.putString("outletname", outletname);
                            cwBunndle.putString("outletmobile", outletmobile);
                            cwBunndle.putString("url", url);
                            cwBunndle.putString("aadhar_no", aadhar_no);
                            cwBunndle.putString("bankName", bankName);
                            //  cwBunndle.putString("retailerId", retailerid);
                            Intent intent = new Intent(this, CashWithdrawalSuccessActivity.class);
                            intent.putExtras(cwBunndle);
                            startActivity(intent);
                            finish();

                        }
                    }
                }


            } else {
                Toast.makeText(this, "Toast 3:"+aepsmessage, Toast.LENGTH_SHORT).show();
                progress_bar.setVisibility(View.GONE);

              //  Toast.makeText(this, "Result Toast 3: "+aepsmessage , Toast.LENGTH_SHORT).show();
//                toast("Beneficiary Adding Failed")
            }
        }


        if (flag.equals(EKYC)) {
            Log.e("EKYC", result);
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString(AppConstants.STATUS);
            String aepsmessage = jsonObject.getString(AppConstants.MESSAGE);
            Log.e(AppConstants.STATUS, status);
            if (status.contains("true")) {
                progress_bar.setVisibility(View.GONE);
                Toast.makeText(this,"Toast2 : "+ aepsmessage, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, NewMainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this,"Toast1 : "+ aepsmessage, Toast.LENGTH_SHORT).show();
                progress_bar.setVisibility(View.GONE);
            }
        }
    }

    private class AuthRequest extends AsyncTask<Void, Void, String> {

        private String uid;
        private PidData pidData;
        private ProgressDialog dialog;
        private int posFingerFormat = 2;

        private AuthRequest(String uid, PidData pidData) {
            this.uid = uid;
            this.pidData = pidData;
            dialog = new ProgressDialog(MantraDeviceActivity.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            posFingerFormat = 2;
            dialog.setMessage("Please wait...");
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                DeviceInfo info = pidData._DeviceInfo;

                Uses uses = new Uses();
                uses.pi = "n";
                uses.pa = "n";
                uses.pfa = "n";
                uses.bio = "y";
                if (posFingerFormat == 2) {
                    uses.bt = "FIR";
                } else {
                    uses.bt = "FMR";
                }
                uses.pin = "n";
                uses.otp = "n";

                Meta meta = new Meta();
                meta.udc = "MANT0";
                meta.rdsId = info.rdsId;
                meta.rdsVer = info.rdsVer;
                meta.dpId = info.dpId;
                meta.dc = info.dc;
                meta.mi = info.mi;
                meta.mc = info.mc;

                AuthReq authReq = new AuthReq();
                authReq.uid = uid;
                authReq.rc = "Y";
                authReq.tid = "registered";
                authReq.ac = "public";
                authReq.sa = "public";
                authReq.ver = "2.0";
                authReq.txn = generateTXN();
                authReq.lk = "MEaMX8fkRa6PqsqK6wGMrEXcXFl_oXHA-YuknI2uf0gKgZ80HaZgG3A"; //AUA
                authReq.skey = pidData._Skey;
                authReq.Hmac = pidData._Hmac;
                authReq.data = pidData._Data;
                authReq.meta = meta;
                authReq.uses = uses;

                StringWriter writer = new StringWriter();
                serializer.write(authReq, writer);
                String pass = "public";
                String reqXML = writer.toString();
                String signAuthXML = XMLSigner.generateSignXML(reqXML, getAssets().open("staging_signature_privateKey.p12"), pass);

                URL url = new URL(getAuthURL(uid));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(30000);
                conn.setConnectTimeout(30000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/xml");
                conn.setUseCaches(false);
                conn.setDefaultUseCaches(false);
                OutputStreamWriter writer2 = new OutputStreamWriter(conn.getOutputStream());
                writer2.write(signAuthXML);
                writer2.flush();
                conn.connect();

                StringBuilder sb = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String response;
                while ((response = reader.readLine()) != null) {
                    sb.append(response).append("\n");
                }
                response = sb.toString();

                AuthRes authRes = serializer.read(AuthRes.class, response);
                String res;
                if (authRes.err != null) {
                    if (authRes.err.equals("0")) {
                        res = "Authentication Success" + "\n"
                                + "Auth Response: " + authRes.ret.toUpperCase() + "\n"
                                + "TXN: " + authRes.txn + "\n"
                                + "";
                    } else {
                        res = "Error Code: " + authRes.err + "\n"
                                + "Auth Response: " + authRes.ret.toUpperCase() + "\n"
                                + "TXN: " + authRes.txn + "\n"
                                + "";
                    }
                } else {
                    res = "Authentication Success" + "\n"
                            + "Auth Response: " + authRes.ret.toUpperCase() + "\n"
                            + "TXN: " + authRes.txn + "\n"
                            + "";
                }
                return res;
            } catch (Exception e) {
                Log.e("Error", "Error while auth request", e);
                return "Error: " + e.toString();
            }
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            if (res != null) {
                setText(res);
            }
            onResetClicked();
            if (dialog.isShowing())
                dialog.dismiss();
        }
    }

    private String generateTXN() {
        try {
            Date tempDate = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.ENGLISH);
            String dTTXN = formatter.format(tempDate);
//            return "UKC:public:" + dTTXN;
            return dTTXN;
        } catch (Exception e) {
            Log.e("generateTXN.Error", e.toString());
            return "";
        }
    }

    private String getAuthURL(String UID) {
        String url = "http://developer.uidai.gov.in/auth/";
//        String url = "http://developer.uidai.gov.in/uidauthserver/";
        url += "public/" + UID.charAt(0) + "/" + UID.charAt(1) + "/";
        url += "MG41KIrkk5moCkcO8w-2fc01-P7I5S-6X2-X7luVcDgZyOa2LXs3ELI"; //ASA
        return url;
    }


    private void aepsTransaction(String cus_id, String txtPidData, String adhaarNumber,
                                 String nationalBankIdenticationNumber, String mobileNumber,
                                 String type, String transactionAmount, String latitude,
                                 String longitude) {

        if (new AppCommonMethods(this).isNetworkAvailable()) {

            progress_bar.setVisibility(View.VISIBLE);

            if (type.equals("aadharpay"))
            {
                type="aadharpayment";
            }

            AppApiCalls mAPIcall = new AppApiCalls(this, AEPS_TRANSACTION, this);
            mAPIcall.aepsTransaction(cus_id,
                    txtPidData,
                    adhaarNumber,
                    nationalBankIdenticationNumber,
                    mobileNumber,
                    type,
                    transactionAmount, latitude, longitude);
        } else {
            Toast.makeText(this, "Internet Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void dummPid(String txtPidData, String pidOptions) {
        if (new AppCommonMethods(this).isNetworkAvailable()) {

            progress_bar.setVisibility(View.VISIBLE);

            AppApiCalls mAPIcall = new AppApiCalls(this, AEPS_TRANSACTION, this);
            mAPIcall.dummyPid(
                    txtPidData, pidOptions
            );
        } else {
            Toast.makeText(this, "Internet Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void eKyc(String requestRemarks,
                      String userPan,
                      String aadhaarNumber,
                      String txtPidData,
                      String PidOptions,
                      String cus_id) {
        if (new AppCommonMethods(this).isNetworkAvailable()) {

            progress_bar.setVisibility(View.VISIBLE);

            AppApiCalls mAPIcall = new AppApiCalls(this, EKYC, this);
            mAPIcall.eKyc(requestRemarks,
                    userPan,
                    aadhaarNumber,
                    txtPidData,
                    PidOptions,
                    cus_id);
        } else {
            Toast.makeText(this, "Internet Error", Toast.LENGTH_SHORT).show();
        }
    }

    public void result(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("yes",
                (arg0, arg1) -> {

                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void open(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("message");
        alertDialogBuilder.setPositiveButton("yes",
                (arg0, arg1) -> {
                    if(flag.equals("ekyc")) {
                        eKyc(requestremarks,
                                pannumberkyc,
                                aadharnumberkyc,
                                message,
                                pidOptionDummy,
                                kyccusid);
                    } else {
                        aepsTransaction(cus_id, message, aadhar_no, nationalBankIdenticationNumber,
                                mobile_no, transactionType, sendAmount, latitude, longitude);


                    }


                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void callServiceSave(String cus_id, String result, String aadhar_no,
                                 String nationalBankIdenticationNumber,
                                 String mobile_no, String transactionType,
                                 String sendAmount, String latitude, String longitude) {

        progress_bar.setVisibility(View.VISIBLE);
        System.setProperty("http.keepAlive", "false");
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.readTimeout(5, TimeUnit.MINUTES).
                connectTimeout(5, TimeUnit.MINUTES).
                writeTimeout(5, TimeUnit.MINUTES).
                retryOnConnectionFailure(true).addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request.Builder builder = original.newBuilder();

                        //Request request = chain.request().newBuilder().addHeader("parameter", "value").build();
                        builder.header("Content-Type", "application/x-www-form-urlencoded");

                        Request request = builder.method(original.method(), original.body())
                                .build();

                        return chain.proceed(request);

                    }
                });

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainIAPI.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        //creating the retrofit api service
        MainIAPI apiService = retrofit.create(MainIAPI.class);


        RequestBody nationalbankidentification = createPartFromString(nationalBankIdenticationNumber);
        RequestBody mobileNumber = createPartFromString(mobile_no);
        RequestBody longitude1 = createPartFromString(longitude);
        RequestBody latitude1 = createPartFromString(latitude);
        RequestBody adhaarnumber = createPartFromString(aadhar_no);
        RequestBody rtid=createPartFromString(cus_id);
        RequestBody fingerprintdata=createPartFromString(result);
        RequestBody callfunctn=createPartFromString(transactionType);
        RequestBody transactionAmount=createPartFromString(sendAmount);



        //Call<ScannerResponse> call = apiService.saveScan(orderId1,vpa1,name1,amount1,mon_no1,member_id1,password1);
        Call<BaseAUthRegistrationResponse> call = apiService.checkTwoStepReg(nationalbankidentification,mobileNumber,longitude1,latitude1,
                adhaarnumber,rtid,fingerprintdata,callfunctn,transactionAmount);


        //making the call to generate checksum
        call.enqueue(new Callback<BaseAUthRegistrationResponse>() {
            @Override
            public void onResponse(Call<BaseAUthRegistrationResponse> call, Response<BaseAUthRegistrationResponse> response) {
                progress_bar.setVisibility(View.GONE);

                if (response.body().getStatus()==true)
                {

                    progress_bar.setVisibility(View.GONE);
                    //   Toast.makeText(this, "Result : "+status , Toast.LENGTH_SHORT).show();

                    if (transactionType.equals("aepstwofactorregistration"))
                    {
                        try{
                            Toast.makeText(MantraDeviceActivity.this, response.body().getAthRegistrationResponse().getMessage(), Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(MantraDeviceActivity.this, AepsTwoFactorAuthenticationActivity.class);
                            intent.putExtra("from","aepstwofactorauthentication");
                            startActivity(intent);
                        }catch (Exception e){
                            Toast.makeText(MantraDeviceActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }else if (transactionType.equals("aepstwofactorauthentication")){
                        Toast.makeText(MantraDeviceActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(MantraDeviceActivity.this, NewMainActivity.class);
                        startActivity(intent);

                    }else {

                        Toast.makeText(MantraDeviceActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                       /* if (transactionType.equalsIgnoreCase("ministatement")) {

                            //          Toast.makeText(this, "" + result, Toast.LENGTH_SHORT).show();
                            ArrayList<MiniStatementModel> miniStatementModelArrayList = new ArrayList<>();

                            JSONArray cast = jsonObject.getJSONArray("ministatement");
                            Toast.makeText(this, "cast : "+cast , Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < cast.length(); i++) {
                                JSONObject notifyObjJson = cast.getJSONObject(i);
                                String date = notifyObjJson.getString("date");

                                Log.e("date", date);
                                MiniStatementModel offersModel = new Gson()
                                        .fromJson(notifyObjJson.toString(), MiniStatementModel.class);
                                miniStatementModelArrayList.add(offersModel);

                            }

                            Bundle newBundle = new Bundle();
                            newBundle.putParcelableArrayList("miniStatementModelArrayList",
                                    miniStatementModelArrayList);
                            Intent intent = new Intent(this, MinistatementActivity.class);
                            intent.putExtras(newBundle);
                            startActivity(intent);

                        } else {


                            JSONObject aepsResultObj = jsonObject.getJSONObject("result");
                            //  JSONObject aepsData = aepsResultObj.getJSONObject("data");
                            String terminalId = aepsResultObj.getString("terminalId");
                            String requestTransactionTime = aepsResultObj.getString("requestTransactionTime");
                            String transactionAmount = aepsResultObj.getString("transactionAmount");
                            String transactionStatus = aepsResultObj.getString("transactionStatus");
                            String balanceAmount = aepsResultObj.getString("balanceAmount");
                            String bankRRN = aepsResultObj.getString("bankRRN");
                            String transactionType = aepsResultObj.getString("transactionType");
                            String fpTransactionId = aepsResultObj.getString("fpTransactionId");
                            String merchantTransactionId = aepsResultObj.getString("merchantTransactionId");
                            String outletname = jsonObject.getString("outletname");
                            String outletmobile = jsonObject.getString("outletmobile");
                            String url = jsonObject.getString("url");

                            //   Toast.makeText(this, "transactionType : "+transactionType , Toast.LENGTH_SHORT).show();

                            // String retailerid = jsonObject.getString("cus_id");
                            if (transactionType.equalsIgnoreCase("BE")) {

                                Bundle beBunndle = new Bundle();
                                beBunndle.putString("aepsmessage", aepsmessage);
                                beBunndle.putString("terminalId", terminalId);
                                beBunndle.putString("requestTransactionTime", requestTransactionTime);
                                beBunndle.putString("transactionAmount", transactionAmount);
                                beBunndle.putString("transactionStatus", transactionStatus);
                                beBunndle.putString("balanceAmount", balanceAmount);
                                beBunndle.putString("bankRRN", bankRRN);
                                beBunndle.putString("transactionType", transactionType);
                                beBunndle.putString("fpTransactionId", fpTransactionId);
                                beBunndle.putString("merchantTransactionId", merchantTransactionId);
                                beBunndle.putString("outletname", outletname);
                                beBunndle.putString("outletmobile", outletmobile);
                                beBunndle.putString("url", url);
                                Intent intent = new Intent(this, BalanceCheckResponseActivity.class);
                                intent.putExtras(beBunndle);
                                startActivity(intent);
                                finish();

                            } else if (transactionType.equalsIgnoreCase("CW") ||
                                    transactionType.equalsIgnoreCase("M")) {
                                Bundle cwBunndle = new Bundle();
                                cwBunndle.putString("aepsmessage", aepsmessage);
                                cwBunndle.putString("terminalId", terminalId);
                                cwBunndle.putString("requestTransactionTime", requestTransactionTime);
                                cwBunndle.putString("transactionAmount", transactionAmount);
                                cwBunndle.putString("transactionStatus", transactionStatus);
                                cwBunndle.putString("balanceAmount", balanceAmount);
                                cwBunndle.putString("bankRRN", bankRRN);
                                cwBunndle.putString("transactionType", transactionType);
                                cwBunndle.putString("fpTransactionId", fpTransactionId);
                                cwBunndle.putString("merchantTransactionId", merchantTransactionId);
                                cwBunndle.putString("outletname", outletname);
                                cwBunndle.putString("outletmobile", outletmobile);
                                cwBunndle.putString("url", url);
                                cwBunndle.putString("aadhar_no", aadhar_no);
                                cwBunndle.putString("bankName", bankName);
                                // cwBunndle.putString("retailerId", retailerid);
                                Intent intent = new Intent(this, CashWithdrawalSuccessActivity.class);
                                intent.putExtras(cwBunndle);
                                startActivity(intent);
                                finish();

                            } else if (transactionType.equalsIgnoreCase("cashdeposit")) {
                                Bundle cwBunndle = new Bundle();
                                cwBunndle.putString("aepsmessage", aepsmessage);
                                cwBunndle.putString("terminalId", terminalId);
                                cwBunndle.putString("requestTransactionTime", requestTransactionTime);
                                cwBunndle.putString("transactionAmount", transactionAmount);
                                cwBunndle.putString("transactionStatus", transactionStatus);
                                cwBunndle.putString("balanceAmount", balanceAmount);
                                cwBunndle.putString("bankRRN", bankRRN);
                                cwBunndle.putString("transactionType", transactionType);
                                cwBunndle.putString("fpTransactionId", fpTransactionId);
                                cwBunndle.putString("merchantTransactionId", merchantTransactionId);
                                cwBunndle.putString("outletname", outletname);
                                cwBunndle.putString("outletmobile", outletmobile);
                                cwBunndle.putString("url", url);
                                cwBunndle.putString("aadhar_no", aadhar_no);
                                cwBunndle.putString("bankName", bankName);
                                //  cwBunndle.putString("retailerId", retailerid);
                                Intent intent = new Intent(this, CashWithdrawalSuccessActivity.class);
                                intent.putExtras(cwBunndle);
                                startActivity(intent);
                                finish();

                            }
                        }*/
                    }



                }else {
                    Toast.makeText(MantraDeviceActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(MantraDeviceActivity.this, NewMainActivity.class);
                    //  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }


                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter

            }

            @Override
            public void onFailure(Call<BaseAUthRegistrationResponse> call, Throwable t) {
                progress_bar.setVisibility(View.GONE);
                // callServiceFalse(mobileNo);
                Toast.makeText(MantraDeviceActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

}