package com.nucleosystechnologies.ofconline.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.nucleosystechnologies.ofconline.R;
import com.nucleosystechnologies.ofconline.Utility.AppSharedPreferences;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

public class PaymentActivity extends AppCompatActivity {
    AppSharedPreferences appSharedPreferences;
    Stripe stripe;
    Card card;
    ProgressDialog pDialog;
    WebView webView;
    String priceget,mast_id,pack;
    private WebSettings webSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Proceed Payment");
        appSharedPreferences = new AppSharedPreferences(this);
        final CardForm cardForm = (CardForm) findViewById(R.id.card_form);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .actionLabel("Purchase")
                .setup(this);
        pDialog =  new ProgressDialog(this);
        TextView price = (TextView)findViewById(R.id.price);

        int pricemake = Integer.parseInt(getIntent().getExtras().getString("price"));
        pricemake = pricemake/100;
        pricemake = pricemake;
        price.setText("Proceed payment of \u20B9 "+pricemake+".00");

        priceget  = getIntent().getExtras().getString("price");
        mast_id= appSharedPreferences.pref.getString(appSharedPreferences.mast_id,"");
        pack = "bussiness";
        Button sub =  (Button)findViewById(R.id.sub);

        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //Card card = new Card(cardForm.getCardNumber(), 12, 2019, cardForm.getCvv());

                if(cardForm.getCardNumber().isEmpty()||cardForm.getCardNumber()=="Null")
                {
                    Toast.makeText(PaymentActivity.this, "Please Enter all card Detail"+cardForm.getCardNumber(), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    pDialog.show();
                    pDialog.setMessage("Loading");
                    pDialog.setCancelable(false);
                    int month= Integer.parseInt(cardForm.getExpirationMonth());
                    int Year= Integer.parseInt(cardForm.getExpirationYear());
                card = new Card(cardForm.getCardNumber(), month, Year, cardForm.getCvv());

                card.setCurrency("usd");
                card.setName(appSharedPreferences.pref.getString(appSharedPreferences.FirstName,"")+" "+appSharedPreferences.pref.getString(appSharedPreferences.LastName,""));


                stripe = new Stripe(getApplicationContext(),"pk_test_AARyUyZkzPCCc060fxvm2Ond");
                stripe.createToken(card, "pk_test_AARyUyZkzPCCc060fxvm2Ond", new TokenCallback() {
                    public void onSuccess(Token token) {
                        // TODO: Send Token information to your backend to initiate a charge
                       // Toast.makeText(getApplicationContext(), "Token created: " + token.getId(), Toast.LENGTH_LONG).show();
                        OtpAlert(token.getId());
                        pDialog.dismiss();
                    }

                    public void onError(Exception error) {
                        Log.d("Stripe", error.getLocalizedMessage());
                    }
                });
                }
            }
        });

    }

    public void OtpAlert(String token)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);


        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.pack_activation_alert, null);
        builder.setCancelable(false);
        builder.setView(promptsView);
        final AlertDialog dialog = builder.create();
        webView = (WebView)promptsView.findViewById(R.id.payment) ;
        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new Client());
        webView.setVisibility(View.GONE);
        webView.addJavascriptInterface(new JavaScriptInterface(), "interface");
        String data = "<body onload=\"document.forms['payment'].submit()\">\n" +
                "<form action=\"http://ofconline.in/Payment/adnroid_pay\"  name=\"payment\" method=\"post\" id=\"payment-form\">\n" +
                "\t<input type=\"hidden\" name=\"stripeToken\" value="+token+" />\n" +
                "\t<input type=\"hidden\" name=\"price\" value="+priceget+" />\n" +
                "\t<input type=\"hidden\" name=\"mast_id\" value="+mast_id+" />\n" +
                "\t<input type=\"hidden\" name=\"pack_person\" value="+pack+" />\n" +
                "</form>\n" +
                "</body>";



        webView.loadDataWithBaseURL("",data,"text/html","UTF-8","");
        dialog.show();
    }

    private class JavaScriptInterface {

        @JavascriptInterface
        public void callFromJS() {
             Intent i = new Intent(PaymentActivity.this,PaymentSuccsess.class);
             startActivity(i);
             finish();
        }


    }
    public class Client extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // If url contains mailto link then open Mail Intent
            if (url.contains("mailto:")) {
                // Could be cleverer and use a regex
                //Open links in new browser
                view.getContext().startActivity(
                        new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                // Here we can open new activity
                return true;
            }else {
                // Stay within this webview and load url
                view.loadUrl(url);
                return true;
            }
        }
        //Show loader on url load
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // Then show progress  Dialog
            // in standard case YourActivity.this

        }
        // Called when all page resources loaded
        public void onPageFinished(WebView view, String url) {
            try {
                // Close progressDialog

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
